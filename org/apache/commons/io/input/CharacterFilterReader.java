package org.apache.commons.io.input;

import java.io.Reader;
import java.util.function.IntPredicate;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/CharacterFilterReader.class */
public class CharacterFilterReader extends AbstractCharacterFilterReader {
    public CharacterFilterReader(Reader reader, int skip) {
        super(reader, c -> {
            return c == skip;
        });
    }

    public CharacterFilterReader(Reader reader, IntPredicate skip) {
        super(reader, skip);
    }
}
