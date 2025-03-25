"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "getRedirectStatusCodeFromError", {
    enumerable: true,
    get: function() {
        return getRedirectStatusCodeFromError;
    }
});
const _redirect = require("./redirect");
function getRedirectStatusCodeFromError(error) {
    if (!(0, _redirect.isRedirectError)(error)) {
        throw new Error("Not a redirect error");
    }
    return error.digest.split(";", 4)[3] === "true" ? 308 : 307;
}

if ((typeof exports.default === 'function' || (typeof exports.default === 'object' && exports.default !== null)) && typeof exports.default.__esModule === 'undefined') {
  Object.defineProperty(exports.default, '__esModule', { value: true });
  Object.assign(exports.default, exports);
  module.exports = exports.default;
}

//# sourceMappingURL=get-redirect-status-code-from-error.js.map