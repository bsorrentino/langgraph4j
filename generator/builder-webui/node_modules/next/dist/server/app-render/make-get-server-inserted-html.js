"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "makeGetServerInsertedHTML", {
    enumerable: true,
    get: function() {
        return makeGetServerInsertedHTML;
    }
});
const _react = /*#__PURE__*/ _interop_require_default(require("react"));
const _notfound = require("../../client/components/not-found");
const _redirect = require("../../client/components/redirect");
const _getredirectstatuscodefromerror = require("../../client/components/get-redirect-status-code-from-error");
const _rendertostring = require("./render-to-string");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
function makeGetServerInsertedHTML({ polyfills, renderServerInsertedHTML }) {
    let flushedErrorMetaTagsUntilIndex = 0;
    let polyfillsFlushed = false;
    return function getServerInsertedHTML(serverCapturedErrors) {
        // Loop through all the errors that have been captured but not yet
        // flushed.
        const errorMetaTags = [];
        for(; flushedErrorMetaTagsUntilIndex < serverCapturedErrors.length; flushedErrorMetaTagsUntilIndex++){
            const error = serverCapturedErrors[flushedErrorMetaTagsUntilIndex];
            if ((0, _notfound.isNotFoundError)(error)) {
                errorMetaTags.push(/*#__PURE__*/ _react.default.createElement("meta", {
                    name: "robots",
                    content: "noindex",
                    key: error.digest
                }), process.env.NODE_ENV === "development" ? /*#__PURE__*/ _react.default.createElement("meta", {
                    name: "next-error",
                    content: "not-found",
                    key: "next-error"
                }) : null);
            } else if ((0, _redirect.isRedirectError)(error)) {
                const redirectUrl = (0, _redirect.getURLFromRedirectError)(error);
                const isPermanent = (0, _getredirectstatuscodefromerror.getRedirectStatusCodeFromError)(error) === 308 ? true : false;
                if (redirectUrl) {
                    errorMetaTags.push(/*#__PURE__*/ _react.default.createElement("meta", {
                        httpEquiv: "refresh",
                        content: `${isPermanent ? 0 : 1};url=${redirectUrl}`,
                        key: error.digest
                    }));
                }
            }
        }
        const flushed = (0, _rendertostring.renderToString)({
            ReactDOMServer: require("react-dom/server.edge"),
            element: /*#__PURE__*/ _react.default.createElement(_react.default.Fragment, null, polyfillsFlushed ? null : polyfills == null ? void 0 : polyfills.map((polyfill)=>{
                return /*#__PURE__*/ _react.default.createElement("script", {
                    key: polyfill.src,
                    ...polyfill
                });
            }), renderServerInsertedHTML(), errorMetaTags)
        });
        polyfillsFlushed = true;
        return flushed;
    };
}

//# sourceMappingURL=make-get-server-inserted-html.js.map