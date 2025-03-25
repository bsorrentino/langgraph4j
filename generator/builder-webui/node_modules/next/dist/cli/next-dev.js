#!/usr/bin/env node
"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "nextDev", {
    enumerable: true,
    get: function() {
        return nextDev;
    }
});
require("../server/lib/cpu-profile");
const _utils = require("../server/lib/utils");
const _log = /*#__PURE__*/ _interop_require_wildcard(require("../build/output/log"));
const _getprojectdir = require("../lib/get-project-dir");
const _constants = require("../shared/lib/constants");
const _path = /*#__PURE__*/ _interop_require_default(require("path"));
const _shared = require("../trace/shared");
const _storage = require("../telemetry/storage");
const _config = /*#__PURE__*/ _interop_require_wildcard(require("../server/config"));
const _findpagesdir = require("../lib/find-pages-dir");
const _fileexists = require("../lib/file-exists");
const _getnpxcommand = require("../lib/helpers/get-npx-command");
const _mkcert = require("../lib/mkcert");
const _uploadtrace = /*#__PURE__*/ _interop_require_default(require("../trace/upload-trace"));
const _env = require("@next/env");
const _trace = require("../trace");
const _turbopackwarning = require("../lib/turbopack-warning");
const _child_process = require("child_process");
const _getreservedport = require("../lib/helpers/get-reserved-port");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
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
let dir;
let child;
let config;
let isTurboSession = false;
let traceUploadUrl;
let sessionStopHandled = false;
let sessionStarted = Date.now();
const handleSessionStop = async (signal)=>{
    if (child) {
        child.kill(signal || 0);
    }
    if (sessionStopHandled) return;
    sessionStopHandled = true;
    try {
        const { eventCliSessionStopped } = require("../telemetry/events/session-stopped");
        config = config || await (0, _config.default)(_constants.PHASE_DEVELOPMENT_SERVER, dir);
        let telemetry = _shared.traceGlobals.get("telemetry") || new _storage.Telemetry({
            distDir: _path.default.join(dir, config.distDir)
        });
        let pagesDir = !!_shared.traceGlobals.get("pagesDir");
        let appDir = !!_shared.traceGlobals.get("appDir");
        if (typeof _shared.traceGlobals.get("pagesDir") === "undefined" || typeof _shared.traceGlobals.get("appDir") === "undefined") {
            const pagesResult = (0, _findpagesdir.findPagesDir)(dir);
            appDir = !!pagesResult.appDir;
            pagesDir = !!pagesResult.pagesDir;
        }
        telemetry.record(eventCliSessionStopped({
            cliCommand: "dev",
            turboFlag: isTurboSession,
            durationMilliseconds: Date.now() - sessionStarted,
            pagesDir,
            appDir
        }), true);
        telemetry.flushDetached("dev", dir);
    } catch (_) {
    // errors here aren't actionable so don't add
    // noise to the output
    }
    if (traceUploadUrl) {
        (0, _uploadtrace.default)({
            traceUploadUrl,
            mode: "dev",
            isTurboSession,
            projectDir: dir,
            distDir: config.distDir
        });
    }
    // ensure we re-enable the terminal cursor before exiting
    // the program, or the cursor could remain hidden
    process.stdout.write("\x1b[?25h");
    process.stdout.write("\n");
    process.exit(0);
};
process.on("SIGINT", ()=>handleSessionStop("SIGINT"));
process.on("SIGTERM", ()=>handleSessionStop("SIGTERM"));
const nextDev = async (args)=>{
    if (args["--help"]) {
        console.log(`
      Description
        Starts the application in development mode (hot-code reloading, error
        reporting, etc.)

      Usage
        $ next dev <dir> -p <port number>

      <dir> represents the directory of the Next.js application.
      If no directory is provided, the current directory will be used.

      Options
        --port, -p      A port number on which to start the application
        --hostname, -H  Hostname on which to start the application (default: 0.0.0.0)
        --experimental-upload-trace=<trace-url>  [EXPERIMENTAL] Report a subset of the debugging trace to a remote http url. Includes sensitive data. Disabled by default and url must be provided.
        --help, -h      Displays this message
    `);
        process.exit(0);
    }
    dir = (0, _getprojectdir.getProjectDir)(process.env.NEXT_PRIVATE_DEV_DIR || args._[0]);
    // Check if pages dir exists and warn if not
    if (!await (0, _fileexists.fileExists)(dir, _fileexists.FileType.Directory)) {
        (0, _utils.printAndExit)(`> No such directory exists as the project root: ${dir}`);
    }
    async function preflight(skipOnReboot) {
        const { getPackageVersion, getDependencies } = await Promise.resolve(require("../lib/get-package-version"));
        const [sassVersion, nodeSassVersion] = await Promise.all([
            getPackageVersion({
                cwd: dir,
                name: "sass"
            }),
            getPackageVersion({
                cwd: dir,
                name: "node-sass"
            })
        ]);
        if (sassVersion && nodeSassVersion) {
            _log.warn("Your project has both `sass` and `node-sass` installed as dependencies, but should only use one or the other. " + "Please remove the `node-sass` dependency from your project. " + " Read more: https://nextjs.org/docs/messages/duplicate-sass");
        }
        if (!skipOnReboot) {
            const { dependencies, devDependencies } = await getDependencies({
                cwd: dir
            });
            // Warn if @next/font is installed as a dependency. Ignore `workspace:*` to not warn in the Next.js monorepo.
            if (dependencies["@next/font"] || devDependencies["@next/font"] && devDependencies["@next/font"] !== "workspace:*") {
                const command = (0, _getnpxcommand.getNpxCommand)(dir);
                _log.warn("Your project has `@next/font` installed as a dependency, please use the built-in `next/font` instead. " + "The `@next/font` package will be removed in Next.js 14. " + `You can migrate by running \`${command} @next/codemod@latest built-in-next-font .\`. Read more: https://nextjs.org/docs/messages/built-in-next-font`);
            }
        }
    }
    const port = (0, _utils.getPort)(args);
    if ((0, _getreservedport.isPortIsReserved)(port)) {
        (0, _utils.printAndExit)((0, _getreservedport.getReservedPortExplanation)(port), 1);
    }
    // If neither --port nor PORT were specified, it's okay to retry new ports.
    const allowRetry = args["--port"] === undefined && process.env.PORT === undefined;
    // We do not set a default host value here to prevent breaking
    // some set-ups that rely on listening on other interfaces
    const host = args["--hostname"];
    const { loadedEnvFiles } = (0, _env.loadEnvConfig)(dir, true, console, false);
    let expFeatureInfo = [];
    config = await (0, _config.default)(_constants.PHASE_DEVELOPMENT_SERVER, dir, {
        onLoadUserConfig (userConfig) {
            const userNextConfigExperimental = (0, _config.getEnabledExperimentalFeatures)(userConfig.experimental);
            expFeatureInfo = userNextConfigExperimental.sort((a, b)=>a.length - b.length);
        }
    });
    // we need to reset env if we are going to create
    // the worker process with the esm loader so that the
    // initial env state is correct
    let envInfo = [];
    if (loadedEnvFiles.length > 0) {
        envInfo = loadedEnvFiles.map((f)=>f.path);
    }
    const isExperimentalTestProxy = args["--experimental-test-proxy"];
    if (args["--experimental-upload-trace"]) {
        traceUploadUrl = args["--experimental-upload-trace"];
    }
    const devServerOptions = {
        dir,
        port,
        allowRetry,
        isDev: true,
        hostname: host,
        isExperimentalTestProxy,
        envInfo,
        expFeatureInfo
    };
    if (args["--turbo"]) {
        process.env.TURBOPACK = "1";
    }
    if (process.env.TURBOPACK) {
        isTurboSession = true;
        await (0, _turbopackwarning.validateTurboNextConfig)({
            ...devServerOptions,
            isDev: true
        });
    }
    const distDir = _path.default.join(dir, config.distDir ?? ".next");
    (0, _shared.setGlobal)("phase", _constants.PHASE_DEVELOPMENT_SERVER);
    (0, _shared.setGlobal)("distDir", distDir);
    const startServerPath = require.resolve("../server/lib/start-server");
    async function startServer(options) {
        return new Promise((resolve)=>{
            let resolved = false;
            const defaultEnv = _env.initialEnv || process.env;
            child = (0, _child_process.fork)(startServerPath, {
                stdio: "inherit",
                env: {
                    ...defaultEnv,
                    TURBOPACK: process.env.TURBOPACK,
                    NEXT_PRIVATE_WORKER: "1",
                    NODE_EXTRA_CA_CERTS: options.selfSignedCertificate ? options.selfSignedCertificate.rootCA : defaultEnv.NODE_EXTRA_CA_CERTS
                }
            });
            child.on("message", (msg)=>{
                if (msg && typeof msg === "object") {
                    if (msg.nextWorkerReady) {
                        child == null ? void 0 : child.send({
                            nextWorkerOptions: options
                        });
                    } else if (msg.nextServerReady && !resolved) {
                        resolved = true;
                        resolve();
                    }
                }
            });
            child.on("exit", async (code, signal)=>{
                if (sessionStopHandled || signal) {
                    return;
                }
                if (code === _utils.RESTART_EXIT_CODE) {
                    return startServer(options);
                }
                await handleSessionStop(signal);
            });
        });
    }
    const runDevServer = async (reboot)=>{
        try {
            if (!!args["--experimental-https"]) {
                _log.warn("Self-signed certificates are currently an experimental feature, use at your own risk.");
                let certificate;
                const key = args["--experimental-https-key"];
                const cert = args["--experimental-https-cert"];
                const rootCA = args["--experimental-https-ca"];
                if (key && cert) {
                    certificate = {
                        key: _path.default.resolve(key),
                        cert: _path.default.resolve(cert),
                        rootCA: rootCA ? _path.default.resolve(rootCA) : undefined
                    };
                } else {
                    certificate = await (0, _mkcert.createSelfSignedCertificate)(host);
                }
                await startServer({
                    ...devServerOptions,
                    selfSignedCertificate: certificate
                });
            } else {
                await startServer(devServerOptions);
            }
            await preflight(reboot);
        } catch (err) {
            console.error(err);
            process.exit(1);
        }
    };
    await (0, _trace.trace)("start-dev-server").traceAsyncFn(async (_)=>{
        await runDevServer(false);
    });
};
function cleanup() {
    if (!child) {
        return;
    }
    child.kill("SIGTERM");
}
process.on("exit", cleanup);
process.on("SIGINT", cleanup);
process.on("SIGTERM", cleanup);

//# sourceMappingURL=next-dev.js.map