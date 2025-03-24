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
var _composeClasses = require("@mui/base/composeClasses");
var _styles = require("../styles");
var _listItemDecoratorClasses = require("./listItemDecoratorClasses");
var _ListItemButtonOrientationContext = _interopRequireDefault(require("../ListItemButton/ListItemButtonOrientationContext"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "className", "children", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = () => {
  const slots = {
    root: ['root']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _listItemDecoratorClasses.getListItemDecoratorUtilityClass, {});
};
const ListItemDecoratorRoot = (0, _styles.styled)('span', {
  name: 'JoyListItemDecorator',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState
}) => (0, _extends2.default)({
  boxSizing: 'border-box',
  display: 'inline-flex',
  alignItems: `var(--unstable_ListItemDecorator-alignItems, ${ownerState.parentOrientation === 'horizontal' ? 'center' : 'initial'})`
}, ownerState.parentOrientation === 'horizontal' ? {
  minInlineSize: 'var(--ListItemDecorator-size)',
  marginInlineEnd: 'calc(-1 * var(--ListItem-gap))'
} : {
  minBlockSize: 'var(--ListItemDecorator-size)',
  justifyContent: 'center',
  marginBlockEnd: 'calc(-1 * var(--ListItem-gap))'
}));
/**
 *
 * Demos:
 *
 * - [Lists](https://mui.com/joy-ui/react-list/)
 *
 * API:
 *
 * - [ListItemDecorator API](https://mui.com/joy-ui/api/list-item-decorator/)
 */
const ListItemDecorator = /*#__PURE__*/React.forwardRef(function ListItemDecorator(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyListItemDecorator'
  });
  const {
      component,
      className,
      children,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const parentOrientation = React.useContext(_ListItemButtonOrientationContext.default);
  const ownerState = (0, _extends2.default)({
    parentOrientation
  }, props);
  const classes = useUtilityClasses();
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: ListItemDecoratorRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? ListItemDecorator.propTypes /* remove-proptypes */ = {
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
var _default = exports.default = ListItemDecorator;