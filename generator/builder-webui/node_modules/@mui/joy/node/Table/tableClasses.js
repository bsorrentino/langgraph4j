"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableUtilityClass = getTableUtilityClass;
var _className = require("../className");
function getTableUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTable', slot);
}
const tableClasses = (0, _className.generateUtilityClasses)('MuiTable', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'stickyHeader', 'stickyFooter', 'noWrap', 'hoverRow', 'borderAxisNone', 'borderAxisX', 'borderAxisXBetween', 'borderAxisY', 'borderAxisYBetween', 'borderAxisBoth', 'borderAxisBothBetween']);
var _default = exports.default = tableClasses;