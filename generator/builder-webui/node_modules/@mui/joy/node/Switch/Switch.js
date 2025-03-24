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
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _useSwitch = require("@mui/base/useSwitch");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _switchClasses = _interopRequireWildcard(require("./switchClasses"));
var _FormControlContext = _interopRequireDefault(require("../FormControl/FormControlContext"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["checked", "defaultChecked", "disabled", "onBlur", "onChange", "onFocus", "onFocusVisible", "readOnly", "required", "id", "color", "variant", "size", "startDecorator", "endDecorator", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    checked,
    disabled,
    focusVisible,
    readOnly,
    color,
    variant,
    size
  } = ownerState;
  const slots = {
    root: ['root', checked && 'checked', disabled && 'disabled', focusVisible && 'focusVisible', readOnly && 'readOnly', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    thumb: ['thumb', checked && 'checked'],
    track: ['track', checked && 'checked'],
    action: ['action', focusVisible && 'focusVisible'],
    input: ['input'],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _switchClasses.getSwitchUtilityClass, {});
};
const switchColorVariables = ({
  theme,
  ownerState
}) => (data = {}) => {
  var _theme$variants, _styles$backgroundCol, _styles$backgroundCol2;
  const styles = ((_theme$variants = theme.variants[`${ownerState.variant}${data.state || ''}`]) == null ? void 0 : _theme$variants[ownerState.color]) || {};
  return {
    '--Switch-trackBackground': (_styles$backgroundCol = styles.backgroundColor) != null ? _styles$backgroundCol : theme.vars.palette.background.surface,
    '--Switch-trackColor': styles.color,
    '--Switch-trackBorderColor': ownerState.variant === 'outlined' ? styles.borderColor : 'currentColor',
    '--Switch-thumbBackground': styles.color,
    '--Switch-thumbColor': (_styles$backgroundCol2 = styles.backgroundColor) != null ? _styles$backgroundCol2 : theme.vars.palette.background.surface
  };
};
const SwitchRoot = (0, _styles.styled)('div', {
  name: 'JoySwitch',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants2;
  const getColorVariables = switchColorVariables({
    theme,
    ownerState
  });
  return (0, _extends2.default)({
    '--Icon-color': 'currentColor',
    '--variant-borderWidth': (_theme$variants2 = theme.variants[ownerState.variant]) == null || (_theme$variants2 = _theme$variants2[ownerState.color]) == null ? void 0 : _theme$variants2['--variant-borderWidth'],
    '--Switch-trackRadius': theme.vars.radius.xl,
    '--Switch-thumbShadow': ownerState.variant === 'soft' ? 'none' : '0 0 0 1px var(--Switch-trackBackground)'
  }, ownerState.size === 'sm' && {
    '--Switch-trackWidth': '26px',
    '--Switch-trackHeight': '16px',
    '--Switch-thumbSize': '10px',
    fontSize: theme.vars.fontSize.sm,
    gap: 'var(--Switch-gap, 6px)'
  }, ownerState.size === 'md' && {
    '--Switch-trackWidth': '32px',
    '--Switch-trackHeight': '20px',
    '--Switch-thumbSize': '14px',
    fontSize: theme.vars.fontSize.md,
    gap: 'var(--Switch-gap, 8px)'
  }, ownerState.size === 'lg' && {
    '--Switch-trackWidth': '40px',
    '--Switch-trackHeight': '24px',
    '--Switch-thumbSize': '18px',
    gap: 'var(--Switch-gap, 12px)'
  }, {
    '--unstable_paddingBlock': `max((var(--Switch-trackHeight) - 2 * var(--variant-borderWidth, 0px) - var(--Switch-thumbSize)) / 2, 0px)`,
    '--Switch-thumbRadius': `max(var(--Switch-trackRadius) - var(--unstable_paddingBlock), min(var(--unstable_paddingBlock) / 2, var(--Switch-trackRadius) / 2))`,
    '--Switch-thumbWidth': 'var(--Switch-thumbSize)',
    '--Switch-thumbOffset': `max((var(--Switch-trackHeight) - var(--Switch-thumbSize)) / 2, 0px)`
  }, getColorVariables(), {
    '&:hover': {
      '@media (hover: hover)': (0, _extends2.default)({}, getColorVariables({
        state: 'Hover'
      }))
    },
    [`&.${_switchClasses.default.checked}`]: (0, _extends2.default)({}, getColorVariables(), {
      '&:hover': {
        '@media (hover: hover)': (0, _extends2.default)({}, getColorVariables({
          state: 'Hover'
        }))
      }
    }),
    [`&.${_switchClasses.default.disabled}`]: (0, _extends2.default)({
      pointerEvents: 'none',
      color: theme.vars.palette.text.tertiary
    }, getColorVariables({
      state: 'Disabled'
    })),
    display: 'inline-flex',
    alignItems: 'center',
    alignSelf: 'center',
    fontFamily: theme.vars.fontFamily.body,
    position: 'relative',
    padding: 'calc((var(--Switch-thumbSize) / 2) - (var(--Switch-trackHeight) / 2)) calc(-1 * var(--Switch-thumbOffset))',
    backgroundColor: 'initial',
    // clear background in case `outlined` variant contain background.
    border: 'none',
    margin: 'var(--unstable_Switch-margin)'
  });
});
const SwitchAction = (0, _styles.styled)('div', {
  name: 'JoySwitch',
  slot: 'Action',
  overridesResolver: (props, styles) => styles.action
})(({
  theme
}) => ({
  borderRadius: 'var(--Switch-trackRadius)',
  position: 'absolute',
  top: 0,
  left: 0,
  bottom: 0,
  right: 0,
  [theme.focus.selector]: theme.focus.default
}));
const SwitchInput = (0, _styles.styled)('input', {
  name: 'JoySwitch',
  slot: 'Input',
  overridesResolver: (props, styles) => styles.input
})({
  margin: 0,
  height: '100%',
  width: '100%',
  opacity: 0,
  position: 'absolute',
  cursor: 'pointer'
});
const SwitchTrack = (0, _styles.styled)('span', {
  name: 'JoySwitch',
  slot: 'Track',
  overridesResolver: (props, styles) => styles.track
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({
  position: 'relative',
  color: 'var(--Switch-trackColor)',
  height: 'var(--Switch-trackHeight)',
  width: 'var(--Switch-trackWidth)',
  display: 'flex',
  flexShrink: 0,
  justifyContent: 'space-between',
  alignItems: 'center',
  boxSizing: 'border-box',
  border: 'var(--variant-borderWidth, 0px) solid',
  borderColor: 'var(--Switch-trackBorderColor)',
  backgroundColor: 'var(--Switch-trackBackground)',
  borderRadius: 'var(--Switch-trackRadius)',
  fontFamily: theme.vars.fontFamily.body
}, ownerState.size === 'sm' && {
  fontSize: theme.vars.fontSize.xs
}, ownerState.size === 'md' && {
  fontSize: theme.vars.fontSize.sm
}, ownerState.size === 'lg' && {
  fontSize: theme.vars.fontSize.md
}));
const SwitchThumb = (0, _styles.styled)('span', {
  name: 'JoySwitch',
  slot: 'Thumb',
  overridesResolver: (props, styles) => styles.thumb
})({
  '--Icon-fontSize': 'calc(var(--Switch-thumbSize) * 0.75)',
  display: 'inline-flex',
  justifyContent: 'center',
  alignItems: 'center',
  position: 'absolute',
  top: '50%',
  left: 'calc(50% - var(--Switch-trackWidth) / 2 + var(--Switch-thumbWidth) / 2 + var(--Switch-thumbOffset))',
  transform: 'translate(-50%, -50%)',
  width: 'var(--Switch-thumbWidth)',
  height: 'var(--Switch-thumbSize)',
  borderRadius: 'var(--Switch-thumbRadius)',
  boxShadow: 'var(--Switch-thumbShadow)',
  color: 'var(--Switch-thumbColor)',
  backgroundColor: 'var(--Switch-thumbBackground)',
  [`&.${_switchClasses.default.checked}`]: {
    left: 'calc(50% + var(--Switch-trackWidth) / 2 - var(--Switch-thumbWidth) / 2 - var(--Switch-thumbOffset))'
  }
});
const SwitchStartDecorator = (0, _styles.styled)('span', {
  name: 'JoySwitch',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})({
  display: 'inline-flex'
});
const SwitchEndDecorator = (0, _styles.styled)('span', {
  name: 'JoySwitch',
  slot: 'EndDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})({
  display: 'inline-flex'
});
/**
 *
 * Demos:
 *
 * - [Switch](https://mui.com/joy-ui/react-switch/)
 *
 * API:
 *
 * - [Switch API](https://mui.com/joy-ui/api/switch/)
 */
const Switch = /*#__PURE__*/React.forwardRef(function Switch(inProps, ref) {
  var _ref, _inProps$size, _inProps$color, _formControl$color, _ref2, _inProps$disabled;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoySwitch'
  });
  const {
      disabled: disabledExternalProp,
      id,
      color: colorProp,
      variant = 'solid',
      size: sizeProp = 'md',
      startDecorator,
      endDecorator,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const formControl = React.useContext(_FormControlContext.default);
  if (process.env.NODE_ENV !== 'production') {
    const registerEffect = formControl == null ? void 0 : formControl.registerEffect;
    // eslint-disable-next-line react-hooks/rules-of-hooks
    React.useEffect(() => {
      if (registerEffect) {
        return registerEffect();
      }
      return undefined;
    }, [registerEffect]);
  }
  const size = (_ref = (_inProps$size = inProps.size) != null ? _inProps$size : formControl == null ? void 0 : formControl.size) != null ? _ref : sizeProp;
  const color = (_inProps$color = inProps.color) != null ? _inProps$color : formControl != null && formControl.error ? 'danger' : (_formControl$color = formControl == null ? void 0 : formControl.color) != null ? _formControl$color : colorProp;
  const useSwitchProps = (0, _extends2.default)({
    disabled: (_ref2 = (_inProps$disabled = inProps.disabled) != null ? _inProps$disabled : formControl == null ? void 0 : formControl.disabled) != null ? _ref2 : disabledExternalProp
  }, props);
  const {
    getInputProps,
    checked,
    disabled,
    focusVisible,
    readOnly
  } = (0, _useSwitch.useSwitch)(useSwitchProps);
  const ownerState = (0, _extends2.default)({}, props, {
    id,
    checked,
    disabled,
    focusVisible,
    readOnly,
    color: checked ? color || 'primary' : color || 'neutral',
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
    className: classes.root,
    elementType: SwitchRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotStartDecorator, startDecoratorProps] = (0, _useSlot.default)('startDecorator', {
    additionalProps: {
      'aria-hidden': true // hide the decorator from assistive technology
    },
    className: classes.startDecorator,
    elementType: SwitchStartDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotEndDecorator, endDecoratorProps] = (0, _useSlot.default)('endDecorator', {
    additionalProps: {
      'aria-hidden': true // hide the decorator from assistive technology
    },
    className: classes.endDecorator,
    elementType: SwitchEndDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotTrack, trackProps] = (0, _useSlot.default)('track', {
    className: classes.track,
    elementType: SwitchTrack,
    externalForwardedProps,
    ownerState
  });
  const [SlotThumb, thumbProps] = (0, _useSlot.default)('thumb', {
    className: classes.thumb,
    elementType: SwitchThumb,
    externalForwardedProps,
    ownerState
  });
  const [SlotAction, actionProps] = (0, _useSlot.default)('action', {
    className: classes.action,
    elementType: SwitchAction,
    externalForwardedProps,
    ownerState
  });
  const [SlotInput, inputProps] = (0, _useSlot.default)('input', {
    additionalProps: {
      id: id != null ? id : formControl == null ? void 0 : formControl.htmlFor,
      'aria-describedby': formControl == null ? void 0 : formControl['aria-describedby']
    },
    className: classes.input,
    elementType: SwitchInput,
    externalForwardedProps,
    getSlotProps: getInputProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
      children: typeof startDecorator === 'function' ? startDecorator(ownerState) : startDecorator
    })), /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotTrack, (0, _extends2.default)({}, trackProps, {
      children: [trackProps == null ? void 0 : trackProps.children, /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotThumb, (0, _extends2.default)({}, thumbProps))]
    })), /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotAction, (0, _extends2.default)({}, actionProps, {
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotInput, (0, _extends2.default)({}, inputProps))
    })), endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
      children: typeof endDecorator === 'function' ? endDecorator(ownerState) : endDecorator
    }))]
  }));
});
process.env.NODE_ENV !== "production" ? Switch.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * If `true`, the component is checked.
   */
  checked: _propTypes.default.bool,
  /**
   * @ignore
   */
  children: _propTypes.default.node,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   * @default 'neutral'
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * The default checked state. Use when the component is not controlled.
   */
  defaultChecked: _propTypes.default.bool,
  /**
   * If `true`, the component is disabled.
   */
  disabled: _propTypes.default.bool,
  /**
   * The element that appears at the end of the switch.
   */
  endDecorator: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.node, _propTypes.default.func]),
  /**
   * @ignore
   */
  id: _propTypes.default.string,
  /**
   * @ignore
   */
  onBlur: _propTypes.default.func,
  /**
   * Callback fired when the state is changed.
   *
   * @param {React.ChangeEvent<HTMLInputElement>} event The event source of the callback.
   * You can pull out the new value by accessing `event.target.value` (string).
   * You can pull out the new checked state by accessing `event.target.checked` (boolean).
   */
  onChange: _propTypes.default.func,
  /**
   * @ignore
   */
  onFocus: _propTypes.default.func,
  /**
   * @ignore
   */
  onFocusVisible: _propTypes.default.func,
  /**
   * If `true`, the component is read only.
   */
  readOnly: _propTypes.default.bool,
  /**
   * If `true`, the `input` element is required.
   */
  required: _propTypes.default.bool,
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
    action: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    endDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    input: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    startDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    thumb: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    track: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    action: _propTypes.default.elementType,
    endDecorator: _propTypes.default.elementType,
    input: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    startDecorator: _propTypes.default.elementType,
    thumb: _propTypes.default.elementType,
    track: _propTypes.default.elementType
  }),
  /**
   * The element that appears at the end of the switch.
   */
  startDecorator: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.node, _propTypes.default.func]),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'solid'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Switch;