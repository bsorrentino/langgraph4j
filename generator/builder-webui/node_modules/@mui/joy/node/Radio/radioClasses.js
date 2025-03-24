"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getRadioUtilityClass = getRadioUtilityClass;
var _className = require("../className");
function getRadioUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiRadio', slot);
}
const radioClasses = (0, _className.generateUtilityClasses)('MuiRadio', ['root', 'radio', 'icon', 'action', 'input', 'label', 'checked', 'disabled', 'focusVisible', 'colorPrimary', 'colorDanger', 'colorNeutral', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = radioClasses;