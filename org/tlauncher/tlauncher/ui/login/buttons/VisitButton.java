package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.Unblockable;
import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/VisitButton.class */
public class VisitButton extends LocalizableButton implements Unblockable {
    private static final long serialVersionUID = 1301825302386488945L;
    private static URI ru = U.makeURI("http://masken.ru/rum.html");
    private static URI en = U.makeURI("http://masken.ru/enmine.html");
    private URI link;

    VisitButton() {
        super("loginform.button.visit");
        addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.login.buttons.VisitButton.1
            public void actionPerformed(ActionEvent e) {
                OS.openLink(VisitButton.this.link);
            }
        });
        updateLocale();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableButton, org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        super.updateLocale();
        this.link = TLauncher.getInstance().getConfiguration().isUSSRLocale() ? ru : en;
    }
}
