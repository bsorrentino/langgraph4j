"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuButtonUtilityClass = getMenuButtonUtilityClass;
var _className = require("../className");
function getMenuButtonUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiMenuButton', slot);
}
const menuButtonClasses = (0, _className.generateUtilityClasses)('MuiMenuButton', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorInfo', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'disabled', 'sizeSm', 'sizeMd', 'sizeLg', 'fullWidth', 'startDecorator', 'endDecorator', 'loading', 'loadingIndicatorCenter']);
var _default = exports.default = menuButtonClasses;