new Promise((resolve, reject) => {
  try {
    const {css, sourceMap} = sass.compileString(globalThis.source, globalThis.options);
    resolve({ css, "sourceMap": JSON.stringify(sourceMap) })
  } catch (e) {
    reject(e)
  }
})
