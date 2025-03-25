"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledListItemButton = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _useButton = require("@mui/base/useButton");
var _styles = require("../styles");
var _listItemClasses = _interopRequireDefault(require("../ListItem/listItemClasses"));
var _listItemButtonClasses = _interopRequireWildcard(require("./listItemButtonClasses"));
var _ListItemButtonOrientationContext = _interopRequireDefault(require("./ListItemButtonOrientationContext"));
var _RowListContext = _interopRequireDefault(require("../List/RowListContext"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "className", "action", "component", "orientation", "role", "selected", "color", "variant", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    color,
    disabled,
    focusVisible,
    focusVisibleClassName,
    selected,
    variant
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', focusVisible && 'focusVisible', color && `color${(0, _utils.unstable_capitalize)(color)}`, selected && 'selected', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`]
  };
  const composedClasses = (0, _composeClasses.unstable_composeClasses)(slots, _listItemButtonClasses.getListItemButtonUtilityClass, {});
  if (focusVisible && focusVisibleClassName) {
    composedClasses.root += ` ${focusVisibleClassName}`;
  }
  return composedClasses;
};
const StyledListItemButton = exports.StyledListItemButton = (0, _styles.styled)('div')(({
  theme,
  ownerState
}) => {
  var _theme$variants, _theme$variants2, _theme$variants3, _theme$variants4, _theme$variants5, _theme$variants6;
  return (0, _extends2.default)({
    '--Icon-margin': 'initial',
    // reset the icon's margin.
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon,
    WebkitTapHighlightColor: 'transparent',
    boxSizing: 'border-box',
    position: 'relative',
    font: 'inherit',
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'stretch',
    // always stretch itself to fill the parent (List|ListItem)
    gap: 'var(--ListItem-gap)'
  }, ownerState.orientation === 'vertical' && {
    flexDirection: 'column',
    justifyContent: 'center'
  }, {
    textAlign: 'initial',
    textDecoration: 'initial',
    // reset native anchor tag
    backgroundColor: 'initial',
    // reset button background
    cursor: 'pointer',
    // In some cases, ListItemButton is a child of ListItem so the margin needs to be controlled by the ListItem. The value is negative to account for the ListItem's padding
    marginInline: 'var(--ListItemButton-marginInline)',
    marginBlock: 'var(--ListItemButton-marginBlock)'
  }, ownerState['data-first-child'] === undefined && {
    marginInlineStart: ownerState.row ? 'var(--List-gap)' : undefined,
    marginBlockStart: ownerState.row ? undefined : 'var(--List-gap)'
  }, {
    // account for the border width, so that all of the ListItemButtons content aligned horizontally
    paddingBlock: 'calc(var(--ListItem-paddingY) - var(--variant-borderWidth, 0px))',
    // account for the border width, so that all of the ListItemButtons content aligned vertically
    paddingInlineStart: 'calc(var(--ListItem-paddingLeft) + var(--ListItem-startActionWidth, var(--unstable_startActionWidth, 0px)))',
    // --internal variable makes it possible to customize the actionWidth from the top List
    paddingInlineEnd: 'calc(var(--ListItem-paddingRight) + var(--ListItem-endActionWidth, var(--unstable_endActionWidth, 0px)))',
    // --internal variable makes it possible to customize the actionWidth from the top List
    minBlockSize: 'var(--ListItem-minHeight)',
    border: '1px solid transparent',
    // use `transparent` as a placeholder to prevent the button from jumping when switching to `outlined` variant
    borderRadius: 'var(--ListItem-radius)',
    flex: 'var(--unstable_ListItem-flex, none)',
    // prevent children from shrinking when the List's height is limited.
    fontSize: 'inherit',
    // prevent user agent style when component="button"
    lineHeight: 'inherit',
    // prevent user agent style when component="button"
    minInlineSize: 0,
    [theme.focus.selector]: (0, _extends2.default)({}, theme.focus.default, {
      zIndex: 1 // to be above of the next element. For example, the first Tab item should be above the second so that the outline is above the second Tab.
    })
  }, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color], {
    '&:active': (_theme$variants2 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants2[ownerState.color],
    [`.${_listItemClasses.default.root} > &`]: {
      '--unstable_ListItem-flex': '1 0 0%' // grow to fill the available space of ListItem
    },
    [`&.${_listItemButtonClasses.default.selected}`]: (0, _extends2.default)({}, (_theme$variants3 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants3[ownerState.color], {
      '--Icon-color': 'currentColor'
    }),
    [`&:not(.${_listItemButtonClasses.default.selected}, [aria-selected="true"])`]: {
      '&:hover': (_theme$variants4 = theme.variants[`${ownerState.variant}Hover`]) == null ? void 0 : _theme$variants4[ownerState.color],
      '&:active': (_theme$variants5 = theme.variants[`${ownerState.variant}Active`]) == null ? void 0 : _theme$variants5[ownerState.color]
    },
    [`&.${_listItemButtonClasses.default.disabled}`]: (0, _extends2.default)({}, (_theme$variants6 = theme.variants[`${ownerState.variant}Disabled`]) == null ? void 0 : _theme$variants6[ownerState.color])
  });
});
const ListItemButtonRoot = (0, _styles.styled)(StyledListItemButton, {
  name: 'JoyListItemButton',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState,
  theme
}) => (0, _extends2.default)({}, !ownerState.row && {
  [`&.${_listItemButtonClasses.default.selected}`]: {
    fontWeight: theme.vars.fontWeight.md
  }
}));
/**
 *
 * Demos:
 *
 * - [Lists](https://mui.com/joy-ui/react-list/)
 *
 * API:
 *
 * - [ListItemButton API](https://mui.com/joy-ui/api/list-item-button/)
 */
