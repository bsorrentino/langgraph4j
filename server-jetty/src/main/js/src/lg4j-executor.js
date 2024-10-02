import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

/**
 * Asynchronously waits for a specified number of milliseconds.
 * 
 * @param {number} ms - The number of milliseconds to wait.
 * @returns {Promise<void>} A promise that resolves after the specified delay.
 */
const delay = async (ms) => (new Promise(resolve => setTimeout(resolve, ms)));

/**
 * Asynchronously fetches data from a given fetch call and yields the data in chunks.
 * 
 * @async
 * @generator
 * @param Response - Response object to stream.
 * @yields {Promise<string>} The decoded text chunk from the response stream.
 */
async function* streamingResponse(response) {
  // Attach Reader
  const reader = response.body.getReader();
  while (true) {
    // wait for next encoded chunk
    const { done, value } = await reader.read();
    // check if stream is done
    if (done) break;
    // Decodes data chunk and yields it
    yield (new TextDecoder().decode(value));
  }
}


/**
 * @typedef {Object} ResultData
 * @property {string} node -
 * @property {string} [checkpoint] -   
 * @property {Record<string,any>} state - 
 */

/**
 * @typedef {Object} Property
 * @property {String | Boolean | Number} [type]
 * @property {boolean} [state] - Set to true to declare the property as internal reactive state. Internal reactive state triggers updates like public reactive properties, 
 *                               but Lit doesn't generate an attribute for it, and users shouldn't access it from outside the component
 * @property {boolean} [attribute] - Whether the property is associated with an attribute, or a custom name for the associated attribute. Default: true. 
 *                                   If attribute is false, the converter, reflect and type options are ignored.
 * @property {boolean} [reflect] - Whether property value is reflected back to the associated attribute. Default: false.
 */



/**
 * LG4JInputElement is a custom web component that extends LitElement.
 * It provides a styled input container with a placeholder.
 * 
 * @class
 * @extends {LitElement}
 */
export class LG4JExecutorElement extends LitElement {

  /**
   * Styles applied to the component.
   * 
   * @static
   * @type {Array}
   */
  static styles = [TWStyles, css`
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
  `];


  /**
   * Properties of the component.
   * 
   * @static
   * @type { Object.<string, Property> }
   */
  static properties = {
    url: { type: String },
    test: { type: Boolean },
    _executing: { state: true }
    
  }

  /**
   * current selected thread
   * 
   * @type {string|undefined} - thread id
   */
  #selectedThread

  /**
   * Represents an event triggered when an edit occurs.
   *
   * @typedef {Object} UpdatedState
   * @property {string} checkpoint - checkpoint id.
   * @property {Record<string, any>} data - the modified state.
   */

  /**
   * current state for update 
   * 
   * @type {UpdatedState|undefined}
   */
  #updatedState

  /**
   * interrupt before this node 
   * 
   * @type {string|null}
   */
  #interruption = null

  /**
   * available interruptable nodes 
   * 
   * @type {string[]}
   */
  #nodes = []

  /**
   * Creates an instance of LG4JInputElement.
   * 
   * @constructor
   */
  constructor() {
    super();
    this.test = false
    this.formMetaData = {}
    this._executing = false
    
  }

