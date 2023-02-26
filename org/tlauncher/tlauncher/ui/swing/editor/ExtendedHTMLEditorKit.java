package org.tlauncher.tlauncher.ui.swing.editor;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/ExtendedHTMLEditorKit.class */
public class ExtendedHTMLEditorKit extends HTMLEditorKit {
    private String popupHref;
    protected static final ExtendedHTMLFactory extendedFactory = new ExtendedHTMLFactory();
    public static final HyperlinkProcessor defaultHyperlinkProcessor = new HyperlinkProcessor() { // from class: org.tlauncher.tlauncher.ui.swing.editor.ExtendedHTMLEditorKit.1
        @Override // org.tlauncher.tlauncher.ui.swing.editor.HyperlinkProcessor
        public void process(String link) {
            if (link == null) {
                return;
            }
            try {
                URI uri = new URI(link);
                OS.openLink(uri);
            } catch (URISyntaxException e) {
                Alert.showLocError("browser.hyperlink.createScrollWrapper.error", e);
            }
        }
    };
    private static final Cursor HAND = Cursor.getPredefinedCursor(12);
    protected final ExtendedLinkController linkController = new ExtendedLinkController();
    private HyperlinkProcessor hlProc = defaultHyperlinkProcessor;
    private boolean processPopup = true;
    private final JPopupMenu popup = new JPopupMenu();

    public ExtendedHTMLEditorKit() {
        LocalizableMenuItem open = new LocalizableMenuItem("browser.hyperlink.popup.open");
        open.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.swing.editor.ExtendedHTMLEditorKit.2
            public void actionPerformed(ActionEvent e) {
                ExtendedHTMLEditorKit.this.hlProc.process(ExtendedHTMLEditorKit.this.popupHref);
            }
        });
        this.popup.add(open);
        LocalizableMenuItem copy = new LocalizableMenuItem("browser.hyperlink.popup.copy");
        copy.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.swing.editor.ExtendedHTMLEditorKit.3
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ExtendedHTMLEditorKit.this.popupHref), (ClipboardOwner) null);
            }
        });
        this.popup.add(copy);
        LocalizableMenuItem show = new LocalizableMenuItem("browser.hyperlink.popup.show");
        show.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.swing.editor.ExtendedHTMLEditorKit.4
            public void actionPerformed(ActionEvent e) {
                Alert.showLocMessage("browser.hyperlink.popup.show.alert", ExtendedHTMLEditorKit.this.popupHref);
            }
        });
        this.popup.add(show);
    }

    public ViewFactory getViewFactory() {
        return extendedFactory;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/ExtendedHTMLEditorKit$ExtendedHTMLFactory.class */
    public static class ExtendedHTMLFactory extends HTMLEditorKit.HTMLFactory {
        public View create(Element elem) {
            HTML.Tag kind = ExtendedHTMLEditorKit.getTag(elem);
            if (kind == HTML.Tag.IMG) {
                return new ExtendedImageView(elem);
            }
            return super.create(elem);
        }
    }

    public void install(JEditorPane pane) {
        MouseMotionListener[] mouseListeners;
        super.install(pane);
        for (MouseMotionListener mouseMotionListener : pane.getMouseListeners()) {
            if (mouseMotionListener instanceof HTMLEditorKit.LinkController) {
                pane.removeMouseListener(mouseMotionListener);
                pane.removeMouseMotionListener(mouseMotionListener);
                pane.addMouseListener(this.linkController);
                pane.addMouseMotionListener(this.linkController);
            }
        }
    }

    public final boolean getProcessPopup() {
        return this.processPopup;
    }

    public final void setProcessPopup(boolean process) {
        this.processPopup = process;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/ExtendedHTMLEditorKit$ExtendedLinkController.class */
    public class ExtendedLinkController extends MouseAdapter {
        public ExtendedLinkController() {
        }

        public void mouseClicked(MouseEvent e) {
            String href;
            JEditorPane editor = (JEditorPane) e.getSource();
            if ((editor.isEnabled() || editor.isDisplayable()) && (href = ExtendedHTMLEditorKit.getAnchorHref(e)) != null) {
                switch (e.getButton()) {
                    case 3:
                        if (ExtendedHTMLEditorKit.this.processPopup) {
                            ExtendedHTMLEditorKit.this.popupHref = href;
                            ExtendedHTMLEditorKit.this.popup.show(editor, e.getX(), e.getY());
                            return;
                        }
                        return;
                    default:
                        ExtendedHTMLEditorKit.this.hlProc.process(href);
                        return;
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            JEditorPane editor = (JEditorPane) e.getSource();
            if (!editor.isEnabled() && !editor.isDisplayable()) {
                return;
            }
            editor.setCursor(ExtendedHTMLEditorKit.getAnchorHref(e) == null ? Cursor.getDefaultCursor() : ExtendedHTMLEditorKit.HAND);
        }

        public void mouseExited(MouseEvent e) {
            JEditorPane editor = (JEditorPane) e.getSource();
            if (!editor.isEnabled() && !editor.isDisplayable()) {
                return;
            }
            editor.setCursor(Cursor.getDefaultCursor());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HTML.Tag getTag(Element elem) {
        AttributeSet attrs = elem.getAttributes();
        Object elementName = attrs.getAttribute("$ename");
        Object o = elementName != null ? null : attrs.getAttribute(StyleConstants.NameAttribute);
        if (o instanceof HTML.Tag) {
            return (HTML.Tag) o;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getAnchorHref(MouseEvent e) {
        Object anchorAttr;
        JEditorPane editor = (JEditorPane) e.getSource();
        if (!(editor.getDocument() instanceof HTMLDocument)) {
            return null;
        }
        HTMLDocument hdoc = editor.getDocument();
        Element elem = hdoc.getCharacterElement(editor.viewToModel(e.getPoint()));
        HTML.Tag tag = getTag(elem);
        if (tag == HTML.Tag.CONTENT && (anchorAttr = elem.getAttributes().getAttribute(HTML.Tag.A)) != null && (anchorAttr instanceof AttributeSet)) {
            AttributeSet anchor = (AttributeSet) anchorAttr;
            Object hrefObject = anchor.getAttribute(HTML.Attribute.HREF);
            if (hrefObject != null && (hrefObject instanceof String)) {
                return (String) hrefObject;
            }
            return null;
        }
        return null;
    }
}
