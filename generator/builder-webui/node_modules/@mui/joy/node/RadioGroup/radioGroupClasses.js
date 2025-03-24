"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getRadioGroupUtilityClass = getRadioGroupUtilityClass;
var _className = require("../className");
function getRadioGroupUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiRadioGroup', slot);
}
const radioGroupClasses = (0, _className.generateUtilityClasses)('MuiRadioGroup', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical']);
var _default = exports.default = radioGroupClasses;