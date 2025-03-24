"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardActionsUtilityClass = getCardActionsUtilityClass;
var _className = require("../className");
function getCardActionsUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCardActions', slot);
}
const cardActionsClasses = (0, _className.generateUtilityClasses)('MuiCardActions', ['root']);
var _default = exports.default = cardActionsClasses;