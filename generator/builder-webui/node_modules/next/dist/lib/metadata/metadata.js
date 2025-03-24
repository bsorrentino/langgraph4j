"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "createMetadataComponents", {
    enumerable: true,
    get: function() {
        return createMetadataComponents;
    }
});
const _react = /*#__PURE__*/ _interop_require_default(require("react"));
const _basic = require("./generate/basic");
const _alternate = require("./generate/alternate");
const _opengraph = require("./generate/opengraph");
const _icons = require("./generate/icons");
const _resolvemetadata = require("./resolve-metadata");
const _meta = require("./generate/meta");
const _defaultmetadata = require("./default-metadata");
const _notfound = require("../../client/components/not-found");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
function createMetadataComponents({ tree, pathname, searchParams, getDynamicParamFromSegment, appUsingSizeAdjustment, errorType }) {
    const metadataContext = {
        pathname
    };
    let resolve;
    // Only use promise.resolve here to avoid unhandled rejections
    const metadataErrorResolving = new Promise((res)=>{
        resolve = res;
    });
    async function MetadataTree() {
        const defaultMetadata = (0, _defaultmetadata.createDefaultMetadata)();
        let metadata = defaultMetadata;
        let error;
        const errorMetadataItem = [
            null,
            null
        ];
        const errorConvention = errorType === "redirect" ? undefined : errorType;
        const [resolvedMetadata, resolvedError] = await (0, _resolvemetadata.resolveMetadata)({
            tree,
            parentParams: {},
            metadataItems: [],
            errorMetadataItem,
            searchParams,
            getDynamicParamFromSegment,
            errorConvention,
            metadataContext
        });
        if (!resolvedError) {
            metadata = resolvedMetadata;
            resolve(undefined);
        } else {
            error = resolvedError;
            // If the error triggers in initial metadata resolving, re-resolve with proper error type.
            // They'll be saved for flight data, when hydrates, it will replaces the SSR'd metadata with this.
            // for not-found error: resolve not-found metadata
            if (!errorType && (0, _notfound.isNotFoundError)(resolvedError)) {
                const [notFoundMetadata, notFoundMetadataError] = await (0, _resolvemetadata.resolveMetadata)({
                    tree,
                    parentParams: {},
                    metadataItems: [],
                    errorMetadataItem,
                    searchParams,
                    getDynamicParamFromSegment,
                    errorConvention: "not-found",
                    metadataContext
                });
                metadata = notFoundMetadata;
                error = notFoundMetadataError || error;
            }
            resolve(error);
        }
        const elements = (0, _meta.MetaFilter)([
            (0, _basic.BasicMetadata)({
                metadata
            }),
            (0, _alternate.AlternatesMetadata)({
                alternates: metadata.alternates
            }),
            (0, _basic.ItunesMeta)({
                itunes: metadata.itunes
            }),
            (0, _basic.FormatDetectionMeta)({
                formatDetection: metadata.formatDetection
            }),
            (0, _basic.VerificationMeta)({
                verification: metadata.verification
            }),
            (0, _basic.AppleWebAppMeta)({
                appleWebApp: metadata.appleWebApp
            }),
            (0, _opengraph.OpenGraphMetadata)({
                openGraph: metadata.openGraph
            }),
            (0, _opengraph.TwitterMetadata)({
                twitter: metadata.twitter
            }),
            (0, _opengraph.AppLinksMeta)({
                appLinks: metadata.appLinks
            }),
            (0, _icons.IconsMetadata)({
                icons: metadata.icons
            })
        ]);
        if (appUsingSizeAdjustment) elements.push(/*#__PURE__*/ _react.default.createElement("meta", {
            name: "next-size-adjust"
        }));
        return /*#__PURE__*/ _react.default.createElement(_react.default.Fragment, null, elements.map((el, index)=>{
            return /*#__PURE__*/ _react.default.cloneElement(el, {
                key: index
            });
        }));
    }
    async function MetadataOutlet() {
        const error = await metadataErrorResolving;
        if (error) {
            throw error;
        }
        return null;
    }
    // @ts-expect-error async server components
    return [
        MetadataTree,
        MetadataOutlet
    ];
}

//# sourceMappingURL=metadata.js.map