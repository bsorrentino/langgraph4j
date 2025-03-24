"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledInputStartDecorator = exports.StyledInputRoot = exports.StyledInputHtml = exports.StyledInputEndDecorator = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _inputClasses = _interopRequireWildcard(require("./inputClasses"));
var _useForwardedInput2 = _interopRequireDefault(require("./useForwardedInput"));
var _colorInversionUtils = require("../colorInversion/colorInversionUtils");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["propsToForward", "rootStateClasses", "inputStateClasses", "getRootProps", "getInputProps", "formControl", "focused", "error", "disabled", "fullWidth", "size", "color", "variant", "startDecorator", "endDecorator", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    disabled,
    fullWidth,
    variant,
    color,
    size
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', fullWidth && 'fullWidth', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    input: ['input'],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _inputClasses.getInputUtilityClass, {});
};
const StyledInputRoot = exports.StyledInputRoot = (0, _styles.styled)('div')(({
  theme,
  ownerState
}) => {
  var _theme$variants, _theme$vars$palette, _theme$vars$palette2, _variantStyle$backgro, _theme$variants2, _theme$variants3;
  const variantStyle = (_theme$variants = theme.variants[`${ownerState.variant}`]) == null ? void 0 : _theme$variants[ownerState.color];
  return [(0, _extends2.default)({
    '--Input-radius': theme.vars.radius.sm,
    '--Input-gap': '0.5rem',
    '--Input-placeholderColor': 'inherit',
    '--Input-placeholderOpacity': 0.64,
    '--Input-decoratorColor': theme.vars.palette.text.icon,
    '--Input-focused': '0',
    '--Input-focusedThickness': theme.vars.focus.thickness,
    '--Input-focusedHighlight': (_theme$vars$palette = theme.vars.palette[ownerState.color === 'neutral' ? 'primary' : ownerState.color]) == null ? void 0 : _theme$vars$palette[500],
    [`&:not([${_colorInversionUtils.INVERTED_COLORS_ATTR}])`]: (0, _extends2.default)({}, ownerState.instanceColor && {
      '--_Input-focusedHighlight': (_theme$vars$palette2 = theme.vars.palette[ownerState.instanceColor === 'neutral' ? 'primary' : ownerState.instanceColor]) == null ? void 0 : _theme$vars$palette2[500]
    }, {
      '--Input-focusedHighlight': `var(--_Input-focusedHighlight, ${theme.vars.palette.focusVisible})`
    })
  }, ownerState.size === 'sm' && {
    '--Input-minHeight': '2rem',
    '--Input-paddingInline': '0.5rem',
    '--Input-decoratorChildHeight': 'min(1.5rem, var(--Input-minHeight))',
    '--Icon-fontSize': theme.vars.fontSize.xl
  }, ownerState.size === 'md' && {
    '--Input-minHeight': '2.25rem',
    '--Input-paddingInline': '0.75rem',
    '--Input-decoratorChildHeight': 'min(1.75rem, var(--Input-minHeight))',
    '--Icon-fontSize': theme.vars.fontSize.xl2
  }, ownerState.size === 'lg' && {
    '--Input-minHeight': '2.75rem',
    '--Input-paddingInline': '1rem',
    '--Input-gap': '0.75rem',
    '--Input-decoratorChildHeight': 'min(2.25rem, var(--Input-minHeight))',
    '--Icon-fontSize': theme.vars.fontSize.xl2
  }, {
    // variables for controlling child components
    '--Input-decoratorChildOffset': 'min(calc(var(--Input-paddingInline) - (var(--Input-minHeight) - 2 * var(--variant-borderWidth, 0px) - var(--Input-decoratorChildHeight)) / 2), var(--Input-paddingInline))',
    '--_Input-paddingBlock': 'max((var(--Input-minHeight) - 2 * var(--variant-borderWidth, 0px) - var(--Input-decoratorChildHeight)) / 2, 0px)',
    '--Input-decoratorChildRadius': 'max(var(--Input-radius) - var(--variant-borderWidth, 0px) - var(--_Input-paddingBlock), min(var(--_Input-paddingBlock) + var(--variant-borderWidth, 0px), var(--Input-radius) / 2))',
    '--Button-minHeight': 'var(--Input-decoratorChildHeight)',
    '--Button-paddingBlock': '0px',
    // to ensure that the height of the button is equal to --Button-minHeight
    '--IconButton-size': 'var(--Input-decoratorChildHeight)',
    '--Button-radius': 'var(--Input-decoratorChildRadius)',
    '--IconButton-radius': 'var(--Input-decoratorChildRadius)',
    boxSizing: 'border-box'
  }, ownerState.variant !== 'plain' && {
    boxShadow: theme.shadow.xs
  }, {
    minWidth: 0,
    minHeight: 'var(--Input-minHeight)'
  }, ownerState.fullWidth && {
    width: '100%'
  }, {
    cursor: 'text',
    position: 'relative',
    display: 'flex',
    paddingInline: `var(--Input-paddingInline)`,
    borderRadius: 'var(--Input-radius)'
  }, theme.typography[`body-${ownerState.size}`], variantStyle, {
    backgroundColor: (_variantStyle$backgro = variantStyle == null ? void 0 : variantStyle.backgroundColor) != null ? _variantStyle$backgro : theme.vars.palette.background.surface,
    '&::before': {
      boxSizing: 'border-box',
      content: '""',
      display: 'block',
      position: 'absolute',
      pointerEvents: 'none',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      zIndex: 1,
      borderRadius: 'inherit',
      margin: 'calc(var(--variant-borderWidth, 0px) * -1)',
      // for outlined variant
      boxShadow: `var(--Input-focusedInset, inset) 0 0 0 calc(var(--Input-focused) * var(--Input-focusedThickness)) var(--Input-focusedHighlight)`
    }
  }), {
    '&:hover': (0, _extends2.default)({}, (_theme$variants2 = theme.variants[`${ownerState.variant}Hover`]) == null ? void 0 : _theme$variants2[ownerState.color], {
      backgroundColor: null // it is not common to change background on hover for Input
    }),
    [`&.${_inputClasses.default.disabled}`]: (_theme$variants3 = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants3[ownerState.color],
    '&:focus-within::before': {
      '--Input-focused': '1'
    }
  }];
});
const StyledInputHtml = exports.StyledInputHtml = (0, _styles.styled)('input')(({
  ownerState
}) => ({
  border: 'none',
  // remove the native input width
  minWidth: 0,
  // remove the native input width
  outline: 0,
  // remove the native input outline
  padding: 0,
  // remove the native input padding
  flex: 1,
  color: 'inherit',
  backgroundColor: 'transparent',
  fontFamily: 'inherit',
  fontSize: 'inherit',
  fontStyle: 'inherit',
  fontWeight: 'inherit',
  lineHeight: 'inherit',
  textOverflow: 'ellipsis',
  '&:-webkit-autofill': (0, _extends2.default)({
    paddingInline: 'var(--Input-paddingInline)'
  }, !ownerState.startDecorator && {
    marginInlineStart: 'calc(-1 * var(--Input-paddingInline))',
    paddingInlineStart: 'var(--Input-paddingInline)',
    borderTopLeftRadius: 'calc(var(--Input-radius) - var(--variant-borderWidth, 0px))',
    borderBottomLeftRadius: 'calc(var(--Input-radius) - var(--variant-borderWidth, 0px))'
  }, !ownerState.endDecorator && {
    marginInlineEnd: 'calc(-1 * var(--Input-paddingInline))',
    paddingInlineEnd: 'var(--Input-paddingInline)',
    borderTopRightRadius: 'calc(var(--Input-radius) - var(--variant-borderWidth, 0px))',
    borderBottomRightRadius: 'calc(var(--Input-radius) - var(--variant-borderWidth, 0px))'
  }),
  '&::-webkit-input-placeholder': {
    color: 'var(--Input-placeholderColor)',
    opacity: 'var(--Input-placeholderOpacity)'
  },
  '&::-moz-placeholder': {
    // Firefox 19+
    color: 'var(--Input-placeholderColor)',
    opacity: 'var(--Input-placeholderOpacity)'
  },
  '&:-ms-input-placeholder': {
    // IE11
    color: 'var(--Input-placeholderColor)',
    opacity: 'var(--Input-placeholderOpacity)'
  },
  '&::-ms-input-placeholder': {
    // Edge
    color: 'var(--Input-placeholderColor)',
    opacity: 'var(--Input-placeholderOpacity)'
  }
}));
const StyledInputStartDecorator = exports.StyledInputStartDecorator = (0, _styles.styled)('div')({
  '--Button-margin': '0 0 0 calc(var(--Input-decoratorChildOffset) * -1)',
  '--IconButton-margin': '0 0 0 calc(var(--Input-decoratorChildOffset) * -1)',
  '--Icon-margin': '0 0 0 calc(var(--Input-paddingInline) / -4)',
  display: 'inherit',
  alignItems: 'center',
  paddingBlock: 'var(--unstable_InputPaddingBlock)',
  // for wrapping Autocomplete's tags
  flexWrap: 'wrap',
  // for wrapping Autocomplete's tags
  marginInlineEnd: 'var(--Input-gap)',
  color: 'var(--Input-decoratorColor)',
  cursor: 'initial'
});
const StyledInputEndDecorator = exports.StyledInputEndDecorator = (0, _styles.styled)('div')({
  '--Button-margin': '0 calc(var(--Input-decoratorChildOffset) * -1) 0 0',
  '--IconButton-margin': '0 calc(var(--Input-decoratorChildOffset) * -1) 0 0',
  '--Icon-margin': '0 calc(var(--Input-paddingInline) / -4) 0 0',
  display: 'inherit',
  alignItems: 'center',
  marginInlineStart: 'var(--Input-gap)',
  color: 'var(--Input-decoratorColor)',
  cursor: 'initial'
});
const InputRoot = (0, _styles.styled)(StyledInputRoot, {
  name: 'JoyInput',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({});
const InputInput = (0, _styles.styled)(StyledInputHtml, {
  name: 'JoyInput',
  slot: 'Input',
  overridesResolver: (props, styles) => styles.input
})({});
const InputStartDecorator = (0, _styles.styled)(StyledInputStartDecorator, {
  name: 'JoyInput',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})({});
const InputEndDecorator = (0, _styles.styled)(StyledInputEndDecorator, {
  name: 'JoyInput',
  slot: 'EndDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})({});
/**
 *
 * Demos:
 *
 * - [Input](https://mui.com/joy-ui/react-input/)
 *
 * API:
 *
 * - [Input API](https://mui.com/joy-ui/api/input/)
 */
const Input = /*#__PURE__*/React.forwardRef(function Input(inProps, ref) {
  var _ref, _inProps$error, _ref2, _inProps$size, _inProps$color, _formControl$color;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyInput'
  });
  const _useForwardedInput = (0, _useForwardedInput2.default)((0, _extends2.default)({}, props, {
      disabledInProp: inProps.disabled
    }), _inputClasses.default),
    {
      propsToForward,
      rootStateClasses,
      inputStateClasses,
      getRootProps,
      getInputProps,
      formControl,
      focused,
      error: errorProp = false,
      disabled,
      fullWidth = false,
      size: sizeProp = 'md',
      color: colorProp = 'neutral',
      variant = 'outlined',
      startDecorator,
      endDecorator,
      component,
      slots = {},
      slotProps = {}
    } = _useForwardedInput,
    other = (0, _objectWithoutPropertiesLoose2.default)(_useForwardedInput, _excluded);
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
  const error = (_ref = (_inProps$error = inProps.error) != null ? _inProps$error : formControl == null ? void 0 : formControl.error) != null ? _ref : errorProp;
  const size = (_ref2 = (_inProps$size = inProps.size) != null ? _inProps$size : formControl == null ? void 0 : formControl.size) != null ? _ref2 : sizeProp;
  const color = (_inProps$color = inProps.color) != null ? _inProps$color : error ? 'danger' : (_formControl$color = formControl == null ? void 0 : formControl.color) != null ? _formControl$color : colorProp;
  const ownerState = (0, _extends2.default)({
    instanceColor: error ? 'danger' : inProps.color
  }, props, {
    fullWidth,
    color,
    disabled,
    error,
    focused,
    size,
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
    className: [classes.root, rootStateClasses],
    elementType: InputRoot,
    getSlotProps: getRootProps,
    externalForwardedProps,
    ownerState
  });
  const [SlotInput, inputProps] = (0, _useSlot.default)('input', (0, _extends2.default)({}, formControl && {
    additionalProps: {
      id: formControl.htmlFor,
      'aria-describedby': formControl['aria-describedby']
    }
  }, {
    className: [classes.input, inputStateClasses],
    elementType: InputInput,
    getSlotProps: getInputProps,
    internalForwardedProps: propsToForward,
    externalForwardedProps,
    ownerState
  }));
  const [SlotStartDecorator, startDecoratorProps] = (0, _useSlot.default)('startDecorator', {
    className: classes.startDecorator,
    elementType: InputStartDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotEndDecorator, endDecoratorProps] = (0, _useSlot.default)('endDecorator', {
    className: classes.endDecorator,
    elementType: InputEndDecorator,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
      children: startDecorator
    })), /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotInput, (0, _extends2.default)({}, inputProps)), endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
      children: endDecorator
    }))]
  }));
});
process.env.NODE_ENV !== "production" ? Input.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * @ignore
   */
  autoComplete: _propTypes.default /* @typescript-to-proptypes-ignore */.string,
  /**
   * @ignore
   */
  autoFocus: _propTypes.default.bool,
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
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * @ignore
   */
  defaultValue: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.string), _propTypes.default.number, _propTypes.default.string]),
  /**
   * @ignore
   */
  disabled: _propTypes.default.bool,
  /**
   * Trailing adornment for this input.
   */
  endDecorator: _propTypes.default.node,
  /**
   * If `true`, the `input` will indicate an error.
   * The prop defaults to the value (`false`) inherited from the parent FormControl component.
   * @default false
   */
  error: _propTypes.default.bool,
  /**
   * If `true`, the input will take up the full width of its container.
   * @default false
   */
  fullWidth: _propTypes.default.bool,
  /**
   * @ignore
   */
  id: _propTypes.default.string,
  /**
   * @ignore
   */
  name: _propTypes.default.string,
  /**
   * @ignore
   */
  onChange: _propTypes.default.func,
  /**
   * @ignore
   */
  placeholder: _propTypes.default.string,
  /**
   * @ignore
   */
  readOnly: _propTypes.default.bool,
  /**
   * @ignore
   */
  required: _propTypes.default.bool,
  /**
   * The size of the component.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['sm', 'md', 'lg']), _propTypes.default.string]),
  /**
   * Leading adornment for this input.
   */
  startDecorator: _propTypes.default.node,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * @ignore
   */
  value: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.string), _propTypes.default.number, _propTypes.default.string]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'outlined'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Input;