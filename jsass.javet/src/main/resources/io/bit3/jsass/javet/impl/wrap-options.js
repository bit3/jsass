function wrapImporter(importer) {
  if (importer) {
    return {
      canonicalize: function (url, context) {
        const result = importer.canonicalize(url, context)
        return result ? new URL(result) : result
      },
      load: function (canonicalUrl) {
        const result = importer.load(canonicalUrl.toString())
        if (result) {
          return {
            ...result,
            sourceMapUrl: result.sourceMapUrl ? new URL(result.sourceMapUrl) : undefined
          }
        } else {
          return null
        }
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
