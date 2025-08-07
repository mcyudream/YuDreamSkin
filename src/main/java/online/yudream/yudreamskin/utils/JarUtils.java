package online.yudream.yudreamskin.utils;

import online.yudream.yudreamskin.YuDreamSkinApplication;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

@Component
public class JarUtils {

    public static void loadJar(File jar) throws Exception {
        URL url = jar.toURI().toURL();
        ClassLoader sys = YuDreamSkinApplication.class.getClassLoader();
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        add.setAccessible(true);
        add.invoke(sys, url);
    }

}
