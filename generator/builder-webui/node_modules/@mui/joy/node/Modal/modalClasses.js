"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getModalUtilityClass = getModalUtilityClass;
var _className = require("../className");
function getModalUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiModal', slot);
}
const modalClasses = (0, _className.generateUtilityClasses)('MuiModal', ['root', 'hidden', 'backdrop']);
var _default = exports.default = modalClasses;