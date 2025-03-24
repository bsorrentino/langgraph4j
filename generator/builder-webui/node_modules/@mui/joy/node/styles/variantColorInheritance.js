"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.VariantColorProvider = VariantColorProvider;
exports.getChildVariantAndColor = getChildVariantAndColor;
exports.useVariantColor = useVariantColor;
var React = _interopRequireWildcard(require("react"));
var _jsxRuntime = require("react/jsx-runtime");
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const VariantColorContext = /*#__PURE__*/React.createContext(undefined);
if (process.env.NODE_ENV !== 'production') {
  VariantColorContext.displayName = 'VariantColorContext';
}

/**
 * @internal For internal usage only.
 *
 * Use this function in a slot to get the matched default variant and color when the parent's variant and/or color changes.
 */
function getChildVariantAndColor(parentVariant, parentColor) {
  let childColor = parentColor;
  let childVariant = parentVariant;
  if (parentVariant === 'outlined') {
    childColor = 'neutral';
    childVariant = 'plain';
  }
  if (parentVariant === 'plain') {
    childColor = 'neutral';
  }
  return {
    variant: childVariant,
    color: childColor
  };
}

/**
 * @internal For internal usage only.
 *
 * This hook should be used in a children that are connected with its parent
 * to get the matched default variant and color when the parent's variant and/or color changes.
 *
 * For example, the `Option` component in `Select` component is using this function.
 */
function useVariantColor(instanceVariant, instanceColor, alwaysInheritColor = false) {
  const value = React.useContext(VariantColorContext);
  const [variant, color] = typeof value === 'string' ? value.split(':') : [];
  const result = getChildVariantAndColor(variant || undefined, color || undefined);
  result.variant = instanceVariant || result.variant;
  result.color = instanceColor || (alwaysInheritColor ? color : result.color);
  return result;
}

/**
 * @internal For internal usage only.
 */
function VariantColorProvider({
  children,
  color,
  variant
}) {
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(VariantColorContext.Provider, {
    value: `${variant || ''}:${color || ''}`,
    children: children
  });
}