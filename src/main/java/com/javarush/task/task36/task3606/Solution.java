package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


/*

Осваиваем ClassLoader и Reflection

*/

public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("secondhiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("firsthiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {
        File[] files = new File(packageName).listFiles((dir, name) -> name.endsWith(".class"));
        if (files == null)
            return;
        for (File file : files
        ) {
            if (!file.isFile())
                continue;
            ClassLoader classLoader = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    try {
                        byte[] bytes = Files.readAllBytes(file.toPath());
                        return defineClass(null, bytes, 0, bytes.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return findClass(name);
                }
            };

            Class<?> clazz = classLoader.loadClass(packageName.replace(File.separator, "\\"));
            hiddenClasses.add(clazz);
        }
    }

    public HiddenClass getHiddenClassObjectByKey(String key) {
        for (Class<?> cl : hiddenClasses
        ) {
            if (cl.getSimpleName().toLowerCase().startsWith(key.toLowerCase())) {
                try {
                    Constructor<?> constructor = cl.getDeclaredConstructor(null);
                    constructor.setAccessible(true);
                    return (HiddenClass) constructor.newInstance();
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
