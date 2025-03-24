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
var _utils = require("@mui/utils");
var _useSlider = require("@mui/base/useSlider");
var _utils2 = require("@mui/base/utils");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _sliderClasses = _interopRequireWildcard(require("./sliderClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["aria-label", "aria-valuetext", "className", "classes", "disableSwap", "disabled", "defaultValue", "getAriaLabel", "getAriaValueText", "marks", "max", "min", "name", "onChange", "onChangeCommitted", "onMouseDown", "orientation", "shiftStep", "scale", "step", "tabIndex", "track", "value", "valueLabelDisplay", "valueLabelFormat", "isRtl", "color", "size", "variant", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
// @ts-ignore
function Identity(x) {
  return x;
}
const useUtilityClasses = ownerState => {
  const {
    disabled,
    dragging,
    marked,
    orientation,
    track,
    variant,
    color,
    size
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', dragging && 'dragging', marked && 'marked', orientation === 'vertical' && 'vertical', track === 'inverted' && 'trackInverted', track === false && 'trackFalse', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    rail: ['rail'],
    track: ['track'],
    thumb: ['thumb', disabled && 'disabled'],
    input: ['input'],
    mark: ['mark'],
    markActive: ['markActive'],
    markLabel: ['markLabel'],
    markLabelActive: ['markLabelActive'],
    valueLabel: ['valueLabel'],
    valueLabelOpen: ['valueLabelOpen'],
    active: ['active'],
    focusVisible: ['focusVisible']
  };
  return (0, _utils.unstable_composeClasses)(slots, _sliderClasses.getSliderUtilityClass, {});
};
const sliderColorVariables = ({
  theme,
  ownerState
}) => (data = {}) => {
  var _theme$variants, _styles$VariantBor;
  const styles = ((_theme$variants = theme.variants[`${ownerState.variant}${data.state || ''}`]) == null ? void 0 : _theme$variants[ownerState.color]) || {};
  return (0, _extends2.default)({}, !data.state && {
    '--variant-borderWidth': (_styles$VariantBor = styles['--variant-borderWidth']) != null ? _styles$VariantBor : '0px'
  }, {
    '--Slider-trackColor': styles.color,
    '--Slider-thumbBackground': styles.color,
    '--Slider-thumbColor': styles.backgroundColor || theme.vars.palette.background.surface,
    '--Slider-trackBackground': styles.backgroundColor || theme.vars.palette.background.surface,
    '--Slider-trackBorderColor': styles.borderColor,
    '--Slider-railBackground': theme.vars.palette.background.level2
  });
};
const SliderRoot = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  const getColorVariables = sliderColorVariables({
    theme,
    ownerState
  });
  return [(0, _extends2.default)({
    '--Slider-size': 'max(42px, max(var(--Slider-thumbSize), var(--Slider-trackSize)))',
    // Reach 42px touch target, about ~8mm on screen.
    '--Slider-trackRadius': 'var(--Slider-size)',
    '--Slider-markBackground': theme.vars.palette.text.tertiary,
    [`& .${_sliderClasses.default.markActive}`]: {
      '--Slider-markBackground': 'var(--Slider-trackColor)'
    }
  }, ownerState.size === 'sm' && {
    '--Slider-markSize': '2px',
    '--Slider-trackSize': '4px',
    '--Slider-thumbSize': '14px',
    '--Slider-valueLabelArrowSize': '6px'
  }, ownerState.size === 'md' && {
    '--Slider-markSize': '2px',
    '--Slider-trackSize': '6px',
    '--Slider-thumbSize': '18px',
    '--Slider-valueLabelArrowSize': '8px'
  }, ownerState.size === 'lg' && {
    '--Slider-markSize': '3px',
    '--Slider-trackSize': '8px',
    '--Slider-thumbSize': '24px',
    '--Slider-valueLabelArrowSize': '10px'
  }, {
    '--Slider-thumbRadius': 'calc(var(--Slider-thumbSize) / 2)',
    '--Slider-thumbWidth': 'var(--Slider-thumbSize)'
  }, getColorVariables(), {
    '&:hover': {
      '@media (hover: hover)': (0, _extends2.default)({}, getColorVariables({
        state: 'Hover'
      }))
    },
    '&:active': (0, _extends2.default)({}, getColorVariables({
      state: 'Active'
    })),
    [`&.${_sliderClasses.default.disabled}`]: (0, _extends2.default)({
      pointerEvents: 'none',
      color: theme.vars.palette.text.tertiary
    }, getColorVariables({
      state: 'Disabled'
    })),
    boxSizing: 'border-box',
    display: 'inline-block',
    position: 'relative',
    cursor: 'pointer',
    touchAction: 'none',
    WebkitTapHighlightColor: 'transparent'
  }, ownerState.orientation === 'horizontal' && {
    padding: 'calc(var(--Slider-size) / 2) 0',
    width: '100%'
  }, ownerState.orientation === 'vertical' && {
    padding: '0 calc(var(--Slider-size) / 2)',
    height: '100%'
  }, {
    '@media print': {
      colorAdjust: 'exact'
    }
  })];
});
const SliderRail = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'Rail',
  overridesResolver: (props, styles) => styles.rail
})(({
  ownerState
}) => [(0, _extends2.default)({
  display: 'block',
  position: 'absolute',
  backgroundColor: ownerState.track === 'inverted' ? 'var(--Slider-trackBackground)' : 'var(--Slider-railBackground)',
  border: ownerState.track === 'inverted' ? 'var(--variant-borderWidth, 0px) solid var(--Slider-trackBorderColor)' : 'initial',
  borderRadius: 'var(--Slider-trackRadius)'
}, ownerState.orientation === 'horizontal' && {
  height: 'var(--Slider-trackSize)',
  top: '50%',
  left: 0,
  right: 0,
  transform: 'translateY(-50%)'
}, ownerState.orientation === 'vertical' && {
  width: 'var(--Slider-trackSize)',
  top: 0,
  bottom: 0,
  left: '50%',
  transform: 'translateX(-50%)'
}, ownerState.track === 'inverted' && {
  opacity: 1
})]);
const SliderTrack = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'Track',
  overridesResolver: (props, styles) => styles.track
})(({
  ownerState
}) => {
  return [(0, _extends2.default)({
    display: 'block',
    position: 'absolute',
    color: 'var(--Slider-trackColor)',
    border: ownerState.track === 'inverted' ? 'initial' : 'var(--variant-borderWidth, 0px) solid var(--Slider-trackBorderColor)',
    backgroundColor: ownerState.track === 'inverted' ? 'var(--Slider-railBackground)' : 'var(--Slider-trackBackground)'
  }, ownerState.orientation === 'horizontal' && {
    height: 'var(--Slider-trackSize)',
    top: '50%',
    transform: 'translateY(-50%)',
    borderRadius: 'var(--Slider-trackRadius) 0 0 var(--Slider-trackRadius)'
  }, ownerState.orientation === 'vertical' && {
    width: 'var(--Slider-trackSize)',
    left: '50%',
    transform: 'translateX(-50%)',
    borderRadius: '0 0 var(--Slider-trackRadius) var(--Slider-trackRadius)'
  }, ownerState.track === false && {
    display: 'none'
  })];
});
const SliderThumb = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'Thumb',
  overridesResolver: (props, styles) => styles.thumb
})(({
  ownerState,
  theme
}) => {
  var _theme$vars$palette;
  return (0, _extends2.default)({
    position: 'absolute',
    boxSizing: 'border-box',
    outline: 0,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: 'var(--Slider-thumbWidth)',
    height: 'var(--Slider-thumbSize)',
    border: 'var(--variant-borderWidth, 0px) solid var(--Slider-trackBorderColor)',
    borderRadius: 'var(--Slider-thumbRadius)',
    boxShadow: 'var(--Slider-thumbShadow)',
    color: 'var(--Slider-thumbColor)',
    backgroundColor: 'var(--Slider-thumbBackground)',
    [theme.focus.selector]: (0, _extends2.default)({}, theme.focus.default, {
      outlineOffset: 0,
      outlineWidth: 'max(4px, var(--Slider-thumbSize) / 3.6)',
      outlineColor: `rgba(${(_theme$vars$palette = theme.vars.palette) == null || (_theme$vars$palette = _theme$vars$palette[ownerState.color]) == null ? void 0 : _theme$vars$palette.mainChannel} / 0.32)`
    })
  }, ownerState.orientation === 'horizontal' && {
    top: '50%',
    transform: 'translate(-50%, -50%)'
  }, ownerState.orientation === 'vertical' && {
    left: '50%',
    transform: 'translate(-50%, 50%)'
  }, {
    '&::before': {
      // use pseudo element to create thumb's ring
      boxSizing: 'border-box',
      content: '""',
      display: 'block',
      position: 'absolute',
      background: 'transparent',
      // to not block the thumb's child
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      border: '2px solid',
      borderColor: 'var(--Slider-thumbColor)',
      borderRadius: 'inherit'
    }
  });
});
const SliderMark = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'Mark',
  overridesResolver: (props, styles) => styles.mark
})(({
  ownerState
}) => {
  return (0, _extends2.default)({
    position: 'absolute',
    width: 'var(--Slider-markSize)',
    height: 'var(--Slider-markSize)',
    borderRadius: 'var(--Slider-markSize)',
    backgroundColor: 'var(--Slider-markBackground)'
  }, ownerState.orientation === 'horizontal' && (0, _extends2.default)({
    top: '50%',
    transform: `translate(calc(var(--Slider-markSize) / -2), -50%)`
  }, ownerState.percent === 0 && {
    transform: `translate(min(var(--Slider-markSize), 3px), -50%)`
  }, ownerState.percent === 100 && {
    transform: `translate(calc(var(--Slider-markSize) * -1 - min(var(--Slider-markSize), 3px)), -50%)`
  }), ownerState.orientation === 'vertical' && (0, _extends2.default)({
    left: '50%',
    transform: 'translate(-50%, calc(var(--Slider-markSize) / 2))'
  }, ownerState.percent === 0 && {
    transform: `translate(-50%, calc(min(var(--Slider-markSize), 3px) * -1))`
  }, ownerState.percent === 100 && {
    transform: `translate(-50%, calc(var(--Slider-markSize) * 1 + min(var(--Slider-markSize), 3px)))`
  }));
});
const SliderValueLabel = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'ValueLabel',
  overridesResolver: (props, styles) => styles.valueLabel
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({}, ownerState.size === 'sm' && {
  fontSize: theme.fontSize.xs,
  lineHeight: theme.lineHeight.md,
  paddingInline: '0.25rem',
  minWidth: '20px'
}, ownerState.size === 'md' && {
  fontSize: theme.fontSize.sm,
  lineHeight: theme.lineHeight.md,
  paddingInline: '0.375rem',
  minWidth: '24px'
}, ownerState.size === 'lg' && {
  fontSize: theme.fontSize.md,
  lineHeight: theme.lineHeight.md,
  paddingInline: '0.5rem',
  minWidth: '28px'
}, {
  zIndex: 1,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  whiteSpace: 'nowrap',
  fontFamily: theme.vars.fontFamily.body,
  fontWeight: theme.vars.fontWeight.md,
  bottom: 0,
  transformOrigin: 'bottom center',
  transform: 'translateY(calc((var(--Slider-thumbSize) + var(--Slider-valueLabelArrowSize)) * -1)) scale(0)',
  position: 'absolute',
  backgroundColor: theme.vars.palette.background.tooltip,
  boxShadow: theme.shadow.sm,
  borderRadius: theme.vars.radius.xs,
  color: '#fff',
  '&::before': {
    display: 'var(--Slider-valueLabelArrowDisplay)',
    position: 'absolute',
    content: '""',
    color: theme.vars.palette.background.tooltip,
    bottom: 0,
    border: 'calc(var(--Slider-valueLabelArrowSize) / 2) solid',
    borderColor: 'currentColor',
    borderRightColor: 'transparent',
    borderBottomColor: 'transparent',
    borderLeftColor: 'transparent',
    left: '50%',
    transform: 'translate(-50%, 100%)',
    backgroundColor: 'transparent'
  },
  [`&.${_sliderClasses.default.valueLabelOpen}`]: {
    transform: 'translateY(calc((var(--Slider-thumbSize) + var(--Slider-valueLabelArrowSize)) * -1)) scale(1)'
  }
}));
const SliderMarkLabel = (0, _styles.styled)('span', {
  name: 'JoySlider',
  slot: 'MarkLabel',
  overridesResolver: (props, styles) => styles.markLabel
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({
  fontFamily: theme.vars.fontFamily.body
}, ownerState.size === 'sm' && {
  fontSize: theme.vars.fontSize.xs
}, ownerState.size === 'md' && {
  fontSize: theme.vars.fontSize.sm
}, ownerState.size === 'lg' && {
  fontSize: theme.vars.fontSize.md
}, {
  color: theme.palette.text.tertiary,
  position: 'absolute',
  whiteSpace: 'nowrap'
}, ownerState.orientation === 'horizontal' && {
  top: 'calc(50% + 4px + (max(var(--Slider-trackSize), var(--Slider-thumbSize)) / 2))',
  transform: 'translateX(-50%)'
}, ownerState.orientation === 'vertical' && {
  left: 'calc(50% + 8px + (max(var(--Slider-trackSize), var(--Slider-thumbSize)) / 2))',
  transform: 'translateY(50%)'
}));
const SliderInput = (0, _styles.styled)('input', {
  name: 'JoySlider',
  slot: 'Input',
  overridesResolver: (props, styles) => styles.input
})({});
/**
 *
 * Demos:
 *
 * - [Slider](https://mui.com/joy-ui/react-slider/)
 *
 * API:
 *
 * - [Slider API](https://mui.com/joy-ui/api/slider/)
 */
const Slider = /*#__PURE__*/React.forwardRef(function Slider(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoySlider'
  });
  const {
      'aria-label': ariaLabel,
      'aria-valuetext': ariaValuetext,
      className,
      classes: classesProp,
      disableSwap = false,
      disabled = false,
      defaultValue,
      getAriaLabel,
      getAriaValueText,
      marks: marksProp = false,
      max = 100,
      min = 0,
      orientation = 'horizontal',
      shiftStep = 10,
      scale = Identity,
      step = 1,
      track = 'normal',
      valueLabelDisplay = 'off',
      valueLabelFormat = Identity,
      isRtl = false,
      color = 'primary',
      size = 'md',
      variant = 'solid',
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    marks: marksProp,
    classes: classesProp,
    disabled,
    defaultValue,
    disableSwap,
    isRtl,
    max,
    min,
    orientation,
    shiftStep,
    scale,
    step,
    track,
    valueLabelDisplay,
    valueLabelFormat,
    color,
    size,
    variant
  });
  const {
    axisProps,
    getRootProps,
    getHiddenInputProps,
    getThumbProps,
    open,
    active,
    axis,
    focusedThumbIndex,
    range,
    dragging,
    marks,
    values,
    trackOffset,
    trackLeap,
    getThumbStyle
  } = (0, _useSlider.useSlider)((0, _extends2.default)({}, ownerState, {
    rootRef: ref
  }));
  ownerState.marked = marks.length > 0 && marks.some(mark => mark.label);
  ownerState.dragging = dragging;
  const trackStyle = (0, _extends2.default)({}, axisProps[axis].offset(trackOffset), axisProps[axis].leap(trackLeap));
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: SliderRoot,
    externalForwardedProps,
    getSlotProps: getRootProps,
    ownerState
  });
  const [SlotRail, railProps] = (0, _useSlot.default)('rail', {
    className: classes.rail,
    elementType: SliderRail,
    externalForwardedProps,
    ownerState
  });
  const [SlotTrack, trackProps] = (0, _useSlot.default)('track', {
    additionalProps: {
      style: trackStyle
    },
    className: classes.track,
    elementType: SliderTrack,
    externalForwardedProps,
    ownerState
  });
  const [SlotMark, markProps] = (0, _useSlot.default)('mark', {
    className: classes.mark,
    elementType: SliderMark,
    externalForwardedProps,
    ownerState
  });
  const [SlotMarkLabel, markLabelProps] = (0, _useSlot.default)('markLabel', {
    className: classes.markLabel,
    elementType: SliderMarkLabel,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      'aria-hidden': true
    }
  });
  const [SlotThumb, thumbProps] = (0, _useSlot.default)('thumb', {
    className: classes.thumb,
    elementType: SliderThumb,
    externalForwardedProps,
    getSlotProps: getThumbProps,
    ownerState
  });
  const [SlotInput, inputProps] = (0, _useSlot.default)('input', {
    className: classes.input,
    elementType: SliderInput,
    externalForwardedProps,
    getSlotProps: getHiddenInputProps,
    ownerState
  });
  const [SlotValueLabel, valueLabelProps] = (0, _useSlot.default)('valueLabel', {
    className: classes.valueLabel,
    elementType: SliderValueLabel,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRail, (0, _extends2.default)({}, railProps)), /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotTrack, (0, _extends2.default)({}, trackProps)), marks.filter(mark => mark.value >= min && mark.value <= max).map((mark, index) => {
      const percent = (0, _useSlider.valueToPercent)(mark.value, min, max);
      const style = axisProps[axis].offset(percent);
      let markActive;
      if (track === false) {
        markActive = values.indexOf(mark.value) !== -1;
      } else {
        markActive = track === 'normal' && (range ? mark.value >= values[0] && mark.value <= values[values.length - 1] : mark.value <= values[0]) || track === 'inverted' && (range ? mark.value <= values[0] || mark.value >= values[values.length - 1] : mark.value >= values[0]);
      }
      return /*#__PURE__*/(0, _jsxRuntime.jsxs)(React.Fragment, {
        children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(SlotMark, (0, _extends2.default)({
          "data-index": index
        }, markProps, !(0, _utils2.isHostComponent)(SlotMark) && {
          ownerState: (0, _extends2.default)({}, markProps.ownerState, {
            percent
          })
        }, {
          style: (0, _extends2.default)({}, style, markProps.style),
          className: (0, _clsx.default)(markProps.className, markActive && classes.markActive)
        })), mark.label != null ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotMarkLabel, (0, _extends2.default)({
          "data-index": index
        }, markLabelProps, {
          style: (0, _extends2.default)({}, style, markLabelProps.style),
          className: (0, _clsx.default)(classes.markLabel, markLabelProps.className, markActive && classes.markLabelActive),
          children: mark.label
        })) : null]
      }, mark.value);
    }), values.map((value, index) => {
      const percent = (0, _useSlider.valueToPercent)(value, min, max);
      const style = axisProps[axis].offset(percent);
      return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotThumb, (0, _extends2.default)({
        "data-index": index
      }, thumbProps, {
        className: (0, _clsx.default)(thumbProps.className, active === index && classes.active, focusedThumbIndex === index && classes.focusVisible),
        style: (0, _extends2.default)({}, style, getThumbStyle(index), thumbProps.style),
        children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(SlotInput, (0, _extends2.default)({
          "data-index": index,
          "aria-label": getAriaLabel ? getAriaLabel(index) : ariaLabel,
          "aria-valuenow": scale(value),
          "aria-valuetext": getAriaValueText ? getAriaValueText(scale(value), index) : ariaValuetext,
          value: values[index]
        }, inputProps)), valueLabelDisplay !== 'off' ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotValueLabel, (0, _extends2.default)({}, valueLabelProps, {
          className: (0, _clsx.default)(valueLabelProps.className, (open === index || active === index || valueLabelDisplay === 'on') && classes.valueLabelOpen),
          children: typeof valueLabelFormat === 'function' ? valueLabelFormat(scale(value), index) : valueLabelFormat
        })) : null]
      }), index);
    })]
  }));
});
process.env.NODE_ENV !== "production" ? Slider.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The label of the slider.
   */
  'aria-label': _propTypes.default.string,
  /**
   * A string value that provides a user-friendly name for the current value of the slider.
   */
  'aria-valuetext': _propTypes.default.string,
  /**
   * @ignore
   */
  children: _propTypes.default.node,
  /**
   * Override or extend the styles applied to the component.
   */
  classes: _propTypes.default.object,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   * @default 'primary'
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * The default value. Use when the component is not controlled.
   */
  defaultValue: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.number), _propTypes.default.number]),
  /**
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * If `true`, the active thumb doesn't swap when moving pointer over a thumb while dragging another thumb.
   * @default false
   */
  disableSwap: _propTypes.default.bool,
  /**
   * Accepts a function which returns a string value that provides a user-friendly name for the thumb labels of the slider.
   * This is important for screen reader users.
   * @param {number} index The thumb label's index to format.
   * @returns {string}
   */
  getAriaLabel: _propTypes.default.func,
  /**
   * Accepts a function which returns a string value that provides a user-friendly name for the current value of the slider.
   * This is important for screen reader users.
   * @param {number} value The thumb label's value to format.
   * @param {number} index The thumb label's index to format.
   * @returns {string}
   */
  getAriaValueText: _propTypes.default.func,
  /**
   * If `true` the Slider will be rendered right-to-left (with the lowest value on the right-hand side).
   * @default false
   */
  isRtl: _propTypes.default.bool,
  /**
   * Marks indicate predetermined values to which the user can move the slider.
   * If `true` the marks are spaced according the value of the `step` prop.
   * If an array, it should contain objects with `value` and an optional `label` keys.
   * @default false
   */
  marks: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.shape({
    label: _propTypes.default.node,
    value: _propTypes.default.number.isRequired
  })), _propTypes.default.bool]),
  /**
   * The maximum allowed value of the slider.
   * Should not be equal to min.
   * @default 100
   */
  max: _propTypes.default.number,
  /**
   * The minimum allowed value of the slider.
   * Should not be equal to max.
   * @default 0
   */
  min: _propTypes.default.number,
  /**
   * Name attribute of the hidden `input` element.
   */
  name: _propTypes.default.string,
  /**
   * Callback function that is fired when the slider's value changed.
   *
   * @param {Event} event The event source of the callback.
   * You can pull out the new value by accessing `event.target.value` (any).
   * **Warning**: This is a generic event not a change event.
   * @param {number | number[]} value The new value.
   * @param {number} activeThumb Index of the currently moved thumb.
   */
  onChange: _propTypes.default.func,
  /**
   * Callback function that is fired when the `mouseup` is triggered.
   *
   * @param {React.SyntheticEvent | Event} event The event source of the callback. **Warning**: This is a generic event not a change event.
   * @param {number | number[]} value The new value.
   */
  onChangeCommitted: _propTypes.default.func,
  /**
   * @ignore
   */
  onMouseDown: _propTypes.default.func,
  /**
   * The component orientation.
   * @default 'horizontal'
   */
  orientation: _propTypes.default.oneOf(['horizontal', 'vertical']),
  /**
   * A transformation function, to change the scale of the slider.
   * @param {any} x
   * @returns {any}
   * @default function Identity(x) {
   *   return x;
   * }
   */
  scale: _propTypes.default.func,
  /**
   * The granularity with which the slider can step through values when using Page Up/Page Down or Shift + Arrow Up/Arrow Down.
   * @default 10
   */
  shiftStep: _propTypes.default.number,
  /**
   * The size of the component.
   * It accepts theme values between 'sm' and 'lg'.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['sm', 'md', 'lg']), _propTypes.default.string]),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    input: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    mark: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    markLabel: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    rail: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    thumb: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    track: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    valueLabel: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    input: _propTypes.default.elementType,
    mark: _propTypes.default.elementType,
    markLabel: _propTypes.default.elementType,
    rail: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    thumb: _propTypes.default.elementType,
    track: _propTypes.default.elementType,
    valueLabel: _propTypes.default.elementType
  }),
  /**
   * The granularity with which the slider can step through values. (A "discrete" slider.)
   * The `min` prop serves as the origin for the valid values.
   * We recommend (max - min) to be evenly divisible by the step.
   *
   * When step is `null`, the thumb can only be slid onto marks provided with the `marks` prop.
   * @default 1
   */
  step: _propTypes.default.number,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * Tab index attribute of the hidden `input` element.
   */
  tabIndex: _propTypes.default.number,
  /**
   * The track presentation:
   *
   * - `normal` the track will render a bar representing the slider value.
   * - `inverted` the track will render a bar representing the remaining slider value.
   * - `false` the track will render without a bar.
   * @default 'normal'
   */
  track: _propTypes.default.oneOf(['inverted', 'normal', false]),
  /**
   * The value of the slider.
   * For ranged sliders, provide an array with two values.
   */
  value: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.number), _propTypes.default.number]),
  /**
   * Controls when the value label is displayed:
   *
   * - `auto` the value label will display when the thumb is hovered or focused.
   * - `on` will display persistently.
   * - `off` will never display.
   * @default 'off'
   */
  valueLabelDisplay: _propTypes.default.oneOf(['auto', 'off', 'on']),
  /**
   * The format function the value label's value.
   *
   * When a function is provided, it should have the following signature:
   *
   * - {number} value The value label's value to format
   * - {number} index The value label's index to format
   * @param {any} x
   * @returns {any}
   * @default function Identity(x) {
   *   return x;
   * }
   */
  valueLabelFormat: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.string]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'solid'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Slider;