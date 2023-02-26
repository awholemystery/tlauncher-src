package org.tlauncher.tlauncher.ui.editor;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorPanel.class */
public class EditorPanel extends AbstractEditorPanel {
    private static final long serialVersionUID = 3428243378644563729L;
    protected final ExtendedPanel container;
    protected final ScrollPane scroll;
    private final List<ExtendedPanel> panels;
    private final List<GridBagConstraints> constraints;
    protected final List<EditorHandler> handlers;
    private byte paneNum;
    private byte rowNum;

    public EditorPanel(Insets insets) {
        super(insets);
        this.container = new ExtendedPanel();
        this.container.setLayout(new BoxLayout(this.container, 3));
        this.panels = new ArrayList();
        this.constraints = new ArrayList();
        this.handlers = new ArrayList();
        this.scroll = new ScrollPane(this.container);
        add((Component) this.messagePanel, (Component) this.scroll);
    }

    public EditorPanel() {
        this(smallSquareNoTopInsets);
    }

    protected void add(EditorPair pair) {
        ExtendedPanel panel;
        GridBagConstraints c;
        Component label = pair.getLabel();
        Component panel2 = pair.getPanel();
        if (this.paneNum == this.panels.size()) {
            panel = new ExtendedPanel((LayoutManager) new GridBagLayout());
            panel.getInsets().set(0, 0, 0, 0);
            c = new GridBagConstraints();
            c.fill = 2;
            this.container.add((Component) panel, (Component) del(0));
            this.panels.add(panel);
            this.constraints.add(c);
        } else {
            panel = (ExtendedPanel) this.panels.get(this.paneNum);
            c = this.constraints.get(this.paneNum);
        }
        c.anchor = 17;
        c.gridy = this.rowNum;
        c.gridx = 0;
        c.weightx = 0.1d;
        panel.add(label, c);
        c.anchor = 13;
        byte b = this.rowNum;
        this.rowNum = (byte) (b + 1);
        c.gridy = b;
        c.gridx = 1;
        c.weightx = 1.0d;
        panel.add(panel2, c);
        this.handlers.addAll(pair.getHandlers());
    }

    protected void nextPane() {
        this.rowNum = (byte) 0;
        this.paneNum = (byte) (this.paneNum + 1);
    }
}
