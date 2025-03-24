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
var _stepClasses = _interopRequireWildcard(require("./stepClasses"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _stepperClasses = _interopRequireDefault(require("../Stepper/stepperClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["active", "completed", "className", "component", "children", "disabled", "orientation", "indicator", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    orientation,
    active,
    completed,
    disabled
  } = ownerState;
  const slots = {
    root: ['root', orientation, active && 'active', completed && 'completed', disabled && 'disabled'],
    indicator: ['indicator']
  };
  return (0, _base.unstable_composeClasses)(slots, _stepClasses.getStepUtilityClass, {});
};

/**
 * CSS architecture:
 * - The root is a flex container with direction based on the provided orientation (horizontal by default).
 * - The indicator slot is used to render the icon or text provided in the `indicator` prop.
 *    - It allows the connector to be shown in the middle of the indicator (because the indicator prop is dynamic and it can be different sizes between step).
 *    - If there is no indicator prop, the indicator will disappear for horizontal Stepper but display a dot for vertical Stepper.
 * - The connector is a pseudo-element that is absolutely positioned relative to the step's width.
 * - Developers can control the CSS variables from the Stepper component or from a specific Step.
 */
const StepRoot = (0, _styled.default)('li', {
  name: 'JoyStep',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme
}) => {
  return {
    position: 'relative',
    display: 'flex',
    gridTemplateColumns: 'var(--Stepper-indicatorColumn) 1fr',
    // for vertical stepper. has no effect on horizontal stepper.
    gridAutoFlow: 'dense',
    flex: 'var(--_Step-flex)',
    flexDirection: 'row',
    alignItems: 'var(--_Step-alignItems, center)',
    justifyContent: 'var(--_Step-justify, center)',
    gap: `var(--Step-gap)`,
    '& > *': {
      zIndex: 1,
      [`&:not(.${_stepClasses.default.indicator})`]: {
        gridColumn: '2'
      }
    },
    '&::after': {
      content: '""',
      display: 'block',
      borderRadius: 'var(--Step-connectorRadius)',
      height: `var(--Step-connectorThickness)`,
      background: `var(--Step-connectorBg, ${theme.vars.palette.divider})`,
      flex: 1,
      marginInlineStart: `calc(var(--Step-connectorInset) - var(--Step-gap))`,
      marginInlineEnd: `var(--Step-connectorInset)`,
      zIndex: 0
    },
    '&[data-last-child]::after': {
      display: 'none'
    },
    [`.${_stepperClasses.default.horizontal} &:not([data-last-child])`]: {
      '--_Step-flex': 'auto',
      // requires to be `auto` to make equally connectors.
      [`&.${_stepClasses.default.vertical}`]: {
        '--_Step-flex': 1 // requires to be `1` to make equally connectors.
      }
    },
    [`.${_stepperClasses.default.vertical} &`]: {
      display: 'grid',
      '--_Step-justify': 'flex-start',
      '&::after': {
        gridColumn: '1',
        width: `var(--Step-connectorThickness)`,
        height: 'auto',
        margin: `calc(var(--Step-connectorInset) - var(--Step-gap)) auto calc(var(--Step-connectorInset) - var(--Stepper-verticalGap))`,
        alignSelf: 'stretch'
      }
    },
    variants: [{
      props: {
        orientation: 'vertical'
      },
      style: {
        flexDirection: 'column',
        [`.${_stepperClasses.default.horizontal} &`]: {
          '&[data-last-child]': {
            // for horizontal stepper, all vertical steps must have flex `1` to stretch equally.
            '--_Step-flex': 1
          },
          '&[data-indicator]': {
            '--_Step-justify': 'flex-start'
          },
          '&::after': {
            margin: 0,
            position: 'absolute',
            height: `var(--Step-connectorThickness)`,
            zIndex: 0,
            top: `calc(var(--StepIndicator-size) / 2 - var(--Step-connectorThickness) / 2)`,
            left: `calc(50% + var(--StepIndicator-size) / 2 + var(--Step-connectorInset))`,
            width: `calc(100% - var(--StepIndicator-size) - 2 * var(--Step-connectorInset))`
          },
          // Eventhough `:has` is <90% support, we can use it because this is an edge case for vertical step without an indicator.
          [`&:has(.${_stepClasses.default.indicator}:empty)::after`]: {
            '--StepIndicator-size': '0px',
            '--Step-connectorInset': '0px',
            top: `calc(50% - var(--Step-connectorThickness) / 2)`
          }
        }
      }
    }]
  };
});
const StepIndicator = (0, _styled.default)('div', {
  name: 'JoyStep',
  slot: 'Indicator',
  overridesResolver: (props, styles) => styles.root
})({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  placeSelf: 'center',
  // for vertical stepper
  width: `var(--StepIndicator-size)`,
  height: `var(--StepIndicator-size)`,
  [`.${_stepperClasses.default.horizontal} &:empty`]: {
    display: 'none'
  },
  [`.${_stepperClasses.default.vertical} &:empty`]: {
    height: 'auto',
    '&::before': {
      content: '""',
      display: 'block',
      width: 'var(--Step-indicatorDotSize)',
      height: 'var(--Step-indicatorDotSize)',
      borderRadius: 'var(--Step-indicatorDotSize)',
      color: 'inherit',
      background: 'currentColor'
    }
  }
});

/**
 *
 * Demos:
 *
 * - [Stepper](https://mui.com/joy-ui/react-stepper/)
 *
 * API:
 *
 * - [Step API](https://mui.com/joy-ui/api/step/)
 */
const Step = /*#__PURE__*/React.forwardRef(function Step(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyStep'
  });
  const {
      active = false,
      completed = false,
      className,
      component = 'li',
      children,
      disabled = false,
      orientation = 'horizontal',
      indicator,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    active,
    completed,
    component,
    disabled,
    orientation
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
    elementType: StepRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      'data-indicator': indicator ? '' : undefined
    }
  });
  const [SlotIndicator, indicatorProps] = (0, _useSlot.default)('indicator', {
    ref,
    className: classes.indicator,
    elementType: StepIndicator,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(SlotIndicator, (0, _extends2.default)({}, indicatorProps, {
      children: indicator
    })), children]
  }));
});
process.env.NODE_ENV !== "production" ? Step.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * If `true`, the active className is appended.
   * You can customize the active state from the Stepper's `sx` prop.
   * @default false
   */
  active: _propTypes.default.bool,
  /**
   * Used to render icon or text elements inside the Step if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
  /**
   * If `true`, the completed className is appended.
   * You can customize the active state from the Stepper's `sx` prop.
   * @default false
   */
  completed: _propTypes.default.bool,
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * If `true`, the active className is appended.
   * You can customize the active state from the Stepper's `sx` prop.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * The indicator to display. If provided, a wrapper element will be used.
   */
  indicator: _propTypes.default.node,
  /**
   * The component orientation.
   * @default 'horizontal'
   */
  orientation: _propTypes.default.oneOf(['horizontal', 'vertical']),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    indicator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    indicator: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object])
} : void 0;
var _default = exports.default = Step;