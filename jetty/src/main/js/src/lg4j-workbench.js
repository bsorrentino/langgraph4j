import './app.css';

import { html, css, LitElement } from 'lit';

export class LG4JWorkbenchElement extends LitElement {

  static styles = css`
    .item-a {
      grid-area: left;
      background-color: red;
    }
    .item-b {
      grid-area: right;
      background-color: blue;
    }
    .item-c {
      grid-area: bottom;
      //background-color: yellow;
    }
    .container {
      height: 100vh;
      display: grid;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      grid-template-rows: 25% 25%  25%  25% ;

      grid-template-areas: 
        "left left right right"
        "left left right right"
        "left left right right"
        "bottom bottom right right";
    }
  `;

  #routeEvent( e ) {
    
    const { type, detail } = e
    
    console.log( 'routeEvent', type )

    const elem = this.querySelector(`[slot="${type}"]`)
    if( !elem ) {
      console.error( `slot "${type}" not found!` )
      return
    }
    elem.dispatchEvent( new CustomEvent( type, { detail } ) )

  }
  connectedCallback() {
    super.connectedCallback()

    this.addEventListener( "result", this.#routeEvent );
    this.addEventListener( "graph", this.#routeEvent );
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.removeEventListener( "result", this.#routeEvent );
    this.removeEventListener( "graph", this.#routeEvent );
  }

  // firstUpdated() {
  // }
  
  render() {
    return html`
<div class="container">
  <div class="item-a" id="panel1"><slot name="graph">LEFT</slot></div>
  <div class="item-b" id="panel3"><slot name="result">RIGHT</slot></div>
  <div class="item-c border border-gray-200" id="panel2"><slot name="executor">BOTTOM</slot></div>
</div>
    `;
  }
}

window.customElements.define('lg4j-workbench', LG4JWorkbenchElement);
