"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = useThemeProps;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _system = require("@mui/system");
var _defaultTheme = _interopRequireDefault(require("./defaultTheme"));
var _identifier = _interopRequireDefault(require("./identifier"));
function useThemeProps({
  props,
  name
}) {
  return (0, _system.useThemeProps)({
    props,
    name,
    defaultTheme: (0, _extends2.default)({}, _defaultTheme.default, {
      components: {}
    }),
    themeId: _identifier.default
  });
}