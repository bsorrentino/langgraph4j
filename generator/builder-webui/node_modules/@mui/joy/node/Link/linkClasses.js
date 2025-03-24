"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getLinkUtilityClass = getLinkUtilityClass;
var _className = require("../className");
function getLinkUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiLink', slot);
}
const linkClasses = (0, _className.generateUtilityClasses)('MuiLink', ['root', 'disabled', 'focusVisible', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'focusVisible', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'underlineNone', 'underlineHover', 'underlineAlways', 'h1', 'h2', 'h3', 'h4', 'title-lg', 'title-md', 'title-sm', 'body-lg', 'body-md', 'body-sm', 'body-xs', 'startDecorator', 'endDecorator']);
var _default = exports.default = linkClasses;