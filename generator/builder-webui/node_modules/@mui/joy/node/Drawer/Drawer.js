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
var _unstable_useModal = require("@mui/base/unstable_useModal");
var _Portal = require("@mui/base/Portal");
var _FocusTrap = require("@mui/base/FocusTrap");
var _styles = require("../styles");
var _colorInversion = require("../colorInversion");
var _Modal = require("../Modal/Modal");
var _CloseModalContext = _interopRequireDefault(require("../Modal/CloseModalContext"));
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _drawerClasses = require("./drawerClasses");
var _ModalDialogVariantColorContext = _interopRequireDefault(require("../ModalDialog/ModalDialogVariantColorContext"));
var _ModalDialogSizeContext = _interopRequireDefault(require("../ModalDialog/ModalDialogSizeContext"));
var _dialogTitleClasses = _interopRequireDefault(require("../DialogTitle/dialogTitleClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "anchor", "container", "disableAutoFocus", "disableEnforceFocus", "disableEscapeKeyDown", "disablePortal", "disableRestoreFocus", "disableScrollLock", "hideBackdrop", "color", "variant", "invertedColors", "size", "onClose", "onKeyDown", "open", "component", "slots", "slotProps"];
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
    root: ['root', !open && 'hidden', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    backdrop: ['backdrop'],
    content: ['content']
  };
  return (0, _base.unstable_composeClasses)(slots, _drawerClasses.getDrawerUtilityClass, {});
};
const DrawerRoot = (0, _styles.styled)(_Modal.StyledModalRoot, {
  name: 'JoyDrawer',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState
}) => (0, _extends2.default)({
  '--Drawer-transitionDuration': '0.3s',
  '--Drawer-transitionFunction': 'ease',
  '--ModalClose-radius': 'max((var(--Drawer-contentRadius) - var(--variant-borderWidth, 0px)) - var(--ModalClose-inset), min(var(--ModalClose-inset) / 2, (var(--Drawer-contentRadius) - var(--variant-borderWidth, 0px)) / 2))'
}, ownerState.size === 'sm' && {
  '--ModalClose-inset': '0.5rem',
  '--Drawer-verticalSize': 'clamp(350px, 30%, 100%)',
  '--Drawer-horizontalSize': 'clamp(256px, 20%, 100%)',
  '--Drawer-titleMargin': '0.625rem 0.75rem calc(0.625rem / 2)'
}, ownerState.size === 'md' && {
  '--ModalClose-inset': '0.5rem',
  '--Drawer-verticalSize': 'clamp(400px, 45%, 100%)',
  '--Drawer-horizontalSize': 'clamp(300px, 30%, 100%)',
  '--Drawer-titleMargin': '0.75rem 0.75rem calc(0.75rem / 2)'
}, ownerState.size === 'lg' && {
  '--ModalClose-inset': '0.75rem',
  '--Drawer-verticalSize': 'clamp(500px, 60%, 100%)',
  '--Drawer-horizontalSize': 'clamp(440px, 60%, 100%)',
  '--Drawer-titleMargin': '1rem 1rem calc(1rem / 2)'
}, {
  transitionProperty: 'visibility',
  transitionDelay: ownerState.open ? '0s' : 'var(--Drawer-transitionDuration)'
}, !ownerState.open && {
  visibility: 'hidden'
}));
const DrawerBackdrop = (0, _styles.styled)(_Modal.StyledModalBackdrop, {
  name: 'JoyDrawer',
  slot: 'Backdrop',
  overridesResolver: (props, styles) => styles.backdrop
})(({
  ownerState
}) => ({
  opacity: ownerState.open ? 1 : 0,
  transition: 'opacity var(--Drawer-transitionDuration) ease-in-out'
}));
const DrawerContent = (0, _styles.styled)('div', {
  name: 'JoyDrawer',
  slot: 'Content',
  overridesResolver: (props, styles) => styles.content
})(({
  theme,
  ownerState
}) => {
  var _theme$variants;
  return (0, _extends2.default)({}, theme.typography[`body-${ownerState.size}`], {
    boxShadow: theme.shadow.md,
    backgroundColor: theme.vars.palette.background.surface,
    outline: 0,
    display: 'flex',
    flexDirection: 'column',
    position: 'fixed',
    boxSizing: 'border-box',
    overflow: 'auto'
  }, ownerState.anchor === 'left' && {
    top: 0,
    left: 0,
    transform: ownerState.open ? 'translateX(0)' : 'translateX(-100%)'
  }, ownerState.anchor === 'right' && {
    top: 0,
    right: 0,
    transform: ownerState.open ? 'translateX(0)' : 'translateX(100%)'
  }, ownerState.anchor === 'top' && {
    top: 0,
    transform: ownerState.open ? 'translateY(0)' : 'translateY(-100%)'
  }, ownerState.anchor === 'bottom' && {
    bottom: 0,
    transform: ownerState.open ? 'translateY(0)' : 'translateY(100%)'
  }, {
    height: ownerState.anchor.match(/(left|right)/) ? '100%' : 'min(100vh, var(--Drawer-verticalSize))',
    width: ownerState.anchor.match(/(top|bottom)/) ? '100vw' : 'min(100vw, var(--Drawer-horizontalSize))',
    transition: 'transform var(--Drawer-transitionDuration) var(--Drawer-transitionFunction)'
  }, ownerState.variant === 'solid' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySolidInversion)(ownerState.color)(theme), ownerState.variant === 'soft' && ownerState.color && ownerState.invertedColors && (0, _colorInversion.applySoftInversion)(ownerState.color)(theme), (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color], {
    [`& > .${_dialogTitleClasses.default.root}`]: {
      '--unstable_DialogTitle-margin': 'var(--Drawer-titleMargin)'
    }
  });
});

