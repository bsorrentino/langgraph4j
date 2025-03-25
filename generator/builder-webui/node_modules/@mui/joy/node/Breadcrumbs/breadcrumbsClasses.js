"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBreadcrumbsUtilityClass = getBreadcrumbsUtilityClass;
var _utils = require("@mui/utils");
function getBreadcrumbsUtilityClass(slot) {
  return (0, _utils.unstable_generateUtilityClass)('MuiBreadcrumbs', slot);
}
const breadcrumbsClasses = (0, _utils.unstable_generateUtilityClasses)('MuiBreadcrumbs', ['root', 'ol', 'li', 'separator', 'sizeSm', 'sizeMd', 'sizeLg']);
var _default = exports.default = breadcrumbsClasses;