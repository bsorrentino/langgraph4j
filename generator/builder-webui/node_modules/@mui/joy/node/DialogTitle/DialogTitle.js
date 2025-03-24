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
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _dialogTitleClasses = require("./dialogTitleClasses");
var _cardOverflowClasses = _interopRequireDefault(require("../CardOverflow/cardOverflowClasses"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _ModalDialogVariantColorContext = _interopRequireDefault(require("../ModalDialog/ModalDialogVariantColorContext"));
var _ModalDialogSizeContext = _interopRequireDefault(require("../ModalDialog/ModalDialogSizeContext"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "children", "variant", "color", "level", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    level,
    color,
    variant
  } = ownerState;
  const slots = {
    root: ['root', level, color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _dialogTitleClasses.getDialogTitleUtilityClass, {});
};
const DialogTitleRoot = (0, _styled.default)('h2', {
  name: 'JoyDialogTitle',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$typography, _theme$variants$owner;
  const lineHeight = ownerState.level !== 'inherit' ? (_theme$typography = theme.typography[ownerState.level]) == null ? void 0 : _theme$typography.lineHeight : '1';
  return (0, _extends2.default)({
    '--Icon-fontSize': `calc(1em * ${lineHeight})`
  }, ownerState.color && {
    '--Icon-color': 'currentColor'
  }, {
    display: 'flex',
    gap: 'clamp(4px, 0.375em, 0.75rem)',
    margin: 'var(--unstable_DialogTitle-margin, 0px)'
  }, ownerState.level && ownerState.level !== 'inherit' && theme.typography[ownerState.level], {
    color: 'inherit'
  }, ownerState.variant && (0, _extends2.default)({
    borderRadius: theme.vars.radius.xs,
    paddingBlock: 'min(0.1em, 4px)',
    paddingInline: '0.25em'
  }, (_theme$variants$owner = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants$owner[ownerState.color]), {
    [`.${_cardOverflowClasses.default.root} > &`]: {
      '--unstable_DialogTitle-margin': 'var(--Card-padding) 0'
    }
  });
});
const sizeToLevel = {
  sm: 'title-md',
  md: 'title-lg',
  lg: 'h4'
};

/**
 *
 * Demos:
 *
 * - [Modal](https://mui.com/joy-ui/react-modal/)
 *
 * API:
 *
 * - [DialogTitle API](https://mui.com/joy-ui/api/dialog-title/)
 */
const DialogTitle = /*#__PURE__*/React.forwardRef(function DialogTitle(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyDialogTitle'
  });
  const size = React.useContext(_ModalDialogSizeContext.default);
  const context = React.useContext(_ModalDialogVariantColorContext.default);
  const {
      component = 'h2',
      children,
      variant,
      color: colorProp,
      level = sizeToLevel[size || 'md'],
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const color = inProps.color || (variant ? colorProp != null ? colorProp : 'neutral' : colorProp);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const ownerState = (0, _extends2.default)({}, props, {
    component,
    color,
    variant,
    level
  });
  const classes = useUtilityClasses(ownerState);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: DialogTitleRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      id: context == null ? void 0 : context.labelledBy
    }
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? DialogTitle.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the DialogTitle if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   */
  color: _propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * Applies the theme typography styles.
   * @default { sm: 'title-md', md: 'title-lg', lg: 'h4' }
   */
  level: _propTypes.default.oneOf(['body-lg', 'body-md', 'body-sm', 'body-xs', 'h1', 'h2', 'h3', 'h4', 'inherit', 'title-lg', 'title-md', 'title-sm']),
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
   */
  variant: _propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid'])
} : void 0;
var _default = exports.default = DialogTitle;