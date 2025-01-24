"use strict";

class KeyValuePairContainer {
  #target;
  #children;

  constructor(id) {
    this.#target = document.getElementById(id);
    this.#children = [];
  }

  get target() {
    return this.#target;
  }

  get children() {
    return this.#children;
  }

  appendChild = () => {
    const kv = new KeyValuePair(this);
    this.#children.push(kv);
    this.#renderChildren();
  };

  applyPairs = (pairs) => {
    if (pairs) {
      for (const [k, v] of Object.entries(pairs)) {
        this.appendChild();
        const child = this.children[this.children.length - 1];
        child.keyElement.value = k;
        child.valueElement.value = v;
      }
    }
  };

  extractPairs = () => {
    const pairs = {};

    for (const child of this.#children) {
      const key = child.keyElement.value;
      const value = child.valueElement.value;

      if (!!key && !pairs[key]) {
        pairs[key] = [];
      }

      if (key && !!value) {
        pairs[key].push(value);
      }
    }

    return pairs;
  };

  #renderChildren = () =>
    this.#target.replaceChildren(...this.#children.map((c) => c.container));
}

class KeyValuePair {
  #container;
  #deleteButton;
  #keyElement;
  #valueElement;

  constructor(parent) {
    this.#container = document.createElement("div");

    this.#deleteButton = document.createElement("button");
    this.#keyElement = document.createElement("input");
    this.#valueElement = document.createElement("input");

    this.#deleteButton.textContent = "X";
    this.#keyElement.placeholder = "key";
    this.#valueElement.placeholder = "value";

    this.#container.appendChild(this.#deleteButton);
    this.#container.appendChild(this.#keyElement);
    this.#container.appendChild(this.#valueElement);

    this.#deleteButton.addEventListener("click", (e) => {
      if (elements.length === 1) {
        this.#keyElement.value = "";
        this.#valueElement.value = "";
        return;
      }
      parent.children = parent.children.filter((c) => c !== this.#container);
      this.#container.remove();
    });

    this.#keyElement.addEventListener("keyup", (e) => {
      if ("Enter" === e.key) {
        this.#valueElement.focus();
      }
    });
    this.#valueElement.addEventListener("keyup", (e) => {
      if ("Enter" === e.key) {
        this.appendKeyValueInputChild(parent);
      }
    });
  }

  get container() {
    return this.#container;
  }

  get keyElement() {
    return this.#keyElement;
  }

  get valueElement() {
    return this.#valueElement;
  }
}

export class Container {
  constructor() {
    this.requestMethod = document.getElementById("req-method");
    this.requestURL = document.getElementById("req-url");
    this.requestHeaders = new KeyValuePairContainer("req-header-children");
    this.requestParams = new KeyValuePairContainer("req-param-children");
    this.requestBody = document.getElementById("body");
    this.responseCode = document.getElementById("res-status-code");
    this.responseHeaders = document.getElementById("res-headers");
    this.responseBody = document.getElementById("res-body");
    this.submit = document.getElementById("submit");
  }
}
