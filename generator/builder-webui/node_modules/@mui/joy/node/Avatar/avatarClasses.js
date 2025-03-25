"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAvatarUtilityClass = getAvatarUtilityClass;
var _className = require("../className");
function getAvatarUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAvatar', slot);
}
const avatarClasses = (0, _className.generateUtilityClasses)('MuiAvatar', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'fallback', 'sizeSm', 'sizeMd', 'sizeLg', 'img', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = avatarClasses;