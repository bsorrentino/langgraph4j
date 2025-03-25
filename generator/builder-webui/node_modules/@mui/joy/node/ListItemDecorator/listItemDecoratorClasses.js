"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemDecoratorUtilityClass = getListItemDecoratorUtilityClass;
var _className = require("../className");
function getListItemDecoratorUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiListItemDecorator', slot);
}
const listItemDecoratorClasses = (0, _className.generateUtilityClasses)('MuiListItemDecorator', ['root']);
var _default = exports.default = listItemDecoratorClasses;