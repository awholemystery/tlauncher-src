package org.apache.http.conn.util;

import java.net.IDN;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/util/PublicSuffixMatcher.class */
public final class PublicSuffixMatcher {
    private final Map<String, DomainType> rules;
    private final Map<String, DomainType> exceptions;

    public PublicSuffixMatcher(Collection<String> rules, Collection<String> exceptions) {
        this(DomainType.UNKNOWN, rules, exceptions);
    }

    public PublicSuffixMatcher(DomainType domainType, Collection<String> rules, Collection<String> exceptions) {
        Args.notNull(domainType, "Domain type");
        Args.notNull(rules, "Domain suffix rules");
        this.rules = new ConcurrentHashMap(rules.size());
        for (String rule : rules) {
            this.rules.put(rule, domainType);
        }
        this.exceptions = new ConcurrentHashMap();
        if (exceptions != null) {
            for (String exception : exceptions) {
                this.exceptions.put(exception, domainType);
            }
        }
    }

    public PublicSuffixMatcher(Collection<PublicSuffixList> lists) {
        Args.notNull(lists, "Domain suffix lists");
        this.rules = new ConcurrentHashMap();
        this.exceptions = new ConcurrentHashMap();
        for (PublicSuffixList list : lists) {
            DomainType domainType = list.getType();
            List<String> rules = list.getRules();
            for (String rule : rules) {
                this.rules.put(rule, domainType);
            }
            List<String> exceptions = list.getExceptions();
            if (exceptions != null) {
                for (String exception : exceptions) {
                    this.exceptions.put(exception, domainType);
                }
            }
        }
    }

    private static boolean hasEntry(Map<String, DomainType> map, String rule, DomainType expectedType) {
        DomainType domainType;
        if (map == null || (domainType = map.get(rule)) == null) {
            return false;
        }
        return expectedType == null || domainType.equals(expectedType);
    }

    private boolean hasRule(String rule, DomainType expectedType) {
        return hasEntry(this.rules, rule, expectedType);
    }

    private boolean hasException(String exception, DomainType expectedType) {
        return hasEntry(this.exceptions, exception, expectedType);
    }

    public String getDomainRoot(String domain) {
        return getDomainRoot(domain, null);
    }

    public String getDomainRoot(String domain, DomainType expectedType) {
        if (domain == null || domain.startsWith(".")) {
            return null;
        }
        String domainName = null;
        String lowerCase = domain.toLowerCase(Locale.ROOT);
        while (true) {
            String segment = lowerCase;
            if (segment != null) {
                if (hasException(IDN.toUnicode(segment), expectedType)) {
                    return segment;
                }
                if (!hasRule(IDN.toUnicode(segment), expectedType)) {
                    int nextdot = segment.indexOf(46);
                    String nextSegment = nextdot != -1 ? segment.substring(nextdot + 1) : null;
                    if (nextSegment != null && hasRule("*." + IDN.toUnicode(nextSegment), expectedType)) {
                        break;
                    }
                    if (nextdot != -1) {
                        domainName = segment;
                    }
                    lowerCase = nextSegment;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return domainName;
    }

    public boolean matches(String domain) {
        return matches(domain, null);
    }

    public boolean matches(String domain, DomainType expectedType) {
        if (domain == null) {
            return false;
        }
        String domainRoot = getDomainRoot(domain.startsWith(".") ? domain.substring(1) : domain, expectedType);
        return domainRoot == null;
    }
}
