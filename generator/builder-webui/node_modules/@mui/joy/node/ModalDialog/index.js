"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  modalDialogClasses: true
};
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _ModalDialog.default;
  }
});
Object.defineProperty(exports, "modalDialogClasses", {
  enumerable: true,
  get: function () {
    return _modalDialogClasses.default;
  }
});
var _ModalDialog = _interopRequireDefault(require("./ModalDialog"));
var _modalDialogClasses = _interopRequireWildcard(require("./modalDialogClasses"));
Object.keys(_modalDialogClasses).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _modalDialogClasses[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _modalDialogClasses[key];
    }
  });
});
var _ModalDialogProps = require("./ModalDialogProps");
Object.keys(_ModalDialogProps).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _ModalDialogProps[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _ModalDialogProps[key];
    }
  });
});
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }