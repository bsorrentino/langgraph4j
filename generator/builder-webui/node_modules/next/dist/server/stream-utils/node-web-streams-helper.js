"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
0 && (module.exports = {
    streamToBufferedResult: null,
    cloneTransformStream: null,
    chainStreams: null,
    streamFromString: null,
    streamToString: null,
    createBufferedTransformStream: null,
    createInsertedHTMLStream: null,
    renderToInitialFizzStream: null,
    createRootLayoutValidatorStream: null,
    continueFizzStream: null
});
function _export(target, all) {
    for(var name in all)Object.defineProperty(target, name, {
        enumerable: true,
        get: all[name]
    });
}
_export(exports, {
    streamToBufferedResult: function() {
        return streamToBufferedResult;
    },
    cloneTransformStream: function() {
        return cloneTransformStream;
    },
    chainStreams: function() {
        return chainStreams;
    },
    streamFromString: function() {
        return streamFromString;
    },
    streamToString: function() {
        return streamToString;
    },
    createBufferedTransformStream: function() {
        return createBufferedTransformStream;
    },
    createInsertedHTMLStream: function() {
        return createInsertedHTMLStream;
    },
    renderToInitialFizzStream: function() {
        return renderToInitialFizzStream;
    },
    createRootLayoutValidatorStream: function() {
        return createRootLayoutValidatorStream;
    },
    continueFizzStream: function() {
        return continueFizzStream;
    }
});
const _nonnullable = require("../../lib/non-nullable");
const _tracer = require("../lib/trace/tracer");
const _constants = require("../lib/trace/constants");
const _encodedecode = require("./encode-decode");
const queueTask = process.env.NEXT_RUNTIME === "edge" ? globalThis.setTimeout : setImmediate;
const streamToBufferedResult = async (renderResult)=>{
    const textDecoder = new TextDecoder();
    let concatenatedString = "";
    const writable = {
        write (chunk) {
            concatenatedString += (0, _encodedecode.decodeText)(chunk, textDecoder);
        },
        end () {},
        // We do not support stream cancellation
        on () {},
        off () {}
    };
    await renderResult.pipe(writable);
    return concatenatedString;
};
function cloneTransformStream(source) {
    const sourceReader = source.readable.getReader();
    const clone = new TransformStream({
        async start (controller) {
            while(true){
                const { done, value } = await sourceReader.read();
                if (done) {
                    break;
                }
                controller.enqueue(value);
            }
        },
        // skip all piped chunks
        transform () {}
    });
    return clone;
}
function chainStreams(streams) {
    const { readable, writable } = new TransformStream();
    let promise = Promise.resolve();
    for(let i = 0; i < streams.length; ++i){
        promise = promise.then(()=>streams[i].pipeTo(writable, {
                preventClose: i + 1 < streams.length
            }));
    }
    return readable;
}
function streamFromString(str) {
    return new ReadableStream({
        start (controller) {
            controller.enqueue((0, _encodedecode.encodeText)(str));
            controller.close();
        }
    });
}
async function streamToString(stream) {
    const reader = stream.getReader();
    const textDecoder = new TextDecoder();
    let bufferedString = "";
    while(true){
        const { done, value } = await reader.read();
        if (done) {
            return bufferedString;
        }
        bufferedString += (0, _encodedecode.decodeText)(value, textDecoder);
    }
}
function createBufferedTransformStream() {
    let bufferedBytes = new Uint8Array();
    let pendingFlush = null;
    const flushBuffer = (controller)=>{
        if (!pendingFlush) {
            pendingFlush = new Promise((resolve)=>{
                queueTask(()=>{
                    controller.enqueue(bufferedBytes);
                    bufferedBytes = new Uint8Array();
                    pendingFlush = null;
                    resolve();
                });
            });
        }
    };
    return new TransformStream({
        transform (chunk, controller) {
            const newBufferedBytes = new Uint8Array(bufferedBytes.length + chunk.byteLength);
            newBufferedBytes.set(bufferedBytes);
            newBufferedBytes.set(chunk, bufferedBytes.length);
            bufferedBytes = newBufferedBytes;
            flushBuffer(controller);
        },
        flush () {
            if (pendingFlush) {
                return pendingFlush;
            }
        }
    });
}
function createInsertedHTMLStream(getServerInsertedHTML) {
    return new TransformStream({
        async transform (chunk, controller) {
            const insertedHTMLChunk = (0, _encodedecode.encodeText)(await getServerInsertedHTML());
            controller.enqueue(insertedHTMLChunk);
            controller.enqueue(chunk);
        }
    });
}
function renderToInitialFizzStream({ ReactDOMServer, element, streamOptions }) {
    return (0, _tracer.getTracer)().trace(_constants.AppRenderSpan.renderToReadableStream, async ()=>ReactDOMServer.renderToReadableStream(element, streamOptions));
}
function createHeadInsertionTransformStream(insert) {
    let inserted = false;
    let freezing = false;
    const textDecoder = new TextDecoder();
    return new TransformStream({
        async transform (chunk, controller) {
            // While react is flushing chunks, we don't apply insertions
            if (freezing) {
                controller.enqueue(chunk);
                return;
            }
            const insertion = await insert();
            if (inserted) {
                controller.enqueue((0, _encodedecode.encodeText)(insertion));
                controller.enqueue(chunk);
                freezing = true;
            } else {
                const content = (0, _encodedecode.decodeText)(chunk, textDecoder);
                const index = content.indexOf("</head>");
                if (index !== -1) {
                    const insertedHeadContent = content.slice(0, index) + insertion + content.slice(index);
                    controller.enqueue((0, _encodedecode.encodeText)(insertedHeadContent));
                    freezing = true;
                    inserted = true;
                }
            }
            if (!inserted) {
                controller.enqueue(chunk);
            } else {
                queueTask(()=>{
                    freezing = false;
                });
            }
        },
        async flush (controller) {
            // Check before closing if there's anything remaining to insert.
            const insertion = await insert();
            if (insertion) {
                controller.enqueue((0, _encodedecode.encodeText)(insertion));
            }
        }
    });
}
// Suffix after main body content - scripts before </body>,
// but wait for the major chunks to be enqueued.
function createDeferredSuffixStream(suffix) {
    let suffixFlushed = false;
    let suffixFlushTask = null;
    return new TransformStream({
        transform (chunk, controller) {
            controller.enqueue(chunk);
            if (!suffixFlushed && suffix.length) {
                suffixFlushed = true;
                suffixFlushTask = new Promise((res)=>{
                    // NOTE: streaming flush
                    // Enqueue suffix part before the major chunks are enqueued so that
                    // suffix won't be flushed too early to interrupt the data stream
                    queueTask(()=>{
                        controller.enqueue((0, _encodedecode.encodeText)(suffix));
                        res();
                    });
                });
            }
        },
        flush (controller) {
            if (suffixFlushTask) return suffixFlushTask;
            if (!suffixFlushed && suffix.length) {
                suffixFlushed = true;
                controller.enqueue((0, _encodedecode.encodeText)(suffix));
            }
        }
    });
}
// Merge two streams into one. Ensure the final transform stream is closed
// when both are finished.
function createMergedTransformStream(dataStream) {
    let dataStreamFinished = null;
    return new TransformStream({
        transform (chunk, controller) {
            controller.enqueue(chunk);
            if (!dataStreamFinished) {
                const dataStreamReader = dataStream.getReader();
                // NOTE: streaming flush
                // We are buffering here for the inlined data stream because the
                // "shell" stream might be chunkenized again by the underlying stream
                // implementation, e.g. with a specific high-water mark. To ensure it's
                // the safe timing to pipe the data stream, this extra tick is
                // necessary.
                dataStreamFinished = new Promise((res)=>// We use `setTimeout` here to ensure that it's inserted after flushing
                    // the shell. Note that this implementation might get stale if impl
                    // details of Fizz change in the future.
                    // Also we are not using `setImmediate` here because it's not available
                    // broadly in all runtimes, for example some edge workers might not
                    // have it.
                    setTimeout(async ()=>{
                        try {
                            while(true){
                                const { done, value } = await dataStreamReader.read();
                                if (done) {
                                    return res();
                                }
                                controller.enqueue(value);
                            }
                        } catch (err) {
                            controller.error(err);
                        }
                        res();
                    }, 0));
            }
        },
        flush () {
            if (dataStreamFinished) {
                return dataStreamFinished;
            }
        }
    });
}
/**
 * This transform stream moves the suffix to the end of the stream, so results
 * like `</body></html><script>...</script>` will be transformed to
 * `<script>...</script></body></html>`.
 */ function createMoveSuffixStream(suffix) {
    let foundSuffix = false;
    const textDecoder = new TextDecoder();
    // Remove suffix from the stream, and enqueue it back in flush
    return new TransformStream({
        transform (chunk, controller) {
            if (!suffix || foundSuffix) {
                return controller.enqueue(chunk);
            }
            const content = (0, _encodedecode.decodeText)(chunk, textDecoder);
            if (content.endsWith(suffix)) {
                foundSuffix = true;
                const contentWithoutSuffix = content.slice(0, -suffix.length);
                controller.enqueue((0, _encodedecode.encodeText)(contentWithoutSuffix));
            } else {
                controller.enqueue(chunk);
            }
        },
        flush (controller) {
            if (suffix) {
                controller.enqueue((0, _encodedecode.encodeText)(suffix));
            }
        }
    });
}
function createRootLayoutValidatorStream(assetPrefix = "", getTree) {
    let foundHtml = false;
    let foundBody = false;
    const textDecoder = new TextDecoder();
    return new TransformStream({
        async transform (chunk, controller) {
            if (!foundHtml || !foundBody) {
                const content = (0, _encodedecode.decodeText)(chunk, textDecoder);
                if (!foundHtml && content.includes("<html")) {
                    foundHtml = true;
                }
                if (!foundBody && content.includes("<body")) {
                    foundBody = true;
                }
            }
            controller.enqueue(chunk);
        },
        flush (controller) {
            // If html or body tag is missing, we need to inject a script to notify
            // the client.
            if (!foundHtml || !foundBody) {
                const missingTags = [
                    foundHtml ? null : "html",
                    foundBody ? null : "body"
                ].filter(_nonnullable.nonNullable);
                controller.enqueue((0, _encodedecode.encodeText)(`<script>self.__next_root_layout_missing_tags_error=${JSON.stringify({
                    missingTags,
                    assetPrefix: assetPrefix ?? "",
                    tree: getTree()
                })}</script>`));
            }
        }
    });
}
async function continueFizzStream(renderStream, { suffix, inlinedDataStream, generateStaticHTML, getServerInsertedHTML, serverInsertedHTMLToHead, validateRootLayout }) {
    const closeTag = "</body></html>";
    // Suffix itself might contain close tags at the end, so we need to split it.
    const suffixUnclosed = suffix ? suffix.split(closeTag)[0] : null;
    if (generateStaticHTML) {
        await renderStream.allReady;
    }
    const transforms = [
        // Buffer everything to avoid flushing too frequently
        createBufferedTransformStream(),
        // Insert generated tags to head
        getServerInsertedHTML && !serverInsertedHTMLToHead ? createInsertedHTMLStream(getServerInsertedHTML) : null,
        // Insert suffix content
        suffixUnclosed != null ? createDeferredSuffixStream(suffixUnclosed) : null,
        // Insert the inlined data (Flight data, form state, etc.) stream into the HTML
        inlinedDataStream ? createMergedTransformStream(inlinedDataStream) : null,
        // Close tags should always be deferred to the end
        createMoveSuffixStream(closeTag),
        // Special head insertions
        // TODO-APP: Insert server side html to end of head in app layout rendering, to avoid
        // hydration errors. Remove this once it's ready to be handled by react itself.
        getServerInsertedHTML && serverInsertedHTMLToHead ? createHeadInsertionTransformStream(getServerInsertedHTML) : null,
        validateRootLayout ? createRootLayoutValidatorStream(validateRootLayout.assetPrefix, validateRootLayout.getTree) : null
    ].filter(_nonnullable.nonNullable);
    return transforms.reduce((readable, transform)=>readable.pipeThrough(transform), renderStream);
}

//# sourceMappingURL=node-web-streams-helper.js.map