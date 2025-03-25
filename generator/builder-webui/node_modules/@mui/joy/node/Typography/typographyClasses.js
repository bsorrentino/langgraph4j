"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTypographyUtilityClass = getTypographyUtilityClass;
var _className = require("../className");
function getTypographyUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTypography', slot);
}
const typographyClasses = (0, _className.generateUtilityClasses)('MuiTypography', ['root', 'h1', 'h2', 'h3', 'h4', 'title-lg', 'title-md', 'title-sm', 'body-lg', 'body-md', 'body-sm', 'body-xs', 'noWrap', 'gutterBottom', 'startDecorator', 'endDecorator', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = typographyClasses;