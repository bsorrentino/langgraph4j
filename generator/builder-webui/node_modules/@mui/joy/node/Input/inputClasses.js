"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getInputUtilityClass = getInputUtilityClass;
var _className = require("../className");
function getInputUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiInput', slot);
}
const inputClasses = (0, _className.generateUtilityClasses)('MuiInput', ['root', 'input', 'formControl', 'focused', 'disabled', 'error', 'adornedStart', 'adornedEnd', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'fullWidth', 'startDecorator', 'endDecorator']);
var _default = exports.default = inputClasses;