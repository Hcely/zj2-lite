package org.zj2.lite.common.util;


import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.function.ByteConsumer;
import org.zj2.lite.common.function.IdxKeyValueConsumer;
import org.zj2.lite.common.function.IntBeanConsumer;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 集合工具类.
 *
 * @author peijie.ye
 * @date 2022/3/27
 * @since 1.0.1
 */
public class CollUtil {

    public static <T> List<T> page(List<T> list, Integer pageNo, Integer pageSize) {
        if (pageSize == null || pageSize < 1) { return list == null ? new ArrayList<>() : list; }
        pageNo = pageNo == null || pageNo < 1 ? 1 : pageNo;
        int startIdx = (pageNo - 1) * pageSize;
        int endIdx = startIdx + pageSize;
        return subList(list, startIdx, endIdx);
    }

    public static <T> List<T> subList(List<T> list, int startIdx, int endIdx) {
        if (isEmpty(list)) { return new ArrayList<>(); }
        endIdx = Math.min(endIdx, size(list));
        return startIdx < endIdx ? list.subList(startIdx, endIdx) : new ArrayList<>();
    }

    public static <T> Iterator<T> iterator(Collection<T> coll) {
        return isEmpty(coll) ? Collections.emptyIterator() : coll.iterator();
    }

    public static <K, V> Iterator<Map.Entry<K, V>> iterator(Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyIterator() : map.entrySet().iterator();
    }

