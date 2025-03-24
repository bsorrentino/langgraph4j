"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionSummaryUtilityClass = getAccordionSummaryUtilityClass;
var _className = require("../className");
function getAccordionSummaryUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAccordionSummary', slot);
}
const accordionSummaryClasses = (0, _className.generateUtilityClasses)('MuiAccordionSummary', ['root', 'button', 'indicator', 'disabled', 'expanded']);
var _default = exports.default = accordionSummaryClasses;