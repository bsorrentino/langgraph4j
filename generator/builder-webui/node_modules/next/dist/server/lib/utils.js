"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    printAndExit: null,
    getDebugPort: null,
    getNodeOptionsWithoutInspect: null,
    getPort: null,
    RESTART_EXIT_CODE: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    printAndExit: function() {
        return printAndExit;
    },
    getDebugPort: function() {
        return getDebugPort;
    },
    getNodeOptionsWithoutInspect: function() {
        return getNodeOptionsWithoutInspect;
    },
    getPort: function() {
        return getPort;
    },
    RESTART_EXIT_CODE: function() {
        return RESTART_EXIT_CODE;
    }
});
function printAndExit(message, code = 1) {
    if (code === 0) {
        console.log(message);
    } else {
        console.error(message);
    }
    process.exit(code);
}
const getDebugPort = ()=>{
    var _process_execArgv_find, _process_env_NODE_OPTIONS_match, _process_env_NODE_OPTIONS_match1, _process_env_NODE_OPTIONS;
    const debugPortStr = ((_process_execArgv_find = process.execArgv.find((localArg)=>localArg.startsWith("--inspect") || localArg.startsWith("--inspect-brk"))) == null ? void 0 : _process_execArgv_find.split("=")[1]) ?? ((_process_env_NODE_OPTIONS = process.env.NODE_OPTIONS) == null ? void 0 : (_process_env_NODE_OPTIONS_match1 = _process_env_NODE_OPTIONS.match) == null ? void 0 : (_process_env_NODE_OPTIONS_match = _process_env_NODE_OPTIONS_match1.call(_process_env_NODE_OPTIONS, /--inspect(-brk)?(=(\S+))?( |$)/)) == null ? void 0 : _process_env_NODE_OPTIONS_match[3]);
    return debugPortStr ? parseInt(debugPortStr, 10) : 9229;
};
const NODE_INSPECT_RE = /--inspect(-brk)?(=\S+)?( |$)/;
function getNodeOptionsWithoutInspect() {
    return (process.env.NODE_OPTIONS || "").replace(NODE_INSPECT_RE, "");
}
function getPort(args) {
    if (typeof args["--port"] === "number") {
        return args["--port"];
    }
    const parsed = process.env.PORT && parseInt(process.env.PORT, 10);
    if (typeof parsed === "number" && !Number.isNaN(parsed)) {
        return parsed;
    }
    return 3000;
}
const RESTART_EXIT_CODE = 77;

//# sourceMappingURL=utils.js.map