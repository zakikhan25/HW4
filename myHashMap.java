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

    private static final float DEFAULT_LOAD_FACTOR = 0.7f;
    private static final int INITIAL_NUM_BUCKETS = 10;

    ArrayList<HashNode<K, V>> bucket;
    int numBuckets;
    int size;

    public myHashMap() {
        this.numBuckets = INITIAL_NUM_BUCKETS;
        this.size = 0;
        bucket = new ArrayList<>(numBuckets);
        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }
    }

    public int Size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void clear() {
        size = 0;
        bucket = new ArrayList<>(numBuckets);
        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }
    }

    private int getBucketIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % numBuckets;
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);
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
        HashNode<K, V> head = bucket.get(index);
        HashNode<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev != null) {
                    prev.next = head.next;
                } else {
                    bucket.set(index, head.next);
                }
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }
        return null;
    }

    public boolean remove(K key, V val) {
        V originalValue = get(key);
        if (originalValue == null || !originalValue.equals(val)) {
            return false;
        }
        remove(key);
        return true;
    }

    public V put(K key, V value) {
        V oldValue = get(key);
        if (oldValue != null) {
            // Directly replace the value without calling put again
            replace(key, value);
            return oldValue;
        }

        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);
        HashNode<K, V> toAdd = new HashNode<>(key, value);
        if (head == null) {
            bucket.set(index, toAdd);
            size++;
        } else {
            while (head != null) {
                if (head.key.equals(key)) {
                    head.value = value;
                    return null;
                }
                head = head.next;
            }
            head = bucket.get(index);
            toAdd.next = head;
            bucket.set(index, toAdd);
            size++;
        }

        if ((1.0 * size) / numBuckets > DEFAULT_LOAD_FACTOR) {
            ArrayList<HashNode<K, V>> tmp = bucket;
            bucket = new ArrayList<>();
            numBuckets = 2 * numBuckets;
            size = 0;

            for (int i = 0; i < numBuckets; i++) {
                bucket.add(null);
            }

            for (HashNode<K, V> headNode : tmp) {
                while (headNode != null) {
                    put(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }

        return null;
    }

    public V replace(K key, V val) {
        if (get(key) == null) {
            return null;
        }
        V oldValue = get(key);
        // Directly update the value
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = val;
                return oldValue;
            }
            head = head.next;
        }
        return null;
    }

    public boolean replace(K key, V oldVal, V newVal) {
        V originalValue = get(key);
        if (originalValue == null || !originalValue.equals(oldVal)) {
            return false;
        }
        replace(key, newVal);
        return true;
    }

    public boolean containsValue(V val) {
        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                if (headNode.value.equals(val)) {
                    return true;
                }
                headNode = headNode.next;
            }
        }
        return false;
    }

    public boolean containsKey(K key) {
        return (get(key) != null);
    }

    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> returnSet = new HashSet<>();
        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                returnSet.add(Map.entry(headNode.key, headNode.value));
                headNode = headNode.next;
            }
        }
        return returnSet;
    }

    public Set<K> keySet() {
        Set<K> returnSet = new HashSet<>();
        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                returnSet.add(headNode.key);
                headNode = headNode.next;
            }
        }
        return returnSet;
    }
} /* end class myHashMap */
