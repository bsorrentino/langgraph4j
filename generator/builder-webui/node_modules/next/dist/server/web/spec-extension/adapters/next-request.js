"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    signalFromNodeResponse: null,
    NextRequestAdapter: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    signalFromNodeResponse: function() {
        return signalFromNodeResponse;
    },
    NextRequestAdapter: function() {
        return NextRequestAdapter;
    }
});
const _requestmeta = require("../../../request-meta");
const _utils = require("../../utils");
const _request = require("../request");
function signalFromNodeResponse(response) {
    const { errored, destroyed } = response;
    if (errored || destroyed) return AbortSignal.abort(errored);
    const controller = new AbortController();
    // If `finish` fires first, then `res.end()` has been called and the close is
    // just us finishing the stream on our side. If `close` fires first, then we
    // know the client disconnected before we finished.
    function onClose() {
        controller.abort();
        // eslint-disable-next-line @typescript-eslint/no-use-before-define
        response.off("finish", onFinish);
    }
    function onFinish() {
        response.off("close", onClose);
    }
    response.once("close", onClose);
    response.once("finish", onFinish);
    return controller.signal;
}
class NextRequestAdapter {
    static fromBaseNextRequest(request, signal) {
        // TODO: look at refining this check
        if ("request" in request && request.request) {
            return NextRequestAdapter.fromWebNextRequest(request);
        }
        return NextRequestAdapter.fromNodeNextRequest(request, signal);
    }
    static fromNodeNextRequest(request, signal) {
        // HEAD and GET requests can not have a body.
        let body = null;
        if (request.method !== "GET" && request.method !== "HEAD" && request.body) {
            // @ts-expect-error - this is handled by undici, when streams/web land use it instead
            body = request.body;
        }
        let url;
        if (request.url.startsWith("http")) {
            url = new URL(request.url);
        } else {
            // Grab the full URL from the request metadata.
            const base = (0, _requestmeta.getRequestMeta)(request, "__NEXT_INIT_URL");
            if (!base || !base.startsWith("http")) {
                // Because the URL construction relies on the fact that the URL provided
                // is absolute, we need to provide a base URL. We can't use the request
                // URL because it's relative, so we use a dummy URL instead.
                url = new URL(request.url, "http://n");
            } else {
                url = new URL(request.url, base);
            }
        }
        return new _request.NextRequest(url, {
            body,
            method: request.method,
            headers: (0, _utils.fromNodeOutgoingHttpHeaders)(request.headers),
            // @ts-expect-error - see https://github.com/whatwg/fetch/pull/1457
            duplex: "half",
            signal
        });
    }
    static fromWebNextRequest(request) {
        // HEAD and GET requests can not have a body.
        let body = null;
        if (request.method !== "GET" && request.method !== "HEAD") {
            body = request.body;
        }
        return new _request.NextRequest(request.url, {
            body,
            method: request.method,
            headers: (0, _utils.fromNodeOutgoingHttpHeaders)(request.headers),
            // @ts-expect-error - see https://github.com/whatwg/fetch/pull/1457
            duplex: "half",
            signal: request.request.signal
        });
    }
}

//# sourceMappingURL=next-request.js.map