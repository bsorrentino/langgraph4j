"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "createComponentAndStyles", {
    enumerable: true,
    get: function() {
        return createComponentAndStyles;
    }
});
const _react = /*#__PURE__*/ _interop_require_default(require("react"));
const _interopdefault = require("./interop-default");
const _getcssinlinedlinktags = require("./get-css-inlined-link-tags");
const _getassetquerystring = require("./get-asset-query-string");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
async function createComponentAndStyles({ filePath, getComponent, injectedCSS, ctx }) {
    const cssHrefs = (0, _getcssinlinedlinktags.getCssInlinedLinkTags)(ctx.clientReferenceManifest, filePath, injectedCSS);
    const styles = cssHrefs ? cssHrefs.map((href, index)=>{
        // In dev, Safari and Firefox will cache the resource during HMR:
        // - https://github.com/vercel/next.js/issues/5860
        // - https://bugs.webkit.org/show_bug.cgi?id=187726
        // Because of this, we add a `?v=` query to bypass the cache during
        // development. We need to also make sure that the number is always
        // increasing.
        const fullHref = `${ctx.assetPrefix}/_next/${href}${(0, _getassetquerystring.getAssetQueryString)(ctx, true)}`;
        // `Precedence` is an opt-in signal for React to handle resource
        // loading and deduplication, etc. It's also used as the key to sort
        // resources so they will be injected in the correct order.
        // During HMR, it's critical to use different `precedence` values
        // for different stylesheets, so their order will be kept.
        // https://github.com/facebook/react/pull/25060
        const precedence = process.env.NODE_ENV === "development" ? "next_" + href : "next";
        return /*#__PURE__*/ _react.default.createElement("link", {
            rel: "stylesheet",
            href: fullHref,
            // @ts-ignore
            precedence: precedence,
            crossOrigin: ctx.renderOpts.crossOrigin,
            key: index
        });
    }) : null;
    const Comp = (0, _interopdefault.interopDefault)(await getComponent());
    return [
        Comp,
        styles
    ];
}

//# sourceMappingURL=create-component-and-styles.js.map