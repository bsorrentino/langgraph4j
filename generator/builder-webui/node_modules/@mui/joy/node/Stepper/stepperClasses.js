"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepperUtilityClass = getStepperUtilityClass;
var _className = require("../className");
function getStepperUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiStepper', slot);
}
const stepperClasses = (0, _className.generateUtilityClasses)('MuiStepper', ['root', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical']);
var _default = exports.default = stepperClasses;