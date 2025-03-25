"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    CacheHandler: null,
    IncrementalCache: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    CacheHandler: function() {
        return CacheHandler;
    },
    IncrementalCache: function() {
        return IncrementalCache;
    }
});
const _fetchcache = /*#__PURE__*/ _interop_require_default(require("./fetch-cache"));
const _filesystemcache = /*#__PURE__*/ _interop_require_default(require("./file-system-cache"));
const _path = /*#__PURE__*/ _interop_require_default(require("../../../shared/lib/isomorphic/path"));
const _encodedecode = require("../../stream-utils/encode-decode");
const _base64arraybuffer = require("../../../shared/lib/base64-arraybuffer");
const _normalizepagepath = require("../../../shared/lib/page-path/normalize-page-path");
const _constants = require("../../../lib/constants");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
function toRoute(pathname) {
    return pathname.replace(/\/$/, "").replace(/\/index$/, "") || "/";
}
class CacheHandler {
    // eslint-disable-next-line
    constructor(_ctx){}
    async get(..._args) {
        return {};
    }
    async set(..._args) {}
    async revalidateTag(_tag) {}
}
class IncrementalCache {
    constructor({ fs, dev, appDir, flushToDisk, fetchCache, minimalMode, serverDistDir, requestHeaders, requestProtocol, maxMemoryCacheSize, getPrerenderManifest, fetchCacheKeyPrefix, CurCacheHandler, allowedRevalidateHeaderKeys }){
        var _this_prerenderManifest_preview, _this_prerenderManifest, _this_prerenderManifest_preview1, _this_prerenderManifest1;
        this.locks = new Map();
        this.unlocks = new Map();
        const debug = !!process.env.NEXT_PRIVATE_DEBUG_CACHE;
        if (!CurCacheHandler) {
            if (fs && serverDistDir) {
                if (debug) {
                    console.log("using filesystem cache handler");
                }
                CurCacheHandler = _filesystemcache.default;
            }
            if (_fetchcache.default.isAvailable({
                _requestHeaders: requestHeaders
            }) && minimalMode && fetchCache) {
                if (debug) {
                    console.log("using fetch cache handler");
                }
                CurCacheHandler = _fetchcache.default;
            }
        } else if (debug) {
            console.log("using custom cache handler", CurCacheHandler.name);
        }
        if (process.env.__NEXT_TEST_MAX_ISR_CACHE) {
            // Allow cache size to be overridden for testing purposes
            maxMemoryCacheSize = parseInt(process.env.__NEXT_TEST_MAX_ISR_CACHE, 10);
        }
        this.dev = dev;
        // this is a hack to avoid Webpack knowing this is equal to this.minimalMode
        // because we replace this.minimalMode to true in production bundles.
        const minimalModeKey = "minimalMode";
        this[minimalModeKey] = minimalMode;
        this.requestHeaders = requestHeaders;
        this.requestProtocol = requestProtocol;
        this.allowedRevalidateHeaderKeys = allowedRevalidateHeaderKeys;
        this.prerenderManifest = getPrerenderManifest();
        this.fetchCacheKeyPrefix = fetchCacheKeyPrefix;
        let revalidatedTags = [];
        if (requestHeaders[_constants.PRERENDER_REVALIDATE_HEADER] === ((_this_prerenderManifest = this.prerenderManifest) == null ? void 0 : (_this_prerenderManifest_preview = _this_prerenderManifest.preview) == null ? void 0 : _this_prerenderManifest_preview.previewModeId)) {
            this.isOnDemandRevalidate = true;
        }
        if (minimalMode && typeof requestHeaders[_constants.NEXT_CACHE_REVALIDATED_TAGS_HEADER] === "string" && requestHeaders[_constants.NEXT_CACHE_REVALIDATE_TAG_TOKEN_HEADER] === ((_this_prerenderManifest1 = this.prerenderManifest) == null ? void 0 : (_this_prerenderManifest_preview1 = _this_prerenderManifest1.preview) == null ? void 0 : _this_prerenderManifest_preview1.previewModeId)) {
            revalidatedTags = requestHeaders[_constants.NEXT_CACHE_REVALIDATED_TAGS_HEADER].split(",");
        }
        if (CurCacheHandler) {
            this.cacheHandler = new CurCacheHandler({
                dev,
                fs,
                flushToDisk,
                serverDistDir,
                revalidatedTags,
                maxMemoryCacheSize,
                _appDir: !!appDir,
                _requestHeaders: requestHeaders,
                fetchCacheKeyPrefix
            });
        }
    }
    calculateRevalidate(pathname, fromTime, dev) {
        // in development we don't have a prerender-manifest
        // and default to always revalidating to allow easier debugging
        if (dev) return new Date().getTime() - 1000;
        // if an entry isn't present in routes we fallback to a default
        // of revalidating after 1 second
        const { initialRevalidateSeconds } = this.prerenderManifest.routes[toRoute(pathname)] || {
            initialRevalidateSeconds: 1
        };
        const revalidateAfter = typeof initialRevalidateSeconds === "number" ? initialRevalidateSeconds * 1000 + fromTime : initialRevalidateSeconds;
        return revalidateAfter;
    }
    _getPathname(pathname, fetchCache) {
        return fetchCache ? pathname : (0, _normalizepagepath.normalizePagePath)(pathname);
    }
    async unlock(cacheKey) {
        const unlock = this.unlocks.get(cacheKey);
        if (unlock) {
            unlock();
            this.locks.delete(cacheKey);
            this.unlocks.delete(cacheKey);
        }
    }
    async lock(cacheKey) {
        if (process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT && process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY && process.env.NEXT_RUNTIME !== "edge") {
            const invokeIpcMethod = require("../server-ipc/request-utils").invokeIpcMethod;
            await invokeIpcMethod({
                method: "lock",
                ipcPort: process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT,
                ipcKey: process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY,
                args: [
                    cacheKey
                ]
            });
            return async ()=>{
                await invokeIpcMethod({
                    method: "unlock",
                    ipcPort: process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT,
                    ipcKey: process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY,
                    args: [
                        cacheKey
                    ]
                });
            };
        }
        let unlockNext = ()=>Promise.resolve();
        const existingLock = this.locks.get(cacheKey);
        if (existingLock) {
            await existingLock;
        } else {
            const newLock = new Promise((resolve)=>{
                unlockNext = async ()=>{
                    resolve();
                };
            });
            this.locks.set(cacheKey, newLock);
            this.unlocks.set(cacheKey, unlockNext);
        }
        return unlockNext;
    }
    async revalidateTag(tag) {
        var _this_cacheHandler_revalidateTag, _this_cacheHandler;
        if (process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT && process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY && process.env.NEXT_RUNTIME !== "edge") {
            const invokeIpcMethod = require("../server-ipc/request-utils").invokeIpcMethod;
            return invokeIpcMethod({
                method: "revalidateTag",
                ipcPort: process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT,
                ipcKey: process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY,
                args: [
                    ...arguments
                ]
            });
        }
        return (_this_cacheHandler = this.cacheHandler) == null ? void 0 : (_this_cacheHandler_revalidateTag = _this_cacheHandler.revalidateTag) == null ? void 0 : _this_cacheHandler_revalidateTag.call(_this_cacheHandler, tag);
    }
    // x-ref: https://github.com/facebook/react/blob/2655c9354d8e1c54ba888444220f63e836925caa/packages/react/src/ReactFetch.js#L23
    async fetchCacheKey(url, init = {}) {
        // this should be bumped anytime a fix is made to cache entries
        // that should bust the cache
        const MAIN_KEY_PREFIX = "v3";
        let cacheKey;
        const bodyChunks = [];
        if (init.body) {
            // handle ReadableStream body
            if (typeof init.body.getReader === "function") {
                const readableBody = init.body;
                const reader = readableBody.getReader();
                let arrayBuffer = new Uint8Array();
                function processValue({ done, value }) {
                    if (done) {
                        return;
                    }
                    if (value) {
                        try {
                            bodyChunks.push(typeof value === "string" ? value : (0, _base64arraybuffer.encode)(value));
                            const curBuffer = typeof value === "string" ? (0, _encodedecode.encodeText)(value) : new Uint8Array(value);
                            const prevBuffer = arrayBuffer;
                            arrayBuffer = new Uint8Array(prevBuffer.byteLength + curBuffer.byteLength);
                            arrayBuffer.set(prevBuffer);
                            arrayBuffer.set(curBuffer, prevBuffer.byteLength);
                        } catch (err) {
                            console.error(err);
                        }
                    }
                    reader.read().then(processValue);
                }
                await reader.read().then(processValue);
                init._ogBody = arrayBuffer;
            } else if (typeof init.body.keys === "function") {
                const formData = init.body;
                init._ogBody = init.body;
                for (const key of new Set([
                    ...formData.keys()
                ])){
                    const values = formData.getAll(key);
                    bodyChunks.push(`${key}=${(await Promise.all(values.map(async (val)=>{
                        if (typeof val === "string") {
                            return val;
                        } else {
                            return await val.text();
                        }
                    }))).join(",")}`);
                }
            // handle blob body
            } else if (typeof init.body.arrayBuffer === "function") {
                const blob = init.body;
                const arrayBuffer = await blob.arrayBuffer();
                bodyChunks.push((0, _base64arraybuffer.encode)(await init.body.arrayBuffer()));
                init._ogBody = new Blob([
                    arrayBuffer
                ], {
                    type: blob.type
                });
            } else if (typeof init.body === "string") {
                bodyChunks.push(init.body);
                init._ogBody = init.body;
            }
        }
        const cacheString = JSON.stringify([
            MAIN_KEY_PREFIX,
            this.fetchCacheKeyPrefix || "",
            url,
            init.method,
            typeof (init.headers || {}).keys === "function" ? Object.fromEntries(init.headers) : init.headers,
            init.mode,
            init.redirect,
            init.credentials,
            init.referrer,
            init.referrerPolicy,
            init.integrity,
            init.cache,
            bodyChunks
        ]);
        if (process.env.NEXT_RUNTIME === "edge") {
            function bufferToHex(buffer) {
                return Array.prototype.map.call(new Uint8Array(buffer), (b)=>b.toString(16).padStart(2, "0")).join("");
            }
            const buffer = (0, _encodedecode.encodeText)(cacheString);
            cacheKey = bufferToHex(await crypto.subtle.digest("SHA-256", buffer));
        } else {
            const crypto1 = require("crypto");
            cacheKey = crypto1.createHash("sha256").update(cacheString).digest("hex");
        }
        return cacheKey;
    }
    // get data from cache if available
    async get(cacheKey, ctx = {}) {
        var _this_cacheHandler, _cacheData_value, _this_prerenderManifest_routes_toRoute;
        if (process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT && process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY && process.env.NEXT_RUNTIME !== "edge") {
            const invokeIpcMethod = require("../server-ipc/request-utils").invokeIpcMethod;
            return invokeIpcMethod({
                method: "get",
                ipcPort: process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT,
                ipcKey: process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY,
                args: [
                    ...arguments
                ]
            });
        }
        // we don't leverage the prerender cache in dev mode
        // so that getStaticProps is always called for easier debugging
        if (this.dev && (!ctx.fetchCache || this.requestHeaders["cache-control"] === "no-cache")) {
            return null;
        }
        cacheKey = this._getPathname(cacheKey, ctx.fetchCache);
        let entry = null;
        let revalidate = ctx.revalidate;
        const cacheData = await ((_this_cacheHandler = this.cacheHandler) == null ? void 0 : _this_cacheHandler.get(cacheKey, ctx));
        if ((cacheData == null ? void 0 : (_cacheData_value = cacheData.value) == null ? void 0 : _cacheData_value.kind) === "FETCH") {
            const combinedTags = [
                ...ctx.tags || [],
                ...ctx.softTags || []
            ];
            // if a tag was revalidated we don't return stale data
            if (combinedTags.some((tag)=>{
                var _this_revalidatedTags;
                return (_this_revalidatedTags = this.revalidatedTags) == null ? void 0 : _this_revalidatedTags.includes(tag);
            })) {
                return null;
            }
            revalidate = revalidate || cacheData.value.revalidate;
            const age = Math.round((Date.now() - (cacheData.lastModified || 0)) / 1000);
            const isStale = age > revalidate;
            const data = cacheData.value.data;
            return {
                isStale: isStale,
                value: {
                    kind: "FETCH",
                    data,
                    revalidate: revalidate
                },
                revalidateAfter: Date.now() + revalidate * 1000
            };
        }
        const curRevalidate = (_this_prerenderManifest_routes_toRoute = this.prerenderManifest.routes[toRoute(cacheKey)]) == null ? void 0 : _this_prerenderManifest_routes_toRoute.initialRevalidateSeconds;
        let isStale;
        let revalidateAfter;
        if ((cacheData == null ? void 0 : cacheData.lastModified) === -1) {
            isStale = -1;
            revalidateAfter = -1 * _constants.CACHE_ONE_YEAR;
        } else {
            revalidateAfter = this.calculateRevalidate(cacheKey, (cacheData == null ? void 0 : cacheData.lastModified) || Date.now(), this.dev && !ctx.fetchCache);
            isStale = revalidateAfter !== false && revalidateAfter < Date.now() ? true : undefined;
        }
        if (cacheData) {
            entry = {
                isStale,
                curRevalidate,
                revalidateAfter,
                value: cacheData.value
            };
        }
        if (!cacheData && this.prerenderManifest.notFoundRoutes.includes(cacheKey)) {
            // for the first hit after starting the server the cache
            // may not have a way to save notFound: true so if
            // the prerender-manifest marks this as notFound then we
            // return that entry and trigger a cache set to give it a
            // chance to update in-memory entries
            entry = {
                isStale,
                value: null,
                curRevalidate,
                revalidateAfter
            };
            this.set(cacheKey, entry.value, ctx);
        }
        return entry;
    }
    // populate the incremental cache with new data
    async set(pathname, data, ctx) {
        if (process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT && process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY && process.env.NEXT_RUNTIME !== "edge") {
            const invokeIpcMethod = require("../server-ipc/request-utils").invokeIpcMethod;
            return invokeIpcMethod({
                method: "set",
                ipcPort: process.env.__NEXT_INCREMENTAL_CACHE_IPC_PORT,
                ipcKey: process.env.__NEXT_INCREMENTAL_CACHE_IPC_KEY,
                args: [
                    ...arguments
                ]
            });
        }
        if (this.dev && !ctx.fetchCache) return;
        // fetchCache has upper limit of 2MB per-entry currently
        if (ctx.fetchCache && JSON.stringify(data).length > 2 * 1024 * 1024) {
            if (this.dev) {
                throw new Error(`fetch for over 2MB of data can not be cached`);
            }
            return;
        }
        pathname = this._getPathname(pathname, ctx.fetchCache);
        try {
            var _this_cacheHandler;
            // we use the prerender manifest memory instance
            // to store revalidate timings for calculating
            // revalidateAfter values so we update this on set
            if (typeof ctx.revalidate !== "undefined" && !ctx.fetchCache) {
                this.prerenderManifest.routes[pathname] = {
                    dataRoute: _path.default.posix.join("/_next/data", `${(0, _normalizepagepath.normalizePagePath)(pathname)}.json`),
                    srcRoute: null,
                    initialRevalidateSeconds: ctx.revalidate
                };
            }
            await ((_this_cacheHandler = this.cacheHandler) == null ? void 0 : _this_cacheHandler.set(pathname, data, ctx));
        } catch (error) {
            console.warn("Failed to update prerender cache for", pathname, error);
        }
    }
}

//# sourceMappingURL=index.js.map