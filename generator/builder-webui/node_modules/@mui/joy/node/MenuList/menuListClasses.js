"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuListUtilityClass = getMenuListUtilityClass;
var _className = require("../className");
function getMenuListUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiMenuList', slot);
}
const menuClasses = (0, _className.generateUtilityClasses)('MuiMenuList', ['root', 'nested', 'sizeSm', 'sizeMd', 'sizeLg', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = menuClasses;