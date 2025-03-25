"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardCoverUtilityClass = getCardCoverUtilityClass;
var _className = require("../className");
function getCardCoverUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCardCover', slot);
}
const cardCoverClasses = (0, _className.generateUtilityClasses)('MuiCardCover', ['root']);
var _default = exports.default = cardCoverClasses;