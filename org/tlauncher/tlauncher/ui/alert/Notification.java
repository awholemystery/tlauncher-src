package org.tlauncher.tlauncher.ui.alert;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/alert/Notification.class */
public class Notification extends JPanel {
    private static final int WIDTH = 500;
    public static final String MEMORY_NOTIFICATION = "memory.notification.off";

    public Notification(String message, String saveKey) {
        BoxLayout box = new BoxLayout(this, 1);
        setLayout(box);
        FlexibleEditorPanel label = new FlexibleEditorPanel("text/html", Localizable.get(message), 500);
        JCheckBox notificationState = new JCheckBox(Localizable.get("skin.notification.state"));
        notificationState.addActionListener(e -> {
            if (notificationState.isSelected()) {
                TLauncher.getInstance().getConfiguration().set(saveKey, "true", true);
            } else {
                TLauncher.getInstance().getConfiguration().set(saveKey, "false", true);
            }
        });
        add(label);
        JPanel panel = new JPanel();
        panel.add(notificationState);
        add(panel);
    }
}
