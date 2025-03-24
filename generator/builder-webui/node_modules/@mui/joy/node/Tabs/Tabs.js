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
var _useTabs = require("@mui/base/useTabs");
var _system = require("@mui/system");
var _styles = require("../styles");
var _styleUtils = require("../styles/styleUtils");
var _SizeTabsContext = _interopRequireDefault(require("./SizeTabsContext"));
var _tabsClasses = require("./tabsClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "value", "defaultValue", "orientation", "direction", "component", "onChange", "selectionFollowsFocus", "variant", "color", "size", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    orientation,
    variant,
    color,
    size
  } = ownerState;
  const slots = {
    root: ['root', orientation, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _tabsClasses.getTabsUtilityClass, {});
};
const TabsRoot = (0, _styles.styled)('div', {
  name: 'JoyTabs',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => {
  var _theme$variants, _theme$variants2;
  const variantStyle = (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color];
  const {
    bgcolor,
    backgroundColor,
    background,
    p,
    padding
  } = (0, _styleUtils.resolveSxValue)({
    theme,
    ownerState
  }, ['bgcolor', 'backgroundColor', 'background', 'p', 'padding']);
  const resolvedBg = (0, _system.getPath)(theme, `palette.${bgcolor}`) || bgcolor || (0, _system.getPath)(theme, `palette.${backgroundColor}`) || backgroundColor || background || (variantStyle == null ? void 0 : variantStyle.backgroundColor) || (variantStyle == null ? void 0 : variantStyle.background) || theme.vars.palette.background.surface;
  return (0, _extends2.default)({}, ownerState.size === 'sm' && {
    '--Tabs-spacing': '0.75rem'
  }, ownerState.size === 'md' && {
    '--Tabs-spacing': '1rem'
  }, ownerState.size === 'lg' && {
    '--Tabs-spacing': '1.25rem'
  }, {
    '--Tab-indicatorThickness': '2px',
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon,
    '--TabList-stickyBackground': resolvedBg === 'transparent' ? 'initial' : resolvedBg,
    // for sticky TabList
    display: 'flex',
    flexDirection: 'column'
  }, ownerState.orientation === 'vertical' && {
    flexDirection: 'row'
  }, {
    backgroundColor: theme.vars.palette.background.surface,
    position: 'relative'
  }, theme.typography[`body-${ownerState.size}`], (_theme$variants2 = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants2[ownerState.color], p !== undefined && {
    '--Tabs-padding': p
  }, padding !== undefined && {
    '--Tabs-padding': padding
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
 * - [Tabs API](https://mui.com/joy-ui/api/tabs/)
 */
const Tabs = /*#__PURE__*/React.forwardRef(function Tabs(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyTabs'
  });
  const {
      children,
      value: valueProp,
      defaultValue: defaultValueProp,
      orientation = 'horizontal',
      direction = 'ltr',
      component,
      variant = 'plain',
      color = 'neutral',
      size = 'md',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const defaultValue = defaultValueProp || (valueProp === undefined ? 0 : undefined);
  const {
    contextValue
  } = (0, _useTabs.useTabs)((0, _extends2.default)({}, props, {
    orientation,
    defaultValue
  }));
  const ownerState = (0, _extends2.default)({}, props, {
    orientation,
    direction,
    variant,
    color,
    size
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    elementType: TabsRoot,
    externalForwardedProps,
    additionalProps: {
      ref,
      as: component
    },
    ownerState,
    className: classes.root
  });
  return (
    /*#__PURE__*/
    // @ts-ignore `defaultValue` between HTMLDiv and TabsProps is conflicted.
    (0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_useTabs.TabsProvider, {
        value: contextValue,
        children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_SizeTabsContext.default.Provider, {
          value: size,
          children: children
        })
      })
    }))
  );
});
process.env.NODE_ENV !== "production" ? Tabs.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The content of the component.
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
   * The default value. Use when the component is not controlled.
   */
  defaultValue: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The direction of the text.
   * @default 'ltr'
   */
  direction: _propTypes.default.oneOf(['ltr', 'rtl']),
  /**
   * Callback invoked when new value is being set.
   */
  onChange: _propTypes.default.func,
  /**
   * The component orientation (layout flow direction).
   * @default 'horizontal'
   */
  orientation: _propTypes.default.oneOf(['horizontal', 'vertical']),
  /**
   * If `true` the selected tab changes on focus. Otherwise it only
   * changes on activation.
   */
  selectionFollowsFocus: _propTypes.default.bool,
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
   * The value of the currently selected `Tab`.
   * If you don't want any selected `Tab`, you can set this prop to `null`.
   */
  value: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Tabs;