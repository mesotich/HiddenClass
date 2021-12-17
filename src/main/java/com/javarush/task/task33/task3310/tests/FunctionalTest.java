package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionalTest {
    @Test
    public void testHashMapStorageStrategy() {
        StorageStrategy storageStrategy = new HashMapStorageStrategy();
        Shortener shortener = new Shortener(storageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testOurHashMapStorageStrategy() {
        StorageStrategy storageStrategy = new OurHashMapStorageStrategy();
        Shortener shortener = new Shortener(storageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testFileStorageStrategy() {
        StorageStrategy storageStrategy = new FileStorageStrategy();
        Shortener shortener = new Shortener(storageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testHashBiMapStorageStrategy() {
        StorageStrategy storageStrategy = new HashBiMapStorageStrategy();
        Shortener shortener = new Shortener(storageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testDualHashBidiMapStorageStrategy() {
        StorageStrategy storageStrategy = new DualHashBidiMapStorageStrategy();
        Shortener shortener = new Shortener(storageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testOurHashBiMapStorageStrategy() {
        StorageStrategy storageStrategy = new OurHashBiMapStorageStrategy();
        Shortener shortener = new Shortener(storageStrategy);
        testStorage(shortener);
    }

    public void testStorage(Shortener shortener) {
        String s1, s2, s3, testS1, testS2, testS3;
        long id1, id2, id3;
        s1 = s3 = Helper.generateRandomString();
        s2 = Helper.generateRandomString();
        id1 = shortener.getId(s1);
        id2 = shortener.getId(s2);
        id3 = shortener.getId(s3);
        Assert.assertNotEquals(id2, id1);
        Assert.assertNotEquals(id2, id3);
        Assert.assertEquals(id1, id3);
        testS1 = shortener.getString(id1);
        testS2 = shortener.getString(id2);
        testS3 = shortener.getString(id3);
        Assert.assertEquals(s1, testS1);
        Assert.assertEquals(s2, testS2);
        Assert.assertEquals(s3, testS3);
    }
}