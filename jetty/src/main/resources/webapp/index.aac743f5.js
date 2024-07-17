var t=globalThis,e={},a={},s=t.parcelRequire3bab;null==s&&((s=function(t){if(t in e)return e[t].exports;if(t in a){var s=a[t];delete a[t];var i={id:t,exports:{}};return e[t]=i,s.call(i.exports,i,i.exports),i.exports}var n=Error("Cannot find module '"+t+"'");throw n.code="MODULE_NOT_FOUND",n}).register=function(t,e){a[t]=e},t.parcelRequire3bab=s),s.register;var i=s("hNeh9"),n=s("800sp");const o=async t=>new Promise(e=>setTimeout(e,t));async function*r(t){let e=t.body.getReader();for(;;){let{done:t,value:a}=await e.read();if(t)break;yield new TextDecoder().decode(a)}}class l extends n.LitElement{static styles=[i.default,(0,n.css)`
    .container {
      display: flex;
      flex-direction: column;
      row-gap: 10px;
    }
  `];static properties={url:{},test:{type:Boolean}};constructor(){super(),this.test=!1,this.formMetaData={}}connectedCallback(){if(super.connectedCallback(),this.test){this.#t();return}this.#e()}async #e(){let t=await fetch(`${this.url}/init`),e=await t.json();console.debug("initData",e),this.dispatchEvent(new CustomEvent("init",{detail:e,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=e.args,this.requestUpdate()}async #t(){await o(1e3),this.dispatchEvent(new CustomEvent("init",{detail:{title:"LangGraph4j : TEST",graph:`
flowchart TD
  start((start))
  stop((stop))
  node1("node1")
  node2("node2")

  start:::start --> node1:::node1
  node1:::node1 --> node2:::node2
  node2:::node2 --> stop:::stop
      `},bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData={input:{type:"string",required:!0}},this.requestUpdate()}render(){return console.debug("render",this.formMetaData),(0,n.html)`
        <div class="container">
          ${Object.entries(this.formMetaData).map(([t,e])=>(0,n.html)`<textarea id="${t}" class="textarea textarea-primary" placeholder="${t}"></textarea>`)}
          <button @click="${this.#a}" class="btn btn-primary">Submit</button>
        </div>
    `}async #a(){if(this.test){await this.#s();return}let t=Object.keys(this.formMetaData).reduce((t,e)=>(t[e]=this.shadowRoot.getElementById(e).value,t),{});for await(let e of r(await fetch(`${this.url}/stream`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(t)})))console.debug(e),this.dispatchEvent(new CustomEvent("result",{detail:JSON.parse(e),bubbles:!0,composed:!0,cancelable:!0}))}async #s(){let t=async t=>{await o(1e3),this.dispatchEvent(new CustomEvent("result",{detail:{node:t,state:{property1:"value1",property2:"value2"}},bubbles:!0,composed:!0,cancelable:!0}))};await t("start"),await t("node1"),await t("node2"),await t("stop")}}window.customElements.define("lg4j-executor",l);
//# sourceMappingURL=index.aac743f5.js.map
