"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = useForwardedInput;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var React = _interopRequireWildcard(require("react"));
var _useInput = require("@mui/base/useInput");
var _FormControlContext = _interopRequireDefault(require("../FormControl/FormControlContext"));
const _excluded = ["aria-describedby", "aria-label", "aria-labelledby", "autoComplete", "autoFocus", "className", "defaultValue", "disabled", "disabledInProp", "error", "id", "name", "onClick", "onChange", "onKeyDown", "onKeyUp", "onFocus", "onBlur", "placeholder", "readOnly", "required", "type", "value"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
function useForwardedInput(props, classes) {
  var _ref;
  const formControl = React.useContext(_FormControlContext.default);
  const {
      'aria-describedby': ariaDescribedby,
      'aria-label': ariaLabel,
      'aria-labelledby': ariaLabelledby,
      autoComplete,
      autoFocus,
      className,
      defaultValue,
      disabled: disabledProp,
      disabledInProp,
      error: errorProp,
      id,
      name,
      onClick,
      onChange,
      onKeyDown,
      onKeyUp,
      onFocus,
      onBlur,
      placeholder,
      readOnly,
      required,
      type,
      value
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const {
    getRootProps,
    getInputProps,
    focused,
    error,
    disabled
  } = (0, _useInput.useInput)({
    disabled: (_ref = disabledInProp != null ? disabledInProp : formControl == null ? void 0 : formControl.disabled) != null ? _ref : disabledProp,
    defaultValue,
    error: errorProp,
    onBlur,
    onClick,
    onChange,
    onFocus,
    required: required != null ? required : formControl == null ? void 0 : formControl.required,
    value
  });
  const rootStateClasses = {
    [classes.disabled]: disabled,
    [classes.error]: error,
    [classes.focused]: focused,
    [classes.formControl]: Boolean(formControl),
    [className]: className
  };
  const inputStateClasses = {
    [classes.disabled]: disabled
  };
  const propsToForward = {
    'aria-describedby': ariaDescribedby,
    'aria-label': ariaLabel,
    'aria-labelledby': ariaLabelledby,
    autoComplete,
    autoFocus,
    disabled,
    id,
    onKeyDown,
    onKeyUp,
    name,
    placeholder,
    readOnly,
    type
  };
  return (0, _extends2.default)({
    formControl,
    propsToForward,
    rootStateClasses,
    inputStateClasses,
    getRootProps,
    getInputProps,
    focused,
    error,
    disabled
  }, other);
}