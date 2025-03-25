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
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _composeClasses = require("@mui/base/composeClasses");
var _useAutocomplete = require("@mui/base/useAutocomplete");
var _Popper = require("@mui/base/Popper");
var _styles = require("../styles");
var _Close = _interopRequireDefault(require("../internal/svg-icons/Close"));
var _ArrowDropDown = _interopRequireDefault(require("../internal/svg-icons/ArrowDropDown"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _variantColorInheritance = require("../styles/variantColorInheritance");
var _IconButton = require("../IconButton/IconButton");
var _Chip = _interopRequireDefault(require("../Chip"));
var _ChipDelete = _interopRequireDefault(require("../ChipDelete"));
var _Input = require("../Input/Input");
var _List = _interopRequireDefault(require("../List"));
var _ListProvider = _interopRequireDefault(require("../List/ListProvider"));
var _ListSubheader = _interopRequireDefault(require("../ListSubheader"));
var _ListItem = _interopRequireDefault(require("../ListItem"));
var _autocompleteClasses = _interopRequireWildcard(require("./autocompleteClasses"));
var _FormControlContext = _interopRequireDefault(require("../FormControl/FormControlContext"));
var _AutocompleteListbox = require("../AutocompleteListbox/AutocompleteListbox");
var _AutocompleteOption = require("../AutocompleteOption/AutocompleteOption");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
var _ClearIcon, _ArrowDropDownIcon;
const _excluded = ["aria-describedby", "aria-label", "aria-labelledby", "autoComplete", "autoHighlight", "autoSelect", "autoFocus", "blurOnSelect", "clearIcon", "clearOnBlur", "clearOnEscape", "clearText", "closeText", "defaultValue", "disableCloseOnSelect", "disabledItemsFocusable", "disableListWrap", "disableClearable", "disabled", "endDecorator", "error", "filterOptions", "filterSelectedOptions", "forcePopupIcon", "freeSolo", "getLimitTagsText", "getOptionDisabled", "getOptionKey", "getOptionLabel", "handleHomeEndKeys", "includeInputInList", "isOptionEqualToValue", "groupBy", "id", "inputValue", "limitTags", "loading", "loadingText", "multiple", "name", "noOptionsText", "onChange", "onClose", "onHighlightChange", "onInputChange", "onOpen", "open", "openOnFocus", "openText", "options", "placeholder", "popupIcon", "readOnly", "renderGroup", "renderOption", "renderTags", "required", "type", "startDecorator", "size", "color", "variant", "value", "component", "selectOnFocus", "slots", "slotProps"],
  _excluded2 = ["onDelete"],
  _excluded3 = ["key"],
  _excluded4 = ["onBlur", "onFocus", "onMouseDown"],
  _excluded5 = ["key"]; // slot components
// default render components
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const defaultIsActiveElementInListbox = listboxRef => listboxRef.current !== null && listboxRef.current.contains(document.activeElement);
// @ts-ignore
const defaultGetOptionLabel = option => {
  var _option$label;
  return (_option$label = option.label) != null ? _option$label : option;
};
const defaultLimitTagsText = more => `+${more}`;
const defaultRenderGroup = params => /*#__PURE__*/(0, _jsxRuntime.jsxs)(_ListItem.default, {
  nested: true,
  children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(_ListSubheader.default, {
    sticky: true,
    children: params.group
  }), /*#__PURE__*/(0, _jsxRuntime.jsx)(_List.default, {
    children: params.children
  })]
}, params.key);
const useUtilityClasses = ownerState => {
  const {
    disabled,
    focused,
    hasClearIcon,
    hasPopupIcon,
    popupOpen,
    variant,
    color,
    size,
    multiple
  } = ownerState;
  const slots = {
    root: ['root', focused && 'focused', hasClearIcon && 'hasClearIcon', hasPopupIcon && 'hasPopupIcon', variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`],
    wrapper: ['wrapper', multiple && 'multiple'],
    input: ['input'],
    startDecorator: ['startDecorator'],
    endDecorator: ['endDecorator'],
    clearIndicator: ['clearIndicator'],
    popupIndicator: ['popupIndicator', popupOpen && 'popupIndicatorOpen', disabled && 'disabled'],
    listbox: ['listbox'],
    option: ['option'],
    loading: ['loading'],
    noOptions: ['noOptions'],
    limitTag: ['limitTag']
  };
  return (0, _composeClasses.unstable_composeClasses)(slots, _autocompleteClasses.getAutocompleteUtilityClass, {});
};
const AutocompleteRoot = (0, _styled.default)(_Input.StyledInputRoot, {
  name: 'JoyAutocomplete',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  ownerState
}) => (0, _extends2.default)({}, ownerState.size === 'sm' && {
  '--Autocomplete-wrapperGap': '3px'
}, ownerState.size === 'md' && {
  '--Autocomplete-wrapperGap': '3px'
}, ownerState.size === 'lg' && {
  '--Autocomplete-wrapperGap': '4px'
}, {
  /* Avoid double tap issue on iOS */
  '@media (pointer: fine)': {
    [`&:hover .${_autocompleteClasses.default.clearIndicator}`]: {
      visibility: 'visible'
    }
  }
}, ownerState.multiple && !ownerState.startDecorator && {
  paddingInlineStart: 0
}));

/**
 * Wrapper groups the chips (multi selection) and the input
 * so that start/end decorators can stay in the normal flow.
 */
const AutocompleteWrapper = (0, _styled.default)('div', {
  name: 'JoyAutocomplete',
  slot: 'Wrapper',
  overridesResolver: (props, styles) => styles.wrapper
})(({
  ownerState
}) => ({
  flex: 1,
  // stretch to fill the root slot
  minWidth: 0,
  // won't push end decorator out of the autocomplete
  display: 'flex',
  alignItems: 'center',
  flexWrap: 'wrap',
  gap: 'var(--Autocomplete-wrapperGap)',
  [`&.${_autocompleteClasses.default.multiple}`]: (0, _extends2.default)({
    paddingBlock: 'var(--Autocomplete-wrapperGap)'
  }, !ownerState.startDecorator && {
    paddingInlineStart: 'var(--Autocomplete-wrapperGap)'
  }, !ownerState.endDecorator && {
    paddingInlineEnd: 'var(--Autocomplete-wrapperGap)'
  })
}));
const AutocompleteInput = (0, _styled.default)(_Input.StyledInputHtml, {
  name: 'JoyAutocomplete',
  slot: 'Input',
  overridesResolver: (props, styles) => styles.input
})(({
  ownerState
}) => (0, _extends2.default)({
  minWidth: 30,
  minHeight: 'var(--Chip-minHeight)'
}, ownerState.multiple && {
  marginInlineStart: 'calc(var(--Autocomplete-wrapperGap) * 2.5)'
}));
const AutocompleteStartDecorator = (0, _styled.default)(_Input.StyledInputStartDecorator, {
  name: 'JoyAutocomplete',
  slot: 'StartDecorator',
  overridesResolver: (props, styles) => styles.startDecorator
})({});
const AutocompleteEndDecorator = (0, _styled.default)(_Input.StyledInputEndDecorator, {
  name: 'JoyAutocomplete',
  slot: 'EndDecorator',
  overridesResolver: (props, styles) => styles.endDecorator
})(({
  ownerState
}) => (0, _extends2.default)({}, (ownerState.hasClearIcon || ownerState.hasPopupIcon) && {
  '--Button-margin': '0px',
  '--IconButton-margin': '0px',
  '--Icon-margin': '0px'
}));
const AutocompleteClearIndicator = (0, _styled.default)(_IconButton.StyledIconButton, {
  name: 'JoyAutocomplete',
  slot: 'ClearIndicator',
  overridesResolver: (props, styles) => styles.clearIndicator
})(({
  ownerState
}) => (0, _extends2.default)({
  alignSelf: 'center'
}, !ownerState.hasPopupIcon && {
  marginInlineEnd: 'calc(var(--Input-decoratorChildOffset) * -1)'
}, {
  marginInlineStart: 'calc(var(--_Input-paddingBlock) / 2)',
  visibility: ownerState.focused ? 'visible' : 'hidden'
}));
const AutocompletePopupIndicator = (0, _styled.default)(_IconButton.StyledIconButton, {
  name: 'JoyAutocomplete',
  slot: 'PopupIndicator',
  overridesResolver: (props, styles) => styles.popupIndicator
})({
  alignSelf: 'center',
  marginInlineStart: 'calc(var(--_Input-paddingBlock) / 2)',
  marginInlineEnd: 'calc(var(--Input-decoratorChildOffset) * -1)',
  [`&.${_autocompleteClasses.default.popupIndicatorOpen}`]: {
    transform: 'rotate(180deg)',
    '--Icon-color': 'currentColor'
  }
});
const AutocompleteListbox = (0, _styled.default)(_AutocompleteListbox.StyledAutocompleteListbox, {
  name: 'JoyAutocomplete',
  slot: 'Listbox',
  overridesResolver: (props, styles) => styles.listbox
})(({
  theme
}) => ({
  // `unstable_popup-zIndex` is a private variable that lets other component, for example Modal, to override the z-index so that the listbox can be displayed above the Modal.
  zIndex: `var(--unstable_popup-zIndex, ${theme.vars.zIndex.popup})`
}));
const AutocompleteOption = (0, _styled.default)(_AutocompleteOption.StyledAutocompleteOption, {
  name: 'JoyAutocomplete',
  slot: 'Option',
  overridesResolver: (props, styles) => styles.option
})({});
const AutocompleteLoading = (0, _styled.default)(_ListItem.default, {
  name: 'JoyAutocomplete',
  slot: 'Loading',
  overridesResolver: (props, styles) => styles.loading
})(({
  theme
}) => ({
  color: (theme.vars || theme).palette.text.secondary
}));
const AutocompleteNoOptions = (0, _styled.default)(_ListItem.default, {
  name: 'JoyAutocomplete',
  slot: 'NoOptions',
  overridesResolver: (props, styles) => styles.noOptions
})(({
  theme
}) => ({
  color: (theme.vars || theme).palette.text.secondary
}));
const AutocompleteLimitTag = (0, _styled.default)('div', {
  name: 'JoyAutocomplete',
  slot: 'NoOptions',
  overridesResolver: (props, styles) => styles.noOptions
})({
  marginInlineStart: 'calc(var(--Input-paddingInline) / 2)',
  marginBlockStart: 'var(--_Input-paddingBlock)'
});
/**
 *
 * Demos:
 *
 * - [Autocomplete](https://mui.com/joy-ui/react-autocomplete/)
 *
 * API:
 *
 * - [Autocomplete API](https://mui.com/joy-ui/api/autocomplete/)
 */
const Autocomplete = /*#__PURE__*/React.forwardRef(function Autocomplete(inProps, ref) {
  var _ref, _inProps$error, _ref2, _inProps$size, _inProps$color, _formControl$color, _ref3;
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyAutocomplete'
  });
  const {
      'aria-describedby': ariaDescribedby,
      'aria-label': ariaLabel,
      'aria-labelledby': ariaLabelledby,
      autoFocus,
      clearIcon = _ClearIcon || (_ClearIcon = /*#__PURE__*/(0, _jsxRuntime.jsx)(_Close.default, {
        fontSize: "md"
      })),
      clearText = 'Clear',
      closeText = 'Close',
      disableClearable = false,
      disabled: disabledProp,
      endDecorator,
      error: errorProp = false,
      forcePopupIcon = 'auto',
      freeSolo = false,
      getLimitTagsText = defaultLimitTagsText,
      getOptionLabel = defaultGetOptionLabel,
      groupBy,
      id,
      limitTags = -1,
      loading = false,
      loadingText = 'Loading…',
      multiple = false,
      name,
      noOptionsText = 'No options',
      openText = 'Open',
      placeholder,
      popupIcon = _ArrowDropDownIcon || (_ArrowDropDownIcon = /*#__PURE__*/(0, _jsxRuntime.jsx)(_ArrowDropDown.default, {})),
      readOnly = false,
      renderGroup = defaultRenderGroup,
      renderOption: renderOptionProp,
      renderTags,
      required,
      type,
      startDecorator,
      size: sizeProp = 'md',
      color: colorProp = 'neutral',
      variant = 'outlined',
      component,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const formControl = React.useContext(_FormControlContext.default);
  const error = (_ref = (_inProps$error = inProps.error) != null ? _inProps$error : formControl == null ? void 0 : formControl.error) != null ? _ref : errorProp;
  const size = (_ref2 = (_inProps$size = inProps.size) != null ? _inProps$size : formControl == null ? void 0 : formControl.size) != null ? _ref2 : sizeProp;
  const color = (_inProps$color = inProps.color) != null ? _inProps$color : error ? 'danger' : (_formControl$color = formControl == null ? void 0 : formControl.color) != null ? _formControl$color : colorProp;
  const disabled = (_ref3 = disabledProp != null ? disabledProp : formControl == null ? void 0 : formControl.disabled) != null ? _ref3 : false;
  const {
    getRootProps,
    getInputProps,
    getPopupIndicatorProps,
    getClearProps,
    getTagProps,
    getListboxProps,
    getOptionProps,
    value,
    dirty,
    popupOpen,
    focused,
    focusedTag,
    anchorEl,
    setAnchorEl,
    inputValue,
    groupedOptions
  } = (0, _useAutocomplete.useAutocomplete)((0, _extends2.default)({}, props, {
    id: id != null ? id : formControl == null ? void 0 : formControl.htmlFor,
    componentName: 'Autocomplete',
    unstable_classNamePrefix: 'Mui',
    unstable_isActiveElementInListbox: defaultIsActiveElementInListbox
  }));
  const {
    onMouseDown: handleInputMouseDown
  } = getInputProps();
  const {
    onClick: handleRootOnClick
  } = getRootProps();
  const hasClearIcon = !disableClearable && !disabled && dirty && !readOnly;
  const hasPopupIcon = (!freeSolo || forcePopupIcon === true) && forcePopupIcon !== false;

  // If you modify this, make sure to keep the `AutocompleteOwnerState` type in sync.
  const ownerState = (0, _extends2.default)({
    instanceColor: inProps.color
  }, props, {
    value,
    disabled,
    focused,
    getOptionLabel,
    hasOptions: !!groupedOptions.length,
    hasClearIcon,
    hasPopupIcon,
    inputFocused: focusedTag === -1,
    popupOpen,
    size,
    color,
    variant
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  let selectedOptions;
  if (multiple && value.length > 0) {
    const getCustomizedTagProps = params => {
      const _getTagProps = getTagProps(params),
        {
          onDelete
        } = _getTagProps,
        tagProps = (0, _objectWithoutPropertiesLoose2.default)(_getTagProps, _excluded2);
      return (0, _extends2.default)({
        disabled,
        size,
        onClick: onDelete
      }, tagProps);
    };
    if (renderTags) {
      selectedOptions = renderTags(value, getCustomizedTagProps, ownerState);
    } else {
      selectedOptions = value.map((option, index) => {
        const _getCustomizedTagProp = getCustomizedTagProps({
            index
          }),
          {
            key: endDecoratorKey
          } = _getCustomizedTagProp,
          endDecoratorProps = (0, _objectWithoutPropertiesLoose2.default)(_getCustomizedTagProp, _excluded3);
        return /*#__PURE__*/(0, _jsxRuntime.jsx)(_Chip.default, {
          size: size,
          variant: "soft",
          color: "neutral",
          endDecorator: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ChipDelete.default, (0, _extends2.default)({}, endDecoratorProps), endDecoratorKey),
          sx: {
            minWidth: 0
          },
          children: getOptionLabel(option)
        }, index);
      });
    }
  }
  const rootRef = (0, _utils.unstable_useForkRef)(ref, setAnchorEl);
  const rootStateClasses = {
    [_autocompleteClasses.default.disabled]: disabled,
    [_autocompleteClasses.default.error]: error,
    [_autocompleteClasses.default.focused]: focused,
    [_autocompleteClasses.default.formControl]: Boolean(formControl)
  };
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref: rootRef,
    className: [classes.root, rootStateClasses],
    elementType: AutocompleteRoot,
    externalForwardedProps,
    ownerState,
    getSlotProps: getRootProps,
    additionalProps: {
      onClick: event => {
        if (handleRootOnClick) {
          handleRootOnClick(event);
        }
        if (event.currentTarget === event.target && handleInputMouseDown) {
          handleInputMouseDown(event);
        }
      }
    }
  });
  const [SlotWrapper, wrapperProps] = (0, _useSlot.default)('wrapper', {
    className: classes.wrapper,
    elementType: AutocompleteWrapper,
    externalForwardedProps,
    ownerState
  });
  const inputStateClasses = {
    [_autocompleteClasses.default.disabled]: disabled
  };
  const [SlotInput, inputProps] = (0, _useSlot.default)('input', {
    className: [classes.input, inputStateClasses],
    elementType: AutocompleteInput,
    getSlotProps: handlers => {
      const _getInputProps = getInputProps(),
        {
          onBlur,
          onFocus,
          onMouseDown
        } = _getInputProps,
        inputSlotProps = (0, _objectWithoutPropertiesLoose2.default)(_getInputProps, _excluded4);
      return (0, _extends2.default)({}, inputSlotProps, {
        onBlur: event => {
          var _handlers$onBlur;
          onBlur == null || onBlur(event);
          (_handlers$onBlur = handlers.onBlur) == null || _handlers$onBlur.call(handlers, event);
        },
        onFocus: event => {
          var _handlers$onFocus;
          onFocus == null || onFocus(event);
          (_handlers$onFocus = handlers.onFocus) == null || _handlers$onFocus.call(handlers, event);
        },
        onMouseDown: event => {
          var _handlers$onMouseDown;
          onMouseDown == null || onMouseDown(event);
          (_handlers$onMouseDown = handlers.onMouseDown) == null || _handlers$onMouseDown.call(handlers, event);
        }
      });
    },
    externalForwardedProps,
    ownerState,
    additionalProps: {
      autoFocus,
      placeholder,
      name,
      readOnly,
      disabled,
      required: required != null ? required : formControl == null ? void 0 : formControl.required,
      type,
      'aria-invalid': error || undefined,
      'aria-label': ariaLabel,
      'aria-labelledby': ariaLabelledby,
      'aria-describedby': ariaDescribedby != null ? ariaDescribedby : formControl == null ? void 0 : formControl['aria-describedby']
    }
  });
  const [SlotStartDecorator, startDecoratorProps] = (0, _useSlot.default)('startDecorator', {
    className: classes.startDecorator,
    elementType: AutocompleteStartDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotEndDecorator, endDecoratorProps] = (0, _useSlot.default)('endDecorator', {
    className: classes.endDecorator,
    elementType: AutocompleteEndDecorator,
    externalForwardedProps,
    ownerState
  });
  const [SlotClearIndicator, clearIndicatorProps] = (0, _useSlot.default)('clearIndicator', {
    className: classes.clearIndicator,
    elementType: AutocompleteClearIndicator,
    getSlotProps: getClearProps,
    externalForwardedProps,
    ownerState,
    getSlotOwnerState: mergedProps => ({
      size: mergedProps.size || size,
      variant: mergedProps.variant || (0, _variantColorInheritance.getChildVariantAndColor)(variant, color).variant || 'plain',
      color: mergedProps.color || (0, _variantColorInheritance.getChildVariantAndColor)(variant, color).color || 'neutral',
      disableColorInversion: !!inProps.color
    }),
    additionalProps: {
      'aria-label': clearText,
      title: clearText
    }
  });
  const [SlotPopupIndicator, popupIndicatorProps] = (0, _useSlot.default)('popupIndicator', {
    className: classes.popupIndicator,
    elementType: AutocompletePopupIndicator,
    getSlotProps: getPopupIndicatorProps,
    externalForwardedProps,
    ownerState,
    getSlotOwnerState: mergedProps => ({
      size: mergedProps.size || size,
      variant: mergedProps.variant || (0, _variantColorInheritance.getChildVariantAndColor)(variant, color).variant || 'plain',
      color: mergedProps.color || (0, _variantColorInheritance.getChildVariantAndColor)(variant, color).color || 'neutral',
      disableColorInversion: !!inProps.color
    }),
    additionalProps: {
      disabled,
      'aria-label': popupOpen ? closeText : openText,
      title: popupOpen ? closeText : openText,
      type: 'button'
    }
  });
  const [SlotListbox, listboxProps] = (0, _useSlot.default)('listbox', {
    className: classes.listbox,
    elementType: AutocompleteListbox,
    getSlotProps: getListboxProps,
    externalForwardedProps,
    ownerState,
    getSlotOwnerState: mergedProps => ({
      size: mergedProps.size || size,
      variant: mergedProps.variant || variant,
      color: mergedProps.color || color,
      disableColorInversion: !mergedProps.disablePortal
    }),
    additionalProps: {
      anchorEl,
      open: popupOpen,
      style: anchorEl ? {
        width: anchorEl.clientWidth
      } : {}
    }
  });
  const [SlotLoading, loadingProps] = (0, _useSlot.default)('loading', {
    className: classes.loading,
    elementType: AutocompleteLoading,
    externalForwardedProps,
    ownerState
  });
  const [SlotNoOptions, noOptionsProps] = (0, _useSlot.default)('noOptions', {
    className: classes.noOptions,
    elementType: AutocompleteNoOptions,
    externalForwardedProps,
    ownerState,
    additionalProps: {
      role: 'presentation',
      onMouseDown: event => {
        // Prevent input blur when interacting with the "no options" content
        event.preventDefault();
      }
    }
  });
  const [SlotLimitTag, limitTagProps] = (0, _useSlot.default)('limitTag', {
    className: classes.limitTag,
    elementType: AutocompleteLimitTag,
    externalForwardedProps,
    ownerState
  });
  if (limitTags > -1 && Array.isArray(selectedOptions)) {
    const more = selectedOptions.length - limitTags;
    if (!focused && more > 0) {
      selectedOptions = selectedOptions.splice(0, limitTags);
      selectedOptions.push( /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotLimitTag, (0, _extends2.default)({}, limitTagProps, {
        children: getLimitTagsText(more)
      }), selectedOptions.length));
    }
  }
  const [SlotOption, baseOptionProps] = (0, _useSlot.default)('option', {
    className: classes.option,
    elementType: AutocompleteOption,
    externalForwardedProps,
    ownerState,
    getSlotOwnerState: mergedProps => ({
      variant: mergedProps.variant || (0, _variantColorInheritance.getChildVariantAndColor)(variant, color).variant || 'plain',
      color: mergedProps.color || (0, _variantColorInheritance.getChildVariantAndColor)(variant, color).color || 'neutral',
      disableColorInversion: !listboxProps.disablePortal
    }),
    additionalProps: {
      as: 'li'
    }
  });
  const defaultRenderOption = (optionProps, option) => {
    const {
        key
      } = optionProps,
      rest = (0, _objectWithoutPropertiesLoose2.default)(optionProps, _excluded5);
    return /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotOption, (0, _extends2.default)({}, rest, {
      children: getOptionLabel(option)
    }), key);
  };
  const renderOption = renderOptionProp || defaultRenderOption;
  const renderListOption = (option, index) => {
    const optionProps = getOptionProps({
      option,
      index
    });
    return renderOption((0, _extends2.default)({}, baseOptionProps, optionProps), option, {
      // `aria-selected` prop will always by boolean, see useAutocomplete hook.
      selected: !!optionProps['aria-selected'],
      inputValue,
      ownerState
    });
  };

  // Wait for `listboxProps` because `slotProps.listbox` could be a function.
  const modifiers = React.useMemo(() => [{
    name: 'offset',
    options: {
      offset: [0, 4]
    }
  }, ...(listboxProps.modifiers || [])], [listboxProps.modifiers]);
  let popup = null;
  if (anchorEl) {
    var _props$slots;
    popup = /*#__PURE__*/(0, _jsxRuntime.jsx)(_variantColorInheritance.VariantColorProvider, {
      variant: variant,
      color: color,
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_ListProvider.default, {
        nested: true,
        children: /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotListbox, (0, _extends2.default)({}, listboxProps, {
          className: (0, _clsx.default)(listboxProps.className)
          // @ts-ignore internal logic (too complex to typed PopperOwnProps to SlotListbox but this should be removed when we have `usePopper`)
          ,
          modifiers: modifiers
        }, !((_props$slots = props.slots) != null && _props$slots.listbox) && {
          as: _Popper.Popper,
          slots: {
            root: listboxProps.as || 'ul'
          }
        }, {
          children: [groupedOptions.map((option, index) => {
            if (groupBy) {
              const typedOption = option;
              return renderGroup({
                key: String(typedOption.key),
                group: typedOption.group,
                children: typedOption.options.map((option2, index2) => renderListOption(option2, typedOption.index + index2))
              });
            }
            return renderListOption(option, index);
          }), loading && groupedOptions.length === 0 ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotLoading, (0, _extends2.default)({}, loadingProps, {
            children: loadingText
          })) : null, groupedOptions.length === 0 && !freeSolo && !loading ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotNoOptions, (0, _extends2.default)({}, noOptionsProps, {
            children: noOptionsText
          })) : null]
        }))
      })
    });
  }
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(React.Fragment, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: [startDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotStartDecorator, (0, _extends2.default)({}, startDecoratorProps, {
        children: startDecorator
      })), /*#__PURE__*/(0, _jsxRuntime.jsxs)(SlotWrapper, (0, _extends2.default)({}, wrapperProps, {
        children: [selectedOptions, /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotInput, (0, _extends2.default)({}, inputProps))]
      })), endDecorator && /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotEndDecorator, (0, _extends2.default)({}, endDecoratorProps, {
        children: endDecorator
      })), hasClearIcon ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotClearIndicator, (0, _extends2.default)({}, clearIndicatorProps, {
        children: clearIcon
      })) : null, hasPopupIcon ? /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotPopupIndicator, (0, _extends2.default)({}, popupIndicatorProps, {
        children: popupIcon
      })) : null]
    })), popup]
  });
});
process.env.NODE_ENV !== "production" ? Autocomplete.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * Identifies the element (or elements) that describes the object.
   * @see aria-labelledby
   */
  'aria-describedby': _propTypes.default.string,
  /**
   * Defines a string value that labels the current element.
   * @see aria-labelledby.
   */
  'aria-label': _propTypes.default.string,
  /**
   * Identifies the element (or elements) that labels the current element.
   * @see aria-describedby.
   */
  'aria-labelledby': _propTypes.default.string,
  /**
   * If `true`, the portion of the selected suggestion that the user hasn't typed,
   * known as the completion string, appears inline after the input cursor in the textbox.
   * The inline completion string is visually highlighted and has a selected state.
   * @default false
   */
  autoComplete: _propTypes.default.bool,
  /**
   * If `true`, the `input` element is focused during the first mount.
   */
  autoFocus: _propTypes.default.bool,
  /**
   * If `true`, the first option is automatically highlighted.
   * @default false
   */
  autoHighlight: _propTypes.default.bool,
  /**
   * If `true`, the selected option becomes the value of the input
   * when the Autocomplete loses focus unless the user chooses
   * a different option or changes the character string in the input.
   *
   * When using the `freeSolo` mode, the typed value will be the input value
   * if the Autocomplete loses focus without highlighting an option.
   * @default false
   */
  autoSelect: _propTypes.default.bool,
  /**
   * Control if the input should be blurred when an option is selected:
   *
   * - `false` the input is not blurred.
   * - `true` the input is always blurred.
   * - `touch` the input is blurred after a touch event.
   * - `mouse` the input is blurred after a mouse event.
   * @default false
   */
  blurOnSelect: _propTypes.default.oneOfType([_propTypes.default.oneOf(['mouse', 'touch']), _propTypes.default.bool]),
  /**
   * The icon to display in place of the default clear icon.
   * @default <ClearIcon fontSize="md" />
   */
  clearIcon: _propTypes.default.node,
  /**
   * If `true`, the input's text is cleared on blur if no value is selected.
   *
   * Set it to `true` if you want to help the user enter a new value.
   * Set it to `false` if you want to help the user resume their search.
   * @default !props.freeSolo
   */
  clearOnBlur: _propTypes.default.bool,
  /**
   * If `true`, clear all values when the user presses escape and the popup is closed.
   * @default false
   */
  clearOnEscape: _propTypes.default.bool,
  /**
   * Override the default text for the *clear* icon button.
   *
   * For localization purposes, you can use the provided [translations](/material-ui/guides/localization/).
   * @default 'Clear'
   */
  clearText: _propTypes.default.string,
  /**
   * Override the default text for the *close popup* icon button.
   *
   * For localization purposes, you can use the provided [translations](/material-ui/guides/localization/).
   * @default 'Close'
   */
  closeText: _propTypes.default.string,
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   * @default 'neutral'
   */
  color: _propTypes.default.oneOf(['danger', 'neutral', 'primary', 'success', 'warning']),
  /**
   * The default value. Use when the component is not controlled.
   * @default props.multiple ? [] : null
   */
  defaultValue: (0, _utils.chainPropTypes)(_propTypes.default.any, props => {
    if (props.multiple && props.defaultValue !== undefined && !Array.isArray(props.defaultValue)) {
      return new Error(['MUI: The Autocomplete expects the `defaultValue` prop to be an array when `multiple={true}` or undefined.', `However, ${props.defaultValue} was provided.`].join('\n'));
    }
    return null;
  }),
  /**
   * If `true`, the input can't be cleared.
   * @default false
   */
  disableClearable: _propTypes.default.bool,
  /**
   * If `true`, the popup won't close when a value is selected.
   * @default false
   */
  disableCloseOnSelect: _propTypes.default.bool,
  /**
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * If `true`, will allow focus on disabled items.
   * @default false
   */
  disabledItemsFocusable: _propTypes.default.bool,
  /**
   * If `true`, the list box in the popup will not wrap focus.
   * @default false
   */
  disableListWrap: _propTypes.default.bool,
  /**
   * Trailing adornment for this input.
   */
  endDecorator: _propTypes.default.node,
  /**
   * If `true`, the `input` will indicate an error.
   * The prop defaults to the value (`false`) inherited from the parent FormControl component.
   * @default false
   */
  error: _propTypes.default.bool,
  /**
   * A function that determines the filtered options to be rendered on search.
   *
   * @default createFilterOptions()
   * @param {Value[]} options The options to render.
   * @param {object} state The state of the component.
   * @returns {Value[]}
   */
  filterOptions: _propTypes.default.func,
  /**
   * If `true`, hide the selected options from the list box.
   * @default false
   */
  filterSelectedOptions: _propTypes.default.bool,
  /**
   * Force the visibility display of the popup icon.
   * @default 'auto'
   */
  forcePopupIcon: _propTypes.default.oneOfType([_propTypes.default.oneOf(['auto']), _propTypes.default.bool]),
  /**
   * If `true`, the Autocomplete is free solo, meaning that the user input is not bound to provided options.
   * @default false
   */
  freeSolo: _propTypes.default.bool,
  /**
   * The label to display when the tags are truncated (`limitTags`).
   *
   * @param {string | number} more The number of truncated tags.
   * @returns {ReactNode}
   * @default (more: string | number) => `+${more}`
   */
  getLimitTagsText: _propTypes.default.func,
  /**
   * Used to determine the disabled state for a given option.
   *
   * @param {Value} option The option to test.
   * @returns {boolean}
   */
  getOptionDisabled: _propTypes.default.func,
  /**
   * Used to determine the key for a given option.
   * This can be useful when the labels of options are not unique (since labels are used as keys by default).
   *
   * @param {Value} option The option to get the key for.
   * @returns {string | number}
   */
  getOptionKey: _propTypes.default.func,
  /**
   * Used to determine the string value for a given option.
   * It's used to fill the input (and the list box options if `renderOption` is not provided).
   *
   * If used in free solo mode, it must accept both the type of the options and a string.
   *
   * @param {Value} option
   * @returns {string}
   * @default (option) => option.label ?? option
   */
  getOptionLabel: _propTypes.default.func,
  /**
   * If provided, the options will be grouped under the returned string.
   * The groupBy value is also used as the text for group headings when `renderGroup` is not provided.
   *
   * @param {Value} options The options to group.
   * @returns {string}
   */
  groupBy: _propTypes.default.func,
  /**
   * If `true`, the component handles the "Home" and "End" keys when the popup is open.
   * It should move focus to the first option and last option, respectively.
   * @default !props.freeSolo
   */
  handleHomeEndKeys: _propTypes.default.bool,
  /**
   * This prop is used to help implement the accessibility logic.
   * If you don't provide an id it will fall back to a randomly generated one.
   */
  id: _propTypes.default.string,
  /**
   * If `true`, the highlight can move to the input.
   * @default false
   */
  includeInputInList: _propTypes.default.bool,
  /**
   * The input value.
   */
  inputValue: _propTypes.default.string,
  /**
   * Used to determine if the option represents the given value.
   * Uses strict equality by default.
   * ⚠️ Both arguments need to be handled, an option can only match with one value.
   *
   * @param {Value} option The option to test.
   * @param {Value} value The value to test against.
   * @returns {boolean}
   */
  isOptionEqualToValue: _propTypes.default.func,
  /**
   * The maximum number of tags that will be visible when not focused.
   * Set `-1` to disable the limit.
   * @default -1
   */
  limitTags: _utils.integerPropType,
  /**
   * If `true`, the component is in a loading state.
   * This shows the `loadingText` in place of suggestions (only if there are no suggestions to show, for example `options` are empty).
   * @default false
   */
  loading: _propTypes.default.bool,
  /**
   * Text to display when in a loading state.
   *
   * For localization purposes, you can use the provided [translations](/material-ui/guides/localization/).
   * @default 'Loading…'
   */
  loadingText: _propTypes.default.node,
  /**
   * If `true`, `value` must be an array and the menu will support multiple selections.
   * @default false
   */
  multiple: _propTypes.default.bool,
  /**
   * Name attribute of the `input` element.
   */
  name: _propTypes.default.string,
  /**
   * Text to display when there are no options.
   *
   * For localization purposes, you can use the provided [translations](/material-ui/guides/localization/).
   * @default 'No options'
   */
  noOptionsText: _propTypes.default.node,
  /**
   * Callback fired when the value changes.
   *
   * @param {React.SyntheticEvent} event The event source of the callback.
   * @param {Value|Value[]} value The new value of the component.
   * @param {string} reason One of "createOption", "selectOption", "removeOption", "blur" or "clear".
   * @param {string} [details]
   */
  onChange: _propTypes.default.func,
  /**
   * Callback fired when the popup requests to be closed.
   * Use in controlled mode (see open).
   *
   * @param {React.SyntheticEvent} event The event source of the callback.
   * @param {string} reason Can be: `"toggleInput"`, `"escape"`, `"selectOption"`, `"removeOption"`, `"blur"`.
   */
  onClose: _propTypes.default.func,
  /**
   * Callback fired when the highlight option changes.
   *
   * @param {React.SyntheticEvent} event The event source of the callback.
   * @param {Value} option The highlighted option.
   * @param {string} reason Can be: `"keyboard"`, `"auto"`, `"mouse"`, `"touch"`.
   */
  onHighlightChange: _propTypes.default.func,
  /**
   * Callback fired when the input value changes.
   *
   * @param {React.SyntheticEvent} event The event source of the callback.
   * @param {string} value The new value of the text input.
   * @param {string} reason Can be: `"input"` (user input), `"reset"` (programmatic change), `"clear"`.
   */
  onInputChange: _propTypes.default.func,
  /**
   * @ignore
   */
  onKeyDown: _propTypes.default.func,
  /**
   * Callback fired when the popup requests to be opened.
   * Use in controlled mode (see open).
   *
   * @param {React.SyntheticEvent} event The event source of the callback.
   */
  onOpen: _propTypes.default.func,
  /**
   * If `true`, the component is shown.
   */
  open: _propTypes.default.bool,
  /**
   * If `true`, the popup will open on input focus.
   * @default false
   */
  openOnFocus: _propTypes.default.bool,
  /**
   * Override the default text for the *open popup* icon button.
   *
   * For localization purposes, you can use the provided [translations](/material-ui/guides/localization/).
   * @default 'Open'
   */
  openText: _propTypes.default.string,
  /**
   * Array of options.
   */
  options: _propTypes.default.array.isRequired,
  /**
   * The input placeholder
   */
  placeholder: _propTypes.default.string,
  /**
   * The icon to display in place of the default popup icon.
   * @default <ArrowDropDownIcon />
   */
  popupIcon: _propTypes.default.node,
  /**
   * If `true`, the component becomes readonly. It is also supported for multiple tags where the tag cannot be deleted.
   * @default false
   */
  readOnly: _propTypes.default.bool,
  /**
   * Render the group.
   *
   * @param {AutocompleteRenderGroupParams} params The group to render.
   * @returns {ReactNode}
   */
  renderGroup: _propTypes.default.func,
  /**
   * Render the option, use `getOptionLabel` by default.
   *
   * @param {object} props The props to apply on the li element.
   * @param {T} option The option to render.
   * @param {object} state The state of the component.
   * @returns {ReactNode}
   */
  renderOption: _propTypes.default.func,
  /**
   * Render the selected value.
   *
   * @param {T[]} value The `value` provided to the component.
   * @param {function} getTagProps A tag props getter.
   * @param {object} ownerState The state of the Autocomplete component.
   * @returns {ReactNode}
   */
  renderTags: _propTypes.default.func,
  /**
   * If `true`, the `input` element is required.
   * The prop defaults to the value (`false`) inherited from the parent FormControl component.
   */
  required: _propTypes.default.bool,
  /**
   * If `true`, the input's text is selected on focus.
   * It helps the user clear the selected value.
   * @default !props.freeSolo
   */
  selectOnFocus: _propTypes.default.bool,
  /**
   * The size of the component.
   * @default 'md'
   */
  size: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['sm', 'md', 'lg']), _propTypes.default.string]),
  /**
   * The props used for each slot inside.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    clearIndicator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    endDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    input: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    limitTag: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    listbox: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    loading: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    noOptions: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    option: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    popupIndicator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    startDecorator: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    wrapper: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside.
   * @default {}
   */
  slots: _propTypes.default.shape({
    clearIndicator: _propTypes.default.elementType,
    endDecorator: _propTypes.default.elementType,
    input: _propTypes.default.elementType,
    limitTag: _propTypes.default.elementType,
    listbox: _propTypes.default.elementType,
    loading: _propTypes.default.elementType,
    noOptions: _propTypes.default.elementType,
    option: _propTypes.default.elementType,
    popupIndicator: _propTypes.default.elementType,
    root: _propTypes.default.elementType,
    startDecorator: _propTypes.default.elementType,
    wrapper: _propTypes.default.elementType
  }),
  /**
   * Leading adornment for this input.
   */
  startDecorator: _propTypes.default.node,
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * Type of the `input` element. It should be [a valid HTML5 input type](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Form_%3Cinput%3E_types).
   */
  type: _propTypes.default.string,
  /**
   * The value of the autocomplete.
   *
   * The value must have reference equality with the option in order to be selected.
   * You can customize the equality behavior with the `isOptionEqualToValue` prop.
   */
  value: (0, _utils.chainPropTypes)(_propTypes.default.any, props => {
    if (props.multiple && props.value !== undefined && !Array.isArray(props.value)) {
      return new Error(['MUI: The Autocomplete expects the `value` prop to be an array when `multiple={true}` or undefined.', `However, ${props.value} was provided.`].join('\n'));
    }
    return null;
  }),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'outlined'
   */
  variant: _propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid'])
} : void 0;
var _default = exports.default = Autocomplete;