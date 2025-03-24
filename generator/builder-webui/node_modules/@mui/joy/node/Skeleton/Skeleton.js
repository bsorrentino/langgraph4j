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
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _system = require("@mui/system");
var _base = require("@mui/base");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _skeletonClasses = require("./skeletonClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "component", "children", "animation", "overlay", "loading", "variant", "level", "height", "width", "sx", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    level
  } = ownerState;
  const slots = {
    root: ['root', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, level && `level${(0, _utils.unstable_capitalize)(level)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _skeletonClasses.getSkeletonUtilityClass, {});
};

// Add solid background for masking the component that has the same background.
// Otherwise, the pulse animation will not work properly.
const pulseKeyframe = (0, _system.keyframes)`
  0% {
    opacity: 1;
  }

  50% {
    opacity: 0.8;
    background: var(--unstable_pulse-bg);
  }

  100% {
    opacity: 1;
  }
`;
const waveKeyframe = (0, _system.keyframes)`
  0% {
    transform: translateX(-100%);
  }

  50% {
    /* +0.5s of delay between each loop */
    transform: translateX(100%);
  }

  100% {
    transform: translateX(100%);
  }
`;
const SkeletonRoot = (0, _styled.default)('span', {
  name: 'JoySkeleton',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(
/**
 * Animations
 */
({
  ownerState,
  theme
}) => ownerState.animation === 'pulse' && ownerState.variant !== 'inline' && (0, _system.css)`
      &::before {
        animation: ${pulseKeyframe} 2s ease-in-out 0.5s infinite;
        background: ${theme.vars.palette.background.level3};
      }
    `, ({
  ownerState,
  theme
}) => ownerState.animation === 'pulse' && ownerState.variant === 'inline' && (0, _system.css)`
      &::after {
        animation: ${pulseKeyframe} 2s ease-in-out 0.5s infinite;
        background: ${theme.vars.palette.background.level3};
      }
    `, ({
  ownerState,
  theme
}) => ownerState.animation === 'wave' && (0, _system.css)`
      /* Fix bug in Safari https://bugs.webkit.org/show_bug.cgi?id=68196 */
      -webkit-mask-image: -webkit-radial-gradient(white, black);
      background: ${theme.vars.palette.background.level3};

      &::after {
        content: ' ';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: var(--unstable_pseudo-zIndex);
        animation: ${waveKeyframe} 1.6s linear 0.5s infinite;
        background: linear-gradient(
          90deg,
          transparent,
          var(--unstable_wave-bg, rgba(0 0 0 / 0.08)),
          transparent
        );
        transform: translateX(-100%); /* Avoid flash during server-side hydration */
      }
    `,
/**
 * Implementation notes:
 * 1. The `Skeleton` has 3 parts:
 *  - the root (span) element as a container
 *  - the ::before pseudo-element for covering the content
 *  - the ::after pseudo-element for animation on top of the ::before pseudo-element
 *
 * 2. The root element and ::before will change to absolute position when shape="overlay" to cover the parent's content.
 *
 * 3. For geometry shape (rectangular, circular), the typography styles are applied to the root element so that width, height can be customized based on the font-size.
 */
({
  ownerState,
  theme
}) => {
  var _components, _theme$typography, _theme$typography2, _theme$typography3;
  const defaultLevel = ((_components = theme.components) == null || (_components = _components.JoyTypography) == null || (_components = _components.defaultProps) == null ? void 0 : _components.level) || 'body1';
  return [{
    display: 'block',
    position: 'relative',
    '--unstable_pseudo-zIndex': 9,
    '--unstable_pulse-bg': theme.vars.palette.background.level1,
    overflow: 'hidden',
    cursor: 'default',
    color: 'transparent',
    '& *': {
      visibility: 'hidden'
    },
    '&::before': {
      display: 'block',
      content: '" "',
      top: 0,
      bottom: 0,
      left: 0,
      right: 0,
      zIndex: 'var(--unstable_pseudo-zIndex)',
      borderRadius: 'inherit'
    },
    [theme.getColorSchemeSelector('dark')]: {
      '--unstable_wave-bg': 'rgba(255 255 255 / 0.1)'
    }
  }, ownerState.variant === 'rectangular' && (0, _extends2.default)({
    borderRadius: 'min(0.15em, 6px)',
    height: 'auto',
    width: '100%',
    '&::before': {
      position: 'absolute'
    }
  }, !ownerState.animation && {
    backgroundColor: theme.vars.palette.background.level3
  }, ownerState.level !== 'inherit' && (0, _extends2.default)({}, theme.typography[ownerState.level])), ownerState.variant === 'circular' && (0, _extends2.default)({
    borderRadius: '50%',
    width: '100%',
    height: '100%',
    '&::before': {
      position: 'absolute'
    }
  }, !ownerState.animation && {
    backgroundColor: theme.vars.palette.background.level3
  }, ownerState.level !== 'inherit' && (0, _extends2.default)({}, theme.typography[ownerState.level])), ownerState.variant === 'text' && (0, _extends2.default)({
    borderRadius: 'min(0.15em, 6px)',
    background: 'transparent',
    width: '100%'
  }, ownerState.level !== 'inherit' && (0, _extends2.default)({}, theme.typography[ownerState.level || defaultLevel], {
    paddingBlockStart: `calc((${((_theme$typography = theme.typography[ownerState.level || defaultLevel]) == null ? void 0 : _theme$typography.lineHeight) || 1} - 1) * 0.56em)`,
    paddingBlockEnd: `calc((${((_theme$typography2 = theme.typography[ownerState.level || defaultLevel]) == null ? void 0 : _theme$typography2.lineHeight) || 1} - 1) * 0.44em)`,
    '&::before': (0, _extends2.default)({
      height: '1em'
    }, theme.typography[ownerState.level || defaultLevel], ownerState.animation === 'wave' && {
      backgroundColor: theme.vars.palette.background.level3
    }, !ownerState.animation && {
      backgroundColor: theme.vars.palette.background.level3
    }),
    '&::after': (0, _extends2.default)({
      height: '1em',
      top: `calc((${((_theme$typography3 = theme.typography[ownerState.level || defaultLevel]) == null ? void 0 : _theme$typography3.lineHeight) || 1} - 1) * 0.56em)`
    }, theme.typography[ownerState.level || defaultLevel])
  })), ownerState.variant === 'inline' && (0, _extends2.default)({
    display: 'inline',
    position: 'initial',
    borderRadius: 'min(0.15em, 6px)'
  }, !ownerState.animation && {
    backgroundColor: theme.vars.palette.background.level3
  }, ownerState.level !== 'inherit' && (0, _extends2.default)({}, theme.typography[ownerState.level]), {
    WebkitMaskImage: '-webkit-radial-gradient(white, black)',
    '&::before': {
      position: 'absolute',
      zIndex: 'var(--unstable_pseudo-zIndex)',
      backgroundColor: theme.vars.palette.background.level3
    }
  }, ownerState.animation === 'pulse' && {
    '&::after': {
      content: '""',
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      zIndex: 'var(--unstable_pseudo-zIndex)',
      backgroundColor: theme.vars.palette.background.level3
    }
  }), ownerState.variant === 'overlay' && (0, _extends2.default)({
    borderRadius: theme.vars.radius.xs,
    position: 'absolute',
    width: '100%',
    height: '100%',
    zIndex: 'var(--unstable_pseudo-zIndex)'
  }, ownerState.animation === 'pulse' && {
    backgroundColor: theme.vars.palette.background.surface
  }, ownerState.level !== 'inherit' && (0, _extends2.default)({}, theme.typography[ownerState.level]), {
    '&::before': {
      position: 'absolute'
    }
  })];
});
/**
 *
 * Demos:
 *
 * - [Skeleton](https://mui.com/joy-ui/react-skeleton/)
 *
 * API:
 *
 * - [Skeleton API](https://mui.com/joy-ui/api/skeleton/)
 */
const Skeleton = /*#__PURE__*/React.forwardRef(function Skeleton(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoySkeleton'
  });
  const {
      className,
      component = 'span',
      children,
      animation = 'pulse',
      overlay = false,
      loading = true,
      variant = 'overlay',
      level = variant === 'text' ? 'body-md' : 'inherit',
      height,
      width,
      sx,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps,
    sx: [{
      width,
      height
    }, ...(Array.isArray(sx) ? sx : [sx])]
  });
  const ownerState = (0, _extends2.default)({}, props, {
    animation,
    component,
    level,
    loading,
    overlay,
    variant,
    width,
    height
  });
  const classes = useUtilityClasses(ownerState);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: SkeletonRoot,
    externalForwardedProps,
    ownerState
  });
  return loading ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  })) : /*#__PURE__*/(0, _jsxRuntime.jsx)(React.Fragment, {
    children: React.Children.map(children, (child, index) => index === 0 && /*#__PURE__*/React.isValidElement(child) ? /*#__PURE__*/React.cloneElement(child, {
      'data-first-child': ''
    }) : child)
  });
});
process.env.NODE_ENV !== "production" ? Skeleton.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The animation.
   * If `false` the animation effect is disabled.
   * @default 'pulse'
   */
  animation: _propTypes.default.oneOf(['pulse', 'wave', false]),
  /**
   * Used to render icon or text elements inside the Skeleton if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * Height of the skeleton.
   * Useful when you don't want to adapt the skeleton to a text element but for instance a card.
   */
  height: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string])), _propTypes.default.number, _propTypes.default.shape({
    lg: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    md: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    sm: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    xl: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    xs: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string])
  }), _propTypes.default.string]),
  /**
   * Applies the theme typography styles.
   * @default variant === 'text' ? 'body-md' : 'inherit'
   */
  level: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['h1', 'h2', 'h3', 'h4', 'title-lg', 'title-md', 'title-sm', 'body-lg', 'body-md', 'body-sm', 'body-xs', 'inherit']), _propTypes.default.string]),
  /**
   * If `true`, the skeleton appears.
   * @default true
   */
  loading: _propTypes.default.bool,
  /**
   * If `true`, the skeleton's position will change to `absolute` to fill the available space of the nearest parent.
   * This prop is useful to create a placeholder that has the element's dimensions.
   * @default false
   */
  overlay: _propTypes.default.bool,
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
   * The type of content that will be rendered.
   * @default 'overlay'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['circular', 'inline', 'overlay', 'rectangular', 'text']), _propTypes.default.string]),
  /**
   * Width of the skeleton.
   * Useful when the skeleton is inside an inline element with no width of its own.
   */
  width: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string])), _propTypes.default.number, _propTypes.default.shape({
    lg: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    md: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    sm: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    xl: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
    xs: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string])
  }), _propTypes.default.string])
} : void 0;

// @ts-ignore internal usage only with Typography and Link
Skeleton.muiName = 'Skeleton';
var _default = exports.default = Skeleton;