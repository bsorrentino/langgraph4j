"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getLinearProgressUtilityClass = getLinearProgressUtilityClass;
var _className = require("../className");
function getLinearProgressUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiLinearProgress', slot);
}
const linearProgressClasses = (0, _className.generateUtilityClasses)('MuiLinearProgress', ['root', 'determinate', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = linearProgressClasses;