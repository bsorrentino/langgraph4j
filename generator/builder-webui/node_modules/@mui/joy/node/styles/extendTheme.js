"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.createGetCssVar = void 0;
exports.default = extendTheme;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _utils = require("@mui/utils");
var _system = require("@mui/system");
var _createTheme = require("@mui/system/createTheme");
var _sxConfig = _interopRequireDefault(require("./sxConfig"));
var _colors = _interopRequireDefault(require("../colors"));
var _shouldSkipGeneratingVar = _interopRequireDefault(require("./shouldSkipGeneratingVar"));
var _className = require("../className");
var _variantUtils = require("./variantUtils");
const _excluded = ["cssVarPrefix", "breakpoints", "spacing", "components", "variants", "shouldSkipGeneratingVar"],
  _excluded2 = ["colorSchemes"];
// Use Partial2Level instead of PartialDeep because nested value type is CSSObject which does not work with PartialDeep.

const createGetCssVar = (cssVarPrefix = 'joy') => (0, _system.unstable_createGetCssVar)(cssVarPrefix);
exports.createGetCssVar = createGetCssVar;
function extendTheme(themeOptions) {
  var _scalesInput$colorSch, _scalesInput$colorSch2, _scalesInput$colorSch3, _scalesInput$colorSch4, _scalesInput$colorSch5, _scalesInput$colorSch6, _scalesInput$focus$th, _scalesInput$focus, _scalesInput$focus$th2, _scalesInput$focus2;
  const _ref = themeOptions || {},
    {
      cssVarPrefix = 'joy',
      breakpoints,
      spacing,
      components: componentsInput,
      variants: variantsInput,
      shouldSkipGeneratingVar = _shouldSkipGeneratingVar.default
    } = _ref,
    scalesInput = (0, _objectWithoutPropertiesLoose2.default)(_ref, _excluded);
  const getCssVar = createGetCssVar(cssVarPrefix);
  const defaultColors = {
    primary: _colors.default.blue,
    neutral: _colors.default.grey,
    danger: _colors.default.red,
    success: _colors.default.green,
    warning: _colors.default.yellow,
    common: {
      white: '#FFF',
      black: '#000'
    }
  };
  const getCssVarColor = cssVar => {
    var _defaultColors$color;
    const tokens = cssVar.split('-');
    const color = tokens[1];
    const index = tokens[2];

    // @ts-ignore
    return getCssVar(cssVar, (_defaultColors$color = defaultColors[color]) == null ? void 0 : _defaultColors$color[index]);
  };
  const createLightModeVariantVariables = color => ({
    plainColor: getCssVarColor(`palette-${color}-500`),
    plainHoverBg: getCssVarColor(`palette-${color}-100`),
    plainActiveBg: getCssVarColor(`palette-${color}-200`),
    plainDisabledColor: getCssVarColor(`palette-neutral-400`),
    outlinedColor: getCssVarColor(`palette-${color}-500`),
    outlinedBorder: getCssVarColor(`palette-${color}-300`),
    outlinedHoverBg: getCssVarColor(`palette-${color}-100`),
    outlinedActiveBg: getCssVarColor(`palette-${color}-200`),
    outlinedDisabledColor: getCssVarColor(`palette-neutral-400`),
    outlinedDisabledBorder: getCssVarColor(`palette-neutral-200`),
    softColor: getCssVarColor(`palette-${color}-700`),
    softBg: getCssVarColor(`palette-${color}-100`),
    softHoverBg: getCssVarColor(`palette-${color}-200`),
    softActiveColor: getCssVarColor(`palette-${color}-800`),
    softActiveBg: getCssVarColor(`palette-${color}-300`),
    softDisabledColor: getCssVarColor(`palette-neutral-400`),
    softDisabledBg: getCssVarColor(`palette-neutral-50`),
    solidColor: getCssVarColor(`palette-common-white`),
    solidBg: getCssVarColor(`palette-${color}-500`),
    solidHoverBg: getCssVarColor(`palette-${color}-600`),
    solidActiveBg: getCssVarColor(`palette-${color}-700`),
    solidDisabledColor: getCssVarColor(`palette-neutral-400`),
    solidDisabledBg: getCssVarColor(`palette-neutral-100`)
  });
  const createDarkModeVariantVariables = color => ({
    plainColor: getCssVarColor(`palette-${color}-300`),
    plainHoverBg: getCssVarColor(`palette-${color}-800`),
    plainActiveBg: getCssVarColor(`palette-${color}-700`),
    plainDisabledColor: getCssVarColor(`palette-neutral-500`),
    outlinedColor: getCssVarColor(`palette-${color}-200`),
    outlinedBorder: getCssVarColor(`palette-${color}-700`),
    outlinedHoverBg: getCssVarColor(`palette-${color}-800`),
    outlinedActiveBg: getCssVarColor(`palette-${color}-700`),
    outlinedDisabledColor: getCssVarColor(`palette-neutral-500`),
    outlinedDisabledBorder: getCssVarColor(`palette-neutral-800`),
    softColor: getCssVarColor(`palette-${color}-200`),
    softBg: getCssVarColor(`palette-${color}-800`),
    softHoverBg: getCssVarColor(`palette-${color}-700`),
    softActiveColor: getCssVarColor(`palette-${color}-100`),
    softActiveBg: getCssVarColor(`palette-${color}-600`),
    softDisabledColor: getCssVarColor(`palette-neutral-500`),
    softDisabledBg: getCssVarColor(`palette-neutral-800`),
    solidColor: getCssVarColor(`palette-common-white`),
    solidBg: getCssVarColor(`palette-${color}-500`),
    solidHoverBg: getCssVarColor(`palette-${color}-600`),
    solidActiveBg: getCssVarColor(`palette-${color}-700`),
    solidDisabledColor: getCssVarColor(`palette-neutral-500`),
    solidDisabledBg: getCssVarColor(`palette-neutral-800`)
  });
  const lightColorSystem = {
    palette: {
      mode: 'light',
      primary: (0, _extends2.default)({}, defaultColors.primary, createLightModeVariantVariables('primary')),
      neutral: (0, _extends2.default)({}, defaultColors.neutral, createLightModeVariantVariables('neutral'), {
        plainColor: getCssVarColor('palette-neutral-700'),
        plainHoverColor: getCssVarColor(`palette-neutral-900`),
        outlinedColor: getCssVarColor('palette-neutral-700')
      }),
      danger: (0, _extends2.default)({}, defaultColors.danger, createLightModeVariantVariables('danger')),
      success: (0, _extends2.default)({}, defaultColors.success, createLightModeVariantVariables('success')),
      warning: (0, _extends2.default)({}, defaultColors.warning, createLightModeVariantVariables('warning')),
      common: {
        white: '#FFF',
        black: '#000'
      },
      text: {
        primary: getCssVarColor('palette-neutral-800'),
        secondary: getCssVarColor('palette-neutral-700'),
        tertiary: getCssVarColor('palette-neutral-600'),
        icon: getCssVarColor('palette-neutral-500')
      },
      background: {
        body: getCssVarColor('palette-common-white'),
        surface: getCssVarColor('palette-neutral-50'),
        popup: getCssVarColor('palette-common-white'),
        level1: getCssVarColor('palette-neutral-100'),
        level2: getCssVarColor('palette-neutral-200'),
        level3: getCssVarColor('palette-neutral-300'),
        tooltip: getCssVarColor('palette-neutral-500'),
        backdrop: `rgba(${getCssVar('palette-neutral-darkChannel', (0, _system.colorChannel)(defaultColors.neutral[900]) // should be the same index as in `attachColorChannels`
        )} / 0.25)`
      },
      divider: `rgba(${getCssVar('palette-neutral-mainChannel', (0, _system.colorChannel)(defaultColors.neutral[500]) // should be the same index as in `attachColorChannels`
      )} / 0.2)`,
      focusVisible: getCssVarColor('palette-primary-500')
    },
    shadowRing: '0 0 #000',
    shadowChannel: '21 21 21',
    shadowOpacity: '0.08'
  };
  const darkColorSystem = {
    palette: {
      mode: 'dark',
      primary: (0, _extends2.default)({}, defaultColors.primary, createDarkModeVariantVariables('primary')),
      neutral: (0, _extends2.default)({}, defaultColors.neutral, createDarkModeVariantVariables('neutral'), {
        plainColor: getCssVarColor('palette-neutral-300'),
        plainHoverColor: getCssVarColor(`palette-neutral-300`)
      }),
      danger: (0, _extends2.default)({}, defaultColors.danger, createDarkModeVariantVariables('danger')),
      success: (0, _extends2.default)({}, defaultColors.success, createDarkModeVariantVariables('success')),
      warning: (0, _extends2.default)({}, defaultColors.warning, createDarkModeVariantVariables('warning')),
      common: {
        white: '#FFF',
        black: '#000'
      },
      text: {
        primary: getCssVarColor('palette-neutral-100'),
        secondary: getCssVarColor('palette-neutral-300'),
        tertiary: getCssVarColor('palette-neutral-400'),
        icon: getCssVarColor('palette-neutral-400')
      },
      background: {
        body: getCssVarColor('palette-common-black'),
        surface: getCssVarColor('palette-neutral-900'),
        popup: getCssVarColor('palette-common-black'),
        level1: getCssVarColor('palette-neutral-800'),
        level2: getCssVarColor('palette-neutral-700'),
        level3: getCssVarColor('palette-neutral-600'),
        tooltip: getCssVarColor('palette-neutral-600'),
        backdrop: `rgba(${getCssVar('palette-neutral-darkChannel', (0, _system.colorChannel)(defaultColors.neutral[50]) // should be the same index as in `attachColorChannels`
        )} / 0.25)`
      },
      divider: `rgba(${getCssVar('palette-neutral-mainChannel', (0, _system.colorChannel)(defaultColors.neutral[500]) // should be the same index as in `attachColorChannels`
      )} / 0.16)`,
      focusVisible: getCssVarColor('palette-primary-500')
    },
    shadowRing: '0 0 #000',
    shadowChannel: '0 0 0',
    shadowOpacity: '0.6'
  };
  const fontFamilyFallback = '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"';
  const fontFamily = (0, _extends2.default)({
    body: `"Inter", ${getCssVar(`fontFamily-fallback, ${fontFamilyFallback}`)}`,
    display: `"Inter", ${getCssVar(`fontFamily-fallback, ${fontFamilyFallback}`)}`,
    code: 'Source Code Pro,ui-monospace,SFMono-Regular,Menlo,Monaco,Consolas,Liberation Mono,Courier New,monospace',
    fallback: fontFamilyFallback
  }, scalesInput.fontFamily);
  const fontWeight = (0, _extends2.default)({
    sm: 300,
    // regular
    md: 500,
    // medium
    lg: 600,
    // semi-bold
    xl: 700
  }, scalesInput.fontWeight);
  const fontSize = (0, _extends2.default)({
    xs: '0.75rem',
    // 12px
    sm: '0.875rem',
    // 14px
    md: '1rem',
    // 16px
    lg: '1.125rem',
    // 18px
    xl: '1.25rem',
    // 20px
    xl2: '1.5rem',
    // 24px
    xl3: '1.875rem',
    // 30px
    xl4: '2.25rem'
  }, scalesInput.fontSize);
  const lineHeight = (0, _extends2.default)({
    xs: '1.33334',
    // largest font sizes: h1, h2
    sm: '1.42858',
    // normal font sizes
    md: '1.5',
    // normal font sizes
    lg: '1.55556',
    // large font sizes for components
    xl: '1.66667'
  }, scalesInput.lineHeight);
  const defaultShadowRing = (_scalesInput$colorSch = (_scalesInput$colorSch2 = scalesInput.colorSchemes) == null || (_scalesInput$colorSch2 = _scalesInput$colorSch2.light) == null ? void 0 : _scalesInput$colorSch2.shadowRing) != null ? _scalesInput$colorSch : lightColorSystem.shadowRing;
  const defaultShadowChannel = (_scalesInput$colorSch3 = (_scalesInput$colorSch4 = scalesInput.colorSchemes) == null || (_scalesInput$colorSch4 = _scalesInput$colorSch4.light) == null ? void 0 : _scalesInput$colorSch4.shadowChannel) != null ? _scalesInput$colorSch3 : lightColorSystem.shadowChannel;
  const defaultShadowOpacity = (_scalesInput$colorSch5 = (_scalesInput$colorSch6 = scalesInput.colorSchemes) == null || (_scalesInput$colorSch6 = _scalesInput$colorSch6.light) == null ? void 0 : _scalesInput$colorSch6.shadowOpacity) != null ? _scalesInput$colorSch5 : lightColorSystem.shadowOpacity;
  const defaultScales = {
    colorSchemes: {
      light: lightColorSystem,
      dark: darkColorSystem
    },
    fontSize,
    fontFamily,
    fontWeight,
    focus: {
      thickness: '2px',
      selector: `&.${(0, _className.generateUtilityClass)('', 'focusVisible')}, &:focus-visible`,
      default: {
        outlineOffset: `var(--focus-outline-offset, ${getCssVar('focus-thickness', (_scalesInput$focus$th = (_scalesInput$focus = scalesInput.focus) == null ? void 0 : _scalesInput$focus.thickness) != null ? _scalesInput$focus$th : '2px')})`,
        outline: `${getCssVar('focus-thickness', (_scalesInput$focus$th2 = (_scalesInput$focus2 = scalesInput.focus) == null ? void 0 : _scalesInput$focus2.thickness) != null ? _scalesInput$focus$th2 : '2px')} solid ${getCssVar('palette-focusVisible', defaultColors.primary[500])}`
      }
    },
    lineHeight,
    radius: {
      xs: '2px',
      sm: '6px',
      md: '8px',
      lg: '12px',
      xl: '16px'
    },
    shadow: {
      xs: `${getCssVar('shadowRing', defaultShadowRing)}, 0px 1px 2px 0px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)})`,
      sm: `${getCssVar('shadowRing', defaultShadowRing)}, 0px 1px 2px 0px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)}), 0px 2px 4px 0px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)})`,
      md: `${getCssVar('shadowRing', defaultShadowRing)}, 0px 2px 8px -2px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)}), 0px 6px 12px -2px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)})`,
      lg: `${getCssVar('shadowRing', defaultShadowRing)}, 0px 2px 8px -2px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)}), 0px 12px 16px -4px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)})`,
      xl: `${getCssVar('shadowRing', defaultShadowRing)}, 0px 2px 8px -2px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)}), 0px 20px 24px -4px rgba(${getCssVar('shadowChannel', defaultShadowChannel)} / ${getCssVar('shadowOpacity', defaultShadowOpacity)})`
    },
    zIndex: {
      badge: 1,
      table: 10,
      popup: 1000,
      modal: 1300,
      snackbar: 1400,
      tooltip: 1500
    },
    typography: {
      h1: {
        fontFamily: getCssVar(`fontFamily-display, ${fontFamily.display}`),
        fontWeight: getCssVar(`fontWeight-xl, ${fontWeight.xl}`),
        fontSize: getCssVar(`fontSize-xl4, ${fontSize.xl4}`),
        lineHeight: getCssVar(`lineHeight-xs, ${lineHeight.xs}`),
        letterSpacing: '-0.025em',
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      h2: {
        fontFamily: getCssVar(`fontFamily-display, ${fontFamily.display}`),
        fontWeight: getCssVar(`fontWeight-xl, ${fontWeight.xl}`),
        fontSize: getCssVar(`fontSize-xl3, ${fontSize.xl3}`),
        lineHeight: getCssVar(`lineHeight-xs, ${lineHeight.xs}`),
        letterSpacing: '-0.025em',
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      h3: {
        fontFamily: getCssVar(`fontFamily-display, ${fontFamily.display}`),
        fontWeight: getCssVar(`fontWeight-lg, ${fontWeight.lg}`),
        fontSize: getCssVar(`fontSize-xl2, ${fontSize.xl2}`),
        lineHeight: getCssVar(`lineHeight-xs, ${lineHeight.xs}`),
        letterSpacing: '-0.025em',
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      h4: {
        fontFamily: getCssVar(`fontFamily-display, ${fontFamily.display}`),
        fontWeight: getCssVar(`fontWeight-lg, ${fontWeight.lg}`),
        fontSize: getCssVar(`fontSize-xl, ${fontSize.xl}`),
        lineHeight: getCssVar(`lineHeight-md, ${lineHeight.md}`),
        letterSpacing: '-0.025em',
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      'title-lg': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontWeight: getCssVar(`fontWeight-lg, ${fontWeight.lg}`),
        fontSize: getCssVar(`fontSize-lg, ${fontSize.lg}`),
        lineHeight: getCssVar(`lineHeight-xs, ${lineHeight.xs}`),
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      'title-md': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontWeight: getCssVar(`fontWeight-md, ${fontWeight.md}`),
        fontSize: getCssVar(`fontSize-md, ${fontSize.md}`),
        lineHeight: getCssVar(`lineHeight-md, ${lineHeight.md}`),
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      'title-sm': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontWeight: getCssVar(`fontWeight-md, ${fontWeight.md}`),
        fontSize: getCssVar(`fontSize-sm, ${fontSize.sm}`),
        lineHeight: getCssVar(`lineHeight-sm, ${lineHeight.sm}`),
        color: getCssVar(`palette-text-primary, ${lightColorSystem.palette.text.primary}`)
      },
      'body-lg': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontSize: getCssVar(`fontSize-lg, ${fontSize.lg}`),
        lineHeight: getCssVar(`lineHeight-md, ${lineHeight.md}`),
        color: getCssVar(`palette-text-secondary, ${lightColorSystem.palette.text.secondary}`)
      },
      'body-md': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontSize: getCssVar(`fontSize-md, ${fontSize.md}`),
        lineHeight: getCssVar(`lineHeight-md, ${lineHeight.md}`),
        color: getCssVar(`palette-text-secondary, ${lightColorSystem.palette.text.secondary}`)
      },
      'body-sm': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontSize: getCssVar(`fontSize-sm, ${fontSize.sm}`),
        lineHeight: getCssVar(`lineHeight-md, ${lineHeight.md}`),
        color: getCssVar(`palette-text-tertiary, ${lightColorSystem.palette.text.tertiary}`)
      },
      'body-xs': {
        fontFamily: getCssVar(`fontFamily-body, ${fontFamily.body}`),
        fontWeight: getCssVar(`fontWeight-md, ${fontWeight.md}`),
        fontSize: getCssVar(`fontSize-xs, ${fontSize.xs}`),
        lineHeight: getCssVar(`lineHeight-md, ${lineHeight.md}`),
        color: getCssVar(`palette-text-tertiary, ${lightColorSystem.palette.text.tertiary}`)
      }
    }
  };
  const _ref2 = scalesInput ? (0, _utils.deepmerge)(defaultScales, scalesInput) : defaultScales,
    {
      colorSchemes
    } = _ref2,
    mergedScales = (0, _objectWithoutPropertiesLoose2.default)(_ref2, _excluded2);
  const theme = (0, _extends2.default)({
    colorSchemes
  }, mergedScales, {
    breakpoints: (0, _system.createBreakpoints)(breakpoints != null ? breakpoints : {}),
    components: (0, _utils.deepmerge)({
      // TODO: find a way to abstract SvgIcon out of @mui/material
      MuiSvgIcon: {
        defaultProps: {
          fontSize: 'xl2'
        },
        styleOverrides: {
          root: ({
            ownerState,
            theme: themeProp
          }) => {
            var _themeProp$vars$palet;
            const instanceFontSize = ownerState.instanceFontSize;
            return (0, _extends2.default)({
              margin: 'var(--Icon-margin)'
            }, ownerState.fontSize && ownerState.fontSize !== 'inherit' && {
              fontSize: `var(--Icon-fontSize, ${themeProp.vars.fontSize[ownerState.fontSize]})`
            }, !ownerState.htmlColor && (0, _extends2.default)({
              color: `var(--Icon-color, ${theme.vars.palette.text.icon})`
            }, ownerState.color && ownerState.color !== 'inherit' && themeProp.vars.palette[ownerState.color] && {
              color: `rgba(${(_themeProp$vars$palet = themeProp.vars.palette[ownerState.color]) == null ? void 0 : _themeProp$vars$palet.mainChannel} / 1)`
            }), instanceFontSize && instanceFontSize !== 'inherit' && {
              '--Icon-fontSize': themeProp.vars.fontSize[instanceFontSize]
            });
          }
        }
      }
    }, componentsInput),
    cssVarPrefix,
    getCssVar,
    spacing: (0, _system.createSpacing)(spacing)
  }); // Need type casting due to module augmentation inside the repo

  /**
   Color channels generation
  */
  function attachColorChannels(supportedColorScheme, palette) {
    Object.keys(palette).forEach(key => {
      const channelMapping = {
        main: '500',
        light: '200',
        dark: '700'
      };
      if (supportedColorScheme === 'dark') {
        // @ts-ignore internal
        channelMapping.main = 400;
      }
      if (!palette[key].mainChannel && palette[key][channelMapping.main]) {
        palette[key].mainChannel = (0, _system.colorChannel)(palette[key][channelMapping.main]);
      }
      if (!palette[key].lightChannel && palette[key][channelMapping.light]) {
        palette[key].lightChannel = (0, _system.colorChannel)(palette[key][channelMapping.light]);
      }
      if (!palette[key].darkChannel && palette[key][channelMapping.dark]) {
        palette[key].darkChannel = (0, _system.colorChannel)(palette[key][channelMapping.dark]);
      }
    });
  }
  // Set the channels
  Object.entries(theme.colorSchemes).forEach(([supportedColorScheme, colorSystem]) => {
    attachColorChannels(supportedColorScheme, colorSystem.palette);
  });

  // ===============================================================
  // Create `theme.vars` that contain `var(--*)` as values
  // ===============================================================
  const parserConfig = {
    prefix: cssVarPrefix,
    shouldSkipGeneratingVar
  };
  const {
    vars: themeVars,
    generateCssVars
  } = (0, _system.unstable_prepareCssVars)( // @ts-ignore property truDark is missing from colorSchemes
  (0, _extends2.default)({
    colorSchemes
  }, mergedScales), parserConfig);
  theme.vars = themeVars;
  theme.generateCssVars = generateCssVars;
  theme.unstable_sxConfig = (0, _extends2.default)({}, _sxConfig.default, themeOptions == null ? void 0 : themeOptions.unstable_sxConfig);
  theme.unstable_sx = function sx(props) {
    return (0, _system.unstable_styleFunctionSx)({
      sx: props,
      theme: this
    });
  };
  theme.getColorSchemeSelector = colorScheme => colorScheme === 'light' ? '&' : `&[data-joy-color-scheme="${colorScheme}"], [data-joy-color-scheme="${colorScheme}"] &`;
  const createVariantInput = {
    getCssVar,
    palette: theme.colorSchemes.light.palette
  };
  theme.variants = (0, _utils.deepmerge)({
    plain: (0, _variantUtils.createVariant)('plain', createVariantInput),
    plainHover: (0, _variantUtils.createVariant)('plainHover', createVariantInput),
    plainActive: (0, _variantUtils.createVariant)('plainActive', createVariantInput),
    plainDisabled: (0, _variantUtils.createVariant)('plainDisabled', createVariantInput),
    outlined: (0, _variantUtils.createVariant)('outlined', createVariantInput),
    outlinedHover: (0, _variantUtils.createVariant)('outlinedHover', createVariantInput),
    outlinedActive: (0, _variantUtils.createVariant)('outlinedActive', createVariantInput),
    outlinedDisabled: (0, _variantUtils.createVariant)('outlinedDisabled', createVariantInput),
    soft: (0, _variantUtils.createVariant)('soft', createVariantInput),
    softHover: (0, _variantUtils.createVariant)('softHover', createVariantInput),
    softActive: (0, _variantUtils.createVariant)('softActive', createVariantInput),
    softDisabled: (0, _variantUtils.createVariant)('softDisabled', createVariantInput),
    solid: (0, _variantUtils.createVariant)('solid', createVariantInput),
    solidHover: (0, _variantUtils.createVariant)('solidHover', createVariantInput),
    solidActive: (0, _variantUtils.createVariant)('solidActive', createVariantInput),
    solidDisabled: (0, _variantUtils.createVariant)('solidDisabled', createVariantInput)
  }, variantsInput);
  theme.palette = (0, _extends2.default)({}, theme.colorSchemes.light.palette, {
    colorScheme: 'light'
  });
  theme.shouldSkipGeneratingVar = shouldSkipGeneratingVar;
  theme.applyStyles = _createTheme.unstable_applyStyles;
  return theme;
}