package org.tlauncher.tlauncher.controller;

import com.google.common.collect.Maps;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import net.minecraft.launcher.Http;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.TLauncherFrame;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.updater.UpdaterMessageView;
import org.tlauncher.tlauncher.updater.client.Offer;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.util.OS;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.statistics.StatisticsUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/UpdaterFormController.class */
public class UpdaterFormController {
    private UpdaterMessageView view;
    private Update update;
    private int messageType;
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Configuration settings;

    public UpdaterFormController(Update update, Configuration settings) {
        this.update = update;
        this.settings = settings;
        int force = update.getUpdaterView();
        String lang = Localizable.get().getSelected().toString();
        boolean hashOfferDelay = false;
        if (settings.isExist("updater.offer.installer.empty.checkbox.delay")) {
            Date offerDelay = DateUtils.addDays(new Date(settings.getLong("updater.offer.installer.empty.checkbox.delay")), update.getOfferEmptyCheckboxDelay());
            if (offerDelay.after(new Date())) {
                hashOfferDelay = true;
            }
        }
        if (settings.isExist("updater.offer.installer.delay")) {
            Date offerDelay2 = DateUtils.addDays(new Date(settings.getLong("updater.offer.installer.delay")), update.getOfferDelay());
            if (offerDelay2.after(new Date())) {
                hashOfferDelay = true;
            }
        }
        try {
            if (settings.isExist("updater.offer.installer.empty.checkbox.delay1")) {
                Date offerDelay3 = DateUtils.addDays(FORMAT.parse(settings.get("updater.offer.installer.empty.checkbox.delay1")), update.getOfferEmptyCheckboxDelay());
                if (offerDelay3.after(new Date())) {
                    hashOfferDelay = true;
                }
            }
            if (settings.isExist("updater.offer.installer.delay1")) {
                Date offerDelay4 = DateUtils.addDays(FORMAT.parse(settings.get("updater.offer.installer.delay1")), update.getOfferDelay());
                if (offerDelay4.after(new Date())) {
                    hashOfferDelay = true;
                }
            }
        } catch (ParseException e) {
        }
        if (!hashOfferDelay && OS.is(OS.WINDOWS) && force == 2 && update.getOfferByLang(lang).isPresent()) {
            update.setSelectedOffer(update.getOfferByLang(lang).get());
            this.messageType = 2;
        } else if (!update.getBanners().isEmpty() && update.getBanners().get(lang) != null && force != 0) {
            this.messageType = 1;
        } else {
            this.messageType = 0;
        }
        this.view = new UpdaterMessageView(update, this.messageType, lang, TlauncherUtil.isAdmin());
    }

    public boolean getResult() {
        long delay = this.settings.getLong("updater.delay");
        int hours = TLauncher.getInnerSettings().getInteger("updater.chooser.delay");
        if (new Date().getTime() < new Date(delay).getTime() + (hours * 3600 * 1000)) {
            return false;
        }
        return processUpdating();
    }

    private boolean processUpdating() {
        UserResult res = this.view.showMessage();
        switch (res.getUserChooser()) {
            case 0:
                if (this.messageType == 2 && isExecutedOffer(res)) {
                    return processUpdating();
                }
                this.settings.set("updater.delay", Long.valueOf(new Date().getTime()), true);
                return false;
            case 1:
                if (this.messageType == 2 && isExecutedOffer(res)) {
                    return processUpdating();
                }
                return true;
            default:
                TLauncherFrame frame = TLauncher.getInstance().getFrame();
                if (Objects.isNull(frame)) {
                    System.exit(0);
                    return false;
                }
                return false;
        }
    }

    private boolean isExecutedOffer(UserResult res) {
        try {
            if (res.getUserChooser() == 1 || (res.getUserChooser() == 0 && this.update.isUpdaterLaterInstall())) {
                Offer offer = this.update.getSelectedOffer();
                Path tempFile = Files.createTempFile("install", ".exe", new FileAttribute[0]);
                FileUtils.copyURLToFile(new URL(offer.getInstaller()), tempFile.toFile(), 30000, 30000);
                String args = offer.getArgs().get(res.getOfferArgs());
                String runningOffer = tempFile + " " + args;
                if (res.isSelectedAnyCheckBox()) {
                    Path runner = Files.createTempFile("TLauncherUpdater", ".exe", new FileAttribute[0]);
                    FileUtils.copyURLToFile(new URL(this.update.getRootAccessExe().get(0)), runner.toFile(), 30000, 30000);
                    TLauncher.getInstance().getDownloader().setConfiguration(ConnectionQuality.BAD);
                    String url = Http.get(TLauncher.getInnerSettings().get("statistics.url") + "updater/save", Maps.newHashMap());
                    String data = TLauncher.getGson().toJson(StatisticsUtil.preparedUpdaterDTO(this.update, res));
                    String[] s = {"cmd", "/c", runner.toString(), runningOffer.replace("\"", "\\\""), url, data.replace("\"", "\\\"")};
                    Process p = Runtime.getRuntime().exec(s);
                    if (p.waitFor() == 1) {
                        return true;
                    }
                } else {
                    StatisticsUtil.sendUpdatingInfo(this.update, res);
                    Runtime.getRuntime().exec(runningOffer);
                }
            }
        } catch (Exception e) {
            U.log(e);
        }
        if (res.isSelectedAnyCheckBox()) {
            TLauncher.getInstance().getConfiguration().set("updater.offer.installer.delay1", FORMAT.format(new Date()), true);
            return false;
        }
        TLauncher.getInstance().getConfiguration().set("updater.offer.installer.empty.checkbox.delay1", FORMAT.format(new Date()), true);
        return false;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/UpdaterFormController$UserResult.class */
    public static class UserResult {
        private String offerArgs;
        private int userChooser;
        private boolean selectedAnyCheckBox;

        public void setOfferArgs(String offerArgs) {
            this.offerArgs = offerArgs;
        }

        public void setUserChooser(int userChooser) {
            this.userChooser = userChooser;
        }

        public void setSelectedAnyCheckBox(boolean selectedAnyCheckBox) {
            this.selectedAnyCheckBox = selectedAnyCheckBox;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof UserResult) {
                UserResult other = (UserResult) o;
                if (other.canEqual(this)) {
                    Object this$offerArgs = getOfferArgs();
                    Object other$offerArgs = other.getOfferArgs();
                    if (this$offerArgs == null) {
                        if (other$offerArgs != null) {
                            return false;
                        }
                    } else if (!this$offerArgs.equals(other$offerArgs)) {
                        return false;
                    }
                    return getUserChooser() == other.getUserChooser() && isSelectedAnyCheckBox() == other.isSelectedAnyCheckBox();
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof UserResult;
        }

        public int hashCode() {
            Object $offerArgs = getOfferArgs();
            int result = (1 * 59) + ($offerArgs == null ? 43 : $offerArgs.hashCode());
            return (((result * 59) + getUserChooser()) * 59) + (isSelectedAnyCheckBox() ? 79 : 97);
        }

        public String toString() {
            return "UpdaterFormController.UserResult(offerArgs=" + getOfferArgs() + ", userChooser=" + getUserChooser() + ", selectedAnyCheckBox=" + isSelectedAnyCheckBox() + ")";
        }

        public String getOfferArgs() {
            return this.offerArgs;
        }

        public int getUserChooser() {
            return this.userChooser;
        }

        public boolean isSelectedAnyCheckBox() {
            return this.selectedAnyCheckBox;
        }
    }
}
