"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getModalOverflowUtilityClass = getModalOverflowUtilityClass;
var _className = require("../className");
function getModalOverflowUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiModalOverflow', slot);
}
const modalOverflowClasses = (0, _className.generateUtilityClasses)('MuiModalOverflow', ['root']);
var _default = exports.default = modalOverflowClasses;