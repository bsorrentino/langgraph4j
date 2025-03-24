"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSvgIconUtilityClass = getSvgIconUtilityClass;
var _className = require("../className");
function getSvgIconUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSvgIcon', slot);
}
const svgIconClasses = (0, _className.generateUtilityClasses)('MuiSvgIcon', ['root', 'colorInherit', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'fontSizeInherit', 'fontSizeXs', 'fontSizeSm', 'fontSizeMd', 'fontSizeLg', 'fontSizeXl', 'fontSizeXl2', 'fontSizeXl3', 'fontSizeXl4', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = svgIconClasses;