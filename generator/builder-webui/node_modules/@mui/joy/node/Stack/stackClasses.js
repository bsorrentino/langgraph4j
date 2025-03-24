"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStackUtilityClass = getStackUtilityClass;
var _className = require("../className");
function getStackUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiStack', slot);
}
const stackClasses = (0, _className.generateUtilityClasses)('MuiStack', ['root']);
var _default = exports.default = stackClasses;