"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSnackbarUtilityClass = getSnackbarUtilityClass;
var _className = require("../className");
function getSnackbarUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSnackbar', slot);
}
const snackbarClasses = (0, _className.generateUtilityClasses)('MuiSnackbar', ['root', 'anchorOriginTopCenter', 'anchorOriginBottomCenter', 'anchorOriginTopRight', 'anchorOriginBottomRight', 'anchorOriginTopLeft', 'anchorOriginBottomLeft', 'colorPrimary', 'colorDanger', 'colorNeutral', 'colorSuccess', 'colorWarning', 'endDecorator', 'sizeSm', 'sizeMd', 'sizeLg', 'startDecorator', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = snackbarClasses;