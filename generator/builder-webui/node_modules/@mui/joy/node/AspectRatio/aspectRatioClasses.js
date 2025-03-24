"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAspectRatioUtilityClass = getAspectRatioUtilityClass;
var _className = require("../className");
function getAspectRatioUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAspectRatio', slot);
}
const aspectRatioClasses = (0, _className.generateUtilityClasses)('MuiAspectRatio', ['root', 'content', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = aspectRatioClasses;