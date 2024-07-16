import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

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
   * Lifecycle method called when the element is added to the document's DOM.
   */
  connectedCallback() {
    super.connectedCallback();

    if(this.test ) {

      setTimeout( () => {
        
          this.dispatchEvent( new CustomEvent( 'graph', { 
            detail: `
            flowchart TD
            Start --> Stop
            `,
            bubbles: true,
            composed: true,
            cancelable: true
          }));

          this.formMetaData = { 
            input: { type: 'string', required: true }
          }
          
          this.requestUpdate()

      }, 1000 );
      
    }
    else {

      this.#init()

    }

  }

  async #init() {

    const initResponse = await fetch( `${this.url}/init` )

    const initData = await initResponse.json()
    
    console.debug( initData );

    this.dispatchEvent( new CustomEvent( 'graph', { 
      detail: initData.graph,
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
    // console.debug( 'test', this.test )
    
    if(this.test ) {

      setTimeout( () => {

          this.dispatchEvent( new CustomEvent( 'result', { 
            detail: { node: 'node1', state: { property1: "value1", property2: "value2" }},
            bubbles: true,
            composed: true,
            cancelable: true
          } ) );

        }, 1000 );
      
      return
    }
    
    const data = Object.keys(this.formMetaData).reduce( (acc, key) => {
      acc[key] = this.shadowRoot.getElementById(key).value
      return acc
    }, {});

    const execResponse = await fetch(`${this.url}/stream`, {
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
  
}


window.customElements.define('lg4j-executor', LG4JExecutorElement);
