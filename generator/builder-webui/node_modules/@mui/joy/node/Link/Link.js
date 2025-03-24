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
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _system = require("@mui/system");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps2 = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _linkClasses = _interopRequireWildcard(require("./linkClasses"));
var _Typography = require("../Typography/Typography");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["color", "textColor", "variant"],
  _excluded2 = ["children", "disabled", "onBlur", "onFocus", "level", "overlay", "underline", "endDecorator", "startDecorator", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    level,
    color,
    variant,
    underline,
    focusVisible,
    disabled
  } = ownerState;
  const slots = {
    root: ['root', color && `color${(0, _utils.unstable_capitalize)(color)}`, disabled && 'disabled', focusVisible && 'focusVisible', level, underline && `underline${(0, _utils.unstable_capitalize)(underline)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator']
  };
  return (0, _base.unstable_composeClasses)(slots, _linkClasses.getLinkUtilityClass, {});
};
const StartDecorator = (0, _styled.default)('span', {
  name: 'JoyLink',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})(({
  ownerState
}) => {
  var _ownerState$sx;
  return (0, _extends2.default)({
    display: 'inline-flex',
    marginInlineEnd: 'clamp(4px, var(--Link-gap, 0.375em), 0.75rem)'
  }, typeof ownerState.startDecorator !== 'string' && (ownerState.alignItems === 'flex-start' || ((_ownerState$sx = ownerState.sx) == null ? void 0 : _ownerState$sx.alignItems) === 'flex-start') && {
    marginTop: '2px' // this makes the alignment perfect in most cases
  });
});
const EndDecorator = (0, _styled.default)('span', {
  name: 'JoyLink',
  slot: 'endDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})(({
  ownerState
}) => {
  var _ownerState$sx2;
  return (0, _extends2.default)({
    display: 'inline-flex',
    marginInlineStart: 'clamp(4px, var(--Link-gap, 0.25em), 0.5rem)'
  }, typeof ownerState.startDecorator !== 'string' && (ownerState.alignItems === 'flex-start' || ((_ownerState$sx2 = ownerState.sx) == null ? void 0 : _ownerState$sx2.alignItems) === 'flex-start') && {
    marginTop: '2px' // this makes the alignment perfect in most cases
  });
});
const LinkRoot = (0, _styled.default)('a', {
  name: 'JoyLink',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$vars$palette, _theme$vars$palette2, _theme$vars$palette3, _theme$variants$owner, _theme$variants, _theme$variants2, _theme$variants3;
  return [(0, _extends2.default)({
    '--Icon-fontSize': '1.25em',
    '--Icon-color': 'currentColor',
    '--CircularProgress-size': '1.25em',
    '--CircularProgress-thickness': '3px'
  }, ownerState.level && ownerState.level !== 'inherit' && theme.typography[ownerState.level], ownerState.level === 'inherit' && {
    font: 'inherit'
  }, ownerState.underline === 'none' && {
    textDecoration: 'none'
  }, ownerState.underline === 'hover' && {
    textDecoration: 'none',
    '&:hover': {
      '@media (hover: hover)': {
        textDecorationLine: 'underline'
      }
    }
  }, ownerState.underline === 'always' && {
    textDecoration: 'underline'
  }, ownerState.startDecorator && {
    verticalAlign: 'bottom' // to make the link align with the parent's content
  }, {
    textDecorationThickness: 'max(0.08em, 1px)',
    // steal from https://moderncss.dev/modern-css-for-dynamic-component-based-architecture/#css-reset-additions
    textUnderlineOffset: '0.15em',
    // steal from https://moderncss.dev/modern-css-for-dynamic-component-based-architecture/#css-reset-additions
    display: 'inline-flex',
    alignItems: 'center',
    WebkitTapHighlightColor: 'transparent',
    backgroundColor: 'transparent',
    // Reset default value
    // We disable the focus ring for mouse, touch and keyboard users.
    outline: 0,
    border: 0,
    margin: 0,
    // Remove the margin in Safari
    borderRadius: theme.vars.radius.xs,
    padding: 0,
    // Remove the padding in Firefox
    cursor: 'pointer',
    textDecorationColor: `var(--variant-outlinedBorder, rgba(${(_theme$vars$palette = theme.vars.palette[ownerState.color]) == null ? void 0 : _theme$vars$palette.mainChannel} / var(--Link-underlineOpacity, 0.72)))`
  }, ownerState.variant ? (0, _extends2.default)({
    paddingBlock: 'min(0.1em, 4px)',
    paddingInline: '0.25em'
  }, !ownerState.nesting && {
    marginInline: '-0.25em'
  }) : {
    color: `var(--variant-plainColor, rgba(${(_theme$vars$palette2 = theme.vars.palette[ownerState.color]) == null ? void 0 : _theme$vars$palette2.mainChannel} / 1))`,
    [`&.${_linkClasses.default.disabled}`]: {
      pointerEvents: 'none',
      color: `var(--variant-plainDisabledColor, rgba(${(_theme$vars$palette3 = theme.vars.palette[ownerState.color]) == null ? void 0 : _theme$vars$palette3.mainChannel} / 0.6))`
    }
  }, {
    userSelect: ownerState.component === 'button' ? 'none' : undefined,
    MozAppearance: 'none',
    // Reset
    WebkitAppearance: 'none',
    // Reset
    '&::-moz-focus-inner': {
      borderStyle: 'none' // Remove Firefox dotted outline.
    }
  }, ownerState.overlay ? {
    position: 'initial',
    '&::after': {
      content: '""',
      display: 'block',
      position: 'absolute',
      top: 0,
      left: 0,
      bottom: 0,
      right: 0,
      borderRadius: `var(--unstable_actionRadius, inherit)`,
      margin: `var(--unstable_actionMargin)`
    },
    [`${theme.focus.selector}`]: {
      '&::after': theme.focus.default
    }
  } : {
    position: 'relative',
    [theme.focus.selector]: theme.focus.default
  }), ownerState.variant && (0, _extends2.default)({}, (_theme$variants$owner = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants$owner[ownerState.color], {
    '&:hover': {
      '@media (hover: hover)': (_theme$variants = theme.variants[`${ownerState.variant}Hover`]) == null ? void 0 : _theme$variants[ownerState.color]
    },
    '&:active': (_theme$variants2 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants2[ownerState.color],
    [`&.${_linkClasses.default.disabled}`]: (_theme$variants3 = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants3[ownerState.color]
  })];
});
/**
 *
 * Demos:
 *
 * - [Link](https://mui.com/joy-ui/react-link/)
 *
 * API:
 *
 * - [Link API](https://mui.com/joy-ui/api/link/)
 */
const Link = /*#__PURE__*/React.forwardRef(function Link(inProps, ref) {
  const _useThemeProps = (0, _useThemeProps2.default)({
      props: inProps,
      name: 'JoyLink'
    }),
    {
      color = 'primary',
      textColor,
      variant
    } = _useThemeProps,
    themeProps = (0, _objectWithoutPropertiesLoose2.default)(_useThemeProps, _excluded);
  const nesting = React.useContext(_Typography.TypographyNestedContext);
  const inheriting = React.useContext(_Typography.TypographyInheritContext);
  const props = (0, _system.unstable_extendSxProp)((0, _extends2.default)({}, themeProps, {
    color: textColor
  }));
  const {
      children,
      disabled = false,
      onBlur,
      onFocus,
      level: levelProp = 'body-md',
      overlay = false,
      underline = 'hover',
      endDecorator,
      startDecorator,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded2);
  const level = nesting || inheriting ? inProps.level || 'inherit' : levelProp;
  const {
    isFocusVisibleRef,
    onBlur: handleBlurVisible,
    onFocus: handleFocusVisible,
    ref: focusVisibleRef
  } = (0, _utils.unstable_useIsFocusVisible)();
  const [focusVisible, setFocusVisible] = React.useState(false);
  const handleRef = (0, _utils.unstable_useForkRef)(ref, focusVisibleRef);
  const handleBlur = event => {
    handleBlurVisible(event);
    if (isFocusVisibleRef.current === false) {
      setFocusVisible(false);
    }
    if (onBlur) {
      onBlur(event);
    }
  };
  const handleFocus = event => {
    handleFocusVisible(event);
    if (isFocusVisibleRef.current === true) {
      setFocusVisible(true);
    }
    if (onFocus) {
      onFocus(event);
    }
  };
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    disabled,
    focusVisible,
    underline,
    variant,
    level,
    overlay,
    nesting
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    additionalProps: {
      onBlur: handleBlur,
      onFocus: handleFocus
    },
    ref: handleRef,
    className: classes.root,
    elementType: LinkRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotStartDecorator, startDecoratorProps] = (0, _useSlot.default)('startDecorator', {
    className: classes.startDecorator,
    elementType: StartDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotEndDecorator, endDecoratorProps] = (0, _useSlot.default)('endDecorator', {
    className: classes.endDecorator,
    elementType: EndDecorator,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_Typography.TypographyNestedContext.Provider, {
    value: true,
    children: /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
        children: startDecorator
      })), (0, _utils.unstable_isMuiElement)(children, ['Skeleton']) ? /*#__PURE__*/React.cloneElement(children, {
        variant: children.props.variant || 'inline'
      }) : children, endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
        children: endDecorator
      }))]
    }))
  });
});
process.env.NODE_ENV !== "production" ? Link.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The content of the component.
   */
  children: _propTypes.default.node,
  /**
   * The color of the link.
   * @default 'primary'
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * Element placed after the children.
   */
  endDecorator: _propTypes.default.node,
  /**
   * Applies the theme typography styles.
   * @default 'body-md'
   */
  level: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['body1', 'body2', 'body3', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'inherit']), _propTypes.default.string]),
  /**
   * @ignore
   */
  onBlur: _propTypes.default.func,
  /**
   * @ignore
   */
  onFocus: _propTypes.default.func,
  /**
   * If `true`, the ::after pseudo element is added to cover the area of interaction.
   * The parent of the overlay Link should have `relative` CSS position.
   * @default false
   */
  overlay: _propTypes.default.bool,
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    endDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    startDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    endDecorator: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    startDecorator: _propTypes.default.elementType
  }),
  /**
   * Element placed before the children.
   */
  startDecorator: _propTypes.default.node,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The system color.
   */
  textColor: _propTypes.default /* @typescript-to-proptypes-ignore */.any,
  /**
   * Controls when the link should have an underline.
   * @default 'hover'
   */
  underline: _propTypes.default.oneOf(['always', 'hover', 'none']),
  /**
   * Applies the theme link styles.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Link;