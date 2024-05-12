function URL(url) { this.url = url; }
URL.prototype.toString = function() {
  return this.url;
};
globalThis.URL = URL;
