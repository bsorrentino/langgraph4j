// ISC License
// Copyright (c) 2021 Alexey Raspopov, Kostiantyn Denysov, Anton Verinov
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
//
// https://github.com/alexeyraspopov/picocolors/blob/b6261487e7b81aaab2440e397a356732cad9e342/picocolors.js#L1
"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    reset: null,
    bold: null,
    dim: null,
    italic: null,
    underline: null,
    inverse: null,
    hidden: null,
    strikethrough: null,
    black: null,
    red: null,
    green: null,
    yellow: null,
    blue: null,
    magenta: null,
    purple: null,
    cyan: null,
    white: null,
    gray: null,
    bgBlack: null,
    bgRed: null,
    bgGreen: null,
    bgYellow: null,
    bgBlue: null,
    bgMagenta: null,
    bgCyan: null,
    bgWhite: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    reset: function() {
        return reset;
    },
    bold: function() {
        return bold;
    },
    dim: function() {
        return dim;
    },
    italic: function() {
        return italic;
    },
    underline: function() {
        return underline;
    },
    inverse: function() {
        return inverse;
    },
    hidden: function() {
        return hidden;
    },
    strikethrough: function() {
        return strikethrough;
    },
    black: function() {
        return black;
    },
    red: function() {
        return red;
    },
    green: function() {
        return green;
    },
    yellow: function() {
        return yellow;
    },
    blue: function() {
        return blue;
    },
    magenta: function() {
        return magenta;
    },
    purple: function() {
        return purple;
    },
    cyan: function() {
        return cyan;
    },
    white: function() {
        return white;
    },
    gray: function() {
        return gray;
    },
    bgBlack: function() {
        return bgBlack;
    },
    bgRed: function() {
        return bgRed;
    },
    bgGreen: function() {
        return bgGreen;
    },
    bgYellow: function() {
        return bgYellow;
    },
    bgBlue: function() {
        return bgBlue;
    },
    bgMagenta: function() {
        return bgMagenta;
    },
    bgCyan: function() {
        return bgCyan;
    },
    bgWhite: function() {
        return bgWhite;
    }
});
var _globalThis;
const { env, stdout } = ((_globalThis = globalThis) == null ? void 0 : _globalThis.process) ?? {};
const enabled = env && !env.NO_COLOR && (env.FORCE_COLOR || (stdout == null ? void 0 : stdout.isTTY) && !env.CI && env.TERM !== "dumb");
const replaceClose = (str, close, replace, index)=>{
    const start = str.substring(0, index) + replace;
    const end = str.substring(index + close.length);
    const nextIndex = end.indexOf(close);
    return ~nextIndex ? start + replaceClose(end, close, replace, nextIndex) : start + end;
};
const formatter = (open, close, replace = open)=>(input)=>{
        const string = "" + input;
        const index = string.indexOf(close, open.length);
        return ~index ? open + replaceClose(string, close, replace, index) + close : open + string + close;
    };
const reset = enabled ? (s)=>`\x1b[0m${s}\x1b[0m` : String;
const bold = enabled ? formatter("\x1b[1m", "\x1b[22m", "\x1b[22m\x1b[1m") : String;
const dim = enabled ? formatter("\x1b[2m", "\x1b[22m", "\x1b[22m\x1b[2m") : String;
const italic = enabled ? formatter("\x1b[3m", "\x1b[23m") : String;
const underline = enabled ? formatter("\x1b[4m", "\x1b[24m") : String;
const inverse = enabled ? formatter("\x1b[7m", "\x1b[27m") : String;
const hidden = enabled ? formatter("\x1b[8m", "\x1b[28m") : String;
const strikethrough = enabled ? formatter("\x1b[9m", "\x1b[29m") : String;
const black = enabled ? formatter("\x1b[30m", "\x1b[39m") : String;
const red = enabled ? formatter("\x1b[31m", "\x1b[39m") : String;
const green = enabled ? formatter("\x1b[32m", "\x1b[39m") : String;
const yellow = enabled ? formatter("\x1b[33m", "\x1b[39m") : String;
const blue = enabled ? formatter("\x1b[34m", "\x1b[39m") : String;
const magenta = enabled ? formatter("\x1b[35m", "\x1b[39m") : String;
const purple = enabled ? formatter("\x1b[38;2;173;127;168m", "\x1b[39m") : String;
const cyan = enabled ? formatter("\x1b[36m", "\x1b[39m") : String;
const white = enabled ? formatter("\x1b[37m", "\x1b[39m") : String;
const gray = enabled ? formatter("\x1b[90m", "\x1b[39m") : String;
const bgBlack = enabled ? formatter("\x1b[40m", "\x1b[49m") : String;
const bgRed = enabled ? formatter("\x1b[41m", "\x1b[49m") : String;
const bgGreen = enabled ? formatter("\x1b[42m", "\x1b[49m") : String;
const bgYellow = enabled ? formatter("\x1b[43m", "\x1b[49m") : String;
const bgBlue = enabled ? formatter("\x1b[44m", "\x1b[49m") : String;
const bgMagenta = enabled ? formatter("\x1b[45m", "\x1b[49m") : String;
const bgCyan = enabled ? formatter("\x1b[46m", "\x1b[49m") : String;
const bgWhite = enabled ? formatter("\x1b[47m", "\x1b[49m") : String;

//# sourceMappingURL=picocolors.js.map