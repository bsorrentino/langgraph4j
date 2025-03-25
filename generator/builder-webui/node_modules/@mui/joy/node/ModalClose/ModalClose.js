"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.ModalCloseRoot = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _useButton = require("@mui/base/useButton");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _styles = require("../styles");
var _IconButton = require("../IconButton/IconButton");
var _modalCloseClasses = require("./modalCloseClasses");
var _Close = _interopRequireDefault(require("../internal/svg-icons/Close"));
var _CloseModalContext = _interopRequireDefault(require("../Modal/CloseModalContext"));
var _ModalDialogSizeContext = _interopRequireDefault(require("../ModalDialog/ModalDialogSizeContext"));
var _ModalDialogVariantColorContext = _interopRequireDefault(require("../ModalDialog/ModalDialogVariantColorContext"));
var _jsxRuntime = require("react/jsx-runtime");
var _CloseIcon;
const _excluded = ["component", "color", "variant", "size", "onClick", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    disabled,
    focusVisible,
    size
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', focusVisible && 'focusVisible', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _modalCloseClasses.getModalCloseUtilityClass, {});
};
const ModalCloseRoot = exports.ModalCloseRoot = (0, _styles.styled)(_IconButton.StyledIconButton, {
  name: 'JoyModalClose',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => {
  var _theme$variants;
  return (0, _extends2.default)({}, ownerState.size === 'sm' && {
    '--IconButton-size': '1.75rem'
  }, ownerState.size === 'md' && {
    '--IconButton-size': '2rem'
  }, ownerState.size === 'lg' && {
    '--IconButton-size': '2.25rem'
  }, {
    position: 'absolute',
    zIndex: 1,
    // stay on top of the title in case it is positioned relatively
    top: `var(--ModalClose-inset, 0.625rem)`,
    right: `var(--ModalClose-inset, 0.625rem)`,
    borderRadius: `var(--ModalClose-radius, ${theme.vars.radius.sm})`
  }, !((_theme$variants = theme.variants[ownerState.variant]) != null && (_theme$variants = _theme$variants[ownerState.color]) != null && _theme$variants.backgroundColor) && {
    color: theme.vars.palette.text.secondary
  });
});
const modalDialogVariantMapping = {
  plain: 'plain',
  outlined: 'plain',
  soft: 'soft',
  solid: 'solid'
};
/**
 *
 * Demos:
 *
 * - [Drawer](https://mui.com/joy-ui/react-drawer/)
 * - [Modal](https://mui.com/joy-ui/react-modal/)
 *
 * API:
 *
 * - [ModalClose API](https://mui.com/joy-ui/api/modal-close/)
 */
const ModalClose = /*#__PURE__*/React.forwardRef(function ModalClose(inProps, ref) {
  var _ref, _inProps$variant, _ref2, _inProps$color, _ref3, _inProps$size;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyModalClose'
  });
  const {
      component = 'button',
      color: colorProp = 'neutral',
      variant: variantProp = 'plain',
      size: sizeProp = 'md',
      onClick,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const closeModalContext = React.useContext(_CloseModalContext.default);
  const modalDialogVariantColor = React.useContext(_ModalDialogVariantColorContext.default);
  const variant = (_ref = (_inProps$variant = inProps.variant) != null ? _inProps$variant : modalDialogVariantMapping[modalDialogVariantColor == null ? void 0 : modalDialogVariantColor.variant]) != null ? _ref : variantProp;
  const color = (_ref2 = (_inProps$color = inProps.color) != null ? _inProps$color : modalDialogVariantColor == null ? void 0 : modalDialogVariantColor.color) != null ? _ref2 : colorProp;
  const modalDialogSize = React.useContext(_ModalDialogSizeContext.default);
  const size = (_ref3 = (_inProps$size = inProps.size) != null ? _inProps$size : modalDialogSize) != null ? _ref3 : sizeProp;
  const {
    focusVisible,
    getRootProps
  } = (0, _useButton.useButton)((0, _extends2.default)({}, props, {
    rootRef: ref
  }));
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    component,
    variant,
    size,
    focusVisible
  });
  const classes = useUtilityClasses(ownerState);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    elementType: ModalCloseRoot,
    getSlotProps: getRootProps,
    externalForwardedProps: (0, _extends2.default)({
      onClick: event => {
        closeModalContext == null || closeModalContext(event, 'closeClick');
        onClick == null || onClick(event);
      }
    }, other, {
      component,
      slots,
      slotProps
    }),
    className: classes.root,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: _CloseIcon || (_CloseIcon = /*#__PURE__*/(0, _jsxRuntime.jsx)(_Close.default, {}))
  }));
});
process.env.NODE_ENV !== "production" ? ModalClose.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * @ignore
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
   * @ignore
   */
  onClick: _propTypes.default.func,
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
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = ModalClose;