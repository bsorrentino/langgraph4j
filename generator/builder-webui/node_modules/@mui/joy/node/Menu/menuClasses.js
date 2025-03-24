"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuUtilityClass = getMenuUtilityClass;
var _className = require("../className");
function getMenuUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiMenu', slot);
}
const menuClasses = (0, _className.generateUtilityClasses)('MuiMenu', ['root', 'listbox', 'expanded', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = menuClasses;