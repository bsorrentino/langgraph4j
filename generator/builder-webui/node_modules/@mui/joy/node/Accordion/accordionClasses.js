"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionUtilityClass = getAccordionUtilityClass;
var _className = require("../className");
function getAccordionUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAccordion', slot);
}
const accordionClasses = (0, _className.generateUtilityClasses)('MuiAccordion', ['root', 'expanded', 'disabled']);
var _default = exports.default = accordionClasses;