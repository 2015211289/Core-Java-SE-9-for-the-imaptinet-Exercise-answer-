import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntFunction;

public class sec7 {

    //exercise1
    public Set<Integer> exercise1(int n) {
        Set<Integer> set = new HashSet<>();
        for (int i = 2; i <= n; i++) {
            set.add(i);
        }
        int s = 2;
        while (s * s <= n) {
            for (int i = s; s * i <= n; i++) {
                set.remove(s * i);
            }
            for (int i = s + 1; i <= n; i++) {
                if (set.contains(i)) {
                    s = i;
                    break;
                }
            }
        }
        return set;
    }

    public BitSet exercise1_(int n) {
        BitSet bitSet = new BitSet(n + 1);
        for (int i = 2; i <= n; i++) {
            bitSet.set(i);
        }
        int s = 2;
        while (s * s <= n) {
            for (int i = s; s * i <= n; i++) {
                bitSet.clear(s * i);
            }
            for (int i = s + 1; i <= n; i++) {
                if (bitSet.get(i)) {
                    s = i;
                    break;
                }
            }
        }
        return bitSet;
    }

    //exercise2
    public void xercise2(ArrayList<String> strings) {
        Iterator<String> iter = strings.iterator();
        int i = 0;
        while (iter.hasNext()) {
            strings.remove(i);
            strings.add(i, iter.next().toUpperCase());
            i++;
        }
    }

