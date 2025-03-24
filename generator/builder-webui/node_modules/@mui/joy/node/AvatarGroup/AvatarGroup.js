"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.AvatarGroupContext = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _base = require("@mui/base");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _avatarGroupClasses = require("./avatarGroupClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "color", "component", "size", "variant", "children", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const AvatarGroupContext = exports.AvatarGroupContext = /*#__PURE__*/React.createContext(undefined);
if (process.env.NODE_ENV !== 'production') {
  AvatarGroupContext.displayName = 'AvatarGroupContext';
}
const useUtilityClasses = () => {
  const slots = {
    root: ['root']
  };
  return (0, _base.unstable_composeClasses)(slots, _avatarGroupClasses.getAvatarGroupUtilityClass, {});
};
const AvatarGroupGroupRoot = (0, _styled.default)('div', {
  name: 'JoyAvatarGroup',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => (0, _extends2.default)({}, ownerState.size === 'sm' && {
  '--AvatarGroup-gap': '-0.375rem',
  '--Avatar-ringSize': '2px'
}, ownerState.size === 'md' && {
  '--AvatarGroup-gap': '-0.5rem',
  '--Avatar-ringSize': '2px'
}, ownerState.size === 'lg' && {
  '--AvatarGroup-gap': '-0.625rem',
  '--Avatar-ringSize': '4px'
}, {
  '--Avatar-ring': `0 0 0 var(--Avatar-ringSize) var(--Avatar-ringColor, ${theme.vars.palette.background.surface})`,
  '--Avatar-marginInlineStart': 'var(--AvatarGroup-gap)',
  display: 'flex',
  marginInlineStart: 'calc(-1 * var(--AvatarGroup-gap))'
}));

/**
 *
 * Demos:
 *
 * - [Avatar](https://mui.com/joy-ui/react-avatar/)
 *
 * API:
 *
 * - [AvatarGroup API](https://mui.com/joy-ui/api/avatar-group/)
 */
const AvatarGroup = /*#__PURE__*/React.forwardRef(function AvatarGroup(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAvatarGroup'
  });
  const {
      className,
      color,
      component = 'div',
      size = 'md',
      variant,
      children,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = React.useMemo(() => (0, _extends2.default)({}, props, {
    color,
    component,
    size,
    variant
  }), [color, component, props, size, variant]);
  const classes = useUtilityClasses();
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: AvatarGroupGroupRoot,
    externalForwardedProps: (0, _extends2.default)({}, other, {
      component,
      slots,
      slotProps
    }),
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(AvatarGroupContext.Provider, {
    value: ownerState,
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: children
    }))
  });
});
process.env.NODE_ENV !== "production" ? AvatarGroup.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the AvatarGroup if `src` is not set.
   * This can be an element, or just a string.
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
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * The size of the component.
   * It accepts theme values between 'sm' and 'lg'.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['lg', 'md', 'sm']), _propTypes.default.string]),
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
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'soft'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = AvatarGroup;