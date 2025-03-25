"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepButtonUtilityClass = getStepButtonUtilityClass;
var _className = require("../className");
function getStepButtonUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiStepButton', slot);
}
const stepButtonClasses = (0, _className.generateUtilityClasses)('MuiStepButton', ['root']);
var _default = exports.default = stepButtonClasses;