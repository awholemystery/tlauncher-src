package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/function/IOFunction.class */
public interface IOFunction<T, R> {
    R apply(T t) throws IOException;

    default <V> IOFunction<V, R> compose(IOFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before");
        return v -> {
            return apply(before.apply(before));
        };
    }

    default <V> IOFunction<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before");
        return v -> {
            return apply(before.apply(before));
        };
    }

    default IOSupplier<R> compose(IOSupplier<? extends T> before) {
        Objects.requireNonNull(before, "before");
        return ()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000e: RETURN  
              (wrap: org.apache.commons.io.function.IOSupplier<R> : 0x0009: INVOKE_CUSTOM (r0v3 org.apache.commons.io.function.IOSupplier<R> A[REMOVE]) = 
              (r3v0 'this' org.apache.commons.io.function.IOFunction<T, R> A[D('this' org.apache.commons.io.function.IOFunction<T, R>), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'before' org.apache.commons.io.function.IOSupplier<? extends T> A[D('before' org.apache.commons.io.function.IOSupplier<? extends T>), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: org.apache.commons.io.function.IOSupplier.get():java.lang.Object
             call insn: ?: INVOKE  (r0 I:org.apache.commons.io.function.IOFunction), (r1 I:org.apache.commons.io.function.IOSupplier) type: DIRECT call: org.apache.commons.io.function.IOFunction.lambda$compose$2(org.apache.commons.io.function.IOSupplier):java.lang.Object)
             in method: org.apache.commons.io.function.IOFunction.compose(org.apache.commons.io.function.IOSupplier<? extends T>):org.apache.commons.io.function.IOSupplier<R>, file: TLauncher-2.876.jar:org/apache/commons/io/function/IOFunction.class
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
            r0 = r4
            java.lang.String r1 = "before"
            java.lang.Object r0 = java.util.Objects.requireNonNull(r0, r1)
            r0 = r3
            r1 = r4
            org.apache.commons.io.function.IOSupplier<R> r0 = () -> { // org.apache.commons.io.function.IOSupplier.get():java.lang.Object
                return r0.lambda$compose$2(r1);
            }
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.function.IOFunction.compose(org.apache.commons.io.function.IOSupplier):org.apache.commons.io.function.IOSupplier");
    }

    default IOSupplier<R> compose(Supplier<? extends T> before) {
        Objects.requireNonNull(before, "before");
        return ()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000e: RETURN  
              (wrap: org.apache.commons.io.function.IOSupplier<R> : 0x0009: INVOKE_CUSTOM (r0v3 org.apache.commons.io.function.IOSupplier<R> A[REMOVE]) = 
              (r3v0 'this' org.apache.commons.io.function.IOFunction<T, R> A[D('this' org.apache.commons.io.function.IOFunction<T, R>), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'before' java.util.function.Supplier<? extends T> A[D('before' java.util.function.Supplier<? extends T>), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: org.apache.commons.io.function.IOSupplier.get():java.lang.Object
             call insn: ?: INVOKE  (r0 I:org.apache.commons.io.function.IOFunction), (r1 I:java.util.function.Supplier) type: DIRECT call: org.apache.commons.io.function.IOFunction.lambda$compose$3(java.util.function.Supplier):java.lang.Object)
             in method: org.apache.commons.io.function.IOFunction.compose(java.util.function.Supplier<? extends T>):org.apache.commons.io.function.IOSupplier<R>, file: TLauncher-2.876.jar:org/apache/commons/io/function/IOFunction.class
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
            r0 = r4
            java.lang.String r1 = "before"
            java.lang.Object r0 = java.util.Objects.requireNonNull(r0, r1)
            r0 = r3
            r1 = r4
            org.apache.commons.io.function.IOSupplier<R> r0 = () -> { // org.apache.commons.io.function.IOSupplier.get():java.lang.Object
                return r0.lambda$compose$3(r1);
            }
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.function.IOFunction.compose(java.util.function.Supplier):org.apache.commons.io.function.IOSupplier");
    }

    default <V> IOFunction<T, V> andThen(IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after");
        return t -> {
            return after.apply(apply(after));
        };
    }

    default <V> IOFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after");
        return t -> {
            return after.apply(apply(after));
        };
    }

    default IOConsumer<T> andThen(IOConsumer<? super R> after) {
        Objects.requireNonNull(after, "after");
        return t -> {
            after.accept(apply(after));
        };
    }

    default IOConsumer<T> andThen(Consumer<? super R> after) {
        Objects.requireNonNull(after, "after");
        return t -> {
            after.accept(apply(after));
        };
    }

    static <T> IOFunction<T, T> identity() {
        return t -> {
            return t;
        };
    }
}
