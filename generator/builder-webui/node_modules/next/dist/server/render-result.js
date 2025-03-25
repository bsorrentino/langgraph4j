"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function() {
        return RenderResult;
    }
});
const _pipereadable = require("./pipe-readable");
class RenderResult {
    /**
   * Creates a new RenderResult instance from a static response.
   *
   * @param value the static response value
   * @returns a new RenderResult instance
   */ static fromStatic(value) {
        return new RenderResult(value);
    }
    constructor(response, { contentType, waitUntil, ...metadata } = {}){
        this.response = response;
        this.contentType = contentType;
        this.metadata = metadata;
        this.waitUntil = waitUntil;
    }
    extendMetadata(metadata) {
        Object.assign(this.metadata, metadata);
    }
    /**
   * Returns true if the response is null. It can be null if the response was
   * not found or was already sent.
   */ get isNull() {
        return this.response === null;
    }
    /**
   * Returns false if the response is a string. It can be a string if the page
   * was prerendered. If it's not, then it was generated dynamically.
   */ get isDynamic() {
        return typeof this.response !== "string";
    }
    /**
   * Returns true if the response is a stream. If the page was dynamic, this
   * will throw an error.
   *
   * @returns The response as a string
   */ toUnchunkedString() {
        if (typeof this.response !== "string") {
            throw new Error("Invariant: dynamic responses cannot be unchunked. This is a bug in Next.js");
        }
        return this.response;
    }
    async pipe(res) {
        if (this.response === null) {
            throw new Error("Invariant: response is null. This is a bug in Next.js");
        }
        if (typeof this.response === "string") {
            throw new Error("Invariant: static responses cannot be piped. This is a bug in Next.js");
        }
        return await (0, _pipereadable.pipeReadable)(this.response, res, this.waitUntil);
    }
}

//# sourceMappingURL=render-result.js.map