var e=globalThis,t={},s={},l=e.parcelRequire3bab;null==l&&((l=function(e){if(e in t)return t[e].exports;if(e in s){var l=s[e];delete s[e];var r={id:e,exports:{}};return t[e]=r,l.call(r.exports,r,r.exports),r.exports}var a=Error("Cannot find module '"+e+"'");throw a.code="MODULE_NOT_FOUND",a}).register=function(e,t){s[e]=t},e.parcelRequire3bab=l),l.register;var r=l("hNeh9"),a=l("800sp");class i extends a.LitElement{static styles=[r.default,(0,a.css)``];static properties={};constructor(){super(),this.results=[]}connectedCallback(){super.connectedCallback(),this.addEventListener("result",this.#e)}disconnectedCallback(){super.disconnectedCallback(),this.removeEventListener("result",this.#e)}#t(e,t){return(0,a.html)`
    <div class="collapse collapse-arrow bg-base-200">
      <input type="radio" name="item-1" checked="checked" />
      <div class="collapse-title text-xm font-medium">${e.node}</div>
      <div class="collapse-content">
        <table class="table">
          <tbody>
            ${Object.entries(e.state).map(([e,t])=>(0,a.html)`
              <tr>
                <td width="30%">${e}</td>
                <td width="70%">${t}</td>
              </tr>
            `)}
          </tbody>
        </table>
      </div>
    </div>
    `}#e=e=>{let{detail:t}=e;console.debug("onResult",e),this.results.push(t),this.dispatchEvent(new CustomEvent("graph-active",{detail:t.node,bubbles:!0,composed:!0,cancelable:!0})),this.requestUpdate()};render(){return(0,a.html)`
      <div class="flex flex-col gap-y-1.5 mx-2 mt-2">
      ${this.results.map((e,t)=>this.#t(e,t))}
      </div>
    `}}window.customElements.define("lg4j-result",i);
//# sourceMappingURL=index.67fe913c.js.map
