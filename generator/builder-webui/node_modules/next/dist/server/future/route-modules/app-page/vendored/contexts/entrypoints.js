"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    HeadManagerContext: null,
    ServerInsertedHtml: null,
    AppRouterContext: null,
    HooksClientContext: null,
    RouterContext: null,
    HtmlContext: null,
    AmpContext: null,
    LoadableContext: null,
    ImageConfigContext: null,
    Loadable: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    HeadManagerContext: function() {
        return _headmanagercontextsharedruntime;
    },
    ServerInsertedHtml: function() {
        return _serverinsertedhtmlsharedruntime;
    },
    AppRouterContext: function() {
        return _approutercontextsharedruntime;
    },
    HooksClientContext: function() {
        return _hooksclientcontextsharedruntime;
    },
    RouterContext: function() {
        return _routercontextsharedruntime;
    },
    HtmlContext: function() {
        return _htmlcontextsharedruntime;
    },
    AmpContext: function() {
        return _ampcontextsharedruntime;
    },
    LoadableContext: function() {
        return _loadablecontextsharedruntime;
    },
    ImageConfigContext: function() {
        return _imageconfigcontextsharedruntime;
    },
    Loadable: function() {
        return _loadablesharedruntime;
    }
});
const _headmanagercontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/head-manager-context.shared-runtime"));
const _serverinsertedhtmlsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/server-inserted-html.shared-runtime"));
const _approutercontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/app-router-context.shared-runtime"));
const _hooksclientcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/hooks-client-context.shared-runtime"));
const _routercontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/router-context.shared-runtime"));
const _htmlcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/html-context.shared-runtime"));
const _ampcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/amp-context.shared-runtime"));
const _loadablecontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/loadable-context.shared-runtime"));
const _imageconfigcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/image-config-context.shared-runtime"));
const _loadablesharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/loadable.shared-runtime"));
function _getRequireWildcardCache(nodeInterop) {
    if (typeof WeakMap !== "function") return null;
    var cacheBabelInterop = new WeakMap();
    var cacheNodeInterop = new WeakMap();
    return (_getRequireWildcardCache = function(nodeInterop) {
        return nodeInterop ? cacheNodeInterop : cacheBabelInterop;
    })(nodeInterop);
}
function _interop_require_wildcard(obj, nodeInterop) {
    if (!nodeInterop && obj && obj.__esModule) {
        return obj;
    }
    if (obj === null || typeof obj !== "object" && typeof obj !== "function") {
        return {
            default: obj
        };
    }
    var cache = _getRequireWildcardCache(nodeInterop);
    if (cache && cache.has(obj)) {
        return cache.get(obj);
    }
    var newObj = {};
    var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor;
    for(var key in obj){
        if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) {
            var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null;
            if (desc && (desc.get || desc.set)) {
                Object.defineProperty(newObj, key, desc);
            } else {
                newObj[key] = obj[key];
            }
        }
    }
    newObj.default = obj;
    if (cache) {
        cache.set(obj, newObj);
    }
    return newObj;
}

//# sourceMappingURL=entrypoints.js.map