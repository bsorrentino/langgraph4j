import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';


export class LG4JResultElement extends LitElement {

  static styles = [TWStyles, css`
    .container {
      display: flex;
      flex-direction: row;
    }
    `]

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

  #renderResult(result) {
    return html`
    <div class="collapse collapse-arrow join-item border-base-300 border">
      <input type="radio" name="my-accordion-4" checked="checked" />
      <div class="collapse-title text-xl font-medium">Click to open this one and close others</div>
      <div class="collapse-content">
        <p>${result}</p>
      </div>
    </div>
    `
  }

  #onResult = ( e ) => {

    console.debug( "onResult", e )
    
    this.results.push( e.detail )
    this.requestUpdate()
    
  }
  render() {
    return html`
      <div class="join join-vertical w-full">
      ${this.results.map(result => this.#renderResult(result))}
      </div>
    `;
  }
}

window.customElements.define('lg4j-result', LG4JResultElement);
