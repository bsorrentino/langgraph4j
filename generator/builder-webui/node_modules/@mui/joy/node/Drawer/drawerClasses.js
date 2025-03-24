"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDrawerUtilityClass = getDrawerUtilityClass;
var _className = require("../className");
function getDrawerUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiDrawer', slot);
}
const drawerClasses = (0, _className.generateUtilityClasses)('MuiDrawer', ['root', 'hidden', 'backdrop', 'content', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = drawerClasses;