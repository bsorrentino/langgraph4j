"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    stripInternalQueries: null,
    stripInternalSearchParams: null,
    stripInternalHeaders: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    stripInternalQueries: function() {
        return stripInternalQueries;
    },
    stripInternalSearchParams: function() {
        return stripInternalSearchParams;
    },
    stripInternalHeaders: function() {
        return stripInternalHeaders;
    }
});
const _approuterheaders = require("../client/components/app-router-headers");
const INTERNAL_QUERY_NAMES = [
    "__nextFallback",
    "__nextLocale",
    "__nextInferredLocaleFromDefault",
    "__nextDefaultLocale",
    "__nextIsNotFound",
    _approuterheaders.NEXT_RSC_UNION_QUERY
];
const EDGE_EXTENDED_INTERNAL_QUERY_NAMES = [
    "__nextDataReq"
];
function stripInternalQueries(query) {
    for (const name of INTERNAL_QUERY_NAMES){
        delete query[name];
    }
}
function stripInternalSearchParams(url, isEdge) {
    const isStringUrl = typeof url === "string";
    const instance = isStringUrl ? new URL(url) : url;
    for (const name of INTERNAL_QUERY_NAMES){
        instance.searchParams.delete(name);
    }
    if (isEdge) {
        for (const name of EDGE_EXTENDED_INTERNAL_QUERY_NAMES){
            instance.searchParams.delete(name);
        }
    }
    return isStringUrl ? instance.toString() : instance;
}
/**
 * Headers that are set by the Next.js server and should be stripped from the
 * request headers going to the user's application.
 */ const INTERNAL_HEADERS = [
    "x-invoke-path",
    "x-invoke-status",
    "x-invoke-error",
    "x-invoke-query",
    "x-invoke-output",
    "x-middleware-invoke"
];
function stripInternalHeaders(headers) {
    for (const key of INTERNAL_HEADERS){
        delete headers[key];
    }
}

//# sourceMappingURL=internal-utils.js.map