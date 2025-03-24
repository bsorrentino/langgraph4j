"use client";

"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    bailOnNotFound: null,
    DevRootNotFoundBoundary: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    bailOnNotFound: function() {
        return bailOnNotFound;
    },
    DevRootNotFoundBoundary: function() {
        return DevRootNotFoundBoundary;
    }
});
const _interop_require_default = require("@swc/helpers/_/_interop_require_default");
const _react = /*#__PURE__*/ _interop_require_default._(require("react"));
const _notfoundboundary = require("./not-found-boundary");
function bailOnNotFound() {
    throw new Error("notFound() is not allowed to use in root layout");
}
function NotAllowedRootNotFoundError() {
    bailOnNotFound();
    return null;
}
function DevRootNotFoundBoundary(param) {
    let { children } = param;
    return /*#__PURE__*/ _react.default.createElement(_notfoundboundary.NotFoundBoundary, {
        notFound: /*#__PURE__*/ _react.default.createElement(NotAllowedRootNotFoundError, null)
    }, children);
}

if ((typeof exports.default === 'function' || (typeof exports.default === 'object' && exports.default !== null)) && typeof exports.default.__esModule === 'undefined') {
  Object.defineProperty(exports.default, '__esModule', { value: true });
  Object.assign(exports.default, exports);
  module.exports = exports.default;
}

//# sourceMappingURL=dev-root-not-found-boundary.js.map