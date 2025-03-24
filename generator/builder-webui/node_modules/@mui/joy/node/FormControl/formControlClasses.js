"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormControlUtilityClass = getFormControlUtilityClass;
var _className = require("../className");
function getFormControlUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiFormControl', slot);
}
const formControlClasses = (0, _className.generateUtilityClasses)('MuiFormControl', ['root', 'error', 'disabled', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical']);
var _default = exports.default = formControlClasses;