"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    defaultConfig: null,
    normalizeConfig: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    defaultConfig: function() {
        return defaultConfig;
    },
    normalizeConfig: function() {
        return normalizeConfig;
    }
});
const _os = /*#__PURE__*/ _interop_require_default(require("os"));
const _imageconfig = require("../shared/lib/image-config");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
const defaultConfig = {
    env: {},
    webpack: null,
    eslint: {
        ignoreDuringBuilds: false
    },
    typescript: {
        ignoreBuildErrors: false,
        tsconfigPath: "tsconfig.json"
    },
    distDir: ".next",
    cleanDistDir: true,
    assetPrefix: "",
    configOrigin: "default",
    useFileSystemPublicRoutes: true,
    generateBuildId: ()=>null,
    generateEtags: true,
    pageExtensions: [
        "tsx",
        "ts",
        "jsx",
        "js"
    ],
    poweredByHeader: true,
    compress: true,
    analyticsId: process.env.VERCEL_ANALYTICS_ID || "",
    images: _imageconfig.imageConfigDefault,
    devIndicators: {
        buildActivity: true,
        buildActivityPosition: "bottom-right"
    },
    onDemandEntries: {
        maxInactiveAge: 60 * 1000,
        pagesBufferLength: 5
    },
    amp: {
        canonicalBase: ""
    },
    basePath: "",
    sassOptions: {},
    trailingSlash: false,
    i18n: null,
    productionBrowserSourceMaps: false,
    optimizeFonts: true,
    excludeDefaultMomentLocales: true,
    serverRuntimeConfig: {},
    publicRuntimeConfig: {},
    reactProductionProfiling: false,
    reactStrictMode: null,
    httpAgentOptions: {
        keepAlive: true
    },
    outputFileTracing: true,
    staticPageGenerationTimeout: 60,
    swcMinify: true,
    output: !!process.env.NEXT_PRIVATE_STANDALONE ? "standalone" : undefined,
    modularizeImports: undefined,
    experimental: {
        serverMinification: true,
        serverSourceMaps: false,
        caseSensitiveRoutes: false,
        useDeploymentId: false,
        deploymentId: undefined,
        useDeploymentIdServerActions: false,
        appDocumentPreloading: undefined,
        clientRouterFilter: true,
        clientRouterFilterRedirects: false,
        fetchCacheKeyPrefix: "",
        middlewarePrefetch: "flexible",
        optimisticClientCache: true,
        manualClientBasePath: false,
        cpus: Math.max(1, (Number(process.env.CIRCLE_NODE_TOTAL) || (_os.default.cpus() || {
            length: 1
        }).length) - 1),
        memoryBasedWorkersCount: false,
        isrFlushToDisk: true,
        workerThreads: false,
        proxyTimeout: undefined,
        optimizeCss: false,
        nextScriptWorkers: false,
        scrollRestoration: false,
        externalDir: false,
        disableOptimizedLoading: false,
        gzipSize: true,
        craCompat: false,
        esmExternals: true,
        // default to 50MB limit
        isrMemoryCacheSize: 50 * 1024 * 1024,
        incrementalCacheHandlerPath: undefined,
        fullySpecified: false,
        outputFileTracingRoot: process.env.NEXT_PRIVATE_OUTPUT_TRACE_ROOT || "",
        swcTraceProfiling: false,
        forceSwcTransforms: false,
        swcPlugins: undefined,
        largePageDataBytes: 128 * 1000,
        disablePostcssPresetEnv: undefined,
        amp: undefined,
        urlImports: undefined,
        adjustFontFallbacks: false,
        adjustFontFallbacksWithSizeAdjust: false,
        turbo: undefined,
        turbotrace: undefined,
        typedRoutes: false,
        instrumentationHook: false,
        bundlePagesExternals: false
    }
};
async function normalizeConfig(phase, config) {
    if (typeof config === "function") {
        config = config(phase, {
            defaultConfig
        });
    }
    // Support `new Promise` and `async () =>` as return values of the config export
    return await config;
}

//# sourceMappingURL=config-shared.js.map