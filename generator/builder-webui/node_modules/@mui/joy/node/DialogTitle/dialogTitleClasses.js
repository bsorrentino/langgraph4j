"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogTitleUtilityClass = getDialogTitleUtilityClass;
var _className = require("../className");
function getDialogTitleUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiDialogTitle', slot);
}
const dialogTitleClasses = (0, _className.generateUtilityClasses)('MuiDialogTitle', ['root', 'h1', 'h2', 'h3', 'h4', 'title-lg', 'title-md', 'title-sm', 'body-lg', 'body-md', 'body-sm', 'body-xs', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = dialogTitleClasses;