"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getContainerUtilityClass = getContainerUtilityClass;
var _className = require("../className");
function getContainerUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiContainer', slot);
}
const containerClasses = (0, _className.generateUtilityClasses)('MuiContainer', ['root', 'disableGutters', 'fixed', 'maxWidthXs', 'maxWidthSm', 'maxWidthMd', 'maxWidthLg', 'maxWidthXl']);
var _default = exports.default = containerClasses;