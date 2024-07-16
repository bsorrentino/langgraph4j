import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

export class LG4JWorkbenchElement extends LitElement {

  static styles = [css`
    .item-graph {
      grid-area: left;
      
      // background-color: red;
    }
    .item-result {
      grid-area: right;
      // background-color: blue;
    }
    .item-executor {
      grid-area: bottom;
      //background-color: yellow;
    }
    .item-container {
      height: 100vh;
      display: grid;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      grid-template-rows: 25% 25%  25%  25% ;
      row-gap: 15px;

      grid-template-areas: 
        "left left right right"
        "left left right right"
        "left left right right"
        "bottom bottom right right";
    }
  `, TWStyles];
  ;

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
<div class="item-container">
  <div class="item-graph border border-gray-300 p-5 flex items-center justify-center" id="panel1"><slot name="graph">LEFT</slot></div>
  <div class="item-result" id="panel3"><slot name="result">RIGHT</slot></div>
  <div class="item-executor" id="panel2"><slot name="executor">BOTTOM</slot></div>
</div>
    `;
  }
}

window.customElements.define('lg4j-workbench', LG4JWorkbenchElement);
