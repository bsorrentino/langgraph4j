import { html, svg, LitElement } from 'lit';
import { Task } from '@lit/task'
import mermaid from 'mermaid';
import { unsafeSVG } from 'lit/directives/unsafe-svg.js';
//const mermaidAPI = mermaid.mermaidAPI;


/**
 * WcMermaid
 * @class
 */
export class LG4jMermaid extends LitElement {


  constructor() {
    super();

    mermaid.initialize({
      logLevel: 'none',
      startOnLoad: false,
      theme: 'dark'
    });

    this._content = null
    this._activeClass = null
  }

  #mermaidTask = new Task(this, {
    task: async ([textContent], { signal }) => {
      return await mermaid.render(
        `graph-${Date.now()}-${Math.floor(Math.random() * 10000)}`,
        textContent);

    },
    args: () => [this.#textContent]
  })

  #renderSVG = (diagram) => html`
  <div>
  ${unsafeSVG(diagram.svg)}
  </div>`;

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

    return this.#textNodes.map(node => node.textContent?.trim()).join('');
  }

  #onContent(e) {
    const { detail: newContent } = e

    this._content = newContent
    this.requestUpdate()
  }

  #onActive(e) {
    
    const { detail: activeClass } = e
    
    this._activeClass = activeClass;
    this.requestUpdate()
  }

  connectedCallback() {
    super.connectedCallback()
    
    this.addEventListener('graph', this.#onContent)
    this.addEventListener('graph-active', this.#onActive)
    // this.__observer = new MutationObserver(() => {
    //   this.__observeTextNodes();
    //   this.__renderGraph();
    // });
    // this.__observer.observe(this, { childList: true });
    // this.__observeTextNodes();
    // this.__renderGraph();
  }

  disconnectedCallback() {
    super.disconnectedCallback()

    this.removeEventListener('graph', this.#onContent)
    this.removeEventListener('graph-active', this.#onActive)
    //   this.__cleanTextNodeObservers();

    //   if (this.__observer) {
    //     this.__observer.disconnect();
    //     this.__observer = null;
    //   }
    // }
  }


  render() {

    return this.#mermaidTask.render({
      pending: () => html`<p>rendering diagram...</p>`,
      complete: this.#renderSVG,
      error: (e) => html`<p>Error: ${e}</p>`
    });
  }

  // __observeTextNodes() {
  //   if (this.__textNodeObservers) {  
  //     this.__cleanTextNodeObservers();
  //   }

  //   this.__textNodeObservers = this.__textNodes.map(textNode => {
  //     const observer = new MutationObserver(this.__renderGraph);

  //     observer.observe(textNode, { characterData: true });

  //     return observer;
  //   });
  // }

  // __cleanTextNodeObservers() {
  //   if (this.__textNodeObservers) {
  //     this.__textNodeObservers.forEach(observer => observer.disconnect());
  //   }
  // }


}

window.customElements.define('lg4j-graph', LG4jMermaid);