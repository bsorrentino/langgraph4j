"use strict";
'use client';

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = ThemeProvider;
exports.useTheme = void 0;
var React = _interopRequireWildcard(require("react"));
var _system = require("@mui/system");
var _defaultTheme = _interopRequireDefault(require("./defaultTheme"));
var _extendTheme = _interopRequireDefault(require("./extendTheme"));
var _identifier = _interopRequireDefault(require("./identifier"));
var _jsxRuntime = require("react/jsx-runtime");
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useTheme = () => {
  const theme = (0, _system.useTheme)(_defaultTheme.default);
  if (process.env.NODE_ENV !== 'production') {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    React.useDebugValue(theme);
  }

  // @ts-ignore internal logic
  return theme[_identifier.default] || theme;
};
exports.useTheme = useTheme;
function ThemeProvider({
  children,
  theme: themeInput
}) {
  let theme = _defaultTheme.default;
  if (themeInput) {
    theme = (0, _extendTheme.default)(_identifier.default in themeInput ? themeInput[_identifier.default] : themeInput);
  }
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_system.ThemeProvider, {
    theme: theme,
    themeId: themeInput && _identifier.default in themeInput ? _identifier.default : undefined,
    children: children
  });
}