"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemUtilityClass = getListItemUtilityClass;
var _className = require("../className");
function getListItemUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiListItem', slot);
}
const listItemClasses = (0, _className.generateUtilityClasses)('MuiListItem', ['root', 'startAction', 'endAction', 'nested', 'nesting', 'sticky', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantSoft', 'variantOutlined', 'variantSolid']);
var _default = exports.default = listItemClasses;