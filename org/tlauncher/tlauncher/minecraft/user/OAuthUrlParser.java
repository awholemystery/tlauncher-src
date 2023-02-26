package org.tlauncher.tlauncher.minecraft.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/OAuthUrlParser.class */
public class OAuthUrlParser {
    private static String findByKey(List<NameValuePair> pairs, String key) {
        Optional<NameValuePair> pair = pairs.stream().filter(p -> {
            return p.getName().equals(key);
        }).findAny();
        return (String) pair.map((v0) -> {
            return v0.getValue();
        }).orElse(null);
    }

    public String parseAndValidate(String url) throws MicrosoftOAuthCodeRequestException, ParseException {
        try {
            List<NameValuePair> pairs = URLEncodedUtils.parse(new URI(url), String.valueOf(StandardCharsets.UTF_8));
            String error = findByKey(pairs, "error");
            if (error != null) {
                if (error.equals("access_denied")) {
                    throw new CodeRequestCancelledException("redirect page received \"access_denied\"");
                }
                throw new CodeRequestErrorException(error, findByKey(pairs, "error_description"));
            }
            Optional<NameValuePair> code = pairs.stream().filter(p -> {
                return p.getName().equals("code");
            }).findAny();
            if (code.isPresent()) {
                return code.get().getValue();
            }
            throw new ParseException("no code in query");
        } catch (URISyntaxException e) {
            throw new ParseException(e);
        }
    }
}
