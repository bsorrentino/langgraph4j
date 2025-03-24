"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemButtonUtilityClass = getListItemButtonUtilityClass;
var _className = require("../className");
function getListItemButtonUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiListItemButton', slot);
}
const listItemButtonClasses = (0, _className.generateUtilityClasses)('MuiListItemButton', ['root', 'horizontal', 'vertical', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'focusVisible', 'disabled', 'selected', 'variantPlain', 'variantSoft', 'variantOutlined', 'variantSolid']);
var _default = exports.default = listItemButtonClasses;