const ListItemButton = /*#__PURE__*/React.forwardRef(function ListItemButton(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyListItemButton'
  });
  const row = React.useContext(_RowListContext.default);
  const {
      children,
      className,
      action,
      component = 'div',
      orientation = 'horizontal',
      role,
      selected = false,
      color = 'neutral',
      variant = 'plain',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const buttonRef = React.useRef(null);
  const handleRef = (0, _utils.unstable_useForkRef)(buttonRef, ref);
  const {
    focusVisible,
    setFocusVisible,
    getRootProps
  } = (0, _useButton.useButton)((0, _extends2.default)({}, props, {
    rootRef: handleRef
  }));
  React.useImperativeHandle(action, () => ({
    focusVisible: () => {
      var _buttonRef$current;
      setFocusVisible(true);
      (_buttonRef$current = buttonRef.current) == null || _buttonRef$current.focus();
    }
  }), [setFocusVisible]);
  const ownerState = (0, _extends2.default)({}, props, {
    component,
    color,
    focusVisible,
    orientation,
    row,
    selected,
    variant
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
    elementType: ListItemButtonRoot,
    externalForwardedProps,
    ownerState,
    getSlotProps: getRootProps
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_ListItemButtonOrientationContext.default.Provider, {
    value: orientation,
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      role: role != null ? role : rootProps.role,
      children: children
    }))
  });
});
process.env.NODE_ENV !== "production" ? ListItemButton.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * A ref for imperative actions. It currently only supports `focusVisible()` action.
   */
  action: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.shape({
    current: _propTypes.default.shape({
      focusVisible: _propTypes.default.func.isRequired
    })
  })]),
  /**
   * If `true`, the list item is focused during the first mount.
   * Focus will also be triggered if the value changes from false to true.
   * @default false
   */
  autoFocus: _propTypes.default.bool,
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
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * This prop can help identify which element has keyboard focus.
   * The class name will be applied when the element gains the focus through keyboard interaction.
   * It's a polyfill for the [CSS :focus-visible selector](https://drafts.csswg.org/selectors-4/#the-focus-visible-pseudo).
   * The rationale for using this feature [is explained here](https://github.com/WICG/focus-visible/blob/HEAD/explainer.md).
   * A [polyfill can be used](https://github.com/WICG/focus-visible) to apply a `focus-visible` class to other components
   * if needed.
   */
  focusVisibleClassName: _propTypes.default.string,
  /**
   * The content direction flow.
   * @default 'horizontal'
   */
  orientation: _propTypes.default.oneOf(['horizontal', 'vertical']),
  /**
   * @ignore
   */
  role: _propTypes.default /* @typescript-to-proptypes-ignore */.string,
  /**
   * If `true`, the component is selected.
   * @default false
   */
  selected: _propTypes.default.bool,
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
   * @default 0
   */
  tabIndex: _propTypes.default.number,
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = ListItemButton;