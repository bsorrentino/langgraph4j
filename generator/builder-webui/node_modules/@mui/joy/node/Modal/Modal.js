"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.StyledModalRoot = exports.StyledModalBackdrop = exports.ModalBackdrop = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _Portal = require("@mui/base/Portal");
var _FocusTrap = require("@mui/base/FocusTrap");
var _unstable_useModal = require("@mui/base/unstable_useModal");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _modalClasses = require("./modalClasses");
var _CloseModalContext = _interopRequireDefault(require("./CloseModalContext"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "container", "disableAutoFocus", "disableEnforceFocus", "disableEscapeKeyDown", "disablePortal", "disableRestoreFocus", "disableScrollLock", "hideBackdrop", "keepMounted", "onClose", "onKeyDown", "open", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    open
  } = ownerState;
  const slots = {
    root: ['root', !open && 'hidden'],
    backdrop: ['backdrop']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _modalClasses.getModalUtilityClass, {});
};
const StyledModalRoot = exports.StyledModalRoot = (0, _styles.styled)('div')(({
  ownerState,
  theme
}) => (0, _extends2.default)({
  '--unstable_popup-zIndex': `calc(${theme.vars.zIndex.modal} + 1)`,
  '& ~ [role="listbox"]': {
    // target all the listbox (Autocomplete, Menu, Select, etc.) that uses portal
    '--unstable_popup-zIndex': `calc(${theme.vars.zIndex.modal} + 1)`
  },
  position: 'fixed',
  zIndex: theme.vars.zIndex.modal,
  right: 0,
  bottom: 0,
  top: 0,
  left: 0
}, !ownerState.open && {
  visibility: 'hidden'
}));
const ModalRoot = (0, _styles.styled)(StyledModalRoot, {
  name: 'JoyModal',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({});
const StyledModalBackdrop = exports.StyledModalBackdrop = (0, _styles.styled)('div')(({
  theme
}) => ({
  zIndex: -1,
  position: 'fixed',
  right: 0,
  bottom: 0,
  top: 0,
  left: 0,
  backgroundColor: theme.vars.palette.background.backdrop,
  WebkitTapHighlightColor: 'transparent',
  backdropFilter: 'blur(8px)'
}));
const ModalBackdrop = exports.ModalBackdrop = (0, _styles.styled)(StyledModalBackdrop, {
  name: 'JoyModal',
  slot: 'Backdrop',
  overridesResolver: (props, styles) => styles.backdrop
})({});
/**
 *
 * Demos:
 *
 * - [Modal](https://mui.com/joy-ui/react-modal/)
 *
 * API:
 *
 * - [Modal API](https://mui.com/joy-ui/api/modal/)
 */
const Modal = /*#__PURE__*/React.forwardRef(function Modal(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyModal'
  });
  const {
      children,
      container,
      disableAutoFocus = false,
      disableEnforceFocus = false,
      disableEscapeKeyDown = false,
      disablePortal = false,
      disableRestoreFocus = false,
      disableScrollLock = false,
      hideBackdrop = false,
      keepMounted = false,
      onClose,
      open,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    disableAutoFocus,
    disableEnforceFocus,
    disableEscapeKeyDown,
    disablePortal,
    disableRestoreFocus,
    disableScrollLock,
    hideBackdrop,
    keepMounted
  });
  const {
    getRootProps,
    getBackdropProps,
    rootRef,
    portalRef,
    isTopModal
  } = (0, _unstable_useModal.unstable_useModal)((0, _extends2.default)({}, ownerState, {
    rootRef: ref
  }));
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref: rootRef,
    className: classes.root,
    elementType: ModalRoot,
    externalForwardedProps,
    getSlotProps: getRootProps,
    ownerState
  });
  const [SlotBackdrop, backdropProps] = (0, _useSlot.default)('backdrop', {
    className: classes.backdrop,
    elementType: ModalBackdrop,
    externalForwardedProps,
    getSlotProps: getBackdropProps,
    ownerState
  });
  if (!keepMounted && !open) {
    return null;
  }
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_CloseModalContext.default.Provider, {
    value: onClose,
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
          children: React.Children.only(children) && /*#__PURE__*/React.cloneElement(children, (0, _extends2.default)({}, children.props.tabIndex === undefined && {
            tabIndex: -1
          }))
        })]
      }))
    })
  });
});
process.env.NODE_ENV !== "production" ? Modal.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * A single child content element.
   */
  children: _utils.elementAcceptingRef.isRequired,
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
   * Always keep the children in the DOM.
   * This prop can be useful in SEO situation or
   * when you want to maximize the responsiveness of the Modal.
   * @default false
   */
  keepMounted: _propTypes.default.bool,
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
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    backdrop: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    backdrop: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object])
} : void 0;
var _default = exports.default = Modal;