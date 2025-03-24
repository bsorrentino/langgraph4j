"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    handleRedirectResponse: null,
    handleBadRequestResponse: null,
    handleNotFoundResponse: null,
    handleMethodNotAllowedResponse: null,
    handleInternalServerErrorResponse: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    handleRedirectResponse: function() {
        return handleRedirectResponse;
    },
    handleBadRequestResponse: function() {
        return handleBadRequestResponse;
    },
    handleNotFoundResponse: function() {
        return handleNotFoundResponse;
    },
    handleMethodNotAllowedResponse: function() {
        return handleMethodNotAllowedResponse;
    },
    handleInternalServerErrorResponse: function() {
        return handleInternalServerErrorResponse;
    }
});
const _requestcookies = require("../../../web/spec-extension/adapters/request-cookies");
function handleRedirectResponse(url, mutableCookies, status) {
    const headers = new Headers({
        location: url
    });
    (0, _requestcookies.appendMutableCookies)(headers, mutableCookies);
    return new Response(null, {
        status,
        headers
    });
}
function handleBadRequestResponse() {
    return new Response(null, {
        status: 400
    });
}
function handleNotFoundResponse() {
    return new Response(null, {
        status: 404
    });
}
function handleMethodNotAllowedResponse() {
    return new Response(null, {
        status: 405
    });
}
function handleInternalServerErrorResponse() {
    return new Response(null, {
        status: 500
    });
}

//# sourceMappingURL=response-handlers.js.map