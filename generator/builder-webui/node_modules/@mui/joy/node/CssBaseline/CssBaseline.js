"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _GlobalStyles = _interopRequireDefault(require("../GlobalStyles"));
var _defaultTheme = _interopRequireDefault(require("../styles/defaultTheme"));
var _jsxRuntime = require("react/jsx-runtime");
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
/**
 * Kickstart an elegant, consistent, and simple baseline to build upon.
 *
 * Demos:
 *
 * - [CSS Baseline](https://mui.com/joy-ui/react-css-baseline/)
 *
 * API:
 *
 * - [CssBaseline API](https://mui.com/joy-ui/api/css-baseline/)
 */
function CssBaseline(props) {
  const {
    children,
    disableColorScheme = false
  } = props;
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(React.Fragment, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(_GlobalStyles.default, {
      styles: theme => {
        var _components$JoyTypogr, _components;
        const colorSchemeStyles = {};
        if (!disableColorScheme) {
          // The CssBaseline is wrapped inside a CssVarsProvider
          Object.entries(theme.colorSchemes).forEach(([key, scheme]) => {
            var _scheme$palette;
            colorSchemeStyles[theme.getColorSchemeSelector(key).replace(/\s*&/, '')] = {
              colorScheme: (_scheme$palette = scheme.palette) == null ? void 0 : _scheme$palette.mode
            };
          });
        }
        const defaultTypographyLevel = (_components$JoyTypogr = (_components = theme.components) == null || (_components = _components.JoyTypography) == null || (_components = _components.defaultProps) == null ? void 0 : _components.level) != null ? _components$JoyTypogr : 'body-md';
        return (0, _extends2.default)({
          html: {
            WebkitFontSmoothing: 'antialiased',
            MozOsxFontSmoothing: 'grayscale',
            // Change from `box-sizing: content-box` so that `width`
            // is not affected by `padding` or `border`.
            boxSizing: 'border-box',
            // Fix font resize problem in iOS
            WebkitTextSizeAdjust: '100%'
          },
          '*, *::before, *::after': {
            boxSizing: 'inherit'
          },
          'strong, b': {
            fontWeight: theme.vars.fontWeight.lg
          },
          body: (0, _extends2.default)({
            margin: 0,
            // Remove the margin in all browsers.
            color: theme.vars.palette.text.primary,
            fontFamily: theme.vars.fontFamily.body
          }, theme.typography[defaultTypographyLevel], {
            backgroundColor: theme.vars.palette.background.body,
            '@media print': {
              // Save printer ink.
              backgroundColor: theme.vars.palette.common.white
            },
            // Add support for document.body.requestFullScreen().
            // Other elements, if background transparent, are not supported.
            '&::backdrop': {
              backgroundColor: theme.vars.palette.background.backdrop
            }
          })
        }, colorSchemeStyles);
      },
      defaultTheme: _defaultTheme.default
    }), children]
  });
}
process.env.NODE_ENV !== "production" ? CssBaseline.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * You can wrap a node.
   */
  children: _propTypes.default.node,
  /**
   * Disable `color-scheme` CSS property.
   *
   * For more details, check out https://developer.mozilla.org/en-US/docs/Web/CSS/color-scheme
   * For browser support, check out https://caniuse.com/?search=color-scheme
   * @default false
   */
  disableColorScheme: _propTypes.default.bool
} : void 0;
var _default = exports.default = CssBaseline;