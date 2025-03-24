"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.SheetRoot = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _clsx = _interopRequireDefault(require("clsx"));
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _system = require("@mui/system");
var _styles = require("../styles");
var _colorInversion = require("../colorInversion");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _styleUtils = require("../styles/styleUtils");
var _sheetClasses = require("./sheetClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "color", "component", "variant", "invertedColors", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color
  } = ownerState;
  const slots = {
    root: ['root', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _sheetClasses.getSheetUtilityClass, {});
};
const SheetRoot = exports.SheetRoot = (0, _styled.default)('div', {
  name: 'JoySheet',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants, _theme$variants2;
  const variantStyle = (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color];
  const {
    borderRadius: childRadius,
    bgcolor,
    backgroundColor,
    background
  } = (0, _styleUtils.resolveSxValue)({
    theme,
    ownerState
  }, ['borderRadius', 'bgcolor', 'backgroundColor', 'background']);
  const resolvedBg = (0, _system.getPath)(theme, `palette.${bgcolor}`) || bgcolor || (0, _system.getPath)(theme, `palette.${backgroundColor}`) || backgroundColor || background || (variantStyle == null ? void 0 : variantStyle.backgroundColor) || (variantStyle == null ? void 0 : variantStyle.background) || theme.vars.palette.background.surface;
  return [(0, _extends2.default)({
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon,
    '--ListItem-stickyBackground': resolvedBg === 'transparent' ? 'initial' : resolvedBg,
    // for sticky List
    '--Sheet-background': resolvedBg === 'transparent' ? 'initial' : resolvedBg
  }, childRadius !== undefined && {
    '--List-radius': `calc(${childRadius} - var(--variant-borderWidth, 0px))`,
    '--unstable_actionRadius': `calc(${childRadius} - var(--variant-borderWidth, 0px))`
  }, {
    backgroundColor: theme.vars.palette.background.surface,
    position: 'relative'
  }), (0, _extends2.default)({}, theme.typography['body-md'], ownerState.variant === 'solid' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySolidInversion)(ownerState.color)(theme), ownerState.variant === 'soft' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySoftInversion)(ownerState.color)(theme), (_theme$variants2 = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants2[ownerState.color], variantStyle)];
});
/**
 *
 * Demos:
 *
 * - [Sheet](https://mui.com/joy-ui/react-sheet/)
 *
 * API:
 *
 * - [Sheet API](https://mui.com/joy-ui/api/sheet/)
 */
const Sheet = /*#__PURE__*/React.forwardRef(function Sheet(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoySheet'
  });
  const {
      className,
      color = 'neutral',
      component = 'div',
      variant = 'plain',
      invertedColors = false,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    component,
    invertedColors,
    variant
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: SheetRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps));
});
process.env.NODE_ENV !== "production" ? Sheet.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The content of the component.
   */
  children: _propTypes.default.node,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   * @default 'neutral'
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * If `true`, the children with an implicit color prop invert their colors to match the component's variant and color.
   * @default false
   */
  invertedColors: _propTypes.default.bool,
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    root: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Sheet;