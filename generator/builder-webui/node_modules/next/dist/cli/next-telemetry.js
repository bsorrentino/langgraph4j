#!/usr/bin/env node
"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "nextTelemetry", {
    enumerable: true,
    get: function() {
        return nextTelemetry;
    }
});
const _picocolors = require("../lib/picocolors");
const _storage = require("../telemetry/storage");
const nextTelemetry = (args)=>{
    if (args["--help"]) {
        console.log(`
      Description
        Allows you to control Next.js' telemetry collection

      Usage
        $ next telemetry [enable/disable]

      You may pass the 'enable' or 'disable' argument to turn Next.js' telemetry collection on or off.

      Options
       --enable    Enables Next.js' telemetry collection
       --disable   Disables Next.js' telemetry collection
       --help, -h  Displays this message

      Learn more: ${(0, _picocolors.cyan)("https://nextjs.org/telemetry")}
    `);
        return;
    }
    const telemetry = new _storage.Telemetry({
        distDir: process.cwd()
    });
    let isEnabled = telemetry.isEnabled;
    if (args["--enable"] || args._[0] === "enable") {
        telemetry.setEnabled(true);
        console.log((0, _picocolors.cyan)("Success!"));
        console.log();
        isEnabled = true;
    } else if (args["--disable"] || args._[0] === "disable") {
        const path = telemetry.setEnabled(false);
        if (isEnabled) {
            console.log((0, _picocolors.cyan)(`Your preference has been saved${path ? ` to ${path}` : ""}.`));
        } else {
            console.log((0, _picocolors.yellow)(`Next.js' telemetry collection is already disabled.`));
        }
        console.log();
        isEnabled = false;
    } else {
        console.log((0, _picocolors.bold)("Next.js Telemetry"));
        console.log();
    }
    console.log(`Status: ${isEnabled ? (0, _picocolors.bold)((0, _picocolors.green)("Enabled")) : (0, _picocolors.bold)((0, _picocolors.red)("Disabled"))}`);
    console.log();
    if (isEnabled) {
        console.log(`Next.js telemetry is completely anonymous. Thank you for participating!`);
    } else {
        console.log(`You have opted-out of Next.js' anonymous telemetry program.`);
        console.log(`No data will be collected from your machine.`);
    }
    console.log(`Learn more: https://nextjs.org/telemetry`);
    console.log();
};

//# sourceMappingURL=next-telemetry.js.map