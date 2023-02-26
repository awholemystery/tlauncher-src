package org.tlauncher.tlauncher.ui.alert;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.TextPopup;
import org.tlauncher.tlauncher.ui.swing.editor.EditorPane;
import org.tlauncher.util.StringUtil;
import org.tlauncher.util.U;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/alert/AlertPanel.class */
public class AlertPanel extends JPanel {
    private static final int MAX_CHARS = 80;
    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 300;
    private static final Dimension MAX_SIZE = new Dimension(500, 300);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlertPanel(String rawMessage, Object rawTextarea) {
        String message;
        setLayout(new BoxLayout(this, 1));
        if (rawMessage == null) {
            message = null;
        } else {
            message = StringUtil.wrap("<html>" + rawMessage + "</html>", 80);
        }
        EditorPane label = new EditorPane("text/html", message);
        label.setAlignmentX(0.0f);
        label.setFocusable(false);
        add(label);
        if (rawTextarea == null) {
            return;
        }
        String textarea = U.toLog(rawTextarea);
        JTextArea area = new JTextArea(textarea);
        area.addMouseListener(new TextPopup());
        area.setFont(getFont());
        area.setEditable(false);
        ScrollPane scroll = new ScrollPane((Component) area, true);
        scroll.setAlignmentX(0.0f);
        scroll.setVBPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        int textAreaHeight = StringUtil.countLines(textarea) * getFontMetrics(getFont()).getHeight();
        if (textAreaHeight > 300) {
            scroll.setPreferredSize(MAX_SIZE);
        }
        add(scroll);
    }
}
