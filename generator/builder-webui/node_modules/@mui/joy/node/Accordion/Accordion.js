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
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _accordionClasses = require("./accordionClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _AccordionContext = _interopRequireDefault(require("./AccordionContext"));
var _ListItem = require("../ListItem/ListItem");
var _accordionDetailsClasses = _interopRequireDefault(require("../AccordionDetails/accordionDetailsClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["accordionId", "component", "color", "children", "defaultExpanded", "disabled", "expanded", "onChange", "variant", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    expanded,
    disabled
  } = ownerState;
  const slots = {
    root: ['root', expanded && 'expanded', disabled && 'disabled', color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _accordionClasses.getAccordionUtilityClass, {});
};
const AccordionRoot = (0, _styled.default)(_ListItem.StyledListItem, {
  name: 'JoyAccordion',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({
  borderBottom: 'var(--Accordion-borderBottom)',
  '&[data-first-child]': {
    '--ListItem-radius': 'var(--unstable_List-childRadius) var(--unstable_List-childRadius) 0 0'
  },
  '&[data-last-child]': {
    '--ListItem-radius': '0 0 var(--unstable_List-childRadius) var(--unstable_List-childRadius)',
    '& [aria-expanded="true"]': {
      '--ListItem-radius': '0'
    },
    [`& .${_accordionDetailsClasses.default.root}`]: {
      '--AccordionDetails-radius': '0 0 var(--unstable_List-childRadius) var(--unstable_List-childRadius)'
    }
  },
  '&:not([data-first-child]):not([data-last-child])': {
    '--ListItem-radius': '0'
  }
});
/**
 *
 * Demos:
 *
 * - [Accordion](https://mui.com/joy-ui/react-accordion/)
 *
 * API:
 *
 * - [Accordion API](https://mui.com/joy-ui/api/accordion/)
 */
const Accordion = /*#__PURE__*/React.forwardRef(function Accordion(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAccordion'
  });
  const {
      accordionId: idOverride,
      component = 'div',
      color = 'neutral',
      children,
      defaultExpanded = false,
      disabled = false,
      expanded: expandedProp,
      onChange,
      variant = 'plain',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const accordionId = (0, _utils.unstable_useId)(idOverride);
  const [expanded, setExpandedState] = (0, _utils.unstable_useControlled)({
    controlled: expandedProp,
    default: defaultExpanded,
    name: 'Accordion',
    state: 'expanded'
  });
  const handleChange = React.useCallback(event => {
    setExpandedState(!expanded);
    if (onChange) {
      onChange(event, !expanded);
    }
  }, [expanded, onChange, setExpandedState]);
  const contextValue = React.useMemo(() => ({
    accordionId,
    expanded,
    disabled,
    toggle: handleChange
  }), [accordionId, expanded, disabled, handleChange]);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const ownerState = (0, _extends2.default)({}, props, {
    component,
    color,
    variant,
    expanded,
    disabled,
    nested: true // for the ListItem styles
  });
  const classes = useUtilityClasses(ownerState);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: AccordionRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_AccordionContext.default.Provider, {
    value: contextValue,
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: React.Children.map(children, (child, index) => /*#__PURE__*/React.isValidElement(child) && index === 0 ? /*#__PURE__*/React.cloneElement(child, {
        // @ts-ignore: to let ListItem knows when to apply margin(Inline|Block)Start
        'data-first-child': ''
      }) : child)
    }))
  });
});
process.env.NODE_ENV !== "production" ? Accordion.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The id to be used in the AccordionDetails which is controlled by the AccordionSummary.
   * If not provided, the id is autogenerated.
   */
  accordionId: _propTypes.default.string,
  /**
   * Used to render icon or text elements inside the Accordion if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   * @default 'neutral'
   */
  color: _propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * If `true`, expands the accordion by default.
   * @default false
   */
  defaultExpanded: _propTypes.default.bool,
  /**
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * If `true`, expands the accordion, otherwise collapse it.
   * Setting this prop enables control over the accordion.
   */
  expanded: _propTypes.default.bool,
  /**
   * Callback fired when the expand/collapse state is changed.
   *
   * @param {React.SyntheticEvent} event The event source of the callback. **Warning**: This is a generic event not a change event.
   * @param {boolean} expanded The `expanded` state of the accordion.
   */
  onChange: _propTypes.default.func,
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
  variant: _propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid'])
} : void 0;
var _default = exports.default = Accordion;