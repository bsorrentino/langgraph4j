"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardUtilityClass = getCardUtilityClass;
var _className = require("../className");
function getCardUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiCard', slot);
}
const cardClasses = (0, _className.generateUtilityClasses)('MuiCard', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'horizontal', 'vertical']);
var _default = exports.default = cardClasses;