"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListDividerUtilityClass = getListDividerUtilityClass;
var _className = require("../className");
function getListDividerUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiListDivider', slot);
}
const listDividerClasses = (0, _className.generateUtilityClasses)('MuiListDivider', ['root', 'insetGutter', 'insetStartDecorator', 'insetStartContent', 'horizontal', 'vertical']);
var _default = exports.default = listDividerClasses;