  /**
   * Event handler for the 'update slected thread' event.
   * 
   * @param {CustomEvent} e - The event object containing the updated data.
   * @private
   */
  #onThreadUpdated( e ) {
    console.debug( 'thread-updated', e.detail )
    this.#selectedThread = e.detail
  }

  /**
   * 
   * @param {CustomEvent<ResultData>} e - The event object containing the result data.
   * @private
   */
  #onNodeUpdated( e ) {
    console.debug( 'onNodeUpdated', e )
  }

  /**
   * Lifecycle method called when the element is added to the document's DOM.
   */
  connectedCallback() {
    super.connectedCallback();

    this.addEventListener( "thread-updated", this.#onThreadUpdated );
    this.addEventListener( 'node-updated', this.#onNodeUpdated )

    if(this.test ) {
      this.#init_test();
      return
    }

    this.#init()

  }

  disconnectedCallback() {
    super.disconnectedCallback();

    this.removeEventListener( "thread-updated", this.#onThreadUpdated )
    this.removeEventListener( 'node-updated', this.#onNodeUpdated )

  }

  async #init() {

    const initResponse = await fetch( `${this.url}/init` )

    const initData = await initResponse.json()
    
    console.debug( 'initData', initData );

    this.dispatchEvent( new CustomEvent( 'init', { 
      detail: initData,
      bubbles: true,
      composed: true,
      cancelable: true
    }));

    this.formMetaData = initData.args
    // this.#nodes = initData.nodes
    this.requestUpdate()
  }

  /**
   * Renders the HTML template for the component.
   * 
   * @returns {TemplateResult} The rendered HTML template.
   */
  render() {

    // console.debug( 'render', this.formMetaData )
    return html`
        <div class="container">
          ${ Object.entries(this.formMetaData).map( ([key,value]) => 
             html`<textarea id="${key}" class="textarea textarea-primary" placeholder="${key}"></textarea>`
          )}
          <div class="commands">
            <button id="submit" ?disabled=${this._executing} @click="${this.#submit}" class="btn btn-primary item1">Submit</button>
            <button id="resume" ?disabled=${!this.#updatedState || this._executing} @click="${ () => {} }" class="btn btn-secondary item2">Resume</button>
            <!--
            <select ?disabled=${ this._executing } class="select select-bordered max-w-xl">              
                  <option disabled ?selected=${this.#interruption === null}>select interruption</option>
                  ${ this.#nodes.map( n =>   
                      html`<option ?selected=${this.#interruption === n}>${n}</option>` )}
            </select>
            -->
          </div>
        </div>
        `;
  }

async #submit() {

  this._executing = true
  try {

    if(this.test ) {
      await this.#submitAction_test();
      return
    }

    await this.#submitAction()
    
  }
  finally {
    this._executing = false
  }
}

async #submitAction() {
  
    const data = Object.keys(this.formMetaData).reduce( (acc, key) => {
      acc[key] = this.shadowRoot.getElementById(key).value
      return acc
    }, {});

    const execResponse = await fetch(`${this.url}/stream?thread=${this.#selectedThread}`, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    for await (let chunk of streamingResponse( execResponse )  ) {
      console.debug( chunk )

      this.dispatchEvent( new CustomEvent( 'result', { 
        detail: JSON.parse(chunk),
        bubbles: true,
        composed: true,
        cancelable: true
      } ) );

    }
  
}
  
////////////////////////////////////////////////////////
// TEST
///////////////////////////////////////////////////////
async #init_test() {
      
  // this.#nodes = []

  await delay( 1000 );
  this.dispatchEvent( new CustomEvent( 'init', { 
    detail: { 
      threads: [ ['default', [] ] ],
      nodes: this.#nodes,
      title: 'LangGraph4j : TEST',
      graph:`
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
      `
      },
      bubbles: true,
      composed: true,
      cancelable: true
    }));

    this.formMetaData = { 
      input: { type: 'string', required: true }
    }
    
    this.requestUpdate()

  }


  async #submitAction_test( ) {

    const thread = this.#selectedThread
    
    const send = async ( nodeId ) => {
      await delay( 1000 );
      this.dispatchEvent( new CustomEvent( 'result', { 
        detail: [ thread, { 
          checkpoint: ( nodeId==='start' || nodeId==='stop') ? undefined : `checkpoint-${nodeId}`,
          node: nodeId, 
          state: { 
            input: "this is input",
            property1: { value: "value1", valid: true } , 
            property2: { value: "value2", children: { elements: [1,2,3]} } }}
          ],
        bubbles: true,
        composed: true,
        cancelable: true
      }));
    }

    await send( 'start' );
    await send( 'retrieve' );
    await send( 'grade_documents');
    await send( 'transform_query');
    await send( 'retrieve' );
    await send( 'grade_documents');
    await send( 'generate');
    await send( 'stop' );

  }

}


window.customElements.define('lg4j-executor', LG4JExecutorElement);
