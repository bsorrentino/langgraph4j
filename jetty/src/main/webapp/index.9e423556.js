var e=globalThis,t={},r={},s=e.parcelRequire3bab;null==s&&((s=function(e){if(e in t)return t[e].exports;if(e in r){var s=r[e];delete r[e];var l={id:e,exports:{}};return t[e]=l,s.call(l.exports,l,l.exports),l.exports}var o=Error("Cannot find module '"+e+"'");throw o.code="MODULE_NOT_FOUND",o}).register=function(e,t){r[e]=t},e.parcelRequire3bab=s),s.register;var l=s("hNeh9"),o=s("800sp");class i extends o.LitElement{static styles=[l.default,(0,o.css)`
    .container {
      display: flex;
      flex-direction: row;
    }
    `];static properties={};constructor(){super(),this.results=[]}get executor(){let e=this.parentElement.querySelector("lg4j-executor");if(!e)throw Error("element lg4j-executor not found!");return e}connectedCallback(){super.connectedCallback(),this.executor.addEventListener("result",this.#e)}disconnectedCallback(){super.disconnectedCallback(),this.executor.removeEventListener("result",this.#e)}#t(e){return(0,o.html)`
    <div class="collapse collapse-arrow join-item border-base-300 border">
      <input type="radio" name="my-accordion-4" checked="checked" />
      <div class="collapse-title text-xl font-medium">Click to open this one and close others</div>
      <div class="collapse-content">
        <p>${e}</p>
      </div>
    </div>
    `}#e=e=>{console.log("onResult",e),this.results.push(e.detail),this.requestUpdate()};render(){return(0,o.html)`
      <div class="join join-vertical w-full">
      ${this.results.map(e=>this.#t(e))}
      </div>
    `}}window.customElements.define("lg4j-output",i);
//# sourceMappingURL=index.9e423556.js.map
