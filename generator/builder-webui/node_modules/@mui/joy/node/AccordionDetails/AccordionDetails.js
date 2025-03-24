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
var _accordionDetailsClasses = _interopRequireWildcard(require("./accordionDetailsClasses"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _AccordionContext = _interopRequireDefault(require("../Accordion/AccordionContext"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "children", "color", "variant", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    expanded
  } = ownerState;
  const slots = {
    root: ['root', expanded && 'expanded'],
    content: ['content', expanded && 'expanded']
  };
  return (0, _base.unstable_composeClasses)(slots, _accordionDetailsClasses.getAccordionDetailsUtilityClass, {});
};
const AccordionDetailsRoot = (0, _styled.default)('div', {
  name: 'JoyAccordionDetails',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => {
  var _theme$variants;
  return (0, _extends2.default)({
    overflow: 'hidden',
    borderRadius: 'var(--AccordionDetails-radius)',
    display: 'grid',
    gridTemplateRows: '1fr',
    marginInline: 'calc(-1 * var(--ListItem-paddingLeft)) calc(-1 * var(--ListItem-paddingRight))',
    transition: 'var(--AccordionDetails-transition)'
  }, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color], {
    [`&:not(.${_accordionDetailsClasses.default.expanded})`]: {
      gridTemplateRows: '0fr'
    }
  });
});

/**
 * The content slot is required because the root slot is a CSS Grid, it needs a child.
 */
const AccordionDetailsContent = (0, _styled.default)('div', {
  name: 'JoyAccordionDetails',
  slot: 'Content',
  overridesResolver: (props, styles) => styles.root
})({
  display: 'flex',
  flexDirection: 'column',
  overflow: 'hidden',
  // required for user-provided transition to work
  // Need to apply padding to content rather than root because the overflow.
  // Otherwise, the focus ring of the children can be cut off.
  paddingInlineStart: 'var(--ListItem-paddingLeft)',
  paddingInlineEnd: 'var(--ListItem-paddingRight)',
  paddingBlockStart: 'calc(var(--ListItem-paddingY) / 2)',
  paddingBlockEnd: 'calc(2.5 * var(--ListItem-paddingY))',
  transition: 'var(--AccordionDetails-transition)',
  [`&:not(.${_accordionDetailsClasses.default.expanded})`]: {
    paddingBlock: 0
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
 * - [AccordionDetails API](https://mui.com/joy-ui/api/accordion-details/)
 */
const AccordionDetails = /*#__PURE__*/React.forwardRef(function AccordionDetails(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAccordionDetails'
  });
  const {
      component = 'div',
      children,
      color = 'neutral',
      variant = 'plain',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const {
    accordionId,
    expanded = false
  } = React.useContext(_AccordionContext.default);
  const rootRef = React.useRef(null);
  const handleRef = (0, _utils.unstable_useForkRef)(rootRef, ref);
  React.useEffect(() => {
    // When accordion is closed, prevent tabbing into the details content.
    if (rootRef.current) {
      const elements = rootRef.current.querySelectorAll('a, button, input, textarea, select, details, [tabindex]:not([tabindex="-1"])');
      elements.forEach(elm => {
        if (expanded) {
          const prevTabIndex = elm.getAttribute('data-prev-tabindex');
          const currentTabIndex = elm.getAttribute('tabindex');
          if (currentTabIndex && prevTabIndex) {
            // restore tabindex
            elm.setAttribute('tabindex', prevTabIndex);
            elm.removeAttribute('data-prev-tabindex');
          }
          if (!prevTabIndex && !currentTabIndex) {
            elm.removeAttribute('tabindex');
          }
        } else {
          elm.setAttribute('data-prev-tabindex', elm.getAttribute('tabindex') || '');
          elm.setAttribute('tabindex', '-1');
        }
      });
    }
  }, [expanded]);
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
    nesting: true // for the List styles
  });
  const classes = useUtilityClasses(ownerState);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref: handleRef,
    className: classes.root,
    elementType: AccordionDetailsRoot,
    externalForwardedProps,
    additionalProps: {
      id: `${accordionId}-details`,
      'aria-labelledby': `${accordionId}-summary`,
      role: 'region',
      hidden: expanded ? undefined : true
    },
    ownerState
  });
  const [SlotContent, contentProps] = (0, _useSlot.default)('content', {
    className: classes.content,
    elementType: AccordionDetailsContent,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotContent, (0, _extends2.default)({}, contentProps, {
      children: children
    }))
  }));
});
process.env.NODE_ENV !== "production" ? AccordionDetails.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the AccordionDetails if `src` is not set.
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
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    content: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    content: _propTypes.default.elementType,
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
var _default = exports.default = AccordionDetails;