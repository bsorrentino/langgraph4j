"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabsUtilityClass = getTabsUtilityClass;
var _className = require("../className");
function getTabsUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTabs', slot);
}
const tabListClasses = (0, _className.generateUtilityClasses)('MuiTabs', ['root', 'horizontal', 'vertical', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = tabListClasses;