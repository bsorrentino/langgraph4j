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
var _base = require("@mui/base");
var _utils = require("@mui/utils");
var _styles = require("../styles");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _Person = _interopRequireDefault(require("../internal/svg-icons/Person"));
var _avatarClasses = require("./avatarClasses");
var _AvatarGroup = require("../AvatarGroup/AvatarGroup");
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["alt", "color", "size", "variant", "src", "srcSet", "children", "component", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    size,
    variant,
    color,
    src,
    srcSet
  } = ownerState;
  const slots = {
    root: ['root', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    img: [(src || srcSet) && 'img'],
    fallback: ['fallback']
  };
  return (0, _base.unstable_composeClasses)(slots, _avatarClasses.getAvatarUtilityClass, {});
};
const AvatarRoot = (0, _styled.default)('div', {
  name: 'JoyAvatar',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants;
  return (0, _extends2.default)({
    '--Icon-color': ownerState.color !== 'neutral' || ownerState.variant === 'solid' ? 'currentColor' : theme.vars.palette.text.icon
  }, theme.typography[`title-${ownerState.size}`], ownerState.size === 'sm' && {
    width: `var(--Avatar-size, 2rem)`,
    height: `var(--Avatar-size, 2rem)`,
    fontSize: `calc(var(--Avatar-size, 2rem) * 0.4375)` // default as 14px
  }, ownerState.size === 'md' && {
    width: `var(--Avatar-size, 2.5rem)`,
    height: `var(--Avatar-size, 2.5rem)`,
    fontSize: `calc(var(--Avatar-size, 2.5rem) * 0.4)` // default as 16px
  }, ownerState.size === 'lg' && {
    width: `var(--Avatar-size, 3rem)`,
    height: `var(--Avatar-size, 3rem)`,
    fontSize: `calc(var(--Avatar-size, 3rem) * 0.375)` // default as 18px
  }, {
    marginInlineStart: 'var(--Avatar-marginInlineStart)',
    boxShadow: `var(--Avatar-ring)`,
    position: 'relative',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexShrink: 0,
    lineHeight: 1,
    overflow: 'hidden',
    borderRadius: 'var(--Avatar-radius, 50%)',
    userSelect: 'none'
  }, (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color]);
});
const AvatarImg = (0, _styled.default)('img', {
  name: 'JoyAvatar',
  slot: 'Img',
  overridesResolver: (props, styles) => styles.img
})({
  width: '100%',
  height: '100%',
  textAlign: 'center',
  // Handle non-square image. The property isn't supported by IE11.
  objectFit: 'cover',
  // Hide alt text.
  color: 'transparent',
  // Hide the image broken icon, only works on Chrome.
  textIndent: 10000
});
const AvatarFallback = (0, _styled.default)(_Person.default, {
  name: 'JoyAvatar',
  slot: 'Fallback',
  overridesResolver: (props, styles) => styles.fallback
})({
  width: '64%',
  height: '64%'
});
function useLoaded({
  crossOrigin,
  referrerPolicy,
  src,
  srcSet
}) {
  const [loaded, setLoaded] = React.useState(false);
  React.useEffect(() => {
    if (!src && !srcSet) {
      return undefined;
    }
    setLoaded(false);
    let active = true;
    const image = new Image();
    image.onload = () => {
      if (!active) {
        return;
      }
      setLoaded('loaded');
    };
    image.onerror = () => {
      if (!active) {
        return;
      }
      setLoaded('error');
    };
    image.crossOrigin = crossOrigin;
    image.referrerPolicy = referrerPolicy;
    if (src) {
      image.src = src;
    }
    if (srcSet) {
      image.srcset = srcSet;
    }
    return () => {
      active = false;
    };
  }, [crossOrigin, referrerPolicy, src, srcSet]);
  return loaded;
}
/**
 *
 * Demos:
 *
 * - [Avatar](https://mui.com/joy-ui/react-avatar/)
 * - [Skeleton](https://mui.com/joy-ui/react-skeleton/)
 *
 * API:
 *
 * - [Avatar API](https://mui.com/joy-ui/api/avatar/)
 */
const Avatar = /*#__PURE__*/React.forwardRef(function Avatar(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAvatar'
  });
  const groupContext = React.useContext(_AvatarGroup.AvatarGroupContext);
  const {
      alt,
      color: colorProp = 'neutral',
      size: sizeProp = 'md',
      variant: variantProp = 'soft',
      src,
      srcSet,
      children: childrenProp,
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const variant = inProps.variant || (groupContext == null ? void 0 : groupContext.variant) || variantProp;
  const color = inProps.color || (groupContext == null ? void 0 : groupContext.color) || colorProp;
  const size = inProps.size || (groupContext == null ? void 0 : groupContext.size) || sizeProp;
  let children = null;
  const ownerState = (0, _extends2.default)({}, props, {
    color,
    size,
    variant,
    grouped: !!groupContext
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: classes.root,
    elementType: AvatarRoot,
    externalForwardedProps,
    ownerState
  });
  const [SlotImg, imageProps] = (0, _useSlot.default)('img', {
    additionalProps: {
      alt,
      src,
      srcSet
    },
    className: classes.img,
    elementType: AvatarImg,
    externalForwardedProps,
    ownerState
  });
  const [SlotFallback, fallbackProps] = (0, _useSlot.default)('fallback', {
    className: classes.fallback,
    elementType: AvatarFallback,
    externalForwardedProps,
    ownerState
  });

  // Use a hook instead of onError on the img element to support server-side rendering.
  const loaded = useLoaded((0, _extends2.default)({}, imageProps, {
    src,
    srcSet
  }));
  const hasImg = src || srcSet;
  const hasImgNotFailing = hasImg && loaded !== 'error';
  if (hasImgNotFailing) {
    children = /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotImg, (0, _extends2.default)({}, imageProps));
  } else if (childrenProp != null) {
    children = childrenProp;
  } else if (alt) {
    children = alt[0];
  } else {
    children = /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotFallback, (0, _extends2.default)({}, fallbackProps));
  }
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? Avatar.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Used in combination with `src` or `srcSet` to
   * provide an alt attribute for the rendered `img` element.
   */
  alt: _propTypes.default.string,
  /**
   * Used to render icon or text elements inside the Avatar if `src` is not set.
   * This can be an element, or just a string.
   */
  children: _propTypes.default.node,
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
   * The size of the component.
   * It accepts theme values between 'sm' and 'lg'.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['lg', 'md', 'sm']), _propTypes.default.string]),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    fallback: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    img: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    fallback: _propTypes.default.elementType,
    img: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The `src` attribute for the `img` element.
   */
  src: _propTypes.default.string,
  /**
   * The `srcSet` attribute for the `img` element.
   * Use this attribute for responsive image display.
   */
  srcSet: _propTypes.default.string,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'soft'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Avatar;