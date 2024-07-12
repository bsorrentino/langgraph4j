import TWStyles from './twlit';

import { html, css, LitElement } from 'lit';


export class MyInputElement extends LitElement {

  static styles = [  TWStyles, css`
    .container {
      display: flex;
      flex-direction: row;
    }
    `]
  // createRenderRoot() {
  //   return this; // turn off shadow dom to access external styles. Now this element can use DaisyUI
  // }

  connectedCallback() {
    super.connectedCallback();
  }

  render() {
    return html`
        <div class="container">
          <input type="text" placeholder="Type here is" class="input w-full max-w-xs" />
          <button class="btn">Button</button>
        </div>
    `;
  }
}

window.customElements.define('my-input', MyInputElement);
