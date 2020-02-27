import java.io.File;
import java.io.FileFilter;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;

public class sec3 {

    //exercise1
    public interface Measurable {
        double getMeasure();
    }

    static class Employee implements Measurable {
        private String name;
        private double salary;

        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public double getMeasure() {
            return salary;
        }
    }

    double average(Measurable[] objects) {
        double sum = 0;
        for (Measurable x : objects) {
            sum += x.getMeasure();
        }
        return objects.length > 0 ? sum / objects.length : 0;
    }

    //exercise2
    Measurable largest(Measurable[] objects) {
        if (objects.length == 0) return null;
        Measurable t = objects[0];
        for (Measurable i : objects) {
            Employee e = (Employee) i;
            if (e.getMeasure() > t.getMeasure()) t = i;
        }
        return t;
    }

    //exercise3
    /*
    String的父类有Object，Serializable, Comparable<String>, CharSequence,
    Constable, ConstantDesc。
    Scanner的父类有Object，Iterator<String>, Closeable。
    ImageOutputStream的父类有Object，ImageInputStream, DataOutput。
    不考虑直接父类以上
     */

    //exercise4
    public interface IntSequence {
        default boolean hasNext() {
            return true;
        }

        int next();

        public static IntSequence of(int... values) {
            return new IntSequence() {

                final int[] array = Arrays.copyOf(values, values.length);
                private int number = 0;

                @Override
                public boolean hasNext() {
                    if (array.length <= number) return false;
                    else return true;
                }

                @Override
                public int next() {
                    number++;
                    return array[number - 1];
                }
            };
        }

        public static IntSequence constant(int number) {
            return () -> number;
        }
    }

    //exercise5在exercise4上已修改

    //exercise6
    interface Sequeue<T> {
        boolean hasNext();

        T next();
    }

    static class SquareSequence implements Sequeue<BigInteger> {
        private BigInteger i = BigInteger.valueOf(0);

        public boolean hasNext() {
            return true;
        }

        public BigInteger next() {
            i = i.add(BigInteger.valueOf(1));
            return i.multiply(i);
        }
    }

    /*
     * exercise7
     * 可以编译，因为forEachRemaining方法是默认方法。
     * 不重新编译也能运行，调用forEachRemaining也可以运行。
     * 调用remove没有问题，因为方法被覆盖。
     */
    static class DigitSequence implements Iterator<Integer> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Integer next() {
            return null;
        }

        @Override
        public void remove() {

        }

        @Override
        public void forEachRemaining(Consumer<? super Integer> action) {

        }
    }

    //exercise8
    void luckSort(ArrayList<String> strings, Comparator<String> comp) {
        while (true) {
            int i;
            for (i = 0; i < strings.size() - 1; i++) {
                if (comp.compare(strings.get(i), strings.get(i + 1)) > 0) break;
            }
            if (i == strings.size() - 1) return;
            else Collections.shuffle(strings);
        }
    }

    //exercise9
    static class Greeter implements Runnable {
        private int n;
        private String target;

        public Greeter(int n, String target) {
            this.n = n;
            this.target = target;
        }

        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                System.out.println("Hello," + target);
            }
        }
    }

    //exercise10
    public static void runTogether(Runnable... tasks) {
        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public static void runInOrder(Runnable... tasks) {
        for (Runnable task : tasks) {
            task.run();
        }
    }

    //exercise11
    public File[] getAllDirectory(String path) {
        File file = new File(path);
        file.listFiles((pathname -> pathname.isDirectory()));
        file.listFiles(File::isDirectory);
        return file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    //exercise12
    public String[] getSpecificFile(String pathname, String filetype) {
        File file = new File(pathname);
        return file.list((dir, name) -> name.endsWith(filetype));
        //捕获形参filetype
    }

    //exercise13
    public void Sort(File[] files) {
        Arrays.sort(files,
                Comparator.comparing(File::isDirectory)
                        .thenComparing((t -> t.getAbsolutePath())));
    }

    //exercise14
    public Runnable mixTasks(Runnable... tasks) {
        return () -> {
            for (Runnable task : tasks) {
                Thread thread = new Thread(task);
                thread.start();
            }
        };
    }

    //exercise15
    void exercise15(Employee... employees) {
        Comparator<Employee> comparator =
                Comparator.comparing(Employee::getMeasure).thenComparing(Employee::getName);
        Arrays.sort(employees, comparator);
        Arrays.sort(employees, comparator.reversed());
    }

    //exercise16
    static class RandomSequence implements IntSequence {
        private int low;
        private int high;
        private Random generator;

        public RandomSequence(int low, int high) {
            this.high = high;
            this.low = low;
            generator = new Random();
        }

        public int next() {
            return low + generator.nextInt(high - low + 1);
        }

        public boolean hasNext() {
            return true;
        }
    }

    public static IntSequence randomInts(int low, int high) {
        return new RandomSequence(low, high);
    }
}
