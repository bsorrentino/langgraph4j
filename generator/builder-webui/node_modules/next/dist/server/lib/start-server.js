"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    getRequestHandlers: null,
    startServer: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    getRequestHandlers: function() {
        return getRequestHandlers;
    },
    startServer: function() {
        return startServer;
    }
});
require("../next");
require("../node-polyfill-fetch");
require("../require-hook");
const _fs = /*#__PURE__*/ _interop_require_default(require("fs"));
const _path = /*#__PURE__*/ _interop_require_default(require("path"));
const _http = /*#__PURE__*/ _interop_require_default(require("http"));
const _https = /*#__PURE__*/ _interop_require_default(require("https"));
const _watchpack = /*#__PURE__*/ _interop_require_default(require("watchpack"));
const _log = /*#__PURE__*/ _interop_require_wildcard(require("../../build/output/log"));
const _debug = /*#__PURE__*/ _interop_require_default(require("next/dist/compiled/debug"));
const _utils = require("./utils");
const _formathostname = require("./format-hostname");
const _routerserver = require("./router-server");
const _isnodedebugging = require("./is-node-debugging");
const _constants = require("../../shared/lib/constants");
const _picocolors = require("../../lib/picocolors");
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
if (performance.getEntriesByName("next-start").length === 0) {
    performance.mark("next-start");
}
const debug = (0, _debug.default)("next:start-server");
async function getRequestHandlers({ dir, port, isDev, server, hostname, minimalMode, isNodeDebugging, keepAliveTimeout, experimentalTestProxy, experimentalHttpsServer }) {
    return (0, _routerserver.initialize)({
        dir,
        port,
        hostname,
        dev: isDev,
        minimalMode,
        server,
        isNodeDebugging: isNodeDebugging || false,
        keepAliveTimeout,
        experimentalTestProxy,
        experimentalHttpsServer
    });
}
function logStartInfo({ networkUrl, appUrl, hostname, envInfo, expFeatureInfo, formatDurationText }) {
    _log.bootstrap((0, _picocolors.bold)((0, _picocolors.purple)(`${_log.prefixes.ready} Next.js ${"13.5.6"}`)));
    _log.bootstrap(`- Local:        ${appUrl}`);
    if (hostname) {
        _log.bootstrap(`- Network:      ${networkUrl}`);
    }
    if (envInfo == null ? void 0 : envInfo.length) _log.bootstrap(`- Environments: ${envInfo.join(", ")}`);
    if (expFeatureInfo == null ? void 0 : expFeatureInfo.length) {
        _log.bootstrap(`- Experiments (use at your own risk):`);
        // only show maximum 3 flags
        for (const exp of expFeatureInfo.slice(0, 3)){
            _log.bootstrap(`   · ${exp}`);
        }
        /* ${expFeatureInfo.length - 3} more */ if (expFeatureInfo.length > 3) {
            _log.bootstrap(`   · ...`);
        }
    }
    // New line after the bootstrap info
    _log.info("");
    _log.event(`Ready in ${formatDurationText}`);
}
async function startServer({ dir, port, isDev, hostname, minimalMode, allowRetry, keepAliveTimeout, isExperimentalTestProxy, selfSignedCertificate, envInfo, expFeatureInfo }) {
    let handlersReady = ()=>{};
    let handlersError = ()=>{};
    let handlersPromise = new Promise((resolve, reject)=>{
        handlersReady = resolve;
        handlersError = reject;
    });
    let requestHandler = async (req, res)=>{
        if (handlersPromise) {
            await handlersPromise;
            return requestHandler(req, res);
        }
        throw new Error("Invariant request handler was not setup");
    };
    let upgradeHandler = async (req, socket, head)=>{
        if (handlersPromise) {
            await handlersPromise;
            return upgradeHandler(req, socket, head);
        }
        throw new Error("Invariant upgrade handler was not setup");
    };
    // setup server listener as fast as possible
    if (selfSignedCertificate && !isDev) {
        throw new Error("Using a self signed certificate is only supported with `next dev`.");
    }
    async function requestListener(req, res) {
        try {
            if (handlersPromise) {
                await handlersPromise;
                handlersPromise = undefined;
            }
            await requestHandler(req, res);
        } catch (err) {
            res.statusCode = 500;
            res.end("Internal Server Error");
            _log.error(`Failed to handle request for ${req.url}`);
            console.error(err);
        }
    }
    const server = selfSignedCertificate ? _https.default.createServer({
        key: _fs.default.readFileSync(selfSignedCertificate.key),
        cert: _fs.default.readFileSync(selfSignedCertificate.cert)
    }, requestListener) : _http.default.createServer(requestListener);
    if (keepAliveTimeout) {
        server.keepAliveTimeout = keepAliveTimeout;
    }
    server.on("upgrade", async (req, socket, head)=>{
        try {
            await upgradeHandler(req, socket, head);
        } catch (err) {
            socket.destroy();
            _log.error(`Failed to handle request for ${req.url}`);
            console.error(err);
        }
    });
    let portRetryCount = 0;
    server.on("error", (err)=>{
        if (allowRetry && port && isDev && err.code === "EADDRINUSE" && portRetryCount < 10) {
            _log.warn(`Port ${port} is in use, trying ${port + 1} instead.`);
            port += 1;
            portRetryCount += 1;
            server.listen(port, hostname);
        } else {
            _log.error(`Failed to start server`);
            console.error(err);
            process.exit(1);
        }
    });
    const isNodeDebugging = (0, _isnodedebugging.checkIsNodeDebugging)();
    await new Promise((resolve)=>{
        server.on("listening", async ()=>{
            const addr = server.address();
            const actualHostname = (0, _formathostname.formatHostname)(typeof addr === "object" ? (addr == null ? void 0 : addr.address) || hostname || "localhost" : addr);
            const formattedHostname = !hostname || actualHostname === "0.0.0.0" ? "localhost" : actualHostname === "[::]" ? "[::1]" : (0, _formathostname.formatHostname)(hostname);
            port = typeof addr === "object" ? (addr == null ? void 0 : addr.port) || port : port;
            const networkUrl = `http://${actualHostname}:${port}`;
            const appUrl = `${selfSignedCertificate ? "https" : "http"}://${formattedHostname}:${port}`;
            if (isNodeDebugging) {
                const debugPort = (0, _utils.getDebugPort)();
                _log.info(`the --inspect${isNodeDebugging === "brk" ? "-brk" : ""} option was detected, the Next.js router server should be inspected at port ${debugPort}.`);
            }
            // expose the main port to render workers
            process.env.PORT = port + "";
            try {
                const cleanup = (code)=>{
                    debug("start-server process cleanup");
                    server.close();
                    process.exit(code ?? 0);
                };
                const exception = (err)=>{
                    // This is the render worker, we keep the process alive
                    console.error(err);
                };
                process.on("exit", (code)=>cleanup(code));
                // callback value is signal string, exit with 0
                process.on("SIGINT", ()=>cleanup(0));
                process.on("SIGTERM", ()=>cleanup(0));
                process.on("uncaughtException", exception);
                process.on("unhandledRejection", exception);
                const initResult = await getRequestHandlers({
                    dir,
                    port,
                    isDev,
                    server,
                    hostname,
                    minimalMode,
                    isNodeDebugging: Boolean(isNodeDebugging),
                    keepAliveTimeout,
                    experimentalTestProxy: !!isExperimentalTestProxy,
                    experimentalHttpsServer: !!selfSignedCertificate
                });
                requestHandler = initResult[0];
                upgradeHandler = initResult[1];
                const startServerProcessDuration = performance.mark("next-start-end") && performance.measure("next-start-duration", "next-start", "next-start-end").duration;
                const formatDurationText = startServerProcessDuration > 2000 ? `${Math.round(startServerProcessDuration / 100) / 10}s` : `${Math.round(startServerProcessDuration)}ms`;
                handlersReady();
                logStartInfo({
                    networkUrl,
                    appUrl,
                    hostname,
                    envInfo,
                    expFeatureInfo,
                    formatDurationText
                });
            } catch (err) {
                // fatal error if we can't setup
                handlersError();
                console.error(err);
                process.exit(1);
            }
            resolve();
        });
        server.listen(port, hostname);
    });
    if (isDev) {
        function watchConfigFiles(dirToWatch, onChange) {
            const wp = new _watchpack.default();
            wp.watch({
                files: _constants.CONFIG_FILES.map((file)=>_path.default.join(dirToWatch, file))
            });
            wp.on("change", onChange);
        }
        watchConfigFiles(dir, async (filename)=>{
            if (process.env.__NEXT_DISABLE_MEMORY_WATCHER) {
                _log.info(`Detected change, manual restart required due to '__NEXT_DISABLE_MEMORY_WATCHER' usage`);
                return;
            }
            _log.warn(`Found a change in ${_path.default.basename(filename)}. Restarting the server to apply the changes...`);
            process.exit(_utils.RESTART_EXIT_CODE);
        });
    }
}
if (process.env.NEXT_PRIVATE_WORKER && process.send) {
    process.addListener("message", async (msg)=>{
        if (msg && typeof msg && msg.nextWorkerOptions && process.send) {
            await startServer(msg.nextWorkerOptions);
            process.send({
                nextServerReady: true
            });
        }
    });
    process.send({
        nextWorkerReady: true
    });
}

//# sourceMappingURL=start-server.js.map