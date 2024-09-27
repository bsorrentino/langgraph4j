import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

/**
 * @typedef {Object} ResultData
 * @property {string} node - The node identifier.
 * @property {Record<string, any>} state - The state associated with the node.
 */


export class LG4JResultElement extends LitElement {

  static styles = [TWStyles, css`
  json-viewer {
    --font-size: .8rem;
  }`]

  static properties = {}

  
  /**
   * @type {Map<string, Record<string, any[]>>}
   */
  threadMap = new Map()
  
  /*
   * @type {string}
   */
  #selectedThread;

  get selectedTab() {
    return this.#selectedThread
  }

  set selectedTab( thread ) {
    this.#selectedThread = thread

    this.dispatchEvent( new CustomEvent( 'update-thread', { 
      detail: thread ,
      bubbles: true,
      composed: true,
      cancelable: true
    }));

  }

  constructor() {
    super()
  }
  
  connectedCallback() {
    super.connectedCallback();

    this.addEventListener( 'result', this.#onResult )
    this.addEventListener( 'init-threads', this.#onInitThreads )
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.removeEventListener( 'result',  this.#onResult )
    this.removeEventListener( 'init-threads',  this.#onInitThreads )
  }

  /**
   * Event handler for the 'init threads' event.
   * 
   * @param {CustomEvent} e - The event object containing the result data.
   * @private
   */
  #onInitThreads = (e) => {
    const { detail: threads  = [] } = e 

    console.debug( threads )

    this.threadMap = new Map( threads )
    
    if( threads && threads.length > 0 ) {
      this.selectedTab = threads[0][0]
      this.requestUpdate()  
    }
  }


  /**
   * Event handler for the 'result' event.
   * 
   * @param {CustomEvent} e - The event object containing the result data.
   * @private
   */
  #onResult = (e) => {

    const [ thread, result ] = e.detail
    console.debug( "onResult", thread, result  )
    
    if( !this.threadMap.has( thread ) ) {
      throw new Error( `result doesn't contain a valid thread!` );
    }

    console.debug( 'onResult', thread )

    let results = this.threadMap.get( thread )
    // TODO: validate e.detail
    const index = results.push( result )

    this.threadMap.set( thread, results );

    this.dispatchEvent( new CustomEvent( 'graph-active', { 
      detail: result.node,
      bubbles: true,
      composed: true,
      cancelable: true
    }));
    
    this.requestUpdate()
    
    this.updateComplete.then(() => {
      const id = `#json${index-1}`
      const elems = this.shadowRoot.querySelectorAll(id);
      console.debug( id, elems );
      for (const elem of elems) {
        elem.expandAll()
      }
    });
  }

  /**
   * Event handler select tab.
   * 
   * @param {Event} event - The event object.
   * @private
   */

  #onSelectTab( event ) {

    console.debug( event.target.id )
    this.selectedTab = event.target.id

    this.requestUpdate();
  }

  #onNewTab(event) {
    console.debug( "NEW TAB", event)

    const threadId = `Thread-${this.threadMap.size+1}`

    this.threadMap.set( threadId, [] );

    this.selectedTab = threadId

    this.requestUpdate();

  }

  /** 
   * Renders a result.
   * @param {ResultData} result - The result data to render.
   * @returns {import('lit').TemplateResult} The template for the result.
   */
  #renderResult(result, index) {

    return html`
    <div class="collapse collapse-arrow bg-base-200">
      <input type="radio" name="item-1" checked="checked" />
      <div class="collapse-title text-ml font-bold">${result.node}</div>
      <div class="collapse-content">
        <lg4j-node-output>${JSON.stringify(result.state).trim()}</log4j-node-output>  
      </div>
    </div>
    `
  }


  #renderTabs() {

    const threads = [ ...this.threadMap.keys() ] 
    return html`
    ${threads.map( t => html`<a id="${t}" @click="${this.#onSelectTab}" role="tab" class="tab ${this.selectedTab===t ? 'tab-active' : ''}" >${t}</a>`)}
    `
  }
  
  render() {
  
    return html`
      
      <div class="h-full">
        <div role="tablist" class="tabs tabs-bordered">
            ${this.#renderTabs()}
            <a role="tab" class="tab" @click="${this.#onNewTab}">
              <svg  xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
                <circle cx="10" cy="10" r="9" fill="none" stroke="white" stroke-width="1.5"/>
                <line x1="5" y1="10" x2="15" y2="10" stroke="white" stroke-width="1.5" stroke-linecap="round"/>
                <line x1="10" y1="5" x2="10" y2="15" stroke="white" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </a>
          </div>
            <div class="max-h-[95%] overflow-x-auto bg-slate-500">
              <table class="table table-pin-rows">
                <tbody>
                    ${this.threadMap.get(this.selectedTab)?.map( (result, index) => html`<tr><td>${this.#renderResult(result, index)}</td></tr>`) }
                </tbody>
              </table>
            </div>
        </div> 
       
    `;
  }


    /** 
   * Renders a result.
   * @param {ResultData} result - The result data to render.
   * @returns {import('lit').TemplateResult} The template for the result.
   * @deprecated
   */
    #renderResultDeprecated(result, index) {

      return html`
      <div class="collapse collapse-arrow bg-base-200">
        <input type="radio" name="item-1" checked="checked" />
        <div class="collapse-title text-ml font-bold">${result.node}</div>
        <div class="collapse-content">
        ${Object.entries(result.state).map(([key, value]) => html`
            <div>
                <h4 class="italic">${key}</h4>
                <p class="my-3">
                  <json-viewer id="json${index}">
                    ${JSON.stringify(value)}
                  </json-viewer>
                </p>
              </div>
          `)}
        </div>
      </div>
      `
    }
  
  // @deprecated
  #renderResultWithCard(result, index) {
    return html`
    <div class="card bg-neutral text-neutral-content">
    <div class="card-body">
      <h2 class="card-title">${result.node}</h2>
      <div class="collapse collapse-arrow bg-base-200">
        <input type="radio" name="item-1" checked="checked" />
        <div class="collapse-content">
        ${Object.entries(result.state).map(([key, value]) => html`
          <div>
              <h4 class="italic">${key}</h4>
              <p class="my-3">
                <json-viewer id="json${index}">
                ${JSON.stringify(value)}
                </json-viewer>
              </p>
            </div>
        `)}
        </div>
        </div>
    </div>
  </div>   `
  }

}

window.customElements.define('lg4j-result', LG4JResultElement);
