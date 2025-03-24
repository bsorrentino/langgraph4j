"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getOptionUtilityClass = getOptionUtilityClass;
var _className = require("../className");
function getOptionUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiOption', slot);
}
const optionClasses = (0, _className.generateUtilityClasses)('MuiOption', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'focusVisible', 'disabled', 'selected', 'highlighted', 'variantPlain', 'variantSoft', 'variantOutlined', 'variantSolid']);
var _default = exports.default = optionClasses;