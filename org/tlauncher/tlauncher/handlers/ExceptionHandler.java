package org.tlauncher.tlauncher.handlers;

import java.lang.Thread;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.util.Reflect;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/handlers/ExceptionHandler.class */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionHandler instance;
    private static long gcLastCall;
    private static final Logger LOGGER = Logger.getLogger("main");

    public static ExceptionHandler getInstance() {
        if (instance == null) {
            instance = new ExceptionHandler();
        }
        return instance;
    }

    private ExceptionHandler() {
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread t, Throwable e) {
        OutOfMemoryError asOOM = (OutOfMemoryError) Reflect.cast(e, OutOfMemoryError.class);
        if (asOOM != null && reduceMemory(asOOM)) {
            return;
        }
        if (scanTrace(e)) {
            try {
                Alert.showError("Exception in thread " + t.getName(), e);
            } catch (Exception w) {
                w.printStackTrace();
            }
        } else {
            LOGGER.warn("exception", e);
            U.log(e);
        }
        if ((e instanceof UnsatisfiedLinkError) && e.getMessage().contains("jfxwebkit.dll")) {
            U.log("should use new jvm");
            TLauncher.getInstance().getConfiguration().set("not.work.jfxwebkit.dll", (Object) true);
        }
        TlauncherUtil.sendLog(e);
    }

    public static boolean reduceMemory(OutOfMemoryError e) {
        if (e == null) {
            return false;
        }
        U.log("OutOfMemory error has occurred, solving...");
        long currentTime = System.currentTimeMillis();
        long diff = Math.abs(currentTime - gcLastCall);
        if (diff > 5000) {
            gcLastCall = currentTime;
            U.gc();
            return true;
        }
        U.log("GC is unable to reduce memory usage");
        return false;
    }

    private static boolean scanTrace(Throwable e) {
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements) {
            if (element.getClassName().startsWith(U.PROGRAM_PACKAGE)) {
                return true;
            }
        }
        return false;
    }
}
