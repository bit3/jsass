package io.bit3.jsass.function.arguments;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.importer.Import;

import java.util.List;

public interface ArgumentConverter {
  public Object convert(List<?> remainingArguments, Import lastImport,
                        Context context);
}
