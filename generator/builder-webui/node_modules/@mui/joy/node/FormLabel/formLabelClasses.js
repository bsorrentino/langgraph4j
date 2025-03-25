"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormLabelUtilityClass = getFormLabelUtilityClass;
var _className = require("../className");
function getFormLabelUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiFormLabel', slot);
}
const formLabelClasses = (0, _className.generateUtilityClasses)('MuiFormLabel', ['root', 'asterisk']);
var _default = exports.default = formLabelClasses;