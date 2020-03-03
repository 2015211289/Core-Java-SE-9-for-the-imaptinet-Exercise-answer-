import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class sec6 {

    //exercise1
    static class Stack<E> {
        private ArrayList<E> arrayList;

        public Stack() {
            arrayList = new ArrayList<>();
        }

        public E pop() {
            E value = arrayList.get(arrayList.size() - 1);
            arrayList.remove(arrayList.size() - 1);
            return value;
        }

        public void push(E value) {
            arrayList.add(value);
        }

        public boolean isEmpty() {
            return arrayList.isEmpty();
        }
    }

    //exercise2
    static class Stack2<E> {
        private E[] values;
        private int index;
        private int size;

        public <T> Stack2(Class<T> cl) {
            index = -1;
            @SuppressWarnings("unchecked") E[] a =
                    (E[]) Array.newInstance(cl, 100);
            values = a;
            size = 100;
        }

        public E pop() {
            if (index < 0) return null;
            index--;
            return values[index + 1];
        }

        public void push(E value) {
            if (index == size - 1) {
                values = java.util.Arrays.copyOf(values, 200);
                size += 100;
            }
            values[index + 1] = value;
            index++;
        }
    }

    static class Stack3<E> {
        private Object[] values;
        private int index;
        private int size;

        public Stack3() {
            index = -1;
            size = 100;
            values = new Object[size];
        }

        public E pop() {
            if (index < 0) return null;
            index--;
            @SuppressWarnings("unchecked") E x = (E) values[index + 1];
            return x;
        }

        public void push(E value) {
            if (index == size - 1) {
                values = java.util.Arrays.copyOf(values, 200);
                size += 100;
            }
            values[index + 1] = value;
            index++;
        }
    }
    /*
    我更喜欢第二种方法，因为不用知道泛型的具体类型。
     */

    //exercise3
    static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    static class Table<K, V> {
        private ArrayList<Entry<K, V>> arrayList;

        public Table() {
            arrayList = new ArrayList<>();
        }

        V findValue(K key) {
            for (Entry<K, V> x : arrayList) {
                if (x.key == key) return x.getValue();
            }
            return null;
        }

        void setValue(K key, V value) {
            for (Entry<K, V> x : arrayList) {
                if (x.key == key) {
                    x.value = value;
                    return;
                }
            }
            arrayList.add(new Entry<>(key, value));
        }

        void removeKey(K key) {
            for (Entry<K, V> x : arrayList) {
                if (x.key == key) {
                    arrayList.remove(x);
                }
            }
        }
    }

    //exercise4
    /*
    Entry类还应该是泛型类，因为外部类的类型参数无法在嵌套类中访问。
    如果Entry类是内部类，则可以访问了类型参数，不需要是泛型类。
     */

    //exercise5
    /*
    不加类型参数前，提示返回值需要是Double[]类型的，但由于类型T的变长参数实例类型不一致，
    所以返回T[]，类型不匹配。
    加上类型参数后，编译器知道类型参数是Double，所以提示函数的参数类型不为Double。
    解决方法很简单，把后面两个数改为double形式就好。
    */

    //exercise6
    public <T> void append(ArrayList<T> origin,
                           ArrayList<? super T> destination) {
        for (T x : origin) {
            destination.add(x);
        }
    }

    public <T> void append1(ArrayList<? extends T> origin,
                            ArrayList<T> destination) {
        for (T x : origin) {
            destination.add(x);
        }
    }

    //exercise7
    static class Pair<E extends Comparable<E>> {
        private E a;
        private E b;

        public Pair(E a, E b) {
            this.a = a;
            this.b = b;
        }

        public E getA() {
            return a;
        }

        public E getB() {
            return b;
        }

        public E min() {
            return a.compareTo(b) > 0 ? b : a;
        }

        public E max() {
            return a.compareTo(b) > 0 ? a : b;
        }
    }

    //exercise8
    /*
    在exercise7上已修改。
     */

    //exercise9
    static class Arrays {
        public static <E extends Comparable<E>> Pair<E> firstLast(ArrayList<?
                extends E> a) {
            if (a.isEmpty()) return null;
            return new Pair<>(a.get(0), a.get(a.size() - 1));
        }

        public static <E extends Comparable<E>> E min(ArrayList<? extends E> a) {
            if (a.isEmpty()) return null;
            E min = a.get(0);
            for (E x : a) {
                if (x.compareTo(min) < 0) min = x;
            }
            return min;
        }

        public static <E extends Comparable<E>> E max(ArrayList<? extends E> a) {
            if (a.isEmpty()) return null;
            E max = a.get(0);
            for (E x : a) {
                if (x.compareTo(max) > 0) max = x;
            }
            return max;
        }

        public static <E extends Comparable<E>> Pair<E> minMax(ArrayList<?
                extends E> a) {
            return new Pair<>(min(a), max(a));
        }

        @SafeVarargs
        public static <T> T[] construct(int n, T... objs) {
            return java.util.Arrays.copyOf(objs, n);
        }
    }

    //exercise10
    /*
    在exercise9上已修改。
     */

    //exercise11
    /*
    在exercise9上已修改。
     */

    //exercise12
    public static <T> void minmax(List<T> elements, Comparator<? super T> comp,
                                  List<? super T> result) {
        if (elements.isEmpty()) return;
        T max = elements.get(0);
        T min = elements.get(0);
        for (T x : elements) {
            if (comp.compare(max, x) < 0) max = x;
            if (comp.compare(min, x) > 0) min = x;
        }
        result.add(min);
        result.add(max);
    }

    //exercise13
    static public class Lists {
        public static boolean hasNulls(List<?> elements) {
            for (Object e : elements) {
                if (e == null) return true;
            }
            return false;
        }

        public static void swap(List<?> elements, int i, int j) {
            swapHelper(elements, i, j);
        }

        private static <T> void swapHelper(List<T> elements, int i, int j) {
            T temp = elements.get(i);
            elements.set(i, elements.get(j));
            elements.set(j, temp);
        }
    }

    public static <T> void maxmin(List<T> elements, Comparator<? super T> comp,
                                  List<? super T> result) {
        minmax(elements, comp, result);
        Lists.swapHelper(result, 0, 1);
    }
    /*
    没有捕获通配符，变量的类型与方法的参数不符。实际上，方法前面的参数变量是capture作用，
    把参数的类型捕获。
     */

    //exercise14
    public static <T extends AutoCloseable> void closeAll(ArrayList<T> elems)
            throws Exception {
        Exception total = null;
        for (T elem : elems) {
            try {
                elem.close();
            } catch (Exception ex) {
                if (total == null) total = ex;
                else {
                    ex.initCause(total);
                    total = ex;
                }
            }
        }
        if (total != null) throw total;
    }

    //exercise15
    public <T, R> R[] functionALl(ArrayList<? extends T> a, Function<T, R> f) {
        if (a.isEmpty()) return null;
        R result = f.apply(a.get(0));
        @SuppressWarnings("unchecked") R[] newArray =
                (R[]) java.lang.reflect.Array.newInstance(result.getClass(),
                        a.size());
        for (int i = 0; i < a.size(); i++) {
            newArray[i] = f.apply(a.get(i));
        }
        return newArray;
    }

    //exercise16
    public static void sort(List<Comparable> list) {
    }

    public static Object max(Collection coll) {
        return null;
    }

    //exercise17
    static class Employee implements Comparable<Employee> {
        private String name;
        private double salary;

        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        @Override
        public int compareTo(Employee o) {
            return 0;
        }
    }
    /*
    合成的桥方法是：
    public int compareTo(java.lang.Object);
     */

    //exercise18
    /*
    因为泛型的类型参数不能是基本类型，此处T的类型为int。通过封装类，把int[]:new改为Integer[]:new.
    对于其他基本类，也要换成封装类。
     */

    //exercise19
    /*
    不能，因为不能实例化参数变量。
     */

    //exercise20
    @SafeVarargs
    public static final <T> T[] repeat(int n, T... objs) {
        T[] newArray = java.util.Arrays.copyOf(objs, n);
        for (int i = 0; i < n; i++) {
            newArray[i] = objs[0];
        }
        return newArray;
    }

    //exercise21
    /*
    exercise9上已修改。
     */

    //exercise22
    public static <V, T extends Throwable> V doWork(Callable<V> c,
                                                    Supplier<T> supplier) throws T {
        try {
            return c.call();
        } catch (Throwable ex) {
            T e = supplier.get();
            e.initCause(ex);
            throw e;
        }
    }

    //exercise23
    /*
    当使用throwAs方法时，由于类型擦除，使得抛出的类型T变为Throwable，然后编译器再转换为RuntimeException
    但如果时直接转换，则把父类变量转换到子类，会引发ClassCastException。
     */

    //exercise24
    /*
    所有方法，因为Class<?>的类型变量是某一个Class实例，所以相应的类型参数唯一，即可调用其方法。
     */

    //exercise25
    public static String genericDeclaration(Method m) {
        StringBuilder stringBuilder = new StringBuilder();
        TypeVariable<Method>[] vars = m.getTypeParameters();
        for (int i = 0; i < vars.length; i++) {
            Type[] bounds = vars[i].getBounds();
            if (bounds.length > 0) {
                stringBuilder.append(vars[i].getName() + ": ");
                for (int j = 0; j < bounds.length; j++) {
                    if (bounds[j] instanceof ParameterizedType)
                        stringBuilder.append(bounds[j] + " ");
                }
            }
            stringBuilder.append("\n");
        }
        Type[] cls = m.getGenericParameterTypes();
        for (int i = 0; i < cls.length; i++) {
            if (cls[i] instanceof ParameterizedType) {
                ParameterizedType p = (ParameterizedType) cls[i];
                stringBuilder.append(p.getTypeName() + ": ");
                Type[] types = p.getActualTypeArguments();
                for (int j = 0; j < types.length; j++) {
                    if (types[j] instanceof TypeVariable) {
                        TypeVariable x = (TypeVariable) types[j];
                        stringBuilder.append(x.getTypeName() + ' ');
                    }
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
