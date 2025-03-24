"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var React = _interopRequireWildcard(require("react"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _svgIconClasses = require("./svgIconClasses");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "className", "color", "component", "fontSize", "htmlColor", "inheritViewBox", "titleAccess", "viewBox", "size", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    color,
    size,
    fontSize
  } = ownerState;
  const slots = {
    root: ['root', color && color !== 'inherit' && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`, fontSize && `fontSize${(0, _utils.unstable_capitalize)(fontSize)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _svgIconClasses.getSvgIconUtilityClass, {});
};
const sizeMap = {
  sm: 'xl',
  md: 'xl2',
  lg: 'xl3'
};
const SvgIconRoot = (0, _styled.default)('svg', {
  name: 'JoySvgIcon',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$vars$palette;
  return (0, _extends2.default)({}, ownerState.instanceSize && {
    '--Icon-fontSize': theme.vars.fontSize[sizeMap[ownerState.instanceSize]]
  }, ownerState.instanceFontSize && ownerState.instanceFontSize !== 'inherit' && {
    '--Icon-fontSize': theme.vars.fontSize[ownerState.instanceFontSize]
  }, {
    userSelect: 'none',
    margin: 'var(--Icon-margin)',
    width: '1em',
    height: '1em',
    display: 'inline-block',
    // the <svg> will define the property that has `currentColor`
    // for example heroicons uses fill="none" and stroke="currentColor"
    fill: ownerState.hasSvgAsChild ? undefined : 'currentColor',
    flexShrink: 0,
    fontSize: `var(--Icon-fontSize, ${theme.vars.fontSize[sizeMap[ownerState.size]] || 'unset'})`
  }, ownerState.fontSize && ownerState.fontSize !== 'inherit' && {
    fontSize: `var(--Icon-fontSize, ${theme.fontSize[ownerState.fontSize]})`
  }, !ownerState.htmlColor && (0, _extends2.default)({
    color: `var(--Icon-color, ${theme.vars.palette.text.icon})`
  }, ownerState.color === 'inherit' && {
    color: 'inherit'
  }, ownerState.color !== 'inherit' && theme.vars.palette[ownerState.color] && {
    color: `rgba(${(_theme$vars$palette = theme.vars.palette[ownerState.color]) == null ? void 0 : _theme$vars$palette.mainChannel} / 1)`
  }));
});
/**
 *
 * Demos:
 *
 * - [Avatar](https://mui.com/joy-ui/react-avatar/)
 *
 * API:
 *
 * - [SvgIcon API](https://mui.com/joy-ui/api/svg-icon/)
 */
const SvgIcon = /*#__PURE__*/React.forwardRef(function SvgIcon(inProps, ref) {
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'JoySvgIcon'
  });
  const {
      children,
      className,
      color,
      component = 'svg',
      fontSize,
      htmlColor,
      inheritViewBox = false,
      titleAccess,
      viewBox = '0 0 24 24',
      size = 'md',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const hasSvgAsChild = /*#__PURE__*/React.isValidElement(children) && children.type === 'svg';
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    component,
    size,
    instanceSize: inProps.size,
    fontSize,
    instanceFontSize: inProps.fontSize,
    inheritViewBox,
    viewBox,
    hasSvgAsChild
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
    elementType: SvgIconRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: (0, _extends2.default)({
      color: htmlColor,
      focusable: false
    }, titleAccess && {
      role: 'img'
    }, !titleAccess && {
      'aria-hidden': true
    }, !inheritViewBox && {
      viewBox
    }, hasSvgAsChild && children.props)
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: [hasSvgAsChild ? children.props.children : children, titleAccess ? /*#__PURE__*/(0, _jsxRuntime.jsx)("title", {
      children: titleAccess
    }) : null]
  }));
});
process.env.NODE_ENV !== "production" ? SvgIcon.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Node passed into the SVG element.
   */
  children: _propTypes.default.node,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   * You can use the `htmlColor` prop to apply a color attribute to the SVG element.
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['danger', 'inherit', 'neutral', 'primary', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * The theme's fontSize applied to the icon that will override the `size` prop.
   * Use this prop when you want to use a specific font-size from the theme.
   */
  fontSize: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['inherit', 'lg', 'md', 'sm', 'xl', 'xl2', 'xl3', 'xl4', 'xs']), _propTypes.default.string]),
  /**
   * Applies a color attribute to the SVG element.
   */
  htmlColor: _propTypes.default.string,
  /**
   * If `true`, the root node will inherit the custom `component`'s viewBox and the `viewBox`
   * prop will be ignored.
   * Useful when you want to reference a custom `component` and have `SvgIcon` pass that
   * `component`'s viewBox to the root node.
   * @default false
   */
  inheritViewBox: _propTypes.default.bool,
  /**
   * The shape-rendering attribute. The behavior of the different options is described on the
   * [MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/shape-rendering).
   * If you are having issues with blurry icons you should investigate this prop.
   */
  shapeRendering: _propTypes.default.string,
  /**
   * The size of the component.
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
   * Provides a human-readable title for the element that contains it.
   * https://www.w3.org/TR/SVG-access/#Equivalent
   */
  titleAccess: _propTypes.default.string,
  /**
   * Allows you to redefine what the coordinates without units mean inside an SVG element.
   * For example, if the SVG element is 500 (width) by 200 (height),
   * and you pass viewBox="0 0 50 20",
   * this means that the coordinates inside the SVG will go from the top left corner (0,0)
   * to bottom right (50,20) and each unit will be worth 10px.
   * @default '0 0 24 24'
   */
  viewBox: _propTypes.default.string
} : void 0;
var _default = exports.default = SvgIcon;