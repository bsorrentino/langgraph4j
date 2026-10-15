package org.bsc.langgraph4j.annotation;

import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.action.AsyncCommandAction;
import org.bsc.langgraph4j.action.AsyncNodeActionWithConfig;
import org.bsc.langgraph4j.action.Command;
import org.bsc.langgraph4j.action.NodeActionWithConfig;
import org.bsc.langgraph4j.edge.Edge;
import org.bsc.langgraph4j.edge.EdgeCondition;
import org.bsc.langgraph4j.edge.EdgeValue;
import org.bsc.langgraph4j.node.Node;
import org.bsc.langgraph4j.state.AgentState;

import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.utils.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;

public interface ActionCatalogRepository {

    Map<String, Node.ActionFactory<? extends AgentState>> nodeActions();
    Map<String, AsyncCommandAction<? extends AgentState>> edgeActions();

    default <State extends AgentState> Edge<State> edge(String sourceId, String actionId, Map<String, String> mappings) {
        @SuppressWarnings("unchecked")
        final var condition = (AsyncCommandAction<State>) edgeActions().get(actionId);
        if (condition == null) {
            throw new IllegalArgumentException("edge action '%s' not found: ".formatted(actionId));
        }
        return new Edge<>(sourceId, new EdgeValue<>( new EdgeCondition<>( condition, mappings)) );
    }

    default <State extends AgentState> Node.ActionFactory<State> nodeAction(String actionId) {
        @SuppressWarnings("unchecked")
        final var action = (Node.ActionFactory<State>)nodeActions().get(actionId);
        if (action == null) {
            throw new IllegalArgumentException("node action '%s' not found: ".formatted(actionId));
        }

        return action;
    }

    default <State extends AgentState> Node<State> node(String id)  {
        @SuppressWarnings("unchecked")
        final var action = (Node.ActionFactory<State>)nodeActions().get(id);
        if (action == null) {
            throw new IllegalArgumentException("node action '%s' not found: ".formatted(id));
        }
        return new Node<>(id, action);
    }

    static <Catalog extends ActionCatalog> ActionCatalogRepository of( Catalog catalog) {

        record CatalogRepositoryImpl(
                Map<String, Node.ActionFactory<? extends AgentState>> nodeActions,
                Map<String, AsyncCommandAction<? extends AgentState>> edgeActions ) implements ActionCatalogRepository {
            @Override
            public Map<String, Node.ActionFactory<? extends AgentState>> nodeActions() {
                return nodeActions;
            }

            @Override
            public Map<String, AsyncCommandAction<? extends AgentState>> edgeActions() {
                return edgeActions;
            }
        }

        final var nodeActions = new HashMap<String, Node.ActionFactory<? extends AgentState>>();
        final var edgeActions = new HashMap<String, AsyncCommandAction<? extends AgentState>>();


        for (var method : catalog.getClass().getMethods()) {
            var annotation = method.getAnnotation(NodeAction.class);
            if (annotation != null) {

                final var name = annotation.name().isBlank() ? method.getName() : annotation.name();
                if (nodeActions.containsKey(name)) {
                    throw new IllegalArgumentException("duplicated node action name: %s".formatted(name));
                }

                nodeActions.put(name, toNodeAction(catalog, method));
                continue;
            }

            final var edgeAnnotation = method.getAnnotation(EdgeAction.class);
            if (edgeAnnotation != null) {
                if (edgeActions.containsKey(edgeAnnotation.name())) {
                    throw new IllegalArgumentException("duplicated edge action name: %s".formatted(edgeAnnotation.name()));
                }
                final var name = edgeAnnotation.name().isBlank() ? method.getName() : edgeAnnotation.name();
                edgeActions.put(name, toEdgeAction(catalog, method));
            }

        }

        return new CatalogRepositoryImpl(nodeActions, edgeActions);
    }

