"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormHelperTextUtilityClass = getFormHelperTextUtilityClass;
var _className = require("../className");
function getFormHelperTextUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiFormHelperText', slot);
}
const formHelperTextClasses = (0, _className.generateUtilityClasses)('MuiFormHelperText', ['root']);
var _default = exports.default = formHelperTextClasses;