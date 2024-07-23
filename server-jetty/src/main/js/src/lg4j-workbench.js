import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';

export class LG4JWorkbenchElement extends LitElement {

  static styles = [css``, TWStyles];

  static properties = {
    title: {},
  }

  constructor() {
    super();

    
  }

  #routeEvent( e, slot ) {
    
    const { type, detail } = e
    
    if( !slot ) {
      slot = type.split('-')[0]
    }

    console.debug( 'routeEvent', type, slot )
    
    const event = new CustomEvent( type, { detail } );

    const elem = this.querySelector(`[slot="${slot}"]`)
    if( !elem ) {
      console.error( `slot "${slot}" not found!` )
      return
    }
    elem.dispatchEvent( event )

  }

  #routeInitEvent( e ) {
      const { graph, title } = e.detail 

      this.#routeEvent( new CustomEvent( "graph", { detail: graph }));

      if( title ) {
        this.title = title
        this.requestUpdate()
      }
  }

  connectedCallback() {
    super.connectedCallback()

    this.addEventListener( "init", this.#routeInitEvent );
    this.addEventListener( "result", this.#routeEvent );
    this.addEventListener( "graph-active", this.#routeEvent );
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.removeEventListener( "init", this.#routeInitEvent );
    this.removeEventListener( "result", this.#routeEvent );
    this.removeEventListener( "graph-active", this.#routeEvent );
  }

  // firstUpdated() {
  // }
  
  render() {
    return html`
<div class="grid grid-cols-2 gap-y-2 grid-rows-[60px_auto_auto_auto_auto_auto] h-screen">
  <div class="col-span-2">
    <div class="navbar bg-base-100">
      <a class="btn btn-ghost text-xl">${this.title}</a>
    </div>
  </div>  
  <div class="row-span-4 border border-gray-300 p-2"><slot name="graph">LEFT</slot></div>
  <div class="row-span-5"><slot name="result">RIGHT</slot></div>
  <div><slot name="executor">BOTTOM</slot></div>
</div>
    `;
  }
}

window.customElements.define('lg4j-workbench', LG4JWorkbenchElement);
