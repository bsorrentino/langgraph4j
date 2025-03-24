"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.DividerRoot = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _styles = require("../styles");
var _dividerClasses = require("./dividerClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "children", "component", "inset", "orientation", "role", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    orientation,
    inset
  } = ownerState;
  const slots = {
    root: ['root', orientation, inset && `inset${(0, _utils.unstable_capitalize)(inset)}`]
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _dividerClasses.getDividerUtilityClass, {});
};
const DividerRoot = exports.DividerRoot = (0, _styles.styled)('hr', {
  name: 'JoyDivider',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({
  '--Divider-thickness': '1px',
  '--Divider-lineColor': theme.vars.palette.divider
}, ownerState.inset === 'none' && {
  '--_Divider-inset': '0px'
}, ownerState.inset === 'context' && {
  '--_Divider-inset': 'var(--Divider-inset, 0px)'
}, {
  margin: 'initial',
  // reset margin for `hr` tag
  marginInline: ownerState.orientation === 'vertical' ? 'initial' : 'var(--_Divider-inset)',
  marginBlock: ownerState.orientation === 'vertical' ? 'var(--_Divider-inset)' : 'initial',
  position: 'relative',
  alignSelf: 'stretch',
  flexShrink: 0
}, ownerState.children ? (0, _extends2.default)({
  '--Divider-gap': theme.spacing(1),
  '--Divider-childPosition': '50%',
  display: 'flex',
  flexDirection: ownerState.orientation === 'vertical' ? 'column' : 'row',
  alignItems: 'center',
  whiteSpace: 'nowrap',
  textAlign: 'center',
  border: 0
}, theme.typography['body-sm'], {
  '&::before, &::after': {
    position: 'relative',
    inlineSize: ownerState.orientation === 'vertical' ? 'var(--Divider-thickness)' : 'initial',
    blockSize: ownerState.orientation === 'vertical' ? 'initial' : 'var(--Divider-thickness)',
    backgroundColor: 'var(--Divider-lineColor)',
    // use logical size + background is better than border because they work with gradient.
    content: '""'
  },
  '&::before': {
    marginInlineEnd: ownerState.orientation === 'vertical' ? 'initial' : 'min(var(--Divider-childPosition) * 999, var(--Divider-gap))',
    marginBlockEnd: ownerState.orientation === 'vertical' ? 'min(var(--Divider-childPosition) * 999, var(--Divider-gap))' : 'initial',
    flexBasis: 'var(--Divider-childPosition)'
  },
  '&::after': {
    marginInlineStart: ownerState.orientation === 'vertical' ? 'initial' : 'min((100% - var(--Divider-childPosition)) * 999, var(--Divider-gap))',
    marginBlockStart: ownerState.orientation === 'vertical' ? 'min((100% - var(--Divider-childPosition)) * 999, var(--Divider-gap))' : 'initial',
    flexBasis: 'calc(100% - var(--Divider-childPosition))'
  }
}) : {
  border: 'none',
  // reset the border for `hr` tag
  listStyle: 'none',
  backgroundColor: 'var(--Divider-lineColor)',
  // use logical size + background is better than border because they work with gradient.
  inlineSize: ownerState.orientation === 'vertical' ? 'var(--Divider-thickness)' : 'initial',
  blockSize: ownerState.orientation === 'vertical' ? 'initial' : 'var(--Divider-thickness)'
}));
/**
 *
 * Demos:
 *
 * - [Divider](https://mui.com/joy-ui/react-divider/)
 *
 * API:
 *
 * - [Divider API](https://mui.com/joy-ui/api/divider/)
 */
const Divider = /*#__PURE__*/React.forwardRef(function Divider(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyDivider'
  });
  const {
      className,
      children,
      component = children !== undefined && children !== null ? 'div' : 'hr',
      inset,
      orientation = 'horizontal',
      role = component !== 'hr' ? 'separator' : undefined,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    inset,
    role,
    orientation,
    component
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
    elementType: DividerRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: (0, _extends2.default)({
      as: component,
      role
    }, role === 'separator' && orientation === 'vertical' && {
      // The implicit aria-orientation of separator is 'horizontal'
      // https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Roles/separator_role
      'aria-orientation': 'vertical'
    })
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? Divider.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The content of the component.
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
   * Class name applied to the divider to shrink or stretch the line based on the orientation.
   */
  inset: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['none', 'context']), _propTypes.default.string]),
  /**
   * The component orientation.
   * @default 'horizontal'
   */
  orientation: _propTypes.default.oneOf(['horizontal', 'vertical']),
  /**
   * @ignore
   */
  role: _propTypes.default /* @typescript-to-proptypes-ignore */.string,
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
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object])
} : void 0;

// @ts-ignore internal logic
Divider.muiName = 'Divider';
var _default = exports.default = Divider;