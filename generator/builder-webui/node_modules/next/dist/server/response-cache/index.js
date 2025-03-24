"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function() {
        return ResponseCache;
    }
});
0 && __export(require("./types"));
const _renderresult = /*#__PURE__*/ _interop_require_default(require("../render-result"));
const _batcher = require("../../lib/batcher");
const _scheduleonnexttick = require("../lib/schedule-on-next-tick");
_export_star(require("./types"), exports);
function _export_star(from, to) {
    Object.keys(from).forEach(function(k) {
        if (k !== "default" && !Object.prototype.hasOwnProperty.call(to, k)) {
            Object.defineProperty(to, k, {
                enumerable: true,
                get: function() {
                    return from[k];
                }
            });
        }
    });
    return from;
}
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
class ResponseCache {
    constructor(minimalMode){
        this.batcher = _batcher.Batcher.create({
            // Ensure on-demand revalidate doesn't block normal requests, it should be
            // safe to run an on-demand revalidate for the same key as a normal request.
            cacheKeyFn: ({ key, isOnDemandRevalidate })=>`${key}-${isOnDemandRevalidate ? "1" : "0"}`,
            // We wait to do any async work until after we've added our promise to
            // `pendingResponses` to ensure that any any other calls will reuse the
            // same promise until we've fully finished our work.
            schedulerFn: _scheduleonnexttick.scheduleOnNextTick
        });
        // this is a hack to avoid Webpack knowing this is equal to this.minimalMode
        // because we replace this.minimalMode to true in production bundles.
        const minimalModeKey = "minimalMode";
        this[minimalModeKey] = minimalMode;
    }
    get(key, responseGenerator, context) {
        // If there is no key for the cache, we can't possibly look this up in the
        // cache so just return the result of the response generator.
        if (!key) return responseGenerator(false, null);
        const { incrementalCache, isOnDemandRevalidate = false } = context;
        return this.batcher.batch({
            key,
            isOnDemandRevalidate
        }, async (cacheKey, resolve)=>{
            var _this_previousCacheItem;
            // We keep the previous cache entry around to leverage when the
            // incremental cache is disabled in minimal mode.
            if (this.minimalMode && ((_this_previousCacheItem = this.previousCacheItem) == null ? void 0 : _this_previousCacheItem.key) === cacheKey && this.previousCacheItem.expiresAt > Date.now()) {
                return this.previousCacheItem.entry;
            }
            let resolved = false;
            let cachedResponse = null;
            try {
                cachedResponse = !this.minimalMode ? await incrementalCache.get(key) : null;
                if (cachedResponse && !isOnDemandRevalidate) {
                    var _cachedResponse_value, _cachedResponse_value1;
                    if (((_cachedResponse_value = cachedResponse.value) == null ? void 0 : _cachedResponse_value.kind) === "FETCH") {
                        throw new Error(`invariant: unexpected cachedResponse of kind fetch in response cache`);
                    }
                    resolve({
                        isStale: cachedResponse.isStale,
                        revalidate: cachedResponse.curRevalidate,
                        value: ((_cachedResponse_value1 = cachedResponse.value) == null ? void 0 : _cachedResponse_value1.kind) === "PAGE" ? {
                            kind: "PAGE",
                            html: _renderresult.default.fromStatic(cachedResponse.value.html),
                            pageData: cachedResponse.value.pageData,
                            headers: cachedResponse.value.headers,
                            status: cachedResponse.value.status
                        } : cachedResponse.value
                    });
                    resolved = true;
                    if (!cachedResponse.isStale || context.isPrefetch) {
                        // The cached value is still valid, so we don't need
                        // to update it yet.
                        return null;
                    }
                }
                const cacheEntry = await responseGenerator(resolved, cachedResponse);
                const resolveValue = cacheEntry === null ? null : {
                    ...cacheEntry,
                    isMiss: !cachedResponse
                };
                // For on-demand revalidate wait to resolve until cache is set.
                // Otherwise resolve now.
                if (!isOnDemandRevalidate && !resolved) {
                    resolve(resolveValue);
                    resolved = true;
                }
                if (cacheEntry && typeof cacheEntry.revalidate !== "undefined") {
                    if (this.minimalMode) {
                        this.previousCacheItem = {
                            key: cacheKey,
                            entry: cacheEntry,
                            expiresAt: Date.now() + 1000
                        };
                    } else {
                        var _cacheEntry_value;
                        await incrementalCache.set(key, ((_cacheEntry_value = cacheEntry.value) == null ? void 0 : _cacheEntry_value.kind) === "PAGE" ? {
                            kind: "PAGE",
                            html: cacheEntry.value.html.toUnchunkedString(),
                            pageData: cacheEntry.value.pageData,
                            headers: cacheEntry.value.headers,
                            status: cacheEntry.value.status
                        } : cacheEntry.value, {
                            revalidate: cacheEntry.revalidate
                        });
                    }
                } else {
                    this.previousCacheItem = undefined;
                }
                return resolveValue;
            } catch (err) {
                // When a getStaticProps path is erroring we automatically re-set the
                // existing cache under a new expiration to prevent non-stop retrying.
                if (cachedResponse) {
                    await incrementalCache.set(key, cachedResponse.value, {
                        revalidate: Math.min(Math.max(cachedResponse.revalidate || 3, 3), 30)
                    });
                }
                // While revalidating in the background we can't reject as we already
                // resolved the cache entry so log the error here.
                if (resolved) {
                    console.error(err);
                    return null;
                }
                // We haven't resolved yet, so let's throw to indicate an error.
                throw err;
            }
        });
    }
}

//# sourceMappingURL=index.js.map