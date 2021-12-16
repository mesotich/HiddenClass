package com.javarush.task.task33.task3310.strategy;

import java.io.Serializable;
import java.util.Objects;

public class Entry implements Serializable {
    static final long SerialVersionUID = 1L;

    int hash;
    Long key;
    String value;
    Entry next;

    public Entry(int hash, Long key, String value, Entry next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final Long getKey() {
        return key;
    }

    public final String getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + key.hashCode();
        result = prime * result + value.hashCode();
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null && !this.getClass().equals(obj.getClass())) {
            return false;
        }
        Entry entry = (Entry) obj;
        return Objects.equals(key, entry.key)
                && Objects.equals(value, entry.value);
    }

    @Override
    public final String toString() {
        return key + "=" + value;
    }
}
