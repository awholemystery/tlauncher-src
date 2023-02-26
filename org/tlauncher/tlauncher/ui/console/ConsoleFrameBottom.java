package org.tlauncher.tlauncher.ui.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.swing.ImageButton;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/ConsoleFrameBottom.class */
public class ConsoleFrameBottom extends BorderPanel implements LocalizableComponent {
    private static final long serialVersionUID = 7438348937589503917L;
    private final ConsoleFrame frame;
    public final LocalizableButton closeCancelButton;
    public final ImageButton pastebin;
    public final ImageButton kill;
    public final ImageButton scrollingDown;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConsoleFrameBottom(ConsoleFrame fr) {
        this.frame = fr;
        setOpaque(true);
        setBackground(Color.darkGray);
        this.closeCancelButton = new LocalizableButton("console.close.cancel");
        this.closeCancelButton.setVisible(false);
        this.closeCancelButton.addActionListener(e -> {
            if (!this.closeCancelButton.isVisible()) {
                return;
            }
            this.frame.hiding = false;
            this.closeCancelButton.setVisible(false);
        });
        setCenter(this.closeCancelButton);
        this.pastebin = newButton("mail-attachment.png", e2 -> {
            this.frame.console.sendPaste();
        });
        this.scrollingDown = newButton("down.png", e3 -> {
            this.frame.scrollDown();
        });
        this.kill = newButton("process-stop.png", new ActionListener() { // from class: org.tlauncher.tlauncher.ui.console.ConsoleFrameBottom.1
            public void actionPerformed(ActionEvent e4) {
                ConsoleFrameBottom.this.frame.console.killProcess();
                ConsoleFrameBottom.this.kill.setEnabled(false);
            }
        });
        this.kill.setEnabled(false);
        updateLocale();
        ExtendedPanel buttonPanel = new ExtendedPanel();
        buttonPanel.add(this.scrollingDown, this.pastebin, this.kill);
        setEast(buttonPanel);
    }

    private ImageButton newButton(String path, ActionListener action) {
        ImageButton button = new ImageButton(path);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(32, 32));
        return button;
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        this.pastebin.setToolTipText(Localizable.get("console.pastebin"));
        this.kill.setToolTipText(Localizable.get("console.kill"));
    }
}
