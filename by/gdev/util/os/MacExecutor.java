package by.gdev.util.os;

import by.gdev.util.model.GPUsDescriptionDTO;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/os/MacExecutor.class */
public class MacExecutor extends LinuxExecutor {
    @Override // by.gdev.util.os.LinuxExecutor, by.gdev.util.os.OSExecutor
    public GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException {
        String res = execute("system_profiler SPDisplaysDataType", 60);
        return getGPUInfo1(res, "chipset model:");
    }
}
