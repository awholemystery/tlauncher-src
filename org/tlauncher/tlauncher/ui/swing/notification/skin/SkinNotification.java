package org.tlauncher.tlauncher.ui.swing.notification.skin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
import org.tlauncher.tlauncher.ui.swing.editor.EditorPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/notification/skin/SkinNotification.class */
public class SkinNotification extends JPanel {
    private static final int WIDTH = 400;

    public SkinNotification() {
        setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel();
        add(panel, "West");
        panel.setLayout(new GridLayout(1, 0, 0, 0));
        JLabel lblNewLabel = new JLabel(ImageCache.getIcon("notification-picture.png"));
        lblNewLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        panel.add(lblNewLabel);
        JPanel panel_1 = new JPanel();
        add(panel_1, "Center");
        panel_1.setLayout(new BoxLayout(panel_1, 1));
        JPanel titlePanel = new JPanel();
        panel_1.add(titlePanel);
        JLabel title = new JLabel(Localizable.get("skin.notification.up.title"));
        Font font = title.getFont();
        title.setFont(new Font(font.getName(), font.getStyle(), 16));
        titlePanel.add(title);
        JPanel commonInformation = new JPanel();
        panel_1.add(commonInformation);
        FlexibleEditorPanel commonInformationLabel = new FlexibleEditorPanel("text/html", Localizable.get("skin.notification.common.message"), 400);
        commonInformation.add(commonInformationLabel);
        JPanel TlIconPanel = new JPanel();
        panel_1.add(TlIconPanel);
        String textImage = String.format(Localizable.get("skin.notification.image.explanation"), ImageCache.getRes("tlauncher-user.png").toExternalForm());
        FlexibleEditorPanel flexibleEditorPanel = new FlexibleEditorPanel("text/html", textImage, 400);
        TlIconPanel.add(flexibleEditorPanel);
        JPanel detailInformation = new JPanel();
        panel_1.add(detailInformation);
        EditorPane detailSkinLink = new EditorPane("text/html", Localizable.get("skin.notification.link.message"));
        detailInformation.add(detailSkinLink);
        JPanel boxPanel = new JPanel();
        panel_1.add(boxPanel);
        boxPanel.setLayout(new FlowLayout(1, 5, 5));
        JCheckBox notificationState = new JCheckBox(Localizable.get("skin.notification.state"));
        boxPanel.add(notificationState);
        notificationState.addActionListener(e -> {
            if (notificationState.isSelected()) {
                TLauncher.getInstance().getConfiguration().set("skin.notification.off", "true");
            }
        });
    }

    public static void showMessage() {
        SkinNotification notification = new SkinNotification();
        Alert.showCustomMonolog(Localizable.get("skin.notification.title"), notification);
    }
}
