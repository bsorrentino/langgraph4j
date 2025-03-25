"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDividerUtilityClass = getDividerUtilityClass;
var _className = require("../className");
function getDividerUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiDivider', slot);
}
const dividerClasses = (0, _className.generateUtilityClasses)('MuiDivider', ['root', 'horizontal', 'vertical', 'insetContext', 'insetNone']);
var _default = exports.default = dividerClasses;