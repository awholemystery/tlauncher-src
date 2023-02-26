package org.tlauncher.util;

import java.awt.Color;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/ColorUtil.class */
public class ColorUtil {
    public static final Color COLOR_248 = new Color(248, 248, 248);
    public static final Color COLOR_247 = new Color(247, 247, 247);
    public static final Color COLOR_246 = new Color(246, 246, 246);
    public static final Color COLOR_244 = new Color(244, 244, 244);
    public static final Color COLOR_241 = new Color(241, 241, 241);
    public static final Color COLOR_235 = new Color(235, 235, 235);
    public static final Color COLOR_233 = new Color(233, 233, 233);
    public static final Color COLOR_222 = new Color(222, 64, 43);
    public static final Color COLOR_215 = new Color((int) ModpackScene.WIDTH_SEARCH_PANEL, (int) ModpackScene.WIDTH_SEARCH_PANEL, (int) ModpackScene.WIDTH_SEARCH_PANEL);
    public static final Color COLOR_213 = new Color(213, 213, 213);
    public static final Color COLOR_204 = new Color((int) HttpStatus.SC_NO_CONTENT, 118, 47);
    public static final Color COLOR_202 = new Color((int) HttpStatus.SC_ACCEPTED, (int) HttpStatus.SC_ACCEPTED, (int) HttpStatus.SC_ACCEPTED);
    public static final Color COLOR_195 = new Color(195, 195, 195);
    public static final Color COLOR_193 = new Color(193, 193, 193);
    public static final Color COLOR_169 = new Color(169, 169, 169);
    public static final Color COLOR_149 = new Color(149, 149, 149);
    public static final Color COLOR_77 = new Color(77, 77, 77);
    public static final Color BLUE_COLOR = new Color(69, 168, (int) CompleteSubEntityScene.FullGameEntity.CompleteDescriptionGamePanel.SHADOW_PANEL);
    public static final Color COLOR_64 = new Color(64, 64, 64);
    public static final Color COLOR_25 = new Color(25, 25, 25);
    public static final Color COLOR_16 = new Color(16, 16, 16);
    public static final Color BLUE_COLOR_UNDER = new Color(2, 161, 221);
    public static final Color BLUE_MODPACK_BUTTON_UP = new Color(2, 161, 221);
    public static final Color BLUE_MODPACK_SEARCH_UP = new Color(2, 165, 221);
    public static final Color BLUE_MODPACK = new Color(60, 170, 232);
    public static final Color BACKGROUND_COMBO_BOX_POPUP = new Color(63, 177, 241);
    public static final Color BACKGROUND_COMBO_BOX_POPUP_SELECTED = new Color(30, 136, 195);

    public static Color get(int i) {
        return new Color(i, i, i);
    }
}
