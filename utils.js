export const encodeRequest = (req) => atob(JSON.stringify(req));
export const decodeRequest = (req) => JSON.parse(btoa(req));

export class KeyController {
  #keys;

  constructor(callback) {
    this.#keys = {};

    window.addEventListener("keydown", (e) => {
      this.#keys[e.key] = true;
    });
    window.addEventListener("keyup", (e) => {
      if (this.#keys["Meta"] && this.#keys["Enter"]) {
        callback();
        this.#keys = {};
      }
      this.#keys[e.key] = false;
    });
  }
}
