package org.tlauncher.tlauncher.ui.updater;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import org.apache.http.HttpStatus;
import org.slf4j.Marker;
import org.tlauncher.tlauncher.controller.UpdaterFormController;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.images.ImageIcon;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.RoundUpdaterButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
import org.tlauncher.tlauncher.ui.swing.ImagePanel;
import org.tlauncher.tlauncher.ui.swing.OwnImageCheckBox;
import org.tlauncher.tlauncher.updater.client.Banner;
import org.tlauncher.tlauncher.updater.client.Offer;
import org.tlauncher.tlauncher.updater.client.PointOffer;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/updater/UpdaterMessageView.class */
public class UpdaterMessageView extends JPanel {
    private static final long serialVersionUID = -9173760470917959755L;
    private static final Dimension SIZE = new Dimension(900, 600);
    private ImagePanel imageTop;
    private int result;
    private final UpdaterButton ok = new RoundUpdaterButton(Color.WHITE, new Color(107, (int) HttpStatus.SC_ACCEPTED, 45), new Color(91, 174, 37), "launcher.update.updater.button");
    private UpdaterButton updater = new RoundUpdaterButton(Color.WHITE, new Color(107, (int) HttpStatus.SC_ACCEPTED, 45), new Color(91, 174, 37), "launcher.update.updater.button");
    private UpdaterButton laterUpdater = new RoundUpdaterButton(Color.WHITE, new Color(235, 132, 46), new Color((int) HttpStatus.SC_OK, (int) SyslogConstants.LOG_ALERT, 38), "launcher.update.later.button");
    private JPanel down = new JPanel();
    private List<JCheckBox> checkBoxList = new ArrayList();

