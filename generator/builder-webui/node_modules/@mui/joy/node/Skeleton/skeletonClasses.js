"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSkeletonUtilityClass = getSkeletonUtilityClass;
var _className = require("../className");
function getSkeletonUtilityClass(slot) {
  return (0, _className.generateUtilityClass)('MuiSkeleton', slot);
}
const skeletonClasses = (0, _className.generateUtilityClasses)('MuiSkeleton', ['root', 'variantOverlay', 'variantCircular', 'variantRectangular', 'variantText', 'variantInline', 'h1', 'h2', 'h3', 'h4', 'title-lg', 'title-md', 'title-sm', 'body-lg', 'body-md', 'body-sm', 'body-xs']);
var _default = exports.default = skeletonClasses;