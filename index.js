"use strict";

import { KeyController } from "./utils.js";
import { Container } from "./viewBinder.js";

const container = new Container();

const renderResponse = (data) => {
  console.log(data);

  container.responseCode.textContent = data.statusCode;
  container.responseHeaders.textContent = JSON.stringify(data.headers, null, 2);
  container.responseBody.textContent = data.body;

  if (
    !container.responseCode.textContent &&
    !container.responseHeaders.textContent
  ) {
    container.responseBody.textContent = JSON.stringify(data, null, 2);
  }
};

const handleFetchException = (e) => {
  console.error(e);
  container.responseBody.textContent = e;
};

const executeAPI = (payload) => {
  console.log(payload);
  saveLastRequest(payload);

  const statusCode = {};
  const responseHeaders = {};

  const url = new URL(payload.url);
  if (payload.params) {
    for (const [key, values] of Object.entries(payload.params)) {
      for (const value of values) {
        url.searchParams.append(key, value);
      }
    }
  }

  fetch(url, {
    method: payload.method,
    headers: { ...payload.headers },
    body: payload.body,
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
    .then((data) =>
      renderResponse({
        statusCode: statusCode.value,
        headers: responseHeaders,
        body: data,
      })
    )
    .catch(handleFetchException);
};

const LAST_REQUEST_STORAGE_KEY = "__last_request__";
const saveRequest = (name, data) =>
  localStorage.setItem(name, JSON.stringify(data));
const saveLastRequest = (data) => saveRequest(LAST_REQUEST_STORAGE_KEY, data);
const loadRequest = (name) => JSON.parse(localStorage.getItem(name));
const loadLastRequest = () => loadRequest(LAST_REQUEST_STORAGE_KEY);

document.addEventListener("DOMContentLoaded", () => {
  new KeyController(() => container.submit.click());

  const lastRequest = loadLastRequest();
  if (lastRequest) {
    container.requestMethod.value = lastRequest.method;
    container.requestURL.value = lastRequest.url;

    if (!!lastRequest.headers) {
      container.requestHeaders.applyPairs(lastRequest.headers);
    }

    if (!!lastRequest.params) {
      container.requestParams.applyPairs(lastRequest.params);
    }

    if (!!lastRequest.body) {
      container.requestBody.textContent = lastRequest.body
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
      new Date().toISOString() + " " + container.requestURL.value
    );

    const data = {
      method: container.requestMethod.value,
      url: container.requestURL.value,
      headers: container.requestHeaders.extractPairs(),
      params: container.requestParams.extractPairs(),
      body: container.requestBody.value || null,
    };

    shouldSave(name) && saveRequest(name, data);
  });

  container.submit.addEventListener("click", (e) => {
    if (!container.requestURL.value) {
      return;
    }

    const method = container.requestMethod.value || "GET";
    const url = (() => {
      const inputValue = container.requestURL.value;
      return inputValue.startsWith("http")
        ? inputValue
        : "http://" + inputValue;
    })();

    const headers = container.requestHeaders.extractPairs();
    const params = container.requestParams.extractPairs();

    container.responseCode.textContent = "";
    container.responseHeaders.textContent = "";
    container.responseBody.textContent = "";

    executeAPI({
      method,
      url,
      headers,
      params,
      body: container.requestBody.value || null,
    });
  });

  container.requestHeaders.appendChild();
  container.requestParams.appendChild();
  container.requestURL.focus();
});
