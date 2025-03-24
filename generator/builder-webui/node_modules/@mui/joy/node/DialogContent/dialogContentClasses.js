"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogContentUtilityClass = getDialogContentUtilityClass;
var _className = require("../className");
function getDialogContentUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiDialogContent', slot);
}
const dialogContentClasses = (0, _className.generateUtilityClasses)('MuiDialogContent', ['root']);
var _default = exports.default = dialogContentClasses;