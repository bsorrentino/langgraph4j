"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getModalDialogUtilityClass = getModalDialogUtilityClass;
var _className = require("../className");
function getModalDialogUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiModalDialog', slot);
}
const modalDialogClasses = (0, _className.generateUtilityClasses)('MuiModalDialog', ['root', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid', 'sizeSm', 'sizeMd', 'sizeLg', 'layoutCenter', 'layoutFullscreen']);
var _default = exports.default = modalDialogClasses;