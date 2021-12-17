package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.Solution;
import com.javarush.task.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SpeedTest {
    @Test
    public void testHashMapStorage() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());
        Set<Long> origIds = LongStream.rangeClosed(1, 10000L)
                .boxed().
                collect(Collectors.toSet());
        Set<String> origStrings = LongStream.rangeClosed(1, 10000L)
                .mapToObj(e -> Helper.generateRandomString())
                .collect(Collectors.toSet());
        long timeHashMap = getTimeToGetIds(shortener1, origStrings, new HashSet<>());
        long timeHashBiMap = getTimeToGetIds(shortener2, origStrings, new HashSet<>());
        Assert.assertTrue(timeHashMap > timeHashBiMap);
        timeHashMap = getTimeToGetStrings(shortener1, origIds, new HashSet<>());
        timeHashBiMap = getTimeToGetStrings(shortener2, origIds, new HashSet<>());
        Assert.assertEquals(timeHashMap, timeHashBiMap, 30f);
    }

    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        long start = new Date().getTime();
        ids = Solution.getIds(shortener, strings);
        long end = new Date().getTime();
        return end - start;
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        long start = new Date().getTime();
        strings = Solution.getStrings(shortener, ids);
        long end = new Date().getTime();
        return end - start;
    }
}
