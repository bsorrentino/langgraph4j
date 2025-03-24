"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = shouldSkipGeneratingVar;
function shouldSkipGeneratingVar(keys) {
  var _keys$;
  return !!keys[0].match(/^(typography|variants|breakpoints)$/) || !!keys[0].match(/sxConfig$/) ||
  // ends with sxConfig
  keys[0] === 'palette' && !!((_keys$ = keys[1]) != null && _keys$.match(/^(mode)$/)) || keys[0] === 'focus' && keys[1] !== 'thickness';
}