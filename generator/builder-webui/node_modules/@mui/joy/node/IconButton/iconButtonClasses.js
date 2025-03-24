"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getIconButtonUtilityClass = getIconButtonUtilityClass;
var _className = require("../className");
function getIconButtonUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiIconButton', slot);
}
const iconButtonClasses = (0, _className.generateUtilityClasses)('MuiIconButton', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'focusVisible', 'disabled', 'sizeSm', 'sizeMd', 'sizeLg', 'loading', 'loadingIndicator']);
var _default = exports.default = iconButtonClasses;