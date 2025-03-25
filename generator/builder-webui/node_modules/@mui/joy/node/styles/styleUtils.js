"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.resolveSxValue = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
/**
 * internal utility
 *
 * Why? to read `sx` values and attach component's CSS variables
 *      e.g. <Card sx={{ borderRadius: 0 }} /> should attach
 *          `--Card-radius: 0px` so that developers don't have to remember
 *
 * Why not reuse `styleFunctionSx`?
 *     `styleFunctionSx` is more expensive as it iterates over all the keys
 */
// eslint-disable-next-line import/prefer-default-export
const resolveSxValue = ({
  theme,
  ownerState
}, keys) => {
  let sxObject = {};
  function resolveSx(sxProp) {
    if (typeof sxProp === 'function') {
      const result = sxProp(theme);
      resolveSx(result);
    } else if (Array.isArray(sxProp)) {
      sxProp.forEach(sxItem => {
        if (typeof sxItem !== 'boolean') {
          resolveSx(sxItem);
        }
      });
    } else if (typeof sxProp === 'object') {
      sxObject = (0, _extends2.default)({}, sxObject, sxProp);
    }
  }
  if (ownerState.sx) {
    resolveSx(ownerState.sx);
    keys.forEach(key => {
      const value = sxObject[key];
      if (typeof value === 'string' || typeof value === 'number') {
        if (key === 'borderRadius') {
          if (typeof value === 'number') {
            sxObject[key] = `${value}px`;
          } else {
            var _theme$vars;
            sxObject[key] = ((_theme$vars = theme.vars) == null ? void 0 : _theme$vars.radius[value]) || value;
          }
        } else if (['p', 'padding', 'm', 'margin'].indexOf(key) !== -1 && typeof value === 'number') {
          sxObject[key] = theme.spacing(value);
        } else {
          sxObject[key] = value;
        }
      } else if (typeof value === 'function') {
        sxObject[key] = value(theme);
      } else {
        sxObject[key] = undefined;
      }
    });
  }
  return sxObject;
};
exports.resolveSxValue = resolveSxValue;