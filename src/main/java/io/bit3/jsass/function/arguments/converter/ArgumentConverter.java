package io.bit3.jsass.function.arguments.converter;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.function.FunctionArgumentSignature;
import io.bit3.jsass.function.FunctionArgumentSignatureFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public interface ArgumentConverter {
  Object convert(
      List<?> remainingArguments,
      ImportStack importStack,
      Context context
  );

  List<FunctionArgumentSignature> argumentSignatures(
      Object object,
      Method method,
      Parameter parameter,
      FunctionArgumentSignatureFactory factory
  );
}
