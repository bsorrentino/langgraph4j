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
var _clsx = _interopRequireDefault(require("clsx"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _base = require("@mui/base");
var _styles = require("../styles");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _tableClasses = require("./tableClasses");
var _Typography = require("../Typography/Typography");
var _useSlot = _interopRequireDefault(require("../utils/useSlot"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["className", "component", "children", "borderAxis", "hoverRow", "noWrap", "size", "variant", "color", "stripe", "stickyHeader", "stickyFooter", "slots", "slotProps"];
function _getRequireWildcardCache(e) { if ("function" != typeof WeakMap) return null; var r = new WeakMap(), t = new WeakMap(); return (_getRequireWildcardCache = function (e) { return e ? t : r; })(e); }
function _interopRequireWildcard(e, r) { if (!r && e && e.__esModule) return e; if (null === e || "object" != typeof e && "function" != typeof e) return { default: e }; var t = _getRequireWildcardCache(r); if (t && t.has(e)) return t.get(e); var n = { __proto__: null }, a = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var u in e) if ("default" !== u && Object.prototype.hasOwnProperty.call(e, u)) { var i = a ? Object.getOwnPropertyDescriptor(e, u) : null; i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u]; } return n.default = e, t && t.set(e, n), n; }
const useUtilityClasses = ownerState => {
  const {
    size,
    variant,
    color,
    borderAxis,
    stickyHeader,
    stickyFooter,
    noWrap,
    hoverRow
  } = ownerState;
  const slots = {
    root: ['root', stickyHeader && 'stickyHeader', stickyFooter && 'stickyFooter', noWrap && 'noWrap', hoverRow && 'hoverRow', borderAxis && `borderAxis${(0, _utils.unstable_capitalize)(borderAxis)}`, variant && `variant${(0, _utils.unstable_capitalize)(variant)}`, color && `color${(0, _utils.unstable_capitalize)(color)}`, size && `size${(0, _utils.unstable_capitalize)(size)}`]
  };
  return (0, _base.unstable_composeClasses)(slots, _tableClasses.getTableUtilityClass, {});
};
const tableSelector = {
  /**
   * According to https://www.w3.org/TR/2014/REC-html5-20141028/tabular-data.html#the-tr-element,
   * `tr` can only have `td | th` as children, so using :first-of-type is better than :first-child to prevent emotion SSR warning
   */
  getColumnExceptFirst() {
    return '& tr > *:not(:first-of-type), & tr > th + td, & tr > td + th';
  },
  /**
   * Every cell in the table
   */
  getCell() {
    return '& th, & td';
  },
  /**
   * `th` cell of the table (could exist in the body)
   */
  getHeadCell() {
    return '& th';
  },
  /**
   * Only the cell of `thead`
   */
  getHeaderCell() {
    return '& thead th';
  },
  getHeaderCellOfRow(row) {
    return `& thead tr:nth-of-type(${row}) th`;
  },
  getBottomHeaderCell() {
    return '& thead th:not([colspan])';
  },
  getHeaderNestedFirstColumn() {
    return '& thead tr:not(:first-of-type) th:not([colspan]):first-of-type';
  },
  /**
   * The body cell that contains data
   */
  getDataCell() {
    return '& td';
  },
  getDataCellExceptLastRow() {
    return '& tr:not(:last-of-type) > td';
  },
  /**
   * The body cell either `td` or `th`
   */
  getBodyCellExceptLastRow() {
    return `${this.getDataCellExceptLastRow()}, & tr:not(:last-of-type) > th[scope="row"]`;
  },
  getBodyCellOfRow(row) {
    if (typeof row === 'number' && row < 0) {
      return `& tbody tr:nth-last-of-type(${Math.abs(row)}) td, & tbody tr:nth-last-of-type(${Math.abs(row)}) th[scope="row"]`;
    }
    return `& tbody tr:nth-of-type(${row}) td, & tbody tr:nth-of-type(${row}) th[scope="row"]`;
  },
  getBodyRow(row) {
    if (row === undefined) {
      return `& tbody tr`;
    }
    return `& tbody tr:nth-of-type(${row})`;
  },
  getFooterCell() {
    return '& tfoot th, & tfoot td';
  },
  getFooterFirstRowCell() {
    return `& tfoot tr:not(:last-of-type) th, & tfoot tr:not(:last-of-type) td`;
  }
};
const TableRoot = (0, _styled.default)('table', {
  name: 'JoyTable',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})(({
  theme,
  ownerState
}) => {
  var _theme$variants, _variantStyle$borderC, _theme$variants2, _ownerState$borderAxi, _ownerState$borderAxi2, _ownerState$borderAxi3, _ownerState$borderAxi4;
  const variantStyle = (_theme$variants = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants[ownerState.color];
  return [(0, _extends2.default)({
    '--Table-headerUnderlineThickness': '2px',
    '--TableCell-borderColor': (_variantStyle$borderC = variantStyle == null ? void 0 : variantStyle.borderColor) != null ? _variantStyle$borderC : theme.vars.palette.divider,
    '--TableCell-headBackground': `var(--Sheet-background, ${theme.vars.palette.background.surface})`
  }, ownerState.size === 'sm' && {
    '--unstable_TableCell-height': 'var(--TableCell-height, 32px)',
    '--TableCell-paddingX': '0.25rem',
    '--TableCell-paddingY': '0.25rem'
  }, ownerState.size === 'md' && {
    '--unstable_TableCell-height': 'var(--TableCell-height, 40px)',
    '--TableCell-paddingX': '0.5rem',
    '--TableCell-paddingY': '0.375rem'
  }, ownerState.size === 'lg' && {
    '--unstable_TableCell-height': 'var(--TableCell-height, 48px)',
    '--TableCell-paddingX': '0.75rem',
    '--TableCell-paddingY': '0.5rem'
  }, {
    tableLayout: 'fixed',
    width: '100%',
    borderSpacing: '0px',
    borderCollapse: 'separate',
    borderRadius: 'var(--TableCell-cornerRadius, var(--unstable_actionRadius))'
  }, theme.typography[`body-${{
    sm: 'xs',
    md: 'sm',
    lg: 'md'
  }[ownerState.size]}`], (_theme$variants2 = theme.variants[ownerState.variant]) == null ? void 0 : _theme$variants2[ownerState.color], {
    '& caption': {
      color: theme.vars.palette.text.tertiary,
      padding: 'calc(2 * var(--TableCell-paddingY)) var(--TableCell-paddingX)'
    },
    [tableSelector.getDataCell()]: (0, _extends2.default)({
      padding: 'var(--TableCell-paddingY) var(--TableCell-paddingX)',
      height: 'var(--unstable_TableCell-height)',
      borderColor: 'var(--TableCell-borderColor)',
      // must come after border bottom
      backgroundColor: 'var(--TableCell-dataBackground)'
    }, ownerState.noWrap && {
      textOverflow: 'ellipsis',
      whiteSpace: 'nowrap',
      overflow: 'hidden'
    }),
    [tableSelector.getHeadCell()]: {
      textAlign: 'left',
      padding: 'var(--TableCell-paddingY) var(--TableCell-paddingX)',
      backgroundColor: 'var(--TableCell-headBackground)',
      // use `background-color` in case the Sheet has gradient background
      height: 'var(--unstable_TableCell-height)',
      fontWeight: theme.vars.fontWeight.lg,
      borderColor: 'var(--TableCell-borderColor)',
      color: theme.vars.palette.text.secondary,
      textOverflow: 'ellipsis',
      whiteSpace: 'nowrap',
      overflow: 'hidden'
    },
    [tableSelector.getHeaderCell()]: {
      verticalAlign: 'bottom',
      // Automatic radius adjustment with Sheet
      '&:first-of-type': {
        borderTopLeftRadius: 'var(--TableCell-cornerRadius, var(--unstable_actionRadius))'
      },
      '&:last-of-type': {
        borderTopRightRadius: 'var(--TableCell-cornerRadius, var(--unstable_actionRadius))'
      }
    },
    '& tfoot tr > *': {
      backgroundColor: `var(--TableCell-footBackground, ${theme.vars.palette.background.level1})`,
      // Automatic radius adjustment with Sheet
      '&:first-of-type': {
        borderBottomLeftRadius: 'var(--TableCell-cornerRadius, var(--unstable_actionRadius))'
      },
      '&:last-of-type': {
        borderBottomRightRadius: 'var(--TableCell-cornerRadius, var(--unstable_actionRadius))'
      }
    }
  }), (((_ownerState$borderAxi = ownerState.borderAxis) == null ? void 0 : _ownerState$borderAxi.startsWith('x')) || ((_ownerState$borderAxi2 = ownerState.borderAxis) == null ? void 0 : _ownerState$borderAxi2.startsWith('both'))) && {
    // insert border between rows
    [tableSelector.getHeaderCell()]: {
      borderBottomWidth: 1,
      borderBottomStyle: 'solid'
    },
    [tableSelector.getBottomHeaderCell()]: {
      borderBottomWidth: 'var(--Table-headerUnderlineThickness)',
      borderBottomStyle: 'solid'
    },
    [tableSelector.getBodyCellExceptLastRow()]: {
      borderBottomWidth: 1,
      borderBottomStyle: 'solid'
    },
    [tableSelector.getFooterCell()]: {
      borderTopWidth: 1,
      borderTopStyle: 'solid'
    }
  }, (((_ownerState$borderAxi3 = ownerState.borderAxis) == null ? void 0 : _ownerState$borderAxi3.startsWith('y')) || ((_ownerState$borderAxi4 = ownerState.borderAxis) == null ? void 0 : _ownerState$borderAxi4.startsWith('both'))) && {
    // insert border between columns
    [`${tableSelector.getColumnExceptFirst()}, ${tableSelector.getHeaderNestedFirstColumn()}`]: {
      borderLeftWidth: 1,
      borderLeftStyle: 'solid'
    }
  }, (ownerState.borderAxis === 'x' || ownerState.borderAxis === 'both') && {
    // insert border at the top of header and bottom of body
    [tableSelector.getHeaderCellOfRow(1)]: {
      borderTopWidth: 1,
      borderTopStyle: 'solid'
    },
    [tableSelector.getBodyCellOfRow(-1)]: {
      borderBottomWidth: 1,
      borderBottomStyle: 'solid'
    },
    [tableSelector.getFooterCell()]: {
      borderBottomWidth: 1,
      borderBottomStyle: 'solid'
    }
  }, (ownerState.borderAxis === 'y' || ownerState.borderAxis === 'both') && {
    // insert border on the left of first column and right of the last column
    '& tr > *:first-of-type': {
      borderLeftWidth: 1,
      borderLeftStyle: 'solid'
    },
    '& tr > *:last-of-type:not(:first-of-type)': {
      borderRightWidth: 1,
      borderRightStyle: 'solid'
    }
  }, ownerState.stripe && {
    [tableSelector.getBodyRow(ownerState.stripe)]: {
      // For customization, a table cell can look for this variable with a fallback value.
      background: `var(--TableRow-stripeBackground, ${theme.vars.palette.background.level2})`,
      color: theme.vars.palette.text.primary
    }
  }, ownerState.hoverRow && {
    [tableSelector.getBodyRow()]: {
      '&:hover': {
        background: `var(--TableRow-hoverBackground, ${theme.vars.palette.background.level3})`
      }
    }
  }, ownerState.stickyHeader && {
    // The column header
    [tableSelector.getHeaderCell()]: {
      position: 'sticky',
      top: 0,
      zIndex: theme.vars.zIndex.table
    },
    [tableSelector.getHeaderCellOfRow(2)]: {
      // support upto 2 rows for the sticky header
      top: 'var(--unstable_TableCell-height)'
    }
  }, ownerState.stickyFooter && {
    // The column header
    [tableSelector.getFooterCell()]: {
      position: 'sticky',
      bottom: 0,
      zIndex: theme.vars.zIndex.table,
      color: theme.vars.palette.text.secondary,
      fontWeight: theme.vars.fontWeight.lg
    },
    [tableSelector.getFooterFirstRowCell()]: {
      // support upto 2 rows for the sticky footer
      bottom: 'var(--unstable_TableCell-height)'
    }
  }];
});
/**
 *
 * Demos:
 *
 * - [Table](https://mui.com/joy-ui/react-table/)
 *
 * API:
 *
 * - [Table API](https://mui.com/joy-ui/api/table/)
 */
const Table = /*#__PURE__*/React.forwardRef(function Table(inProps, ref) {
  const props = (0, _styles.useThemeProps)({
    props: inProps,
    name: 'JoyTable'
  });
  const {
      className,
      component,
      children,
      borderAxis = 'xBetween',
      hoverRow = false,
      noWrap = false,
      size = 'md',
      variant = 'plain',
      color = 'neutral',
      stripe,
      stickyHeader = false,
      stickyFooter = false,
      slots = {},
      slotProps = {}
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const ownerState = (0, _extends2.default)({}, props, {
    borderAxis,
    hoverRow,
    noWrap,
    component,
    size,
    color,
    variant,
    stripe,
    stickyHeader,
    stickyFooter
  });
  const classes = useUtilityClasses(ownerState);
  const externalForwardedProps = (0, _extends2.default)({}, other, {
    component,
    slots,
    slotProps
  });
  const [SlotRoot, rootProps] = (0, _useSlot.default)('root', {
    ref,
    className: (0, _clsx.default)(classes.root, className),
    elementType: TableRoot,
    externalForwardedProps,
    ownerState
  });
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_Typography.TypographyInheritContext.Provider, {
    value: true,
    children: /*#__PURE__*/(0, _jsxRuntime.jsx)(SlotRoot, (0, _extends2.default)({}, rootProps, {
      children: children
    }))
  });
});
process.env.NODE_ENV !== "production" ? Table.propTypes /* remove-proptypes */ = {
  // ┌────────────────────────────── Warning ──────────────────────────────┐
  // │ These PropTypes are generated from the TypeScript type definitions. │
  // │ To update them, edit the TypeScript types and run `pnpm proptypes`. │
  // └─────────────────────────────────────────────────────────────────────┘
  /**
   * The axis to display a border on the table cell.
   * @default 'xBetween'
   */
  borderAxis: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['both', 'bothBetween', 'none', 'x', 'xBetween', 'y', 'yBetween']), _propTypes.default.string]),
  /**
   * Children of the table
   */
  children: _propTypes.default.node,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
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
   * If `true`, the table row will shade on hover.
   * @default false
   */
  hoverRow: _propTypes.default.bool,
  /**
   * If `true`, the body cells will not wrap, but instead will truncate with a text overflow ellipsis.
   *
   * Note: Header cells are always truncated with overflow ellipsis.
   *
   * @default false
   */
  noWrap: _propTypes.default.bool,
  /**
   * The size of the component.
   * It accepts theme values between 'sm' and 'lg'.
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
   * If `true`, the footer always appear at the bottom of the overflow table.
   *
   * ⚠️ It doesn't work with IE11.
   * @default false
   */
  stickyFooter: _propTypes.default.bool,
  /**
   * If `true`, the header always appear at the top of the overflow table.
   *
   * ⚠️ It doesn't work with IE11.
   * @default false
   */
  stickyHeader: _propTypes.default.bool,
  /**
   * The odd or even row of the table body will have subtle background color.
   */
  stripe: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['odd', 'even']), _propTypes.default.string]),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The [global variant](https://mui.com/joy-ui/main-features/global-variants/) to use.
   * @default 'plain'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['outlined', 'plain', 'soft', 'solid']), _propTypes.default.string])
} : void 0;
var _default = exports.default = Table;