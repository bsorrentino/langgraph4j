"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "createComponentTree", {
    enumerable: true,
    get: function() {
        return createComponentTree;
    }
});
const _react = /*#__PURE__*/ _interop_require_default(require("react"));
const _clientreference = require("../../lib/client-reference");
const _appdirmodule = require("../lib/app-dir-module");
const _interopdefault = require("./interop-default");
const _preloadcomponent = require("./preload-component");
const _createflightrouterstatefromloadertree = require("./create-flight-router-state-from-loader-tree");
const _parseloadertree = require("./parse-loader-tree");
const _createcomponentandstyles = require("./create-component-and-styles");
const _getlayerassets = require("./get-layer-assets");
const _hasloadingcomponentintree = require("./has-loading-component-in-tree");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
async function createComponentTree({ createSegmentPath, loaderTree: tree, parentParams, firstItem, rootLayoutIncluded, injectedCSS, injectedFontPreloadTags, asNotFound, metadataOutlet, ctx }) {
    const { renderOpts: { nextConfigOutput }, staticGenerationStore, componentMod: { staticGenerationBailout, NotFoundBoundary, LayoutRouter, RenderFromTemplateContext, StaticGenerationSearchParamsBailoutProvider, serverHooks: { DynamicServerError } }, pagePath, getDynamicParamFromSegment, query, isPrefetch, searchParamsProps } = ctx;
    const { page, layoutOrPagePath, segment, components, parallelRoutes } = (0, _parseloadertree.parseLoaderTree)(tree);
    const { layout, template, error, loading, "not-found": notFound } = components;
    const injectedCSSWithCurrentLayout = new Set(injectedCSS);
    const injectedFontPreloadTagsWithCurrentLayout = new Set(injectedFontPreloadTags);
    const styles = (0, _getlayerassets.getLayerAssets)({
        ctx,
        layoutOrPagePath,
        injectedCSS: injectedCSSWithCurrentLayout,
        injectedFontPreloadTags: injectedFontPreloadTagsWithCurrentLayout
    });
    const [Template, templateStyles] = template ? await (0, _createcomponentandstyles.createComponentAndStyles)({
        ctx,
        filePath: template[1],
        getComponent: template[0],
        injectedCSS: injectedCSSWithCurrentLayout
    }) : [
        _react.default.Fragment
    ];
    const [ErrorComponent, errorStyles] = error ? await (0, _createcomponentandstyles.createComponentAndStyles)({
        ctx,
        filePath: error[1],
        getComponent: error[0],
        injectedCSS: injectedCSSWithCurrentLayout
    }) : [];
    const [Loading, loadingStyles] = loading ? await (0, _createcomponentandstyles.createComponentAndStyles)({
        ctx,
        filePath: loading[1],
        getComponent: loading[0],
        injectedCSS: injectedCSSWithCurrentLayout
    }) : [];
    const isLayout = typeof layout !== "undefined";
    const isPage = typeof page !== "undefined";
    const [layoutOrPageMod] = await (0, _appdirmodule.getLayoutOrPageModule)(tree);
    /**
   * Checks if the current segment is a root layout.
   */ const rootLayoutAtThisLevel = isLayout && !rootLayoutIncluded;
    /**
   * Checks if the current segment or any level above it has a root layout.
   */ const rootLayoutIncludedAtThisLevelOrAbove = rootLayoutIncluded || rootLayoutAtThisLevel;
    const [NotFound, notFoundStyles] = notFound ? await (0, _createcomponentandstyles.createComponentAndStyles)({
        ctx,
        filePath: notFound[1],
        getComponent: notFound[0],
        injectedCSS: injectedCSSWithCurrentLayout
    }) : [];
    let dynamic = layoutOrPageMod == null ? void 0 : layoutOrPageMod.dynamic;
    if (nextConfigOutput === "export") {
        if (!dynamic || dynamic === "auto") {
            dynamic = "error";
        } else if (dynamic === "force-dynamic") {
            staticGenerationStore.forceDynamic = true;
            staticGenerationStore.dynamicShouldError = true;
            staticGenerationBailout(`output: export`, {
                dynamic,
                link: "https://nextjs.org/docs/advanced-features/static-html-export"
            });
        }
    }
    if (typeof dynamic === "string") {
        // the nested most config wins so we only force-static
        // if it's configured above any parent that configured
        // otherwise
        if (dynamic === "error") {
            staticGenerationStore.dynamicShouldError = true;
        } else if (dynamic === "force-dynamic") {
            staticGenerationStore.forceDynamic = true;
            staticGenerationBailout(`force-dynamic`, {
                dynamic
            });
        } else {
            staticGenerationStore.dynamicShouldError = false;
            if (dynamic === "force-static") {
                staticGenerationStore.forceStatic = true;
            } else {
                staticGenerationStore.forceStatic = false;
            }
        }
    }
    if (typeof (layoutOrPageMod == null ? void 0 : layoutOrPageMod.fetchCache) === "string") {
        staticGenerationStore.fetchCache = layoutOrPageMod == null ? void 0 : layoutOrPageMod.fetchCache;
    }
    if (typeof (layoutOrPageMod == null ? void 0 : layoutOrPageMod.revalidate) === "number") {
        ctx.defaultRevalidate = layoutOrPageMod.revalidate;
        if (typeof staticGenerationStore.revalidate === "undefined" || typeof staticGenerationStore.revalidate === "number" && staticGenerationStore.revalidate > ctx.defaultRevalidate) {
            staticGenerationStore.revalidate = ctx.defaultRevalidate;
        }
        if (staticGenerationStore.isStaticGeneration && ctx.defaultRevalidate === 0) {
            const dynamicUsageDescription = `revalidate: 0 configured ${segment}`;
            staticGenerationStore.dynamicUsageDescription = dynamicUsageDescription;
            throw new DynamicServerError(dynamicUsageDescription);
        }
    }
    if (staticGenerationStore == null ? void 0 : staticGenerationStore.dynamicUsageErr) {
        throw staticGenerationStore.dynamicUsageErr;
    }
    const LayoutOrPage = layoutOrPageMod ? (0, _interopdefault.interopDefault)(layoutOrPageMod) : undefined;
    /**
   * The React Component to render.
   */ let Component = LayoutOrPage;
    const parallelKeys = Object.keys(parallelRoutes);
    const hasSlotKey = parallelKeys.length > 1;
    if (hasSlotKey && rootLayoutAtThisLevel) {
        Component = (componentProps)=>{
            const NotFoundComponent = NotFound;
            const RootLayoutComponent = LayoutOrPage;
            return /*#__PURE__*/ _react.default.createElement(NotFoundBoundary, {
                notFound: /*#__PURE__*/ _react.default.createElement(_react.default.Fragment, null, styles, /*#__PURE__*/ _react.default.createElement(RootLayoutComponent, null, notFoundStyles, /*#__PURE__*/ _react.default.createElement(NotFoundComponent, null)))
            }, /*#__PURE__*/ _react.default.createElement(RootLayoutComponent, componentProps));
        };
    }
    if (process.env.NODE_ENV === "development") {
        const { isValidElementType } = require("next/dist/compiled/react-is");
        if ((isPage || typeof Component !== "undefined") && !isValidElementType(Component)) {
            throw new Error(`The default export is not a React Component in page: "${pagePath}"`);
        }
        if (typeof ErrorComponent !== "undefined" && !isValidElementType(ErrorComponent)) {
            throw new Error(`The default export of error is not a React Component in page: ${segment}`);
        }
        if (typeof Loading !== "undefined" && !isValidElementType(Loading)) {
            throw new Error(`The default export of loading is not a React Component in ${segment}`);
        }
        if (typeof NotFound !== "undefined" && !isValidElementType(NotFound)) {
            throw new Error(`The default export of notFound is not a React Component in ${segment}`);
        }
    }
    // Handle dynamic segment params.
    const segmentParam = getDynamicParamFromSegment(segment);
    /**
   * Create object holding the parent params and current params
   */ const currentParams = // Handle null case where dynamic param is optional
    segmentParam && segmentParam.value !== null ? {
        ...parentParams,
        [segmentParam.param]: segmentParam.value
    } : parentParams;
    // Resolve the segment param
    const actualSegment = segmentParam ? segmentParam.treeSegment : segment;
    // This happens outside of rendering in order to eagerly kick off data fetching for layouts / the page further down
    const parallelRouteMap = await Promise.all(Object.keys(parallelRoutes).map(async (parallelRouteKey)=>{
        const isChildrenRouteKey = parallelRouteKey === "children";
        const currentSegmentPath = firstItem ? [
            parallelRouteKey
        ] : [
            actualSegment,
            parallelRouteKey
        ];
        const parallelRoute = parallelRoutes[parallelRouteKey];
        const childSegment = parallelRoute[0];
        const childSegmentParam = getDynamicParamFromSegment(childSegment);
        const notFoundComponent = NotFound && isChildrenRouteKey ? /*#__PURE__*/ _react.default.createElement(NotFound, null) : undefined;
        function getParallelRoutePair(currentChildProp, currentStyles) {
            // This is turned back into an object below.
            return [
                parallelRouteKey,
                /*#__PURE__*/ _react.default.createElement(LayoutRouter, {
                    parallelRouterKey: parallelRouteKey,
                    segmentPath: createSegmentPath(currentSegmentPath),
                    loading: Loading ? /*#__PURE__*/ _react.default.createElement(Loading, null) : undefined,
                    loadingStyles: loadingStyles,
                    // TODO-APP: Add test for loading returning `undefined`. This currently can't be tested as the `webdriver()` tab will wait for the full page to load before returning.
                    hasLoading: Boolean(Loading),
                    error: ErrorComponent,
                    errorStyles: errorStyles,
                    template: /*#__PURE__*/ _react.default.createElement(Template, null, /*#__PURE__*/ _react.default.createElement(RenderFromTemplateContext, null)),
                    templateStyles: templateStyles,
                    notFound: notFoundComponent,
                    notFoundStyles: notFoundStyles,
                    childProp: currentChildProp,
                    styles: currentStyles
                })
            ];
        }
        // if we're prefetching and that there's a Loading component, we bail out
        // otherwise we keep rendering for the prefetch.
        // We also want to bail out if there's no Loading component in the tree.
        let currentStyles = undefined;
        let childElement = null;
        const childPropSegment = (0, _createflightrouterstatefromloadertree.addSearchParamsIfPageSegment)(childSegmentParam ? childSegmentParam.treeSegment : childSegment, query);
        if (!(isPrefetch && (Loading || !(0, _hasloadingcomponentintree.hasLoadingComponentInTree)(parallelRoute)))) {
            // Create the child component
            const { Component: ChildComponent, styles: childComponentStyles } = await createComponentTree({
                createSegmentPath: (child)=>{
                    return createSegmentPath([
                        ...currentSegmentPath,
                        ...child
                    ]);
                },
                loaderTree: parallelRoute,
                parentParams: currentParams,
                rootLayoutIncluded: rootLayoutIncludedAtThisLevelOrAbove,
                injectedCSS: injectedCSSWithCurrentLayout,
                injectedFontPreloadTags: injectedFontPreloadTagsWithCurrentLayout,
                asNotFound,
                metadataOutlet,
                ctx
            });
            currentStyles = childComponentStyles;
            childElement = /*#__PURE__*/ _react.default.createElement(ChildComponent, null);
        }
        const childProp = {
            current: childElement,
            segment: childPropSegment
        };
        return getParallelRoutePair(childProp, currentStyles);
    }));
    // Convert the parallel route map into an object after all promises have been resolved.
    const parallelRouteComponents = parallelRouteMap.reduce((list, [parallelRouteKey, Comp])=>{
        list[parallelRouteKey] = Comp;
        return list;
    }, {});
    // When the segment does not have a layout or page we still have to add the layout router to ensure the path holds the loading component
    if (!Component) {
        return {
            Component: ()=>/*#__PURE__*/ _react.default.createElement(_react.default.Fragment, null, parallelRouteComponents.children),
            styles
        };
    }
    const isClientComponent = (0, _clientreference.isClientReference)(layoutOrPageMod);
    // If it's a not found route, and we don't have any matched parallel
    // routes, we try to render the not found component if it exists.
    let notFoundComponent = {};
    if (NotFound && asNotFound && // In development, it could hit the parallel-route-default not found, so we only need to check the segment.
    // Or if there's no parallel routes means it reaches the end.
    !parallelRouteMap.length) {
        notFoundComponent = {
            children: /*#__PURE__*/ _react.default.createElement(_react.default.Fragment, null, /*#__PURE__*/ _react.default.createElement("meta", {
                name: "robots",
                content: "noindex"
            }), process.env.NODE_ENV === "development" && /*#__PURE__*/ _react.default.createElement("meta", {
                name: "next-error",
                content: "not-found"
            }), notFoundStyles, /*#__PURE__*/ _react.default.createElement(NotFound, null))
        };
    }
    const props = {
        ...parallelRouteComponents,
        ...notFoundComponent,
        // TODO-APP: params and query have to be blocked parallel route names. Might have to add a reserved name list.
        // Params are always the current params that apply to the layout
        // If you have a `/dashboard/[team]/layout.js` it will provide `team` as a param but not anything further down.
        params: currentParams,
        // Query is only provided to page
        ...(()=>{
            if (isClientComponent && staticGenerationStore.isStaticGeneration) {
                return {};
            }
            if (isPage) {
                return searchParamsProps;
            }
        })()
    };
    // Eagerly execute layout/page component to trigger fetches early.
    if (!isClientComponent) {
        Component = await Promise.resolve().then(()=>(0, _preloadcomponent.preloadComponent)(Component, props));
    }
    return {
        Component: ()=>{
            return /*#__PURE__*/ _react.default.createElement(_react.default.Fragment, null, isPage ? metadataOutlet : null, isPage && isClientComponent ? /*#__PURE__*/ _react.default.createElement(StaticGenerationSearchParamsBailoutProvider, {
                propsForComponent: props,
                Component: Component,
                isStaticGeneration: staticGenerationStore.isStaticGeneration
            }) : /*#__PURE__*/ _react.default.createElement(Component, props), null);
        },
        styles
    };
}

//# sourceMappingURL=create-component-tree.js.map