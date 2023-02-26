package org.tlauncher.tlauncher.ui.swing;

import ch.qos.logback.core.CoreConstants;
import java.awt.Insets;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.launcher.resource.TlauncherResource;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/HtmlTextPane.class */
public class HtmlTextPane extends JEditorPane {
    private static final HtmlTextPane HTML_TEXT_PANE = new HtmlTextPane("text/html", CoreConstants.EMPTY_STRING);
    private static final HtmlTextPane HTML_TEXT_PANE_WIDTH = new HtmlTextPane("text/html", CoreConstants.EMPTY_STRING);

    public HtmlTextPane(String type, String text) {
        super(type, text);
        getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        setMargin(new Insets(0, 0, 0, 0));
        setEditable(false);
        setOpaque(false);
        HTMLEditorKit kit = new HTMLEditorKit();
        setEditorKit(kit);
        addHyperlinkListener(e -> {
            URL url;
            if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || (url = e.getURL()) == null) {
                return;
            }
            OS.openLink(url);
        });
    }

    public static HtmlTextPane get(String text) {
        HTML_TEXT_PANE.setText(text);
        return HTML_TEXT_PANE;
    }

    public static HtmlTextPane get(String text, int width) {
        HTMLEditorKit kit = HTML_TEXT_PANE_WIDTH.getEditorKit();
        kit.getStyleSheet().addRule(String.format("body {width:%spx;}", Integer.valueOf(width)));
        kit.getStyleSheet().addRule("a { text-decoration: underline; color: #147de0;}");
        HTML_TEXT_PANE_WIDTH.setText(text);
        return HTML_TEXT_PANE_WIDTH;
    }

    public static HtmlTextPane getWithWidth(String text, int width) {
        HtmlTextPane h = new HtmlTextPane("text/html", CoreConstants.EMPTY_STRING);
        HTMLEditorKit kit = h.getEditorKit();
        kit.getStyleSheet().addRule(String.format("body {width:%spx;}", Integer.valueOf(width)));
        kit.getStyleSheet().addRule("a { text-decoration: underline; color: #147de0;}");
        h.setText(text);
        return h;
    }

    public static HtmlTextPane createNew(String text, int width) {
        HtmlTextPane pane = new HtmlTextPane("text/html", CoreConstants.EMPTY_STRING);
        pane.setText(text);
        HTMLEditorKit kit = pane.getEditorKit();
        StyleSheet ss = new StyleSheet();
        ss.importStyleSheet(TlauncherResource.getResource("updater.css"));
        kit.getStyleSheet().addStyleSheet(ss);
        return pane;
    }

    public static JScrollPane createNewAndWrap(String text, int width) {
        return wrap(createNew(text, width));
    }

    private static JScrollPane wrap(HtmlTextPane pane) {
        JScrollPane jScrollPane = new JScrollPane(pane, 21, 31);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setOpaque(false);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        return jScrollPane;
    }
}
