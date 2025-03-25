"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    RouterContext: null,
    LoadableContext: null,
    Loadable: null,
    ImageConfigContext: null,
    HtmlContext: null,
    HooksClientContext: null,
    HeadManagerContext: null,
    AppRouterContext: null,
    AmpContext: null,
    ServerInsertedHtml: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    RouterContext: function() {
        return _routercontextsharedruntime;
    },
    LoadableContext: function() {
        return _loadablecontextsharedruntime;
    },
    Loadable: function() {
        return _loadablesharedruntime;
    },
    ImageConfigContext: function() {
        return _imageconfigcontextsharedruntime;
    },
    HtmlContext: function() {
        return _htmlcontextsharedruntime;
    },
    HooksClientContext: function() {
        return _hooksclientcontextsharedruntime;
    },
    HeadManagerContext: function() {
        return _headmanagercontextsharedruntime;
    },
    AppRouterContext: function() {
        return _approutercontextsharedruntime;
    },
    AmpContext: function() {
        return _ampcontextsharedruntime;
    },
    ServerInsertedHtml: function() {
        return _serverinsertedhtmlsharedruntime;
    }
});
const _routercontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/router-context.shared-runtime"));
const _loadablecontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/loadable-context.shared-runtime"));
const _loadablesharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/loadable.shared-runtime"));
const _imageconfigcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/image-config-context.shared-runtime"));
const _htmlcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/html-context.shared-runtime"));
const _hooksclientcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/hooks-client-context.shared-runtime"));
const _headmanagercontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/head-manager-context.shared-runtime"));
const _approutercontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/app-router-context.shared-runtime"));
const _ampcontextsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/amp-context.shared-runtime"));
const _serverinsertedhtmlsharedruntime = /*#__PURE__*/ _interop_require_wildcard(require("../../../../../../shared/lib/server-inserted-html.shared-runtime"));
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