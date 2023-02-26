package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator.class */
public class ParallelScatterZipCreator {
    private final Deque<ScatterZipOutputStream> streams;
    private final ExecutorService es;
    private final ScatterGatherBackingStoreSupplier backingStoreSupplier;
    private final Deque<Future<? extends ScatterZipOutputStream>> futures;
    private final long startedAt;
    private long compressionDoneAt;
    private long scatterDoneAt;
    private final int compressionLevel;
    private final ThreadLocal<ScatterZipOutputStream> tlScatterStreams;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator$DefaultBackingStoreSupplier.class */
    private static class DefaultBackingStoreSupplier implements ScatterGatherBackingStoreSupplier {
        final AtomicInteger storeNum;

        private DefaultBackingStoreSupplier() {
            this.storeNum = new AtomicInteger(0);
        }

        @Override // org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier
        public ScatterGatherBackingStore get() throws IOException {
            Path tempFile = Files.createTempFile("parallelscatter", "n" + this.storeNum.incrementAndGet(), new FileAttribute[0]);
            return new FileBasedScatterGatherBackingStore(tempFile);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ScatterZipOutputStream createDeferred(ScatterGatherBackingStoreSupplier scatterGatherBackingStoreSupplier) throws IOException {
        ScatterGatherBackingStore bs = scatterGatherBackingStoreSupplier.get();
        StreamCompressor sc = StreamCompressor.create(this.compressionLevel, bs);
        return new ScatterZipOutputStream(bs, sc);
    }

    public ParallelScatterZipCreator() {
        this(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    public ParallelScatterZipCreator(ExecutorService executorService) {
        this(executorService, new DefaultBackingStoreSupplier());
    }

    public ParallelScatterZipCreator(ExecutorService executorService, ScatterGatherBackingStoreSupplier backingStoreSupplier) {
        this(executorService, backingStoreSupplier, -1);
    }

    public ParallelScatterZipCreator(ExecutorService executorService, ScatterGatherBackingStoreSupplier backingStoreSupplier, int compressionLevel) throws IllegalArgumentException {
        this.streams = new ConcurrentLinkedDeque();
        this.futures = new ConcurrentLinkedDeque();
        this.startedAt = System.currentTimeMillis();
        this.tlScatterStreams = new ThreadLocal<ScatterZipOutputStream>() { // from class: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public ScatterZipOutputStream initialValue() {
                try {
                    ScatterZipOutputStream scatterStream = ParallelScatterZipCreator.this.createDeferred(ParallelScatterZipCreator.this.backingStoreSupplier);
                    ParallelScatterZipCreator.this.streams.add(scatterStream);
                    return scatterStream;
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
        if ((compressionLevel < 0 || compressionLevel > 9) && compressionLevel != -1) {
            throw new IllegalArgumentException("Compression level is expected between -1~9");
        }
        this.backingStoreSupplier = backingStoreSupplier;
        this.es = executorService;
        this.compressionLevel = compressionLevel;
    }

    public void addArchiveEntry(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier source) {
        submitStreamAwareCallable(createCallable(zipArchiveEntry, source));
    }

    public void addArchiveEntry(ZipArchiveEntryRequestSupplier zipArchiveEntryRequestSupplier) {
        submitStreamAwareCallable(createCallable(zipArchiveEntryRequestSupplier));
    }

    public final void submit(Callable<? extends Object> callable) {
        submitStreamAwareCallable(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0008: INVOKE  
              (r4v0 'this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator A[D('this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator), IMMUTABLE_TYPE, THIS])
              (wrap: java.util.concurrent.Callable<? extends org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> : 0x0003: INVOKE_CUSTOM (r1v1 java.util.concurrent.Callable<? extends org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> A[REMOVE]) = 
              (r4v0 'this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator A[D('this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r5v0 'callable' java.util.concurrent.Callable<? extends java.lang.Object> A[D('callable' java.util.concurrent.Callable<? extends java.lang.Object>), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.util.concurrent.Callable.call():java.lang.Object
             call insn: ?: INVOKE  (r1 I:org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator), (r2 I:java.util.concurrent.Callable) type: DIRECT call: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.lambda$submit$0(java.util.concurrent.Callable):org.apache.commons.compress.archivers.zip.ScatterZipOutputStream)
             type: VIRTUAL call: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.submitStreamAwareCallable(java.util.concurrent.Callable):void in method: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.submit(java.util.concurrent.Callable<? extends java.lang.Object>):void, file: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
            	... 15 more
            */
        /*
            this = this;
            r0 = r4
            r1 = r4
            r2 = r5
            void r1 = () -> { // java.util.concurrent.Callable.call():java.lang.Object
                return r1.lambda$submit$0(r2);
            }
            r0.submitStreamAwareCallable(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.submit(java.util.concurrent.Callable):void");
    }

    public final void submitStreamAwareCallable(Callable<? extends ScatterZipOutputStream> callable) {
        this.futures.add(this.es.submit(callable));
    }

    public final Callable<ScatterZipOutputStream> createCallable(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier source) {
        int method = zipArchiveEntry.getMethod();
        if (method == -1) {
            throw new IllegalArgumentException("Method must be set on zipArchiveEntry: " + zipArchiveEntry);
        }
        ZipArchiveEntryRequest zipArchiveEntryRequest = ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, source);
        return ()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0034: RETURN  
              (wrap: java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> : 0x002f: INVOKE_CUSTOM (r0v6 java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> A[REMOVE]) = 
              (r5v0 'this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator A[D('this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r0v4 'zipArchiveEntryRequest' org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequest A[D('zipArchiveEntryRequest' org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequest), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.util.concurrent.Callable.call():java.lang.Object
             call insn: ?: INVOKE  
              (r0 I:org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator)
              (r1 I:org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequest)
             type: DIRECT call: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.lambda$createCallable$1(org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequest):org.apache.commons.compress.archivers.zip.ScatterZipOutputStream)
             in method: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.createCallable(org.apache.commons.compress.archivers.zip.ZipArchiveEntry, org.apache.commons.compress.parallel.InputStreamSupplier):java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream>, file: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator.class
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
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:345)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 19 more
            */
        /*
            this = this;
            r0 = r6
            int r0 = r0.getMethod()
            r8 = r0
            r0 = r8
            r1 = -1
            if (r0 != r1) goto L25
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Method must be set on zipArchiveEntry: "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r6
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L25:
            r0 = r6
            r1 = r7
            org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequest r0 = org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequest.createZipArchiveEntryRequest(r0, r1)
            r9 = r0
            r0 = r5
            r1 = r9
            java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> r0 = () -> { // java.util.concurrent.Callable.call():java.lang.Object
                return r0.lambda$createCallable$1(r1);
            }
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.createCallable(org.apache.commons.compress.archivers.zip.ZipArchiveEntry, org.apache.commons.compress.parallel.InputStreamSupplier):java.util.concurrent.Callable");
    }

    public final Callable<ScatterZipOutputStream> createCallable(ZipArchiveEntryRequestSupplier zipArchiveEntryRequestSupplier) {
        return ()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: RETURN  
              (wrap: java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> : 0x0002: INVOKE_CUSTOM (r0v1 java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> A[REMOVE]) = 
              (r3v0 'this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator A[D('this' org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'zipArchiveEntryRequestSupplier' org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier A[D('zipArchiveEntryRequestSupplier' org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.util.concurrent.Callable.call():java.lang.Object
             call insn: ?: INVOKE  
              (r0 I:org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator)
              (r1 I:org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier)
             type: DIRECT call: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.lambda$createCallable$2(org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier):org.apache.commons.compress.archivers.zip.ScatterZipOutputStream)
             in method: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.createCallable(org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier):java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream>, file: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ParallelScatterZipCreator.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:345)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 15 more
            */
        /*
            this = this;
            r0 = r3
            r1 = r4
            java.util.concurrent.Callable<org.apache.commons.compress.archivers.zip.ScatterZipOutputStream> r0 = () -> { // java.util.concurrent.Callable.call():java.lang.Object
                return r0.lambda$createCallable$2(r1);
            }
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator.createCallable(org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier):java.util.concurrent.Callable");
    }

    public void writeTo(ZipArchiveOutputStream targetStream) throws IOException, InterruptedException, ExecutionException {
        try {
            for (Future<?> future : this.futures) {
                future.get();
            }
            this.es.shutdown();
            this.es.awaitTermination(60000L, TimeUnit.SECONDS);
            this.compressionDoneAt = System.currentTimeMillis();
            for (Future<? extends ScatterZipOutputStream> future2 : this.futures) {
                ScatterZipOutputStream scatterStream = future2.get();
                scatterStream.zipEntryWriter().writeNextZipEntry(targetStream);
            }
            for (ScatterZipOutputStream scatterStream2 : this.streams) {
                scatterStream2.close();
            }
            this.scatterDoneAt = System.currentTimeMillis();
            closeAll();
        } catch (Throwable th) {
            closeAll();
            throw th;
        }
    }

    public ScatterStatistics getStatisticsMessage() {
        return new ScatterStatistics(this.compressionDoneAt - this.startedAt, this.scatterDoneAt - this.compressionDoneAt);
    }

    private void closeAll() {
        for (ScatterZipOutputStream scatterStream : this.streams) {
            try {
                scatterStream.close();
            } catch (IOException e) {
            }
        }
    }
}
