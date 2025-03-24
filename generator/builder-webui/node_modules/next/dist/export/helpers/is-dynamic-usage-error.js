"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "isDynamicUsageError", {
    enumerable: true,
    get: function() {
        return isDynamicUsageError;
    }
});
const _hooksservercontext = require("../../client/components/hooks-server-context");
const _notfound = require("../../client/components/not-found");
const _redirect = require("../../client/components/redirect");
const _nossrerror = require("../../shared/lib/lazy-dynamic/no-ssr-error");
const isDynamicUsageError = (err)=>err.digest === _hooksservercontext.DYNAMIC_ERROR_CODE || (0, _notfound.isNotFoundError)(err) || err.digest === _nossrerror.NEXT_DYNAMIC_NO_SSR_CODE || (0, _redirect.isRedirectError)(err);

//# sourceMappingURL=is-dynamic-usage-error.js.map