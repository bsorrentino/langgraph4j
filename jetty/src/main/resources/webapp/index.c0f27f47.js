var e=globalThis,t={},a={},s=e.parcelRequire3bab;null==s&&((s=function(e){if(e in t)return t[e].exports;if(e in a){var s=a[e];delete a[e];var n={id:e,exports:{}};return t[e]=n,s.call(n.exports,n,n.exports),n.exports}var r=Error("Cannot find module '"+e+"'");throw r.code="MODULE_NOT_FOUND",r}).register=function(e,t){a[e]=t},e.parcelRequire3bab=s),s.register;var n=s("hNeh9"),r=s("800sp");const o=async e=>new Promise(t=>setTimeout(t,e));async function*i(e){let t=e.body.getReader();for(;;){let{done:e,value:a}=await t.read();if(e)break;yield new TextDecoder().decode(a)}}class l extends r.LitElement{static styles=[n.default,(0,r.css)`
    .container {
      display: flex;
      flex-direction: column;
      row-gap: 10px;
    }
  `];static properties={url:{},test:{type:Boolean}};constructor(){super(),this.test=!1,this.formMetaData={}}connectedCallback(){if(super.connectedCallback(),this.test){this.#e();return}this.#t()}async #t(){let e=await fetch(`${this.url}/init`),t=await e.json();console.debug(t),this.dispatchEvent(new CustomEvent("graph",{detail:t.graph,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=t.args,this.requestUpdate()}async #e(){await o(1e3),this.dispatchEvent(new CustomEvent("graph",{detail:`
      flowchart TD
        start((start))
	      stop((stop))
        node1("node1")
        node2("node2")

        start:::start --> node1:::node1
        node1:::node1 --> node2:::node2
        node2:::node2 --> stop:::stop
      `,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData={input:{type:"string",required:!0}},this.requestUpdate()}render(){return console.debug("render",this.formMetaData),(0,r.html)`
        <div class="container">
          ${Object.entries(this.formMetaData).map(([e,t])=>(0,r.html)`<textarea id="${e}" class="textarea textarea-primary" placeholder="${e}"></textarea>`)}
          <button @click="${this.#a}" class="btn btn-primary">Submit</button>
        </div>
    `}async #a(){if(this.test){await this.#s();return}let e=Object.keys(this.formMetaData).reduce((e,t)=>(e[t]=this.shadowRoot.getElementById(t).value,e),{});for await(let t of i(await fetch(`${this.url}/stream`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(e)})))console.debug(t),this.dispatchEvent(new CustomEvent("result",{detail:JSON.parse(t),bubbles:!0,composed:!0,cancelable:!0}))}async #s(){let e=async e=>{await o(1e3),this.dispatchEvent(new CustomEvent("result",{detail:{node:e,state:{property1:"value1",property2:"value2"}},bubbles:!0,composed:!0,cancelable:!0}))};await e("node1"),await e("node2")}}window.customElements.define("lg4j-executor",l);
//# sourceMappingURL=index.c0f27f47.js.map
