"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.generateUtilityClasses = exports.generateUtilityClass = void 0;
Object.defineProperty(exports, "unstable_ClassNameGenerator", {
  enumerable: true,
  get: function () {
    return _utils.unstable_ClassNameGenerator;
  }
});
var _utils = require("@mui/utils");
const generateUtilityClass = (componentName, slot) => (0, _utils.unstable_generateUtilityClass)(componentName, slot, 'Mui');
exports.generateUtilityClass = generateUtilityClass;
const generateUtilityClasses = (componentName, slots) => (0, _utils.unstable_generateUtilityClasses)(componentName, slots, 'Mui');
exports.generateUtilityClasses = generateUtilityClasses;