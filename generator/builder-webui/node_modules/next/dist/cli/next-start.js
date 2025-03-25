#!/usr/bin/env node
"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "nextStart", {
    enumerable: true,
    get: function() {
        return nextStart;
    }
});
require("../server/lib/cpu-profile");
const _startserver = require("../server/lib/start-server");
const _utils = require("../server/lib/utils");
const _getprojectdir = require("../lib/get-project-dir");
const _getreservedport = require("../lib/helpers/get-reserved-port");
const nextStart = async (args)=>{
    if (args["--help"]) {
        console.log(`
      Description
        Starts the application in production mode.
        The application should be compiled with \`next build\` first.

      Usage
        $ next start <dir> -p <port>

      <dir> represents the directory of the Next.js application.
      If no directory is provided, the current directory will be used.

      Options
        --port, -p          A port number on which to start the application
        --hostname, -H      Hostname on which to start the application (default: 0.0.0.0)
        --keepAliveTimeout  Max milliseconds to wait before closing inactive connections
        --help, -h          Displays this message
    `);
        process.exit(0);
    }
    const dir = (0, _getprojectdir.getProjectDir)(args._[0]);
    const host = args["--hostname"];
    const port = (0, _utils.getPort)(args);
    if ((0, _getreservedport.isPortIsReserved)(port)) {
        (0, _utils.printAndExit)((0, _getreservedport.getReservedPortExplanation)(port), 1);
    }
    const isExperimentalTestProxy = args["--experimental-test-proxy"];
    const keepAliveTimeoutArg = args["--keepAliveTimeout"];
    if (typeof keepAliveTimeoutArg !== "undefined" && (Number.isNaN(keepAliveTimeoutArg) || !Number.isFinite(keepAliveTimeoutArg) || keepAliveTimeoutArg < 0)) {
        (0, _utils.printAndExit)(`Invalid --keepAliveTimeout, expected a non negative number but received "${keepAliveTimeoutArg}"`, 1);
    }
    const keepAliveTimeout = keepAliveTimeoutArg ? Math.ceil(keepAliveTimeoutArg) : undefined;
    await (0, _startserver.startServer)({
        dir,
        isDev: false,
        isExperimentalTestProxy,
        hostname: host,
        port,
        keepAliveTimeout
    });
};

//# sourceMappingURL=next-start.js.map