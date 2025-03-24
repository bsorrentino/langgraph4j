"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepIndicatorUtilityClass = getStepIndicatorUtilityClass;
var _className = require("../className");
function getStepIndicatorUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiStepIndicator', slot);
}
const stepIndicatorClasses = (0, _className.generateUtilityClasses)('MuiStepIndicator', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'horizontal', 'vertical']);
var _default = exports.default = stepIndicatorClasses;