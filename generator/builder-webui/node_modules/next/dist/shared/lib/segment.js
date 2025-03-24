"use strict";
Object.defineProperty(exports, "__esModule", {
    value: true
});
Object.defineProperty(exports, "isGroupSegment", {
    enumerable: true,
    get: function() {
        return isGroupSegment;
    }
});
function isGroupSegment(segment) {
    // Use array[0] for performant purpose
    return segment[0] === "(" && segment.endsWith(")");
}

//# sourceMappingURL=segment.js.map