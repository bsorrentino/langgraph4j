"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardContentUtilityClass = getCardContentUtilityClass;
var _className = require("../className");
function getCardContentUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCardContent', slot);
}
const cardClasses = (0, _className.generateUtilityClasses)('MuiCardContent', ['root']);
var _default = exports.default = cardClasses;