import { LitElement, html, css } from 'lit';

class ImageUploader extends LitElement {
  static styles = css`
    :host {
      display: block;
      font-family: Arial, sans-serif;
    }
    .uploader {
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      gap: 10px;
      border: 1px dashed #ccc;
      border-radius: 4px;
      padding: 1px;
      text-align: center;
      cursor: pointer;
    }
    .uploader:hover {
      border-color: #aaa;
    }
    input[type="file"] {
      display: none;
    }
    .thumbnail {
      width: 50px; /* Fixed width */
      height: 50px; /* Fixed height */
      object-fit: cover; /* Maintain aspect ratio, crop if necessary */
      border-radius: 8px; /* Optional: rounded corners */
      margin-top: 10px;
    }
  `;

  static properties = {
    // _imageUrl: { state: true },
    _file: { state: true }, // Holds the uploaded file
    nopreview: { attribute: 'no-preview', type: Boolean } // New reactive property
  };

  constructor() {
    super();
    // this._imageUrl = null;
    // this._fileName = null;
    this.nopreview = false; 
    this._file = null
  }

  get value() {
    return this._file
  }
  
  get #imageUrl() {
    return this._file && URL.createObjectURL(this._file);  
  }

  render() {
    return html`
      <div class="uploader" @click=${this._openFileDialog}>
        <input type="file" @change=${this._handleFileUpload} accept="image/*">
        ${this.#imageUrl
          ? this.nopreview
            ? html`<p>Uploaded File: ${this._file.name}</p>` 
            : html`<p>Uploaded Image:</p><img class="thumbnail" src=${this.#imageUrl} alt="Uploaded image">`
          : html`<p>Click here or drag and drop an image file</p>`}
      </div>
    `;
  }

  _openFileDialog() {
    /**  @type {HTMLInputElement|null|undefined} */
    const input = this.shadowRoot?.querySelector('input[type="file"]')
    input?.click();
  }

  /**
   *
   * @param {Event} e  event
   */
  _handleFileUpload(e) {
    // @ts-ignore
    const file = e.target?.files[0];
    if (file && file.type.startsWith('image/')) {
      this._file = file; // Set the file name
      // const reader = new FileReader();
      // reader.onload = (event) => {
      //   this._imageUrl = event.target.result;
      // };
      // reader.readAsDataURL(file);
    } else {
      alert('Please select a valid image file.');
    }
  }
}

customElements.define('lg4j-image-uploader', ImageUploader);