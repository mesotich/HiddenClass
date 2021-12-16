package com.javarush.task.task36.task3606;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        int mapSize = 0;
        for (Entry<K, List<V>> entry : map.entrySet()
        ) {
            mapSize += entry.getValue().size();
        }
        return mapSize;
    }

    @Override
    public V put(K key, V value) {
        V result = null;
        List<V> values;
        if (map.containsKey(key)) {
            values = map.get(key);
            result = values.get(values.size() - 1);
            if (values.size() == repeatCount) {
                values.remove(0);
            }
        } else {
            values = new ArrayList<>();
        }
        values.add(value);
        map.put(key, values);
        return result;
    }

    @Override
    public V remove(Object key) {
        if (!map.containsKey(key)) {
            return null;
        }
        List<V> values = map.get(key);
        V result = values.remove(0);
        if(values.size()==0)
            map.remove(key);
        else map.put((K) key,values);
        return result;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();
        for (Entry<K, List<V>> entry : map.entrySet()
        ) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, List<V>> entry : map.entrySet()
        ) {
            if (entry.getValue().contains(value))
                return true;
        }
        PriorityQueue<Object> pq;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}