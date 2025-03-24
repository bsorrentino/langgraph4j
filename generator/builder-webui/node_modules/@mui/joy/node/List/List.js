"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledList = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _styles = require("../styles");
var _styleUtils = require("../styles/styleUtils");
var _listClasses = require("./listClasses");
var _NestedListContext = _interopRequireDefault(require("./NestedListContext"));
var _ComponentListContext = _interopRequireDefault(require("./ComponentListContext"));
var _GroupListContext = _interopRequireDefault(require("./GroupListContext"));
var _ListProvider = _interopRequireDefault(require("./ListProvider"));
var _RadioGroupContext = _interopRequireDefault(require("../RadioGroup/RadioGroupContext"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["component", "className", "children", "size", "orientation", "wrap", "variant", "color", "role", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    variant,
    color,
    size,
    nesting,
    orientation,
    instanceSize
  } = ownerState;
  const slots = {
    root: ['root', orientation, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, !instanceSize && !nesting && size && `size${(0, _utils.unstable_capitalize)(size)}`, instanceSize && `size${(0, _utils.unstable_capitalize)(instanceSize)}`, nesting && 'nesting']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _listClasses.getListUtilityClass, {});
};
const StyledList = exports.StyledList = (0, _styles.styled)('ul')(({
  theme,
  ownerState
}) => {
  var _theme$variants;
  const {
    p,
    padding,
    borderRadius
  } = (0, _styleUtils.resolveSxValue)({
    theme,
    ownerState
  }, ['p', 'padding', 'borderRadius']);
  function applySizeVars(size) {
    if (size === 'sm') {
      return {
        '--ListDivider-gap': '0.25rem',
        '--ListItem-minHeight': '2rem',
        '--ListItem-paddingY': '3px',
        '--ListItem-paddingX': ownerState.marker ? '3px' : '0.5rem',
        '--ListItem-gap': '0.5rem',
        '--ListItemDecorator-size': ownerState.orientation === 'horizontal' ? '1.5rem' : '2rem',
        '--Icon-fontSize': theme.vars.fontSize.lg
      };
    }
    if (size === 'md') {
      return {
        '--ListDivider-gap': '0.375rem',
        '--ListItem-minHeight': '2.25rem',
        '--ListItem-paddingY': '0.25rem',
        '--ListItem-paddingX': ownerState.marker ? '0.25rem' : '0.75rem',
        '--ListItem-gap': '0.625rem',
        '--ListItemDecorator-size': ownerState.orientation === 'horizontal' ? '1.75rem' : '2.5rem',
        '--Icon-fontSize': theme.vars.fontSize.xl
      };
    }
    if (size === 'lg') {
      return {
        '--ListDivider-gap': '0.5rem',
        '--ListItem-minHeight': '2.75rem',
        '--ListItem-paddingY': '0.375rem',
        '--ListItem-paddingX': ownerState.marker ? '0.5rem' : '1rem',
        '--ListItem-gap': '0.75rem',
        '--ListItemDecorator-size': ownerState.orientation === 'horizontal' ? '2.25rem' : '3rem',
        '--Icon-fontSize': theme.vars.fontSize.xl2
      };
    }
    return {};
  }
  return [ownerState.nesting && (0, _extends2.default)({}, applySizeVars(ownerState.instanceSize), {
    '--ListItem-paddingRight': 'var(--ListItem-paddingX)',
    '--ListItem-paddingLeft': 'var(--NestedListItem-paddingLeft)',
    // reset ListItem, ListItemButton negative margin (caused by NestedListItem)
    '--ListItemButton-marginBlock': '0px',
    '--ListItemButton-marginInline': '0px',
    '--ListItem-marginBlock': '0px',
    '--ListItem-marginInline': '0px',
    padding: 0
  }, ownerState.marker && {
    paddingInlineStart: 'calc(3ch - var(--_List-markerDeduct, 0px))' // the width of the marker
  }, {
    marginInlineStart: 'var(--NestedList-marginLeft)',
    marginInlineEnd: 'var(--NestedList-marginRight)',
    marginBlockStart: 'var(--List-gap)',
    marginBlockEnd: 'initial' // reset user agent stylesheet.
  }), !ownerState.nesting && (0, _extends2.default)({}, applySizeVars(ownerState.size), {
    '--List-gap': '0px',
    '--List-nestedInsetStart': '0px',
    '--ListItem-paddingLeft': 'var(--ListItem-paddingX)',
    '--ListItem-paddingRight': 'var(--ListItem-paddingX)'
  }, ownerState.marker && {
    '--_List-markerDeduct': '1ch'
  }, {
    // Automatic radius adjustment kicks in only if '--List-padding' and '--List-radius' are provided.
    '--unstable_List-childRadius': 'calc(max(var(--List-radius) - var(--List-padding), min(var(--List-padding) / 2, var(--List-radius) / 2)) - var(--variant-borderWidth, 0px))',
    '--ListItem-radius': 'var(--unstable_List-childRadius)',
    // by default, The ListItem & ListItemButton use automatic radius adjustment based on the parent List.
    '--ListItem-startActionTranslateX': 'calc(0.5 * var(--ListItem-paddingLeft))',
    '--ListItem-endActionTranslateX': 'calc(-0.5 * var(--ListItem-paddingRight))',
    margin: 'initial'
  }, theme.typography[`body-${ownerState.size}`], ownerState.orientation === 'horizontal' ? (0, _extends2.default)({}, ownerState.wrap ? {
    padding: 'var(--List-padding)',
    // Fallback is not needed for row-wrap List
    marginInlineStart: 'calc(-1 * var(--List-gap))',
    marginBlockStart: 'calc(-1 * var(--List-gap))'
  } : {
    paddingInline: 'var(--List-padding, var(--ListDivider-gap))',
    paddingBlock: 'var(--List-padding)'
  }) : {
    paddingBlock: 'var(--List-padding, var(--ListDivider-gap))',
    paddingInline: 'var(--List-padding)'
  }, ownerState.marker && {
    paddingInlineStart: '3ch' // the width of the marker
  }), (0, _extends2.default)({
    boxSizing: 'border-box',
    borderRadius: 'var(--List-radius)',
    listStyle: 'none',
    display: 'flex',
    flexDirection: ownerState.orientation === 'horizontal' ? 'row' : 'column'
  }, ownerState.wrap && {
    flexWrap: 'wrap'
  }, ownerState.marker && {
    '--_List-markerDisplay': 'list-item',
    '--_List-markerType': ownerState.marker,
    lineHeight: 'calc(var(--ListItem-minHeight) - 2 * var(--ListItem-paddingY))'
  }, {
    flexGrow: 1,
    position: 'relative'
  }, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color], {
    '--unstable_List-borderWidth': 'var(--variant-borderWidth, 0px)'
  }, borderRadius !== undefined && {
    '--List-radius': borderRadius
  }, p !== undefined && {
    '--List-padding': p
  }, padding !== undefined && {
    '--List-padding': padding
  })];
});
const ListRoot = (0, _styles.styled)(StyledList, {
  name: 'JoyList',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({});
/**
 *
 * Demos:
 *
 * - [Lists](https://mui.com/joy-ui/react-list/)
 *
 * API:
 *
 * - [List API](https://mui.com/joy-ui/api/list/)
 */
const List = /*#__PURE__*/React.forwardRef(function List(inProps, ref) {
  var _inProps$size;
  const nesting = React.useContext(_NestedListContext.default);
  const group = React.useContext(_GroupListContext.default);
  const radioGroupContext = React.useContext(_RadioGroupContext.default);
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyList'
  });
  const {
      component,
      className,
      children,
      size: sizeProp,
      orientation = 'vertical',
      wrap = false,
      variant = 'plain',
      color = 'neutral',
      role: roleProp,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const size = sizeProp || ((_inProps$size = inProps.size) != null ? _inProps$size : 'md');
  let role;
  if (group) {
    role = 'group';
  }
  if (radioGroupContext) {
    role = 'presentation';
  }
  if (roleProp) {
    role = roleProp;
  }
  const ownerState = (0, _extends2.default)({}, props, {
    instanceSize: inProps.size,
    size,
    nesting,
    orientation,
    wrap,
    variant,
    color,
    role
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
    elementType: ListRoot,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      as: component,
      role,
      'aria-labelledby': typeof nesting === 'string' ? nesting : undefined
    }
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ComponentListContext.default.Provider, {
      value: `${typeof component === 'string' ? component : ''}:${role || ''}`,
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ListProvider.default, {
        row: orientation === 'horizontal',
        wrap: wrap,
        children: children
      })
    })
  }));
});
process.env.NODE_ENV !== "production" ? List.propTypes /* remove-proptypes */ = {
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
   * The marker (such as a disc, character, or custom counter style) of the list items.
   * When this prop is specified, the List Item changes the CSS display to `list-item` in order to apply the marker.
   *
   * To see all available options, check out the [MDN list-style-type page](https://developer.mozilla.org/en-US/docs/Web/CSS/list-style-type).
   */
  marker: _propTypes.default.string,
  /**
   * The component orientation.
   * @default 'vertical'
   */
  orientation: _propTypes.default.oneOf(['horizontal', 'vertical']),
  /**
   * @ignore
   */
  role: _propTypes.default /* @typescript-to-proptypes-ignore */.string,
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
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string]),
  /**
   * Only for horizontal list.
   * If `true`, the list sets the flex-wrap to "wrap" and adjust margin to have gap-like behavior (will move to `gap` in the future).
   *
   * @default false
   */
  wrap: _propTypes.default.bool
} : void 0;
var _default = exports.default = List;