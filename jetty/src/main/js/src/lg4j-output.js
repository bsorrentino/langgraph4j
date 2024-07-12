import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';


export class LG4JOutputElement extends LitElement {

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

  get executor() {
    const elem = this.parentElement.querySelector('lg4j-executor')
    if( !elem ) throw new Error( `element lg4j-executor not found!` )
    return elem 
  }
  
  connectedCallback() {
    super.connectedCallback();

    this.executor.addEventListener( 'result', this.#onResult )
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.executor.removeEventListener( 'result',  this.#onResult )
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
    console.log( "onResult", e )
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

window.customElements.define('lg4j-output', LG4JOutputElement);
