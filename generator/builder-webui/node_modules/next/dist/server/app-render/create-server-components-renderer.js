"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "createServerComponentRenderer", {
    enumerable: true,
    get: function() {
        return createServerComponentRenderer;
    }
});
const _react = /*#__PURE__*/ _interop_require_wildcard(require("react"));
const _useflightresponse = require("./use-flight-response");
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
function createServerComponentRenderer(ComponentToRender, ComponentMod, { inlinedDataTransformStream, clientReferenceManifest, serverContexts, formState }, serverComponentsErrorHandler, nonce) {
    let flightStream;
    const createFlightStream = (props)=>{
        if (!flightStream) {
            flightStream = ComponentMod.renderToReadableStream(/*#__PURE__*/ _react.default.createElement(ComponentToRender, props), clientReferenceManifest.clientModules, {
                context: serverContexts,
                onError: serverComponentsErrorHandler
            });
        }
        return flightStream;
    };
    const flightResponseRef = {
        current: null
    };
    const writable = inlinedDataTransformStream.writable;
    return function ServerComponentWrapper(props) {
        const response = (0, _useflightresponse.useFlightResponse)(writable, createFlightStream(props), clientReferenceManifest, flightResponseRef, formState, nonce);
        return (0, _react.use)(response);
    };
}

//# sourceMappingURL=create-server-components-renderer.js.map