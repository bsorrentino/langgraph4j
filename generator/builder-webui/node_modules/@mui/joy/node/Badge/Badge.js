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
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _badgeClasses = _interopRequireWildcard(require("./badgeClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["anchorOrigin", "badgeInset", "children", "size", "color", "invisible", "max", "badgeContent", "showZero", "variant", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    color,
    variant,
    size,
    anchorOrigin,
    invisible
  } = ownerState;
  const slots = {
    root: ['root'],
    badge: ['badge', invisible && 'invisible', anchorOrigin && `anchorOrigin${(0, _utils.unstable_capitalize)(anchorOrigin.vertical)}${(0, _utils.unstable_capitalize)(anchorOrigin.horizontal)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _badgeClasses.getBadgeUtilityClass, {});
};
const BadgeRoot = (0, _styled.default)('span', {
  name: 'JoyBadge',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({}, ownerState.size === 'sm' && (0, _extends2.default)({
  '--Badge-minHeight': '0.5rem'
}, ownerState.badgeContent && {
  '--Badge-minHeight': '1rem'
}, {
  '--Badge-paddingX': '0.25rem'
}), ownerState.size === 'md' && (0, _extends2.default)({
  '--Badge-minHeight': '0.75rem'
}, ownerState.badgeContent && {
  '--Badge-minHeight': '1.25rem'
}, {
  '--Badge-paddingX': '0.375rem'
}), ownerState.size === 'lg' && (0, _extends2.default)({
  '--Badge-minHeight': '1rem'
}, ownerState.badgeContent && {
  '--Badge-minHeight': '1.5rem'
}, {
  '--Badge-paddingX': '0.5rem'
}), {
  '--Badge-ringSize': '2px',
  '--Badge-ring': `0 0 0 var(--Badge-ringSize) var(--Badge-ringColor, ${theme.vars.palette.background.surface})`,
  position: 'relative',
  display: 'inline-flex',
  // For correct alignment with the text.
  verticalAlign: 'middle',
  flexShrink: 0
}));
const BadgeBadge = (0, _styled.default)('span', {
  name: 'JoyBadge',
  slot: 'Badge',
  overridesResolver: (props, styles) => styles.badge
})(({
  theme,
  ownerState
}) => {
  var _ownerState$anchorOri, _ownerState$anchorOri2, _ownerState$anchorOri3, _ownerState$anchorOri4, _typography$lineHeigh, _theme$variants;
  const inset = {
    top: ownerState.badgeInset,
    left: ownerState.badgeInset,
    bottom: ownerState.badgeInset,
    right: ownerState.badgeInset
  };
  if (typeof ownerState.badgeInset === 'string') {
    const insetValues = ownerState.badgeInset.split(' ');
    if (insetValues.length > 1) {
      inset.top = insetValues[0];
      inset.right = insetValues[1];
      if (insetValues.length === 2) {
        inset.bottom = insetValues[0];
        inset.left = insetValues[1];
      }
      if (insetValues.length === 3) {
        inset.left = insetValues[1];
        inset.bottom = insetValues[2];
      }
      if (insetValues.length === 4) {
        inset.bottom = insetValues[2];
        inset.left = insetValues[3];
      }
    }
  }
  const translateY = ((_ownerState$anchorOri = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri.vertical) === 'top' ? 'translateY(-50%)' : 'translateY(50%)';
  const translateX = ((_ownerState$anchorOri2 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri2.horizontal) === 'left' ? 'translateX(-50%)' : 'translateX(50%)';
  const transformOriginY = ((_ownerState$anchorOri3 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri3.vertical) === 'top' ? '0%' : '100%';
  const transformOriginX = ((_ownerState$anchorOri4 = ownerState.anchorOrigin) == null ? void 0 : _ownerState$anchorOri4.horizontal) === 'left' ? '0%' : '100%';
  const typography = theme.typography[`body-${{
    sm: 'xs',
    md: 'sm',
    lg: 'md'
  }[ownerState.size]}`];
  return (0, _extends2.default)({
    '--Icon-color': 'currentColor',
    '--Icon-fontSize': `calc(1em * ${(_typography$lineHeigh = typography == null ? void 0 : typography.lineHeight) != null ? _typography$lineHeigh : '1'})`,
    display: 'inline-flex',
    flexWrap: 'wrap',
    justifyContent: 'center',
    alignContent: 'center',
    alignItems: 'center',
    position: 'absolute',
    boxSizing: 'border-box',
    boxShadow: 'var(--Badge-ring)',
    lineHeight: 1,
    padding: '0 calc(var(--Badge-paddingX) - var(--variant-borderWidth, 0px))',
    minHeight: 'var(--Badge-minHeight)',
    minWidth: 'var(--Badge-minHeight)',
    borderRadius: 'var(--Badge-radius, var(--Badge-minHeight))',
    zIndex: theme.vars.zIndex.badge,
    backgroundColor: theme.vars.palette.background.surface,
    [ownerState.anchorOrigin.vertical]: inset[ownerState.anchorOrigin.vertical],
    [ownerState.anchorOrigin.horizontal]: inset[ownerState.anchorOrigin.horizontal],
    transform: `scale(1) ${translateX} ${translateY}`,
    transformOrigin: `${transformOriginX} ${transformOriginY}`,
    [`&.${_badgeClasses.default.invisible}`]: {
      transform: `scale(0) ${translateX} ${translateY}`
    }
  }, typography, {
    fontWeight: theme.vars.fontWeight.md
  }, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]);
});
/**
 *
 * Demos:
 *
 * - [Badge](https://mui.com/joy-ui/react-badge/)
 *
 * API:
 *
 * - [Badge API](https://mui.com/joy-ui/api/badge/)
 */
const Badge = /*#__PURE__*/React.forwardRef(function Badge(inProps, ref) {
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'JoyBadge'
  });
  const {
      anchorOrigin: anchorOriginProp = {
        vertical: 'top',
        horizontal: 'right'
      },
      badgeInset: badgeInsetProp = 0,
      children,
      size: sizeProp = 'md',
      color: colorProp = 'primary',
      invisible: invisibleProp = false,
      max = 99,
      badgeContent: badgeContentProp = '',
      showZero = false,
      variant: variantProp = 'solid',
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const prevProps = (0, _utils.usePreviousProps)({
    anchorOrigin: anchorOriginProp,
    size: sizeProp,
    badgeInset: badgeInsetProp,
    color: colorProp,
    variant: variantProp
  });
  let invisible = invisibleProp;
  if (invisibleProp === false && (badgeContentProp === 0 && !showZero || badgeContentProp == null)) {
    invisible = true;
  }
  const {
    color = colorProp,
    size = sizeProp,
    anchorOrigin = anchorOriginProp,
    variant = variantProp,
    badgeInset = badgeInsetProp
  } = invisible ? prevProps : props;
  const ownerState = (0, _extends2.default)({}, props, {
    anchorOrigin,
    badgeInset,
    variant,
    invisible,
    color,
    size
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  let displayValue = badgeContentProp && Number(badgeContentProp) > max ? `${max}+` : badgeContentProp;
  if (invisible && badgeContentProp === 0) {
    displayValue = '';
  }
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: BadgeRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotBadge, badgeProps] = (0, _useSlot.default)('badge', {
    className: classes.badge,
    elementType: BadgeBadge,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [children, /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotBadge, (0, _extends2.default)({}, badgeProps, {
      children: displayValue
    }))]
  }));
});
process.env.NODE_ENV !== "production" ? Badge.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The anchor of the badge.
   * @default {
   *   vertical: 'top',
   *   horizontal: 'right',
   * }
   */
  anchorOrigin: _propTypes.default.shape({
    horizontal: _propTypes.default.oneOf(['left', 'right']).isRequired,
    vertical: _propTypes.default.oneOf(['bottom', 'top']).isRequired
  }),
  /**
   * The content rendered within the badge.
   * @default ''
   */
  badgeContent: _propTypes.default.node,
  /**
   * The inset of the badge. Support shorthand syntax as described in https://developer.mozilla.org/en-US/docs/Web/CSS/inset.
   * @default 0
   */
  badgeInset: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The badge will be added relative to this node.
   */
  children: _propTypes.default.node,
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
   * If `true`, the badge is invisible.
   * @default false
   */
  invisible: _propTypes.default.bool,
  /**
   * Max count to show.
   * @default 99
   */
  max: _propTypes.default.number,
  /**
   * Controls whether the badge is hidden when `badgeContent` is zero.
   * @default false
   */
  showZero: _propTypes.default.bool,
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
    badge: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    badge: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
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
var _default = exports.default = Badge;