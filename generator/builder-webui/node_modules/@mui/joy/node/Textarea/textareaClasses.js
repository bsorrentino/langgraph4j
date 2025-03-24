"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTextareaUtilityClass = getTextareaUtilityClass;
var _className = require("../className");
function getTextareaUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTextarea', slot);
}
const textareaClasses = (0, _className.generateUtilityClasses)('MuiTextarea', ['root', 'textarea', 'startDecorator', 'endDecorator', 'formControl', 'disabled', 'error', 'focused', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft']);
var _default = exports.default = textareaClasses;