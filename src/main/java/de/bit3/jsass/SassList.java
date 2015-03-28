package de.bit3.jsass;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class SassList extends ArrayList<Object> {
    private Separator separator = Separator.COMMA;

    public SassList() {
    }

    public SassList(Collection<?> c) {
        super(c);
    }

    public SassList(Separator separator) {
        this.separator = separator;
    }

    public SassList(Collection<?> c, Separator separator) {
        super(c);
        this.separator = separator;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return "(" + StringUtils.join(this, separator.character) + ")";
    }
}
