package org.tlauncher.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.launcher.resource.TlauncherResource;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/SwingUtil.class */
public class SwingUtil {
    private static Map<FontTL, Font> FONTS = new HashMap();
    private static final List<Image> favicons = new ArrayList();
    private static final String methodName = "getApplication";
    private static final String className = "com.apple.eawt.Application";
    private static final String methodSetDocIconImage = "setDockIconImage";

    private static List<Image> getFavicons() {
        if (!favicons.isEmpty()) {
            return Collections.unmodifiableList(favicons);
        }
        int[] sizes = {256, 128, 96, 64, 48, 32, 24, 16};
        StringBuilder loadedBuilder = new StringBuilder();
        for (int i : sizes) {
            Image image = ImageCache.getImage("fav" + i + ".png", false);
            if (image != null) {
                loadedBuilder.append(", ").append(i).append("px");
                favicons.add(image);
            }
        }
        String loaded = loadedBuilder.toString();
        if (loaded.isEmpty()) {
            log("No favicon is loaded.");
        } else {
            log("Favicons loaded:", loaded.substring(2));
        }
        return favicons;
    }

    public static void init() throws IOException, FontFormatException {
        FONTS.put(FontTL.ROBOTO_REGULAR, Font.createFont(0, TlauncherResource.getResource("Roboto-Regular.ttf").openStream()));
        FONTS.put(FontTL.ROBOTO_BOLD, Font.createFont(0, TlauncherResource.getResource("Roboto-Bold.ttf").openStream()));
        FONTS.put(FontTL.ROBOTO_MEDIUM, Font.createFont(0, TlauncherResource.getResource("Roboto-Medium.ttf").openStream()));
        FONTS.put(FontTL.CALIBRI, Font.createFont(0, TlauncherResource.getResource("Calibri.ttf").openStream()));
        FONTS.put(FontTL.CALIBRI_BOLD, Font.createFont(0, TlauncherResource.getResource("Calibri-Bold.ttf").openStream()));
    }

    public static void setFavicons(JFrame frame) {
        if (OS.is(OS.OSX)) {
            try {
                Image image = Toolkit.getDefaultToolkit().getImage(ImageCache.getRes("fav256.png"));
                Class app = Class.forName(className);
                Method method = app.getMethod(methodName, new Class[0]);
                Object instanceApplication = method.invoke(null, new Object[0]);
                Method method2 = instanceApplication.getClass().getMethod(methodSetDocIconImage, Image.class);
                method2.invoke(instanceApplication, image);
                return;
            } catch (Exception e) {
                U.log("couldn't set a favicon for mac os platform", e);
                return;
            }
        }
        frame.setIconImages(getFavicons());
    }

    public static void initFont(int defSize) {
        try {
            UIDefaults defaults = UIManager.getDefaults();
            int maxSize = defSize + 2;
            Enumeration<?> e = defaults.keys();
            while (e.hasMoreElements()) {
                Object key = e.nextElement();
                Object value = defaults.get(key);
                if (value instanceof Font) {
                    Font font = (Font) value;
                    int size = font.getSize();
                    if (size < defSize) {
                        size = defSize;
                    } else if (size > maxSize) {
                        size = maxSize;
                    }
                    if (value instanceof FontUIResource) {
                        defaults.put(key, new FontUIResource(font.getFamily(), font.getStyle(), size));
                    } else {
                        defaults.put(key, new Font("Roboto", font.getStyle(), size));
                    }
                }
            }
        } catch (Exception e2) {
            log("Cannot change font sizes!", e2);
        }
    }

    public static Cursor getCursor(int type) {
        try {
            return Cursor.getPredefinedCursor(type);
        } catch (IllegalArgumentException iaE) {
            iaE.printStackTrace();
            return null;
        }
    }

    public static void setFontSize(JComponent comp, float size) {
        comp.setFont(comp.getFont().deriveFont(size));
    }