    public UpdaterMessageView(Update update, int messageType, String lang, boolean isAdmin) {
        String image;
        setPreferredSize(SIZE);
        if (messageType == 2) {
            image = "offer.png";
        } else if (messageType == 1) {
            image = "banner.png";
        } else {
            image = "without-banner-offer.png";
        }
        this.imageTop = new ImagePanel(image, 1.0f, 1.0f, true);
        Component jLabel = new JLabel("TLAUNCHER " + update.getVersion());
        Component createNewAndWrap = HtmlTextPane.createNewAndWrap(Localizable.get(update.isMandatory() ? "launcher.update.message.mandatory" : "launcher.update.message.optional"), 246);
        Component createNewAndWrap2 = HtmlTextPane.createNewAndWrap(update.getDescription(), 246);
        JScrollPane notice = HtmlTextPane.createNewAndWrap(Localizable.get("updater.notice"), 700);
        SpringLayout spring = new SpringLayout();
        LayoutManager springLayout = new SpringLayout();
        SpringLayout downSpring = new SpringLayout();
        setLayout(spring);
        this.imageTop.setLayout(springLayout);
        this.down.setLayout(downSpring);
        spring.putConstraint("West", this.imageTop, 0, "West", this);
        spring.putConstraint("East", this.imageTop, 0, "East", this);
        spring.putConstraint("North", this.imageTop, 0, "North", this);
        spring.putConstraint("South", this.imageTop, 453, "North", this);
        add(this.imageTop);
        spring.putConstraint("West", this.down, 0, "West", this);
        spring.putConstraint("East", this.down, 0, "East", this);
        spring.putConstraint("North", this.down, 453, "North", this);
        spring.putConstraint("South", this.down, 600, "North", this);
        add(this.down);
        springLayout.putConstraint("West", jLabel, 40, "West", this.imageTop);
        springLayout.putConstraint("East", jLabel, 306, "West", this.imageTop);
        springLayout.putConstraint("North", jLabel, 37, "North", this.imageTop);
        springLayout.putConstraint("South", jLabel, 67, "North", this.imageTop);
        this.imageTop.add(jLabel);
        springLayout.putConstraint("West", createNewAndWrap, 40, "West", this.imageTop);
        springLayout.putConstraint("East", createNewAndWrap, 286, "West", this.imageTop);
        springLayout.putConstraint("North", createNewAndWrap, 60, "North", this.imageTop);
        springLayout.putConstraint("South", createNewAndWrap, 160, "North", this.imageTop);
        this.imageTop.add(createNewAndWrap);
        springLayout.putConstraint("West", createNewAndWrap2, 40, "West", this.imageTop);
        springLayout.putConstraint("East", createNewAndWrap2, 286, "West", this.imageTop);
        springLayout.putConstraint("North", createNewAndWrap2, 144, "North", this.imageTop);
        springLayout.putConstraint("South", createNewAndWrap2, -40, "South", this.imageTop);
        this.imageTop.add(createNewAndWrap2);
        downSpring.putConstraint("West", notice, 40, "West", this.down);
        downSpring.putConstraint("East", notice, 0, "East", this.down);
        downSpring.putConstraint("North", notice, 5, "North", this.down);
        downSpring.putConstraint("South", notice, 65, "North", this.down);
        this.down.add(notice);
        if (update.isMandatory()) {
            downSpring.putConstraint("West", this.ok, 365, "West", this.down);
            downSpring.putConstraint("East", this.ok, 535, "West", this.down);
            downSpring.putConstraint("North", this.ok, 86, "North", this.down);
            downSpring.putConstraint("South", this.ok, (int) CoreConstants.CURLY_LEFT, "North", this.down);
            this.down.add(this.ok);
        } else {
            downSpring.putConstraint("West", this.updater, 273, "West", this.down);
            downSpring.putConstraint("East", this.updater, 443, "West", this.down);
            downSpring.putConstraint("North", this.updater, 86, "North", this.down);
            downSpring.putConstraint("South", this.updater, (int) CoreConstants.CURLY_LEFT, "North", this.down);
            this.down.add(this.updater);
            downSpring.putConstraint("West", this.laterUpdater, 454, "West", this.down);
            downSpring.putConstraint("East", this.laterUpdater, 624, "West", this.down);
            downSpring.putConstraint("North", this.laterUpdater, 86, "North", this.down);
            downSpring.putConstraint("South", this.laterUpdater, (int) CoreConstants.CURLY_LEFT, "North", this.down);
            this.down.add(this.laterUpdater);
        }
        if (messageType == 1) {
            final Banner banner = update.getBanners().get(lang).get(0);
            JLabel imagePanel = null;
            try {
                imagePanel = new JLabel(new ImageIcon(ImageCache.loadImage(new URL(banner.getImage()))));
                imagePanel.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.updater.UpdaterMessageView.1
                    public void mouseClicked(MouseEvent e) {
                        OS.openLink(banner.getClickLink());
                    }
                });
            } catch (MalformedURLException e) {
            }
            springLayout.putConstraint("West", imagePanel, 326, "West", this.imageTop);
            springLayout.putConstraint("East", imagePanel, 0, "East", this.imageTop);
            springLayout.putConstraint("North", imagePanel, 0, "North", this.imageTop);
            springLayout.putConstraint("South", imagePanel, 453, "North", this.imageTop);
            this.imageTop.add((Component) imagePanel);
        } else if (messageType == 2) {
            Offer offer = update.getSelectedOffer();
            Component createNewAndWrap3 = HtmlTextPane.createNewAndWrap(offer.getTopText().get(lang), 574);
            springLayout.putConstraint("West", createNewAndWrap3, 326, "West", this.imageTop);
            springLayout.putConstraint("East", createNewAndWrap3, 0, "East", this.imageTop);
            springLayout.putConstraint("North", createNewAndWrap3, 0, "North", this.imageTop);
            springLayout.putConstraint("South", createNewAndWrap3, offer.getStartCheckboxSouth(), "North", this.imageTop);
            this.imageTop.add(createNewAndWrap3);
            int start = offer.getStartCheckboxSouth();
            for (PointOffer p : offer.getCheckBoxes()) {
                Component ownImageCheckBox = new OwnImageCheckBox(p.getTexts().get(lang), "updater-checkbox-on.png", "updater-checkbox-off.png");
                if (isAdmin) {
                    ownImageCheckBox.setSelected(p.isActive());
                } else {
                    ownImageCheckBox.setSelected(false);
                }
                ownImageCheckBox.setIconTextGap(18);
                ownImageCheckBox.setActionCommand(p.getName());
                ownImageCheckBox.setVerticalAlignment(0);
                SwingUtil.changeFontFamily(ownImageCheckBox, FontTL.CALIBRI, 16);
                springLayout.putConstraint("West", ownImageCheckBox, 378, "West", this.imageTop);
                springLayout.putConstraint("East", ownImageCheckBox, 0, "East", this.imageTop);
                springLayout.putConstraint("North", ownImageCheckBox, start, "North", this.imageTop);
                springLayout.putConstraint("South", ownImageCheckBox, start + 39, "North", this.imageTop);
                start += 39;
                this.checkBoxList.add(ownImageCheckBox);
                this.imageTop.add(ownImageCheckBox);
            }
            Component createNewAndWrap4 = HtmlTextPane.createNewAndWrap(offer.getDownText().get(lang), 574);
            springLayout.putConstraint("West", createNewAndWrap4, 328, "West", this.imageTop);
            springLayout.putConstraint("East", createNewAndWrap4, 0, "East", this.imageTop);
            springLayout.putConstraint("North", createNewAndWrap4, 320, "North", this.imageTop);
            springLayout.putConstraint("South", createNewAndWrap4, 0, "South", this.imageTop);
            this.imageTop.add(createNewAndWrap4);
        }
        this.down.setBackground(Color.WHITE);
        SwingUtil.changeFontFamily(jLabel, FontTL.CALIBRI_BOLD, 30);
        SwingUtil.changeFontFamily(this.updater, FontTL.ROBOTO_REGULAR, 13);
        SwingUtil.changeFontFamily(this.ok, FontTL.ROBOTO_REGULAR, 13);
        SwingUtil.changeFontFamily(this.laterUpdater, FontTL.ROBOTO_REGULAR, 13);
        jLabel.setHorizontalTextPosition(2);
        this.updater.addActionListener(e2 -> {
            this.result = 1;
        });
        this.laterUpdater.addActionListener(e3 -> {
            this.result = 0;
        });
        this.ok.addActionListener(e4 -> {
            this.result = 1;
        });
    }

    public UpdaterFormController.UserResult showMessage() {
        this.result = -1;
        Alert.showMessage("  " + Localizable.get("launcher.update.title"), this, new JButton[]{this.updater, this.laterUpdater, this.ok});
        UpdaterFormController.UserResult res = new UpdaterFormController.UserResult();
        res.setUserChooser(this.result);
        StringBuilder builder = new StringBuilder();
        for (JCheckBox box : this.checkBoxList) {
            if (box.isSelected()) {
                if (builder.length() > 0) {
                    builder.append(Marker.ANY_NON_NULL_MARKER);
                }
                builder.append(box.getActionCommand());
                res.setSelectedAnyCheckBox(true);
            }
        }
        res.setOfferArgs(builder.toString());
        return res;
    }
}
