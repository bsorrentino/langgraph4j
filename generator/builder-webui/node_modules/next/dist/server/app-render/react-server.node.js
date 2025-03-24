// This file should be opted into the react-server layer
"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    decodeReply: null,
    decodeReplyFromBusboy: null,
    decodeAction: null,
    decodeFormState: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    decodeReply: function() {
        return _servernode.decodeReply;
    },
    decodeReplyFromBusboy: function() {
        return _servernode.decodeReplyFromBusboy;
    },
    decodeAction: function() {
        return _servernode.decodeAction;
    },
    decodeFormState: function() {
        return _servernode.decodeFormState;
    }
});
const _servernode = require("react-server-dom-webpack/server.node");

//# sourceMappingURL=react-server.node.js.map