package by.gdev.http.download.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/service/GsonService.class */
public interface GsonService {
    <T> T getObject(String str, Class<T> cls, boolean z) throws FileNotFoundException, IOException, NoSuchAlgorithmException;

    <T> T getObjectByUrls(List<String> list, String str, Class<T> cls, boolean z) throws FileNotFoundException, IOException, NoSuchAlgorithmException;

    <T> T getObjectWithoutSaving(String str, Class<T> cls) throws IOException;

    <T> T getObjectWithoutSaving(String str, Class<T> cls, Map<String, String> map) throws IOException;

    <T> T getObjectWithoutSaving(String str, Type type) throws IOException;

    <T> T getObjectWithoutSaving(String str, Type type, Map<String, String> map) throws IOException;
}
