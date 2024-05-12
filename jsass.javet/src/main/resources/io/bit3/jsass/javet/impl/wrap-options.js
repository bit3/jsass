function wrapImporter(importer) {
  if (importer) {
    return {
      canonicalize: function (url, context) {
        const result = importer.canonicalize(url, context)
        const r2 = result ? new URL(result) : result
        console.debug("canonicalize " + url + " to " + r2)
        return r2
      },
      load: function (canonicalUrl) {
        const result = importer.load(canonicalUrl.toString())
        const r2 = result ? {...result, sourceMapUrl: result.sourceMapUrl ? new URL(result.sourceMapUrl) : undefined} : null
        console.debug("load", canonicalUrl, "as", "r2")
        return r2
      }
    }
  }
}

if (globalThis.options.url) {
  globalThis.options.url = new URL(globalThis.options.url)
}

if (globalThis.options.importer) {
  globalThis.options.importer = wrapImporter(globalThis.options.importer)
}

if (globalThis.options.importers) {
  globalThis.options.importers = globalThis.options.importers.map(wrapImporter)
}
