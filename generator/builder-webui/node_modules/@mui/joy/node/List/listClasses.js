"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListUtilityClass = getListUtilityClass;
var _className = require("../className");
function getListUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiList', slot);
}
const listClasses = (0, _className.generateUtilityClasses)('MuiList', ['root', 'nesting', 'scoped', 'sizeSm', 'sizeMd', 'sizeLg', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'horizontal', 'vertical']);
var _default = exports.default = listClasses;