"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBadgeUtilityClass = getBadgeUtilityClass;
var _className = require("../className");
function getBadgeUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiBadge', slot);
}
const badgeClasses = (0, _className.generateUtilityClasses)('MuiBadge', ['root', 'badge', 'anchorOriginTopRight', 'anchorOriginBottomRight', 'anchorOriginTopLeft', 'anchorOriginBottomLeft', 'colorPrimary', 'colorDanger', 'colorNeutral', 'colorSuccess', 'colorWarning', 'colorContext', 'invisible', 'locationInside', 'locationOutside', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = badgeClasses;