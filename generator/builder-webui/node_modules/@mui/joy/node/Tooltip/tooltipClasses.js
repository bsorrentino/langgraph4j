"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTooltipUtilityClass = getTooltipUtilityClass;
var _className = require("../className");
function getTooltipUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiTooltip', slot);
}
const tooltipClasses = (0, _className.generateUtilityClasses)('MuiTooltip', ['root', 'tooltipArrow', 'arrow', 'touch', 'placementLeft', 'placementRight', 'placementTop', 'placementBottom', 'colorPrimary', 'colorDanger', 'colorNeutral', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = tooltipClasses;