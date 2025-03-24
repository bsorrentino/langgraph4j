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
var _ClickAwayListener = require("@mui/base/ClickAwayListener");
var _useSnackbar = require("@mui/base/useSnackbar");
var _utils = require("@mui/utils");
var _system = require("@mui/system");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _styles = require("../styles");
var _styleUtils = require("../styles/styleUtils");
var _colorInversion = require("../colorInversion");
var _snackbarClasses = require("./snackbarClasses");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["anchorOrigin", "animationDuration", "autoHideDuration", "color", "children", "className", "component", "disableWindowBlurListener", "endDecorator", "invertedColors", "onBlur", "onClose", "onFocus", "onMouseEnter", "onMouseLeave", "onUnmount", "open", "resumeHideDuration", "size", "slots", "slotProps", "startDecorator", "variant"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    size,
    anchorOrigin
  } = ownerState;
  const slots = {
    root: ['root', size && `size${(0, _utils.unstable_capitalize)(size)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, `anchorOrigin${(0, _utils.unstable_capitalize)(anchorOrigin.vertical)}${(0, _utils.unstable_capitalize)(anchorOrigin.horizontal)}`],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator']
  };
  return (0, _base.unstable_composeClasses)(slots, _snackbarClasses.getSnackbarUtilityClass, {});
};
const enterAnimation = (0, _system.keyframes)`
  0% {
    transform: translateX(var(--Snackbar-translateX, 0px)) translateY(calc(var(--_Snackbar-anchorBottom, 1) * 100%));
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    transform: translateX(var(--Snackbar-translateX, 0px)) translateY(0);
  }
`;
const exitAnimation = (0, _system.keyframes)`
  0% {
    transform: translateX(var(--Snackbar-translateX, 0px)) translateY(0);
    opacity: 1;
  }
  100% {
    transform: translateX(var(--Snackbar-translateX, 0px)) translateY(calc(var(--_Snackbar-anchorBottom, 1) * 100%));
    opacity: 0;
  }
`;
const SnackbarRoot = (0, _styled.default)('div', {
  name: 'JoySnackbar',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _ownerState$anchorOri, _ownerState$anchorOri2, _ownerState$anchorOri3, _ownerState$anchorOri4, _ownerState$anchorOri5, _ownerState$anchorOri6, _theme$variants;
  const {
    p,
    padding,
    borderRadius
  } = (0, _styleUtils.resolveSxValue)({
    theme,
    ownerState
  }, ['p', 'padding', 'borderRadius']);
  return [(0, _extends2.default)({
    '--Snackbar-radius': theme.vars.radius.sm,
    '--Snackbar-decoratorChildRadius': 'max((var(--Snackbar-radius) - var(--variant-borderWidth, 0px)) - var(--Snackbar-padding), min(var(--Snackbar-padding) + var(--variant-borderWidth, 0px), var(--Snackbar-radius) / 2))',
    '--Button-minHeight': 'var(--Snackbar-decoratorChildHeight)',
    '--IconButton-size': 'var(--Snackbar-decoratorChildHeight)',
    '--Button-radius': 'var(--Snackbar-decoratorChildRadius)',
    '--IconButton-radius': 'var(--Snackbar-decoratorChildRadius)',
    '--Icon-color': 'currentColor'
  }, ownerState.size === 'sm' && {
    '--Snackbar-padding': '0.75rem',
    '--Snackbar-inset': '0.5rem',
    '--Snackbar-decoratorChildHeight': '1.5rem',
    '--Icon-fontSize': theme.vars.fontSize.xl,
    gap: '0.5rem'
  }, ownerState.size === 'md' && {
    '--Snackbar-padding': '1rem',
    '--Snackbar-inset': '0.75rem',
    // the spacing between Snackbar and the viewport
    '--Snackbar-decoratorChildHeight': '2rem',
    '--Icon-fontSize': theme.vars.fontSize.xl,
    gap: '0.625rem'
  }, ownerState.size === 'lg' && {
    '--Snackbar-padding': '1.25rem',
    '--Snackbar-inset': '1rem',
    '--Snackbar-decoratorChildHeight': '2.375rem',
    '--Icon-fontSize': theme.vars.fontSize.xl2,
    gap: '0.875rem'
  }, {
    zIndex: theme.vars.zIndex.snackbar,
    position: 'fixed',
    display: 'flex',
    alignItems: 'center',
    minWidth: 300,
    top: ((_ownerState$anchorOri = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri.vertical) === 'top' ? 'var(--Snackbar-inset)' : undefined,
    left: ((_ownerState$anchorOri2 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri2.horizontal) === 'left' ? 'var(--Snackbar-inset)' : undefined,
    bottom: ((_ownerState$anchorOri3 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri3.vertical) === 'bottom' ? 'var(--Snackbar-inset)' : undefined,
    right: ((_ownerState$anchorOri4 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri4.horizontal) === 'right' ? 'var(--Snackbar-inset)' : undefined
  }, ((_ownerState$anchorOri5 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri5.horizontal) === 'center' && {
    '--Snackbar-translateX': '-50%',
    left: '50%',
    transform: 'translateX(var(--Snackbar-translateX))'
  }, ((_ownerState$anchorOri6 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri6.vertical) === 'top' && {
    '--_Snackbar-anchorBottom': '-1'
  }, {
    animation: `${enterAnimation} ${ownerState.animationDuration}ms forwards`
  }, !ownerState.open && {
    animationName: exitAnimation
  }, {
    boxShadow: theme.vars.shadow.lg,
    backgroundColor: theme.vars.palette.background.surface,
    padding: `var(--Snackbar-padding)`,
    borderRadius: 'var(--Snackbar-radius)'
  }, theme.typography[`body-${{
    sm: 'xs',
    md: 'sm',
    lg: 'md'
  }[ownerState.size]}`], ownerState.variant === 'solid' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySolidInversion)(ownerState.color)(theme), ownerState.variant === 'soft' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySoftInversion)(ownerState.color)(theme), (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]), p !== undefined && {
    '--Snackbar-padding': p
  }, padding !== undefined && {
    '--Snackbar-padding': padding
  }, borderRadius !== undefined && {
    '--Snackbar-radius': borderRadius
  }];
});
const SnackbarStartDecorator = (0, _styled.default)('span', {
  name: 'JoySnackbar',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})({
  display: 'inherit',
  flex: 'none'
});
const SnackbarEndDecorator = (0, _styled.default)('span', {
  name: 'JoySnackbar',
  slot: 'EndDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})({
  display: 'inherit',
  flex: 'none',
  marginLeft: 'auto'
});
const defaultAnchorOrigin = {
  vertical: 'bottom',
  horizontal: 'right'
};

/**
 *
 * Demos:
 *
 * - [Snackbar](https://mui.com/joy-ui/react-snackbar/)
 *
 * API:
 *
 * - [Snackbar API](https://mui.com/joy-ui/api/snackbar/)
 */
const Snackbar = /*#__PURE__*/React.forwardRef(function Snackbar(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoySnackbar'
  });
  const {
      anchorOrigin = defaultAnchorOrigin,
      animationDuration = 300,
      autoHideDuration = null,
      color = 'neutral',
      children,
      className,
      component,
      disableWindowBlurListener = false,
      endDecorator,
      invertedColors = false,
      onUnmount,
      open,
      size = 'md',
      slots = {},
      slotProps,
      startDecorator,
      variant = 'outlined'
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);

  // For animation
  const [exited, setExited] = React.useState(true);

  // `exiting` is a state for preventing click away event during exiting
  // because there is a case where the Snackbar is exiting and the user open a Snackbar again.
  // Without this state, the snack will open and close immediately since click away is called immediately after the click event.
  const [exiting, setExiting] = React.useState(false);

  // To call a function when the component is about to be unmounted.
  // Useful for preserving content in the Snackbar when undergoing exit animation.
  const unmountRef = React.useRef(onUnmount);
  unmountRef.current = onUnmount;
  React.useEffect(() => {
    if (open) {
      setExiting(false);
      setExited(false);
    } else {
      setExiting(true);
      const timer = setTimeout(() => {
        var _unmountRef$current;
        setExited(true);
        setExiting(false);
        (_unmountRef$current = unmountRef.current) == null || _unmountRef$current.call(unmountRef);
      }, animationDuration);
      return () => {
        clearTimeout(timer);
      };
    }
    return undefined;
  }, [open, animationDuration]);
  const ownerState = (0, _extends2.default)({}, props, {
    anchorOrigin,
    autoHideDuration,
    color,
    animationDuration,
    disableWindowBlurListener,
    invertedColors,
    size,
    variant
  });
  delete ownerState.onUnmount; // `on*` are considered as event handler which does not work with ClickAwayListener

  const classes = useUtilityClasses(ownerState);
  const {
    getRootProps,
    onClickAway
  } = (0, _useSnackbar.useSnackbar)(ownerState);
  const handleClickAway = event => {
    if (!exiting) {
      onClickAway(event);
    }
  };
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: SnackbarRoot,
    externalForwardedProps,
    getSlotProps: getRootProps,
    ownerState
  });
  const [SlotStartDecorator, startDecoratorProps] = (0, _useSlot.default)('startDecorator', {
    className: classes.startDecorator,
    elementType: SnackbarStartDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotEndDecorator, endDecoratorProps] = (0, _useSlot.default)('endDecorator', {
    className: classes.endDecorator,
    elementType: SnackbarEndDecorator,
    externalForwardedProps,
    ownerState
  });
  const SlotClickAway = slots.clickAway || _ClickAwayListener.ClickAwayListener;

  // So we only render active snackbars.
  if (!open && exited) {
    return null;
  }
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotClickAway, (0, _extends2.default)({
    onClickAway: handleClickAway
  }, typeof (slotProps == null ? void 0 : slotProps.clickAway) === 'function' ? slotProps.clickAway(ownerState) : slotProps == null ? void 0 : slotProps.clickAway, {
    children: /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
        children: startDecorator
      })), children, endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
        children: endDecorator
      }))]
    }))
  }));
});
process.env.NODE_ENV !== "production" ? Snackbar.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The anchor of the `Snackbar`.
   * On smaller screens, the component grows to occupy all the available width,
   * the horizontal alignment is ignored.
   * @default { vertical: 'bottom', horizontal: 'right' }
   */
  anchorOrigin: _propTypes.default.shape({
    horizontal: _propTypes.default.oneOf(['center', 'left', 'right']).isRequired,
    vertical: _propTypes.default.oneOf(['bottom', 'top']).isRequired
  }),
  /**
   * The duration of the animation in milliseconds. This value is used to control
   * the length of time it takes for an animation to complete one cycle. It is also
   * utilized for delaying the unmount of the component.
   * Provide this value if you have your own animation so that we can precisely
   * time the component's unmount to match your custom animation.
   * @default 300
   */
  animationDuration: _propTypes.default.number,
  /**
   * The number of milliseconds to wait before automatically calling the
   * `onClose` function. `onClose` should then set the state of the `open`
   * prop to hide the Snackbar. This behavior is disabled by default with
   * the `null` value.
   * @default null
   */
  autoHideDuration: _propTypes.default.number,
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
  color: _propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * If `true`, the `autoHideDuration` timer will expire even if the window is not focused.
   * @default false
   */
  disableWindowBlurListener: _propTypes.default.bool,
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
   * When displaying multiple consecutive snackbars using a single parent-rendered
   * `<Snackbar/>`, add the `key` prop to ensure independent treatment of each message.
   * For instance, use `<Snackbar key={message} />`. Otherwise, messages might update
   * in place, and features like `autoHideDuration` could be affected.
   */
  key: () => null,
  /**
   * @ignore
   */
  onBlur: _propTypes.default.func,
  /**
   * Callback fired when the component requests to be closed.
   * Typically `onClose` is used to set state in the parent component,
   * which is used to control the `Snackbar` `open` prop.
   * The `reason` parameter can optionally be used to control the response to `onClose`,
   * for example ignoring `clickaway`.
   *
   * @param {React.SyntheticEvent<any> | Event} event The event source of the callback.
   * @param {string} reason Can be: `"timeout"` (`autoHideDuration` expired), `"clickaway"`, or `"escapeKeyDown"`.
   */
  onClose: _propTypes.default.func,
  /**
   * @ignore
   */
  onFocus: _propTypes.default.func,
  /**
   * @ignore
   */
  onMouseEnter: _propTypes.default.func,
  /**
   * @ignore
   */
  onMouseLeave: _propTypes.default.func,
  /**
   * A callback fired when the component is about to be unmounted.
   */
  onUnmount: _propTypes.default.func,
  /**
   * If `true`, the component is shown.
   */
  open: _propTypes.default.bool.isRequired,
  /**
   * The number of milliseconds to wait before dismissing after user interaction.
   * If `autoHideDuration` prop isn't specified, it does nothing.
   * If `autoHideDuration` prop is specified but `resumeHideDuration` isn't,
   * we default to `autoHideDuration / 2` ms.
   */
  resumeHideDuration: _propTypes.default.number,
  /**
   * The size of the component.
   * @default 'md'
   */
  size: _propTypes.default.oneOf(['sm', 'md', 'lg']),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    clickAway: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.shape({
      children: _propTypes.default.element.isRequired,
      disableReactTree: _propTypes.default.bool,
      mouseEvent: _propTypes.default.oneOf(['onClick', 'onMouseDown', 'onMouseUp', 'onPointerDown', 'onPointerUp', false]),
      onClickAway: _propTypes.default.func.isRequired,
      touchEvent: _propTypes.default.oneOf(['onTouchEnd', 'onTouchStart', false])
    })]),
    endDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    startDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    clickAway: _propTypes.default.elementType,
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
   * @default 'outlined'
   */
  variant: _propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid'])
} : void 0;
var _default = exports.default = Snackbar;