"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getModalCloseUtilityClass = getModalCloseUtilityClass;
var _className = require("../className");
function getModalCloseUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiModalClose', slot);
}
const modalCloseClasses = (0, _className.generateUtilityClasses)('MuiModalClose', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = modalCloseClasses;