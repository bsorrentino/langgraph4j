"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _useButton = require("@mui/base/useButton");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _variantColorInheritance = require("../styles/variantColorInheritance");
var _Cancel2 = _interopRequireDefault(require("../internal/svg-icons/Cancel"));
var _chipDeleteClasses = require("./chipDeleteClasses");
var _ChipContext = _interopRequireDefault(require("../Chip/ChipContext"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _IconButton = require("../IconButton/IconButton");
var _jsxRuntime = require("react/jsx-runtime");
var _Cancel;
const _excluded = ["children", "variant", "color", "disabled", "onKeyDown", "onDelete", "onClick", "component", "slots", "slotProps"],
  _excluded2 = ["onDelete"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    focusVisible,
    variant,
    color,
    disabled
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', focusVisible && 'focusVisible', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`]
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _chipDeleteClasses.getChipDeleteUtilityClass, {});
};
const ChipDeleteRoot = (0, _styled.default)(_IconButton.StyledIconButton, {
  name: 'JoyChipDelete',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme
}) => ({
  '--IconButton-size': 'var(--Chip-deleteSize, 2rem)',
  '--Icon-fontSize': 'calc(var(--IconButton-size, 2rem) / 1.3)',
  minWidth: 'var(--IconButton-size, 2rem)',
  // use min-width instead of height to make the button resilient to its content
  minHeight: 'var(--IconButton-size, 2rem)',
  // use min-height instead of height to make the button resilient to its content
  fontSize: theme.vars.fontSize.sm,
  paddingInline: '2px',
  // add a gap, in case the content is long, for example multiple icons
  pointerEvents: 'visible',
  // force the ChipDelete to be hoverable because the decorator can have pointerEvents 'none'
  borderRadius: 'var(--Chip-deleteRadius, 50%)',
  zIndex: 1,
  // overflow above sibling button or anchor
  padding: 0 // reset user agent stylesheet
}));

/**
 *
 * Demos:
 *
 * - [Chip](https://mui.com/joy-ui/react-chip/)
 *
 * API:
 *
 * - [ChipDelete API](https://mui.com/joy-ui/api/chip-delete/)
 */
const ChipDelete = /*#__PURE__*/React.forwardRef(function ChipDelete(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyChipDelete'
  });
  const {
      children,
      variant: variantProp = 'plain',
      color: colorProp = 'neutral',
      disabled: disabledProp,
      onKeyDown,
      onDelete,
      onClick,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const chipContext = React.useContext(_ChipContext.default);
  const {
    variant = variantProp,
    color: inheritedColor = colorProp
  } = (0, _variantColorInheritance.useVariantColor)(inProps.variant, inProps.color, true);
  const color = inProps.color || inheritedColor;
  const disabled = disabledProp != null ? disabledProp : chipContext.disabled;
  const buttonRef = React.useRef(null);
  const handleRef = (0, _utils.unstable_useForkRef)(buttonRef, ref);
  const {
    focusVisible,
    getRootProps
  } = (0, _useButton.useButton)((0, _extends2.default)({}, props, {
    disabled,
    rootRef: handleRef
  }));
  const ownerState = (0, _extends2.default)({}, props, {
    disabled,
    variant,
    color,
    focusVisible
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const handleClickDelete = event => {
    if (!disabled && onDelete) {
      onDelete(event);
    }
    if (onClick) {
      onClick(event);
    }
  };
  const handleKeyDelete = event => {
    if (['Backspace', 'Enter', 'Delete'].includes(event.key)) {
      event.preventDefault();
      if (!disabled && onDelete) {
        onDelete(event);
      }
    }
    if (onKeyDown) {
      onKeyDown(event);
    }
  };
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    elementType: ChipDeleteRoot,
    getSlotProps: getRootProps,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      as: component,
      onKeyDown: handleKeyDelete,
      onClick: handleClickDelete
    },
    className: classes.root
  });
  const restOfRootProps = (0, _objectWithoutPropertiesLoose2.default)(rootProps, _excluded2);
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, restOfRootProps, {
    children: children != null ? children : _Cancel || (_Cancel = /*#__PURE__*/(0, _jsxRuntime.jsx)(_Cancel2.default, {}))
  }));
});
process.env.NODE_ENV !== "production" ? ChipDelete.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * If provided, it will replace the default icon.
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
   * If `true`, the component is disabled.
   * If `undefined`, the value inherits from the parent chip via a React context.
   */
  disabled: _propTypes.default.bool,
  /**
   * @ignore
   */
  onClick: _propTypes.default.func,
  /**
   * Callback fired when the component is not disabled and either:
   * - `Backspace`, `Enter` or `Delete` is pressed.
   * - The component is clicked.
   */
  onDelete: _propTypes.default.func,
  /**
   * @ignore
   */
  onKeyDown: _propTypes.default.func,
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
var _default = exports.default = ChipDelete;