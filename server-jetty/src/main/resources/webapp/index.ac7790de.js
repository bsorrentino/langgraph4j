var e=globalThis,t={},a={},r=e.parcelRequire3bab;null==r&&((r=function(e){if(e in t)return t[e].exports;if(e in a){var r=a[e];delete a[e];var s={id:e,exports:{}};return t[e]=s,r.call(s.exports,s,s.exports),s.exports}var n=Error("Cannot find module '"+e+"'");throw n.code="MODULE_NOT_FOUND",n}).register=function(e,t){a[e]=t},e.parcelRequire3bab=r),r.register;var s=r("hNeh9"),n=r("800sp");const i=async e=>new Promise(t=>setTimeout(t,e));async function*o(e){let t=e.body.getReader();for(;;){let{done:e,value:a}=await t.read();if(e)break;yield new TextDecoder().decode(a)}}class c extends n.LitElement{static styles=[s.default,(0,n.css)`
    .container {
      display: flex;
      flex-direction: column;
      row-gap: 10px;
    }
  `];static properties={url:{},test:{type:Boolean}};constructor(){super(),this.test=!1,this.formMetaData={}}connectedCallback(){if(super.connectedCallback(),this.test){this.#e();return}this.#t()}async #t(){let e=await fetch(`${this.url}/init`),t=await e.json();console.debug("initData",t),this.dispatchEvent(new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=t.args,this.requestUpdate()}async #e(){await i(1e3),this.dispatchEvent(new CustomEvent("init",{detail:{title:"LangGraph4j : TEST",graph:`
---
title: TEST
---        
flowchart TD
  start((start))
  stop((stop))
  web_search("web_search")
  retrieve("retrieve")
  grade_documents("grade_documents")
  generate("generate")
  transform_query("transform_query")
  start:::start -->|web_search| web_search:::web_search
  start:::start -->|vectorstore| retrieve:::retrieve
  web_search:::web_search --> generate:::generate
  retrieve:::retrieve --> grade_documents:::grade_documents
  grade_documents:::grade_documents -->|transform_query| transform_query:::transform_query
  grade_documents:::grade_documents -->|generate| generate:::generate
  transform_query:::transform_query --> retrieve:::retrieve
  generate:::generate -->|not supported| generate:::generate
  generate:::generate -->|not useful| transform_query:::transform_query
  generate:::generate -->|useful| stop:::stop
      `},bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData={input:{type:"string",required:!0}},this.requestUpdate()}render(){return console.debug("render",this.formMetaData),(0,n.html)`
        <div class="container">
          ${Object.entries(this.formMetaData).map(([e,t])=>(0,n.html)`<textarea id="${e}" class="textarea textarea-primary" placeholder="${e}"></textarea>`)}
          <button @click="${this.#a}" class="btn btn-primary">Submit</button>
        </div>
    `}async #a(){if(this.test){await this.#r();return}let e=Object.keys(this.formMetaData).reduce((e,t)=>(e[t]=this.shadowRoot.getElementById(t).value,e),{});for await(let t of o(await fetch(`${this.url}/stream`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(e)})))console.debug(t),this.dispatchEvent(new CustomEvent("result",{detail:JSON.parse(t),bubbles:!0,composed:!0,cancelable:!0}))}async #r(){let e=async e=>{await i(1e3),this.dispatchEvent(new CustomEvent("result",{detail:{node:e,state:{input:"this is input",property1:{value:"value1",valid:!0},property2:{value:"value2",children:{elements:[1,2,3]}}}},bubbles:!0,composed:!0,cancelable:!0}))};await e("start"),await e("retrieve"),await e("grade_documents"),await e("transform_query"),await e("retrieve"),await e("grade_documents"),await e("generate"),await e("stop")}}window.customElements.define("lg4j-executor",c);
//# sourceMappingURL=index.ac7790de.js.map
