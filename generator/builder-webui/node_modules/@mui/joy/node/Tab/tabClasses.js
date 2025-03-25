"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabUtilityClass = getTabUtilityClass;
var _className = require("../className");
function getTabUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTab', slot);
}
const tabListClasses = (0, _className.generateUtilityClasses)('MuiTab', ['root', 'disabled', 'focusVisible', 'selected', 'horizontal', 'vertical', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = tabListClasses;