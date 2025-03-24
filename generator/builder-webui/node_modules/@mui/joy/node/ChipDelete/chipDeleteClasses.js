"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getChipDeleteUtilityClass = getChipDeleteUtilityClass;
var _className = require("../className");
function getChipDeleteUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiChipDelete', slot);
}
const chipDeleteClasses = (0, _className.generateUtilityClasses)('MuiChipDelete', ['root', 'disabled', 'focusVisible', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantSolid', 'variantSoft', 'variantOutlined']);
var _default = exports.default = chipDeleteClasses;