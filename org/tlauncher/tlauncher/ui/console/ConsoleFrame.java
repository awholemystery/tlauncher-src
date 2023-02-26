package org.tlauncher.tlauncher.ui.console;

import ch.qos.logback.core.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import javax.swing.Action;
import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.explorer.filters.FilesAndExtentionFilter;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.EmptyAction;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.TextPopup;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.pastebin.Paste;
import org.tlauncher.util.pastebin.PasteListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/ConsoleFrame.class */
public class ConsoleFrame extends JFrame implements PasteListener, LocalizableComponent {
    public static final int MIN_WIDTH = 670;
    public static final int MIN_HEIGHT = 500;
    public final Console console;
    public final JScrollBar vScrollbar;
    public final ConsoleFrameBottom bottom;
    public final ConsoleTextPopup popup;
    private int lastWindowWidth;
    private int scrollBarValue;
    private boolean scrollDown;
    volatile boolean hiding;
    private final Object busy = new Object();
    public final JTextArea textarea = new JTextArea();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConsoleFrame(Console console) {
        this.console = console;
        this.textarea.setLineWrap(true);
        this.textarea.setEditable(false);
        this.textarea.setAutoscrolls(true);
        this.textarea.setMargin(new Insets(0, 0, 0, 0));
        this.textarea.setFont(new Font("DialogInput", 0, (int) (new LocalizableLabel().getFont().getSize() * 1.2d)));
        this.textarea.setForeground(Color.white);
        this.textarea.setCaretColor(Color.white);
        this.textarea.setBackground(Color.black);
        this.textarea.setSelectionColor(Color.gray);
        this.textarea.getCaret().setUpdatePolicy(2);
        this.popup = new ConsoleTextPopup();
        this.textarea.addMouseListener(this.popup);
        ScrollPane scrollPane = new ScrollPane(this.textarea);
        scrollPane.setBorder(null);
        scrollPane.setVBPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.vScrollbar = scrollPane.getVerticalScrollBar();
        BoundedRangeModel vsbModel = this.vScrollbar.getModel();
        this.vScrollbar.addAdjustmentListener(e -> {
            if (getWidth() != this.lastWindowWidth) {
                return;
            }
            int nv = vsbModel.getValue();
            if (nv < this.scrollBarValue) {
                this.scrollDown = false;
            } else if (nv == vsbModel.getMaximum() - vsbModel.getExtent()) {
                this.scrollDown = true;
            }
            this.scrollBarValue = nv;
        });
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.console.ConsoleFrame.1
            public void componentResized(ComponentEvent e2) {
                ConsoleFrame.this.lastWindowWidth = ConsoleFrame.this.getWidth();
            }
        });
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, "Center");
        Container contentPane = getContentPane();
        ConsoleFrameBottom consoleFrameBottom = new ConsoleFrameBottom(this);
        this.bottom = consoleFrameBottom;
        contentPane.add(consoleFrameBottom, "South");
        SwingUtil.setFavicons(this);
    }

    public void println(String string) {
        print(string + '\n');
    }

    public void print(String string) {
        synchronized (this.busy) {
            Document document = this.textarea.getDocument();
            try {
                document.insertString(document.getLength(), string, (AttributeSet) null);
            } catch (Throwable th) {
            }
            if (this.scrollDown) {
                scrollDown();
            }
        }
    }

    public void clear() {
        this.textarea.setText(CoreConstants.EMPTY_STRING);
    }

    public void scrollDown() {
        SwingUtilities.invokeLater(() -> {
            this.vScrollbar.setValue(this.vScrollbar.getMaximum());
        });
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setTitle(Localizable.get("console"));
        Localizable.updateContainer(this);
    }

    @Override // org.tlauncher.util.pastebin.PasteListener
    public void pasteUploading(Paste paste) {
        this.bottom.pastebin.setEnabled(false);
        this.popup.pastebinAction.setEnabled(false);
    }

    @Override // org.tlauncher.util.pastebin.PasteListener
    public void pasteDone(Paste paste) {
        this.bottom.pastebin.setEnabled(true);
        this.popup.pastebinAction.setEnabled(true);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/ConsoleFrame$ConsoleTextPopup.class */
    public class ConsoleTextPopup extends TextPopup {
        private final FileChooser explorer = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
        private final Action saveAllAction;
        private final Action pastebinAction;
        private final Action clearAllAction;

        ConsoleTextPopup() {
            this.explorer.setFileFilter(new FilesAndExtentionFilter("log", "log"));
            this.saveAllAction = new EmptyAction() { // from class: org.tlauncher.tlauncher.ui.console.ConsoleFrame.ConsoleTextPopup.1
                public void actionPerformed(ActionEvent e) {
                    ConsoleTextPopup.this.onSavingCalled();
                }
            };
            this.pastebinAction = new EmptyAction() { // from class: org.tlauncher.tlauncher.ui.console.ConsoleFrame.ConsoleTextPopup.2
                public void actionPerformed(ActionEvent e) {
                    ConsoleFrame.this.console.sendPaste();
                }
            };
            this.clearAllAction = new EmptyAction() { // from class: org.tlauncher.tlauncher.ui.console.ConsoleFrame.ConsoleTextPopup.3
                public void actionPerformed(ActionEvent e) {
                    ConsoleTextPopup.this.onClearCalled();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.tlauncher.tlauncher.ui.swing.TextPopup
        public JPopupMenu getPopup(MouseEvent e, JTextComponent comp) {
            JPopupMenu menu = super.getPopup(e, comp);
            if (menu == null) {
                return null;
            }
            menu.addSeparator();
            menu.add(this.saveAllAction).setText(Localizable.get("console.save.popup"));
            menu.add(this.pastebinAction).setText(Localizable.get("console.pastebin"));
            menu.addSeparator();
            menu.add(this.clearAllAction).setText(Localizable.get("console.clear.popup"));
            return menu;
        }

        protected void onSavingCalled() {
            this.explorer.setSelectedFile(new File(ConsoleFrame.this.console.getName() + ".log"));
            int result = this.explorer.showSaveDialog(ConsoleFrame.this);
            if (result != 0) {
                return;
            }
            File file = this.explorer.getSelectedFile();
            if (file == null) {
                U.log("Returned NULL. Damn it!");
                return;
            }
            String path = file.getAbsolutePath();
            if (!path.endsWith(".log")) {
                path = path + ".log";
            }
            File file2 = new File(path);
            OutputStream output = null;
            try {
                FileUtil.createFile(file2);
                StringReader input = new StringReader(U.readFileLog());
                output = new BufferedOutputStream(new FileOutputStream(file2));
                while (true) {
                    int current = input.read();
                    if (current == -1) {
                        break;
                    }
                    if (current == 10 && OS.WINDOWS.isCurrent()) {
                        output.write(13);
                    }
                    output.write(current);
                }
                output.close();
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ignored) {
                        ignored.printStackTrace();
                    }
                }
            } catch (Throwable throwable) {
                try {
                    Alert.showLocError("console.save.error", throwable);
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException ignored2) {
                            ignored2.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException ignored3) {
                            ignored3.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        }

        protected void onClearCalled() {
            ConsoleFrame.this.console.clear();
        }
    }
}