    public static void setFontSize(JComponent comp, float size, int type) {
        comp.setFont(comp.getFont().deriveFont(type, size));
    }

    public static Point getRelativeLocation(Component parent, Component comp) {
        Point compLocation = comp.getLocationOnScreen();
        Point parentLocation = parent.getLocationOnScreen();
        return new Point(compLocation.x - parentLocation.x, compLocation.y - parentLocation.y);
    }

    private static void log(Object... o) {
        U.log("[Swing]", o);
    }

    public static void changeFontFamily(JComponent component, FontTL family, int size) {
        try {
            if (new Locale("zh").getLanguage().equals(TLauncher.getInstance().getConfiguration().getLocale().getLanguage())) {
                component.setFont(component.getFont().deriveFont(size));
                return;
            }
        } catch (NullPointerException e) {
            U.log("lg " + new Locale("zh").getLanguage());
            U.log("lg " + TLauncher.getInstance().getConfiguration().getLocale());
        }
        component.setFont(FONTS.get(family).deriveFont(size));
    }

    public static void changeFontFamily(Component component, FontTL family) {
        Component[] components;
        Font f = component.getFont();
        if (f != null) {
            component.setFont(FONTS.get(family).deriveFont(f.getStyle(), f.getSize()));
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFontFamily(child, family);
            }
        }
    }

    public static void changeFontFamily(JComponent component, FontTL family, int size, Color foreground) {
        changeFontFamily(component, family, size);
        component.setForeground(foreground);
    }

    public static void setImageJLabel(JLabel label, String url) {
        setImageJLabel(label, url, null);
    }

    public static void configHorizontalSpingLayout(SpringLayout spring, JComponent target, JComponent coordinated, int width) {
        spring.putConstraint("West", target, 0, "East", coordinated);
        spring.putConstraint("East", target, width, "East", coordinated);
        spring.putConstraint("North", target, 0, "North", coordinated);
        spring.putConstraint("South", target, 0, "South", coordinated);
    }

    public static void setImageJLabel(JLabel label, String url, Dimension dimension) {
        try {
            URL link = new URL(url);
            if (!ImageCache.setLocalIcon(label, url)) {
                AsyncThread.execute(() -> {
                    try {
                        setIcon(label, link, dimension);
                        label.repaint();
                    } catch (RuntimeException e) {
                        U.log(e);
                    }
                });
            } else {
                setIcon(label, link, dimension);
            }
        } catch (MalformedURLException e) {
            log(e);
        }
    }

    private static void setIcon(JLabel label, URL link, Dimension dimension) {
        BufferedImage bufferedImage = ImageCache.loadImage(link);
        if (dimension != null) {
            label.setIcon(new ImageIcon(bufferedImage.getScaledInstance(dimension.width, dimension.height, 4)));
        } else {
            label.setIcon(new ImageIcon(bufferedImage));
        }
    }

    public static int getWidthText(JComponent c, String text) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        return (int) c.getFont().getStringBounds(text, frc).getWidth();
    }

    public static void paintShadowLine(Rectangle rec, Graphics g, int i, int max) {
        if (i < 0) {
            i = 0;
        }
        int current = 0;
        Graphics2D g2 = (Graphics2D) g;
        for (int y = rec.y; y < rec.height + rec.y; y++) {
            g2.setColor(new Color(i, i, i));
            if (current != max && i != 255) {
                current++;
                i++;
            }
            g2.drawLine(rec.x, y, rec.x + rec.width, y);
        }
    }

    public static void paintText(Graphics2D g2d, JComponent comp, String text) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g2d);
        g2d.setFont(comp.getFont());
        g2d.setColor(comp.getForeground());
        int x = (comp.getWidth() - ((int) r.getWidth())) / 2;
        int y = (((comp.getHeight() - ((int) r.getHeight())) / 2) + fm.getAscent()) - 1;
        g2d.drawString(text, x, y);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
