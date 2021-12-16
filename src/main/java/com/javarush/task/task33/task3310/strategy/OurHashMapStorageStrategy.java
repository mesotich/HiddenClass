package com.javarush.task.task33.task3310.strategy;

import java.util.Arrays;

public class OurHashMapStorageStrategy implements StorageStrategy {
    private final static int DEFAULT_INITIAL_CAPACITY = 16;
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;
    private Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private final float loadFactor = DEFAULT_LOAD_FACTOR;

    int hash(Long k) {
        return (k == null) ? 0 : 31 * k.hashCode();
    }

    int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    Entry getEntry(Long key) {
        if (key == null)
            return null;
        int index = indexFor(hash(key), table.length);
        Entry entry = table[index];
        if (entry == null) {
            return null;
        }
        while (entry.key.longValue() != key.longValue()) {
            entry = entry.next;
        }
        return entry;
    }

    void addEntry(int hash, Long key, String value, int bucketIndex) {
        size++;
        if (size > threshold) {
            resize(table.length * 2);
            threshold = (int) (table.length * loadFactor);
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    void createEntry(int hash, Long key, String value, int bucketIndex) {
        Entry mainEntry = table[bucketIndex];
        if (mainEntry == null) {
            mainEntry = new Entry(hash, key, value, null);
        } else {
            Entry entry = mainEntry;
            while (entry.next != null) {
                entry = entry.next;
            }
            entry.next = new Entry(hash, key, value, null);
        }
        table[bucketIndex] = mainEntry;
    }

    void resize(int newCapacity) {
        transfer(new Entry[newCapacity]);
    }

    void transfer(Entry[] newTable) {
        Entry[] oldTable = Arrays.copyOf(table, table.length);
        table = newTable;
        for (Entry entry : oldTable
        ) {
            while (entry != null) {
                int newIndex = indexFor(entry.hash, table.length);
                createEntry(entry.hash, entry.key, entry.value, newIndex);
                entry = entry.next;
            }
        }
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (Entry entry : table
        ) {
            Entry e = entry;
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
        for (Entry entry : table
        ) {
            Entry e = entry;
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
