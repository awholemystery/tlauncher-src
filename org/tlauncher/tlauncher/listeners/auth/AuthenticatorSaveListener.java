package org.tlauncher.tlauncher.listeners.auth;

import by.gdev.util.excepiton.NotAllowWriteFileOperation;
import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.util.Objects;
import javax.swing.SwingUtilities;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
import org.tlauncher.tlauncher.exceptions.auth.BlockedUserException;
import org.tlauncher.tlauncher.exceptions.auth.NotCorrectTokenOrIdException;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
import org.tlauncher.tlauncher.minecraft.user.InvalidStatusCodeException;
import org.tlauncher.tlauncher.minecraft.user.gos.GameOwnershipValidationException;
import org.tlauncher.tlauncher.minecraft.user.xb.xsts.ChildAccountException;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/listeners/auth/AuthenticatorSaveListener.class */
public class AuthenticatorSaveListener implements AuthenticatorListener {
    private static final String fieldConfig = "mojang.account.protection.hide";

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassing(Authenticator auth) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassingError(Authenticator auth, Exception e) {
        showError(e);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassed(Authenticator auth) {
        ProfileManager pm = TLauncher.getInstance().getProfileManager();
        try {
            U.log("onAuthPassed");
            pm.save(auth.getAccount());
        } catch (IOException exception) {
            showError(exception);
        }
        Configuration c = TLauncher.getInstance().getConfiguration();
        if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(auth.getAccount().getType()) && !c.getBoolean(fieldConfig) && Alert.showWarningMessageWithCheckBox(Localizable.get("account.protection.message.title"), Localizable.get("account.protection.message"), 350, Localizable.get("account.message.show.again"))) {
            c.set(fieldConfig, (Object) true);
        }
        if (auth.getAccount().getType().equals(Account.AccountType.FREE)) {
            String u = auth.getAccount().getUsername();
            Configuration con = TLauncher.getInstance().getConfiguration();
            if (Objects.nonNull(u) && !con.getBoolean("not.proper.username.warning") && !MinecraftUtil.isUsernameValid(u) && Alert.showWarningMessageWithCheckBox(CoreConstants.EMPTY_STRING, "auth.error.username.not.valid", HttpStatus.SC_BAD_REQUEST)) {
                con.set("not.proper.username.warning", (Object) true);
            }
        }
    }

    private void showError(Throwable e) {
        String description = "unknown";
        if (e instanceof AuthenticatorException) {
            AuthenticatorException ae = (AuthenticatorException) e;
            if (ae.getLangpath() != null) {
                description = ae.getLangpath();
            }
            if (ae.getCause() instanceof GameOwnershipValidationException) {
                description = "no.ownership.found";
            }
            if (ae.getCause() instanceof ChildAccountException) {
                description = "child.limit";
            }
            if (Objects.nonNull(ae.getCause()) && Objects.nonNull(ae.getCause().getCause()) && (ae.getCause().getCause() instanceof InvalidStatusCodeException) && ((InvalidStatusCodeException) e.getCause().getCause()).getResponse().contains("http://support.xbox.com/xbox-live/country-not-authorized")) {
                U.log("set limit");
                description = "limit.country";
            }
        } else if (e instanceof NotAllowWriteFileOperation) {
            Alert.showErrorHtml("auth.error.title", Localizable.get("auth.error.can.not.write", e.getMessage()));
            return;
        }
        if (e instanceof BlockedUserException) {
            Alert.showErrorHtml("auth.error.title", Localizable.get("auth.error." + description, ((BlockedUserException) e).getMinutes()));
            return;
        }
        Alert.showErrorHtml("auth.error.title", "auth.error." + description);
        if (e instanceof NotCorrectTokenOrIdException) {
            SwingUtilities.invokeLater(() -> {
                MainPane m = TLauncher.getInstance().getFrame().mp;
                m.openAccountEditor();
            });
        }
    }
}
