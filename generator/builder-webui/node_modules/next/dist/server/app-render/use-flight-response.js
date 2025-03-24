"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "useFlightResponse", {
    enumerable: true,
    get: function() {
        return useFlightResponse;
    }
});
const _encodedecode = require("../stream-utils/encode-decode");
const _htmlescape = require("../htmlescape");
const isEdgeRuntime = process.env.NEXT_RUNTIME === "edge";
const INLINE_FLIGHT_PAYLOAD_BOOTSTRAP = 0;
const INLINE_FLIGHT_PAYLOAD_DATA = 1;
const INLINE_FLIGHT_PAYLOAD_FORM_STATE = 2;
function useFlightResponse(writable, flightStream, clientReferenceManifest, flightResponseRef, formState, nonce) {
    if (flightResponseRef.current !== null) {
        return flightResponseRef.current;
    }
    // react-server-dom-webpack/client.edge must not be hoisted for require cache clearing to work correctly
    let createFromReadableStream;
    // @TODO: investigate why the aliasing for turbopack doesn't pick this up, requiring this runtime check
    if (process.env.TURBOPACK) {
        createFromReadableStream = // eslint-disable-next-line import/no-extraneous-dependencies
        require("react-server-dom-turbopack/client.edge").createFromReadableStream;
    } else {
        createFromReadableStream = // eslint-disable-next-line import/no-extraneous-dependencies
        require("react-server-dom-webpack/client.edge").createFromReadableStream;
    }
    const [renderStream, forwardStream] = flightStream.tee();
    const res = createFromReadableStream(renderStream, {
        ssrManifest: {
            moduleLoading: clientReferenceManifest.moduleLoading,
            moduleMap: isEdgeRuntime ? clientReferenceManifest.edgeSSRModuleMapping : clientReferenceManifest.ssrModuleMapping
        },
        nonce
    });
    flightResponseRef.current = res;
    let bootstrapped = false;
    // We only attach CSS chunks to the inlined data.
    const forwardReader = forwardStream.getReader();
    const writer = writable.getWriter();
    const startScriptTag = nonce ? `<script nonce=${JSON.stringify(nonce)}>` : "<script>";
    const textDecoder = new TextDecoder();
    function read() {
        forwardReader.read().then(({ done, value })=>{
            if (!bootstrapped) {
                bootstrapped = true;
                writer.write((0, _encodedecode.encodeText)(`${startScriptTag}(self.__next_f=self.__next_f||[]).push(${(0, _htmlescape.htmlEscapeJsonString)(JSON.stringify([
                    INLINE_FLIGHT_PAYLOAD_BOOTSTRAP
                ]))});self.__next_f.push(${(0, _htmlescape.htmlEscapeJsonString)(JSON.stringify([
                    INLINE_FLIGHT_PAYLOAD_FORM_STATE,
                    formState
                ]))})</script>`));
            }
            if (done) {
                // Add a setTimeout here because the error component is too small, the first forwardReader.read() read will return the full chunk
                // and then it immediately set flightResponseRef.current as null.
                // react renders the component twice, the second render will run into the state with useFlightResponse where flightResponseRef.current is null,
                // so it tries to render the flight payload again
                setTimeout(()=>{
                    flightResponseRef.current = null;
                });
                writer.close();
            } else {
                const responsePartial = (0, _encodedecode.decodeText)(value, textDecoder);
                const scripts = `${startScriptTag}self.__next_f.push(${(0, _htmlescape.htmlEscapeJsonString)(JSON.stringify([
                    INLINE_FLIGHT_PAYLOAD_DATA,
                    responsePartial
                ]))})</script>`;
                writer.write((0, _encodedecode.encodeText)(scripts));
                read();
            }
        });
    }
    read();
    return res;
}

//# sourceMappingURL=use-flight-response.js.map