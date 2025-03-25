"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isVariantPalette = exports.createVariantStyle = exports.createVariant = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
const isVariantPalette = colorPalette => colorPalette && typeof colorPalette === 'object' && Object.keys(colorPalette).some(value => {
  var _value$match;
  return (_value$match = value.match) == null ? void 0 : _value$match.call(value, /^(plain(Hover|Active|Disabled)?(Color|Bg)|outlined(Hover|Active|Disabled)?(Color|Border|Bg)|soft(Hover|Active|Disabled)?(Color|Bg)|solid(Hover|Active|Disabled)?(Color|Bg))$/);
});
exports.isVariantPalette = isVariantPalette;
const assignCss = (target, variantVar, value) => {
  if (variantVar.includes('Color')) {
    target.color = value;
  }
  if (variantVar.includes('Bg')) {
    target.backgroundColor = value;
  }
  if (variantVar.includes('Border')) {
    target.borderColor = value;
  }
};

/**
 *
 * @param name variant name
 * @example 'plain'
 *
 * @param palette object that contains palette tokens
 * @example { primary: { plainColor: '', plainHoverColor: '', ...tokens }, ...other palette }
 *
 * @param getCssVar a function that receive variant token and return a CSS variable
 *
 * result will be the stylesheet based on the palette tokens
 * @example {
 *   color: '--token',
 *   backgroundColor: '--token',
 *   '--variant-borderWidth': '0px',
 * }
 * @example {
 *   cursor: 'pointer',
 *   color: '--token',
 *   backgroundColor: '--token',
 *   '--variant-borderWidth': '1px',
 * }
 * @example {
 *   pointerEvents: 'none',
 *   cursor: 'default',
 *   color: '--token',
 *   backgroundColor: '--token',
 *   '--variant-borderWidth': '0px',
 * }
 */
const createVariantStyle = (name, palette, getCssVar) => {
  const result = {};
  Object.entries(palette || {}).forEach(([variantVar, value]) => {
    if (variantVar.match(new RegExp(`${name}(color|bg|border)`, 'i')) && !!value) {
      const cssVar = getCssVar ? getCssVar(variantVar) : value;
      if (variantVar.includes('Disabled')) {
        result.pointerEvents = 'none';
        result.cursor = 'default';
        result['--Icon-color'] = 'currentColor';
      }
      if (variantVar.match(/(Hover|Active|Disabled)/)) {
        assignCss(result, variantVar, cssVar);
      } else {
        // initial state
        if (!result['--variant-borderWidth']) {
          // important to prevent inheritance, otherwise the children will have the wrong styles e.g.
          //   <Card variant="outlined">
          //     <Typography variant="soft">
          result['--variant-borderWidth'] = '0px';
        }
        if (variantVar.includes('Border')) {
          result['--variant-borderWidth'] = '1px';
          result.border = 'var(--variant-borderWidth) solid';
        }
        // border color should come later
        assignCss(result, variantVar, cssVar);
      }
    }
  });
  return result;
};
exports.createVariantStyle = createVariantStyle;
// It's used only in extendTheme, so it's safe to always include default values
const createVariant = (variant, theme) => {
  let result = {};
  if (theme) {
    const {
      getCssVar,
      palette
    } = theme;
    Object.entries(palette).forEach(entry => {
      const [color, colorPalette] = entry;
      if (isVariantPalette(colorPalette) && typeof colorPalette === 'object') {
        result = (0, _extends2.default)({}, result, {
          [color]: createVariantStyle(variant, colorPalette, variantVar => `var(--variant-${variantVar}, ${getCssVar(`palette-${color}-${variantVar}`, palette[color][variantVar])})`)
        });
      }
    });
  }
  result.context = createVariantStyle(variant, {
    plainColor: 'var(--variant-plainColor)',
    plainHoverColor: `var(--variant-plainHoverColor)`,
    plainHoverBg: 'var(--variant-plainHoverBg)',
    plainActiveBg: 'var(--variant-plainActiveBg)',
    plainDisabledColor: 'var(--variant-plainDisabledColor)',
    outlinedColor: 'var(--variant-outlinedColor)',
    outlinedBorder: 'var(--variant-outlinedBorder)',
    outlinedHoverColor: `var(--variant-outlinedHoverColor)`,
    outlinedHoverBorder: `var(--variant-outlinedHoverBorder)`,
    outlinedHoverBg: `var(--variant-outlinedHoverBg)`,
    outlinedActiveBg: `var(--variant-outlinedActiveBg)`,
    outlinedDisabledColor: `var(--variant-outlinedDisabledColor)`,
    outlinedDisabledBorder: `var(--variant-outlinedDisabledBorder)`,
    softColor: 'var(--variant-softColor)',
    softBg: 'var(--variant-softBg)',
    softHoverColor: 'var(--variant-softHoverColor)',
    softHoverBg: 'var(--variant-softHoverBg)',
    softActiveBg: 'var(--variant-softActiveBg)',
    softDisabledColor: 'var(--variant-softDisabledColor)',
    softDisabledBg: 'var(--variant-softDisabledBg)',
    solidColor: 'var(--variant-solidColor)',
    solidBg: 'var(--variant-solidBg)',
    solidHoverBg: 'var(--variant-solidHoverBg)',
    solidActiveBg: 'var(--variant-solidActiveBg)',
    solidDisabledColor: 'var(--variant-solidDisabledColor)',
    solidDisabledBg: 'var(--variant-solidDisabledBg)'
  });
  return result;
};
exports.createVariant = createVariant;