    @SuppressWarnings("unchecked")
    private static <State extends AgentState> Node.ActionFactory<State> toNodeAction(Object catalog, Method method) {
        method.setAccessible(true);

        final var paramTypes = method.getParameterTypes();

        final boolean hasCompileConfig = ( paramTypes.length == 1 && CompileConfig.class.isAssignableFrom(paramTypes[0]) );

        if( hasCompileConfig) {

            return config -> {

                try {
                    final var result = method.invoke(catalog, config);
                    if( result instanceof AsyncNodeActionWithConfig<?> action) {
                        return (AsyncNodeActionWithConfig<State>) action;
                    }
                    if( result instanceof NodeActionWithConfig<?> action) {
                        return AsyncNodeActionWithConfig.node_async((NodeActionWithConfig<State>)action);
                    }
                    throw new IllegalStateException(
                            "unsupported return type %s on method %s: only Node.ActionFactory is allowed"
                            .formatted(result.getClass().getName(), method));
                } catch (Throwable e) {
                    throw new RuntimeException(ExceptionUtils.getRootCause(e));
                }
            };

        }


        for (Class<?> type : paramTypes) {
            if (!(AgentState.class.isAssignableFrom(type) || RunnableConfig.class.isAssignableFrom(type))) {
                throw new IllegalArgumentException(
                        "unsupported parameter type %s on method %s: only AgentState and RunnableConfig are allowed"
                                .formatted(type.getName(), method));
            }
        }

        final Function<Class<?>,CompletableFuture<Map<String, Object>>> unsupportedReturn = cls ->
                failedFuture(new IllegalStateException(
                        "unsupported return type %s on method %s"
                                .formatted(cls.getName(), method)));

        final AsyncNodeActionWithConfig<State> action =  (state, config) -> {
            try {
                final var args = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    if (AgentState.class.isAssignableFrom(paramTypes[i])) {
                        args[i] = state;
                    } else if (RunnableConfig.class.isAssignableFrom(paramTypes[i])) {
                        args[i] = config;
                    }
                }
                final var result = method.invoke(catalog, args);

                if (result == null) {
                    return completedFuture(Map.<String, Object>of());
                }

                if (result instanceof Map<?, ?> map) {
                    return completedFuture((Map<String, Object>) map);
                }

                if (result instanceof CompletableFuture<?> future) {
                    return future.thenCompose(res -> {
                        if( res == null ) {
                            return completedFuture(Map.<String, Object>of());
                        }
                        if( res instanceof Map<?, ?> map) {
                            return completedFuture((Map<String, Object>) map);
                        }
                        return unsupportedReturn.apply(res.getClass());
                    });
                }

                return unsupportedReturn.apply(result.getClass());

            } catch (Throwable e) {
                return failedFuture(ExceptionUtils.getRootCause(e));
            }
        };

        return config -> action;
    }

    private static <State extends AgentState> AsyncCommandAction<State> toEdgeAction(Object catalog, Method method) {
        method.setAccessible(true);

        final var paramTypes = method.getParameterTypes();
        for (Class<?> type : paramTypes) {
            if (!(AgentState.class.isAssignableFrom(type) || RunnableConfig.class.isAssignableFrom(type))) {
                throw new IllegalArgumentException(
                        "unsupported parameter type %s on method %s: only AgentState and RunnableConfig are allowed"
                                .formatted(type.getName(), method));
            }
        }

        final Function<Class<?>,CompletableFuture<Command>> unsupportedReturn = cls ->
                failedFuture(new IllegalStateException(
                        "unsupported return type %s on method %s"
                                .formatted(cls.getName(), method)));

        return (state, config) -> {
            try {
                final var args = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    if (AgentState.class.isAssignableFrom(paramTypes[i])) {
                        args[i] = state;
                    } else if (RunnableConfig.class.isAssignableFrom(paramTypes[i])) {
                        args[i] = config;
                    }
                }
                final var result = method.invoke(catalog, args);

                if (result == null) {
                    return completedFuture(new Command(Map.of()));
                }
                if (result instanceof Command command) {
                    return completedFuture(command);
                }
                if( result instanceof String gotoNode) {
                    return completedFuture(new Command(gotoNode));
                }
                if (result instanceof CompletableFuture<?> future) {
                    return future.thenCompose(res -> {
                        if (res == null) {
                            return completedFuture(new Command(Map.of()));
                        }
                        if (res instanceof Command command) {
                            return completedFuture(command);
                        }
                        return unsupportedReturn.apply(res.getClass());
                    });
                }

                return unsupportedReturn.apply(result.getClass());

            } catch (Throwable e) {
                return failedFuture(ExceptionUtils.getRootCause(e));
            }
        };
    }
}
