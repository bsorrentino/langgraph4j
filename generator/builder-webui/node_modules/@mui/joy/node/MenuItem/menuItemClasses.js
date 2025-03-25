"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuItemUtilityClass = getMenuItemUtilityClass;
var _className = require("../className");
function getMenuItemUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiMenuItem', slot);
}
const menuItemClasses = (0, _className.generateUtilityClasses)('MuiMenuItem', ['root', 'focusVisible', 'disabled', 'selected', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantSoft', 'variantOutlined', 'variantSolid']);
var _default = exports.default = menuItemClasses;