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
  `];

  /**
   * Properties of the component.
   * 
   * @static
   * @type {Object}
   */
  static properties = {
    url: {},
    test: { type: Boolean }
  }

  /**
   * @type {string}
   */
  selectedThread

  /**
   * Creates an instance of LG4JInputElement.
   * 
   * @constructor
   */
  constructor() {
    super();
    this.test = false
    this.formMetaData = {}
  }

  /**
   * Event handler for the 'update slected thread' event.
   * 
   * @param {CustomEvent} e - The event object containing the updated data.
   * @private
   */
  #onUpdateThread( e ) {
    console.debug( 'update-thread', e.detail )
    this.selectedThread = e.detail
  }


  /**
   * Lifecycle method called when the element is added to the document's DOM.
   */
  connectedCallback() {
    super.connectedCallback();

    this.addEventListener( "update-thread", this.#onUpdateThread );

    if(this.test ) {
      this.#init_test();
      return
    }

    this.#init()

  }

  disconnectedCallback() {
    super.disconnectedCallback( "update-thread", this.#onUpdateThread );

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
    this.requestUpdate()
  }

  /**
   * Renders the HTML template for the component.
   * 
   * @returns {TemplateResult} The rendered HTML template.
   */
  render() {

    console.debug( 'render', this.formMetaData )
    
    return html`
        <div class="container">
          ${ Object.entries(this.formMetaData).map( ([key,value]) => 
             html`<textarea id="${key}" class="textarea textarea-primary" placeholder="${key}"></textarea>`
          )}
          <button @click="${this.#submit}" class="btn btn-primary">Submit</button>
        </div>
    `;
  }


async #submit() {
  
  if(this.test ) {
    await this.#submit_test();
    return
  }
  
  const data = Object.keys(this.formMetaData).reduce( (acc, key) => {
    acc[key] = this.shadowRoot.getElementById(key).value
    return acc
  }, {});

  const execResponse = await fetch(`${this.url}/stream?thread=${this.selectedThread}`, {
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
        
  await delay( 1000 );
  this.dispatchEvent( new CustomEvent( 'init', { 
    detail: { 
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


  async #submit_test( ) {

    const send = async ( nodeId ) => {
      await delay( 1000 );
      this.dispatchEvent( new CustomEvent( 'result', { 
        detail: [ this.selectedThread, { 
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
