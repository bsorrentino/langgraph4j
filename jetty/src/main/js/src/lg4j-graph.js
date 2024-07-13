import { html, svg, LitElement } from 'lit';
import { Task } from '@lit/task'
import mermaid from 'mermaid';
import { unsafeSVG } from 'lit/directives/unsafe-svg.js';
const mermaidAPI = mermaid.mermaidAPI;


const renderSVG = (diagram) => html`<div>${unsafeSVG(diagram.svg)}</div>`;

/**
 * WcMermaid
 * @class
 */
export class LG4jMermaid extends LitElement {


  constructor() {
    super();

    mermaidAPI.initialize({
      logLevel: 'none',
      startOnLoad: false,
    });

    this._content = null
  }

  #mermaidTask = new Task(this, {
    task: async ([textContent], { signal }) => {
      return await mermaidAPI.render(
        'graph',
        textContent);

    },
    args: () => [this.#textContent]
  })
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
    return this._content ?? this.#textNodes.map(node => node.textContent?.trim()).join('');
  }

  #onContent(e) {
    this._content = e.detail
    this.requestUpdate()
  }

  connectedCallback() {
    super.connectedCallback()
    
    this.addEventListener('graph', this.#onContent)
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
      complete: renderSVG,
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