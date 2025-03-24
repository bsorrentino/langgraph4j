"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListSubheaderUtilityClass = getListSubheaderUtilityClass;
var _className = require("../className");
function getListSubheaderUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiListSubheader', slot);
}
const listSubheaderClasses = (0, _className.generateUtilityClasses)('MuiListSubheader', ['root', 'sticky', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantSoft', 'variantOutlined', 'variantSolid']);
var _default = exports.default = listSubheaderClasses;