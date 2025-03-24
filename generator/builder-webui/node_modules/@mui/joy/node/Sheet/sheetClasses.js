"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSheetUtilityClass = getSheetUtilityClass;
var _className = require("../className");
function getSheetUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSheet', slot);
}
const sheetClasses = (0, _className.generateUtilityClasses)('MuiSheet', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = sheetClasses;