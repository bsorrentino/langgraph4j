"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "getLayerAssets", {
    enumerable: true,
    get: function() {
        return getLayerAssets;
    }
});
const _react = /*#__PURE__*/ _interop_require_default(require("react"));
const _getcssinlinedlinktags = require("./get-css-inlined-link-tags");
const _getpreloadablefonts = require("./get-preloadable-fonts");
const _getassetquerystring = require("./get-asset-query-string");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
function getLayerAssets({ ctx, layoutOrPagePath, injectedCSS: injectedCSSWithCurrentLayout, injectedFontPreloadTags: injectedFontPreloadTagsWithCurrentLayout }) {
    const stylesheets = layoutOrPagePath ? (0, _getcssinlinedlinktags.getCssInlinedLinkTags)(ctx.clientReferenceManifest, layoutOrPagePath, injectedCSSWithCurrentLayout, true) : [];
    const preloadedFontFiles = layoutOrPagePath ? (0, _getpreloadablefonts.getPreloadableFonts)(ctx.renderOpts.nextFontManifest, layoutOrPagePath, injectedFontPreloadTagsWithCurrentLayout) : null;
    if (preloadedFontFiles) {
        if (preloadedFontFiles.length) {
            for(let i = 0; i < preloadedFontFiles.length; i++){
                const fontFilename = preloadedFontFiles[i];
                const ext = /\.(woff|woff2|eot|ttf|otf)$/.exec(fontFilename)[1];
                const type = `font/${ext}`;
                const href = `${ctx.assetPrefix}/_next/${fontFilename}`;
                ctx.componentMod.preloadFont(href, type, ctx.renderOpts.crossOrigin);
            }
        } else {
            try {
                let url = new URL(ctx.assetPrefix);
                ctx.componentMod.preconnect(url.origin, "anonymous");
            } catch (error) {
                // assetPrefix must not be a fully qualified domain name. We assume
                // we should preconnect to same origin instead
                ctx.componentMod.preconnect("/", "anonymous");
            }
        }
    }
    const styles = stylesheets ? stylesheets.map((href, index)=>{
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
        ctx.componentMod.preloadStyle(fullHref, ctx.renderOpts.crossOrigin);
        return /*#__PURE__*/ _react.default.createElement("link", {
            rel: "stylesheet",
            href: fullHref,
            // @ts-ignore
            precedence: precedence,
            crossOrigin: ctx.renderOpts.crossOrigin,
            key: index
        });
    }) : null;
    return styles;
}

//# sourceMappingURL=get-layer-assets.js.map