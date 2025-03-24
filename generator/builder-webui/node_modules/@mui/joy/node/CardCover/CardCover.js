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
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _base = require("@mui/base");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _cardCoverClasses = require("./cardCoverClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "component", "children", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = () => {
  const slots = {
    root: ['root']
  };
  return (0, _base.unstable_composeClasses)(slots, _cardCoverClasses.getCardCoverUtilityClass, {});
};
const CardCoverRoot = (0, _styled.default)('div', {
  name: 'JoyCardCover',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({
  position: 'absolute',
  zIndex: 0,
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  borderRadius: 'var(--CardCover-radius)',
  // use data-attribute instead of :first-child to support zero config SSR (emotion)
  // use nested selector for integrating with nextjs image `fill` layout (spans are inserted on top of the img)
  '& [data-first-child]': {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%',
    objectFit: 'cover',
    boxSizing: 'border-box',
    borderRadius: 'var(--CardCover-radius)',
    margin: 0,
    padding: 0,
    '& > img': {
      // support art-direction that uses <picture><img /></picture>
      width: '100%',
      height: '100%',
      objectFit: 'cover'
    }
  }
});
/**
 *
 * Demos:
 *
 * - [Card](https://mui.com/joy-ui/react-card/)
 *
 * API:
 *
 * - [CardCover API](https://mui.com/joy-ui/api/card-cover/)
 */
const CardCover = /*#__PURE__*/React.forwardRef(function CardCover(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyCardCover'
  });
  const {
      className,
      component = 'div',
      children,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    component
  });
  const classes = useUtilityClasses();
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: CardCoverRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: React.Children.map(children, (child, index) => index === 0 && /*#__PURE__*/React.isValidElement(child) ? /*#__PURE__*/React.cloneElement(child, {
      'data-first-child': ''
    }) : child)
  }));
});
process.env.NODE_ENV !== "production" ? CardCover.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the CardCover if `src` is not set.
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
var _default = exports.default = CardCover;