package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.*;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Solution {
    public static void main(String[] args) {
        //testStrategy(new HashMapStorageStrategy(), 10000L);
        testStrategy(new OurHashBiMapStorageStrategy(), 1_000_000L);
        testStrategy(new HashBiMapStorageStrategy(), 1_000_000L);
        //testStrategy(new OurHashMapStorageStrategy(), 10000L);
        testStrategy(new DualHashBidiMapStorageStrategy(), 1_000_000L);
        //testStrategy(new FileStorageStrategy(), 1000L);
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        return strings.stream()
                .map(shortener::getId)
                .collect(Collectors.toSet());
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        return keys.stream()
                .map(shortener::getString)
                .collect(Collectors.toSet());
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Class<?> storageStrategyClass = strategy.getClass();
        String implClassName = storageStrategyClass.asSubclass(storageStrategyClass).getSimpleName();
        Set<String> strings = LongStream.range(0, elementsNumber)
                .mapToObj(x -> Helper.generateRandomString())
                .collect(Collectors.toSet());
        Shortener shortener = new Shortener(strategy);
        Date startKeys = new Date();
        Set<Long> keys = getIds(shortener, strings);
        Date endKeys = new Date();
        Date startStringsValue = new Date();
        Set<String> stringsValue = getStrings(shortener, keys);
        Date endStringsValue = new Date();
        Helper.printMessage(implClassName);
        Helper.printMessage("Время получения ключей по строкам = " + String.valueOf(endKeys.getTime() - startKeys.getTime()) + " ms");
        Helper.printMessage("Время получения строк по ключам = " + String.valueOf(endStringsValue.getTime() - startStringsValue.getTime()) + " ms");
        if (strings.equals(stringsValue))
            Helper.printMessage("Тест пройден.");
        else
            Helper.printMessage("Тест не пройден.");
    }
}
