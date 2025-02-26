package cn.afternode.commons.library;

import cn.afternode.commons.ReflectionError;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClasspathAppender {
    private static Unsafe unsafe;
    private static MethodHandles.Lookup lookup;

    private static void init() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.trySetAccessible();
            unsafe = (Unsafe) unsafeField.get(null);

            Field lookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            lookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(lookupField), unsafe.staticFieldOffset(lookupField));
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    /**
     * Append to Java 9+ AppClassLoader
     * @throws ReflectionError Unable to get ucp field
     * @throws RuntimeException addURL failed
     */
    public static void app(URL url, ClassLoader ldr) {
        Field ucpField;
        try {
            ucpField = ldr.getClass().getDeclaredField("ucp");
        } catch (NoSuchFieldException e) {
            try {
                ucpField = ldr.getClass().getSuperclass().getDeclaredField("ucp");
            } catch (NoSuchFieldException ex) {
                throw new ReflectionError(ldr.getClass(), ex);
            }
        }

        try {
            addURL(ldr, ucpField, url);
        } catch (Throwable t) {
            throw new RuntimeException("addURL failed", t);
        }
    }

    /**
     * Append to current AppClassLoader
     */
    public static void app(URL url) {
        app(url, ClasspathAppender.class.getClassLoader());
    }

    /**
     * Append to URLClassLoader
     */
    public static void ucl(URL url, URLClassLoader ldr) {
        try {
            MethodHandle mh = lookup.findVirtual(URLClassLoader.class, "addURL", MethodType.methodType(void.class, URL.class));
            mh.invoke(ldr, url);
        } catch (Throwable e) {
            throw new ReflectionError(ldr.getClass(), e);
        }
    }

    /**
     * Append to current URLClassLoader
     */
    public static void ucl(URL url) {
        ucl(url, (URLClassLoader) ClasspathAppender.class.getClassLoader());
    }

    private static void addURL(ClassLoader ldr, Field ucpField, URL url) throws Throwable {
        Object ucp = unsafe.getObject(ldr, unsafe.objectFieldOffset(ucpField));

        MethodHandle hnd = lookup.findVirtual(ucp.getClass(), "addURL", MethodType.methodType(void.class, URL.class));
        hnd.invoke(ucp, url);
    }

    static {
        init();
    }
}
