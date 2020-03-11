import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class sec8 {

    //exercise1
    static void exercise1() throws IOException {
        String contents = Files.readString(Paths.get("1.txt"));
        List<String> words = List.of(contents.split("\\PL+"));
        Stream<String> longwords =
                words.stream().peek(System.out::println).filter(w -> w.length() > 12)
                        .limit(5);

        Object[] objects = longwords.toArray();
    }
    /*
    在filter处打断点即可观察执行次数，当满足5个长单词后不再输出。
     */

    //exercise2
    static void exercise2() throws IOException {
        String contents = Files.readString(Paths.get("patterns.txt"));
        List<String> words = List.of(contents.split("\\PL+"));
        long count = words.stream().filter(w -> w.length() > 12).count();
        System.out.println(count);
    }

    static void exercise2_() throws IOException {
        String contents = Files.readString(Paths.get("patterns.txt"));
        List<String> words = List.of(contents.split("\\PL+"));
        long count = words.parallelStream().filter(w -> w.length() > 12).count();
        System.out.println(count);
    }
    /*
    一个13.4MB的文件，parallelStream平均用时861ms，stream平均用时822ms。
    说明在文件不是特别大的条件下，使用parallelStream的效率不会更高。
     */

    //exercise3
    static void exercise3() {
        Integer[] values = {1, 4, 9, 16};
        Stream<Integer> stream = Stream.of(values);
    }
    /*
    会提示类型不匹配，因为无法使用基本类型在泛型函数上。
    解决办法是把int[]改为Integer[]。
     */

    //exercise4
    static Stream<Long> exercise4(long a, long m, long c) {
        BigInteger ba = BigInteger.valueOf(a);
        BigInteger bm = BigInteger.valueOf(m);
        BigInteger bc = BigInteger.valueOf(c);

        return Stream.iterate(a, n -> {
            BigInteger bn = BigInteger.valueOf(n);
            BigInteger result =
                    ba.multiply(bn).add(bc);
            BigInteger[] x = result.divideAndRemainder(bm);
            return x[1].longValue();
        });
    }

    //exercise5
    static Stream<String> codePoints(String s) {
        IntStream intStream = IntStream.iterate(0,
                n -> n >= s.length(),
                n -> s.offsetByCodePoints(n, 1));
        Stream<String> stringStream =
                intStream.mapToObj(n -> {
                    return String.valueOf(s.codePointAt(n));
                });
        return stringStream;
    }

    //exercise6
    static boolean exercise6(String s) {
        long n = s.length();
        IntStream intStream = s.codePoints();
        long count = intStream.filter(Character::isAlphabetic).count();
        return n == count;
    }

    static boolean exercise6_(String s) {
        long num = s.length();
        IntStream intStream = s.codePoints();
        IntStream intStream1 = intStream.limit(1);
        long f = intStream.filter(Character::isJavaIdentifierStart).count();
        if (f != 1) return false;
        f = intStream.skip(1).filter(Character::isJavaIdentifierPart).count();
        return f == num - 1;
    }

    //exercise7
    static List<String> exercise7() throws IOException {
        String contents = new String(Files.readString(Paths.get("alice.txt")));
        Stream<String> words = new Scanner(contents).tokens();
        return words.filter(sec8::exercise6).limit(100)
                .collect(toList());
    }

    static List<String> exercise7_() throws IOException {
        String contents = new String(Files.readString(Paths.get("alice.txt")));
        Stream<String> words = new Scanner(contents).tokens();
        Map<String, Long> map = words.filter(sec8::exercise6).collect(
                groupingBy(String::toLowerCase, Collectors.counting())
        );
        Set<Map.Entry<String, Long>> set = map.entrySet();
        ArrayList<Map.Entry<String, Long>> arrayList = new ArrayList<>(set);
        arrayList.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        List<String> w = new ArrayList<>();
        for (int i = 0; i < 10; i++) w.add(arrayList.get(i).getKey());
        return w;
    }

    //exercise8
    static class Student {
        private int id;
        private String name;

        public Student(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Optional<Integer> getId() {
            return Stream.of(id).findFirst();
        }

        public String getName() {
            return name;
        }
    }

    static public void exercise8() {
        Stream<Student> studentStream = Stream.of(new Student(1, "sd"));
        Map<String, Set<Integer>> studentMap = studentStream.collect(groupingBy(
                Student::getName, flatMapping(s -> s.getId().stream(), toSet())
        ));
    }

    //exercise9
    static String[] exercise9(List<String> s) {
        return s.stream().filter(n -> n.toLowerCase().contains("a")).filter(
                n -> n.toLowerCase().contains("e")).filter(
                n -> n.toLowerCase().contains("i")).filter(
                n -> n.toLowerCase().contains("o")).filter(
                n -> n.toLowerCase().contains("u")).toArray(String[]::new);
    }

    //exercise10
    static double exercise10(Stream<String> stringStream) {
        return stringStream.collect(Collectors.summarizingInt(String::length)).getAverage();
    }

    //exercise11
    static List<String> exercise11(Stream<String> stringStream) {
        Map<Integer, List<String>> map =
                stringStream.collect(Collectors.groupingBy(String::length));
        int max = Collections.max(map.keySet());
        return map.get(max);
    }

    //exercise12
    public static <T> boolean isFinite(Stream<T> stream) {
        Object[] array = stream.toArray();
        return false;
    }
    /*
    如果一个流是无限流，则无法停止判断程序，没有返回结果。
     */

    //exercise13
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Object[] f = first.toArray();
        Object[] s = second.toArray();
        int len = Math.min(f.length, s.length);
        ArrayList<T> list = new ArrayList<>();
        int i;
        for (i = 0; i < len; i++) {
            list.add((T) f[i]);
            list.add((T) s[i]);
        }
        if (i == f.length) {
            for (int j = i; j < s.length; j++) {
                list.add(null);
                list.add((T) s[j]);
            }
        } else {
            for (int j = i; j < f.length; j++) {
                list.add((T) f[j]);
                list.add(null);
            }
        }
        return list.stream();
    }

    //exercise14
    static <T> ArrayList<T> exercise14(Stream<ArrayList<T>> stream) {
        return stream.reduce((x, y) -> {
            ArrayList<T> z = new ArrayList<>(x);
            z.addAll(y);
            return z;
        }).orElse(new ArrayList<>());
    }

    static <T> ArrayList<T> exercise14_1(Stream<ArrayList<T>> stream) {
        return stream.reduce(new ArrayList<>(), (x, y) -> {
            ArrayList<T> z = new ArrayList<>(x);
            z.addAll(y);
            return z;
        });
    }

    static <T> ArrayList<T> exercise14_2(Stream<ArrayList<T>> stream) {
        return stream.reduce(new ArrayList<>(), (x, y) -> {
            x.addAll(y);
            return x;
        }, (x, y) -> {
            x.addAll(y);
            return x;
        });
    }

    //exercise15
    static double exercise15(Stream<Double> stream) {
        ArrayList<Double> sum =
                stream.filter(Objects::nonNull).reduce(new ArrayList<Double>(),
                        (x, y) -> {
                            x.add(y);
                            return x;
                        }, (x, y) -> {
                            x.addAll(y);
                            return x;
                        });
        double n = 0;
        for (double x : sum) n += x;
        return n / sum.size();
    }
    /*
    因为一个流不能使用两次。
     */

    //exercise16
    static void exercise16() {
        StringBuilder fifty = new StringBuilder();
        fifty.append(1);
        for (int i = 0; i < 49; i++) fifty.append(0);

        Stream<BigInteger> stream = Stream.iterate(new BigInteger(fifty.toString()),
                n -> n.add(BigInteger.ONE)).parallel().unordered()
                .filter(n -> {
                    String s = n.toString();
                    return s.length() == 50;
                })
                .filter(n -> n.isProbablePrime(10)).limit(500);
        Object[] x = stream.toArray();
    }
    /*
    由于题意不明，所以理解为数位为50的素数。发现并行流速度比串行流慢。
    串行：1401ms；并行：1649ms
     */

    //exercise17
    static List<String> exercise17() throws IOException {
        String contents = Files.readString(Paths.get("patterns.txt"));
        List<String> words = List.of(contents.split("\\PL+"));
        Map<Integer, List<String>> map =
                words.parallelStream().collect(Collectors.groupingByConcurrent(String::length));
        Integer[] s = map.keySet().toArray(new Integer[0]);
        Arrays.sort(s, Comparator.reverseOrder());
        int num = 0;
        List<String> longwords = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            int temp = map.get(s[i]).size();
            if (num >= 500) break;
            else {
                num += temp;
                longwords.addAll(map.get(s[i]));
            }

        }
        int r = num - 500;
        while (r > 0) {
            longwords.remove(longwords.size() - 1);
            r--;
        }
        return longwords;
    }
    /*
    使用了一个13.4MB的文件，结果发现使用并行流的速度，还是比串行流慢。
    串行：811ms；并行：881ms
     */

    //exercise18
    static <T> Stream<T> exercise18(Stream<T> stream) {
        ArrayList<T> arrayList = stream.reduce(new ArrayList<T>(),
                (total, word) -> {
                    if (total.get(total.size() - 1).equals(word)) return total;
                    else {
                        total.add(word);
                        return total;
                    }
                }, (total1, total2) -> {
                    total1.addAll(total2);
                    return total1;
                });
        return arrayList.stream();
    }
    /*
    如果并行流执行，结果将错误。因为并行流的起点和前一个终点元素可能会重复，但不会被删去。
     */
}
