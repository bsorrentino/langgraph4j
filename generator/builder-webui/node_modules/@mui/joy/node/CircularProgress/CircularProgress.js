"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var React = _interopRequireWildcard(require("react"));
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _base = require("@mui/base");
var _system = require("@mui/system");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _circularProgressClasses = require("./circularProgressClasses");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["color", "backgroundColor"],
  _excluded2 = ["children", "className", "color", "size", "variant", "thickness", "determinate", "value", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const circulate = (0, _system.keyframes)({
  '0%': {
    // let the progress start at the top of the ring
    transform: 'rotate(-90deg)'
  },
  '100%': {
    transform: 'rotate(270deg)'
  }
});
const useUtilityClasses = ownerState => {
  const {
    determinate,
    color,
    variant,
    size
  } = ownerState;
  const slots = {
    root: ['root', determinate && 'determinate', color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    svg: ['svg'],
    track: ['track'],
    progress: ['progress']
  };
  return (0, _base.unstable_composeClasses)(slots, _circularProgressClasses.getCircularProgressUtilityClass, {});
};
function getThickness(slot, defaultValue) {
  return `var(--CircularProgress-${slot}Thickness, var(--CircularProgress-thickness, ${defaultValue}))`;
}
const CircularProgressRoot = (0, _styled.default)('span', {
  name: 'JoyCircularProgress',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => {
  var _theme$variants, _theme$variants$solid, _theme$variants$softH, _theme$variants$solid2;
  const _ref = ((_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]) || {},
    {
      color,
      backgroundColor
    } = _ref,
    rest = (0, _objectWithoutPropertiesLoose2.default)(_ref, _excluded);
  return (0, _extends2.default)({
    // integration with icon
    '--Icon-fontSize': 'calc(0.4 * var(--_root-size))',
    // public variables
    '--CircularProgress-trackColor': backgroundColor,
    '--CircularProgress-progressColor': color,
    '--CircularProgress-percent': ownerState.value,
    // 0 - 100
    '--CircularProgress-linecap': 'round'
  }, ownerState.size === 'sm' && {
    '--_root-size': 'var(--CircularProgress-size, 24px)',
    // use --_root-size to let other components overrides via --CircularProgress-size
    '--_track-thickness': getThickness('track', '3px'),
    '--_progress-thickness': getThickness('progress', '3px')
  }, ownerState.instanceSize === 'sm' && {
    '--CircularProgress-size': '24px'
  }, ownerState.size === 'md' && {
    '--_track-thickness': getThickness('track', '6px'),
    '--_progress-thickness': getThickness('progress', '6px'),
    '--_root-size': 'var(--CircularProgress-size, 40px)'
  }, ownerState.instanceSize === 'md' && {
    '--CircularProgress-size': '40px'
  }, ownerState.size === 'lg' && {
    '--_track-thickness': getThickness('track', '8px'),
    '--_progress-thickness': getThickness('progress', '8px'),
    '--_root-size': 'var(--CircularProgress-size, 64px)'
  }, ownerState.instanceSize === 'lg' && {
    '--CircularProgress-size': '64px'
  }, ownerState.thickness && {
    '--_track-thickness': `${ownerState.thickness}px`,
    '--_progress-thickness': `${ownerState.thickness}px`
  }, {
    // internal variables
    '--_thickness-diff': 'calc(var(--_track-thickness) - var(--_progress-thickness))',
    '--_inner-size': 'calc(var(--_root-size) - 2 * var(--variant-borderWidth, 0px))',
    '--_outlined-inset': 'max(var(--_track-thickness), var(--_progress-thickness))',
    width: 'var(--_root-size)',
    height: 'var(--_root-size)',
    borderRadius: 'var(--_root-size)',
    margin: 'var(--CircularProgress-margin)',
    boxSizing: 'border-box',
    display: 'inline-flex',
    justifyContent: 'center',
    alignItems: 'center',
    flexShrink: 0,
    // prevent from shrinking when CircularProgress is in a flex container.
    position: 'relative',
    color
  }, ownerState.children && {
    // only add font related properties when there is a child.
    // so that when there is no child, the size can be controlled by the parent font-size e.g. Link
    fontFamily: theme.vars.fontFamily.body,
    fontWeight: theme.vars.fontWeight.md,
    fontSize: 'calc(0.2 * var(--_root-size))'
  }, rest, ownerState.variant === 'outlined' && {
    '&::before': (0, _extends2.default)({
      content: '""',
      display: 'block',
      position: 'absolute',
      borderRadius: 'inherit',
      top: 'var(--_outlined-inset)',
      left: 'var(--_outlined-inset)',
      right: 'var(--_outlined-inset)',
      bottom: 'var(--_outlined-inset)'
    }, rest)
  }, ownerState.variant === 'soft' && {
    '--CircularProgress-trackColor': theme.variants.soft.neutral.backgroundColor,
    '--CircularProgress-progressColor': (_theme$variants$solid = theme.variants.solid) == null ? void 0 : _theme$variants$solid[ownerState.color].backgroundColor
  }, ownerState.variant === 'solid' && {
    '--CircularProgress-trackColor': (_theme$variants$softH = theme.variants.softHover) == null ? void 0 : _theme$variants$softH[ownerState.color].backgroundColor,
    '--CircularProgress-progressColor': (_theme$variants$solid2 = theme.variants.solid) == null ? void 0 : _theme$variants$solid2[ownerState.color].backgroundColor
  });
});
const CircularProgressSvg = (0, _styled.default)('svg', {
  name: 'JoyCircularProgress',
  slot: 'Svg',
  overridesResolver: (props, styles) => styles.svg
})({
  width: 'inherit',
  height: 'inherit',
  display: 'inherit',
  boxSizing: 'inherit',
  position: 'absolute',
  top: 'calc(-1 * var(--variant-borderWidth, 0px))',
  // centered align
  left: 'calc(-1 * var(--variant-borderWidth, 0px))' // centered align
});
const CircularProgressTrack = (0, _styled.default)('circle', {
  name: 'JoyCircularProgress',
  slot: 'track',
  overridesResolver: (props, styles) => styles.track
})({
  cx: '50%',
  cy: '50%',
  r: 'calc(var(--_inner-size) / 2 - var(--_track-thickness) / 2 + min(0px, var(--_thickness-diff) / 2))',
  fill: 'transparent',
  strokeWidth: 'var(--_track-thickness)',
  stroke: 'var(--CircularProgress-trackColor)'
});
const CircularProgressProgress = (0, _styled.default)('circle', {
  name: 'JoyCircularProgress',
  slot: 'progress',
  overridesResolver: (props, styles) => styles.progress
})({
  '--_progress-radius': 'calc(var(--_inner-size) / 2 - var(--_progress-thickness) / 2 - max(0px, var(--_thickness-diff) / 2))',
  '--_progress-length': 'calc(2 * 3.1415926535 * var(--_progress-radius))',
  // the circumference around the progress
  cx: '50%',
  cy: '50%',
  r: 'var(--_progress-radius)',
  fill: 'transparent',
  strokeWidth: 'var(--_progress-thickness)',
  stroke: 'var(--CircularProgress-progressColor)',
  strokeLinecap: 'var(--CircularProgress-linecap, round)',
  // can't use CSS variable directly, need to cast type.
  strokeDasharray: 'var(--_progress-length)',
  strokeDashoffset: 'calc(var(--_progress-length) - var(--CircularProgress-percent) * var(--_progress-length) / 100)',
  transformOrigin: 'center',
  transform: 'rotate(-90deg)' // to initially appear at the top-center of the circle.
}, ({
  ownerState
}) => !ownerState.determinate && (0, _system.css)`
      animation: var(--CircularProgress-circulation, 0.8s linear 0s infinite normal none running)
        ${circulate};
    `);

/**
 * ## ARIA
 *
 * If the progress bar is describing the loading progress of a particular region of a page,
 * you should use `aria-describedby` to point to the progress bar, and set the `aria-busy`
 * attribute to `true` on that region until it has finished loading.
 *
 * Demos:
 *
 * - [Circular Progress](https://mui.com/joy-ui/react-circular-progress/)
 *
 * API:
 *
 * - [CircularProgress API](https://mui.com/joy-ui/api/circular-progress/)
 */
const CircularProgress = /*#__PURE__*/React.forwardRef(function CircularProgress(inProps, ref) {
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'JoyCircularProgress'
  });
  const {
      children,
      className,
      color = 'primary',
      size = 'md',
      variant = 'soft',
      thickness,
      determinate = false,
      value = determinate ? 0 : 25,
      // `25` is the 1/4 of the circle.
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded2);
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    size,
    variant,
    thickness,
    value,
    determinate,
    instanceSize: inProps.size
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
    elementType: CircularProgressRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: (0, _extends2.default)({
      role: 'progressbar',
      style: {
        // Setting this CSS variable via inline-style
        // prevents the generation of new CSS every time
        // `value` prop updates
        '--CircularProgress-percent': value
      }
    }, value && determinate && {
      'aria-valuenow': typeof value === 'number' ? Math.round(value) : Math.round(Number(value || 0))
    })
  });
  const [SlotSvg, svgProps] = (0, _useSlot.default)('svg', {
    className: classes.svg,
    elementType: CircularProgressSvg,
    externalForwardedProps,
    ownerState
  });
  const [SlotTrack, trackProps] = (0, _useSlot.default)('track', {
    className: classes.track,
    elementType: CircularProgressTrack,
    externalForwardedProps,
    ownerState
  });
  const [SlotProgress, progressProps] = (0, _useSlot.default)('progress', {
    className: classes.progress,
    elementType: CircularProgressProgress,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotSvg, (0, _extends2.default)({}, svgProps, {
      children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(SlotTrack, (0, _extends2.default)({}, trackProps)), /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotProgress, (0, _extends2.default)({}, progressProps))]
    })), children]
  }));
});
process.env.NODE_ENV !== "production" ? CircularProgress.propTypes /* remove-proptypes */ = {
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
   * @default 'primary'
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * The boolean to select a variant.
   * Use indeterminate when there is no progress value.
   * @default false
   */
  determinate: _propTypes.default.bool,
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
    progress: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    svg: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    track: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    progress: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    svg: _propTypes.default.elementType,
    track: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The thickness of the circle.
   */
  thickness: _propTypes.default.number,
  /**
   * The value of the progress indicator for the determinate variant.
   * Value between 0 and 100.
   *
   * @default determinate ? 0 : 25
   */
  value: _propTypes.default.number,
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'soft'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = CircularProgress;