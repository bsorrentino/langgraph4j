"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionGroupUtilityClass = getAccordionGroupUtilityClass;
var _className = require("../className");
function getAccordionGroupUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAccordionGroup', slot);
}
const accordionGroupClasses = (0, _className.generateUtilityClasses)('MuiAccordionGroup', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = accordionGroupClasses;