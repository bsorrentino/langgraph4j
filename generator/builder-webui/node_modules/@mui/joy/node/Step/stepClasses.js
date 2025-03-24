"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepUtilityClass = getStepUtilityClass;
var _className = require("../className");
function getStepUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiStep', slot);
}
const stepClasses = (0, _className.generateUtilityClasses)('MuiStep', ['root', 'indicator', 'horizontal', 'vertical', 'active', 'completed', 'disabled']);
var _default = exports.default = stepClasses;