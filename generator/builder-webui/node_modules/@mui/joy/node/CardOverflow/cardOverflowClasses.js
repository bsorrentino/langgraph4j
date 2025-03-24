"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardOverflowUtilityClass = getCardOverflowUtilityClass;
var _className = require("../className");
function getCardOverflowUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCardOverflow', slot);
}
const aspectRatioClasses = (0, _className.generateUtilityClasses)('MuiCardOverflow', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = aspectRatioClasses;