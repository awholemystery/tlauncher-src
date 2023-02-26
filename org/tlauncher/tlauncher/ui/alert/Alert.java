package org.tlauncher.tlauncher.ui.alert;

import ch.qos.logback.core.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.http.HttpStatus;
import org.apache.log4j.varia.ExternallyRolledFileAppender;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/alert/Alert.class */
public class Alert {
    private static final String PREFIX = "TLauncher : ";
    private static final String MISSING_TITLE = "MISSING TITLE";
    private static final String MISSING_MESSAGE = "MISSING MESSAGE";
    private static final String MISSING_QUESTION = "MISSING QUESTION";
    private static final Color YES = new Color(88, 159, 42);
    private static final Color NO = new Color((int) HttpStatus.SC_NO_CONTENT, 118, 47);
    private static final JFrame frame = new JFrame();
    private static String DEFAULT_TITLE = "An error occurred";
    private static String DEFAULT_MESSAGE = "An unexpected error occurred";

    static {
        frame.setAlwaysOnTop(true);
    }

    public static void showError(String title, String message, Object textarea) {
        if (textarea instanceof Throwable) {
            U.log("Showing error:", textarea);
            Throwable throwable = (Throwable) textarea;
            String messageThrowable = CoreConstants.EMPTY_STRING;
            if (Objects.nonNull(throwable.getCause())) {
                messageThrowable = throwable.getCause().getMessage();
            }
            showMonolog(0, title, message, throwable.getMessage() + " : " + messageThrowable);
            return;
        }
        showMonolog(0, title, message, textarea);
    }

    public static void showError(String title, String message) {
        showError(title, message, null);
    }

    public static void showError(String message, Object textarea) {
        showError(DEFAULT_TITLE, message, textarea);
    }

    public static void showError(Object textarea, boolean exit) {
        showError(DEFAULT_TITLE, DEFAULT_MESSAGE, textarea);
        if (exit) {
            System.exit(-1);
        }
    }

    public static void showError(Object textarea) {
        showError(textarea, false);
    }

    public static void showLocError(String titlePath, String messagePath, Object textarea) {
        showError(getLoc(titlePath, MISSING_TITLE), getLoc(messagePath, MISSING_MESSAGE), textarea);
    }

    public static void showLocError(String path, Object textarea) {
        showError(getLoc(path + ".title", MISSING_TITLE), getLoc(path, MISSING_MESSAGE), textarea);
    }

    public static void showLocError(String path) {
        showLocError(path, null);
    }

    public static void showMessage(String title, String message, Object textarea) {
        showMonolog(1, title, message, textarea);
    }

    public static void showMessage(String title, String message) {
        showMessage(title, message, (Object) null);
    }

    public static void showLocMessage(String titlePath, String messagePath, Object textarea) {
        showMessage(getLoc(titlePath, MISSING_TITLE), getLoc(messagePath, MISSING_MESSAGE), textarea);
    }

    public static void showLocMessage(String path, Object textarea) {
        showMessage(getLoc(path + ".title", MISSING_TITLE), getLoc(path, MISSING_MESSAGE), textarea);
    }

    public static void showLocMessageWithoutTitle(String path) {
        showMessage(getLoc(CoreConstants.EMPTY_STRING, MISSING_TITLE), getLoc(path, MISSING_MESSAGE));
    }

    public static void showLocMessage(String path) {
        showLocMessage(path, null);
    }

    public static void showWarning(String title, String message, Object textarea) {
        showMonolog(2, title, message, textarea);
    }

    public static void showWarning(String title, String message) {
        showWarning(title, message, null);
    }

    public static void showLocWarning(String titlePath, String messagePath, Object textarea) {
        showWarning(getLoc(titlePath, MISSING_TITLE), getLoc(messagePath, MISSING_MESSAGE), textarea);
    }

    private static void showLocWarning(String titlePath, String messagePath) {
        showLocWarning(titlePath, messagePath, null);
    }

