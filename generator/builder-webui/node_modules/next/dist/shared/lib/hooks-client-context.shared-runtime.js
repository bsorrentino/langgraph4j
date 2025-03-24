"use client";

"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    SearchParamsContext: null,
    PathnameContext: null,
    PathParamsContext: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    SearchParamsContext: function() {
        return SearchParamsContext;
    },
    PathnameContext: function() {
        return PathnameContext;
    },
    PathParamsContext: function() {
        return PathParamsContext;
    }
});
const _react = require("react");
const SearchParamsContext = (0, _react.createContext)(null);
const PathnameContext = (0, _react.createContext)(null);
const PathParamsContext = (0, _react.createContext)(null);
if (process.env.NODE_ENV !== "production") {
    SearchParamsContext.displayName = "SearchParamsContext";
    PathnameContext.displayName = "PathnameContext";
    PathParamsContext.displayName = "PathParamsContext";
}

//# sourceMappingURL=hooks-client-context.shared-runtime.js.map