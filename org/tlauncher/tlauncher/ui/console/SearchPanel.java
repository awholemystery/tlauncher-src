package org.tlauncher.tlauncher.ui.console;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
import org.tlauncher.tlauncher.ui.loc.LocalizableInvalidateTextField;
import org.tlauncher.tlauncher.ui.swing.ImageButton;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchPanel.class */
public class SearchPanel extends ExtendedPanel {
    final ConsoleFrame cf;
    public final SearchField field = new SearchField();
    public final SearchPrefs prefs = new SearchPrefs();
    public final FindButton find = new FindButton();
    public final KillButton kill = new KillButton();
    private int startIndex;
    private int endIndex;
    private String lastText;
    private boolean lastRegexp;

    SearchPanel(ConsoleFrame cf) {
        this.cf = cf;
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.field).addComponent(this.prefs)).addGap(4).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.find, 48, 48, Integer.MAX_VALUE).addComponent(this.kill)));
        layout.linkSize(0, new Component[]{this.find, this.kill});
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.field).addComponent(this.find, 24, 24, Integer.MAX_VALUE)).addGap(2).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.prefs).addComponent(this.kill)));
        layout.linkSize(1, new Component[]{this.field, this.prefs, this.find, this.kill});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void search() {
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchPanel$SearchField.class */
    public class SearchField extends LocalizableInvalidateTextField {
        private SearchField() {
            super("console.search.placeholder");
            addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.console.SearchPanel.SearchField.1
                public void actionPerformed(ActionEvent e) {
                    SearchPanel.this.search();
                }
            });
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchPanel$SearchPrefs.class */
    public class SearchPrefs extends BorderPanel {
        public final LocalizableCheckbox regexp;

        private SearchPrefs() {
            this.regexp = new LocalizableCheckbox("console.search.prefs.regexp");
            this.regexp.setFocusable(false);
            SearchPanel.this.field.setFont(this.regexp.getFont());
            setWest(this.regexp);
        }

        public boolean getUseRegExp() {
            return this.regexp.isSelected();
        }

        public void setUseRegExp(boolean use) {
            this.regexp.setSelected(use);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchPanel$FindButton.class */
    public class FindButton extends ImageButton {
        private FindButton() {
            addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.console.SearchPanel.FindButton.1
                public void actionPerformed(ActionEvent e) {
                    SearchPanel.this.search();
                }
            });
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchPanel$KillButton.class */
    public class KillButton extends ImageButton {
        private KillButton() {
        }
    }

    private void focus() {
        this.field.requestFocusInWindow();
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchPanel$Range.class */
    private class Range {
        private int start;
        private int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        boolean isCorrect() {
            return this.start > 0 && this.end > this.start;
        }
    }
}
