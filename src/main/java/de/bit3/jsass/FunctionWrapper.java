package de.bit3.jsass;

import com.sun.jna.Pointer;
import sass.SassLibrary;

class FunctionWrapper implements SassLibrary.Sass_C_Function {
    /**
     * SASS library adapter.
     */
    private final SassLibrary SASS;

    private final FunctionDeclaration declaration;

    public FunctionWrapper(SassLibrary SASS, FunctionDeclaration declaration) {
        this.SASS = SASS;
        this.declaration = declaration;
    }

    @Override
    public SassLibrary.Sass_Value apply(SassLibrary.Sass_Value value, Pointer cookie) {
        try {
            Object decodedValue = ValueUtils.decodeValue(SASS, value);
            SassList sassList;

            if (decodedValue instanceof SassList) {
                sassList = (SassList)decodedValue;
            } else {
                sassList = new SassList();
                sassList.add(decodedValue);
            }

            Object result = declaration.invoke(sassList);

            return ValueUtils.encodeValue(SASS, result);
        } catch (CompilationException e) {
            throw new RuntimeException(e);
        }
    }
}

