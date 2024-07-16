var e=globalThis,t={},a={},s=e.parcelRequire3bab;null==s&&((s=function(e){if(e in t)return t[e].exports;if(e in a){var s=a[e];delete a[e];var r={id:e,exports:{}};return t[e]=r,s.call(r.exports,r,r.exports),r.exports}var i=Error("Cannot find module '"+e+"'");throw i.code="MODULE_NOT_FOUND",i}).register=function(e,t){a[e]=t},e.parcelRequire3bab=s),s.register;var r=s("hNeh9"),i=s("800sp");async function*o(e){let t=e.body.getReader();for(;;){let{done:e,value:a}=await t.read();if(e)break;yield new TextDecoder().decode(a)}}class n extends i.LitElement{static styles=[r.default,(0,i.css)`
    .container {
      display: flex;
      flex-direction: column;
      row-gap: 10px;
    }
  `];static properties={url:{},test:{type:Boolean}};constructor(){super(),this.test=!1,this.formMetaData={}}connectedCallback(){super.connectedCallback(),this.test?setTimeout(()=>{this.dispatchEvent(new CustomEvent("graph",{detail:`
            flowchart TD
            Start --> Stop
            `,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData={input:{type:"string",required:!0}},this.requestUpdate()},1e3):this.#e()}async #e(){let e=await fetch(`${this.url}/init`),t=await e.json();console.debug(t),this.dispatchEvent(new CustomEvent("graph",{detail:t.graph,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=t.args,this.requestUpdate()}render(){return console.debug("render",this.formMetaData),(0,i.html)`
        <div class="container">
          ${Object.entries(this.formMetaData).map(([e,t])=>(0,i.html)`<textarea id="${e}" class="textarea textarea-primary" placeholder="${e}"></textarea>`)}
          <button @click="${this.#t}" class="btn btn-primary">Submit</button>
        </div>
    `}async #t(){if(this.test){setTimeout(()=>{this.dispatchEvent(new CustomEvent("result",{detail:{node:"node1",state:{property1:"value1",property2:"value2"}},bubbles:!0,composed:!0,cancelable:!0}))},1e3);return}let e=Object.keys(this.formMetaData).reduce((e,t)=>(e[t]=this.shadowRoot.getElementById(t).value,e),{});for await(let t of o(await fetch(`${this.url}/stream`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(e)})))console.debug(t),this.dispatchEvent(new CustomEvent("result",{detail:JSON.parse(t),bubbles:!0,composed:!0,cancelable:!0}))}}window.customElements.define("lg4j-executor",n);
//# sourceMappingURL=index.4c99d4d4.js.map
