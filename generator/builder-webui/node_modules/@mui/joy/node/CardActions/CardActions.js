"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledCardActionsRoot = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _base = require("@mui/base");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _cardActionsClasses = require("./cardActionsClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _buttonClasses = _interopRequireDefault(require("../Button/buttonClasses"));
var _iconButtonClasses = _interopRequireDefault(require("../IconButton/iconButtonClasses"));
var _cardClasses = _interopRequireDefault(require("../Card/cardClasses"));
var _cardOverflowClasses = _interopRequireDefault(require("../CardOverflow/cardOverflowClasses"));
var _dividerClasses = _interopRequireDefault(require("../Divider/dividerClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "component", "children", "buttonFlex", "orientation", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = () => {
  const slots = {
    root: ['root']
  };
  return (0, _base.unstable_composeClasses)(slots, _cardActionsClasses.getCardActionsUtilityClass, {});
};
const StyledCardActionsRoot = exports.StyledCardActionsRoot = (0, _styled.default)('div')(({
  ownerState
}) => {
  var _ownerState$orientati;
  return (0, _extends2.default)({
    '--Button-radius': 'var(--Card-childRadius)',
    '--IconButton-radius': 'var(--Card-childRadius)',
    display: 'flex'
  }, ((_ownerState$orientati = ownerState.orientation) == null ? void 0 : _ownerState$orientati.startsWith('horizontal')) && {
    alignItems: 'center' // it is common to have children aligned center in horizontal orientation, but not vertically.
  }, {
    flexDirection: ownerState.orientation === 'horizontal' ? 'row' : 'column'
  }, ownerState.orientation === 'horizontal-reverse' && {
    flexDirection: 'row-reverse'
  }, {
    zIndex: 1,
    // render above Link's overlay
    gap: 'calc(0.625 * var(--Card-padding))',
    padding: 'var(--unstable_padding)',
    '--unstable_padding': 'calc(0.75 * var(--Card-padding)) 0 0 0',
    [`.${_cardOverflowClasses.default.root} > &`]: {
      '--unstable_padding': 'calc(0.75 * var(--Card-padding)) 0 var(--Card-padding)'
    },
    [`.${_cardClasses.default.root} > .${_dividerClasses.default.root} + &`]: {
      '--unstable_padding': '0'
    }
  }, ownerState.buttonFlex ? {
    [`& > :not(.${_iconButtonClasses.default.root})`]: {
      flex: ownerState.buttonFlex
    },
    [`& > :not(button) > .${_buttonClasses.default.root}`]: {
      width: '100%' // for button to fill its wrapper.
    }
  } : {
    [`& > .${_buttonClasses.default.root}:only-child`]: {
      flex: 'auto'
    }
  });
});
const CardActionsRoot = (0, _styled.default)(StyledCardActionsRoot, {
  name: 'JoyCardActions',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({});
/**
 *
 * Demos:
 *
 * - [Card](https://mui.com/joy-ui/react-card/)
 *
 * API:
 *
 * - [CardActions API](https://mui.com/joy-ui/api/card-actions/)
 */
const CardActions = /*#__PURE__*/React.forwardRef(function CardActions(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyCardActions'
  });
  const {
      className,
      component = 'div',
      children,
      buttonFlex,
      orientation = 'horizontal',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const ownerState = (0, _extends2.default)({}, props, {
    component,
    buttonFlex,
    orientation
  });
  const classes = useUtilityClasses();
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: CardActionsRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? CardActions.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The CSS `flex` for the Button and its wrapper.
   */
  buttonFlex: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string]),
  /**
   * Used to render icon or text elements inside the CardActions if `src` is not set.
   * This can be an element, or just a string.
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
   * The component orientation.
   * @default 'horizontal'
   */
  orientation: _propTypes.default.oneOf(['horizontal-reverse', 'horizontal', 'vertical']),
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
var _default = exports.default = CardActions;