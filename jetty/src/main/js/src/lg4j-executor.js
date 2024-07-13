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
      flex-direction: row;
    }
  `];

  /**
   * Properties of the component.
   * 
   * @static
   * @type {Object}
   */
  static properties = {
    placeholder: {},
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
    this.placeholder = "prompt";
    this.test = false
  }

  /**
   * Lifecycle method called when the element is added to the document's DOM.
   */
  connectedCallback() {
    super.connectedCallback();

    if( this.test ) {
      setTimeout( () => 
        this.dispatchEvent( new CustomEvent( 'graph', { 
          detail: `
          flowchart TD
          Start --> Stop
          `,
          bubbles: true,
          composed: true,
          cancelable: true
        })), 1000 );
      }
  
  }

  /**
   * Renders the HTML template for the component.
   * 
   * @returns {TemplateResult} The rendered HTML template.
   */
  render() {
    return html`
        <div class="container">
          <textarea id="prompt" class="textarea textarea-bordered" placeholder="${this.placeholder}"></textarea>
          <button @click="${this.#submit}" class="btn btn-primary">Submit</button>
        </div>
    `;
  }

  get #prompt() {
    // console.debug(  ' --> ' + this.shadowRoot.getElementById('prompt') )
    return this.shadowRoot.getElementById('prompt').value
  }

  async #submit() {
    // console.debug( 'test', this.test )
    if(this.test ) {
      this.dispatchEvent( new CustomEvent( 'result', { 
        detail: `TEST: ${this.#prompt}`,
        bubbles: true,
        composed: true,
        cancelable: true
      } ) );
      return
    }

    const data = { 'prompt': this.#prompt }

    
    const execResponse = await fetch(`${this.url}/stream`, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    const graphResponse = await fetch( `${this.url}/graph` )

    const graphText = await graphResponse.text()

    this.dispatchEvent( new CustomEvent( 'graph', { 
      detail: graphText,
      bubbles: true,
      composed: true,
      cancelable: true
    }));

    for await (let chunk of streamingResponse( execResponse )  ) {
      console.debug( chunk )

      this.dispatchEvent( new CustomEvent( 'result', { 
        detail: chunk,
        bubbles: true,
        composed: true,
        cancelable: true
      } ) );

    }
  }
  
}


window.customElements.define('lg4j-executor', LG4JExecutorElement);
