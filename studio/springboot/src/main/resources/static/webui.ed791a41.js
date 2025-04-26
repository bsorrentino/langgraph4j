var e=globalThis,t={},a={},n=e.parcelRequire0031;null==n&&((n=function(e){if(e in t)return t[e].exports;if(e in a){var n=a[e];delete a[e];var s={id:e,exports:{}};return t[e]=s,n.call(s.exports,s,s.exports),s.exports}var i=Error("Cannot find module '"+e+"'");throw i.code="MODULE_NOT_FOUND",i}).register=function(e,t){a[e]=t},e.parcelRequire0031=n),n.register;var s=n("hNeh9"),i=n("800sp");const l=async e=>new Promise(t=>setTimeout(t,e)),r={callInit:async e=>{let t={threads:[["default",[]]],title:"LangGraph4j : TEST",args:[{name:"input",type:"IMAGE",required:!0}],graph:`
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
  `};await l(1e3);let a=new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0});return e.dispatchEvent(a),t},callSubmitAction:async(e,t)=>{let a=async(a,n)=>{let s=new CustomEvent("result",{detail:[t,{checkpoint:"start"===a||"stop"===a?void 0:`checkpoint-${a}`,node:a,next:n,state:{input:"this is input",property1:{value:"value1",valid:!0},property2:{value:"value2",children:{elements:[1,2,3]}}}}],bubbles:!0,composed:!0,cancelable:!0});await l(1e3),e.dispatchEvent(s)};await a("__START__","agent_describer"),await a("agent_describer","agent_sequence_plantuml"),await a("agent_generic_plantuml","___START__"),await a("___START__","_evaluate_result"),await a("_evaluate_result","__END__"),await a("___END__"),await a("__END__"),e.dispatchEvent(new CustomEvent("state-updated",{detail:"stop",bubbles:!0,composed:!0,cancelable:!0}))}},o=(0,n("8uVid").debug)({on:!0,topic:"LG4JExecutor"});async function*_(e){let t=e.body?.getReader(),a=new TextDecoder,n="";for(;t;){let{done:e,value:s}=await t.read();if(e)break;try{n+=a.decode(s);let e=JSON.parse(n);n="",yield e}catch(e){console.warn("JSON parse error:",e)}}}class c extends i.LitElement{static styles=[s.default,(0,i.css)`
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
  `];static properties={url:{type:String,reflect:!0},test:{type:Boolean,reflect:!0},_executing:{state:!0}};url=null;#e;#t=null;constructor(){super(),this.test=!1,this.formMetaData=[],this._executing=!1}#a(){this._executing=!0,this.dispatchEvent(new CustomEvent("state-updated",{detail:"start",bubbles:!0,composed:!0,cancelable:!0}))}#n(e){if(this._executing=!1,!e)return;if(e instanceof Error){this.dispatchEvent(new CustomEvent("state-updated",{detail:"error",bubbles:!0,composed:!0,cancelable:!0}));return}let[t,{node:a}]=e;this.dispatchEvent(new CustomEvent("state-updated",{detail:"__END__"!==a?"interrupted":"stop",bubbles:!0,composed:!0,cancelable:!0}))}#s(e){o("thread-updated",e.detail),this.#e=e.detail,this.#t=null,this.requestUpdate()}#i(e){o("onNodeUpdated",e),this.#t=e.detail,this.requestUpdate()}connectedCallback(){if(super.connectedCallback(),this.addEventListener("thread-updated",this.#s),this.addEventListener("node-updated",this.#i),this.test){r.callInit(this).then(e=>{this.formMetaData=e.args,this.requestUpdate()});return}this.#l()}disconnectedCallback(){super.disconnectedCallback(),this.removeEventListener("thread-updated",this.#s),this.removeEventListener("node-updated",this.#i)}render(){return(0,i.html)`
        <div class="container">
          ${this.formMetaData.map(({name:e,type:t})=>{switch(t){case"STRING":return(0,i.html)`<textarea id="${e}" class="textarea textarea-primary" placeholder="${e}"></textarea>`;case"IMAGE":return(0,i.html)`<lg4j-image-uploader id="${e}"></lg4j-image-uploader>`}})}
          <div class="commands">
            <button id="submit" ?disabled=${this._executing} @click="${this.#r}" class="btn btn-primary item1">Submit</button>
            <button id="resume" ?disabled=${!this.#t||this._executing} @click="${this.#o}" class="btn btn-secondary item2">
            Resume ${this.#t?"(from "+this.#t?.node+")":""}
            </button>
          </div>
        </div>
        <!--
        ==============
        ERROR DIALOG 
        ==============
        -->
        <dialog id="error_dialog" class="modal">
          <div class="modal-box">
            <form method="dialog">
              <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
            </form>
              <div class="flex items-center gap-2 mb-4 text-error">
              <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6 shrink-0 stroke-current"
              fill="none"
              viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p id="error_message" class="text-lg font-bold">ERROR</p>
          </div>
          </div>
        </dialog>        
        `}#_(e){let t=this.shadowRoot?.getElementById("error_dialog");if(t&&"showModal"in t){let a=t.querySelector("#error_message");a&&(a.textContent=e),t.showModal()}}async #l(){let e=await fetch(`${this.url}/init`);if(!e.ok)return this.#_(e.statusText),null;let t=await e.json();o("initData",t),this.dispatchEvent(new CustomEvent("init",{detail:t,bubbles:!0,composed:!0,cancelable:!0})),this.formMetaData=t.args,this.requestUpdate()}async #o(){this.#a();let e=null;try{if(this.test){await r.callSubmitAction(this,this.#e);return}e=await this.#c()}catch(t){t instanceof Error&&(this.#_(t.message),e=t)}finally{this.#n(e)}}async #c(){let e=await fetch(`${this.url}/stream?thread=${this.#e}&resume=true&node=${this.#t?.node}&checkpoint=${this.#t?.checkpoint}`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(this.#t?.data)});if(!e.ok)throw Error(e.statusText);this.#t=null;let t=null;for await(let a of _(e))o(a),t=a,this.dispatchEvent(new CustomEvent("result",{detail:a,bubbles:!0,composed:!0,cancelable:!0}));return t}async #r(){this.#a();let e=null;try{this.test&&await r.callSubmitAction(this,this.#e),e=await this.#d()}catch(t){t instanceof Error&&(this.#_(t.message),e=t)}finally{this.#n(e)}}async #d(){let e=this.formMetaData.reduce((e,t)=>{let{name:a,type:n}=t,s=this.shadowRoot?.getElementById(a);switch(n){case"STRING":case"IMAGE":e[a]=s?.value}return e},{}),t=await fetch(`${this.url}/stream?thread=${this.#e}`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(e)});if(!t.ok)throw Error(t.statusText);let a=null;for await(let e of _(t))o("SUBMIT RESULT",e),a=e,this.dispatchEvent(new CustomEvent("result",{detail:e,bubbles:!0,composed:!0,cancelable:!0}));return a}}window.customElements.define("lg4j-executor",c);
//# sourceMappingURL=webui.ed791a41.js.map
