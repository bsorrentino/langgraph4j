"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCircularProgressUtilityClass = getCircularProgressUtilityClass;
var _className = require("../className");
function getCircularProgressUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCircularProgress', slot);
}
const circularProgressClasses = (0, _className.generateUtilityClasses)('MuiCircularProgress', ['root', 'determinate', 'svg', 'track', 'progress', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = circularProgressClasses;