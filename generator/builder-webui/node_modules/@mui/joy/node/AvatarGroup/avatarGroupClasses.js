"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAvatarGroupUtilityClass = getAvatarGroupUtilityClass;
var _className = require("../className");
function getAvatarGroupUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAvatarGroup', slot);
}
const avatarGroupClasses = (0, _className.generateUtilityClasses)('MuiAvatarGroup', ['root']);
var _default = exports.default = avatarGroupClasses;