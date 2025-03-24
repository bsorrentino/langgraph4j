"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.TypographyNestedContext = exports.TypographyInheritContext = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _system = require("@mui/system");
var _composeClasses = require("@mui/base/composeClasses");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps2 = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _typographyClasses = require("./typographyClasses");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["color", "textColor"],
  _excluded2 = ["component", "gutterBottom", "noWrap", "level", "levelMapping", "children", "endDecorator", "startDecorator", "variant", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
/**
 * @internal
 * For creating nested Typography to have inherit level (unless an explicit `level` prop is provided)
 * and change the HTML tag to `span` (unless an explicit `component` prop is provided).
 */
const TypographyNestedContext = exports.TypographyNestedContext = /*#__PURE__*/React.createContext(false);
if (process.env.NODE_ENV !== 'production') {
  TypographyNestedContext.displayName = 'TypographyNestedContext';
}

/**
 * @internal
 * Typography's level will be inherit within this context unless an explicit `level` prop is provided.
 *
 * This is used in components, for example Table, to inherit the parent's size by default.
 */
const TypographyInheritContext = exports.TypographyInheritContext = /*#__PURE__*/React.createContext(false);
if (process.env.NODE_ENV !== 'production') {
  TypographyInheritContext.displayName = 'TypographyInheritContext';
}
const useUtilityClasses = ownerState => {
  const {
    gutterBottom,
    noWrap,
    level,
    color,
    variant
  } = ownerState;
  const slots = {
    root: ['root', level, gutterBottom && 'gutterBottom', noWrap && 'noWrap', color && `color${(0, _utils.unstable_capitalize)(color)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _typographyClasses.getTypographyUtilityClass, {});
};
const StartDecorator = (0, _styled.default)('span', {
  name: 'JoyTypography',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})({
  display: 'inline-flex',
  marginInlineEnd: 'clamp(4px, var(--Typography-gap, 0.375em), 0.75rem)'
});
const EndDecorator = (0, _styled.default)('span', {
  name: 'JoyTypography',
  slot: 'endDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})({
  display: 'inline-flex',
  marginInlineStart: 'clamp(4px, var(--Typography-gap, 0.375em), 0.75rem)'
});
const TypographyRoot = (0, _styled.default)('span', {
  name: 'JoyTypography',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$typography, _theme$typography$own, _theme$typography$own2, _theme$vars$palette$o, _theme$variants$owner;
  const lineHeight = ownerState.level !== 'inherit' ? (_theme$typography = theme.typography[ownerState.level]) == null ? void 0 : _theme$typography.lineHeight : '1';
  return (0, _extends2.default)({
    '--Icon-fontSize': `calc(1em * ${lineHeight})`
  }, ownerState.color && {
    '--Icon-color': 'currentColor'
  }, {
    margin: 'var(--Typography-margin, 0px)'
  }, ownerState.nesting ? {
    display: 'inline' // looks better than `inline-block` when using with `variant` prop.
  } : (0, _extends2.default)({
    display: 'block'
  }, ownerState.unstable_hasSkeleton && {
    position: 'relative'
  }), (ownerState.startDecorator || ownerState.endDecorator) && (0, _extends2.default)({
    display: 'flex',
    alignItems: 'center'
  }, ownerState.nesting && (0, _extends2.default)({
    display: 'inline-flex'
  }, ownerState.startDecorator && {
    verticalAlign: 'bottom' // to make the text align with the parent's content
  })), ownerState.level && ownerState.level !== 'inherit' && theme.typography[ownerState.level], {
    fontSize: `var(--Typography-fontSize, ${ownerState.level && ownerState.level !== 'inherit' ? (_theme$typography$own = (_theme$typography$own2 = theme.typography[ownerState.level]) == null ? void 0 : _theme$typography$own2.fontSize) != null ? _theme$typography$own : 'inherit' : 'inherit'})`
  }, ownerState.noWrap && {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap'
  }, ownerState.gutterBottom && {
    marginBottom: '0.35em'
  }, ownerState.color && {
    color: `var(--variant-plainColor, rgba(${(_theme$vars$palette$o = theme.vars.palette[ownerState.color]) == null ? void 0 : _theme$vars$palette$o.mainChannel} / 1))`
  }, ownerState.variant && (0, _extends2.default)({
    borderRadius: theme.vars.radius.xs,
    paddingBlock: 'min(0.1em, 4px)',
    paddingInline: '0.25em'
  }, !ownerState.nesting && {
    marginInline: '-0.25em'
  }, (_theme$variants$owner = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants$owner[ownerState.color]));
});
const defaultVariantMapping = {
  h1: 'h1',
  h2: 'h2',
  h3: 'h3',
  h4: 'h4',
  'title-lg': 'p',
  'title-md': 'p',
  'title-sm': 'p',
  'body-lg': 'p',
  'body-md': 'p',
  'body-sm': 'p',
  'body-xs': 'span',
  inherit: 'p'
};
/**
 *
 * Demos:
 *
 * - [Skeleton](https://mui.com/joy-ui/react-skeleton/)
 * - [Typography](https://mui.com/joy-ui/react-typography/)
 *
 * API:
 *
 * - [Typography API](https://mui.com/joy-ui/api/typography/)
 */
const Typography = /*#__PURE__*/React.forwardRef(function Typography(inProps, ref) {
  var _inProps$color;
  const _useThemeProps = (0, _useThemeProps2.default)({
      props: inProps,
      name: 'JoyTypography'
    }),
    {
      color: colorProp,
      textColor
    } = _useThemeProps,
    themeProps = (0, _objectWithoutPropertiesLoose2.default)(_useThemeProps, _excluded);
  const nesting = React.useContext(TypographyNestedContext);
  const inheriting = React.useContext(TypographyInheritContext);
  const props = (0, _system.unstable_extendSxProp)((0, _extends2.default)({}, themeProps, {
    color: textColor
  }));
  const {
      component: componentProp,
      gutterBottom = false,
      noWrap = false,
      level: levelProp = 'body-md',
      levelMapping = defaultVariantMapping,
      children,
      endDecorator,
      startDecorator,
      variant,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded2);
  const color = (_inProps$color = inProps.color) != null ? _inProps$color : variant ? colorProp != null ? colorProp : 'neutral' : colorProp;
  const level = nesting || inheriting ? inProps.level || 'inherit' : levelProp;
  const hasSkeleton = (0, _utils.unstable_isMuiElement)(children, ['Skeleton']);
  const component = componentProp || (nesting ? 'span' : levelMapping[level] || defaultVariantMapping[level] || 'span');
  const ownerState = (0, _extends2.default)({}, props, {
    level,
    component,
    color,
    gutterBottom,
    noWrap,
    nesting,
    variant,
    unstable_hasSkeleton: hasSkeleton
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: TypographyRoot,
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
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(TypographyNestedContext.Provider, {
    value: true,
    children: /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
        children: startDecorator
      })), hasSkeleton ? /*#__PURE__*/React.cloneElement(children, {
        variant: children.props.variant || 'inline'
      }) : children, endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
        children: endDecorator
      }))]
    }))
  });
});
process.env.NODE_ENV !== "production" ? Typography.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The content of the component.
   */
  children: _propTypes.default.node,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * Element placed after the children.
   */
  endDecorator: _propTypes.default.node,
  /**
   * If `true`, the text will have a bottom margin.
   * @default false
   */
  gutterBottom: _propTypes.default.bool,
  /**
   * Applies the theme typography styles.
   * @default 'body-md'
   */
  level: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['h1', 'h2', 'h3', 'h4', 'title-lg', 'title-md', 'title-sm', 'body-lg', 'body-md', 'body-sm', 'body-xs', 'inherit']), _propTypes.default.string]),
  /**
   * The component maps the variant prop to a range of different HTML element types.
   * For instance, body1 to `<h6>`.
   * If you wish to change that mapping, you can provide your own.
   * Alternatively, you can use the `component` prop.
   * @default {
   *   h1: 'h1',
   *   h2: 'h2',
   *   h3: 'h3',
   *   h4: 'h4',
   *   'title-lg': 'p',
   *   'title-md': 'p',
   *   'title-sm': 'p',
   *   'body-lg': 'p',
   *   'body-md': 'p',
   *   'body-sm': 'p',
   *   'body-xs': 'span',
   *   inherit: 'p',
   * }
   */
  levelMapping: _propTypes.default /* @typescript-to-proptypes-ignore */.object,
  /**
   * If `true`, the text will not wrap, but instead will truncate with a text overflow ellipsis.
   *
   * Note that text overflow can only happen with block or inline-block level elements
   * (the element needs to have a width in order to overflow).
   * @default false
   */
  noWrap: _propTypes.default.bool,
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
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;

// @ts-ignore internal logic to let communicate with Breadcrumbs
Typography.muiName = 'Typography';
var _default = exports.default = Typography;