package org.tlauncher.tlauncher.ui.swing.notification;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/notification/UpdaterJavaNotification.class */
public class UpdaterJavaNotification extends JPanel {
    private final Dimension DIMENSION = new Dimension(500, 100);

    public UpdaterJavaNotification(LangConfiguration configuration) {
        setLayout(new BoxLayout(this, 1));
        JPanel messagePanel = new JPanel();
        add(messagePanel);
        messagePanel.setLayout(new GridLayout(0, 1, 0, 0));
        String userMessage = OS.is(OS.WINDOWS) ? configuration.get("updater.java.notification.message") : configuration.get("updater.java.notification.message.special");
        FlexibleEditorPanel message = new FlexibleEditorPanel("text/html", userMessage, this.DIMENSION.width);
        messagePanel.add(message);
    }

    public static int showUpdaterJavaNotfication(LangConfiguration configuration) {
        List<Object> buttons = new ArrayList<>(3);
        if (OS.is(OS.WINDOWS)) {
            buttons.add(configuration.get("updater.java.install.button"));
        }
        buttons.add(configuration.get("updater.java.myself.button"));
        buttons.add(configuration.get("updater.java.reminder.button"));
        JFrame f = new JFrame();
        f.setAlwaysOnTop(true);
        int res = JOptionPane.showOptionDialog(f, new UpdaterJavaNotification(configuration), configuration.get("updater.java.notification.title"), -1, 2, (Icon) null, buttons.toArray(), (Object) null);
        if (!OS.is(OS.WINDOWS) && res > -1) {
            res++;
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        LangConfiguration langConfiguration = new LangConfiguration(new Locale[]{new Locale("ru", "Ru")}, new Locale("ru", "Ru"), "/lang/tlauncher/");
        showUpdaterJavaNotfication(langConfiguration);
    }
}
