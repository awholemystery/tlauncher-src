package org.tlauncher.tlauncher.ui.log;

import com.google.common.collect.Maps;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.minecraft.launcher.Http;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.tlauncher.configuration.InnerConfiguration;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.util.ValidateUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/log/LogFrame.class */
public class LogFrame extends JFrame {
    private final JFrame parent;
    private JPanel contentPane;
    private JTextField emailField;

    public LogFrame(JFrame frame, Throwable errorMessage) {
        if (frame == null) {
            this.parent = new JFrame();
        } else {
            this.parent = frame;
        }
        setBounds(100, 100, HttpStatus.SC_BAD_REQUEST, 366);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(this.contentPane);
        this.contentPane.setLayout((LayoutManager) null);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle(Localizable.get("log.form.title"));
        SwingUtil.setFavicons(this);
        JPanel panel = new JPanel();
        panel.setBounds(0, 11, (int) HttpStatus.SC_BAD_REQUEST, 316);
        this.contentPane.add(panel);
        panel.setLayout((LayoutManager) null);
        JLabel lblEmail = new JLabel(Localizable.get("check.email.name"));
        lblEmail.setBounds(10, 10, 334, 15);
        panel.add(lblEmail);
        this.emailField = new JTextField();
        this.emailField.setBounds(10, 25, (int) HttpStatus.SC_MULTI_STATUS, 20);
        panel.add(this.emailField);
        this.emailField.setColumns(10);
        JLabel descriptionErrorLabel = new JLabel(Localizable.get("log.email.error.description"));
        descriptionErrorLabel.setBounds(10, 55, 334, 15);
        panel.add(descriptionErrorLabel);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(31);
        scrollPane.setBounds(10, 70, 380, 80);
        panel.add(scrollPane);
        JTextArea descriptionErrorArea = new JTextArea();
        descriptionErrorArea.setLineWrap(true);
        descriptionErrorArea.setRows(5);
        descriptionErrorArea.setColumns(10);
        scrollPane.setViewportView(descriptionErrorArea);
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setHorizontalScrollBarPolicy(31);
        scrollPane_1.setViewportBorder(UIManager.getBorder("TextPane.border"));
        scrollPane_1.setBounds(10, 175, 380, 96);
        panel.add(scrollPane_1);
        JTextArea outputErrorArea = new JTextArea();
        outputErrorArea.setColumns(10);
        outputErrorArea.setRows(5);
        outputErrorArea.setLineWrap(true);
        scrollPane_1.setViewportView(outputErrorArea);
        outputErrorArea.setText(errorMessage.getClass().getName() + ": " + errorMessage.getMessage());
        outputErrorArea.setEditable(false);
        outputErrorArea.setEnabled(true);
        JButton btnNewButton = new JButton(Localizable.get("log.form.send"));
        btnNewButton.setBounds(61, 282, 134, 23);
        panel.add(btnNewButton);
        JLabel outputLabelError = new JLabel(Localizable.get("log.email.error.issue"));
        outputLabelError.setBounds(10, 161, 334, 14);
        panel.add(outputLabelError);
        JButton noSendButton = new JButton(Localizable.get("log.form.send.no"));
        noSendButton.setBounds((int) HttpStatus.SC_RESET_CONTENT, 282, 128, 23);
        panel.add(noSendButton);
        this.parent.setEnabled(false);
        addWindowListener(new WindowAdapter() { // from class: org.tlauncher.tlauncher.ui.log.LogFrame.1
            public void windowClosing(WindowEvent e) {
                LogFrame.this.releaseLog(LogFrame.this.parent);
            }
        });
        btnNewButton.addActionListener(e -> {
            if (!this.emailField.getText().isEmpty() && !ValidateUtil.validateEmail(this.emailField.getText())) {
                JOptionPane.showMessageDialog(this, "check.email.input");
                return;
            }
            releaseLog(this.parent);
            setVisible(false);
            this.parent.setEnabled(true);
            InnerConfiguration s = TLauncher.getInnerSettings();
            try {
                Map<String, Object> query = Maps.newHashMap();
                query.put(ClientCookie.VERSION_ATTR, Double.valueOf(TLauncher.getVersion()));
                query.put("clientType", s.get("type"));
                URL url = Http.constantURL(Http.get(TLauncher.getInnerSettings().get("log.system"), query));
                Http.performPost(url, U.readFileLog().getBytes(TlauncherUtil.LOG_CHARSET), "text/plain", true);
                Alert.showMonologError(Localizable.get().get("alert.error.send.log.success"), 1);
            } catch (Throwable ex) {
                StringWriter stringWriter = new StringWriter();
                ex.printStackTrace(new PrintWriter(stringWriter));
                U.log(stringWriter.toString());
                Alert.showMonologError(Localizable.get().get("alert.error.send.log.unsuccess"), 0);
            }
            dispose();
        });
        noSendButton.addActionListener(e2 -> {
            releaseLog(this.parent);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseLog(JFrame parent) {
        parent.setEnabled(true);
        dispose();
    }
}
