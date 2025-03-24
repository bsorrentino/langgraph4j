"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _clsx = _interopRequireDefault(require("clsx"));
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _colorInversion = require("../colorInversion");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _alertClasses = require("./alertClasses");
var _styleUtils = require("../styles/styleUtils");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "className", "color", "invertedColors", "role", "variant", "size", "startDecorator", "endDecorator", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    size
  } = ownerState;
  const slots = {
    root: ['root', size && `size${(0, _utils.unstable_capitalize)(size)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator']
  };
  return (0, _base.unstable_composeClasses)(slots, _alertClasses.getAlertUtilityClass, {});
};
const AlertRoot = (0, _styled.default)('div', {
  name: 'JoyAlert',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants;
  const {
    p,
    padding,
    borderRadius
  } = (0, _styleUtils.resolveSxValue)({
    theme,
    ownerState
  }, ['p', 'padding', 'borderRadius']);
  return [(0, _extends2.default)({
    '--Alert-radius': theme.vars.radius.sm,
    '--Alert-decoratorChildRadius': 'max((var(--Alert-radius) - var(--variant-borderWidth, 0px)) - var(--Alert-padding), min(var(--Alert-padding) + var(--variant-borderWidth, 0px), var(--Alert-radius) / 2))',
    '--Button-minHeight': 'var(--Alert-decoratorChildHeight)',
    '--IconButton-size': 'var(--Alert-decoratorChildHeight)',
    '--Button-radius': 'var(--Alert-decoratorChildRadius)',
    '--IconButton-radius': 'var(--Alert-decoratorChildRadius)',
    '--Icon-color': 'currentColor'
  }, ownerState.size === 'sm' && {
    '--Alert-padding': '0.5rem',
    '--Alert-decoratorChildHeight': '1.5rem',
    '--Icon-fontSize': theme.vars.fontSize.xl,
    gap: '0.5rem'
  }, ownerState.size === 'md' && {
    '--Alert-padding': '0.75rem',
    '--Alert-decoratorChildHeight': '2rem',
    '--Icon-fontSize': theme.vars.fontSize.xl,
    gap: '0.625rem'
  }, ownerState.size === 'lg' && {
    '--Alert-padding': '1rem',
    '--Alert-decoratorChildHeight': '2.375rem',
    '--Icon-fontSize': theme.vars.fontSize.xl2,
    gap: '0.875rem'
  }, {
    backgroundColor: theme.vars.palette.background.surface,
    display: 'flex',
    position: 'relative',
    alignItems: 'center',
    padding: `var(--Alert-padding)`,
    borderRadius: 'var(--Alert-radius)'
  }, theme.typography[`body-${{
    sm: 'xs',
    md: 'sm',
    lg: 'md'
  }[ownerState.size]}`], {
    fontWeight: theme.vars.fontWeight.md
  }, ownerState.variant === 'solid' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySolidInversion)(ownerState.color)(theme), ownerState.variant === 'soft' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySoftInversion)(ownerState.color)(theme), (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]), p !== undefined && {
    '--Alert-padding': p
  }, padding !== undefined && {
    '--Alert-padding': padding
  }, borderRadius !== undefined && {
    '--Alert-radius': borderRadius
  }];
});
const AlertStartDecorator = (0, _styled.default)('span', {
  name: 'JoyAlert',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})({
  display: 'inherit',
  flex: 'none'
});
const AlertEndDecorator = (0, _styled.default)('span', {
  name: 'JoyAlert',
  slot: 'EndDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})({
  display: 'inherit',
  flex: 'none',
  marginLeft: 'auto'
});
/**
 *
 * Demos:
 *
 * - [Alert](https://mui.com/joy-ui/react-alert/)
 *
 * API:
 *
 * - [Alert API](https://mui.com/joy-ui/api/alert/)
 */
const Alert = /*#__PURE__*/React.forwardRef(function Alert(inProps, ref) {
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'JoyAlert'
  });
  const {
      children,
      className,
      color = 'neutral',
      invertedColors = false,
      role = 'alert',
      variant = 'soft',
      size = 'md',
      startDecorator,
      endDecorator,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    invertedColors,
    variant,
    size
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
    elementType: AlertRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      role
    }
  });
  const [SlotStartDecorator, startDecoratorProps] = (0, _useSlot.default)('startDecorator', {
    className: classes.startDecorator,
    elementType: AlertStartDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotEndDecorator, endDecoratorProps] = (0, _useSlot.default)('endDecorator', {
    className: classes.endDecorator,
    elementType: AlertEndDecorator,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
      children: startDecorator
    })), children, endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
      children: endDecorator
    }))]
  }));
});
process.env.NODE_ENV !== "production" ? Alert.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * @ignore
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
   * Element placed after the children.
   */
  endDecorator: _propTypes.default.node,
  /**
   * If `true`, the children with an implicit color prop invert their colors to match the component's variant and color.
   * @default false
   */
  invertedColors: _propTypes.default.bool,
  /**
   * The ARIA role attribute of the element.
   * @default 'alert'
   */
  role: _propTypes.default.string,
  /**
   * The size of the component.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['sm', 'md', 'lg']), _propTypes.default.string]),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    endDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    startDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    endDecorator: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    startDecorator: _propTypes.default.elementType
  }),
  /**
   * Element placed before the children.
   */
  startDecorator: _propTypes.default.node,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'soft'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Alert;