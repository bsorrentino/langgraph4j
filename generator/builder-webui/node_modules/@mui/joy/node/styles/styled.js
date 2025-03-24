"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _system = require("@mui/system");
var _defaultTheme = _interopRequireDefault(require("./defaultTheme"));
var _identifier = _interopRequireDefault(require("./identifier"));
const styled = (0, _system.createStyled)({
  defaultTheme: _defaultTheme.default,
  themeId: _identifier.default
});
var _default = exports.default = styled;