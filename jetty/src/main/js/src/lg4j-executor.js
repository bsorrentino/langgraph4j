import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

/**
 * Asynchronously fetches data from a given fetch call and yields the data in chunks.
 * 
 * @async
 * @generator
 * @param {Function} fetchcall - A function that returns a Promise resolving to a Response object.
 * @yields {Promise<string>} The decoded text chunk from the response stream.
 */
async function* streamingFetch(fetchcall) {
  const response = await fetchcall();
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
    url: {}
  }

  /**
   * Creates an instance of LG4JInputElement.
   * 
   * @constructor
   */
  constructor() {
    super();
    this.placeholder = "prompt";
  }

  /**
   * Lifecycle method called when the element is added to the document's DOM.
   */
  connectedCallback() {
    super.connectedCallback();

  }


  /**
   * Renders the HTML template for the component.
   * 
   * @returns {TemplateResult} The rendered HTML template.
   */
  render() {
    return html`
        <div class="container">
          <textarea class="textarea textarea-bordered" placeholder="${this.placeholder}"></textarea>
          <button @click="${this.#submit}" class="btn btn-primary">Submit</button>
        </div>
    `;
  }

  async #submit() {

    // this.dispatchEvent( new CustomEvent( 'result', { 
    //   detail: "TEST",
    //   bubbles: true,
    //   composed: true,
    //   cancelable: true
    // } ) );


    for await (let chunk of streamingFetch(() => fetch(`${this.url}`))) {
      console.log(chunk)

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
