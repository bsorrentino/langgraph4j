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

  /**
   * Event handler for the 'init' event.
   * 
   * @param {CustomEvent} e - The event object containing init data.
   * @private
   */

  #routeInitEvent( e ) {
      const { graph, title, threads  } = e.detail 

      this.#routeEvent( new CustomEvent( "graph", { detail: graph }));
      this.#routeEvent( new CustomEvent( "init-threads", { detail: threads }), 'result');

      if( title ) {
        this.title = title
        this.requestUpdate()
      }
  }

  
  /**
   * Event handler for the 'updates' event.
   * 
   * @param {CustomEvent} e - The event object containing the updated data.
   * @private
   */
  #routeUpdateEvent( e ) {
    console.debug( 'got updated event', e );
    this.#routeEvent( new CustomEvent( `${e.type}`, { detail: e.detail }), 'executor');
  }

  connectedCallback() {
    super.connectedCallback()

    this.addEventListener( "update-thread", this.#routeUpdateEvent );
    this.addEventListener( "init", this.#routeInitEvent );
    this.addEventListener( "result", this.#routeEvent );
    this.addEventListener( "graph-active", this.#routeEvent );
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.removeEventListener( "update-thread", this.#routeUpdateEvent );
    this.removeEventListener( "init", this.#routeInitEvent );
    this.removeEventListener( "result", this.#routeEvent );
    this.removeEventListener( "graph-active", this.#routeEvent );
  }

  // firstUpdated() {
  // }
  
  render() {
    return html`
<div class="h-screen">

  <div class="navbar bg-base-100">
    <a class="btn btn-ghost text-xl">${this.title}</a>
  </div>

  <div class="h-full grid gap-x-2 gap-y-9 grid-cols-2 grid-rows-5 ">    
    <div class="row-span-3 border border-gray-300"><slot name="graph">LEFT</slot></div>
    <div class="row-span-5"><slot name="result">RIGHT</slot></div>
    <div class=" border-slate-50"><slot name="executor">BOTTOM</slot></div>
  </div>
</div>
    `;
  }
}

window.customElements.define('lg4j-workbench', LG4JWorkbenchElement);
