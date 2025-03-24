"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    collectMetadata: null,
    resolveMetadataItems: null,
    accumulateMetadata: null,
    resolveMetadata: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    collectMetadata: function() {
        return collectMetadata;
    },
    resolveMetadataItems: function() {
        return resolveMetadataItems;
    },
    accumulateMetadata: function() {
        return accumulateMetadata;
    },
    resolveMetadata: function() {
        return resolveMetadata;
    }
});
const _defaultmetadata = require("./default-metadata");
const _resolveopengraph = require("./resolvers/resolve-opengraph");
const _resolvetitle = require("./resolvers/resolve-title");
const _utils = require("./generate/utils");
const _clientreference = require("../client-reference");
const _appdirmodule = require("../../server/lib/app-dir-module");
const _interopdefault = require("../interop-default");
const _resolvebasics = require("./resolvers/resolve-basics");
const _resolveicons = require("./resolvers/resolve-icons");
const _tracer = require("../../server/lib/trace/tracer");
const _constants = require("../../server/lib/trace/constants");
const _constants1 = require("../../shared/lib/constants");
function hasIconsProperty(icons, prop) {
    if (!icons) return false;
    if (prop === "icon") {
        // Detect if icons.icon will be presented, icons array and icons string will all be merged into icons.icon
        return !!(typeof icons === "string" || icons instanceof URL || Array.isArray(icons) || prop in icons && icons[prop]);
    } else {
        // Detect if icons.apple will be presented, only icons.apple will be merged into icons.apple
        return !!(typeof icons === "object" && prop in icons && icons[prop]);
    }
}
function mergeStaticMetadata(source, target, staticFilesMetadata, metadataContext, titleTemplates) {
    var _source_twitter, _source_openGraph;
    if (!staticFilesMetadata) return;
    const { icon, apple, openGraph, twitter, manifest } = staticFilesMetadata;
    // file based metadata is specified and current level metadata icons is not specified
    if (icon && !hasIconsProperty(source == null ? void 0 : source.icons, "icon") || apple && !hasIconsProperty(source == null ? void 0 : source.icons, "apple")) {
        target.icons = {
            icon: icon || [],
            apple: apple || []
        };
    }
    // file based metadata is specified and current level metadata twitter.images is not specified
    if (twitter && !(source == null ? void 0 : (_source_twitter = source.twitter) == null ? void 0 : _source_twitter.hasOwnProperty("images"))) {
        const resolvedTwitter = (0, _resolveopengraph.resolveTwitter)({
            ...target.twitter,
            images: twitter
        }, target.metadataBase, titleTemplates.twitter);
        target.twitter = resolvedTwitter;
    }
    // file based metadata is specified and current level metadata openGraph.images is not specified
    if (openGraph && !(source == null ? void 0 : (_source_openGraph = source.openGraph) == null ? void 0 : _source_openGraph.hasOwnProperty("images"))) {
        const resolvedOpenGraph = (0, _resolveopengraph.resolveOpenGraph)({
            ...target.openGraph,
            images: openGraph
        }, target.metadataBase, metadataContext, titleTemplates.openGraph);
        target.openGraph = resolvedOpenGraph;
    }
    if (manifest) {
        target.manifest = manifest;
    }
    return target;
}
// Merge the source metadata into the resolved target metadata.
function merge({ source, target, staticFilesMetadata, titleTemplates, metadataContext }) {
    // If there's override metadata, prefer it otherwise fallback to the default metadata.
    const metadataBase = typeof (source == null ? void 0 : source.metadataBase) !== "undefined" ? source.metadataBase : target.metadataBase;
    for(const key_ in source){
        const key = key_;
        switch(key){
            case "title":
                {
                    target.title = (0, _resolvetitle.resolveTitle)(source.title, titleTemplates.title);
                    break;
                }
            case "alternates":
                {
                    target.alternates = (0, _resolvebasics.resolveAlternates)(source.alternates, metadataBase, metadataContext);
                    break;
                }
            case "openGraph":
                {
                    target.openGraph = (0, _resolveopengraph.resolveOpenGraph)(source.openGraph, metadataBase, metadataContext, titleTemplates.openGraph);
                    break;
                }
            case "twitter":
                {
                    target.twitter = (0, _resolveopengraph.resolveTwitter)(source.twitter, metadataBase, titleTemplates.twitter);
                    break;
                }
            case "verification":
                target.verification = (0, _resolvebasics.resolveVerification)(source.verification);
                break;
            case "viewport":
                {
                    target.viewport = (0, _resolvebasics.resolveViewport)(source.viewport);
                    break;
                }
            case "icons":
                {
                    target.icons = (0, _resolveicons.resolveIcons)(source.icons);
                    break;
                }
            case "appleWebApp":
                target.appleWebApp = (0, _resolvebasics.resolveAppleWebApp)(source.appleWebApp);
                break;
            case "appLinks":
                target.appLinks = (0, _resolvebasics.resolveAppLinks)(source.appLinks);
                break;
            case "robots":
                {
                    target.robots = (0, _resolvebasics.resolveRobots)(source.robots);
                    break;
                }
            case "themeColor":
                {
                    target.themeColor = (0, _resolvebasics.resolveThemeColor)(source.themeColor);
                    break;
                }
            case "archives":
            case "assets":
            case "bookmarks":
            case "keywords":
                {
                    target[key] = (0, _utils.resolveAsArrayOrUndefined)(source[key]);
                    break;
                }
            case "authors":
                {
                    target[key] = (0, _utils.resolveAsArrayOrUndefined)(source.authors);
                    break;
                }
            case "itunes":
                {
                    target[key] = (0, _resolvebasics.resolveItunes)(source.itunes, metadataBase, metadataContext);
                    break;
                }
            // directly assign fields that fallback to null
            case "applicationName":
            case "description":
            case "generator":
            case "creator":
            case "publisher":
            case "category":
            case "classification":
            case "referrer":
            case "colorScheme":
            case "formatDetection":
            case "manifest":
                // @ts-ignore TODO: support inferring
                target[key] = source[key] || null;
                break;
            case "other":
                target.other = Object.assign({}, target.other, source.other);
                break;
            case "metadataBase":
                target.metadataBase = metadataBase;
                break;
            default:
                break;
        }
    }
    mergeStaticMetadata(source, target, staticFilesMetadata, metadataContext, titleTemplates);
}
async function getDefinedMetadata(mod, props, tracingProps) {
    // Layer is a client component, we just skip it. It can't have metadata exported.
    // Return early to avoid accessing properties error for client references.
    if ((0, _clientreference.isClientReference)(mod)) {
        return null;
    }
    if (typeof mod.generateMetadata === "function") {
        const { route } = tracingProps;
        return (parent)=>(0, _tracer.getTracer)().trace(_constants.ResolveMetadataSpan.generateMetadata, {
                spanName: `generateMetadata ${route}`,
                attributes: {
                    "next.page": route
                }
            }, ()=>mod.generateMetadata(props, parent));
    }
    return mod.metadata || null;
}
async function collectStaticImagesFiles(metadata, props, type) {
    var _this;
    if (!(metadata == null ? void 0 : metadata[type])) return undefined;
    const iconPromises = metadata[type].map(async (imageModule)=>(0, _interopdefault.interopDefault)(await imageModule(props)));
    return (iconPromises == null ? void 0 : iconPromises.length) > 0 ? (_this = await Promise.all(iconPromises)) == null ? void 0 : _this.flat() : undefined;
}
async function resolveStaticMetadata(components, props) {
    const { metadata } = components;
    if (!metadata) return null;
    const [icon, apple, openGraph, twitter] = await Promise.all([
        collectStaticImagesFiles(metadata, props, "icon"),
        collectStaticImagesFiles(metadata, props, "apple"),
        collectStaticImagesFiles(metadata, props, "openGraph"),
        collectStaticImagesFiles(metadata, props, "twitter")
    ]);
    const staticMetadata = {
        icon,
        apple,
        openGraph,
        twitter,
        manifest: metadata.manifest
    };
    return staticMetadata;
}
async function collectMetadata({ tree, metadataItems, errorMetadataItem, props, route, errorConvention }) {
    let mod;
    let modType;
    const hasErrorConventionComponent = Boolean(errorConvention && tree[2][errorConvention]);
    if (errorConvention) {
        mod = await (0, _appdirmodule.getComponentTypeModule)(tree, "layout");
        modType = errorConvention;
    } else {
        [mod, modType] = await (0, _appdirmodule.getLayoutOrPageModule)(tree);
    }
    if (modType) {
        route += `/${modType}`;
    }
    const staticFilesMetadata = await resolveStaticMetadata(tree[2], props);
    const metadataExport = mod ? await getDefinedMetadata(mod, props, {
        route
    }) : null;
    metadataItems.push([
        metadataExport,
        staticFilesMetadata
    ]);
    if (hasErrorConventionComponent && errorConvention) {
        const errorMod = await (0, _appdirmodule.getComponentTypeModule)(tree, errorConvention);
        const errorMetadataExport = errorMod ? await getDefinedMetadata(errorMod, props, {
            route
        }) : null;
        errorMetadataItem[0] = errorMetadataExport;
        errorMetadataItem[1] = staticFilesMetadata;
    }
}
async function resolveMetadataItems({ tree, parentParams, metadataItems, errorMetadataItem, treePrefix = [], getDynamicParamFromSegment, searchParams, errorConvention }) {
    const [segment, parallelRoutes, { page }] = tree;
    const currentTreePrefix = [
        ...treePrefix,
        segment
    ];
    const isPage = typeof page !== "undefined";
    // Handle dynamic segment params.
    const segmentParam = getDynamicParamFromSegment(segment);
    /**
   * Create object holding the parent params and current params
   */ const currentParams = // Handle null case where dynamic param is optional
    segmentParam && segmentParam.value !== null ? {
        ...parentParams,
        [segmentParam.param]: segmentParam.value
    } : parentParams;
    const layerProps = {
        params: currentParams,
        ...isPage && {
            searchParams
        }
    };
    await collectMetadata({
        tree,
        metadataItems,
        errorMetadataItem,
        errorConvention,
        props: layerProps,
        route: currentTreePrefix// __PAGE__ shouldn't be shown in a route
        .filter((s)=>s !== _constants1.PAGE_SEGMENT_KEY).join("/")
    });
    for(const key in parallelRoutes){
        const childTree = parallelRoutes[key];
        await resolveMetadataItems({
            tree: childTree,
            metadataItems,
            errorMetadataItem,
            parentParams: currentParams,
            treePrefix: currentTreePrefix,
            searchParams,
            getDynamicParamFromSegment,
            errorConvention
        });
    }
    if (Object.keys(parallelRoutes).length === 0 && errorConvention) {
        // If there are no parallel routes, place error metadata as the last item.
        // e.g. layout -> layout -> not-found
        metadataItems.push(errorMetadataItem);
    }
    return metadataItems;
}
const commonOgKeys = [
    "title",
    "description",
    "images"
];
function postProcessMetadata(metadata, titleTemplates) {
    const { openGraph, twitter } = metadata;
    if (openGraph) {
        let autoFillProps = {};
        const hasTwTitle = twitter == null ? void 0 : twitter.title.absolute;
        const hasTwDescription = twitter == null ? void 0 : twitter.description;
        const hasTwImages = Boolean((twitter == null ? void 0 : twitter.hasOwnProperty("images")) && twitter.images);
        if (!hasTwTitle) autoFillProps.title = openGraph.title;
        if (!hasTwDescription) autoFillProps.description = openGraph.description;
        if (!hasTwImages) autoFillProps.images = openGraph.images;
        if (Object.keys(autoFillProps).length > 0) {
            const partialTwitter = (0, _resolveopengraph.resolveTwitter)(autoFillProps, metadata.metadataBase, titleTemplates.twitter);
            if (metadata.twitter) {
                metadata.twitter = Object.assign({}, metadata.twitter, {
                    ...!hasTwTitle && {
                        title: partialTwitter == null ? void 0 : partialTwitter.title
                    },
                    ...!hasTwDescription && {
                        description: partialTwitter == null ? void 0 : partialTwitter.description
                    },
                    ...!hasTwImages && {
                        images: partialTwitter == null ? void 0 : partialTwitter.images
                    }
                });
            } else {
                metadata.twitter = partialTwitter;
            }
        }
    }
    return metadata;
}
async function accumulateMetadata(metadataItems, metadataContext) {
    const resolvedMetadata = (0, _defaultmetadata.createDefaultMetadata)();
    const resolvers = [];
    const generateMetadataResults = [];
    let titleTemplates = {
        title: null,
        twitter: null,
        openGraph: null
    };
    // Loop over all metadata items again, merging synchronously any static object exports,
    // awaiting any static promise exports, and resolving parent metadata and awaiting any generated metadata
    let resolvingIndex = 0;
    for(let i = 0; i < metadataItems.length; i++){
        const [metadataExport, staticFilesMetadata] = metadataItems[i];
        let metadata = null;
        if (typeof metadataExport === "function") {
            if (!resolvers.length) {
                for(let j = i; j < metadataItems.length; j++){
                    const [preloadMetadataExport] = metadataItems[j];
                    // call each `generateMetadata function concurrently and stash their resolver
                    if (typeof preloadMetadataExport === "function") {
                        generateMetadataResults.push(preloadMetadataExport(new Promise((resolve)=>{
                            resolvers.push(resolve);
                        })));
                    }
                }
            }
            const resolveParent = resolvers[resolvingIndex];
            const generatedMetadata = generateMetadataResults[resolvingIndex++];
            // In dev we clone and freeze to prevent relying on mutating resolvedMetadata directly.
            // In prod we just pass resolvedMetadata through without any copying.
            const currentResolvedMetadata = process.env.NODE_ENV === "development" ? Object.freeze(require("./clone-metadata").cloneMetadata(resolvedMetadata)) : resolvedMetadata;
            // This resolve should unblock the generateMetadata function if it awaited the parent
            // argument. If it didn't await the parent argument it might already have a value since it was
            // called concurrently. Regardless we await the return value before continuing on to the next layer
            resolveParent(currentResolvedMetadata);
            metadata = generatedMetadata instanceof Promise ? await generatedMetadata : generatedMetadata;
        } else if (metadataExport !== null && typeof metadataExport === "object") {
            // This metadataExport is the object form
            metadata = metadataExport;
        }
        merge({
            metadataContext,
            target: resolvedMetadata,
            source: metadata,
            staticFilesMetadata,
            titleTemplates
        });
        // If the layout is the same layer with page, skip the leaf layout and leaf page
        // The leaf layout and page are the last two items
        if (i < metadataItems.length - 2) {
            var _resolvedMetadata_title, _resolvedMetadata_openGraph, _resolvedMetadata_twitter;
            titleTemplates = {
                title: ((_resolvedMetadata_title = resolvedMetadata.title) == null ? void 0 : _resolvedMetadata_title.template) || null,
                openGraph: ((_resolvedMetadata_openGraph = resolvedMetadata.openGraph) == null ? void 0 : _resolvedMetadata_openGraph.title.template) || null,
                twitter: ((_resolvedMetadata_twitter = resolvedMetadata.twitter) == null ? void 0 : _resolvedMetadata_twitter.title.template) || null
            };
        }
    }
    return postProcessMetadata(resolvedMetadata, titleTemplates);
}
async function resolveMetadata({ tree, parentParams, metadataItems, errorMetadataItem, getDynamicParamFromSegment, searchParams, errorConvention, metadataContext }) {
    const resolvedMetadataItems = await resolveMetadataItems({
        tree,
        parentParams,
        metadataItems,
        errorMetadataItem,
        getDynamicParamFromSegment,
        searchParams,
        errorConvention
    });
    let metadata = (0, _defaultmetadata.createDefaultMetadata)();
    let error;
    try {
        metadata = await accumulateMetadata(resolvedMetadataItems, metadataContext);
    } catch (err) {
        error = err;
    }
    return [
        metadata,
        error
    ];
}

//# sourceMappingURL=resolve-metadata.js.map