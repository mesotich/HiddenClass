package com.javarush.task.task33.task3310.strategy;

import java.util.Arrays;

public class FileStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000L;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private int size;
    long maxBucketSize;

    int hash(Long k) {
        return (k == null) ? 0 : 31*k.hashCode();
    }

    int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    Entry getEntry(Long key) {
        if (key == null)
            return null;
        int index = indexFor(hash(key), table.length);
        FileBucket fileBucket = table[index];
        if (fileBucket == null)
            return null;
        Entry entry = fileBucket.getEntry();
        while (true) {
            if (entry == null) {
                return null;
            }
            if (entry.getKey().longValue() == key)
                return entry;
            entry = entry.next;
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex) {
        size++;
        createEntry(hash, key, value, bucketIndex);
        long bucketSize = table[bucketIndex].getFileSize();
        if (bucketSize > maxBucketSize)
            maxBucketSize = bucketSize;
        if (maxBucketSize > bucketSizeLimit) {
            resize(table.length * 2);
        }
    }

    void createEntry(int hash, Long key, String value, int bucketIndex) {
        FileBucket fileBucket = table[bucketIndex];
        if (fileBucket == null) {
            fileBucket = new FileBucket();
            table[bucketIndex] = fileBucket;
        }
        Entry mainEntry = fileBucket.getEntry();
        if (mainEntry == null) {
            mainEntry = new Entry(hash, key, value, null);
        } else {
            Entry entry = mainEntry;
            while (entry.next != null) {
                entry = entry.next;
            }
            entry.next = new Entry(hash, key, value, null);
        }
        fileBucket.putEntry(mainEntry);
        table[bucketIndex] = fileBucket;
    }

    void resize(int newCapacity) {
        maxBucketSize = 0L;
        transfer(new FileBucket[newCapacity]);
    }

    void transfer(FileBucket[] newTable) {
        FileBucket[] oldTable = Arrays.copyOf(table, table.length);
        table = newTable;
        for (FileBucket fileBucket : oldTable
        ) {
            Entry entry = fileBucket.getEntry();
            while (entry != null) {
                int newIndex = indexFor(entry.hash, table.length);
                createEntry(entry.hash, entry.key, entry.value, newIndex);
                long bucketSize = table[newIndex].getFileSize();
                if (bucketSize > maxBucketSize)
                    maxBucketSize = bucketSize;
                entry = entry.next;
            }
        }
    }

    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (FileBucket fileBucket : table
        ) {
            if (fileBucket == null)
                continue;
            Entry e = fileBucket.getEntry();
            while (e != null) {
                if (e.getValue().equals(value))
                    return true;
                e = e.next;
            }
        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int bucketIndex = indexFor(hash, table.length);
        addEntry(hash, key, value, bucketIndex);
    }

    @Override
    public Long getKey(String value) {
        for (FileBucket fileBucket : table
        ) {
            if (fileBucket == null)
                continue;
            Entry e = fileBucket.getEntry();
            while (e != null) {
                if (e.getValue().equals(value))
                    return e.getKey();
                e = e.next;
            }
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        return getEntry(key).getValue();
    }
}
