"use strict";
'use client';

// reexports from system for module augmentation
var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  THEME_ID: true,
  CssVarsProvider: true,
  useColorScheme: true,
  getInitColorSchemeScript: true,
  shouldSkipGeneratingVar: true,
  styled: true,
  ThemeProvider: true,
  useThemeProps: true,
  extendTheme: true,
  createGetCssVar: true,
  StyledEngineProvider: true
};
Object.defineProperty(exports, "CssVarsProvider", {
  enumerable: true,
  get: function () {
    return _CssVarsProvider.CssVarsProvider;
  }
});
Object.defineProperty(exports, "StyledEngineProvider", {
  enumerable: true,
  get: function () {
    return _StyledEngineProvider.default;
  }
});
Object.defineProperty(exports, "THEME_ID", {
  enumerable: true,
  get: function () {
    return _identifier.default;
  }
});
Object.defineProperty(exports, "ThemeProvider", {
  enumerable: true,
  get: function () {
    return _ThemeProvider.default;
  }
});
Object.defineProperty(exports, "createGetCssVar", {
  enumerable: true,
  get: function () {
    return _extendTheme.createGetCssVar;
  }
});
Object.defineProperty(exports, "extendTheme", {
  enumerable: true,
  get: function () {
    return _extendTheme.default;
  }
});
Object.defineProperty(exports, "getInitColorSchemeScript", {
  enumerable: true,
  get: function () {
    return _CssVarsProvider.getInitColorSchemeScript;
  }
});
Object.defineProperty(exports, "shouldSkipGeneratingVar", {
  enumerable: true,
  get: function () {
    return _shouldSkipGeneratingVar.default;
  }
});
Object.defineProperty(exports, "styled", {
  enumerable: true,
  get: function () {
    return _styled.default;
  }
});
Object.defineProperty(exports, "useColorScheme", {
  enumerable: true,
  get: function () {
    return _CssVarsProvider.useColorScheme;
  }
});
Object.defineProperty(exports, "useThemeProps", {
  enumerable: true,
  get: function () {
    return _useThemeProps.default;
  }
});
var _identifier = _interopRequireDefault(require("./identifier"));
var _CssVarsProvider = require("./CssVarsProvider");
var _shouldSkipGeneratingVar = _interopRequireDefault(require("./shouldSkipGeneratingVar"));
var _styled = _interopRequireDefault(require("./styled"));
var _ThemeProvider = _interopRequireWildcard(require("./ThemeProvider"));
Object.keys(_ThemeProvider).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _ThemeProvider[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _ThemeProvider[key];
    }
  });
});
var _useThemeProps = _interopRequireDefault(require("./useThemeProps"));
var _extendTheme = _interopRequireWildcard(require("./extendTheme"));
var _StyledEngineProvider = _interopRequireDefault(require("./StyledEngineProvider"));
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }