"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAutocompleteUtilityClass = getAutocompleteUtilityClass;
var _className = require("../className");
function getAutocompleteUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiAutocomplete', slot);
}
const autocompleteClasses = (0, _className.generateUtilityClasses)('MuiAutocomplete', ['root', 'wrapper', 'input', 'startDecorator', 'endDecorator', 'formControl', 'focused', 'disabled', 'error', 'multiple', 'limitTag', 'hasPopupIcon', 'hasClearIcon', 'clearIndicator', 'popupIndicator', 'popupIndicatorOpen', 'listbox', 'option', 'loading', 'noOptions', 'colorPrimary', 'colorNeutral', 'colorDanger', 'colorSuccess', 'colorWarning', 'colorContext', 'sizeSm', 'sizeMd', 'sizeLg', 'variantPlain', 'variantOutlined', 'variantSoft', 'variantSolid']);
var _default = exports.default = autocompleteClasses;