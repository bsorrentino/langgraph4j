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
var _base = require("@mui/base");
var _useSwitch = require("@mui/base/useSwitch");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _radioClasses = _interopRequireWildcard(require("./radioClasses"));
var _RadioGroupContext = _interopRequireDefault(require("../RadioGroup/RadioGroupContext"));
var _Typography = require("../Typography/Typography");
var _FormControlContext = _interopRequireDefault(require("../FormControl/FormControlContext"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["checked", "checkedIcon", "defaultChecked", "disabled", "disableIcon", "overlay", "label", "id", "name", "onBlur", "onChange", "onFocus", "onFocusVisible", "readOnly", "required", "color", "variant", "size", "uncheckedIcon", "value", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    checked,
    disabled,
    disableIcon,
    focusVisible,
    color,
    variant,
    size
  } = ownerState;
  const slots = {
    root: ['root', checked && 'checked', disabled && 'disabled', focusVisible && 'focusVisible', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    radio: ['radio', checked && 'checked', disabled && 'disabled'],
    // disabled class is necessary for displaying global variant
    icon: ['icon'],
    action: ['action', checked && 'checked', disableIcon && disabled && 'disabled',
    // add disabled class to action element for displaying global variant
    focusVisible && 'focusVisible'],
    input: ['input'],
    label: ['label']
  };
  return (0, _base.unstable_composeClasses)(slots, _radioClasses.getRadioUtilityClass, {});
};
function areEqualValues(a, b) {
  if (typeof b === 'object' && b !== null) {
    return a === b;
  }

  // The value could be a number, the DOM will stringify it anyway.
  return String(a) === String(b);
}
const RadioRoot = (0, _styles.styled)('span', {
  name: 'JoyRadio',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => {
  var _theme$variants$plain, _theme$variants, _theme$variants2;
  return [(0, _extends2.default)({
    '--Icon-fontSize': 'var(--Radio-size)',
    '--Icon-color': 'currentColor'
  }, ownerState.size === 'sm' && {
    '--Radio-size': '1rem',
    // --FormHelperText-margin is equal to --Radio-size + --Radio-gap but we can't use calc() with CSS variables because the FormHelperText is a sibling element
    '& ~ *': {
      '--FormHelperText-margin': '0 0 0 1.5rem'
    },
    fontSize: theme.vars.fontSize.sm,
    gap: 'var(--Radio-gap, 0.5rem)'
  }, ownerState.size === 'md' && {
    '--Radio-size': '1.25rem',
    '& ~ *': {
      '--FormHelperText-margin': '0.25rem 0 0 1.875rem'
    },
    fontSize: theme.vars.fontSize.md,
    gap: 'var(--Radio-gap, 0.625rem)'
  }, ownerState.size === 'lg' && {
    '--Radio-size': '1.5rem',
    '& ~ *': {
      '--FormHelperText-margin': '0.375rem 0 0 2.25rem'
    },
    fontSize: theme.vars.fontSize.lg,
    gap: 'var(--Radio-gap, 0.75rem)'
  }, {
    position: ownerState.overlay ? 'initial' : 'relative',
    display: 'inline-flex',
    boxSizing: 'border-box',
    minWidth: 0,
    fontFamily: theme.vars.fontFamily.body,
    lineHeight: 'var(--Radio-size)',
    // prevent label from having larger height than the checkbox
    color: theme.vars.palette.text.primary,
    [`&.${_radioClasses.default.disabled}`]: {
      color: (_theme$variants$plain = theme.variants.plainDisabled) == null || (_theme$variants$plain = _theme$variants$plain[ownerState.color]) == null ? void 0 : _theme$variants$plain.color
    }
  }, ownerState.disableIcon && {
    color: (_theme$variants = theme.variants[ownerState.variant]) == null || (_theme$variants = _theme$variants[ownerState.color]) == null ? void 0 : _theme$variants.color,
    [`&.${_radioClasses.default.disabled}`]: {
      color: (_theme$variants2 = theme.variants[`${ownerState.variant}Disabled`]) == null || (_theme$variants2 = _theme$variants2[ownerState.color]) == null ? void 0 : _theme$variants2.color
    }
  }, ownerState['data-parent'] === 'RadioGroup' && ownerState['data-first-child'] === undefined && {
    marginInlineStart: ownerState.orientation === 'horizontal' ? 'var(--RadioGroup-gap)' : undefined,
    marginBlockStart: ownerState.orientation === 'horizontal' ? undefined : 'var(--RadioGroup-gap)'
  })];
});
const RadioRadio = (0, _styles.styled)('span', {
  name: 'JoyRadio',
  slot: 'Radio',
  overridesResolver: (props, styles) => styles.radio
})(({
  ownerState,
  theme
}) => {
  var _theme$variants3, _variantStyle$backgro, _theme$variants4, _theme$variants5, _theme$variants6;
  const variantStyle = (_theme$variants3 = theme.variants[`${ownerState.variant}`]) == null ? void 0 : _theme$variants3[ownerState.color];
  return [(0, _extends2.default)({
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon,
    margin: 0,
    boxSizing: 'border-box',
    width: 'var(--Radio-size)',
    height: 'var(--Radio-size)',
    borderRadius: 'var(--Radio-size)',
    display: 'inline-flex',
    justifyContent: 'center',
    alignItems: 'center',
    flexShrink: 0
  }, ownerState.disableIcon && {
    display: 'contents'
  }, {
    [`&.${_radioClasses.default.checked}`]: {
      '--Icon-color': 'currentColor'
    }
  }), ...(!ownerState.disableIcon ? [(0, _extends2.default)({}, variantStyle, {
    backgroundColor: (_variantStyle$backgro = variantStyle == null ? void 0 : variantStyle.backgroundColor) != null ? _variantStyle$backgro : theme.vars.palette.background.surface
  }), {
    '&:hover': {
      '@media (hover: hover)': (_theme$variants4 = theme.variants[`${ownerState.variant}Hover`]) == null ? void 0 : _theme$variants4[ownerState.color]
    }
  }, {
    '&:active': (_theme$variants5 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants5[ownerState.color]
  }, {
    [`&.${_radioClasses.default.disabled}`]: (_theme$variants6 = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants6[ownerState.color]
  }] : [])];
});
const RadioAction = (0, _styles.styled)('span', {
  name: 'JoyRadio',
  slot: 'Action',
  overridesResolver: (props, styles) => styles.action
})(({
  theme,
  ownerState
}) => {
  var _theme$variants7, _theme$variants8, _theme$variants9, _theme$variants10;
  return [{
    position: 'absolute',
    textAlign: 'left',
    // prevent text-align inheritance
    borderRadius: `var(--Radio-actionRadius, ${
    // Automatic radius adjustment when composing with ListItem or Sheet
    ownerState.overlay ? 'var(--unstable_actionRadius, inherit)' : 'inherit'})`,
    top: 'calc(-1 * var(--variant-borderWidth, 0px))',
    // clickable on the border and focus outline does not move when checked/unchecked
    left: 'calc(-1 * var(--variant-borderWidth, 0px))',
    bottom: 'calc(-1 * var(--variant-borderWidth, 0px))',
    right: 'calc(-1 * var(--variant-borderWidth, 0px))',
    zIndex: 1,
    // The action element usually cover the area of nearest positioned parent
    [theme.focus.selector]: theme.focus.default
  }, ...(ownerState.disableIcon ? [(_theme$variants7 = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants7[ownerState.color], {
    '&:hover': {
      '@media (hover: hover)': (_theme$variants8 = theme.variants[`${ownerState.variant}Hover`]) == null ? void 0 : _theme$variants8[ownerState.color]
    }
  }, {
    '&:active': (_theme$variants9 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants9[ownerState.color]
  }, {
    [`&.${_radioClasses.default.disabled}`]: (_theme$variants10 = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants10[ownerState.color]
  }] : [])];
});
const RadioInput = (0, _styles.styled)('input', {
  name: 'JoyRadio',
  slot: 'Input',
  overridesResolver: (props, styles) => styles.input
})(() => ({
  margin: 0,
  opacity: 0,
  position: 'absolute',
  height: '100%',
  width: '100%',
  cursor: 'pointer'
}));
const RadioLabel = (0, _styles.styled)('label', {
  name: 'JoyRadio',
  slot: 'Label',
  overridesResolver: (props, styles) => styles.label
})(({
  ownerState
}) => (0, _extends2.default)({
  flex: 1,
  minWidth: 0
}, ownerState.disableIcon && {
  zIndex: 1,
  // label should stay on top of the action.
  pointerEvents: 'none' // makes hover ineffect.
}));

/**
 * internal component
 */
const RadioIcon = (0, _styles.styled)('span', {
  name: 'JoyRadio',
  slot: 'Icon',
  overridesResolver: (props, styles) => styles.icon
})(({
  ownerState
}) => ({
  width: 'calc(var(--Radio-size) / 2)',
  height: 'calc(var(--Radio-size) / 2)',
  borderRadius: 'inherit',
  color: 'inherit',
  backgroundColor: 'currentColor',
  transform: ownerState.checked ? 'scale(1)' : 'scale(0)'
}));
/**
 *
 * Demos:
 *
 * - [Radio](https://mui.com/joy-ui/react-radio-button/)
 *
 * API:
 *
 * - [Radio API](https://mui.com/joy-ui/api/radio/)
 */
const Radio = /*#__PURE__*/React.forwardRef(function Radio(inProps, ref) {
  var _ref, _ref2, _inProps$color, _ref3, _ref4, _inProps$color2, _inProps$color3;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyRadio'
  });
  const {
      checked: checkedProp,
      checkedIcon,
      defaultChecked,
      disabled: disabledProp,
      disableIcon: disableIconProp = false,
      overlay: overlayProp = false,
      label,
      id: idOverride,
      name: nameProp,
      onBlur,
      onChange,
      onFocus,
      onFocusVisible,
      readOnly,
      required,
      color: colorProp,
      variant = 'outlined',
      size: sizeProp = 'md',
      uncheckedIcon,
      value,
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
  const id = (0, _utils.unstable_useId)(idOverride != null ? idOverride : formControl == null ? void 0 : formControl.htmlFor);
  const radioGroup = React.useContext(_RadioGroupContext.default);
  const activeColor = formControl != null && formControl.error ? 'danger' : (_ref = (_ref2 = (_inProps$color = inProps.color) != null ? _inProps$color : formControl == null ? void 0 : formControl.color) != null ? _ref2 : colorProp) != null ? _ref : 'primary';
  const inactiveColor = formControl != null && formControl.error ? 'danger' : (_ref3 = (_ref4 = (_inProps$color2 = inProps.color) != null ? _inProps$color2 : formControl == null ? void 0 : formControl.color) != null ? _ref4 : colorProp) != null ? _ref3 : 'neutral';
  const size = inProps.size || (formControl == null ? void 0 : formControl.size) || (radioGroup == null ? void 0 : radioGroup.size) || sizeProp;
  const name = inProps.name || (radioGroup == null ? void 0 : radioGroup.name) || nameProp;
  const disableIcon = inProps.disableIcon || (radioGroup == null ? void 0 : radioGroup.disableIcon) || disableIconProp;
  const overlay = inProps.overlay || (radioGroup == null ? void 0 : radioGroup.overlay) || overlayProp;
  const radioChecked = typeof checkedProp === 'undefined' && value != null ? areEqualValues(radioGroup == null ? void 0 : radioGroup.value, value) : checkedProp;
  const useRadioProps = {
    checked: radioChecked,
    defaultChecked,
    disabled: inProps.disabled || (formControl == null ? void 0 : formControl.disabled) || disabledProp,
    onBlur,
    onChange,
    onFocus,
    onFocusVisible
  };
  const {
    getInputProps,
    checked,
    disabled,
    focusVisible
  } = (0, _useSwitch.useSwitch)(useRadioProps);
  const color = (_inProps$color3 = inProps.color) != null ? _inProps$color3 : checked ? activeColor : inactiveColor;
  const ownerState = (0, _extends2.default)({}, props, {
    checked,
    disabled,
    focusVisible,
    color,
    variant,
    size,
    disableIcon,
    overlay,
    orientation: radioGroup == null ? void 0 : radioGroup.orientation
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
    elementType: RadioRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotRadio, radioProps] = (0, _useSlot.default)('radio', {
    className: classes.radio,
    elementType: RadioRadio,
    externalForwardedProps,
    ownerState
  });
  const [SlotIcon, iconProps] = (0, _useSlot.default)('icon', {
    className: classes.icon,
    elementType: RadioIcon,
    externalForwardedProps,
    ownerState
  });
  const [SlotAction, actionProps] = (0, _useSlot.default)('action', {
    className: classes.action,
    elementType: RadioAction,
    externalForwardedProps,
    ownerState
  });
  const [SlotInput, inputProps] = (0, _useSlot.default)('input', {
    additionalProps: {
      type: 'radio',
      role: undefined,
      id,
      name,
      readOnly,
      required: required != null ? required : formControl == null ? void 0 : formControl.required,
      value: String(value),
      'aria-describedby': formControl == null ? void 0 : formControl['aria-describedby']
    },
    className: classes.input,
    elementType: RadioInput,
    externalForwardedProps,
    getSlotProps: () => getInputProps({
      onChange: radioGroup == null ? void 0 : radioGroup.onChange
    }),
    ownerState
  });
  const [SlotLabel, labelProps] = (0, _useSlot.default)('label', {
    additionalProps: {
      htmlFor: id
    },
    className: classes.label,
    elementType: RadioLabel,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRadio, (0, _extends2.default)({}, radioProps, {
      children: [checked && !disableIcon && checkedIcon, !checked && !disableIcon && uncheckedIcon, !checkedIcon && !uncheckedIcon && !disableIcon && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotIcon, (0, _extends2.default)({}, iconProps)), /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotAction, (0, _extends2.default)({}, actionProps, {
        children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotInput, (0, _extends2.default)({}, inputProps))
      }))]
    })), label && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotLabel, (0, _extends2.default)({}, labelProps, {
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_Typography.TypographyNestedContext.Provider, {
        value: true,
        children: label
      })
    }))]
  }));
});
process.env.NODE_ENV !== "production" ? Radio.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * If `true`, the component is checked.
   */
  checked: _propTypes.default.bool,
  /**
   * The icon to display when the component is checked.
   */
  checkedIcon: _propTypes.default.node,
  /**
   * @ignore
   */
  children: _propTypes.default.node,
  /**
   * Class name applied to the root element.
   */
  className: _propTypes.default.string,
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
   * If `true`, the checked icon is removed and the selected variant is applied on the `action` element instead.
   * @default false
   */
  disableIcon: _propTypes.default.bool,
  /**
   * @ignore
   */
  id: _propTypes.default.string,
  /**
   * The label element at the end the radio.
   */
  label: _propTypes.default.node,
  /**
   * The `name` attribute of the input.
   */
  name: _propTypes.default.string,
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
   * If `true`, the root element's position is set to initial which allows the action area to fill the nearest positioned parent.
   * This prop is useful for composing Radio with ListItem component.
   * @default false
   */
  overlay: _propTypes.default.bool,
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
    icon: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    input: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    label: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    radio: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    action: _propTypes.default.elementType,
    icon: _propTypes.default.elementType,
    input: _propTypes.default.elementType,
    label: _propTypes.default.elementType,
    radio: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The icon to display when the component is not checked.
   */
  uncheckedIcon: _propTypes.default.node,
  /**
   * The value of the component. The DOM API casts this to a string.
   */
  value: _propTypes.default.any,
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'outlined'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Radio;