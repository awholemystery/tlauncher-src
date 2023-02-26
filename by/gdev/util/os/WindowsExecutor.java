package by.gdev.util.os;

import by.gdev.util.model.GPUDescription;
import by.gdev.util.model.GPUDriverVersion;
import by.gdev.util.model.GPUsDescriptionDTO;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/os/WindowsExecutor.class */
public class WindowsExecutor implements OSExecutor {
    private static final Logger log = Logger.getLogger(WindowsExecutor.class.getName());
    private static final String UNKNOWN = "unknown";

    @Override // by.gdev.util.os.OSExecutor
    public String execute(String command, int seconds) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("cmd.exe /C chcp 437 & " + command);
        p.waitFor(seconds, TimeUnit.SECONDS);
        String res = IOUtils.toString(p.getInputStream(), "IBM437");
        p.getInputStream().close();
        return res;
    }

    @Override // by.gdev.util.os.OSExecutor
    public GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException {
        Path path = null;
        try {
            path = Files.createTempFile("dxdiag", ".txt", new FileAttribute[0]);
            String command = String.format("dxdiag /whql:off /t %s", path.toAbsolutePath());
            execute(command, 60);
            long size = -1;
            for (int i = 0; i < 60; i++) {
                Thread.sleep(500L);
                if (Files.exists(path, new LinkOption[0])) {
                    if (size == path.toFile().length()) {
                        break;
                    }
                    size = path.toFile().length();
                }
            }
            List<String> list = Files.readAllLines(path, Charset.forName("437"));
            GPUsDescriptionDTO processSystemInfoLines = processSystemInfoLines(list);
            if (Objects.nonNull(path)) {
                Files.deleteIfExists(path);
            }
            return processSystemInfoLines;
        } catch (Throwable th) {
            if (Objects.nonNull(path)) {
                Files.deleteIfExists(path);
            }
            throw th;
        }
    }

    public GPUsDescriptionDTO processSystemInfoLines(List<String> list) {
        Set<String> set = new HashSet<>();
        set.add(UNKNOWN);
        List<GPUDescription> gpus = new ArrayList<>();
        GPUsDescriptionDTO desc = new GPUsDescriptionDTO();
        desc.setRawDescription((String) list.stream().collect(Collectors.joining(System.lineSeparator())));
        for (String s : (List) list.stream().map((v0) -> {
            return v0.toLowerCase();
        }).collect(Collectors.toList())) {
            if (StringUtils.contains(s, "card name:")) {
                GPUDescription g = new GPUDescription();
                g.setName(s.split(":")[1]);
                gpus.add(g);
            }
            if (gpus.size() > 0) {
                if (StringUtils.contains(s, "chip type:")) {
                    gpus.get(gpus.size() - 1).setChipType(s.split(":")[1]);
                } else if (StringUtils.contains(s, "display memory:")) {
                    gpus.get(gpus.size() - 1).setMemory(s.split(":")[1]);
                } else if (StringUtils.contains(s, "current mode:")) {
                    String cm = s.split(":")[1].trim();
                    if (!UNKNOWN.equalsIgnoreCase(cm) && set.size() > 1) {
                        gpus.remove(gpus.size() - 1);
                    } else {
                        set.add(cm);
                    }
                }
            }
        }
        desc.setGpus(gpus);
        return desc;
    }

    @Override // by.gdev.util.os.OSExecutor
    public GPUDriverVersion getGPUDriverVersion() throws IOException, InterruptedException {
        String res = execute("nvcc --version", 60);
        log.config("nvcc --version -> " + res);
        String[] array = res.split(System.lineSeparator());
        if (array.length == 5) {
            String[] array1 = array[4].trim().split(",");
            if (array1.length == 3) {
                String[] array2 = array1[1].trim().split(" ");
                if (array2.length == 2) {
                    String rawCudaVersion = array2[1];
                    log.config("raw cuda version " + rawCudaVersion);
                    Optional<GPUDriverVersion> c = Arrays.stream(GPUDriverVersion.values()).filter(e -> {
                        return e.getValue().equalsIgnoreCase("10.2");
                    }).findAny();
                    if (c.isPresent()) {
                        return c.get();
                    }
                    return null;
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
