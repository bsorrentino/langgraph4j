"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSelectUtilityClass = getSelectUtilityClass;
var _className = require("../className");
function getSelectUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSelect', slot);
}
const selectClasses = (0, _className.generateUtilityClasses)('MuiSelect', ['root', 'button', 'indicator', 'startDecorator', 'endDecorator', 'popper', 'listbox', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'focusVisible', 'disabled', 'expanded', 'multiple']);
var _default = exports.default = selectClasses;