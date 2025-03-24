"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabListUtilityClass = getTabListUtilityClass;
var _className = require("../className");
function getTabListUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTabList', slot);
}
const tabListClasses = (0, _className.generateUtilityClasses)('MuiTabList', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = tabListClasses;