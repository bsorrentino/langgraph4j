"use strict";
'use client';

// do not remove the following import (https://github.com/microsoft/TypeScript/issues/29808#issuecomment-1320713018)
/* eslint-disable @typescript-eslint/no-unused-vars */
// @ts-ignore
var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useColorScheme = exports.getInitColorSchemeScript = exports.CssVarsProvider = void 0;
var _system = require("@mui/system");
var _defaultTheme = _interopRequireDefault(require("./defaultTheme"));
var _identifier = _interopRequireDefault(require("./identifier"));
var _InitColorSchemeScript = require("../InitColorSchemeScript/InitColorSchemeScript");
const {
  CssVarsProvider,
  useColorScheme,
  getInitColorSchemeScript: getInitColorSchemeScriptSystem
} = (0, _system.unstable_createCssVarsProvider)({
  themeId: _identifier.default,
  theme: _defaultTheme.default,
  attribute: _InitColorSchemeScript.defaultConfig.attribute,
  modeStorageKey: _InitColorSchemeScript.defaultConfig.modeStorageKey,
  colorSchemeStorageKey: _InitColorSchemeScript.defaultConfig.colorSchemeStorageKey,
  defaultColorScheme: {
    light: _InitColorSchemeScript.defaultConfig.defaultLightColorScheme,
    dark: _InitColorSchemeScript.defaultConfig.defaultDarkColorScheme
  }
});

/**
 * @deprecated use `InitColorSchemeScript` instead
 *
 * ```diff
 * - import { getInitColorSchemeScript } from '@mui/joy/styles';
 * + import InitColorSchemeScript from '@mui/joy/InitColorSchemeScript';
 *
 * - getInitColorSchemeScript();
 * + <InitColorSchemeScript />;
 * ```
 */
exports.useColorScheme = useColorScheme;
exports.CssVarsProvider = CssVarsProvider;
const getInitColorSchemeScript = exports.getInitColorSchemeScript = getInitColorSchemeScriptSystem;