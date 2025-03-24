"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    loadManifest: null,
    clearManifestCache: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    loadManifest: function() {
        return loadManifest;
    },
    clearManifestCache: function() {
        return clearManifestCache;
    }
});
const _fs = require("fs");
const cache = new Map();
function loadManifest(path, shouldCache = true) {
    const cached = shouldCache && cache.get(path);
    if (cached) {
        return cached;
    }
    const manifest = JSON.parse((0, _fs.readFileSync)(path, "utf8"));
    if (shouldCache) {
        cache.set(path, manifest);
    }
    return manifest;
}
function clearManifestCache(path) {
    return cache.delete(path);
}

//# sourceMappingURL=load-manifest.js.map