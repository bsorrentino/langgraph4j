"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "flatReaddir", {
    enumerable: true,
    get: function() {
        return flatReaddir;
    }
});
const _path = require("path");
const _promises = /*#__PURE__*/ _interop_require_default(require("fs/promises"));
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
async function flatReaddir(dir, includes) {
    const dirents = await _promises.default.opendir(dir);
    const result = [];
    for await (const part of dirents){
        let shouldOmit = part.isDirectory() || !includes.some((include)=>include.test(part.name));
        if (part.isSymbolicLink()) {
            const stats = await _promises.default.stat((0, _path.join)(dir, part.name));
            shouldOmit = stats.isDirectory();
        }
        if (!shouldOmit) {
            result.push((0, _path.join)(dir, part.name));
        }
    }
    return result;
}

//# sourceMappingURL=flat-readdir.js.map