"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCheckboxUtilityClass = getCheckboxUtilityClass;
var _className = require("../className");
function getCheckboxUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCheckbox', slot);
}
const checkboxClasses = (0, _className.generateUtilityClasses)('MuiCheckbox', ['root', 'checkbox', 'action', 'input', 'label', 'checked', 'disabled', 'focusVisible', 'indeterminate', 'colorPrimary', 'colorDanger', 'colorNeutral', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = checkboxClasses;