"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "validateTurboNextConfig", {
    enumerable: true,
    get: function() {
        return validateTurboNextConfig;
    }
});
const _path = /*#__PURE__*/ _interop_require_default(require("path"));
const _config = /*#__PURE__*/ _interop_require_default(require("../server/config"));
const _constants = require("../shared/lib/constants");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
const supportedTurbopackNextConfigOptions = [
    // Options that affect compilation
    "output",
    "crossOrigin",
    "configFileName",
    "env",
    "basePath",
    "modularizeImports",
    "compiler.emotion",
    "compiler.relay",
    "compiler.styledComponents",
    "images",
    "pageExtensions",
    "onDemandEntries",
    "rewrites",
    "redirects",
    "headers",
    "reactStrictMode",
    "swcMinify",
    "transpilePackages",
    "trailingSlash",
    "i18n.defaultLocale",
    "i18n.domains",
    "i18n.localeDetection",
    "i18n.locales",
    "sassOptions",
    "configOrigin",
    "httpAgentOptions",
    "useFileSystemPublicRoutes",
    "generateEtags",
    "assetPrefix",
    "distDir",
    "skipMiddlewareUrlNormalize",
    "skipTrailingSlashRedirect",
    "amp",
    "devIndicators",
    "analyticsId",
    // Options that are ignored as they don't affect Turbopack
    "webpack",
    "onDemandEntries",
    "experimental.cpus",
    // Experimental options that affect compilation
    "experimental.swcPlugins",
    "experimental.strictNextHead",
    "experimental.manualClientBasePath",
    "experimental.middlewarePrefetch",
    "experimental.optimizeCss",
    "experimental.nextScriptWorkers",
    "experimental.optimisticClientCache",
    "experimental.webVitalsAttribution",
    "experimental.externalMiddlewareRewritesResolve",
    "experimental.serverComponentsExternalPackages",
    "experimental.mdxRs",
    "experimental.turbo",
    "experimental.useDeploymentId",
    "experimental.useDeploymentIdServerActions",
    "experimental.deploymentId",
    "experimental.serverActions",
    // Experimental options that don't affect compilation
    "serverRuntimeConfig",
    "publicRuntimeConfig",
    "experimental.proxyTimeout",
    "experimental.caseSensitiveRoutes",
    "experimental.workerThreads",
    "experimental.isrFlushToDisk",
    "experimental.logging.level",
    "experimental.logging.fullUrl",
    "experimental.scrollRestoration",
    "experimental.forceSwcTransforms",
    "experimental.serverActionsBodySizeLimit",
    "experimental.memoryBasedWorkersCount",
    "experimental.clientRouterFilterRedirects",
    "experimental.webpackBuildWorker",
    "experimental.appDocumentPreloading",
    "experimental.incrementalCacheHandlerPath",
    "experimental.amp",
    "experimental.disableOptimizedLoading",
    "experimental.isrMemoryCacheSize",
    "experimental.largePageDataBytes",
    "experimental.gzipSize",
    "experimental.trustHostHeader",
    // Left to be implemented (priority)
    // 'experimental.ppr', // Checked in `needs-experimental-react.ts`
    // clientRouterFilter is `true` by default currently in config-shared.ts,
    // might be removed as an option altogether.
    "experimental.clientRouterFilter",
    "experimental.optimizePackageImports",
    // 'compiler.emotion',
    // 'compiler.reactRemoveProperties',
    // 'compiler.relay',
    // 'compiler.removeConsole',
    // 'compiler.styledComponents',
    // 'experimental.fetchCacheKeyPrefix',
    // 'experimental.instrumentationHook',
    // Left to be implemented
    "excludeDefaultMomentLocales",
    "experimental.optimizeServerReact",
    // 'experimental.clientRouterFilterAllowedRate',
    "experimental.serverMinification",
    "experimental.serverSourceMaps"
];
// The following will need to be supported by `next build --turbo`
const prodSpecificTurboNextConfigOptions = [
    "eslint",
    "typescript",
    "staticPageGenerationTimeout",
    "outputFileTracing",
    "generateBuildId",
    "compress",
    "productionBrowserSourceMaps",
    "optimizeFonts",
    "poweredByHeader",
    "staticPageGenerationTimeout",
    "reactProductionProfiling",
    "cleanDistDir",
    "experimental.turbotrace",
    "experimental.outputFileTracingRoot",
    "experimental.outputFileTracingExcludes",
    "experimental.outputFileTracingIgnores",
    "experimental.outputFileTracingIncludes"
];
async function validateTurboNextConfig({ dir, isDev }) {
    const { getPkgManager } = require("../lib/helpers/get-pkg-manager");
    const { getBabelConfigFile } = require("../build/get-babel-config-file");
    const { defaultConfig } = require("../server/config-shared");
    const { bold, cyan, dim, red, underline, yellow } = require("../lib/picocolors");
    const { interopDefault } = require("../lib/interop-default");
    // To regenerate the TURBOPACK gradient require('gradient-string')('blue', 'red')('>>> TURBOPACK')
    const isTTY = process.stdout.isTTY;
    const turbopackGradient = `${bold(isTTY ? "\x1b[38;2;0;0;255m>\x1b[39m\x1b[38;2;23;0;232m>\x1b[39m\x1b[38;2;46;0;209m>\x1b[39m \x1b[38;2;70;0;185mT\x1b[39m\x1b[38;2;93;0;162mU\x1b[39m\x1b[38;2;116;0;139mR\x1b[39m\x1b[38;2;139;0;116mB\x1b[39m\x1b[38;2;162;0;93mO\x1b[39m\x1b[38;2;185;0;70mP\x1b[39m\x1b[38;2;209;0;46mA\x1b[39m\x1b[38;2;232;0;23mC\x1b[39m\x1b[38;2;255;0;0mK\x1b[39m" : ">>> TURBOPACK")} ${dim("(beta)")}\n\n`;
    let thankYouMessage = [
        "Thank you for trying Next.js v13 with Turbopack! As a reminder",
        "Turbopack is currently in beta and not yet ready for production.",
        "We appreciate your ongoing support as we work to make it ready",
        "for everyone."
    ].join("\n") + "\n\n";
    let unsupportedParts = "";
    let babelrc = await getBabelConfigFile(dir);
    if (babelrc) babelrc = _path.default.basename(babelrc);
    let hasWebpack = false;
    let hasTurbo = false;
    let unsupportedConfig = [];
    let rawNextConfig = {};
    try {
        rawNextConfig = interopDefault(await (0, _config.default)(_constants.PHASE_DEVELOPMENT_SERVER, dir, {
            rawConfig: true
        }));
        if (typeof rawNextConfig === "function") {
            rawNextConfig = rawNextConfig(_constants.PHASE_DEVELOPMENT_SERVER, {
                defaultConfig
            });
        }
        const flattenKeys = (obj, prefix = "")=>{
            let keys = [];
            for(const key in obj){
                if (typeof (obj == null ? void 0 : obj[key]) === "undefined") {
                    continue;
                }
                const pre = prefix.length ? `${prefix}.` : "";
                if (typeof obj[key] === "object" && !Array.isArray(obj[key]) && obj[key] !== null) {
                    keys = keys.concat(flattenKeys(obj[key], pre + key));
                } else {
                    keys.push(pre + key);
                }
            }
            return keys;
        };
        const getDeepValue = (obj, keys)=>{
            if (typeof keys === "string") {
                keys = keys.split(".");
            }
            if (keys.length === 1) {
                return obj == null ? void 0 : obj[keys == null ? void 0 : keys[0]];
            }
            return getDeepValue(obj == null ? void 0 : obj[keys == null ? void 0 : keys[0]], keys.slice(1));
        };
        const customKeys = flattenKeys(rawNextConfig);
        let supportedKeys = isDev ? [
            ...supportedTurbopackNextConfigOptions,
            ...prodSpecificTurboNextConfigOptions
        ] : supportedTurbopackNextConfigOptions;
        for (const key of customKeys){
            if (key.startsWith("webpack")) {
                hasWebpack = true;
            }
            if (key.startsWith("experimental.turbo")) {
                hasTurbo = true;
            }
            let isSupported = supportedKeys.some((supportedKey)=>key.startsWith(supportedKey)) || getDeepValue(rawNextConfig, key) === getDeepValue(defaultConfig, key);
            if (!isSupported) {
                unsupportedConfig.push(key);
            }
        }
    } catch (e) {
        console.error("Unexpected error occurred while checking config", e);
    }
    const hasWarningOrError = babelrc || unsupportedConfig.length;
    if (!hasWarningOrError) {
        thankYouMessage = dim(thankYouMessage);
    }
    console.log(turbopackGradient + thankYouMessage);
    let feedbackMessage = `Learn more about Next.js v13 and Turbopack: ${underline("https://nextjs.link/with-turbopack")}\n`;
    if (hasWebpack && !hasTurbo) {
        console.warn(`\n${yellow("Warning:")} Webpack is configured while Turbopack is not, which may cause problems.\n
  ${`See instructions if you need to configure Turbopack:\n  https://turbo.build/pack/docs/features/customizing-turbopack\n`}`);
    }
    if (babelrc) {
        unsupportedParts += `\n- Babel detected (${cyan(babelrc)})\n  Babel is not yet supported. To use Turbopack at the moment,\n  you'll need to remove your usage of Babel.`;
    }
    if (unsupportedConfig.length === 1 && unsupportedConfig[0] === "experimental.optimizePackageImports") {
        console.warn(`\n${yellow("Warning:")} ${cyan("experimental.optimizePackageImports")} is not yet supported by Turbopack and will be ignored.`);
    } else if (unsupportedConfig.length) {
        unsupportedParts += `\n\n- Unsupported Next.js configuration option(s) (${cyan("next.config.js")})\n  To use Turbopack, remove the following configuration options:\n${unsupportedConfig.map((name)=>`    - ${red(name)}\n`).join("")}`;
    }
    if (unsupportedParts) {
        const pkgManager = getPkgManager(dir);
        console.error(`Error: You are using configuration and/or tools that are not yet\nsupported by Next.js v13 with Turbopack:\n${unsupportedParts}\n
If you cannot make the changes above, but still want to try out\nNext.js v13 with Turbopack, create the Next.js v13 playground app\nby running the following commands:

  ${bold(cyan(`${pkgManager === "npm" ? "npx create-next-app" : `${pkgManager} create next-app`} --example with-turbopack with-turbopack-app`))}\n  cd with-turbopack-app\n  ${pkgManager} run dev
        `);
        console.warn(feedbackMessage);
        process.exit(1);
    }
    console.log(feedbackMessage);
    return rawNextConfig;
}

//# sourceMappingURL=turbopack-warning.js.map