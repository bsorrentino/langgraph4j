"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    ExportedAppPageFiles: null,
    generatePrefetchRsc: null,
    exportAppPage: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    ExportedAppPageFiles: function() {
        return ExportedAppPageFiles;
    },
    generatePrefetchRsc: function() {
        return generatePrefetchRsc;
    },
    exportAppPage: function() {
        return exportAppPage;
    }
});
const _approuterheaders = require("../../client/components/app-router-headers");
const _isdynamicusageerror = require("../helpers/is-dynamic-usage-error");
const _constants = require("../../lib/constants");
const _ciinfo = require("../../telemetry/ci-info");
const _modulerender = require("../../server/future/route-modules/app-page/module.render");
var ExportedAppPageFiles;
(function(ExportedAppPageFiles) {
    ExportedAppPageFiles["HTML"] = "HTML";
    ExportedAppPageFiles["FLIGHT"] = "FLIGHT";
    ExportedAppPageFiles["META"] = "META";
    ExportedAppPageFiles["POSTPONED"] = "POSTPONED";
})(ExportedAppPageFiles || (ExportedAppPageFiles = {}));
async function generatePrefetchRsc(req, path, res, pathname, htmlFilepath, renderOpts, fileWriter) {
    req.headers[_approuterheaders.RSC.toLowerCase()] = "1";
    req.headers[_approuterheaders.NEXT_URL.toLowerCase()] = path;
    req.headers[_approuterheaders.NEXT_ROUTER_PREFETCH.toLowerCase()] = "1";
    renderOpts.supportsDynamicHTML = true;
    renderOpts.isPrefetch = true;
    delete renderOpts.isRevalidate;
    const prefetchRenderResult = await (0, _modulerender.lazyRenderAppPage)(req, res, pathname, {}, renderOpts);
    prefetchRenderResult.pipe(res);
    await res.hasStreamed;
    const prefetchRscData = Buffer.concat(res.buffers);
    if (renderOpts.store.staticPrefetchBailout) return;
    await fileWriter("FLIGHT", htmlFilepath.replace(/\.html$/, ".prefetch.rsc"), prefetchRscData);
}
async function exportAppPage(req, res, page, path, pathname, query, renderOpts, htmlFilepath, debugOutput, isDynamicError, isAppPrefetch, fileWriter) {
    // If the page is `/_not-found`, then we should update the page to be `/404`.
    if (page === "/_not-found") {
        pathname = "/404";
    }
    try {
        if (isAppPrefetch) {
            await generatePrefetchRsc(req, path, res, pathname, htmlFilepath, renderOpts, fileWriter);
            return {
                revalidate: 0
            };
        }
        const result = await (0, _modulerender.lazyRenderAppPage)(req, res, pathname, query, renderOpts);
        const html = result.toUnchunkedString();
        const { metadata } = result;
        const flightData = metadata.pageData;
        const revalidate = metadata.revalidate ?? false;
        if (revalidate === 0) {
            if (isDynamicError) {
                throw new Error(`Page with dynamic = "error" encountered dynamic data method on ${path}.`);
            }
            if (!renderOpts.store.staticPrefetchBailout) {
                await generatePrefetchRsc(req, path, res, pathname, htmlFilepath, renderOpts, fileWriter);
            }
            const { staticBailoutInfo = {} } = metadata;
            if (revalidate === 0 && debugOutput && (staticBailoutInfo == null ? void 0 : staticBailoutInfo.description)) {
                const err = new Error(`Static generation failed due to dynamic usage on ${path}, reason: ${staticBailoutInfo.description}`);
                // Update the stack if it was provided via the bailout info.
                const { stack } = staticBailoutInfo;
                if (stack) {
                    err.stack = err.message + stack.substring(stack.indexOf("\n"));
                }
                console.warn(err);
            }
            return {
                revalidate: 0
            };
        }
        let headers;
        if (metadata.fetchTags) {
            headers = {
                [_constants.NEXT_CACHE_TAGS_HEADER]: metadata.fetchTags
            };
        }
        // Writing static HTML to a file.
        await fileWriter("HTML", htmlFilepath, html ?? "", "utf8");
        // Writing the request metadata to a file.
        const meta = {
            headers
        };
        await fileWriter("META", htmlFilepath.replace(/\.html$/, ".meta"), JSON.stringify(meta));
        // Writing the RSC payload to a file.
        await fileWriter("FLIGHT", htmlFilepath.replace(/\.html$/, ".rsc"), flightData);
        return {
            // Only include the metadata if the environment has next support.
            metadata: _ciinfo.hasNextSupport ? meta : undefined,
            revalidate
        };
    } catch (err) {
        if (!(0, _isdynamicusageerror.isDynamicUsageError)(err)) {
            throw err;
        }
        return {
            revalidate: 0
        };
    }
}

//# sourceMappingURL=app-page.js.map