package io.bit3.jsass.adapter;

import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.JsassCustomHeaderImporter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

class NativeImporterWrapper {
  private final ImportStack importStack;
  private final Importer importer;

  public NativeImporterWrapper(ImportStack importStack, Importer importer) {
    this.importStack = importStack;
    this.importer = importer;
  }

  public Collection<NativeImport> apply(String url, NativeImport previousNative) {
    try {
      Import previous = new Import(
          previousNative.uri,
          previousNative.base,
          previousNative.contents,
          previousNative.sourceMap
      );

      boolean isNotJsassCustomImporter = !(importer instanceof JsassCustomHeaderImporter);
      Collection<Import> imports = this.importer.apply(url, previous);

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
    } catch (Throwable e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  private NativeImport createPreImport(Import importSource) {
    int id = importStack.register(importSource);

    StringBuilder preSource = new StringBuilder();

    // $jsass-void: jsass_import_stack_push(<id>) !global;
    preSource.append(
        String.format(
            "$jsass-void: jsass_import_stack_push(%d) !global;\n",
            id
        )
    );

    try {
      return new NativeImport(
          new Import(
              new URI(importSource.getUri() + "/JSASS_PRE_IMPORT.scss"),
              importSource.getUri(),
              preSource.toString()
          )
      );
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private NativeImport createPostImport(Import importSource) {
    StringBuilder postSource = new StringBuilder();

    // $jsass-void: jsass_import_stack_pop() !global;
    postSource.append("$jsass-void: jsass_import_stack_pop() !global;\n");

    try {
      return new NativeImport(
          new Import(
              new URI(importSource.getUri() + "/JSASS_POST_IMPORT.scss"),
              importSource.getUri(),
              postSource.toString()
          )
      );
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
