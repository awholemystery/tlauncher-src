package org.tlauncher.tlauncher.ui.login.buttons;

import org.tlauncher.tlauncher.ui.block.Unblockable;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.async.AsyncThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/FolderButton.class */
public class FolderButton extends MainImageButton implements Unblockable {
    private static final long serialVersionUID = 2488886626114461187L;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FolderButton(LoginForm loginform) {
        super(DARK_GREEN_COLOR, "folder-mouse-under.png", "folder.png");
        addActionListener(e -> {
            AsyncThread.execute(() -> {
                OS.openFolder(MinecraftUtil.getWorkingDirectory());
            });
        });
    }
}
