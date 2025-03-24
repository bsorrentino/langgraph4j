"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionDetailsUtilityClass = getAccordionDetailsUtilityClass;
var _className = require("../className");
function getAccordionDetailsUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAccordionDetails', slot);
}
const accordionDetailsClasses = (0, _className.generateUtilityClasses)('MuiAccordionDetails', ['root', 'content', 'expanded']);
var _default = exports.default = accordionDetailsClasses;