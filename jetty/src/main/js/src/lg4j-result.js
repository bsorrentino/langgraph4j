import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

/**
 * @typedef {Object} ResultData
 * @property {string} node - The node identifier.
 * @property {Record<string, any>} state - The state associated with the node.
 */


export class LG4JResultElement extends LitElement {

  static styles = [TWStyles, css``]

  static properties = {
  }

  constructor() {
    super()
    this.results = []
  }
  
  connectedCallback() {
    super.connectedCallback();

    this.addEventListener( 'result', this.#onResult )
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.removeEventListener( 'result',  this.#onResult )
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
      <div class="collapse-title text-xm font-medium">${result.node}</div>
      <div class="collapse-content">
        <table class="table">
          <tbody>
            ${Object.entries(result.state).map(([key, value]) => html`
              <tr>
                <td width="30%">${key}</td>
                <td width="70%">${JSON.stringify(value,4)}</td>
              </tr>
            `)}
          </tbody>
        </table>
      </div>
    </div>
    `
  }

  
  /**
   * Event handler for the 'result' event.
   * 
   * @param {CustomEvent} e - The event object containing the result data.
   * @private
   */
  #onResult = (e) => {

    const { detail: result } = e 
    console.debug( "onResult", e )
    
    // TODO: validate e.detail
    this.results.push( result )

    this.dispatchEvent( new CustomEvent( 'graph-active', { 
      detail: result.node,
      bubbles: true,
      composed: true,
      cancelable: true
    }));
    
    this.requestUpdate()
    
  }


  render() {
  
    return html`
      <div class="flex flex-col gap-y-1.5 mx-2 mt-2">
      ${this.results.map( (result, index) => this.#renderResult(result, index))}
      </div>
    `;
  }
}

window.customElements.define('lg4j-result', LG4JResultElement);
