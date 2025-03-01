/*
 * *** Zaki Khan / 272 001 ***
 *
 * This hashMap object represents an over simplification of Java's implementation of HashMap within
 * Java's Collection Framework Library. You are to complete the following methods:
 *  - remove(K)
 *  - replace(K,V)
 *  - replace(K,V,V)
 *
 * In addition to the documentation below, you can read the online Java documentation for HashMap for
 * the expected behavior / return values of each method below. This object follows the same behavior
 * of those methods implemented in this Java library.
 */

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Class HashNode
 *
 * Node object representing a <Key, Value> pair stored in the Hash Map, elements
 * hashed to the same bucket slot will be chained through a singly linked-list.
 */
class HashNode<K, V> {
    K key;
    V value;
    HashNode<K, V> next;

    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.next = null; // Initialize next to null
    }
}

/**
 * A simple implementation of a HashMap that is built to emulate the Map Interface.
 * The <key, values> pairs are stored in a Map, where the key represents a hash
 * bucket slot number and the value represents a node which will form as linked-list
 * for hash collisions on that bucket's slot.
 *
 * The array in this class represents the buckets, and each bucket has a pointer
 * to a node class for the linked-list of <k,v> pairs. The key for this bucket array
 * is generated using a hash function that returns a number from 0 to n-1, where n
 * is the number of buckets (array size).
 */
class myHashMap<K, V> {

    private static final float LOAD_FACTOR_THRESHOLD = 0.7f;
    private static final int INITIAL_CAPACITY = 10;

    private ArrayList<HashNode<K, V>> buckets;
    private int numBuckets;
    private int size;

    public myHashMap() {
        this.numBuckets = INITIAL_CAPACITY;
        this.size = 0;
        buckets = new ArrayList<>(numBuckets);
        for (int i = 0; i < numBuckets; i++) {
            buckets.add(null);
        }
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void clear() {
        size = 0;
        buckets = new ArrayList<>(numBuckets);
        for (int i = 0; i < numBuckets; i++) {
            buckets.add(null);
        }
    }

    private int getBucketIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % numBuckets;
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    public V remove(K key) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);
        HashNode<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev != null) {
                    prev.next = head.next;
                } else {
                    buckets.set(index, head.next);
                }
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }
        return null;
    }

    public boolean remove(K key, V value) {
        V currentValue = get(key);
        if (currentValue == null || !currentValue.equals(value)) {
            return false;
        }
        remove(key);
        return true;
    }

    public V put(K key, V value) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);

        while (head != null) {
            if (head.key.equals(key)) {
                = head.value;
                head.value = value;
                return oldValue;
            }
            head = head.next;
        }

        size++;
        head = buckets.get(index);
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = head;
        buckets.set(index, newNode);

        if ((1.0 * size) / numBuckets > LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        return null;
    }

    private void resize() {
        ArrayList<HashNode<K, V>> temp = buckets;
        numBuckets = 2 * numBuckets;
        size = 0;
        buckets = new ArrayList<>(numBuckets);
        for (int i = 0; i < numBuckets; i++) {
            buckets.add(null);
        }

        for (HashNode<K, V> headNode : temp) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    public V replace(K key, V value) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);

        while (head != null) {
            if (head.key.equals(key)) {
                V oldValue = head.value;
                head.value = value;
                return oldValue;
            }
            head = head.next;
        }
        return null;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = buckets.get(index);

        while (head != null) {
            if (head.key.equals(key) && head.value.equals(oldValue)) {
                head.value = newValue;
                return true;
            }
            head = head.next;
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (HashNode<K, V> headNode : buckets) {
            while (headNode != null) {
                if (headNode.value.equals(value)) {
                    return true;
                }
                headNode = headNode.next;
            }
        }
        return false;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (HashNode<K, V> headNode : buckets) {
            while (headNode != null) {
                entrySet.add(Map.entry(headNode.key, headNode.value));
                headNode = headNode.next;
            }
        }
        return entrySet;
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (HashNode<K, V> headNode : buckets) {
            while (headNode != null) {
                keySet.add(headNode.key);
                headNode = headNode.next;
            }
        }
        return keySet;
    }
} /* end class myHashMap */
