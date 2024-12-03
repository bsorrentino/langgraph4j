
import TWStyles from './twlit.js';

import { html, css, LitElement, CSSResult } from 'lit';

import { imageToDiagram as test } from './lg4j-executor-test.js';

/**
 * @file
 * @typedef {import('./types.js').ResultData} ResultData * 
 * @typedef {import('./types.js').EditEvent} EditEvent
 * @typedef {import('./types.js').UpdatedState} UpdatedState
 * @typedef {import('./types.js').InitData} InitData
 * @typedef {import('./types.js').ArgumentMetadata} ArgumentMetadata
 * 
 */

/**
 * Asynchronously waits for a specified number of milliseconds.
 * 
 * @param {number} ms - The number of milliseconds to wait.
 * @returns {Promise<void>} A promise that resolves after the specified delay.
 */
const delay = async (ms) => (new Promise(resolve => setTimeout(resolve, ms)));

/**
 * Asynchronously fetches data from a given fetch call and yields the data in chunks.
 * @async
 * @generator
 * @param {Response} response
 * @yields {Promise<string>} The decoded text chunk from the response stream.
 */
async function* streamingResponse(response) {
  // Attach Reader
  const reader = response.body?.getReader();

  while (true && reader) {
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
   * @type {Array<CSSResult>}
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
   * @type { import('lit').PropertyDeclarations }
   */
  static properties = {
    url: { type: String, reflect: true },
    test: { type: Boolean, reflect: true },
    _executing: { state: true }

  }

  /**
   * @type {string | null }
   */
  url = null

  /**
   * current selected thread
   * 
   * @type {string|undefined} - thread id
   */
  #selectedThread

  /**
   * current state for update 
   * 
   * @type {UpdatedState|null}
   */
  #updatedState = null


  /**
   * Creates an instance of LG4JInputElement.
   * 
   * @constructor
   */
  constructor() {
    super();
    this.test = false
    /** @type {ArgumentMetadata[]} */
    this.formMetaData = []
    this._executing = false

  }

  /**
   * Event handler for the 'update slected thread' event.
   * 
   * @param {CustomEvent<string>} e - The event object containing the updated data.
   */
  #onThreadUpdated(e) {
    console.debug('thread-updated', e.detail)
    this.#selectedThread = e.detail
    this.#updatedState = null
    this.requestUpdate()
  }

  /**
   * 
   * @param {CustomEvent<UpdatedState>} e - The event object containing the result data.
   */
  #onNodeUpdated(e) {
    console.debug('onNodeUpdated', e)
    this.#updatedState = e.detail
    this.requestUpdate()
  }

  /**
   * Lifecycle method called when the element is added to the document's DOM.
   */
  connectedCallback() {
    super.connectedCallback();

    // @ts-ignore
    this.addEventListener("thread-updated", this.#onThreadUpdated);
    // @ts-ignore
    this.addEventListener('node-updated', this.#onNodeUpdated)

    if (this.test) {
      test.callInit(this)
        .then(data => {
          this.formMetaData = data.args 
          this.requestUpdate()
        })
      return
    }

    this.#callInit()

  }

  disconnectedCallback() {
    super.disconnectedCallback();

    // @ts-ignore
    this.removeEventListener("thread-updated", this.#onThreadUpdated)
    // @ts-ignore
    this.removeEventListener('node-updated', this.#onNodeUpdated)

  }

  /**
   * Renders the HTML template for the component.
   * 
   * @returns The rendered HTML template.
   */
  render() {

    // console.debug( 'render', this.formMetaData )
    return html`
        <div class="container">
          ${this.formMetaData.map(({ name, type }) => {
      switch (type) {
        case 'STRING':
          return html`<textarea id="${name}" class="textarea textarea-primary" placeholder="${name}"></textarea>`
        case 'IMAGE':
          return html`<lg4j-image-uploader id="${name}"></lg4j-image-uploader>`
      }
    })}
          <div class="commands">
            <button id="submit" ?disabled=${this._executing} @click="${this.#callSubmit}" class="btn btn-primary item1">Submit</button>
            <button id="resume" ?disabled=${!this.#updatedState || this._executing} @click="${this.#callResume}" class="btn btn-secondary item2">
            Resume ${this.#updatedState ? '(from ' + this.#updatedState?.node + ')' : ''}
            </button>
          </div>
        </div>
        `;
  }

  async #callInit() {

    const initResponse = await fetch(`${this.url}/init`)

    /** @type {InitData} */
    const initData = await initResponse.json()

    console.debug('initData', initData);

    this.dispatchEvent(new CustomEvent('init', {
      detail: initData,
      bubbles: true,
      composed: true,
      cancelable: true
    }));

    this.formMetaData = initData.args
    // this.#nodes = initData.nodes
    this.requestUpdate()
  }

  async #callResume() {
    this._executing = true
    try {

      if (this.test) {
        await test.callSubmitAction(this, this.#selectedThread);
        return
      }

      await this.#callResumeAction()


    }
    finally {
      this._executing = false
    }

  }

  async #callResumeAction() {

    const execResponse = await fetch(`${this.url}/stream?thread=${this.#selectedThread}&resume=true&node=${this.#updatedState?.node}&checkpoint=${this.#updatedState?.checkpoint}`, {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(this.#updatedState?.data)
    });

    this.#updatedState = null

    for await (let chunk of streamingResponse(execResponse)) {
      console.debug(chunk)

      this.dispatchEvent(new CustomEvent('result', {
        detail: JSON.parse(chunk),
        bubbles: true,
        composed: true,
        cancelable: true
      }));

    }


  }

  async #callSubmit() {

    this._executing = true
    try {

      if (this.test) {
        await test.callSubmitAction(this, this.#selectedThread);
        return
      }

      await this.#callSubmitAction()

    }
    finally {
      this._executing = false
    }
  }

  async #callSubmitAction() {

    // Get input as object
    /** @type { Record<string,any> } */
    const result = {}
    /** @type { Record<string,any> } data */
    const data = this.formMetaData.reduce((acc, md) => {

      const { name, type } = md
      const elem = this.shadowRoot?.getElementById(name)

      switch (type) {
        case 'STRING':
          //@ts-ignore
          acc[name] = elem?.value
          break;
        case 'IMAGE':
          //@ts-ignore
          acc[name] = elem?.value
          break;
      }

      return acc
    }, result);

    const execResponse = await fetch(`${this.url}/stream?thread=${this.#selectedThread}`, {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });

    for await (let chunk of streamingResponse(execResponse)) {
      console.debug(chunk)

      this.dispatchEvent(new CustomEvent('result', {
        detail: JSON.parse(chunk),
        bubbles: true,
        composed: true,
        cancelable: true
      }));

    }

  }

}

window.customElements.define('lg4j-executor', LG4JExecutorElement);
