"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAutocompleteOptionUtilityClass = getAutocompleteOptionUtilityClass;
var _className = require("../className");
function getAutocompleteOptionUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAutocompleteOption', slot);
}
const autocompleteOptionClasses = (0, _className.generateUtilityClasses)('MuiAutocompleteOption', ['root', 'focused', 'focusVisible', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'variantPlain', 'variantSoft', 'variantOutlined', 'variantSolid']);
var _default = exports.default = autocompleteOptionClasses;