    public static <T> List<T> of(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T> Set<T> of(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <T> Collection<T> of(Collection<T> coll) {
        return coll == null ? Collections.emptyList() : coll;
    }

    public static <K, V> Map<K, V> of(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    public static <K, V> Collection<V> values(Map<K, V> map) {
        return map == null ? Collections.emptySet() : map.values();
    }

    public static <K, V> Set<K> keys(Map<K, V> map) {
        return map == null ? Collections.emptySet() : map.keySet();
    }

    public static <K, V> Set<Map.Entry<K, V>> entries(Map<K, V> map) {
        return map == null ? Collections.emptySet() : map.entrySet();
    }

    public static <T> void foreach(Iterator<T> iterator, Consumer<T> action) {
        if (iterator != null && action != null) {
            while (iterator.hasNext()) {
                T e = iterator.next();
                if (e != null) { action.accept(e); }
            }
        }
    }

    public static <T> void foreach(Iterator<T> iterator, IntBeanConsumer<T> action) {
        if (iterator != null && action != null) {
            int idx = 0;
            while (iterator.hasNext()) {
                T e = iterator.next();
                if (e != null) { action.accept(idx, e); }
                ++idx;
            }
        }
    }

    public static <T> void foreach(Enumeration<T> enumeration, Consumer<T> action) {
        if (enumeration != null && action != null) {
            while (enumeration.hasMoreElements()) {
                T e = enumeration.nextElement();
                if (e != null) { action.accept(e); }
            }
        }
    }

    public static <T> void foreach(Enumeration<T> enumeration, IntBeanConsumer<T> action) {
        if (enumeration != null && action != null) {
            int idx = 0;
            while (enumeration.hasMoreElements()) {
                T e = enumeration.nextElement();
                if (e != null) { action.accept(idx, e); }
                ++idx;
            }
        }
    }

    public static <T> void foreach(Collection<T> coll, Consumer<T> action) {
        if (isNotEmpty(coll) && action != null) {
            for (T e : coll) {
                if (e != null) { action.accept(e); }
            }
        }
    }

    public static <T> void foreach(Collection<T> coll, IntBeanConsumer<T> action) {
        if (isNotEmpty(coll) && action != null) {
            int idx = 0;
            for (T e : coll) {
                if (e != null) { action.accept(idx, e); }
                ++idx;
            }
        }
    }

    public static <K, V> void foreach(Map<K, V> map, BiConsumer<K, V> consumer) {
        if (consumer != null && isNotEmpty(map)) {
            for (Map.Entry<K, V> e : map.entrySet()) {
                V value = e.getValue();
                if (value != null) {
                    consumer.accept(e.getKey(), value);
                }
            }
        }
    }

    public static <K, V> void foreach(Map<K, V> map, IdxKeyValueConsumer<K, V> consumer) {
        if (consumer != null && isNotEmpty(map)) {
            int idx = 0;
            for (Map.Entry<K, V> e : map.entrySet()) {
                V value = e.getValue();
                if (value != null) { consumer.accept(idx, e.getKey(), value); }
                ++idx;
            }
        }
    }

    public static void foreach(byte[] array, ByteConsumer action) {
        if (array != null && action != null) {
            for (byte e : array) { action.accept(e); }
        }
    }

    public static void foreach(int[] array, IntConsumer action) {
        if (array != null && action != null) {
            for (int e : array) { action.accept(e); }
        }
    }

    public static void foreach(long[] array, LongConsumer action) {
        if (array != null && action != null) {
            for (long e : array) { action.accept(e); }
        }
    }

    public static void foreach(double[] array, DoubleConsumer action) {
        if (array != null && action != null) {
            for (double e : array) { action.accept(e); }
        }
    }

    public static <T> void foreach(T[] array, Consumer<T> action) {
        if (array != null && action != null) {
            for (T e : array) { if (e != null) { action.accept(e); } }
        }
    }

    public static <T> void foreach(T[] array, IntBeanConsumer<T> action) {
        if (array != null && action != null) {
            for (int i = 0, len = array.length; i < len; ++i) {
                T e = array[i];
                if (e != null) { action.accept(i, e); }
            }
        }
    }

    public static <T> void descForeach(Collection<T> coll, Consumer<T> action) {//NOSONAR
        if (action == null) { return; }
        int size = size(coll);
        if (size == 0) { return; }
        if (size == 1) {
            foreach(coll, action);
        } else if (coll instanceof Deque) {
            Deque<T> list = (Deque<T>) coll;
            for (Iterator<T> it = list.descendingIterator(); it.hasNext(); ) {
                T e = it.next();
                if (e != null) { action.accept(e); }
            }
        } else {
            List<T> list = coll instanceof List ? ((List<T>) coll) : new ArrayList<>(coll);
            ListIterator<T> it = list.listIterator(size);
            while (it.hasPrevious()) {
                T e = it.previous();
                if (e != null) { action.accept(e); }
            }
        }
    }

    public static <T> void descForeach(Collection<T> coll, IntBeanConsumer<T> action) {//NOSONAR
        if (action == null) { return; }
        int size = size(coll);
        if (size == 0) { return; }
        if (size == 1) {
            foreach(coll, action);
        } else if (coll instanceof Deque) {
            Deque<T> list = (Deque<T>) coll;
            int idx = 0;
            for (Iterator<T> it = list.descendingIterator(); it.hasNext(); ) {
                T e = it.next();
                if (e != null) { action.accept(idx, e); }
                ++idx;
            }
        } else {
            List<T> list = coll instanceof List ? ((List<T>) coll) : new ArrayList<>(coll);
            ListIterator<T> it = list.listIterator(size);
            int idx = 0;
            while (it.hasPrevious()) {
                T e = it.previous();
                if (e != null) { action.accept(idx, e); }
                ++idx;
            }
        }
    }

    public static <T> Iterable<T> descIterable(Collection<T> coll) {
        int size = size(coll);
        if (size == 0) {
            return Collections.emptyList();
        } else if (size == 1) {
            return coll;
        } else {
            return () -> descIterator(coll);
        }
    }

    public static <T> Iterator<T> descIterator(Collection<T> coll) {
        int size = size(coll);
        if (size == 0) {
            return Collections.emptyIterator();
        } else if (size == 1) {
            return coll.iterator();
        } else if (coll instanceof Deque) {
            return ((Deque<T>) coll).descendingIterator();
        } else {
            List<T> list = coll instanceof List ? ((List<T>) coll) : new ArrayList<>(coll);
            return new DescendingIterator<>(list.listIterator(size));
        }
    }

    public static <T> Foreacher<T, T> foreach(T object) {
        return new Foreacher<>(object);
    }

    @SuppressWarnings("all")
    public static <T> Foreacher<T, T> foreach(Collection<T> coll) {
        return new Foreacher(coll).next(e -> coll);
    }

    public static <T> List<T> sort(List<T> coll, Comparator<T> comparator) {
        if (coll == null) {
            return new ArrayList<>();
        }
        if (coll.size() > 1) {
            coll.sort(comparator);
        }
        return coll;
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    public static <T> Iterator<T> emptyIterator() {
        return Collections.emptyIterator();
    }

    public static <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    }

    public static <T> List<T> singletonList(T value) {
        return Collections.singletonList(value);
    }

    public static <T> Set<T> singletonSet(T value) {
        return Collections.singleton(value);
    }

    public static <K, V> Map<K, V> singletonMap(K key, V value) {
        return Collections.singletonMap(key, value);
    }

    public static <T> Stream<T> stream(Collection<T> coll) {
        return (coll == null ? Collections.<T>emptyList() : coll).stream();
    }

    public static <K, V> List<K> toDistinctList(Collection<V> coll, Function<V, K> func) {
        if (isEmpty(coll)) { return new ArrayList<>(); }
        Set<K> result = transToTarget(coll, new LinkedHashSet<>(coll.size()), func, true);
        return new ArrayList<>(result);
    }

    public static <K, V> List<K> toList(V[] array, Function<V, K> func) {
        return toList(array, func, false);
    }

    public static <K, V> List<K> toList(V[] array, Function<V, K> func, boolean ignoreEmpty) {
        if (isEmpty(array)) { return new ArrayList<>(); }
        return transToTarget(array, new ArrayList<>(array.length), func, ignoreEmpty);
    }

    public static <K, V> Set<K> toSet(V[] array, Function<V, K> func) {
        return toSet(array, func, false);
    }

    public static <K, V> Set<K> toSet(V[] array, Function<V, K> func, boolean ignoreEmpty) {
        if (isEmpty(array)) { return new LinkedHashSet<>(); }
        return transToTarget(array, new LinkedHashSet<>(array.length), func, ignoreEmpty);
    }

    public static <K, V> List<K> toList(Collection<V> coll, Function<V, K> func) {
        return toList(coll, func, false);
    }

    public static <K, V> List<K> toList(Collection<V> coll, Function<V, K> func, boolean ignoreEmpty) {
        if (isEmpty(coll)) { return new ArrayList<>(); }
        return transToTarget(coll, new ArrayList<>(coll.size()), func, ignoreEmpty);
    }

    public static <K, V> Set<K> toSet(Collection<V> coll, Function<V, K> func) {
        return toSet(coll, func, false);
    }

    public static <K, V> Set<K> toSet(Collection<V> coll, Function<V, K> func, boolean ignoreEmpty) {
        if (isEmpty(coll)) { return new LinkedHashSet<>(); }
        return transToTarget(coll, new LinkedHashSet<>(coll.size()), func, ignoreEmpty);
    }

    private static <K, V, C extends Collection<K>> C transToTarget(V[] src, C dst, Function<V, K> func,
            boolean ignoreEmpty) {
        for (V e : src) { addToTarget(e, dst, func, ignoreEmpty); }
        return dst;
    }

    private static <K, V, C extends Collection<K>> C transToTarget(Collection<V> src, C dst, Function<V, K> func,
            boolean ignoreEmpty) {
        for (V e : src) { addToTarget(e, dst, func, ignoreEmpty); }
        return dst;
    }

    private static <K, V, C extends Collection<K>> void addToTarget(V e, C dst, Function<V, K> func,
            boolean ignoreEmpty) {
        if (e == null) { return; }
        K value = func.apply(e);
        if (ignoreEmpty && (value == null || (value instanceof String && StringUtils.isEmpty(value.toString())))) {
            return;
        }
        dst.add(value);
    }

    public static <K, V> Map<K, List<V>> groupingBy(Collection<V> coll, Function<V, K> func) {
        Map<K, List<V>> result = new LinkedHashMap<>();
        if (isNotEmpty(coll)) {
            for (V e : coll) {
                if (e != null) {
                    result.computeIfAbsent(func.apply(e), k -> new ArrayList<>()).add(e);
                }
            }
        }
        return result;
    }

    public static <K, R> List<R> mapReduceAsList(Collection<R> values, Function<R, K> mapFunc,
            BiConsumer<R, R> reduceFunc) {
        return mapReduceAsList(values, mapFunc, Function.identity(), reduceFunc);
    }

    public static <K, R> Map<K, R> mapReduce(Collection<R> values, Function<R, K> mapFunc,
            BiConsumer<R, R> reduceFunc) {
        return mapReduce(values, mapFunc, Function.identity(), reduceFunc);
    }

    public static <K, T, R> List<T> mapReduceAsList(Collection<R> values, Function<R, K> mapFunc,
            Function<R, T> convertFunc, BiConsumer<T, R> reduceFunc) {
        if (isEmpty(values)) { return new ArrayList<>(); }
        Map<K, T> result = mapReduce(values, mapFunc, convertFunc, reduceFunc);
        return new ArrayList<>(result.values());
    }

    public static <K, T, R> Map<K, T> mapReduce(Collection<R> values, Function<R, K> mapFunc,
            Function<R, T> convertFunc, BiConsumer<T, R> reduceFunc) {
        if (isEmpty(values)) { return new LinkedHashMap<>(); }
        Map<K, T> result = new LinkedHashMap<>(values.size());
        for (R v : values) {
            if (v == null) { continue; }
            final K key = mapFunc.apply(v);
            T value = result.get(key);
            if (value == null) {
                value = convertFunc.apply(v);
                if (value != null) {
                    result.put(key, value);
                    if (value != v) { reduceFunc.accept(value, v); }
                }
            } else {
                reduceFunc.accept(value, v);
            }
        }
        return result;
    }


    public static <V> V[] defaultIfEmpty(V[] array, V[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    public static <V, C extends Collection<V>> C defaultIfEmpty(C coll, C defaultColl) {
        return isEmpty(coll) ? defaultColl : coll;
    }

    public static <K, V> Map<K, V> toMap(Collection<V> coll, Function<V, K> keyFunc) {
        return toMap(coll, keyFunc, Function.identity());
    }

    public static <K, V, R> Map<K, R> toMap(Collection<V> coll, Function<V, K> keyFunc, Function<V, R> valueFunc) {
        Map<K, R> result = new HashMap<>();
        if (isNotEmpty(coll) && keyFunc != null && valueFunc != null) {
            for (V v : coll) {
                if (v != null) { result.put(keyFunc.apply(v), valueFunc.apply(v)); }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> K[] toArray(Collection<V> coll, Function<V, K> func, Class<K> type) {
        int len = coll == null ? 0 : coll.size();
        K[] array = (K[]) Array.newInstance(type, len);
        if (coll != null) {
            int idx = 0;
            for (V e : coll) {
                array[idx] = e == null ? null : func.apply(e);
                ++idx;
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> coll, Class<T> type) {
        int len = coll == null ? 0 : coll.size();
        T[] array = (T[]) Array.newInstance(type, len);
        if (coll != null) {
            coll.toArray(array);
        }
        return array;
    }

    public static <V> BigDecimal sum(Collection<V> coll, Function<V, BigDecimal> func) {
        if (isEmpty(coll)) { return BigDecimal.ZERO; }
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal value;
        for (V e : coll) {
            if (e != null && (value = func.apply(e)) != null) {
                result = NumUtil.add(result, value);
            }
        }
        return result;
    }

    public static <V> int sumInt(Collection<V> coll, Function<V, Integer> func) {//NOSONAR
        if (isEmpty(coll)) { return 0; }
        int result = 0;
        Integer value;
        for (V e : coll) {
            if (e != null && (value = func.apply(e)) != null) {
                result += value;
            }
        }
        return result;
    }

    public static <T> List<T> addAll(List<T> list, Collection<T> values) {
        if (list != null) {
            if (values != null) {
                list.addAll(values);
            }
            return list;
        } else if (values != null) {
            return values instanceof List ? (List<T>) values : new ArrayList<>(values);
        }
        return new ArrayList<>();
    }

    public static <T> Set<T> addAll(Set<T> set, Collection<T> values) {
        if (set != null) {
            if (values != null) {
                set.addAll(values);
            }
            return set;
        } else if (values != null) {
            return values instanceof Set ? (Set<T>) values : new LinkedHashSet<>(values);
        }
        return new LinkedHashSet<>();
    }

    public static boolean isAllNull(Object... array) {
        if (array == null || array.length == 0) {
            return false;
        }
        for (Object e : array) {
            if (e != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNull(Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return false;
        }
        for (Object e : coll) {
            if (e != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyNull(Object... array) {
        return !isNoneNull(array);
    }

    public static boolean isAnyNull(Collection<?> coll) {
        return !isNoneNull(coll);
    }

    public static boolean isNoneNull(Object... array) {
        if (array == null || array.length == 0) {
            return true;
        }
        for (Object e : array) {
            if (e == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNull(Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return true;
        }
        for (Object e : coll) {
            if (e == null) {
                return false;
            }
        }
        return true;
    }

    public static int size(Object[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(byte[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(boolean[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(char[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(short[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(float[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(int[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(long[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(double[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(Collection<?> coll) {
        return coll == null ? 0 : coll.size();
    }

    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> List<T> filter(Collection<T> coll, Predicate<T> filter) {
        if (isEmpty(coll)) { return new ArrayList<>(); }
        List<T> result = new ArrayList<>(coll.size());
        for (T e : coll) { if (e != null && filter.test(e)) { result.add(e); } }
        return result;
    }

    public static <T> boolean anyMatch(T[] array, Predicate<T> predicate) {
        if (isEmpty(array)) { return false; }
        for (T e : array) { if (e != null && predicate.test(e)) { return true; } }
        return false;
    }

    public static <T> boolean anyMatch(Collection<T> coll, Predicate<T> predicate) {
        if (isEmpty(coll)) { return false; }
        for (T e : coll) { if (e != null && predicate.test(e)) { return true; } }
        return false;
    }

    public static <T> boolean allMatch(Collection<T> coll, Predicate<T> predicate) {
        if (isEmpty(coll)) { return false; }
        for (T e : coll) { if (e == null || !predicate.test(e)) { return false; } }
        return true;
    }

    public static <T> boolean allMatch(T[] array, Predicate<T> predicate) {
        if (isEmpty(array)) { return false; }
        for (T e : array) { if (e == null || !predicate.test(e)) { return false; } }
        return true;
    }

    public static <T> T get(Collection<T> coll, int idx) {
        if (coll == null || idx < 0 || idx >= (coll).size()) { return null; }
        if (coll instanceof List) { return ((List<T>) coll).get(idx); }
        for (T e : coll) {
            if (idx < 1) { return e; }
            --idx;
        }
        return null;
    }

    public static <T> T findFirst(Collection<T> coll, Predicate<T> predicate) {
        if (isEmpty(coll)) { return null; }
        for (T e : coll) { if (e != null && predicate.test(e)) { return e; } }
        return null;
    }

    public static <T> T findFirst(T[] array, Predicate<T> predicate) {
        if (isEmpty(array)) { return null; }
        for (T e : array) { if (e != null && predicate.test(e)) { return e; } }
        return null;
    }

    public static <T> T getFirst(Collection<T> coll) {
        if (isEmpty(coll)) { return null; }
        if (coll instanceof LinkedList) { return ((LinkedList<T>) coll).peekFirst(); }
        return get(coll, 0);
    }

    public static <T> T getFirst(T[] array) {
        return isEmpty(array) ? null : array[0];
    }

    public static <T> T getLast(Collection<T> coll) {
        if (isEmpty(coll)) { return null; }
        if (coll instanceof LinkedList) { return ((LinkedList<T>) coll).peekLast(); }
        return get(coll, size(coll) - 1);
    }

    public static <K, V> V getFirst(Map<K, V> map) {
        if (isEmpty(map)) { return null; }
        return map.values().iterator().next();
    }

    public static <K, V> V getLast(Map<K, V> map) {
        if (isEmpty(map)) { return null; }
        V result = null;
        for (V e : map.values()) { result = e; }
        return result;
    }

    public static <K, T> T get(Map<K, T> map, K key) {
        return get(map, key, null);
    }

    public static <K, T> T get(Map<K, T> map, K key, T def) {
        if (isEmpty(map)) { return def; }
        return map.getOrDefault(key, def);
    }

    public static <V> boolean contains(Object[] array, V value) {
        if (isNotEmpty(array)) {
            for (Object v : array) { if (Objects.equals(v, value)) { return true; } }
        }
        return false;
    }

    public static <V> boolean contains(Collection<V> coll, V value) {
        return isNotEmpty(coll) && coll.contains(value);
    }

    public static <K, T> boolean containsKey(Map<K, T> map, K key) {
        return isNotEmpty(map) && map.containsKey(key);
    }

    @SafeVarargs
    public static <T> List<T> newList(T... arrays) {
        if (isEmpty(arrays)) { return new ArrayList<>(); }
        List<T> result = new ArrayList<>(arrays.length);
        Collections.addAll(result, arrays);
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> newSet(T... arrays) {
        Set<T> result = new LinkedHashSet<>();
        if (isNotEmpty(arrays)) { Collections.addAll(result, arrays); }
        return result;
    }

    @SuppressWarnings("all")
    public static class Foreacher<P, T> {
        private static final Predicate ALLOW_FILTER = o -> true;
        private final Object value;
        private List<Function<Object, Collection>> getters;
        private List<Predicate<Object>> filters;
        private Consumer consumer;
        private BiConsumer biConsumer;

        protected Foreacher(T value) {
            this.value = value;
        }

        public <E> Foreacher<T, E> next(Function<T, ? extends Collection<E>> getter) {
            return next(getter, ALLOW_FILTER);
        }

        public <E> Foreacher<T, E> next(Function<T, ? extends Collection<E>> getter, Predicate<E> filter) {
            Predicate tmp = filter == null ? ALLOW_FILTER : filter;
            if (getters == null || filters == null) {
                this.getters = new ArrayList<>(2);
                this.filters = new ArrayList<>(2);
            }
            this.getters.add((Function) getter);
            this.filters.add(tmp);
            return (Foreacher<T, E>) this;
        }

        public void handle(Consumer<T> consumer) {
            this.consumer = consumer;
            execute(ALLOW_FILTER, null, value, 0);
        }

        public void handle(BiConsumer<P, T> consumer) {
            this.biConsumer = consumer;
            execute(ALLOW_FILTER, null, value, 0);
        }

        public List<T> toList() {
            return toList(Function.identity(), false);
        }

        public Set<T> toSet() {
            return toSet(Function.identity(), false);
        }

        public List<T> toList(boolean ignoreEmpty) {
            return toList(Function.identity(), ignoreEmpty);
        }

        public Set<T> toSet(boolean ignoreEmpty) {
            return toSet(Function.identity(), ignoreEmpty);
        }

        public <E> List<E> toList(Function<T, E> getter) {
            return toList(getter, false);
        }

        public <E> Set<E> toSet(Function<T, E> getter) {
            return toSet(getter, false);
        }

        public <E> List<E> toList(Function<T, E> getter, boolean ignoreEmpty) {
            final List<E> result = new ArrayList<>();
            toColl(getter, result, ignoreEmpty);
            return result;
        }

        public <E> Set<E> toSet(Function<T, E> getter, boolean ignoreEmpty) {
            final Set<E> result = new LinkedHashSet<>();
            toColl(getter, result, ignoreEmpty);
            return result;
        }

        private <E> void toColl(Function<T, E> getter, Collection<E> result, boolean ignoreEmpty) {
            handle(t -> {
                E value = getter.apply(t);
                if (ignoreEmpty && (value == null || (value.getClass() == String.class
                        && ((String) value).isEmpty()))) {
                    return;
                }
                result.add(value);
            });
        }

        private void execute(Predicate<Object> filter, Object parent, Object value, int idx) {
            if (value == null || !filter.test(value)) {
                return;
            }
            if (getters == null || getters.size() == idx) {
                if (consumer != null) {
                    consumer.accept(value);
                } else if (biConsumer != null) {
                    biConsumer.accept(parent, value);
                }
            } else {
                Function<Object, Collection> getter = getters.get(idx);
                Predicate<Object> nextFilter = filters.get(idx);
                Collection coll = getter.apply(value);
                if (coll == null || coll.isEmpty()) {
                    return;
                }
                for (Object e : coll) {
                    execute(nextFilter, value, e, idx + 1);
                }
            }
        }
    }

    private static class DescendingIterator<T> implements Iterator<T> {

        private final ListIterator<T> it;

        private DescendingIterator(ListIterator<T> it) {
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasPrevious();
        }

        @Override
        public T next() throws NoSuchElementException {
            if (!hasNext()) { throw new NoSuchElementException(); }
            return it.previous();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }
}
