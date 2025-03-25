"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "createIncrementalCache", {
    enumerable: true,
    get: function() {
        return createIncrementalCache;
    }
});
const _path = /*#__PURE__*/ _interop_require_default(require("path"));
const _fs = /*#__PURE__*/ _interop_require_default(require("fs"));
const _incrementalcache = require("../../server/lib/incremental-cache");
const _ciinfo = require("../../telemetry/ci-info");
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
function createIncrementalCache(incrementalCacheHandlerPath, isrMemoryCacheSize, fetchCacheKeyPrefix, distDir) {
    // Custom cache handler overrides.
    let CacheHandler;
    if (incrementalCacheHandlerPath) {
        CacheHandler = require(incrementalCacheHandlerPath);
        CacheHandler = CacheHandler.default || CacheHandler;
    }
    const incrementalCache = new _incrementalcache.IncrementalCache({
        dev: false,
        requestHeaders: {},
        flushToDisk: true,
        fetchCache: true,
        maxMemoryCacheSize: isrMemoryCacheSize,
        fetchCacheKeyPrefix,
        getPrerenderManifest: ()=>({
                version: 4,
                routes: {},
                dynamicRoutes: {},
                preview: {
                    previewModeEncryptionKey: "",
                    previewModeId: "",
                    previewModeSigningKey: ""
                },
                notFoundRoutes: []
            }),
        fs: {
            readFile: _fs.default.promises.readFile,
            readFileSync: _fs.default.readFileSync,
            writeFile: (f, d)=>_fs.default.promises.writeFile(f, d),
            mkdir: (dir)=>_fs.default.promises.mkdir(dir, {
                    recursive: true
                }),
            stat: (f)=>_fs.default.promises.stat(f)
        },
        serverDistDir: _path.default.join(distDir, "server"),
        CurCacheHandler: CacheHandler,
        minimalMode: _ciinfo.hasNextSupport
    });
    globalThis.__incrementalCache = incrementalCache;
    return incrementalCache;
}

//# sourceMappingURL=create-incremental-cache.js.map