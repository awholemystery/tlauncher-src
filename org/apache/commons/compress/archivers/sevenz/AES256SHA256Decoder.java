package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.compress.PasswordRequiredException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/AES256SHA256Decoder.class */
class AES256SHA256Decoder extends CoderBase {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AES256SHA256Decoder() {
        super(new Class[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
    public InputStream decode(final String archiveName, final InputStream in, long uncompressedLength, final Coder coder, final byte[] passwordBytes, int maxMemoryLimitInKb) {
        return new InputStream() { // from class: org.apache.commons.compress.archivers.sevenz.AES256SHA256Decoder.1
            private boolean isInitialized;
            private CipherInputStream cipherInputStream;

            private CipherInputStream init() throws IOException {
                byte[] aesKeyBytes;
                if (this.isInitialized) {
                    return this.cipherInputStream;
                }
                if (coder.properties == null) {
                    throw new IOException("Missing AES256 properties in " + archiveName);
                }
                if (coder.properties.length < 2) {
                    throw new IOException("AES256 properties too short in " + archiveName);
                }
                int byte0 = 255 & coder.properties[0];
                int numCyclesPower = byte0 & 63;
                int byte1 = 255 & coder.properties[1];
                int ivSize = ((byte0 >> 6) & 1) + (byte1 & 15);
                int saltSize = ((byte0 >> 7) & 1) + (byte1 >> 4);
                if (2 + saltSize + ivSize > coder.properties.length) {
                    throw new IOException("Salt size + IV size too long in " + archiveName);
                }
                byte[] salt = new byte[saltSize];
                System.arraycopy(coder.properties, 2, salt, 0, saltSize);
                byte[] iv = new byte[16];
                System.arraycopy(coder.properties, 2 + saltSize, iv, 0, ivSize);
                if (passwordBytes == null) {
                    throw new PasswordRequiredException(archiveName);
                }
                if (numCyclesPower == 63) {
                    aesKeyBytes = new byte[32];
                    System.arraycopy(salt, 0, aesKeyBytes, 0, saltSize);
                    System.arraycopy(passwordBytes, 0, aesKeyBytes, saltSize, Math.min(passwordBytes.length, aesKeyBytes.length - saltSize));
                } else {
                    try {
                        MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
                        byte[] extra = new byte[8];
                        long j = 0;
                        while (true) {
                            long j2 = j;
                            if (j2 >= (1 << numCyclesPower)) {
                                break;
                            }
                            digest.update(salt);
                            digest.update(passwordBytes);
                            digest.update(extra);
                            for (int k = 0; k < extra.length; k++) {
                                int i = k;
                                extra[i] = (byte) (extra[i] + 1);
                                if (extra[k] != 0) {
                                    break;
                                }
                            }
                            j = j2 + 1;
                        }
                        aesKeyBytes = digest.digest();
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        throw new IOException("SHA-256 is unsupported by your Java implementation", noSuchAlgorithmException);
                    }
                }
                SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
                try {
                    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                    cipher.init(2, aesKey, new IvParameterSpec(iv));
                    this.cipherInputStream = new CipherInputStream(in, cipher);
                    this.isInitialized = true;
                    return this.cipherInputStream;
                } catch (GeneralSecurityException generalSecurityException) {
                    throw new IOException("Decryption error (do you have the JCE Unlimited Strength Jurisdiction Policy Files installed?)", generalSecurityException);
                }
            }

            @Override // java.io.InputStream
            public int read() throws IOException {
                return init().read();
            }

            @Override // java.io.InputStream
            public int read(byte[] b, int off, int len) throws IOException {
                return init().read(b, off, len);
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                if (this.cipherInputStream != null) {
                    this.cipherInputStream.close();
                }
            }
        };
    }
}
