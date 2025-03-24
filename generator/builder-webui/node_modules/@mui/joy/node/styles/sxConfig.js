"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _system = require("@mui/system");
const sxConfig = (0, _extends2.default)({}, _system.unstable_defaultSxConfig, {
  // The default system themeKey is shape
  borderRadius: {
    themeKey: 'radius'
  },
  // The default system themeKey is shadows
  boxShadow: {
    themeKey: 'shadow'
  },
  // The default system themeKey is typography
  fontFamily: {
    themeKey: 'fontFamily'
  },
  // The default system themeKey is typography
  fontSize: {
    themeKey: 'fontSize'
  },
  // The default system themeKey is typography
  fontWeight: {
    themeKey: 'fontWeight'
  },
  // The default system themeKey is typography
  letterSpacing: {
    themeKey: 'letterSpacing'
  },
  // The default system themeKey is typography
  lineHeight: {
    themeKey: 'lineHeight'
  }
});
var _default = exports.default = sxConfig;