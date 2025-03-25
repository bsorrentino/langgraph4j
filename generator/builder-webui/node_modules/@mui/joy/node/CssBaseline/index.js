"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {};
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _CssBaseline.default;
  }
});
var _CssBaseline = _interopRequireDefault(require("./CssBaseline"));
var _CssBaselineProps = require("./CssBaselineProps");
Object.keys(_CssBaselineProps).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _CssBaselineProps[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _CssBaselineProps[key];
    }
  });
});