"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSliderUtilityClass = getSliderUtilityClass;
var _className = require("../className");
function getSliderUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSlider', slot);
}
const sliderClasses = (0, _className.generateUtilityClasses)('MuiSlider', ['root', 'disabled', 'dragging', 'focusVisible', 'marked', 'vertical', 'trackInverted', 'trackFalse', 'rail', 'track', 'mark', 'markActive', 'markLabel', 'thumb', 'thumbStart', 'thumbEnd', 'valueLabel', 'valueLabelOpen', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'disabled', 'sizeSm', 'sizeMd', 'sizeLg', 'input']);
var _default = exports.default = sliderClasses;