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
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _aspectRatioClasses = require("./aspectRatioClasses");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "ratio", "minHeight", "maxHeight", "objectFit", "color", "variant", "component", "flex", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color
  } = ownerState;
  const slots = {
    root: ['root'],
    content: ['content', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _aspectRatioClasses.getAspectRatioUtilityClass, {});
};

// Use to control the width of the content, usually in a flexbox row container
const AspectRatioRoot = (0, _styled.default)('div', {
  name: 'JoyAspectRatio',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => {
  const minHeight = typeof ownerState.minHeight === 'number' ? `${ownerState.minHeight}px` : ownerState.minHeight;
  const maxHeight = typeof ownerState.maxHeight === 'number' ? `${ownerState.maxHeight}px` : ownerState.maxHeight;
  return {
    // a context variable for any child component
    '--AspectRatio-paddingBottom': `clamp(var(--AspectRatio-minHeight), calc(100% / (${ownerState.ratio})), var(--AspectRatio-maxHeight))`,
    '--AspectRatio-maxHeight': maxHeight || '9999px',
    '--AspectRatio-minHeight': minHeight || '0px',
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon,
    borderRadius: 'var(--AspectRatio-radius)',
    display: ownerState.flex ? 'flex' : 'block',
    flex: ownerState.flex ? 1 : 'initial',
    flexDirection: 'column',
    margin: 'var(--AspectRatio-margin)'
  };
});
const AspectRatioContent = (0, _styled.default)('div', {
  name: 'JoyAspectRatio',
  slot: 'Content',
  overridesResolver: (props, styles) => styles.content
})(({
  theme,
  ownerState
}) => {
  var _theme$variants;
  return (0, _extends2.default)({
    flex: 1,
    position: 'relative',
    borderRadius: 'inherit',
    height: 0,
    paddingBottom: 'calc(var(--AspectRatio-paddingBottom) - 2 * var(--variant-borderWidth, 0px))',
    overflow: 'hidden',
    transition: 'inherit',
    // makes it easy to add transition to the content
    // use data-attribute instead of :first-child to support zero config SSR (emotion)
    // use nested selector for integrating with nextjs image `fill` layout (spans are inserted on top of the img)
    '& [data-first-child]': {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      boxSizing: 'border-box',
      position: 'absolute',
      width: '100%',
      height: '100%',
      objectFit: ownerState.objectFit,
      margin: 0,
      padding: 0,
      '& > img': {
        // support art-direction that uses <picture><img /></picture>
        width: '100%',
        height: '100%',
        objectFit: ownerState.objectFit
      }
    }
  }, theme.typography['body-md'], (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]);
});

/**
 *
 * Demos:
 *
 * - [Aspect Ratio](https://mui.com/joy-ui/react-aspect-ratio/)
 * - [Skeleton](https://mui.com/joy-ui/react-skeleton/)
 *
 * API:
 *
 * - [AspectRatio API](https://mui.com/joy-ui/api/aspect-ratio/)
 */
const AspectRatio = /*#__PURE__*/React.forwardRef(function AspectRatio(inProps, ref) {
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'JoyAspectRatio'
  });
  const {
      children,
      ratio = '16 / 9',
      minHeight,
      maxHeight,
      objectFit = 'cover',
      color = 'neutral',
      variant = 'soft',
      component,
      flex = false,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    flex,
    minHeight,
    maxHeight,
    objectFit,
    ratio,
    color,
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
    className: classes.root,
    elementType: AspectRatioRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotContent, contentProps] = (0, _useSlot.default)('content', {
    className: classes.content,
    elementType: AspectRatioContent,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotContent, (0, _extends2.default)({}, contentProps, {
      children: React.Children.map(children, (child, index) => index === 0 && /*#__PURE__*/React.isValidElement(child) ? /*#__PURE__*/React.cloneElement(child, {
        'data-first-child': ''
      }) : child)
    }))
  }));
});
process.env.NODE_ENV !== "production" ? AspectRatio.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the AspectRatio if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
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
   * By default, the AspectRatio will maintain the aspect ratio of its content.
   * Set this prop to `true` when the container is a flex row and you want the AspectRatio to fill the height of its container.
   * @default false
   */
  flex: _propTypes.default.bool,
  /**
   * The maximum calculated height of the element (not the CSS height).
   */
  maxHeight: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The minimum calculated height of the element (not the CSS height).
   */
  minHeight: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The CSS object-fit value of the first-child.
   * @default 'cover'
   */
  objectFit: _propTypes.default.oneOf(['-moz-initial', 'contain', 'cover', 'fill', 'inherit', 'initial', 'none', 'revert-layer', 'revert', 'scale-down', 'unset']),
  /**
   * The aspect-ratio of the element. The current implementation uses padding instead of the CSS aspect-ratio due to browser support.
   * https://caniuse.com/?search=aspect-ratio
   * @default '16 / 9'
   */
  ratio: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    content: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    content: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
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
var _default = exports.default = AspectRatio;