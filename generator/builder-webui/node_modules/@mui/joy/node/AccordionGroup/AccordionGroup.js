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
var _accordionGroupClasses = require("./accordionGroupClasses");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _ListProvider = _interopRequireDefault(require("../List/ListProvider"));
var _List = require("../List/List");
var _accordionDetailsClasses = _interopRequireDefault(require("../AccordionDetails/accordionDetailsClasses"));
var _accordionClasses = _interopRequireDefault(require("../Accordion/accordionClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "color", "children", "disableDivider", "variant", "transition", "size", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    size
  } = ownerState;
  const slots = {
    root: ['root', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _accordionGroupClasses.getAccordionGroupUtilityClass, {});
};
const AccordionGroupRoot = (0, _styled.default)(_List.StyledList, {
  name: 'JoyAccordionGroup',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  let transition = {};
  if (ownerState.transition) {
    if (typeof ownerState.transition === 'string') {
      transition = {
        '--AccordionDetails-transition': `grid-template-rows ${ownerState.transition}, padding-block ${ownerState.transition}`
      };
    }
    if (typeof ownerState.transition === 'object') {
      transition = {
        '--AccordionDetails-transition': `grid-template-rows ${ownerState.transition.initial}, padding-block ${ownerState.transition.initial}`,
        [`& .${_accordionDetailsClasses.default.root}.${_accordionDetailsClasses.default.expanded}`]: {
          '--AccordionDetails-transition': `grid-template-rows ${ownerState.transition.expanded}, padding-block ${ownerState.transition.expanded}`
        }
      };
    }
  }
  return (0, _extends2.default)({
    '--List-padding': '0px',
    '--ListDivider-gap': '0px'
  }, transition, !ownerState.disableDivider && {
    [`& .${_accordionClasses.default.root}:not([data-last-child])`]: {
      '--Accordion-borderBottom': `1px solid ${theme.vars.palette.divider}`
    }
  });
});

/**
 *
 * Demos:
 *
 * - [Accordion](https://mui.com/joy-ui/react-accordion/)
 *
 * API:
 *
 * - [AccordionGroup API](https://mui.com/joy-ui/api/accordion-group/)
 */
const AccordionGroup = /*#__PURE__*/React.forwardRef(function AccordionGroup(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAccordionGroup'
  });
  const {
      component = 'div',
      color = 'neutral',
      children,
      disableDivider = false,
      variant = 'plain',
      transition = '0.2s ease',
      size = 'md',
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
    color,
    disableDivider,
    variant,
    transition,
    size
  });
  const classes = useUtilityClasses(ownerState);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: AccordionGroupRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ListProvider.default, {
      children: children
    })
  }));
});
process.env.NODE_ENV !== "production" ? AccordionGroup.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used to render icon or text elements inside the AccordionGroup if `src` is not set.
   * This can be an element, or just a string.
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
   * If `true`, the divider between accordions will be hidden.
   * @default false
   */
  disableDivider: _propTypes.default.bool,
  /**
   * The size of the component (affect other nested list* components).
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
   * The CSS transition for the Accordion details.
   * @default '0.2s ease'
   */
  transition: _propTypes.default.oneOfType([_propTypes.default.shape({
    expanded: _propTypes.default.string.isRequired,
    initial: _propTypes.default.string.isRequired
  }), _propTypes.default.string]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = AccordionGroup;