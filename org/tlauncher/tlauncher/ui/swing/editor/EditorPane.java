package org.tlauncher.tlauncher.ui.swing.editor;

import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.StyleSheet;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/EditorPane.class */
public class EditorPane extends JEditorPane {
    private static final long serialVersionUID = -2857352867725574106L;

    public EditorPane(Font font) {
        if (font != null) {
            setFont(font);
        } else {
            font = getFont();
        }
        StyleSheet css = new StyleSheet();
        css.importStyleSheet(getClass().getResource("styles.css"));
        css.addRule("body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; } a { text-decoration: underline; }");
        ExtendedHTMLEditorKit html = new ExtendedHTMLEditorKit();
        html.setStyleSheet(css);
        getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        setMargin(new Insets(0, 0, 0, 0));
        setEditorKit(html);
        setEditable(false);
        setOpaque(false);
        addHyperlinkListener(new HyperlinkListener() { // from class: org.tlauncher.tlauncher.ui.swing.editor.EditorPane.1
            public void hyperlinkUpdate(HyperlinkEvent e) {
                URL url;
                if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || (url = e.getURL()) == null) {
                    return;
                }
                OS.openLink(url);
            }
        });
    }

    public EditorPane() {
        this(new LocalizableLabel().getFont());
    }

    public EditorPane(URL initialPage) throws IOException {
        this();
        setPage(initialPage);
    }

    public EditorPane(String url) throws IOException {
        this();
        setPage(url);
    }

    public EditorPane(String type, String text) {
        this();
        setContentType(type);
        setText(text);
    }
}
