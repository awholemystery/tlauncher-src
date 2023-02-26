package org.tlauncher.tlauncher.ui.browser;

import org.tlauncher.tlauncher.ui.login.LoginFormHelper;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/BrowserBridge.class */
public class BrowserBridge {
    private final BrowserHolder holder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BrowserBridge(BrowserPanel browser) {
        this.holder = browser.holder;
    }

    public void toggleHelper() {
        if (this.holder.pane.defaultScene.loginFormHelper.getState() == LoginFormHelper.LoginFormHelperState.NONE) {
            this.holder.pane.defaultScene.loginFormHelper.setState(LoginFormHelper.LoginFormHelperState.SHOWN);
        } else {
            this.holder.pane.defaultScene.loginFormHelper.setState(LoginFormHelper.LoginFormHelperState.NONE);
        }
    }
}
