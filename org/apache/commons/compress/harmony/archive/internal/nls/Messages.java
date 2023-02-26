package org.apache.commons.compress.harmony.archive.internal.nls;

import ch.qos.logback.core.CoreConstants;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/archive/internal/nls/Messages.class */
public class Messages {
    private static ResourceBundle bundle;

    static {
        bundle = null;
        try {
            bundle = setLocale(Locale.getDefault(), "org.apache.commons.compress.harmony.archive.internal.nls.messages");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getString(String msg) {
        if (bundle == null) {
            return msg;
        }
        try {
            return bundle.getString(msg);
        } catch (MissingResourceException e) {
            return "Missing message: " + msg;
        }
    }

    public static String getString(String msg, Object arg) {
        return getString(msg, new Object[]{arg});
    }

    public static String getString(String msg, int arg) {
        return getString(msg, new Object[]{Integer.toString(arg)});
    }

    public static String getString(String msg, char arg) {
        return getString(msg, new Object[]{String.valueOf(arg)});
    }

    public static String getString(String msg, Object arg1, Object arg2) {
        return getString(msg, new Object[]{arg1, arg2});
    }

    public static String getString(String msg, Object[] args) {
        String format = msg;
        if (bundle != null) {
            try {
                format = bundle.getString(msg);
            } catch (MissingResourceException e) {
            }
        }
        return format(format, args);
    }

    public static String format(String format, Object[] args) {
        int i;
        StringBuilder answer = new StringBuilder(format.length() + (args.length * 20));
        String[] argStrings = new String[args.length];
        Arrays.setAll(argStrings, i2 -> {
            return Objects.toString(args[i2], "<null>");
        });
        int lastI = 0;
        int indexOf = format.indexOf(CoreConstants.CURLY_LEFT, 0);
        while (true) {
            int i3 = indexOf;
            if (i3 < 0) {
                break;
            }
            if (i3 != 0 && format.charAt(i3 - 1) == '\\') {
                if (i3 != 1) {
                    answer.append(format.substring(lastI, i3 - 1));
                }
                answer.append('{');
                i = i3 + 1;
            } else if (i3 > format.length() - 3) {
                answer.append(format.substring(lastI));
                i = format.length();
            } else {
                int argnum = (byte) Character.digit(format.charAt(i3 + 1), 10);
                if (argnum < 0 || format.charAt(i3 + 2) != '}') {
                    answer.append(format.substring(lastI, i3 + 1));
                    i = i3 + 1;
                } else {
                    answer.append(format.substring(lastI, i3));
                    if (argnum >= argStrings.length) {
                        answer.append("<missing argument>");
                    } else {
                        answer.append(argStrings[argnum]);
                    }
                    i = i3 + 3;
                }
            }
            lastI = i;
            indexOf = format.indexOf(CoreConstants.CURLY_LEFT, lastI);
        }
        if (lastI < format.length()) {
            answer.append(format.substring(lastI));
        }
        return answer.toString();
    }

    public static ResourceBundle setLocale(Locale locale, String resource) {
        try {
            return (ResourceBundle) AccessController.doPrivileged(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0010: RETURN  
                  (wrap: java.util.ResourceBundle : 0x000d: CHECK_CAST (r0v5 java.util.ResourceBundle A[REMOVE]) = (java.util.ResourceBundle) (wrap: java.lang.Object : 0x000a: INVOKE  (r0v4 java.lang.Object A[REMOVE]) = 
                  (wrap: java.security.PrivilegedAction : 0x0005: INVOKE_CUSTOM (r0v3 java.security.PrivilegedAction A[REMOVE]) = 
                  (r5v0 'resource' java.lang.String A[D('resource' java.lang.String), DONT_INLINE])
                  (r4v0 'locale' java.util.Locale A[D('locale' java.util.Locale), DONT_INLINE])
                  (null java.lang.ClassLoader)
                
                 handle type: INVOKE_STATIC
                 lambda: java.security.PrivilegedAction.run():java.lang.Object
                 call insn: ?: INVOKE  (r0 I:java.lang.String), (r1 I:java.util.Locale), (r2 I:java.lang.ClassLoader) type: STATIC call: org.apache.commons.compress.harmony.archive.internal.nls.Messages.lambda$setLocale$1(java.lang.String, java.util.Locale, java.lang.ClassLoader):java.lang.Object)
                 type: STATIC call: java.security.AccessController.doPrivileged(java.security.PrivilegedAction):java.lang.Object))
                 in method: org.apache.commons.compress.harmony.archive.internal.nls.Messages.setLocale(java.util.Locale, java.lang.String):java.util.ResourceBundle, file: TLauncher-2.876.jar:org/apache/commons/compress/harmony/archive/internal/nls/Messages.class
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                Caused by: java.lang.ClassCastException: class jadx.core.dex.instructions.args.LiteralArg cannot be cast to class jadx.core.dex.instructions.args.RegisterArg (jadx.core.dex.instructions.args.LiteralArg and jadx.core.dex.instructions.args.RegisterArg are in unnamed module of loader 'app')
                	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:958)
                	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:322)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:345)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                	... 21 more
                */
            /*
                r0 = 0
                r6 = r0
                r0 = r5
                r1 = r4
                r2 = r6
                java.util.ResourceBundle r0 = () -> { // java.security.PrivilegedAction.run():java.lang.Object
                    return lambda$setLocale$1(r0, r1, r2);
                }     // Catch: java.util.MissingResourceException -> L11
                java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch: java.util.MissingResourceException -> L11
                java.util.ResourceBundle r0 = (java.util.ResourceBundle) r0     // Catch: java.util.MissingResourceException -> L11
                return r0
            L11:
                r6 = move-exception
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.harmony.archive.internal.nls.Messages.setLocale(java.util.Locale, java.lang.String):java.util.ResourceBundle");
        }
    }
