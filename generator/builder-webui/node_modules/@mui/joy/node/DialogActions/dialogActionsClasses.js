"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogActionsUtilityClass = getDialogActionsUtilityClass;
var _className = require("../className");
function getDialogActionsUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiDialogActions', slot);
}
const dialogActionsClasses = (0, _className.generateUtilityClasses)('MuiDialogActions', ['root']);
var _default = exports.default = dialogActionsClasses;