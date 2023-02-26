package org.tlauncher.tlauncher.minecraft.launcher.assitent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/assitent/LanguageAssistance.class */
public class LanguageAssistance extends MinecraftLauncherAssistantWrapper {
    public static String OPTIONS = "options.txt";

    @Override // org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper, org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface
    public void constructProgramArguments(MinecraftLauncher launcher) {
        CompleteVersion v = launcher.getVersion();
        Date date = v.getReleaseTime();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date checkDate = format.parse("2016-11-14");
            File gameFolder = launcher.getRunningMinecraftDir();
            File options = new File(gameFolder, OPTIONS);
            if (options.exists()) {
                List<String> lines = Files.readAllLines(options.toPath(), StandardCharsets.UTF_8);
                int index = -1;
                int i = 0;
                while (true) {
                    if (i >= lines.size()) {
                        break;
                    } else if (!lines.get(i).contains("lang:")) {
                        i++;
                    } else {
                        index = i;
                        break;
                    }
                }
                if (index != -1 && date.before(checkDate)) {
                    String line = lines.get(index);
                    String[] array = line.split(":");
                    String[] arraysLang = array[1].split("_");
                    String end = arraysLang[1];
                    if (!end.toUpperCase().equals(end)) {
                        String line2 = "lang:" + arraysLang[0] + "_" + end.toUpperCase();
                        lines.remove(index);
                        lines.add(index, line2);
                        FileUtil.writeFile(options, String.join(System.lineSeparator(), lines));
                    }
                } else if (index == -1) {
                    String lang = getLang(date, checkDate);
                    lines.add(lang);
                    FileUtil.writeFile(options, String.join(System.lineSeparator(), lines));
                }
            } else {
                String lang2 = getLang(date, checkDate);
                FileUtil.writeFile(options, lang2);
            }
        } catch (Throwable e) {
            U.log(e);
        }
    }

    private String getLang(Date date, Date checkDate) {
        String lang = Localizable.get().getSelected().toString();
        if (date.after(checkDate)) {
            lang = lang.toLowerCase();
        }
        return "lang:" + lang;
    }
}