    public void exercise2_(ArrayList<String> strings) {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            strings.remove(i);
            strings.add(i, strings.get(i).toUpperCase());
        }
    }

    public void exercise2__(ArrayList<String> strings) {
        strings.replaceAll(String::toUpperCase);
    }

    //exercise3
    public <T> void union(Set<T> a, Set<? extends T> b) {
        a.addAll(b);
    }

    public <T> void intersection(Set<T> a, Set<? extends T> b) {
        a.retainAll(b);
    }

    public <T> void deduction(Set<T> a, Set<? extends T> b) {
        a.retainAll(b);
    }

    //exercise4
    /*
    使用java.util.concurrent中的包里的数据结构代替共享数据结构。
    或者不修改集合的数据，只读。
     */

    //exercise5
    public static void swap(List<?> list, int i, int j) {
        if (list instanceof RandomAccess) {
            swapHelper(list, i, j);
        } else {
            ListIterator iter = list.listIterator();
            int dest;
            int origin;
            if (i < j) {
                dest = j;
                origin = i;
            } else {
                dest = i;
                origin = j;
            }
            for (int k = 0; k < origin; k++) {
                iter.next();
            }
            Object o = iter.next();
            for (int k = origin + 1; k < dest; k++) iter.next();
            Object d = iter.next();
            iter.set(o);
            for (int k = dest + 1; k > origin; k--) iter.previous();
            iter.set(d);
        }
    }

    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    //exercise6
    /*
    会报错，提示类型不匹配。因为泛型是不变形的，HashSet和Set不是同个类型，所以不能转换。
    可以把HashSet换成Set类型，即可编译。
     */

    //exercise7
    void printWordFrequency(String filename) throws IOException {
        Scanner in = new Scanner(Paths.get(filename), StandardCharsets.UTF_8);
        in.useDelimiter("\\PL+");

        Map<String, Integer> frequency = new TreeMap<>();
        while (in.hasNext()) {
            String word = in.next();
            frequency.merge(word, 1, Integer::sum);
        }
        in.close();
        frequency.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }

    //exercise8
    void printWordLines(String filename) throws IOException {
        Scanner in = new Scanner(Paths.get(filename), StandardCharsets.UTF_8);
        Map<String, Set<Integer>> WordLines = new TreeMap<>();
        int line = 1;
        while (in.hasNextLine()) {
            String words = in.nextLine();
            String[] word = words.split("\\PL+");
            for (String w : word) {
                Set<Integer> oldValue = WordLines.get(w);
                if (oldValue == null) {
                    Set x = new HashSet();
                    x.add(line);
                    WordLines.put(w, x);
                } else {
                    oldValue.add(line);
                    WordLines.put(w, oldValue);
                }
            }
            line++;
        }
        in.close();
        WordLines.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }

    //exercise9
    void exercise9a(Map<String, Integer> counts, String word) {
        if (counts.containsKey(word)) {
            counts.put(word, counts.get(word) + 1);
        } else counts.put(word, 1);
    }

    void exercise9b(Map<String, Integer> counts, String word) {
        if (counts.get(word) != null) {
            counts.put(word, counts.get(word) + 1);
        } else counts.put(word, 1);
    }

    void exercise9c(Map<String, Integer> counts, String word) {
        int count = counts.getOrDefault(word, 0);
        if (count != 0) {
            counts.put(word, counts.get(word) + 1);
        } else counts.put(word, 1);
    }

    void exercise9d(Map<String, Integer> counts, String word) {
        if (counts.putIfAbsent(word, 1) != null) {
            counts.put(word, counts.get(word) + 1);
        }
    }

    //exercise10
    static class Neighbor implements Comparable<Neighbor> {
        private String cityName;
        private double distance;

        public Neighbor(String name, double dis) {
            cityName = name;
            distance = dis;
        }

        public String getCityName() {
            return cityName;
        }

        public double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(Neighbor o) {
            return Double.compare(distance, o.getDistance());
        }
    }

    public static void Dijkstra(String city) throws IOException {
        Scanner in = new Scanner(Paths.get("cities.txt"),
                StandardCharsets.UTF_8);
        Map<String, Set<Neighbor>> map = new HashMap<>();
        // build graph
        while (in.hasNextLine()) {
            String[] t = in.nextLine().split(",");
            Neighbor a = new Neighbor(t[0], Double.valueOf(t[2]));
            Neighbor b = new Neighbor(t[1], Double.valueOf(t[2]));
            Set x;
            if (map.containsKey(t[0])) {
                x = map.get(t[0]);
            } else {
                x = new HashSet();
            }
            x.add(b);
            map.put(t[0], x);
            if (map.containsKey(t[1])) {
                x = map.get(t[1]);
            } else {
                x = new HashSet();
            }
            x.add(a);
            map.put(t[1], x);
        }
        in.close();
        // compute shortest distance

        Set<String> S = new HashSet<>();
        S.add(city);
        Map<String, Double> D = new HashMap<>();
        D.put(city, 0.0);
        PriorityQueue<Neighbor> priorityQueue = new PriorityQueue<>();
        for (Neighbor x : map.get(city)) {
            D.put(x.getCityName(), x.getDistance());
            priorityQueue.add(x);
        }
        while (S.size() != map.size()) {
            Neighbor n = priorityQueue.remove();
            S.add(n.getCityName());
            for (Neighbor x : map.get(n.getCityName())) {
                if (!D.containsKey(x.getCityName()) ||
                        D.get(x.getCityName()) > x.getDistance() + D.get(n.getCityName())) {
                    D.put(x.getCityName(),
                            x.getDistance() + D.get(n.getCityName()));
                    priorityQueue.remove(x);
                    priorityQueue.add(new Neighbor(x.getCityName(),
                            x.getDistance() + D.get(n.getCityName())));
                }
            }
        }
        System.out.println("origin: " + city);
        D.forEach((k, v) -> {
            System.out.println("city: " + k + ", " + "dis: " + v);
        });
    }

    //exercise11
    static void shuffle() throws IOException {
        List<String> words = new ArrayList<>();
        Scanner in = new Scanner(Paths.get("1.txt"), StandardCharsets.UTF_8);
        in.useDelimiter("\\PL+");
        while (in.hasNext()) {
            words.add(in.next());
        }
        in.close();
        Collections.shuffle(words.subList(1, words.size() - 1), new Random());
        for (int i = 0; i < words.size(); i++) {
            System.out.print(words.get(i) + " ");
        }
    }

    //exercise12
    static void shuffle1() throws IOException {
        List<String> words = new ArrayList<>();
        Scanner in = new Scanner(Paths.get("1.txt"), StandardCharsets.UTF_8);
        in.useDelimiter("\\PL+");
        while (in.hasNext()) {
            words.add(in.next().toLowerCase());
        }
        in.close();
        Collections.shuffle(words, new Random());
        String t = words.get(0).toUpperCase();
        words.set(0, t);
        for (int i = 0; i < words.size(); i++) {
            if (i == words.size() - 1) System.out.print(words.get(i) + ".");
            else System.out.print(words.get(i) + " ");
        }
    }

    //exercise13
    static class Cache<K, V> extends LinkedHashMap<K, V> {
        private int limit;

        public Cache(int limit) {
            super();
            this.limit = limit;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return this.size() >= limit;
        }
    }

    //exercise14
    static List<Integer> exercise14(int n) {
        Integer[] number = new Integer[n + 1];
        for (int i = 0; i <= n; i++) number[i] = i;
        return List.of(number);
    }

    //exercise15 
    static <R> List<R> exercise15(IntFunction<R> intFunction, int n) {
        ArrayList<R> list = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            list.add(intFunction.apply(i));
        }
        return Collections.unmodifiableList(list);
    }

    //exercise16
    static <R> List<R> exercise16(IntFunction<R> intFunction, int n) {
        ArrayList<R> list = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            list.add(intFunction.apply(i));
        }
        return n > 100 ? list.subList(n - 100 + 1, n + 1) : list.subList(0, n + 1);
    }

    //exercise17
    static void exercise17() {
        List strings = Collections.checkedList(new ArrayList<>(),
                String.class);
        int x = 1;
        strings.add(x);
    }
    /*
    java.lang.ClassCastException: Attempt to insert class java.lang.Integer
    element into collection with element type class java.lang.String
    他会详细说明错误类型和发生错误的插入代码位置。
     */

    //exercise18
    /*
    因为静态变量是原生类型，会产生uncheck警告。而方法返回的是泛型，可以指定类型，保证了类型安全。
     */

}
