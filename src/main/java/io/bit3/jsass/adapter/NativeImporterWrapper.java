package io.bit3.jsass.adapter;

import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.ImportException;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.JsassCustomHeaderImporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

class NativeImporterWrapper {
  private static final Logger LOG = LoggerFactory.getLogger(NativeImporterWrapper.class);

  private final ImportStack importStack;
  private final Importer importer;

  public NativeImporterWrapper(ImportStack importStack, Importer importer) {
    this.importStack = importStack;
    this.importer = importer;
  }

  public Collection<NativeImport> apply(String url, NativeImport previousNative) {
    try {
      Import previous = new Import(
          previousNative.importPath,
          previousNative.absolutePath,
          previousNative.contents,
          previousNative.sourceMap
      );

      boolean isNotJsassCustomImporter = !(importer instanceof JsassCustomHeaderImporter);
      Collection<Import> imports = this.importer.apply(url, previous);

      if (null == imports) {
        return null;
      }

      Collection<NativeImport> nativeImports = new LinkedList<>();

      for (Import importObject : imports) {
        if (isNotJsassCustomImporter) {
          NativeImport preImport = createPreImport(importObject);
          nativeImports.add(preImport);
        }

        nativeImports.add(new NativeImport(importObject));

        if (isNotJsassCustomImporter) {
          NativeImport postImport = createPostImport(importObject);
          nativeImports.add(postImport);
        }
      }

      return nativeImports;
    } catch (Throwable throwable) {
      LOG.warn(throwable.getMessage(), throwable);
      NativeImport nativeImport = new NativeImport(throwable);
      return Collections.singletonList(nativeImport);
    }
  }

  private NativeImport createPreImport(Import importSource) {
    int id = importStack.register(importSource);

    StringBuilder preSource = new StringBuilder();

    // $jsass-void: jsass_import_stack_push(<id>);
    preSource.append(
        String.format(
            "$jsass-void: jsass_import_stack_push(%d);%n",
            id
        )
    );

    try {
      return new NativeImport(
          new Import(
              new URI(importSource.getAbsoluteUri() + "/JSASS_PRE_IMPORT.scss"),
              new URI(importSource.getAbsoluteUri() + "/JSASS_PRE_IMPORT.scss"),
              preSource.toString()
          )
      );
    } catch (URISyntaxException e) {
      throw new ImportException(e);
    }
  }

  private static NativeImport createPostImport(Import importSource) {
    StringBuilder postSource = new StringBuilder();

    // $jsass-void: jsass_import_stack_pop();
    postSource
        .append("$jsass-void: jsass_import_stack_pop();")
        .append(System.lineSeparator());

    try {
      return new NativeImport(
          new Import(
              new URI(importSource.getAbsoluteUri() + "/JSASS_POST_IMPORT.scss"),
              new URI(importSource.getAbsoluteUri() + "/JSASS_POST_IMPORT.scss"),
              postSource.toString()
          )
      );
    } catch (URISyntaxException e) {
      throw new ImportException(e);
    }
  }
}
