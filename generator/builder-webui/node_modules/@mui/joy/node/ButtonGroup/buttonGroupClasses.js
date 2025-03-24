"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getButtonGroupUtilityClass = getButtonGroupUtilityClass;
var _className = require("../className");
function getButtonGroupUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiButtonGroup', slot);
}
const buttonGroupClasses = (0, _className.generateUtilityClasses)('MuiButtonGroup', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical']);
var _default = exports.default = buttonGroupClasses;