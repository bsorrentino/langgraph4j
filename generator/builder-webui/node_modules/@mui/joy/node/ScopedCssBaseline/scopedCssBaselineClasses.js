"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getScopedCssBaselineUtilityClass = getScopedCssBaselineUtilityClass;
var _className = require("../className");
function getScopedCssBaselineUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiScopedCssBaseline', slot);
}
const scopedCssBaselineClasses = (0, _className.generateUtilityClasses)('MuiScopedCssBaseline', ['root']);
var _default = exports.default = scopedCssBaselineClasses;