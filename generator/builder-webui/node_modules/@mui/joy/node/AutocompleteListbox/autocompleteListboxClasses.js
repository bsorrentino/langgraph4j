"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAutocompleteListboxUtilityClass = getAutocompleteListboxUtilityClass;
var _className = require("../className");
function getAutocompleteListboxUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAutocompleteListbox', slot);
}
const autocompleteListboxClasses = (0, _className.generateUtilityClasses)('MuiAutocompleteListbox', ['root', 'sizeSm', 'sizeMd', 'sizeLg', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = autocompleteListboxClasses;