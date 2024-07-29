import mermaid from 'mermaid';
//const mermaidAPI = mermaid.mermaidAPI;
import * as d3 from 'd3'

/**
 * WcMermaid
 * @class
 */
export class LG4jMermaid extends HTMLElement {

  constructor() {
    super();

    
    mermaid.initialize({
      logLevel: 'none',
      startOnLoad: false,
      theme: 'dark',
      flowchart: {
        useMaxWidth: false
      }
    });

    this._content = null
    this._activeClass = null
    this._lastTransform = null


    const shadowRoot = this.attachShadow({ mode: "open" });

    const style = document.createElement("style");
    style.textContent = `
    :host {
      display: block;
      width: 100%;
      height: 100%;
    }
    .h-full {
      height: 100%;
    }
    .w-full {
      width: 100%;
    }
    .flex {
      display: flex;
    }
    .items-center {
      align-items: center;
    }
    .justify-center {
      justify-content: center;
    }
    .bg-neutral {
      --tw-bg-opacity: 1;
      background-color: var(--fallback-n,oklch(var(--n)/var(--tw-bg-opacity)));
  }
    `
    shadowRoot.appendChild(style);

    const container = document.createElement('div')
    container.classList.add("h-full");
    container.classList.add("w-full");
    container.classList.add("flex");
    container.classList.add("items-center");
    container.classList.add("justify-center");
    container.classList.add("bg-neutral");
    container.classList.add("mermaid");

    // const pre = document.createElement('pre')
    // pre.classList.add("mermaid");
    // container.appendChild( pre );

    shadowRoot.appendChild( container );

    this.#renderDiagram()

  }


  
  /**
   * @returns {ChildNode[]}
   * @private
   */
  get #textNodes() {
    return Array.from(this.childNodes).filter(
      node => node.nodeType === this.TEXT_NODE
    );
  }

  /**
   * @returns {string}
   * @private
   */
  get #textContent() {
    
    if( this._content ) {

      if( this._activeClass ) {
        return `
        ${this._content}
        classDef ${this._activeClass} fill:#f96
        `
      }
      
      return this._content
    }

    return "flowchart TD" //this.#textNodes.map(node => node.textContent?.trim()).join('');
  }


  async #renderDiagram( ) {
    const svgContainer = this.shadowRoot.querySelector('.mermaid')

    // console.debug( svgContainer );
    return mermaid.render( 'graph', this.#textContent )
    
        .then( res => { 
          console.debug( "RENDER COMPLETE", svgContainer );
          // svgContainer.innerHTML = res.svg
          const { right: width,  bottom: height } = svgContainer.getBoundingClientRect();
          // console.debug( res.svg )
          const translated = res.svg
            .replace( /height="[\d\.]+"/, `height="${height}"`) 
            .replace( /width="[\d\.]+"/, `width="${width}"`);
          // console.debug( translated );
          svgContainer.innerHTML = translated;
        })
        .then( () => this.#svgPanZoom() )
        .catch( e => console.error( "RENDER ERROR", e ) )

  }

  #svgPanZoom() {

    console.debug( '_lastTransform', this._lastTransform )
    const svgs = d3.select( this.shadowRoot ).select(".mermaid svg");
    // console.debug( 'svgs', svgs )

    const self = this;

    svgs.each( function() {
      // 'this' refers to the current DOM element
      const svg = d3.select(this);
      
      // console.debug( 'svg', svg );
      svg.html("<g>" + svg.html() + "</g>");

      const inner = svg.select("g");
      // console.debug( 'inner', inner )
   
      const zoom = d3.zoom().on("zoom", event => {
          inner.attr("transform", event.transform);
          self._lastTransform = event.transform;
        }); 
      
      const selection = svg.call(zoom);

      if( self._lastTransform !== null ) {
        inner.attr("transform", self._lastTransform)
        // [D3.js Set initial zoom level](https://stackoverflow.com/a/46437252/521197)
        selection.call(zoom.transform, self._lastTransform);
      }  

    });

  }

  #onContent(e) {
    const { detail: newContent } = e

    this._content = newContent
    this.#renderDiagram()
  }

  #onActive(e) {
    
    const { detail: activeClass } = e
    
    this._activeClass = activeClass;
    this.#renderDiagram()
  }

  #resizeHandler = () => this.#renderDiagram()

  connectedCallback() {
    
    this.addEventListener('graph', this.#onContent)
    this.addEventListener('graph-active', this.#onActive)
    window.addEventListener('resize', this.#resizeHandler )

  }

  disconnectedCallback() {

    this.removeEventListener('graph', this.#onContent)
    this.removeEventListener('graph-active', this.#onActive)
    window.removeEventListener('resize', this.#resizeHandler )
  }

  // @deprecated
  async #renderDiagramWithRun( ) {

    // console.debug( this.shadowRoot )
    const pres = this.shadowRoot.querySelectorAll('.mermaid')
    // console.debug( pres )
    pres[0].textContent = this.#textContent ;

    return mermaid.run( {
          nodes:  pres , // this.shadowRoot.querySelectorAll('.mermaid'),
          suppressErrors: true
        })
        .then( () => console.debug( "RUN COMPLETE") )
        .then( () => this.#svgPanZoom() )
        .catch( e => console.error( "RUN ERROR", e ) )

  }


}

window.customElements.define('lg4j-graph', LG4jMermaid);