package org.apache.http.conn.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.util.Args;
import org.slf4j.Marker;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/ssl/AbstractVerifier.class */
public abstract class AbstractVerifier implements X509HostnameVerifier {
    private final Log log = LogFactory.getLog(getClass());
    static final String[] BAD_COUNTRY_2LDS = {"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};

    static {
        Arrays.sort(BAD_COUNTRY_2LDS);
    }

    @Override // org.apache.http.conn.ssl.X509HostnameVerifier
    public final void verify(String host, SSLSocket ssl) throws IOException {
        Args.notNull(host, "Host");
        SSLSession session = ssl.getSession();
        if (session == null) {
            InputStream in = ssl.getInputStream();
            in.available();
            session = ssl.getSession();
            if (session == null) {
                ssl.startHandshake();
                session = ssl.getSession();
            }
        }
        Certificate[] certs = session.getPeerCertificates();
        X509Certificate x509 = (X509Certificate) certs[0];
        verify(host, x509);
    }

    @Override // javax.net.ssl.HostnameVerifier
    public final boolean verify(String host, SSLSession session) {
        try {
            Certificate[] certs = session.getPeerCertificates();
            X509Certificate x509 = (X509Certificate) certs[0];
            verify(host, x509);
            return true;
        } catch (SSLException ex) {
            if (this.log.isDebugEnabled()) {
                this.log.debug(ex.getMessage(), ex);
                return false;
            }
            return false;
        }
    }

    @Override // org.apache.http.conn.ssl.X509HostnameVerifier
    public final void verify(String host, X509Certificate cert) throws SSLException {
        boolean ipv4 = InetAddressUtils.isIPv4Address(host);
        boolean ipv6 = InetAddressUtils.isIPv6Address(host);
        int subjectType = (ipv4 || ipv6) ? 7 : 2;
        List<String> subjectAlts = DefaultHostnameVerifier.extractSubjectAlts(cert, subjectType);
        X500Principal subjectPrincipal = cert.getSubjectX500Principal();
        String cn = DefaultHostnameVerifier.extractCN(subjectPrincipal.getName("RFC2253"));
        verify(host, cn != null ? new String[]{cn} : null, (subjectAlts == null || subjectAlts.isEmpty()) ? null : (String[]) subjectAlts.toArray(new String[subjectAlts.size()]));
    }

    public final void verify(String host, String[] cns, String[] subjectAlts, boolean strictWithSubDomains) throws SSLException {
        String cn = (cns == null || cns.length <= 0) ? null : cns[0];
        List<String> subjectAltList = (subjectAlts == null || subjectAlts.length <= 0) ? null : Arrays.asList(subjectAlts);
        String normalizedHost = InetAddressUtils.isIPv6Address(host) ? DefaultHostnameVerifier.normaliseAddress(host.toLowerCase(Locale.ROOT)) : host;
        if (subjectAltList != null) {
            for (String subjectAlt : subjectAltList) {
                String normalizedAltSubject = InetAddressUtils.isIPv6Address(subjectAlt) ? DefaultHostnameVerifier.normaliseAddress(subjectAlt) : subjectAlt;
                if (matchIdentity(normalizedHost, normalizedAltSubject, strictWithSubDomains)) {
                    return;
                }
            }
            throw new SSLException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAltList);
        } else if (cn != null) {
            String normalizedCN = InetAddressUtils.isIPv6Address(cn) ? DefaultHostnameVerifier.normaliseAddress(cn) : cn;
            if (matchIdentity(normalizedHost, normalizedCN, strictWithSubDomains)) {
                return;
            }
            throw new SSLException("Certificate for <" + host + "> doesn't match common name of the certificate subject: " + cn);
        } else {
            throw new SSLException("Certificate subject for <" + host + "> doesn't contain a common name and does not have alternative names");
        }
    }

    private static boolean matchIdentity(String host, String identity, boolean strict) {
        boolean match;
        if (host == null) {
            return false;
        }
        String normalizedHost = host.toLowerCase(Locale.ROOT);
        String normalizedIdentity = identity.toLowerCase(Locale.ROOT);
        String[] parts = normalizedIdentity.split("\\.");
        boolean doWildcard = parts.length >= 3 && parts[0].endsWith(Marker.ANY_MARKER) && (!strict || validCountryWildcard(parts));
        if (doWildcard) {
            String firstpart = parts[0];
            if (firstpart.length() > 1) {
                String prefix = firstpart.substring(0, firstpart.length() - 1);
                String suffix = normalizedIdentity.substring(firstpart.length());
                String hostSuffix = normalizedHost.substring(prefix.length());
                match = normalizedHost.startsWith(prefix) && hostSuffix.endsWith(suffix);
            } else {
                match = normalizedHost.endsWith(normalizedIdentity.substring(1));
            }
            return match && (!strict || countDots(normalizedHost) == countDots(normalizedIdentity));
        }
        return normalizedHost.equals(normalizedIdentity);
    }

    private static boolean validCountryWildcard(String[] parts) {
        return (parts.length == 3 && parts[2].length() == 2 && Arrays.binarySearch(BAD_COUNTRY_2LDS, parts[1]) >= 0) ? false : true;
    }

    public static boolean acceptableCountryWildcard(String cn) {
        return validCountryWildcard(cn.split("\\."));
    }

    public static String[] getCNs(X509Certificate cert) {
        String subjectPrincipal = cert.getSubjectX500Principal().toString();
        try {
            String cn = DefaultHostnameVerifier.extractCN(subjectPrincipal);
            if (cn != null) {
                return new String[]{cn};
            }
            return null;
        } catch (SSLException e) {
            return null;
        }
    }

    public static String[] getDNSSubjectAlts(X509Certificate cert) {
        List<String> subjectAlts = DefaultHostnameVerifier.extractSubjectAlts(cert, 2);
        if (subjectAlts == null || subjectAlts.isEmpty()) {
            return null;
        }
        return (String[]) subjectAlts.toArray(new String[subjectAlts.size()]);
    }

    public static int countDots(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                count++;
            }
        }
        return count;
    }
}