    public static void showLocWarning(String path, Object textarea) {
        showWarning(getLoc(path + ".title", MISSING_TITLE), getLoc(path, MISSING_MESSAGE), textarea);
    }

    public static void showLocWarning(String path) {
        showLocWarning(path, (Object) null);
    }

    public static boolean showQuestion(String title, String question, Object textarea) {
        return showConfirmDialog(0, 3, title, question, textarea) == 0;
    }

    public static boolean showQuestion(String title, String question) {
        return showQuestion(title, question, null);
    }

    public static boolean showLocQuestion(String titlePath, String questionPath, Object textarea) {
        return showQuestion(getLoc(titlePath, MISSING_TITLE), getLoc(questionPath, MISSING_QUESTION), textarea);
    }

    public static boolean showLocQuestion(String titlePath, String questionPath) {
        return showQuestion(getLoc(titlePath, titlePath), getLoc(questionPath, questionPath), null);
    }

    public static boolean showLocQuestion(String path, Object textarea) {
        return showQuestion(getLoc(path + ".title", MISSING_TITLE), getLoc(path, MISSING_QUESTION), textarea);
    }

    public static boolean showLocQuestion(String path) {
        return showLocQuestion(path, (Object) null);
    }

    private static void showMonolog(int messageType, String title, String message, Object textarea) {
        String button = "ui.ok";
        if (!Localizable.exists()) {
            button = ExternallyRolledFileAppender.OK;
        }
        UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, button);
        addListener(ok, YES);
        ok.setForeground(Color.WHITE);
        JOptionPane.showOptionDialog(frame, new AlertPanel(message, textarea), getTitle(title), 1, messageType, (Icon) null, new Object[]{ok}, 0);
    }

    public static int showErrorMessage(String title, String message, String button1Text, String button2Text) {
        UpdaterButton button1 = new UpdaterButton(UpdaterButton.GREEN_COLOR, button1Text);
        UpdaterButton button2 = new UpdaterButton(UpdaterButton.ORANGE_COLOR, button2Text);
        addListener(button1, YES);
        addListener(button2, NO);
        button1.setForeground(Color.WHITE);
        SwingUtil.setFontSize(button1, 13.0f);
        SwingUtil.setFontSize(button2, 13.0f);
        button2.setForeground(Color.WHITE);
        return JOptionPane.showOptionDialog(frame, Localizable.get(message), Localizable.get(title), 0, 0, (Icon) null, new Object[]{button1, button2}, 0);
    }

    public static void showCustomMonolog(String title, Object textarea) {
        UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, "ui.ok");
        addListener(ok, YES);
        ok.setForeground(Color.WHITE);
        JOptionPane.showOptionDialog(frame, textarea, getTitle(title), 0, -1, (Icon) null, new Object[]{ok}, 0);
    }

    public static int showConfirmDialog(int optionType, int messageType, String title, String message, Object textarea) {
        return showConfirmDialog(optionType, messageType, title, message, textarea, "ui.yes", "ui.no");
    }

    public static int showConfirmDialog(int optionType, int messageType, String title, String message, Object textarea, String yesText, String noText) {
        UpdaterButton yes = new UpdaterButton(UpdaterButton.GREEN_COLOR, yesText);
        yes.setForeground(Color.WHITE);
        UpdaterButton no = new UpdaterButton(UpdaterButton.ORANGE_COLOR, noText);
        no.setForeground(Color.WHITE);
        addListener(yes, YES);
        addListener(no, NO);
        return JOptionPane.showOptionDialog(frame, new AlertPanel(message, textarea), getTitle(title), 0, messageType, (Icon) null, new Object[]{yes, no}, 0);
    }

    public static void prepareLocal() {
        DEFAULT_TITLE = getLoc("alert.error.title", DEFAULT_TITLE);
        DEFAULT_MESSAGE = getLoc("alert.error.message", DEFAULT_MESSAGE);
    }

    private static String getTitle(String title) {
        return PREFIX + (title == null ? MISSING_TITLE : title);
    }

    private static String getLoc(String path, String fallbackMessage) {
        String result = Localizable.get(path);
        return result == null ? fallbackMessage : result;
    }

    public static void showMonologError(String message, int number) {
        JOptionPane.showMessageDialog((Component) null, new AlertPanel(message, null), CoreConstants.EMPTY_STRING, number, (Icon) null);
    }

    protected static JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane(parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }

    public static void addListener(final UpdaterButton changes, final Color color) {
        changes.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent) e.getSource());
            pane.setValue(changes);
        });
        changes.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.alert.Alert.1
            public void mouseEntered(MouseEvent e2) {
                UpdaterButton.this.setBackground(color);
            }

            public void mouseExited(MouseEvent e2) {
                UpdaterButton.this.setBackground(UpdaterButton.this.getBackgroundColor());
            }
        });
    }

    public static boolean showWarningMessageWithCheckBox(String title, String message, int width) {
        return showWarningMessageWithCheckBox(title, message, width, "skin.notification.state");
    }

    public static boolean showWarningMessageWithCheckBox(String title, String message, int width, String buttonText) {
        UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, "ui.ok");
        addListener(ok, YES);
        LocalizableCheckbox b = new LocalizableCheckbox(buttonText, LocalizableCheckbox.PANEL_TYPE.SETTINGS);
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        b.setHorizontalAlignment(0);
        b.setIconTextGap(10);
        b.setState(false);
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        HtmlTextPane pane = HtmlTextPane.get(Localizable.get(message), width);
        panel.add(pane, "Center");
        panel.add(b, "South");
        ok.setForeground(Color.WHITE);
        JOptionPane.showOptionDialog(frame, panel, " " + Localizable.get(title), 0, 1, ImageCache.getIcon("warning.png"), new Object[]{ok}, (Object) null);
        return b.getState();
    }

    public static void showHtmlMessage(String title, String message, int type, int width) {
        String textValue;
        if (Localizable.exists()) {
            textValue = Localizable.get("ui.ok");
        } else {
            textValue = ExternallyRolledFileAppender.OK;
        }
        UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, textValue);
        addListener(ok, YES);
        ok.setForeground(Color.WHITE);
        JOptionPane.showOptionDialog(frame, HtmlTextPane.get(Localizable.get(message), width), Localizable.get(title), 0, type, (Icon) null, new Object[]{ok}, (Object) null);
    }

    public static void showErrorHtml(String message, int width) {
        showHtmlMessage(CoreConstants.EMPTY_STRING, message, 0, width);
    }

    public static void showErrorHtml(String title, String message) {
        showHtmlMessage(title, message, 0, 500);
    }

    public static void showMessage(String title, JPanel content, JButton[] buttons) {
        JDialog jDialog = new JDialog(frame, title);
        Arrays.stream(buttons).forEach(e -> {
            e.addActionListener(a -> {
                jDialog.setVisible(false);
            });
        });
        jDialog.setAlwaysOnTop(true);
        jDialog.setResizable(false);
        jDialog.setContentPane(content);
        jDialog.setModal(true);
        jDialog.pack();
        jDialog.setLocationRelativeTo((Component) null);
        jDialog.setVisible(true);
    }

    public static boolean showWarningMessageWithCheckBox1(String title, String message, int width, String buttonText) {
        JButton ok = new JButton(ExternallyRolledFileAppender.OK);
        ok.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent) e.getSource());
            pane.setValue(ok);
        });
        JCheckBox b = new JCheckBox(buttonText);
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        b.setHorizontalAlignment(0);
        b.setIconTextGap(10);
        b.getModel().setSelected(false);
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        HtmlTextPane pane = HtmlTextPane.get(Localizable.get(message), width);
        panel.add(pane, "Center");
        panel.add(b, "South");
        ok.setForeground(Color.WHITE);
        JOptionPane.showOptionDialog((Component) null, panel, " " + Localizable.get(title), 0, 1, (Icon) null, new Object[]{ok}, (Object) null);
        return b.getModel().isSelected();
    }
}
