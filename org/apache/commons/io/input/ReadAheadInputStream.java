package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/ReadAheadInputStream.class */
public class ReadAheadInputStream extends InputStream {
    private static final ThreadLocal<byte[]> oneByte;
    private final ReentrantLock stateChangeLock;
    private ByteBuffer activeBuffer;
    private ByteBuffer readAheadBuffer;
    private boolean endOfStream;
    private boolean readInProgress;
    private boolean readAborted;
    private Throwable readException;
    private boolean isClosed;
    private boolean isUnderlyingInputStreamBeingClosed;
    private boolean isReading;
    private final AtomicBoolean isWaiting;
    private final InputStream underlyingInputStream;
    private final ExecutorService executorService;
    private final boolean shutdownExecutorService;
    private final Condition asyncReadComplete;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReadAheadInputStream.class.desiredAssertionStatus();
        oneByte = ThreadLocal.withInitial(() -> {
            return new byte[1];
        });
    }

    private static ExecutorService newExecutorService() {
        return Executors.newSingleThreadExecutor(ReadAheadInputStream::newThread);
    }

    private static Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "commons-io-read-ahead");
        thread.setDaemon(true);
        return thread;
    }

    public ReadAheadInputStream(InputStream inputStream, int bufferSizeInBytes) {
        this(inputStream, bufferSizeInBytes, newExecutorService(), true);
    }

    public ReadAheadInputStream(InputStream inputStream, int bufferSizeInBytes, ExecutorService executorService) {
        this(inputStream, bufferSizeInBytes, executorService, false);
    }

    private ReadAheadInputStream(InputStream inputStream, int bufferSizeInBytes, ExecutorService executorService, boolean shutdownExecutorService) {
        this.stateChangeLock = new ReentrantLock();
        this.isWaiting = new AtomicBoolean(false);
        this.asyncReadComplete = this.stateChangeLock.newCondition();
        if (bufferSizeInBytes <= 0) {
            throw new IllegalArgumentException("bufferSizeInBytes should be greater than 0, but the value is " + bufferSizeInBytes);
        }
        this.executorService = (ExecutorService) Objects.requireNonNull(executorService, "executorService");
        this.underlyingInputStream = (InputStream) Objects.requireNonNull(inputStream, "inputStream");
        this.shutdownExecutorService = shutdownExecutorService;
        this.activeBuffer = ByteBuffer.allocate(bufferSizeInBytes);
        this.readAheadBuffer = ByteBuffer.allocate(bufferSizeInBytes);
        this.activeBuffer.flip();
        this.readAheadBuffer.flip();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        this.stateChangeLock.lock();
        try {
            return (int) Math.min(2147483647L, this.activeBuffer.remaining() + this.readAheadBuffer.remaining());
        } finally {
            this.stateChangeLock.unlock();
        }
    }

    private void checkReadException() throws IOException {
        if (this.readAborted) {
            if (this.readException instanceof IOException) {
                throw ((IOException) this.readException);
            }
            throw new IOException(this.readException);
        }
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        boolean isSafeToCloseUnderlyingInputStream = false;
        this.stateChangeLock.lock();
        try {
            if (this.isClosed) {
                return;
            }
            this.isClosed = true;
            if (!this.isReading) {
                isSafeToCloseUnderlyingInputStream = true;
                this.isUnderlyingInputStreamBeingClosed = true;
            }
            try {
                if (this.shutdownExecutorService) {
                    try {
                        this.executorService.shutdownNow();
                        this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                        if (isSafeToCloseUnderlyingInputStream) {
                            this.underlyingInputStream.close();
                        }
                    } catch (InterruptedException e) {
                        InterruptedIOException iio = new InterruptedIOException(e.getMessage());
                        iio.initCause(e);
                        throw iio;
                    }
                }
            } catch (Throwable th) {
                if (isSafeToCloseUnderlyingInputStream) {
                    this.underlyingInputStream.close();
                }
                throw th;
            }
        } finally {
            this.stateChangeLock.unlock();
        }
    }

    private void closeUnderlyingInputStreamIfNecessary() {
        boolean needToCloseUnderlyingInputStream = false;
        this.stateChangeLock.lock();
        try {
            this.isReading = false;
            if (this.isClosed) {
                if (!this.isUnderlyingInputStreamBeingClosed) {
                    needToCloseUnderlyingInputStream = true;
                }
            }
            if (needToCloseUnderlyingInputStream) {
                try {
                    this.underlyingInputStream.close();
                } catch (IOException e) {
                }
            }
        } finally {
            this.stateChangeLock.unlock();
        }
    }

    private boolean isEndOfStream() {
        return (this.activeBuffer.hasRemaining() || this.readAheadBuffer.hasRemaining() || !this.endOfStream) ? false : true;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.activeBuffer.hasRemaining()) {
            return this.activeBuffer.get() & 255;
        }
        byte[] oneByteArray = oneByte.get();
        if (read(oneByteArray, 0, 1) == -1) {
            return -1;
        }
        return oneByteArray[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int offset, int len) throws IOException {
        if (offset < 0 || len < 0 || len > b.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        if (!this.activeBuffer.hasRemaining()) {
            this.stateChangeLock.lock();
            try {
                waitForAsyncReadComplete();
                if (!this.readAheadBuffer.hasRemaining()) {
                    readAsync();
                    waitForAsyncReadComplete();
                    if (isEndOfStream()) {
                        return -1;
                    }
                }
                swapBuffers();
                readAsync();
                this.stateChangeLock.unlock();
            } finally {
                this.stateChangeLock.unlock();
            }
        }
        int len2 = Math.min(len, this.activeBuffer.remaining());
        this.activeBuffer.get(b, offset, len2);
        return len2;
    }

    private void readAsync() throws IOException {
        this.stateChangeLock.lock();
        try {
            byte[] arr = this.readAheadBuffer.array();
            if (this.endOfStream || this.readInProgress) {
                return;
            }
            checkReadException();
            this.readAheadBuffer.position(0);
            this.readAheadBuffer.flip();
            this.readInProgress = true;
            this.executorService.execute(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x005e: INVOKE  
                  (wrap: java.util.concurrent.ExecutorService : 0x0054: IGET  (r0v25 java.util.concurrent.ExecutorService A[REMOVE]) = 
                  (r4v0 'this' org.apache.commons.io.input.ReadAheadInputStream A[D('this' org.apache.commons.io.input.ReadAheadInputStream), IMMUTABLE_TYPE, THIS])
                 org.apache.commons.io.input.ReadAheadInputStream.executorService java.util.concurrent.ExecutorService)
                  (wrap: java.lang.Runnable : 0x0059: INVOKE_CUSTOM (r1v3 java.lang.Runnable A[REMOVE]) = 
                  (r4v0 'this' org.apache.commons.io.input.ReadAheadInputStream A[D('this' org.apache.commons.io.input.ReadAheadInputStream), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r0v7 'arr' byte[] A[D('arr' byte[]), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  (r1 I:org.apache.commons.io.input.ReadAheadInputStream), (r2 I:byte[]) type: DIRECT call: org.apache.commons.io.input.ReadAheadInputStream.lambda$readAsync$1(byte[]):void)
                 type: INTERFACE call: java.util.concurrent.ExecutorService.execute(java.lang.Runnable):void in method: org.apache.commons.io.input.ReadAheadInputStream.readAsync():void, file: TLauncher-2.876.jar:org/apache/commons/io/input/ReadAheadInputStream.class
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:302)
                	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
                	at java.base/java.util.ArrayList.forEach(Unknown Source)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
                Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
                	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
                	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
                	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
                	at java.base/java.util.Objects.checkIndex(Unknown Source)
                	at java.base/java.util.ArrayList.get(Unknown Source)
                	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
                	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                	... 25 more
                */
            /*
                this = this;
                r0 = r4
                java.util.concurrent.locks.ReentrantLock r0 = r0.stateChangeLock
                r0.lock()
                r0 = r4
                java.nio.ByteBuffer r0 = r0.readAheadBuffer     // Catch: java.lang.Throwable -> L49
                byte[] r0 = r0.array()     // Catch: java.lang.Throwable -> L49
                r5 = r0
                r0 = r4
                boolean r0 = r0.endOfStream     // Catch: java.lang.Throwable -> L49
                if (r0 != 0) goto L1d
                r0 = r4
                boolean r0 = r0.readInProgress     // Catch: java.lang.Throwable -> L49
                if (r0 == 0) goto L25
            L1d:
                r0 = r4
                java.util.concurrent.locks.ReentrantLock r0 = r0.stateChangeLock
                r0.unlock()
                return
            L25:
                r0 = r4
                r0.checkReadException()     // Catch: java.lang.Throwable -> L49
                r0 = r4
                java.nio.ByteBuffer r0 = r0.readAheadBuffer     // Catch: java.lang.Throwable -> L49
                r1 = 0
                java.nio.Buffer r0 = r0.position(r1)     // Catch: java.lang.Throwable -> L49
                r0 = r4
                java.nio.ByteBuffer r0 = r0.readAheadBuffer     // Catch: java.lang.Throwable -> L49
                java.nio.Buffer r0 = r0.flip()     // Catch: java.lang.Throwable -> L49
                r0 = r4
                r1 = 1
                r0.readInProgress = r1     // Catch: java.lang.Throwable -> L49
                r0 = r4
                java.util.concurrent.locks.ReentrantLock r0 = r0.stateChangeLock
                r0.unlock()
                goto L53
            L49:
                r6 = move-exception
                r0 = r4
                java.util.concurrent.locks.ReentrantLock r0 = r0.stateChangeLock
                r0.unlock()
                r0 = r6
                throw r0
            L53:
                r0 = r4
                java.util.concurrent.ExecutorService r0 = r0.executorService
                r1 = r4
                r2 = r5
                void r1 = () -> { // java.lang.Runnable.run():void
                    r1.lambda$readAsync$1(r2);
                }
                r0.execute(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.input.ReadAheadInputStream.readAsync():void");
        }

        private void signalAsyncReadComplete() {
            this.stateChangeLock.lock();
            try {
                this.asyncReadComplete.signalAll();
            } finally {
                this.stateChangeLock.unlock();
            }
        }

        @Override // java.io.InputStream
        public long skip(long n) throws IOException {
            if (n <= 0) {
                return 0L;
            }
            if (n <= this.activeBuffer.remaining()) {
                this.activeBuffer.position(((int) n) + this.activeBuffer.position());
                return n;
            }
            this.stateChangeLock.lock();
            try {
                long skipped = skipInternal(n);
                this.stateChangeLock.unlock();
                return skipped;
            } catch (Throwable th) {
                this.stateChangeLock.unlock();
                throw th;
            }
        }

        private long skipInternal(long n) throws IOException {
            if ($assertionsDisabled || this.stateChangeLock.isLocked()) {
                waitForAsyncReadComplete();
                if (isEndOfStream()) {
                    return 0L;
                }
                if (available() >= n) {
                    int toSkip = (int) n;
                    int toSkip2 = toSkip - this.activeBuffer.remaining();
                    if ($assertionsDisabled || toSkip2 > 0) {
                        this.activeBuffer.position(0);
                        this.activeBuffer.flip();
                        this.readAheadBuffer.position(toSkip2 + this.readAheadBuffer.position());
                        swapBuffers();
                        readAsync();
                        return n;
                    }
                    throw new AssertionError();
                }
                int skippedBytes = available();
                long toSkip3 = n - skippedBytes;
                this.activeBuffer.position(0);
                this.activeBuffer.flip();
                this.readAheadBuffer.position(0);
                this.readAheadBuffer.flip();
                long skippedFromInputStream = this.underlyingInputStream.skip(toSkip3);
                readAsync();
                return skippedBytes + skippedFromInputStream;
            }
            throw new AssertionError();
        }

        private void swapBuffers() {
            ByteBuffer temp = this.activeBuffer;
            this.activeBuffer = this.readAheadBuffer;
            this.readAheadBuffer = temp;
        }

        private void waitForAsyncReadComplete() throws IOException {
            this.stateChangeLock.lock();
            try {
                try {
                    this.isWaiting.set(true);
                    while (this.readInProgress) {
                        this.asyncReadComplete.await();
                    }
                    checkReadException();
                } catch (InterruptedException e) {
                    InterruptedIOException iio = new InterruptedIOException(e.getMessage());
                    iio.initCause(e);
                    throw iio;
                }
            } finally {
                this.isWaiting.set(false);
                this.stateChangeLock.unlock();
            }
        }
    }
