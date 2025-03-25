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
var _composeClasses = require("@mui/base/composeClasses");
var _useMenu = require("@mui/base/useMenu");
var _useList = require("@mui/base/useList");
var _Popper = require("@mui/base/Popper");
var _utils2 = require("@mui/base/utils");
var _List = require("../List/List");
var _ListProvider = _interopRequireWildcard(require("../List/ListProvider"));
var _GroupListContext = _interopRequireDefault(require("../List/GroupListContext"));
var _styles = require("../styles");
var _colorInversion = require("../colorInversion");
var _variantColorInheritance = require("../styles/variantColorInheritance");
var _menuClasses = require("./menuClasses");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["actions", "children", "color", "component", "disablePortal", "keepMounted", "id", "invertedColors", "onItemsChange", "modifiers", "variant", "size", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    open,
    variant,
    color,
    size
  } = ownerState;
  const slots = {
    root: ['root', open && 'expanded', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    listbox: ['listbox']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _menuClasses.getMenuUtilityClass, {});
};
const MenuRoot = (0, _styles.styled)(_List.StyledList, {
  name: 'JoyMenu',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants, _theme$variants2;
  const variantStyle = (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color];
  return [(0, _extends2.default)({
    '--focus-outline-offset': `calc(${theme.vars.focus.thickness} * -1)`,
    // to prevent the focus outline from being cut by overflow
    '--ListItem-stickyBackground': (variantStyle == null ? void 0 : variantStyle.backgroundColor) || (variantStyle == null ? void 0 : variantStyle.background) || theme.vars.palette.background.popup,
    '--ListItem-stickyTop': 'calc(var(--List-padding, var(--ListDivider-gap)) * -1)'
  }, _ListProvider.scopedVariables, {
    borderRadius: `var(--List-radius, ${theme.vars.radius.sm})`,
    boxShadow: theme.shadow.md,
    overflow: 'auto',
    // `unstable_popup-zIndex` is a private variable that lets other component, for example Modal, to override the z-index so that the listbox can be displayed above the Modal.
    zIndex: `var(--unstable_popup-zIndex, ${theme.vars.zIndex.popup})`
  }, !(variantStyle != null && variantStyle.backgroundColor) && {
    backgroundColor: theme.vars.palette.background.popup
  }, ownerState.variant === 'solid' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySolidInversion)(ownerState.color)(theme), ownerState.variant === 'soft' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySoftInversion)(ownerState.color)(theme), (_theme$variants2 = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants2[ownerState.color])];
});

/**
 *
 * Demos:
 *
 * - [Menu](https://mui.com/joy-ui/react-menu/)
 *
 * API:
 *
 * - [Menu API](https://mui.com/joy-ui/api/menu/)
 * - inherits [Popper API](https://mui.com/base-ui/react-popper/components-api/#popper)
 */
const Menu = /*#__PURE__*/React.forwardRef(function Menu(inProps, ref) {
  var _props$slots;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyMenu'
  });
  const {
      actions,
      children,
      color = 'neutral',
      component,
      disablePortal = false,
      keepMounted = false,
      id,
      invertedColors = false,
      onItemsChange,
      modifiers: modifiersProp,
      variant = 'outlined',
      size = 'md',
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const {
    contextValue,
    getListboxProps,
    dispatch,
    open,
    triggerElement
  } = (0, _useMenu.useMenu)({
    onItemsChange,
    id,
    listboxRef: ref
  });
  React.useImperativeHandle(actions, () => ({
    dispatch,
    resetHighlight: () => dispatch({
      type: _useList.ListActionTypes.resetHighlight,
      event: null
    })
  }), [dispatch]);
  const ownerState = (0, _extends2.default)({}, props, {
    disablePortal,
    invertedColors,
    color,
    variant,
    size,
    open,
    nesting: false,
    row: false
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const modifiers = React.useMemo(() => [{
    name: 'offset',
    options: {
      offset: [0, 4]
    }
  }, ...(modifiersProp || [])], [modifiersProp]);
  const rootProps = (0, _utils2.useSlotProps)({
    elementType: MenuRoot,
    getSlotProps: getListboxProps,
    externalForwardedProps,
    externalSlotProps: {},
    ownerState: ownerState,
    additionalProps: {
      anchorEl: triggerElement,
      open: open && triggerElement !== null,
      disablePortal,
      keepMounted,
      modifiers
    },
    className: classes.root
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(MenuRoot, (0, _extends2.default)({}, rootProps, !((_props$slots = props.slots) != null && _props$slots.root) && {
    as: _Popper.Popper,
    slots: {
      root: component || 'ul'
    }
  }, {
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_useMenu.MenuProvider, {
      value: contextValue,
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_variantColorInheritance.VariantColorProvider, {
        variant: invertedColors ? undefined : variant,
        color: color,
        children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_GroupListContext.default.Provider, {
          value: "menu",
          children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ListProvider.default, {
            nested: true,
            children: children
          })
        })
      })
    })
  }));
});
process.env.NODE_ENV !== "production" ? Menu.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * A ref with imperative actions.
   * It allows to select the first or last menu item.
   */
  actions: _utils.refType,
  /**
   * @ignore
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
   * The `children` will be under the DOM hierarchy of the parent component.
   * @default false
   */
  disablePortal: _propTypes.default.bool,
  /**
   * @ignore
   */
  id: _propTypes.default.string,
  /**
   * If `true`, the children with an implicit color prop invert their colors to match the component's variant and color.
   * @default false
   */
  invertedColors: _propTypes.default.bool,
  /**
   * Always keep the children in the DOM.
   * This prop can be useful in SEO situation or
   * when you want to maximize the responsiveness of the Popper.
   * @default false
   */
  keepMounted: _propTypes.default.bool,
  /**
   * Popper.js is based on a "plugin-like" architecture,
   * most of its features are fully encapsulated "modifiers".
   *
   * A modifier is a function that is called each time Popper.js needs to
   * compute the position of the popper.
   * For this reason, modifiers should be very performant to avoid bottlenecks.
   * To learn how to create a modifier, [read the modifiers documentation](https://popper.js.org/docs/v2/modifiers/).
   */
  modifiers: _propTypes.default.arrayOf(_propTypes.default.shape({
    data: _propTypes.default.object,
    effect: _propTypes.default.func,
    enabled: _propTypes.default.bool,
    fn: _propTypes.default.func,
    name: _propTypes.default.any,
    options: _propTypes.default.object,
    phase: _propTypes.default.oneOf(['afterMain', 'afterRead', 'afterWrite', 'beforeMain', 'beforeRead', 'beforeWrite', 'main', 'read', 'write']),
    requires: _propTypes.default.arrayOf(_propTypes.default.string),
    requiresIfExists: _propTypes.default.arrayOf(_propTypes.default.string)
  })),
  /**
   * Triggered when focus leaves the menu and the menu should close.
   */
  onClose: _propTypes.default.func,
  /**
   * Function called when the items displayed in the menu change.
   */
  onItemsChange: _propTypes.default.func,
  /**
   * Controls whether the menu is displayed.
   * @default false
   */
  open: _propTypes.default.bool,
  /**
   * The size of the component (affect other nested list* components because the `Menu` inherits `List`).
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
   * @default 'outlined'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Menu;