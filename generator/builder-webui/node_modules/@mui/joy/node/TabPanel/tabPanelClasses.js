"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabPanelUtilityClass = getTabPanelUtilityClass;
var _className = require("../className");
function getTabPanelUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTabPanel', slot);
}
const tabListClasses = (0, _className.generateUtilityClasses)('MuiTabPanel', ['root', 'hidden', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = tabListClasses;