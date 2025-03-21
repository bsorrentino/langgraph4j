var e=globalThis,t={},a={},n=e.parcelRequire0031;null==n&&((n=function(e){if(e in t)return t[e].exports;if(e in a){var n=a[e];delete a[e];var i={id:e,exports:{}};return t[e]=i,n.call(i.exports,i,i.exports),i.exports}var s=Error("Cannot find module '"+e+"'");throw s.code="MODULE_NOT_FOUND",s}).register=function(e,t){a[e]=t},e.parcelRequire0031=n),n.register;var i=n("hNeh9"),s=n("800sp");const l=async e=>new Promise(t=>setTimeout(t,e)),_={callInit:async e=>{let t={threads:[["default",[]]],title:"LangGraph4j : TEST",args:[{name:"input",type:"IMAGE",required:!0}],graph:`
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
  `};await l(1e3);let a=new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0});return e.dispatchEvent(a),t},callSubmitAction:async(e,t)=>{let a=async(a,n)=>{let i=new CustomEvent("result",{detail:[t,{checkpoint:"start"===a||"stop"===a?void 0:`checkpoint-${a}`,node:a,next:n,state:{input:"this is input",property1:{value:"value1",valid:!0},property2:{value:"value2",children:{elements:[1,2,3]}}}}],bubbles:!0,composed:!0,cancelable:!0});await l(1e3),e.dispatchEvent(i)};await a("__START__","agent_describer"),await a("agent_describer","agent_sequence_plantuml"),await a("agent_generic_plantuml","___START__"),await a("___START__","_evaluate_result"),await a("_evaluate_result","__END__"),await a("___END__"),await a("__END__"),e.dispatchEvent(new CustomEvent("state-updated",{detail:"stop",bubbles:!0,composed:!0,cancelable:!0}))}};async function*r(e){let t=e.body?.getReader(),a=new TextDecoder,n="";for(;t;){let{done:e,value:i}=await t.read();if(e)break;try{n+=a.decode(i);let e=JSON.parse(n);n="",yield e}catch(e){console.warn("JSON parse error:",e)}}}class c extends s.LitElement{static styles=[i.default,(0,s.css)`
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
  `];static properties={url:{type:String,reflect:!0},test:{type:Boolean,reflect:!0},_executing:{state:!0}};url=null;#e;#t=null;constructor(){super(),this.test=!1,this.formMetaData=[],this._executing=!1}#a(){this._executing=!0,this.dispatchEvent(new CustomEvent("state-updated",{detail:"start",bubbles:!0,composed:!0,cancelable:!0}))}#n(e){if(this._executing=!1,e){let[t,{node:a}]=e;this.dispatchEvent(new CustomEvent("state-updated",{detail:"__END__"!==a?"interrupted":"stop",bubbles:!0,composed:!0,cancelable:!0}))}}#i(e){console.debug("thread-updated",e.detail),this.#e=e.detail,this.#t=null,this.requestUpdate()}#s(e){console.debug("onNodeUpdated",e),this.#t=e.detail,this.requestUpdate()}connectedCallback(){if(super.connectedCallback(),this.addEventListener("thread-updated",this.#i),this.addEventListener("node-updated",this.#s),this.test){_.callInit(this).then(e=>{this.formMetaData=e.args,this.requestUpdate()});return}this.#l()}disconnectedCallback(){super.disconnectedCallback(),this.removeEventListener("thread-updated",this.#i),this.removeEventListener("node-updated",this.#s)}render(){return(0,s.html)`
        <div class="container">
          ${this.formMetaData.map(({name:e,type:t})=>{switch(t){case"STRING":return(0,s.html)`<textarea id="${e}" class="textarea textarea-primary" placeholder="${e}"></textarea>`;case"IMAGE":return(0,s.html)`<lg4j-image-uploader id="${e}"></lg4j-image-uploader>`}})}
          <div class="commands">
            <button id="submit" ?disabled=${this._executing} @click="${this.#_}" class="btn btn-primary item1">Submit</button>
            <button id="resume" ?disabled=${!this.#t||this._executing} @click="${this.#r}" class="btn btn-secondary item2">
            Resume ${this.#t?"(from "+this.#t?.node+")":""}
            </button>
          </div>
        </div>
        `}async #l(){let e=await fetch(`${this.url}/init`),t=await e.json();console.debug("initData",t),this.dispatchEvent(new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=t.args,this.requestUpdate()}async #r(){this.#a();let e=null;try{if(this.test){await _.callSubmitAction(this,this.#e);return}e=await this.#c()}finally{this.#n(e)}}async #c(){let e=await fetch(`${this.url}/stream?thread=${this.#e}&resume=true&node=${this.#t?.node}&checkpoint=${this.#t?.checkpoint}`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(this.#t?.data)});this.#t=null;let t=null;for await(let a of r(e))console.debug(a),t=a,this.dispatchEvent(new CustomEvent("result",{detail:a,bubbles:!0,composed:!0,cancelable:!0}));return t}async #_(){this.#a();let e=null;try{if(this.test){await _.callSubmitAction(this,this.#e);return}e=await this.#d()}finally{this.#n(e)}}async #d(){let e=this.formMetaData.reduce((e,t)=>{let{name:a,type:n}=t,i=this.shadowRoot?.getElementById(a);switch(n){case"STRING":case"IMAGE":e[a]=i?.value}return e},{}),t=await fetch(`${this.url}/stream?thread=${this.#e}`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(e)}),a=null;for await(let e of r(t))console.debug("SUBMIT RESULT",e),a=e,this.dispatchEvent(new CustomEvent("result",{detail:e,bubbles:!0,composed:!0,cancelable:!0}));return a}}window.customElements.define("lg4j-executor",c);
//# sourceMappingURL=webui.6df5a6ea.js.map
