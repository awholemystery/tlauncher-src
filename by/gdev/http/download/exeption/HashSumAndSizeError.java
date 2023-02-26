package by.gdev.http.download.exeption;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/exeption/HashSumAndSizeError.class */
public class HashSumAndSizeError extends UploadFileException {
    private static final long serialVersionUID = 6549216849433173596L;

    public HashSumAndSizeError(String uri, String localPath, String message) {
        super(uri, localPath, message);
    }
}
