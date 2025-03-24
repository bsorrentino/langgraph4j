"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemContentUtilityClass = getListItemContentUtilityClass;
var _className = require("../className");
function getListItemContentUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiListItemContent', slot);
}
const listItemContentClasses = (0, _className.generateUtilityClasses)('MuiListItemContent', ['root']);
var _default = exports.default = listItemContentClasses;