"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getButtonUtilityClass = getButtonUtilityClass;
var _className = require("../className");
function getButtonUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiButton', slot);
}
const buttonClasses = (0, _className.generateUtilityClasses)('MuiButton', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'focusVisible', 'disabled', 'sizeSm', 'sizeMd', 'sizeLg', 'fullWidth', 'startDecorator', 'endDecorator', 'loading', 'loadingIndicatorCenter']);
var _default = exports.default = buttonClasses;