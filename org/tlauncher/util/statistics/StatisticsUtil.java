package org.tlauncher.util.statistics;

import ch.qos.logback.core.CoreConstants;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import net.minecraft.launcher.Http;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.statistics.UniqueClientDTO;
import org.tlauncher.statistics.UpdaterDTO;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.controller.UpdaterFormController;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/statistics/StatisticsUtil.class */
public class StatisticsUtil {
    public static void startSending(String path, Object ob, Map<String, Object> queries) {
        if (!TLauncher.getInstance().getConfiguration().getBoolean("gui.statistics.checkbox")) {
            AsyncThread.execute(() -> {
                try {
                    send(path, ob, queries);
                } catch (IOException e) {
                    U.log(e);
                }
            });
        }
    }

    public static void send(String path, Object ob, Map<String, Object> queries) throws IOException {
        String domain = TLauncher.getInnerSettings().get("statistics.url");
        Http.performPost(new URL(Http.get(domain + path, queries)), TLauncher.getGson().toJson(ob), Http.JSON_CONTENT_TYPE);
    }

    public static void sendMachineInfo(Configuration conf) {
        UniqueClientDTO running = new UniqueClientDTO();
        running.setClientVersion(TLauncher.getVersion());
        running.setOs(OS.CURRENT.name());
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        running.setResolution(size.width + "x" + size.height);
        running.setJavaVersion(System.getProperty("java.version"));
        running.setOsVersion(OS.VERSION);
        running.setUuid(conf.get("client").replace("-", CoreConstants.EMPTY_STRING));
        running.setGpu(conf.get("gpu.info"));
        running.setRam(OS.Arch.TOTAL_RAM_MB);
        String processor = conf.get("process.info");
        if (Objects.nonNull(processor)) {
            processor = processor.trim();
        }
        running.setCpu(processor);
        startSending("save/run/tlauncher/unique/month", running, Maps.newHashMap());
    }

    public static void sendUpdatingInfo(Update update, UpdaterFormController.UserResult res) {
        try {
            UpdaterDTO dto = preparedUpdaterDTO(update, res);
            send("updater/save", dto, Maps.newHashMap());
        } catch (Throwable t) {
            U.log(t);
        }
    }

    public static UpdaterDTO preparedUpdaterDTO(Update update, UpdaterFormController.UserResult res) {
        UpdaterDTO dto = new UpdaterDTO();
        dto.setClient(TLauncher.getInstance().getConfiguration().get("client"));
        dto.setOffer(update.getSelectedOffer().getOffer());
        dto.setArgs(res.getOfferArgs());
        dto.setCurrentVersion(TLauncher.getInnerSettings().getDouble(ClientCookie.VERSION_ATTR));
        dto.setNewVersion(update.getVersion());
        dto.setUpdaterLater(res.getUserChooser() == 0);
        dto.setRequestTime(new Date().toString());
        return dto;
    }
}
