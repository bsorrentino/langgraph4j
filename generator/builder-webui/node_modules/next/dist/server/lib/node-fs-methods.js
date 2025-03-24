"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "nodeFs", {
    enumerable: true,
    get: function() {
        return nodeFs;
    }
});
const _fs = /*#__PURE__*/ _interop_require_default(require("fs"));
function _interop_require_default(obj) {
    return obj && obj.__esModule ? obj : {
        default: obj
    };
}
const nodeFs = {
    readFile: _fs.default.promises.readFile,
    readFileSync: _fs.default.readFileSync,
    writeFile: (f, d)=>_fs.default.promises.writeFile(f, d),
    mkdir: (dir)=>_fs.default.promises.mkdir(dir, {
            recursive: true
        }),
    stat: (f)=>_fs.default.promises.stat(f)
};

//# sourceMappingURL=node-fs-methods.js.map