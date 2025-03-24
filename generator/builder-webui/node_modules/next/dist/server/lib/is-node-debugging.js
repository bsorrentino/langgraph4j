"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "checkIsNodeDebugging", {
    enumerable: true,
    get: function() {
        return checkIsNodeDebugging;
    }
});
function checkIsNodeDebugging() {
    var _process_env_NODE_OPTIONS_match, _process_env_NODE_OPTIONS, _process_env_NODE_OPTIONS_match1, _process_env_NODE_OPTIONS1;
    let isNodeDebugging = !!(process.execArgv.some((localArg)=>localArg.startsWith("--inspect")) || ((_process_env_NODE_OPTIONS = process.env.NODE_OPTIONS) == null ? void 0 : (_process_env_NODE_OPTIONS_match = _process_env_NODE_OPTIONS.match) == null ? void 0 : _process_env_NODE_OPTIONS_match.call(_process_env_NODE_OPTIONS, /--inspect(=\S+)?( |$)/)));
    if (process.execArgv.some((localArg)=>localArg.startsWith("--inspect-brk")) || ((_process_env_NODE_OPTIONS1 = process.env.NODE_OPTIONS) == null ? void 0 : (_process_env_NODE_OPTIONS_match1 = _process_env_NODE_OPTIONS1.match) == null ? void 0 : _process_env_NODE_OPTIONS_match1.call(_process_env_NODE_OPTIONS1, /--inspect-brk(=\S+)?( |$)/))) {
        isNodeDebugging = "brk";
    }
    return isNodeDebugging;
}

//# sourceMappingURL=is-node-debugging.js.map