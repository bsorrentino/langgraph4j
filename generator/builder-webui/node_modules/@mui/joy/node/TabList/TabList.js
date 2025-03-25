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
var _useTabsList = require("@mui/base/useTabsList");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _List = require("../List/List");
var _ListProvider = _interopRequireWildcard(require("../List/ListProvider"));
var _SizeTabsContext = _interopRequireDefault(require("../Tabs/SizeTabsContext"));
var _tabListClasses = require("./tabListClasses");
var _tabClasses = _interopRequireDefault(require("../Tab/tabClasses"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "children", "variant", "color", "size", "disableUnderline", "underlinePlacement", "tabFlex", "sticky", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    orientation,
    size,
    variant,
    color
  } = ownerState;
  const slots = {
    root: ['root', orientation, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _tabListClasses.getTabListUtilityClass, {});
};
const TabListRoot = (0, _styled.default)(_List.StyledList, {
  name: 'JoyTabList',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants;
  const variantStyle = (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color];
  return (0, _extends2.default)({
    '--List-gap': '0px',
    '--ListDivider-gap': '0px',
    '--ListItem-paddingX': 'var(--Tabs-spacing)',
    '--ListItem-gap': '0.375rem',
    // the `var(--unknown,)` is a workaround because emotion does not support space toggle.
    '--unstable_TabList-hasUnderline': ownerState.disableUnderline ? 'var(--unknown,)' : 'initial'
  }, _ListProvider.scopedVariables, {
    flexGrow: 'initial',
    flexDirection: ownerState.orientation === 'vertical' ? 'column' : 'row',
    borderRadius: `var(--List-radius, 0px)`,
    padding: `var(--List-padding, 0px)`,
    zIndex: 1
  }, ownerState.sticky && {
    // sticky in list item can be found in grouped options
    position: 'sticky',
    top: ownerState.sticky === 'top' ? 'calc(-1 * var(--Tabs-padding, 0px))' : 'initial',
    bottom: ownerState.sticky === 'bottom' ? 'calc(-1 * var(--Tabs-padding, 0px))' : 'initial',
    backgroundColor: (variantStyle == null ? void 0 : variantStyle.backgroundColor) || `var(--TabList-stickyBackground, ${theme.vars.palette.background.body})`
  }, !ownerState.disableUnderline && (0, _extends2.default)({}, ownerState.underlinePlacement === 'bottom' && {
    '--unstable_TabList-underlineBottom': '1px',
    paddingBottom: 1,
    boxShadow: `inset 0 -1px ${theme.vars.palette.divider}`
  }, ownerState.underlinePlacement === 'top' && {
    '--unstable_TabList-underlineTop': '1px',
    paddingTop: 1,
    boxShadow: `inset 0 1px ${theme.vars.palette.divider}`
  }, ownerState.underlinePlacement === 'right' && {
    '--unstable_TabList-underlineRight': '1px',
    paddingRight: 1,
    boxShadow: `inset -1px 0 ${theme.vars.palette.divider}`
  }, ownerState.underlinePlacement === 'left' && {
    '--unstable_TabList-underlineLeft': '1px',
    paddingLeft: 1,
    boxShadow: `inset 1px 0 ${theme.vars.palette.divider}`
  }), ownerState.tabFlex && {
    [`& .${_tabClasses.default.root}`]: {
      flex: ownerState.tabFlex
    }
  });
});
/**
 *
 * Demos:
 *
 * - [Tabs](https://mui.com/joy-ui/react-tabs/)
 *
 * API:
 *
 * - [TabList API](https://mui.com/joy-ui/api/tab-list/)
 */
const TabList = /*#__PURE__*/React.forwardRef(function TabList(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyTabList'
  });
  const tabsSize = React.useContext(_SizeTabsContext.default);
  const {
    isRtl,
    orientation,
    getRootProps,
    contextValue
  } = (0, _useTabsList.useTabsList)({
    rootRef: ref
  });
  const {
      component = 'div',
      children,
      variant = 'plain',
      color = 'neutral',
      size: sizeProp,
      disableUnderline = false,
      underlinePlacement = orientation === 'horizontal' ? 'bottom' : 'right',
      tabFlex,
      sticky,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const size = sizeProp != null ? sizeProp : tabsSize;
  const ownerState = (0, _extends2.default)({}, props, {
    isRtl,
    orientation,
    variant,
    color,
    size,
    sticky,
    tabFlex,
    nesting: false,
    disableUnderline,
    underlinePlacement
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    elementType: TabListRoot,
    getSlotProps: getRootProps,
    externalForwardedProps,
    ownerState,
    className: classes.root
  });
  return (
    /*#__PURE__*/
    // @ts-ignore conflicted ref types
    (0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_useTabsList.TabsListProvider, {
        value: contextValue,
        children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ListProvider.default, {
          row: orientation === 'horizontal',
          nested: true,
          children: children
        })
      })
    }))
  );
});
process.env.NODE_ENV !== "production" ? TabList.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the TabList if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
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
   * If `true`, the TabList's underline will disappear.
   * @default false
   */
  disableUnderline: _propTypes.default.bool,
  /**
   * The size of the component.
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['sm', 'md', 'lg']), _propTypes.default.string]),
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
   * If provided, the TabList will have postion `sticky`.
   */
  sticky: _propTypes.default.oneOf(['bottom', 'top']),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The flex value of the Tab.
   * @example tabFlex={1} will set flex: '1 1 auto' on each tab (stretch the tab to equally fill the available space).
   */
  tabFlex: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The placement of the TabList's underline.
   * @default orientation === 'horizontal' ? 'bottom' : 'right'
   */
  underlinePlacement: _propTypes.default.oneOf(['bottom', 'left', 'right', 'top']),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = TabList;