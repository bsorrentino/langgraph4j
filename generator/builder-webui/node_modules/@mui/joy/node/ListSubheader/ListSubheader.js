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
var _composeClasses = require("@mui/base/composeClasses");
var _styles = require("../styles");
var _listSubheaderClasses = require("./listSubheaderClasses");
var _ListSubheaderContext = _interopRequireDefault(require("./ListSubheaderContext"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _colorInversionUtils = require("../colorInversion/colorInversionUtils");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "className", "children", "id", "sticky", "variant", "color", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    sticky
  } = ownerState;
  const slots = {
    root: ['root', sticky && 'sticky', color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`]
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _listSubheaderClasses.getListSubheaderUtilityClass, {});
};
const ListSubheaderRoot = (0, _styles.styled)('div', {
  name: 'JoyListSubheader',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$vars$palette, _theme$variants;
  return (0, _extends2.default)({
    boxSizing: 'border-box',
    display: 'flex',
    alignItems: 'center',
    marginInline: 'var(--ListItem-marginInline)',
    paddingBlock: 'var(--ListItem-paddingY)',
    paddingInlineStart: 'var(--ListItem-paddingLeft)',
    paddingInlineEnd: 'var(--ListItem-paddingRight)',
    minBlockSize: 'var(--ListItem-minHeight)'
  }, theme.typography['body-xs'], {
    fontSize: 'max(0.75em, 0.625rem)',
    textTransform: 'uppercase',
    letterSpacing: '0.1em'
  }, ownerState.sticky && {
    position: 'sticky',
    top: 'var(--ListItem-stickyTop, 0px)',
    // integration with Menu and Select.
    zIndex: 1,
    background: 'var(--ListItem-stickyBackground)'
  }, {
    color: ownerState.color ? `var(--_Link-color, rgba(${(_theme$vars$palette = theme.vars.palette[ownerState.color]) == null ? void 0 : _theme$vars$palette.mainChannel} / 1))` : theme.vars.palette.text.tertiary
  }, ownerState.instanceColor && {
    [`&:not([${_colorInversionUtils.INVERTED_COLORS_ATTR}])`]: {
      '--_Link-color': theme.vars.palette.text.secondary
    }
  }, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]);
});
/**
 *
 * Demos:
 *
 * - [Lists](https://mui.com/joy-ui/react-list/)
 *
 * API:
 *
 * - [ListSubheader API](https://mui.com/joy-ui/api/list-subheader/)
 */
const ListSubheader = /*#__PURE__*/React.forwardRef(function ListSubheader(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyListSubheader'
  });
  const {
      component,
      className,
      children,
      id: idOverride,
      sticky = false,
      variant,
      color,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const id = (0, _utils.unstable_useId)(idOverride);
  const setSubheaderId = React.useContext(_ListSubheaderContext.default);
  React.useEffect(() => {
    if (setSubheaderId) {
      setSubheaderId(id || '');
    }
  }, [setSubheaderId, id]);
  const ownerState = (0, _extends2.default)({
    instanceColor: inProps.color
  }, props, {
    id,
    sticky,
    variant,
    color: variant ? color != null ? color : 'neutral' : color
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
    elementType: ListSubheaderRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      as: component,
      id
    }
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? ListSubheader.propTypes /* remove-proptypes */ = {
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
   * The color of the component. It supports those theme colors that make sense for this component.
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * @ignore
   */
  id: _propTypes.default.string,
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
   * If `true`, the component has sticky position (with top = 0).
   * @default false
   */
  sticky: _propTypes.default.bool,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = ListSubheader;