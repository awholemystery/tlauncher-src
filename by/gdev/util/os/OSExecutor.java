package by.gdev.util.os;

import by.gdev.util.model.GPUDriverVersion;
import by.gdev.util.model.GPUsDescriptionDTO;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/os/OSExecutor.class */
public interface OSExecutor {
    String execute(String str, int i) throws IOException, InterruptedException;

    GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException;

    GPUDriverVersion getGPUDriverVersion() throws IOException, InterruptedException;
}
