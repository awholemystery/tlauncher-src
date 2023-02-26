package org.tlauncher.tlauncher.ui.editor;

import ch.qos.logback.core.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.Del;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.extended.TabbedPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/TabbedEditorPanel.class */
public class TabbedEditorPanel extends AbstractEditorPanel {
    protected final BorderPanel container;
    protected final TabbedPane tabPane;
    protected final List<EditorPanelTab> tabs;

    public TabbedEditorPanel(CenterPanelTheme theme, Insets insets) {
        super(theme, insets);
        this.tabs = new ArrayList();
        this.tabPane = new TabbedPane();
        if (this.tabPane.getExtendedUI() != null) {
            this.tabPane.getExtendedUI().setTheme(getTheme());
        }
        this.container = new BorderPanel();
        this.container.setNorth(this.messagePanel);
        this.container.setCenter(this.tabPane);
        setLayout(new BorderLayout());
        super.add((Component) this.container, "Center");
    }

    public TabbedEditorPanel(CenterPanelTheme theme) {
        this(theme, null);
    }

    public TabbedEditorPanel(Insets insets) {
        this(null, insets);
    }

    public TabbedEditorPanel() {
        this(smallSquareNoTopInsets);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void add(EditorPanelTab tab) {
        if (tab == null) {
            throw new NullPointerException("tab is null");
        }
        this.tabPane.addTab(tab.getTabName(), tab.getTabIcon(), tab.getScroll(), tab.getTabTip());
        this.tabs.add(tab);
    }

    protected void remove(EditorPanelTab tab) {
        if (tab == null) {
            throw new NullPointerException("tab is null");
        }
        int index = this.tabs.indexOf(tab);
        if (index != -1) {
            this.tabPane.removeTabAt(index);
            this.tabs.remove(index);
        }
    }

    protected int getTabOf(EditorPair pair) {
        return this.tabPane.indexOfComponent(pair.getPanel());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.center.CenterPanel
    public Del del(int aligment) {
        Color border;
        try {
            border = this.tabPane.getExtendedUI().getTheme().getBorder();
        } catch (Exception e) {
            border = getTheme().getBorder();
        }
        return new Del(1, aligment, border);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/TabbedEditorPanel$EditorScrollPane.class */
    public class EditorScrollPane extends ScrollPane {
        private final EditorPanelTab tab;

        EditorScrollPane(EditorPanelTab tab) {
            super(tab);
            this.tab = tab;
        }

        public EditorPanelTab getTab() {
            return this.tab;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/TabbedEditorPanel$EditorPanelTab.class */
    public class EditorPanelTab extends ExtendedPanel implements LocalizableComponent {
        private final String name;
        private final String tip;
        private final Icon icon;
        private final List<ExtendedPanel> panels;
        private final EditorScrollPane scroll;
        private boolean savingEnabled;
        private final GridBagConstraints c;

        public EditorPanelTab(String name, String tip, Icon icon) {
            this.savingEnabled = true;
            this.c = new GridBagConstraints();
            this.c.fill = 2;
            this.c.gridy = 0;
            if (name == null) {
                throw new NullPointerException();
            }
            if (name.isEmpty()) {
                throw new IllegalArgumentException("name is empty");
            }
            this.name = name;
            this.tip = tip;
            this.icon = icon;
            this.panels = new ArrayList();
            setLayout(new GridBagLayout());
            this.scroll = new EditorScrollPane(this);
        }

        public EditorPanelTab(TabbedEditorPanel this$0, String name) {
            this(name, null, null);
        }

        public String getTabName() {
            return Localizable.get(this.name);
        }

        public Icon getTabIcon() {
            return this.icon;
        }

        public String getTabTip() {
            return Localizable.get(this.tip);
        }

        public EditorScrollPane getScroll() {
            return this.scroll;
        }

        public boolean getSavingEnabled() {
            return this.savingEnabled;
        }

        public void setSavingEnabled(boolean b) {
            this.savingEnabled = b;
        }

        public void add(EditorPair pair) {
            LocalizableLabel label = pair.getLabel();
            ExtendedPanel field = pair.getPanel();
            this.c.gridx = 0;
            this.c.weightx = 0.0d;
            add((Component) label, (Object) this.c);
            this.c.gridx++;
            this.c.gridx++;
            add(Box.createHorizontalStrut(50), this.c);
            this.c.gridx++;
            this.c.weightx = 1.0d;
            add((Component) field, (Object) this.c);
            this.c.gridy++;
            add(Box.createVerticalStrut(20), this.c);
            this.c.gridy++;
            this.panels.add(field);
            TabbedEditorPanel.this.handlers.addAll(pair.getHandlers());
        }

        public void nextPane() {
        }

        @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
        public void updateLocale() {
            int index = TabbedEditorPanel.this.tabPane.indexOfComponent(this.scroll);
            if (index == -1) {
                throw new RuntimeException("Cannot find scroll component in tabPane for tab: " + this.name);
            }
            TabbedEditorPanel.this.tabPane.setTitleAt(index, getTabName());
            TabbedEditorPanel.this.tabPane.setToolTipTextAt(index, getTabTip());
        }

        public void addButtons(ExtendedPanel buttonPanel) {
            this.c.gridwidth = 4;
            this.c.gridx = 0;
            add((Component) buttonPanel, (Object) this.c);
        }

        public void addVerticalGap(int i) {
            add(Box.createVerticalStrut(i), this.c);
            this.c.gridy++;
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize((int) HttpStatus.SC_MULTIPLE_CHOICES, 500);
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 2;
        String test = CoreConstants.EMPTY_STRING;
        for (int i = 0; i < 15; i++) {
            test = test + i;
            c.gridx = 0;
            c.gridy = i;
            p.add(new JButton(test), c);
            c.gridx = 1;
            p.add(Box.createHorizontalStrut(0), c);
            c.gridx = 2;
            p.add(new JButton(test), c);
        }
        f.add(p);
        f.setDefaultCloseOperation(3);
        f.setVisible(true);
    }
}
