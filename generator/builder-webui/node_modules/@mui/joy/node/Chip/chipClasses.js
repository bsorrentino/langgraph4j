"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getChipUtilityClass = getChipUtilityClass;
var _className = require("../className");
function getChipUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiChip', slot);
}
const chipClasses = (0, _className.generateUtilityClasses)('MuiChip', ['root', 'clickable', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'disabled', 'endDecorator', 'focusVisible', 'label', 'labelSm', 'labelMd', 'labelLg', 'sizeSm', 'sizeMd', 'sizeLg', 'startDecorator', 'variantPlain', 'variantSolid', 'variantSoft', 'variantOutlined']);
var _default = exports.default = chipClasses;