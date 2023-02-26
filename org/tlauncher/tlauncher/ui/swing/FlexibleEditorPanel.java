package org.tlauncher.tlauncher.ui.swing;

import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.StyleSheet;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.editor.ExtendedHTMLEditorKit;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/FlexibleEditorPanel.class */
public class FlexibleEditorPanel extends JEditorPane implements LocalizableComponent {
    public FlexibleEditorPanel(Font font, int width) {
        if (font != null) {
            setFont(font);
        } else {
            font = getFont();
        }
        StyleSheet css = new StyleSheet();
        css.importStyleSheet(getClass().getResource("styles.css"));
        css.addRule("body { font-family: " + font.getFamily() + ";width:" + width + "; font-size: " + font.getSize() + "pt; } a { text-decoration: underline; }");
        ExtendedHTMLEditorKit html = new ExtendedHTMLEditorKit();
        html.setStyleSheet(css);
        getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        setMargin(new Insets(0, 0, 0, 0));
        setEditorKit(html);
        setEditable(false);
        setOpaque(false);
        addHyperlinkListener(new HyperlinkListener() { // from class: org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel.1
            public void hyperlinkUpdate(HyperlinkEvent e) {
                URL url;
                if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || (url = e.getURL()) == null) {
                    return;
                }
                OS.openLink(url);
            }
        });
    }

    public FlexibleEditorPanel(int width) {
        this(new LocalizableLabel().getFont(), width);
    }

    public FlexibleEditorPanel(String type, String text, int width) {
        this(width);
        setContentType(type);
        setText(Localizable.get(text));
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setText(Localizable.get("auth.tip.tlauncher"));
    }
}
