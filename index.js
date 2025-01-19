"use strict";

import { encodeRequest, decodeRequest, KeyController } from "./utils.js";

const container = {
  request: {
    instance: document.getElementById("request-container"),
    method: {
      instance: document.getElementById("req-method-container"),
      target: document.getElementById("req-method"),
    },
    url: {
      instance: document.getElementById("req-url-container"),
      target: document.getElementById("req-url"),
    },
    header: {
      instance: document.getElementById("req-header-container"),
      target: document.getElementById("req-header-children"),
      children: [],
    },
    param: {
      instance: document.getElementById("req-param-container"),
      target: document.getElementById("req-param-children"),
      children: [],
    },
  },
  response: {
    instance: document.getElementById("response-container"),
    statusCode: {
      instance: document.getElementById("res-status-code-container"),
      target: document.getElementById("res-status-code"),
    },
    header: {
      instance: document.getElementById("res-headers-container"),
      target: document.getElementById("res-headers"),
    },
    body: {
      instance: document.getElementById("res-body-container"),
      target: document.getElementById("res-body"),
    },
  },
  submit: {
    instance: document.getElementById("submit-container"),
    target: document.getElementById("submit"),
  },
};

const appendKeyValueInputChild = (container) => {
  const kvContainer = document.createElement("div");
  const deleteButton = document.createElement("button");
  const ke = document.createElement("input");
  const ve = document.createElement("input");

  deleteButton.textContent = "X";
  ke.placeholder = "key";
  ve.placeholder = "value";

  deleteButton.addEventListener("click", (e) => {
    if (container.children.length === 1) {
      ke.value = "";
      ve.value = "";
      return;
    }
    container.children = container.children.filter(
      (child) => child !== kvContainer
    );
    renderChildren(container);
  });
  ke.addEventListener("keyup", (e) => {
    if ("Enter" === e.key) {
      ve.focus();
    }
  });
  ve.addEventListener("keyup", (e) => {
    if ("Enter" === e.key) {
      appendKeyValueInputChild(container);
    }
  });

  kvContainer.appendChild(deleteButton);
  kvContainer.appendChild(ke);
  kvContainer.appendChild(ve);
  container.children.push(kvContainer);

  renderChildren(container);
  ke.focus();
};

const renderChildren = (container) => {
  container.target.replaceChildren(...container.children);
};

const renderResponse = (data, { statusCode, headers, body }) => {
  console.log(data);

  statusCode.textContent = data.statusCode;
  headers.textContent = JSON.stringify(data.headers, null, 2);
  body.textContent = data.body;

  if (!statusCode.textContent && !header.textContent) {
    body.textContent = JSON.stringify(data, null, 2);
  }
};

const handleFetchException = (e) => {
  console.error(e);
  container.response.body.target.textContent = e;
};

const fetchViaProxy = (options) => {
  fetch("/relay", { ...options, body: JSON.stringify(options.body) })
    .then((res) => res.json())
    .then((data) => {
      renderResponse(data, {
        statusCode: container.response.statusCode.target,
        headers: container.response.header.target,
        body: container.response.body.target,
      });
    })
    .catch(handleFetchException);
};

const fetchViaLocal = (options) => {
  const statusCode = {};
  const responseHeaders = {};

  saveLastRequest(options.body);

  fetch(options.body.url, {
    method: options.body.method,
    headers: { ...options.body.headers },
  })
    .then((res) => {
      res.headers.forEach((v, k) => {
        if (!responseHeaders[k]) {
          responseHeaders[k] = [];
        }
        responseHeaders[k].push(v);
      });
      statusCode.value = res.status;

      return res.text();
    })
    .then((data) => {
      renderResponse(
        {
          statusCode: statusCode.value,
          headers: responseHeaders,
          body: data,
        },
        {
          statusCode: container.response.statusCode.target,
          headers: container.response.header.target,
          body: container.response.body.target,
        }
      );
    })
    .catch(handleFetchException);
};

const LAST_REQUEST_STORAGE_KEY = "__last_request__";
const saveRequest = (name, data) =>
  localStorage.setItem(name, JSON.stringify(data));
const saveLastRequest = (data) => saveRequest(LAST_REQUEST_STORAGE_KEY, data);
const loadRequest = (name) => JSON.parse(localStorage.getItem(name));
const loadLastRequest = () => loadRequest(LAST_REQUEST_STORAGE_KEY);

document.addEventListener("DOMContentLoaded", () => {
  new KeyController(() => container.submit.target.click());

  const lastRequest = loadLastRequest();
  if (lastRequest) {
    container.request.method.target.value = lastRequest.method;
    container.request.url.target.value = lastRequest.url;

    if (lastRequest.headers) {
      for (const [k, v] of Object.entries(lastRequest.headers)) {
        appendKeyValueInputChild(container.request.header);
        const children = container.request.header.children;
        children[children.length - 1].children[1].value = v;
      }
    }

    if (lastRequest.params) {
      for (const [k, v] of Object.entries(lastRequest.params)) {
        appendKeyValueInputChild(container.request.param);
        const children = container.request.param.children;
        children[children.length - 1].children[1].value = v;
      }
    }

    if (!!lastRequest.body) {
      container.request.body.target.value = lastRequest.body;
    }
  }

  document.getElementById("save").addEventListener("click", () => {
    const shouldSave = (name) => {
      if (localStorage.getItem(name)) {
        return confirm("do you want to overwrite?");
      }
      return true;
    };
    const name = prompt(
      "name your api",
      new Date().toISOString() + " " + container.request.url.target.value
    );
    const data = {
      method: container.request.method.target.value,
      url: container.request.url.target.value,
      headers: {
        ...Object.fromEntries(
          container.request.header.children.map((child) => [
            child.children[0].value,
            child.children[1].value,
          ])
        ),
      },
      params: {
        ...Object.fromEntries(
          container.request.param.children.map((child) => [
            child.children[0].value,
            child.children[1].value,
          ])
        ),
      },
      body: container?.request?.body?.target?.value || null,
    };

    shouldSave(name) && localStorage.setItem(name, JSON.stringify(data));
  });

  container.submit.target.addEventListener("click", (e) => {
    if (!container.request.url.target.value) {
      return;
    }

    const method = container.request.method.target.value || "GET";
    const url = (() => {
      const inputValue = container.request.url.target.value;
      return inputValue.startsWith("http")
        ? inputValue
        : "http://" + inputValue;
    })();

    const headers = {};
    for (const child of container.request.header.children) {
      const k = child.children[0].value;
      const v = child.children[1].value;

      if (k && v) {
        if (!headers[k]) {
          headers[k] = [];
        }
        headers[k].push(v);
      }
    }

    const queryParams = {};
    for (const child of container.request.param.children) {
      const k = child.children[0].value;
      const v = child.children[1].value;

      if (k && v) {
        if (!headers[k]) {
          queryParams[k] = [];
        }
        queryParams[k].push(v);
      }
    }

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: { method, url, headers, queryParams },
    };

    console.log(options.body);
    container.response.statusCode.target.textContent = "";
    container.response.header.target.textContent = "";
    container.response.body.target.textContent = "";

    fetchViaLocal(options);
  });

  appendKeyValueInputChild(container.request.header);
  appendKeyValueInputChild(container.request.param);

  container.request.url.target.focus();
});
