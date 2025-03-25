// This adds a `Promise.withResolvers` polyfill. This will soon be adopted into
// the spec.
//
// TODO: remove this polyfill when it is adopted into the spec.
//
// https://tc39.es/proposal-promise-with-resolvers/
//
"use strict";
if (!("withResolvers" in Promise) || typeof Promise.withResolvers !== "function") {
    Promise.withResolvers = ()=>{
        let resolvers;
        // Create the promise and assign the resolvers to the object.
        const promise = new Promise((resolve, reject)=>{
            resolvers = {
                resolve,
                reject
            };
        });
        // We know that resolvers is defined because the Promise constructor runs
        // synchronously.
        return {
            promise,
            resolve: resolvers.resolve,
            reject: resolvers.reject
        };
    };
}

//# sourceMappingURL=polyfill-promise-with-resolvers.js.map