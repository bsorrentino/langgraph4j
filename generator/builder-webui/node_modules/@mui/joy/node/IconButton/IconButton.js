"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledIconButton = exports.IconButtonRoot = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _useButton = require("@mui/base/useButton");
var _composeClasses = require("@mui/base/composeClasses");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _iconButtonClasses = require("./iconButtonClasses");
var _ButtonGroupContext = _interopRequireDefault(require("../ButtonGroup/ButtonGroupContext"));
var _ToggleButtonGroupContext = _interopRequireDefault(require("../ToggleButtonGroup/ToggleButtonGroupContext"));
var _CircularProgress = _interopRequireDefault(require("../CircularProgress"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "action", "component", "color", "disabled", "variant", "loading", "loadingIndicator", "size", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    color,
    disabled,
    focusVisible,
    focusVisibleClassName,
    size,
    variant,
    loading
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', focusVisible && 'focusVisible', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`, loading && 'loading'],
    loadingIndicator: ['loadingIndicator']
  };
  const composedClasses = (0, _composeClasses.unstable_composeClasses)(slots, _iconButtonClasses.getIconButtonUtilityClass, {});
  if (focusVisible && focusVisibleClassName) {
    composedClasses.root += ` ${focusVisibleClassName}`;
  }
  return composedClasses;
};
const StyledIconButton = exports.StyledIconButton = (0, _styles.styled)('button')(({
  theme,
  ownerState
}) => {
  var _theme$variants, _theme$variants2, _theme$variants3, _theme$variants4;
  return [(0, _extends2.default)({
    '--Icon-margin': 'initial',
    // reset the icon's margin.
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon
  }, ownerState.instanceSize && {
    '--IconButton-size': {
      sm: '2rem',
      md: '2.25rem',
      lg: '2.75rem'
    }[ownerState.instanceSize]
  }, ownerState.size === 'sm' && {
    '--Icon-fontSize': 'calc(var(--IconButton-size, 2rem) / 1.6)',
    // 1.25rem by default
    '--CircularProgress-size': '20px',
    '--CircularProgress-thickness': '2px',
    minWidth: 'var(--IconButton-size, 2rem)',
    // use min-width instead of height to make the button resilient to its content
    minHeight: 'var(--IconButton-size, 2rem)',
    // use min-height instead of height to make the button resilient to its content
    fontSize: theme.vars.fontSize.sm,
    paddingInline: '2px' // add a gap, in case the content is long, for example multiple icons
  }, ownerState.size === 'md' && {
    '--Icon-fontSize': 'calc(var(--IconButton-size, 2.25rem) / 1.5)',
    // 1.5rem by default
    '--CircularProgress-size': '20px',
    '--CircularProgress-thickness': '2px',
    minWidth: 'var(--IconButton-size, 2.25rem)',
    minHeight: 'var(--IconButton-size, 2.25rem)',
    fontSize: theme.vars.fontSize.md,
    paddingInline: '0.25rem'
  }, ownerState.size === 'lg' && {
    '--Icon-fontSize': 'calc(var(--IconButton-size, 2.75rem) / 1.571)',
    // 1.75rem by default
    '--CircularProgress-size': '28px',
    '--CircularProgress-thickness': '4px',
    minWidth: 'var(--IconButton-size, 2.75rem)',
    minHeight: 'var(--IconButton-size, 2.75rem)',
    fontSize: theme.vars.fontSize.lg,
    paddingInline: '0.375rem'
  }, {
    WebkitTapHighlightColor: 'transparent',
    paddingBlock: 0,
    fontFamily: theme.vars.fontFamily.body,
    fontWeight: theme.vars.fontWeight.md,
    margin: `var(--IconButton-margin)`,
    // to be controlled by other components, for example Input
    borderRadius: `var(--IconButton-radius, ${theme.vars.radius.sm})`,
    // to be controlled by other components, for example Input
    border: 'none',
    boxSizing: 'border-box',
    backgroundColor: 'transparent',
    cursor: 'pointer',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
    [theme.focus.selector]: (0, _extends2.default)({
      '--Icon-color': 'currentColor'
    }, theme.focus.default)
  }), (0, _extends2.default)({}, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color], {
    '&:hover': {
      '@media (hover: hover)': (0, _extends2.default)({
        '--Icon-color': 'currentColor'
      }, (_theme$variants2 = theme.variants[`${ownerState.variant}Hover`]) == null ? void 0 : _theme$variants2[ownerState.color])
    },
    '&:active, &[aria-pressed="true"]': (0, _extends2.default)({
      '--Icon-color': 'currentColor'
    }, (_theme$variants3 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants3[ownerState.color]),
    '&:disabled': (_theme$variants4 = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants4[ownerState.color]
  })];
});
const IconButtonRoot = exports.IconButtonRoot = (0, _styles.styled)(StyledIconButton, {
  name: 'JoyIconButton',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({});
const ButtonLoading = (0, _styles.styled)('span', {
  name: 'JoyIconButton',
  slot: 'LoadingIndicator',
  overridesResolver: (props, styles) => styles.loadingIndicator
})(({
  theme,
  ownerState
}) => {
  var _theme$variants5, _theme$variants6;
  return (0, _extends2.default)({
    display: 'inherit',
    position: 'absolute',
    left: '50%',
    transform: 'translateX(-50%)',
    color: (_theme$variants5 = theme.variants[ownerState.variant]) == null || (_theme$variants5 = _theme$variants5[ownerState.color]) == null ? void 0 : _theme$variants5.color
  }, ownerState.disabled && {
    color: (_theme$variants6 = theme.variants[`${ownerState.variant}Disabled`]) == null || (_theme$variants6 = _theme$variants6[ownerState.color]) == null ? void 0 : _theme$variants6.color
  });
});

/**
 *
 * Demos:
 *
 * - [Button](https://mui.com/joy-ui/react-button/)
 * - [Button Group](https://mui.com/joy-ui/react-button-group/)
 * - [Toggle Button Group](https://mui.com/joy-ui/react-toggle-button-group/)
 *
 * API:
 *
 * - [IconButton API](https://mui.com/joy-ui/api/icon-button/)
 */
const IconButton = /*#__PURE__*/React.forwardRef(function IconButton(inProps, ref) {
  var _ref;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyIconButton'
  });
  const {
      children,
      action,
      component = 'button',
      color: colorProp = 'neutral',
      disabled: disabledProp,
      variant: variantProp = 'plain',
      loading = false,
      loadingIndicator: loadingIndicatorProp,
      size: sizeProp = 'md',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const buttonGroup = React.useContext(_ButtonGroupContext.default);
  const toggleButtonGroup = React.useContext(_ToggleButtonGroupContext.default);
  const variant = inProps.variant || buttonGroup.variant || variantProp;
  const size = inProps.size || buttonGroup.size || sizeProp;
  const color = inProps.color || buttonGroup.color || colorProp;
  const disabled = (_ref = inProps.loading || inProps.disabled) != null ? _ref : buttonGroup.disabled || loading || disabledProp;
  const buttonRef = React.useRef(null);
  const handleRef = (0, _utils.unstable_useForkRef)(buttonRef, ref);
  const {
    focusVisible,
    setFocusVisible,
    getRootProps
  } = (0, _useButton.useButton)((0, _extends2.default)({}, props, {
    disabled,
    rootRef: handleRef
  }));
  const loadingIndicator = loadingIndicatorProp != null ? loadingIndicatorProp : /*#__PURE__*/(0, _jsxRuntime.jsx)(_CircularProgress.default, {
    color: color,
    thickness: {
      sm: 2,
      md: 3,
      lg: 4
    }[size] || 3
  });
  React.useImperativeHandle(action, () => ({
    focusVisible: () => {
      var _buttonRef$current;
      setFocusVisible(true);
      (_buttonRef$current = buttonRef.current) == null || _buttonRef$current.focus();
    }
  }), [setFocusVisible]);
  const ownerState = (0, _extends2.default)({}, props, {
    component,
    color,
    disabled,
    variant,
    loading,
    size,
    focusVisible,
    instanceSize: inProps.size
  });
  const classes = useUtilityClasses(ownerState);
  const handleClick = event => {
    var _onClick;
    let onClick = props.onClick;
    if (typeof slotProps.root === 'function') {
      onClick = slotProps.root(ownerState).onClick;
    } else if (slotProps.root) {
      onClick = slotProps.root.onClick;
    }
    (_onClick = onClick) == null || _onClick(event);
    if (toggleButtonGroup) {
      var _toggleButtonGroup$on;
      (_toggleButtonGroup$on = toggleButtonGroup.onClick) == null || _toggleButtonGroup$on.call(toggleButtonGroup, event, props.value);
    }
  };
  let ariaPressed = props['aria-pressed'];
  if (typeof slotProps.root === 'function') {
    ariaPressed = slotProps.root(ownerState)['aria-pressed'];
  } else if (slotProps.root) {
    ariaPressed = slotProps.root['aria-pressed'];
  }
  if (toggleButtonGroup != null && toggleButtonGroup.value) {
    if (Array.isArray(toggleButtonGroup.value)) {
      ariaPressed = toggleButtonGroup.value.indexOf(props.value) !== -1;
    } else {
      ariaPressed = toggleButtonGroup.value === props.value;
    }
  }
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: IconButtonRoot,
    getSlotProps: getRootProps,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      onClick: handleClick,
      'aria-pressed': ariaPressed
    }
  });
  const [SlotLoadingIndicator, loadingIndicatorProps] = (0, _useSlot.default)('loadingIndicator', {
    className: classes.loadingIndicator,
    elementType: ButtonLoading,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: loading ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotLoadingIndicator, (0, _extends2.default)({}, loadingIndicatorProps, {
      children: loadingIndicator
    })) : children
  }));
});
process.env.NODE_ENV !== "production" ? IconButton.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * A ref for imperative actions. It currently only supports `focusVisible()` action.
   */
  action: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.shape({
    current: _propTypes.default.shape({
      focusVisible: _propTypes.default.func.isRequired
    })
  })]),
  /**
   * @ignore
   */
  children: _propTypes.default.node,
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
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * This prop can help identify which element has keyboard focus.
   * The class name will be applied when the element gains the focus through keyboard interaction.
   * It's a polyfill for the [CSS :focus-visible selector](https://drafts.csswg.org/selectors-4/#the-focus-visible-pseudo).
   * The rationale for using this feature [is explained here](https://github.com/WICG/focus-visible/blob/HEAD/explainer.md).
   * A [polyfill can be used](https://github.com/WICG/focus-visible) to apply a `focus-visible` class to other components
   * if needed.
   */
  focusVisibleClassName: _propTypes.default.string,
  /**
   * If `true`, the loading indicator is shown and the icon button becomes disabled.
   * @default false
   */
  loading: _propTypes.default.bool,
  /**
   * The node should contain an element with `role="progressbar"` with an accessible name.
   * By default we render a `CircularProgress` that is labelled by the button itself.
   * @default <CircularProgress />
   */
  loadingIndicator: _propTypes.default.node,
  /**
   * @ignore
   */
  onClick: _propTypes.default.func,
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
    loadingIndicator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    loadingIndicator: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * @default 0
   */
  tabIndex: _propTypes.default.number,
  /**
   * @ignore
   */
  value: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.string), _propTypes.default.number, _propTypes.default.string]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;

// @ts-ignore internal logic for ToggleButtonGroup
IconButton.muiName = 'IconButton';
var _default = exports.default = IconButton;