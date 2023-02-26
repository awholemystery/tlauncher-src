package org.apache.commons.io.input;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/CharacterSetFilterReader.class */
public class CharacterSetFilterReader extends AbstractCharacterFilterReader {
    private static IntPredicate toIntPredicate(Set<Integer> skip) {
        if (skip == null) {
            return SKIP_NONE;
        }
        Set<Integer> unmodifiableSet = Collections.unmodifiableSet(skip);
        return c -> {
            return unmodifiableSet.contains(Integer.valueOf(c));
        };
    }

    public CharacterSetFilterReader(Reader reader, Integer... skip) {
        this(reader, new HashSet(Arrays.asList(skip)));
    }

    public CharacterSetFilterReader(Reader reader, Set<Integer> skip) {
        super(reader, toIntPredicate(skip));
    }
}
