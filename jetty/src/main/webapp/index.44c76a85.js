var e=globalThis,t={},s={},r=e.parcelRequire3bab;null==r&&((r=function(e){if(e in t)return t[e].exports;if(e in s){var r=s[e];delete s[e];var l={id:e,exports:{}};return t[e]=l,r.call(l.exports,l,l.exports),l.exports}var i=Error("Cannot find module '"+e+"'");throw i.code="MODULE_NOT_FOUND",i}).register=function(e,t){s[e]=t},e.parcelRequire3bab=r),r.register;var l=r("hNeh9"),i=r("800sp");class n extends i.LitElement{static styles=[l.default,(0,i.css)`
    .container {
      display: flex;
      flex-direction: row;
    }
    `];static properties={};constructor(){super(),this.results=[]}connectedCallback(){super.connectedCallback(),this.addEventListener("result",this.#e)}disconnectedCallback(){super.disconnectedCallback(),this.removeEventListener("result",this.#e)}#t(e){return(0,i.html)`
    <div class="collapse collapse-arrow join-item border-base-300 border">
      <input type="radio" name="my-accordion-4" checked="checked" />
      <div class="collapse-title text-xl font-medium">Click to open this one and close others</div>
      <div class="collapse-content">
        <p>${e}</p>
      </div>
    </div>
    `}#e=e=>{console.debug("onResult",e),this.results.push(e.detail),this.requestUpdate()};render(){return(0,i.html)`
      <div class="join join-vertical w-full">
      ${this.results.map(e=>this.#t(e))}
      </div>
    `}}window.customElements.define("lg4j-result",n);
//# sourceMappingURL=index.44c76a85.js.map
