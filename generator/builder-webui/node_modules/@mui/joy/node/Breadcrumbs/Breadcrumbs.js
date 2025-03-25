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
var _clsx = _interopRequireDefault(require("clsx"));
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _breadcrumbsClasses = require("./breadcrumbsClasses");
var _Typography = require("../Typography/Typography");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "className", "size", "separator", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    size
  } = ownerState;
  const slots = {
    root: ['root', size && `size${(0, _utils.unstable_capitalize)(size)}`],
    li: ['li'],
    ol: ['ol'],
    separator: ['separator']
  };
  return (0, _base.unstable_composeClasses)(slots, _breadcrumbsClasses.getBreadcrumbsUtilityClass, {});
};
const BreadcrumbsRoot = (0, _styled.default)('nav', {
  name: 'JoyBreadcrumbs',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({}, ownerState.size === 'sm' && {
  '--Icon-fontSize': theme.vars.fontSize.lg,
  gap: 'var(--Breadcrumbs-gap, 0.25rem)',
  padding: '0.5rem'
}, ownerState.size === 'md' && {
  '--Icon-fontSize': theme.vars.fontSize.xl,
  gap: 'var(--Breadcrumbs-gap, 0.375rem)',
  padding: '0.75rem'
}, ownerState.size === 'lg' && {
  '--Icon-fontSize': theme.vars.fontSize.xl2,
  gap: 'var(--Breadcrumbs-gap, 0.5rem)',
  padding: '1rem'
}, theme.typography[`body-${ownerState.size}`]));
const BreadcrumbsOl = (0, _styled.default)('ol', {
  name: 'JoyBreadcrumbs',
  slot: 'Ol',
  overridesResolver: (props, styles) => styles.ol
})({
  display: 'flex',
  flexWrap: 'wrap',
  alignItems: 'center',
  gap: 'inherit',
  // reset user-agent style
  padding: 0,
  margin: 0,
  listStyle: 'none'
});
const BreadcrumbsLi = (0, _styled.default)('li', {
  name: 'JoyBreadcrumbs',
  slot: 'Li',
  overridesResolver: (props, styles) => styles.li
})({
  display: 'flex',
  alignItems: 'center'
});
const BreadcrumbsSeparator = (0, _styled.default)('li', {
  name: 'JoyBreadcrumbs',
  slot: 'Separator',
  overridesResolver: (props, styles) => styles.separator
})({
  display: 'flex',
  userSelect: 'none'
});
/**
 *
 * Demos:
 *
 * - [Breadcrumbs](https://mui.com/joy-ui/react-breadcrumbs/)
 *
 * API:
 *
 * - [Breadcrumbs API](https://mui.com/joy-ui/api/breadcrumbs/)
 */
const Breadcrumbs = /*#__PURE__*/React.forwardRef(function Breadcrumbs(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyBreadcrumbs'
  });
  const {
      children,
      className,
      size = 'md',
      separator = '/',
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    separator,
    size
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
    elementType: BreadcrumbsRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotOl, olProps] = (0, _useSlot.default)('ol', {
    className: classes.ol,
    elementType: BreadcrumbsOl,
    externalForwardedProps,
    ownerState
  });
  const [SlotLi, liProps] = (0, _useSlot.default)('li', {
    className: classes.li,
    elementType: BreadcrumbsLi,
    externalForwardedProps,
    ownerState
  });
  const [SlotSeparator, separatorProps] = (0, _useSlot.default)('separator', {
    additionalProps: {
      'aria-hidden': true
    },
    className: classes.separator,
    elementType: BreadcrumbsSeparator,
    externalForwardedProps,
    ownerState
  });
  const allItems = React.Children.toArray(children).filter(child => {
    return /*#__PURE__*/React.isValidElement(child);
  }).map((child, index) => {
    var _child$props$componen;
    return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotLi, (0, _extends2.default)({}, liProps, {
      children: (0, _utils.unstable_isMuiElement)(child, ['Typography']) ? /*#__PURE__*/React.cloneElement(child, {
        component: (_child$props$componen = child.props.component) != null ? _child$props$componen : 'span'
      }) : child
    }), `child-${index}`);
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_Typography.TypographyInheritContext.Provider, {
    value: true,
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotOl, (0, _extends2.default)({}, olProps, {
        children: allItems.reduce((acc, current, index) => {
          if (index < allItems.length - 1) {
            acc = acc.concat(current, /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotSeparator, (0, _extends2.default)({}, separatorProps, {
              children: separator
            }), `separator-${index}`));
          } else {
            acc.push(current);
          }
          return acc;
        }, [])
      }))
    }))
  });
});
process.env.NODE_ENV !== "production" ? Breadcrumbs.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The content of the component.
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
   * Custom separator node.
   * @default '/'
   */
  separator: _propTypes.default.node,
  /**
   * The size of the component.
   * It accepts theme values between 'sm' and 'lg'.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['sm', 'md', 'lg']), _propTypes.default.string]),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    li: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    ol: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    separator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    li: _propTypes.default.elementType,
    ol: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    separator: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object])
} : void 0;
var _default = exports.default = Breadcrumbs;