package org.tlauncher.tlauncher.ui.review;

import ch.qos.logback.core.CoreConstants;
import java.util.regex.Pattern;
import org.tlauncher.tlauncher.entity.Review;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/review/ValidateReview.class */
public class ValidateReview {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validate(Review review) {
        if (review.getDescription().isEmpty()) {
            Alert.showWarning(CoreConstants.EMPTY_STRING, Localizable.get().get("review.message.fill") + " " + Localizable.get().get("review.message.description"));
            return false;
        } else if (review.getDescription().equals(Localizable.get("review.description"))) {
            Alert.showWarning(CoreConstants.EMPTY_STRING, Localizable.get().get("review.message.fill") + " " + Localizable.get().get("review.message.description"));
            return false;
        } else {
            String mail = review.getMailReview();
            if (mail.isEmpty()) {
                Alert.showLocWarning("review.message.email.invalid");
                return false;
            } else if (!mail.startsWith("https://vk.com") && !mail.startsWith("http://vk.com") && !mail.startsWith("vk.com") && !Pattern.compile(EMAIL_PATTERN).matcher(review.getMailReview()).matches()) {
                Alert.showWarning(CoreConstants.EMPTY_STRING, Localizable.get().get("review.message.email.invalid"));
                return false;
            } else {
                return true;
            }
        }
    }
}
