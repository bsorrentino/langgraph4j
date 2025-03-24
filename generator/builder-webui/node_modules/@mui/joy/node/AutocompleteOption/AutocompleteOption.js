"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledAutocompleteOption = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _ListItemButton = require("../ListItemButton/ListItemButton");
var _styles = require("../styles");
var _variantColorInheritance = require("../styles/variantColorInheritance");
var _autocompleteOptionClasses = require("./autocompleteOptionClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "component", "color", "variant", "className", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    color,
    variant
  } = ownerState;
  const slots = {
    root: ['root', color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`]
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _autocompleteOptionClasses.getAutocompleteOptionUtilityClass, {});
};
const StyledAutocompleteOption = exports.StyledAutocompleteOption = (0, _styles.styled)(_ListItemButton.StyledListItemButton)(({
  theme,
  ownerState
}) => {
  var _theme$variants, _theme$variants2;
  return {
    '&[aria-disabled="true"]': (_theme$variants = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants[ownerState.color],
    '&[aria-selected="true"]': (0, _extends2.default)({}, (_theme$variants2 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants2[ownerState.color], {
      fontWeight: theme.vars.fontWeight.md
    })
  };
});
const AutocompleteOptionRoot = (0, _styles.styled)(StyledAutocompleteOption, {
  name: 'JoyAutocompleteOption',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({});
/**
 *
 * Demos:
 *
 * - [Autocomplete](https://mui.com/joy-ui/react-autocomplete/)
 *
 * API:
 *
 * - [AutocompleteOption API](https://mui.com/joy-ui/api/autocomplete-option/)
 */
const AutocompleteOption = /*#__PURE__*/React.forwardRef(function AutocompleteOption(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAutocompleteOption'
  });
  const {
      children,
      component = 'li',
      color: colorProp = 'neutral',
      variant: variantProp = 'plain',
      className,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const {
    variant = variantProp,
    color = colorProp
  } = (0, _variantColorInheritance.useVariantColor)(inProps.variant, inProps.color);
  const ownerState = (0, _extends2.default)({}, props, {
    component,
    color,
    variant
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
    elementType: AutocompleteOptionRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      as: component,
      role: 'option'
    }
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? AutocompleteOption.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * @ignore
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
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['contained', 'light', 'outlined', 'text']), _propTypes.default.string])
} : void 0;
var _default = exports.default = AutocompleteOption;