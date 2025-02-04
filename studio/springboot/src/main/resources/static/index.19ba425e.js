var e=globalThis,t={},a={},i=e.parcelRequire94c2;null==i&&((i=function(e){if(e in t)return t[e].exports;if(e in a){var i=a[e];delete a[e];var n={id:e,exports:{}};return t[e]=n,i.call(n.exports,n,n.exports),n.exports}var l=Error("Cannot find module '"+e+"'");throw l.code="MODULE_NOT_FOUND",l}).register=function(e,t){a[e]=t},e.parcelRequire94c2=i),i.register;var n=i("hNeh9"),l=i("800sp");const s=async e=>new Promise(t=>setTimeout(t,e)),_={callInit:async e=>{let t={threads:[["default",[]]],title:"LangGraph4j : TEST",args:[{name:"input",type:"IMAGE",required:!0}],graph:`
---
title: Image to diagram with correction
---
flowchart TD
	__START__((start))
	__END__((stop))
	agent_describer("agent_describer")
	agent_sequence_plantuml("agent_sequence_plantuml")
	agent_generic_plantuml("agent_generic_plantuml")
subgraph evaluate_result
	___START__((start)):::___START__
	___END__((stop)):::___END__
	_evaluate_result("evaluate_result")
	_agent_review("agent_review")
	%%	_condition1{"check state"}
	___START__:::___START__ --> _evaluate_result:::_evaluate_result
	_agent_review:::_agent_review --> _evaluate_result:::_evaluate_result
	%%	_evaluate_result:::_evaluate_result --> _condition1:::_condition1
	%%	_condition1:::_condition1 -->|ERROR| _agent_review:::_agent_review
	_evaluate_result:::_evaluate_result -->|ERROR| _agent_review:::_agent_review
	%%	_condition1:::_condition1 -->|UNKNOWN| ___END__:::___END__
	_evaluate_result:::_evaluate_result -->|UNKNOWN| ___END__:::___END__
	%%	_condition1:::_condition1 -->|OK| ___END__:::___END__
	_evaluate_result:::_evaluate_result -->|OK| ___END__:::___END__
end
	%%	condition1{"check state"}
	__START__:::__START__ --> agent_describer:::agent_describer
	%%	agent_describer:::agent_describer --> condition1:::condition1
	%%	condition1:::condition1 -->|sequence| agent_sequence_plantuml:::agent_sequence_plantuml
	agent_describer:::agent_describer -->|sequence| agent_sequence_plantuml:::agent_sequence_plantuml
	%%	condition1:::condition1 -->|generic| agent_generic_plantuml:::agent_generic_plantuml
	agent_describer:::agent_describer -->|generic| agent_generic_plantuml:::agent_generic_plantuml
	agent_sequence_plantuml:::agent_sequence_plantuml --> evaluate_result:::evaluate_result
	agent_generic_plantuml:::agent_generic_plantuml --> evaluate_result:::evaluate_result
	evaluate_result:::evaluate_result --> __END__:::__END__

	classDef ___START__ fill:black,stroke-width:1px,font-size:xx-small;
	classDef ___END__ fill:black,stroke-width:1px,font-size:xx-small;
  `};await s(1e3);let a=new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0});return e.dispatchEvent(a),t},callSubmitAction:async(e,t)=>{let a=async a=>{let i=new CustomEvent("result",{detail:[t,{checkpoint:"start"===a||"stop"===a?void 0:`checkpoint-${a}`,node:a,state:{input:"this is input",property1:{value:"value1",valid:!0},property2:{value:"value2",children:{elements:[1,2,3]}}}}],bubbles:!0,composed:!0,cancelable:!0});await s(1e3),e.dispatchEvent(i)};await a("__START__"),await a("agent_describer"),await a("agent_generic_plantuml"),await a("___START__"),await a("_evaluate_result"),await a("___END__"),await a("__END__")}};async function*r(e){let t=e.body?.getReader();for(;t;){let{done:e,value:a}=await t.read();if(e)break;yield new TextDecoder().decode(a)}}class c extends l.LitElement{static styles=[n.default,(0,l.css)`
    .container {
      display: flex;
      flex-direction: column;
      row-gap: 10px;
    }

    .commands {
      display: flex;
      flex-direction: row;
      column-gap: 10px;
    }

    .item1 {
      flex-grow: 2;
    }
    .item2 {
      flex-grow: 2;
    }
  `];static properties={url:{type:String,reflect:!0},test:{type:Boolean,reflect:!0},_executing:{state:!0}};url=null;#e;#t=null;constructor(){super(),this.test=!1,this.formMetaData=[],this._executing=!1}#a(e){console.debug("thread-updated",e.detail),this.#e=e.detail,this.#t=null,this.requestUpdate()}#i(e){console.debug("onNodeUpdated",e),this.#t=e.detail,this.requestUpdate()}connectedCallback(){if(super.connectedCallback(),this.addEventListener("thread-updated",this.#a),this.addEventListener("node-updated",this.#i),this.test){_.callInit(this).then(e=>{this.formMetaData=e.args,this.requestUpdate()});return}this.#n()}disconnectedCallback(){super.disconnectedCallback(),this.removeEventListener("thread-updated",this.#a),this.removeEventListener("node-updated",this.#i)}render(){return(0,l.html)`
        <div class="container">
          ${this.formMetaData.map(({name:e,type:t})=>{switch(t){case"STRING":return(0,l.html)`<textarea id="${e}" class="textarea textarea-primary" placeholder="${e}"></textarea>`;case"IMAGE":return(0,l.html)`<lg4j-image-uploader id="${e}"></lg4j-image-uploader>`}})}
          <div class="commands">
            <button id="submit" ?disabled=${this._executing} @click="${this.#l}" class="btn btn-primary item1">Submit</button>
            <button id="resume" ?disabled=${!this.#t||this._executing} @click="${this.#s}" class="btn btn-secondary item2">
            Resume ${this.#t?"(from "+this.#t?.node+")":""}
            </button>
          </div>
        </div>
        `}async #n(){let e=await fetch(`${this.url}/init`),t=await e.json();console.debug("initData",t),this.dispatchEvent(new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=t.args,this.requestUpdate()}async #s(){this._executing=!0;try{if(this.test){await _.callSubmitAction(this,this.#e);return}await this.#_()}finally{this._executing=!1}}async #_(){let e=await fetch(`${this.url}/stream?thread=${this.#e}&resume=true&node=${this.#t?.node}&checkpoint=${this.#t?.checkpoint}`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(this.#t?.data)});for await(let t of(this.#t=null,r(e)))console.debug(t),this.dispatchEvent(new CustomEvent("result",{detail:JSON.parse(t),bubbles:!0,composed:!0,cancelable:!0}))}async #l(){this._executing=!0;try{if(this.test){await _.callSubmitAction(this,this.#e);return}await this.#r()}finally{this._executing=!1}}async #r(){let e=this.formMetaData.reduce((e,t)=>{let{name:a,type:i}=t,n=this.shadowRoot?.getElementById(a);switch(i){case"STRING":case"IMAGE":e[a]=n?.value}return e},{});for await(let t of r(await fetch(`${this.url}/stream?thread=${this.#e}`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(e)})))console.debug(t),this.dispatchEvent(new CustomEvent("result",{detail:JSON.parse(t),bubbles:!0,composed:!0,cancelable:!0}))}}window.customElements.define("lg4j-executor",c);
//# sourceMappingURL=index.19ba425e.js.map
