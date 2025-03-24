"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAlertUtilityClass = getAlertUtilityClass;
var _className = require("../className");
function getAlertUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAlert', slot);
}
const alertClasses = (0, _className.generateUtilityClasses)('MuiAlert', ['root', 'startDecorator', 'endDecorator', 'colorPrimary', 'colorDanger', 'colorNeutral', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = alertClasses;