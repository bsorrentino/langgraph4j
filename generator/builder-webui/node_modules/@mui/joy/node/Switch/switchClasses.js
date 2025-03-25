"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSwitchUtilityClass = getSwitchUtilityClass;
var _className = require("../className");
function getSwitchUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSwitch', slot);
}
const switchClasses = (0, _className.generateUtilityClasses)('MuiSwitch', ['root', 'checked', 'disabled', 'action', 'input', 'thumb', 'track', 'focusVisible', 'readOnly', 'colorPrimary', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantOutlined', 'variantSoft', 'variantSolid', 'startDecorator', 'endDecorator']);
var _default = exports.default = switchClasses;