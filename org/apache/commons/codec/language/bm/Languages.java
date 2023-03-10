package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/bm/Languages.class */
public class Languages {
    public static final String ANY = "any";
    private static final Map<NameType, Languages> LANGUAGES = new EnumMap(NameType.class);
    private final Set<String> languages;
    public static final LanguageSet NO_LANGUAGES;
    public static final LanguageSet ANY_LANGUAGE;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/bm/Languages$LanguageSet.class */
    public static abstract class LanguageSet {
        public abstract boolean contains(String str);

        public abstract String getAny();

        public abstract boolean isEmpty();

        public abstract boolean isSingleton();

        public abstract LanguageSet restrictTo(LanguageSet languageSet);

        public static LanguageSet from(Set<String> langs) {
            return langs.isEmpty() ? Languages.NO_LANGUAGES : new SomeLanguages(langs);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/bm/Languages$SomeLanguages.class */
    public static final class SomeLanguages extends LanguageSet {
        private final Set<String> languages;

        private SomeLanguages(Set<String> languages) {
            this.languages = Collections.unmodifiableSet(languages);
        }

        @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
        public boolean contains(String language) {
            return this.languages.contains(language);
        }

        @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
        public String getAny() {
            return this.languages.iterator().next();
        }

        public Set<String> getLanguages() {
            return this.languages;
        }

        @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
        public boolean isEmpty() {
            return this.languages.isEmpty();
        }

        @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
        public boolean isSingleton() {
            return this.languages.size() == 1;
        }

        @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
        public LanguageSet restrictTo(LanguageSet other) {
            if (other == Languages.NO_LANGUAGES) {
                return other;
            }
            if (other == Languages.ANY_LANGUAGE) {
                return this;
            }
            SomeLanguages sl = (SomeLanguages) other;
            Set<String> ls = new HashSet<>(Math.min(this.languages.size(), sl.languages.size()));
            for (String lang : this.languages) {
                if (sl.languages.contains(lang)) {
                    ls.add(lang);
                }
            }
            return from(ls);
        }

        public String toString() {
            return "Languages(" + this.languages.toString() + ")";
        }
    }

    static {
        NameType[] arr$ = NameType.values();
        for (NameType s : arr$) {
            LANGUAGES.put(s, getInstance(langResourceName(s)));
        }
        NO_LANGUAGES = new LanguageSet() { // from class: org.apache.commons.codec.language.bm.Languages.1
            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public boolean contains(String language) {
                return false;
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public String getAny() {
                throw new NoSuchElementException("Can't fetch any language from the empty language set.");
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public boolean isEmpty() {
                return true;
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public boolean isSingleton() {
                return false;
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public LanguageSet restrictTo(LanguageSet other) {
                return this;
            }

            public String toString() {
                return "NO_LANGUAGES";
            }
        };
        ANY_LANGUAGE = new LanguageSet() { // from class: org.apache.commons.codec.language.bm.Languages.2
            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public boolean contains(String language) {
                return true;
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public String getAny() {
                throw new NoSuchElementException("Can't fetch any language from the any language set.");
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public boolean isEmpty() {
                return false;
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public boolean isSingleton() {
                return false;
            }

            @Override // org.apache.commons.codec.language.bm.Languages.LanguageSet
            public LanguageSet restrictTo(LanguageSet other) {
                return other;
            }

            public String toString() {
                return "ANY_LANGUAGE";
            }
        };
    }

    public static Languages getInstance(NameType nameType) {
        return LANGUAGES.get(nameType);
    }

    public static Languages getInstance(String languagesResourceName) {
        Set<String> ls = new HashSet<>();
        InputStream langIS = Languages.class.getClassLoader().getResourceAsStream(languagesResourceName);
        if (langIS == null) {
            throw new IllegalArgumentException("Unable to resolve required resource: " + languagesResourceName);
        }
        Scanner lsScanner = new Scanner(langIS, "UTF-8");
        boolean inExtendedComment = false;
        while (lsScanner.hasNextLine()) {
            try {
                String line = lsScanner.nextLine().trim();
                if (inExtendedComment) {
                    if (line.endsWith("*/")) {
                        inExtendedComment = false;
                    }
                } else if (line.startsWith("/*")) {
                    inExtendedComment = true;
                } else if (line.length() > 0) {
                    ls.add(line);
                }
            } finally {
                lsScanner.close();
            }
        }
        return new Languages(Collections.unmodifiableSet(ls));
    }

    private static String langResourceName(NameType nameType) {
        return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", nameType.getName());
    }

    private Languages(Set<String> languages) {
        this.languages = languages;
    }

    public Set<String> getLanguages() {
        return this.languages;
    }
}