/**
 * The navigation drawers (or "sidebars") provide ergonomic access to destinations in a site or app functionality such as switching accounts.
 *
 * Demos:
 *
 * - [Drawer](https://mui.com/joy-ui/react-drawer/)
 *
 * API:
 *
 * - [Drawer API](https://mui.com/joy-ui/api/drawer/)
 */
const Drawer = /*#__PURE__*/React.forwardRef(function Drawer(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyDrawer'
  });
  const {
      children,
      anchor = 'left',
      container,
      disableAutoFocus = false,
      disableEnforceFocus = false,
      disableEscapeKeyDown = false,
      disablePortal = false,
      disableRestoreFocus = false,
      disableScrollLock = false,
      hideBackdrop = false,
      color = 'neutral',
      variant = 'plain',
      invertedColors = false,
      size = 'md',
      onClose,
      open,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    anchor,
    disableAutoFocus,
    disableEnforceFocus,
    disableEscapeKeyDown,
    disablePortal,
    disableRestoreFocus,
    disableScrollLock,
    hideBackdrop,
    invertedColors,
    color,
    variant,
    size
  });
  const {
    getRootProps,
    getBackdropProps,
    rootRef,
    portalRef,
    isTopModal
  } = (0, _unstable_useModal.unstable_useModal)((0, _extends2.default)({}, ownerState, {
    rootRef: ref,
    children: null
  }));
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const labelledBy = (0, _utils.unstable_useId)();
  const describedBy = (0, _utils.unstable_useId)();
  const contextValue = React.useMemo(() => ({
    variant,
    color,
    labelledBy,
    describedBy
  }), [color, variant, labelledBy, describedBy]);
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref: rootRef,
    className: classes.root,
    elementType: DrawerRoot,
    externalForwardedProps,
    getSlotProps: getRootProps,
    ownerState
  });
  const [SlotBackdrop, backdropProps] = (0, _useSlot.default)('backdrop', {
    className: classes.backdrop,
    elementType: DrawerBackdrop,
    externalForwardedProps,
    getSlotProps: getBackdropProps,
    ownerState
  });
  const [SlotContent, contentProps] = (0, _useSlot.default)('content', {
    className: classes.content,
    elementType: DrawerContent,
    additionalProps: {
      tabIndex: -1,
      role: 'dialog',
      'aria-modal': 'true',
      'aria-labelledby': labelledBy,
      'aria-describedby': describedBy
    },
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_CloseModalContext.default.Provider, {
    value: onClose,
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ModalDialogSizeContext.default.Provider, {
      value: size,
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ModalDialogVariantColorContext.default.Provider, {
        value: contextValue,
        children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_Portal.Portal, {
          ref: portalRef,
          container: container,
          disablePortal: disablePortal,
          children: /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
            children: [!hideBackdrop ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotBackdrop, (0, _extends2.default)({}, backdropProps)) : null, /*#__PURE__*/(0, _jsxRuntime.jsx)(_FocusTrap.FocusTrap, {
              disableEnforceFocus: disableEnforceFocus,
              disableAutoFocus: disableAutoFocus,
              disableRestoreFocus: disableRestoreFocus,
              isEnabled: isTopModal,
              open: open,
              children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotContent, (0, _extends2.default)({}, contentProps, {
                children: children
              }))
            })]
          }))
        })
      })
    })
  });
});
process.env.NODE_ENV !== "production" ? Drawer.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Side from which the drawer will appear.
   * @default 'left'
   */
  anchor: _propTypes.default.oneOf(['bottom', 'left', 'right', 'top']),
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
   * An HTML element or function that returns one.
   * The `container` will have the portal children appended to it.
   *
   * You can also provide a callback, which is called in a React layout effect.
   * This lets you set the container from a ref, and also makes server-side rendering possible.
   *
   * By default, it uses the body of the top-level document object,
   * so it's simply `document.body` most of the time.
   */
  container: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_utils.HTMLElementType, _propTypes.default.func]),
  /**
   * If `true`, the modal will not automatically shift focus to itself when it opens, and
   * replace it to the last focused element when it closes.
   * This also works correctly with any modal children that have the `disableAutoFocus` prop.
   *
   * Generally this should never be set to `true` as it makes the modal less
   * accessible to assistive technologies, like screen readers.
   * @default false
   */
  disableAutoFocus: _propTypes.default.bool,
  /**
   * If `true`, the modal will not prevent focus from leaving the modal while open.
   *
   * Generally this should never be set to `true` as it makes the modal less
   * accessible to assistive technologies, like screen readers.
   * @default false
   */
  disableEnforceFocus: _propTypes.default.bool,
  /**
   * If `true`, hitting escape will not fire the `onClose` callback.
   * @default false
   */
  disableEscapeKeyDown: _propTypes.default.bool,
  /**
   * The `children` will be under the DOM hierarchy of the parent component.
   * @default false
   */
  disablePortal: _propTypes.default.bool,
  /**
   * If `true`, the modal will not restore focus to previously focused element once
   * modal is hidden or unmounted.
   * @default false
   */
  disableRestoreFocus: _propTypes.default.bool,
  /**
   * Disable the scroll lock behavior.
   * @default false
   */
  disableScrollLock: _propTypes.default.bool,
  /**
   * If `true`, the backdrop is not rendered.
   * @default false
   */
  hideBackdrop: _propTypes.default.bool,
  /**
   * If `true`, the children with an implicit color prop invert their colors to match the component's variant and color.
   * @default false
   */
  invertedColors: _propTypes.default.bool,
  /**
   * Callback fired when the component requests to be closed.
   * The `reason` parameter can optionally be used to control the response to `onClose`.
   *
   * @param {object} event The event source of the callback.
   * @param {string} reason Can be: `"escapeKeyDown"`, `"backdropClick"`, `"closeClick"`.
   */
  onClose: _propTypes.default.func,
  /**
   * @ignore
   */
  onKeyDown: _propTypes.default.func,
  /**
   * If `true`, the component is shown.
   */
  open: _propTypes.default.bool.isRequired,
  /**
   * The size of the component.
   * @default 'md'
   */
  size: _propTypes.default.oneOf(['sm', 'md', 'lg']),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    backdrop: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    content: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    backdrop: _propTypes.default.elementType,
    content: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid'])
} : void 0;
var _default = exports.default = Drawer;