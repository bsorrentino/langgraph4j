"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getToggleButtonGroupUtilityClass = getToggleButtonGroupUtilityClass;
var _className = require("../className");
function getToggleButtonGroupUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiToggleButtonGroup', slot);
}
const toggleButtonGroupClasses = (0, _className.generateUtilityClasses)('MuiToggleButtonGroup', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical']);
var _default = exports.default = toggleButtonGroupClasses;