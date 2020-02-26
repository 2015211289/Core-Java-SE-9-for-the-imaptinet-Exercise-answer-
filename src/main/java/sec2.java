import com.opencsv.CSVReaderHeaderAware;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static java.lang.System.out;
import static java.time.LocalDate.of;

public class sec2 {
    public static void exercise1() {
        int year = 2020;
        int month = 2;
        LocalDate date = LocalDate.of(year, month, 1);
        System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
        int value = date.getDayOfWeek().getValue() % 7;
        for (int i = 0; i < value; i++) System.out.print("    ");
        while (date.getMonthValue() == month) {
            System.out.printf("%4d", date.getDayOfMonth());
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 7)
                System.out.println();
        }
        if (date.getDayOfWeek().getValue() != 7)
            System.out.println();
    }

    public static void exercise2() {
        /*
        Scanner类的nextInt方法读取下一个令牌，是修改器方法。
        Random类的nextInt方法不改变生成器序列，是访问器方法。
         */
    }

    public static void exercise3() {
        /*
        堆栈的出栈操作，不仅修改对象，也返回出栈元素。
        没有碰上返回void的访问器方法。
         */
    }

    void exercise4() {
        /*
        int是基本类型，不能在方法中传递引用，所以无法交换值。
        IntHolder对象在JavaSE13中没找到。
        Integer没有实例方法修改内部值，所以无法交换值。
         */
    }

    // exercise5

    /**
     * 一个<code>Point</code>2维坐标对象
     *
     * @author 谢宇
     * @version 1.0
     */
    public class Point {
        private double x, y;

        public Point() {
            x = 0;
            y = 0;
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Point translate(double x, double y) {
            return new Point(x + this.x, y + this.y);
        }

        public Point scale(double rate) {
            return new Point(this.x * rate, this.y * rate);
        }
    }

    void exercise6() {
        class Point {
            private double x, y;

            public Point() {
                x = 0;
                y = 0;
            }

            public Point(double x, double y) {
                this.x = x;
                this.y = y;
            }

            public double getX() {
                return x;
            }

            public double getY() {
                return y;
            }

            /**
             * 移动点的坐标
             * @param x 横坐标
             * @param y 纵坐标
             */
            public void translate(double x, double y) {
                this.x += x;
                this.y += y;
            }

            /**
             * 按比例更新坐标
             * @param rate 比例系数
             */
            public void scale(double rate) {
                this.x *= rate;
                this.y *= rate;
            }
        }
    }

    void exercise8() {
        /*
        在Intellij IDEA中，我发现右键类，有generate选项，
        可以生成一些Object类的方法和构造函数。
         */
    }

    //exercise9
    public class Car {
        private double e;
        private double dis;
        private double oil;

        public Car(double e) {
            this.e = e;
            dis = 0;
        }

        public int run(double miles) {
            double need = miles / e;
            if (oil < need) return -1;
            else {
                oil -= need;
                dis += miles;
                return 0;
            }
        }

        public int addOil(double add) {
            if (add > 0) {
                oil += add;
                return 0;
            } else return -1;
        }

        public double getDis() {
            return dis;
        }
        /*
        Car类应该是可修改类，代表了车的状态会变化，但仍然是同一辆车。
         */
    }

    //exercise10
    static class RandomNumbers {
        static int randomElement(int[] array) {
            Random random = new Random();
            if (array.length == 0) return 0;
            return array[random.nextInt(array.length - 1)];
        }

        static int randomElement(ArrayList<Integer> arrayList) {
            if (arrayList.size() == 0) return 0;
            Random random = new Random();
            return arrayList.get(random.nextInt(arrayList.size() - 1));
        }
        /*
        不在int[]和ArrayList作为实例方法是因为没有对应的接口吧。
        */
    }

    void exercise11() {
        int year = 2020;
        int month = 2;
        LocalDate date = of(year, month, 1);
        out.println(" Sun Mon Tue Wed Thu Fri Sat");
        int value = date.getDayOfWeek().getValue() % 7;
        for (int i = 0; i < value; i++) out.print("    ");
        while (date.getMonthValue() == month) {
            out.printf("%4d", date.getDayOfMonth());
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 7)
                out.println();
        }
        if (date.getDayOfWeek().getValue() != 7)
            out.println();
    }

    /*
    exercise12
    找不到class名，因为class名之前会加上包名。
    javac -d . 可以让class文件生成在当前路径为根的包名对应路径下，然后java通过全名来执行程序。
  */

    void exercise13() throws Exception {
        Map<String, String> values = new CSVReaderHeaderAware
                (new FileReader("yourfile.csv")).readMap();
    }

    /*
    exercise14
    在Network$Member中，存在一个final修饰外部类类型的变量this，指向外部类。
     */

    //exercise15
    static class Invoice {
        public static class Item {
            String description;
            int quantity;
            double unitPrice;

            double price() {
                return quantity * unitPrice;
            }

            public Item(String d, int q, double u) {
                this.unitPrice = u;
                this.quantity = q;
                this.description = d;
            }
        }

        private ArrayList<Item> items = new ArrayList<>();

        public void add(Item i) {
            items.add(i);
        }

        void print() {
            for (Item item : items) {
                out.printf("description:%s\nquantity:%d\nunitPrice:%f" +
                                "\ntotalPrice:%f\n", item.description, item.quantity,
                        item.unitPrice, item.price());
            }
        }
    }

    //exercise16
    static class Queue {
        static class Node {
            public String string;
            public Node next;

            public Node(String s) {
                string = s;
                next = null;
            }
        }

        class Iterator {
            private Queue.Node index;

            public Iterator() {
                index = front;
            }

            public String next() {
                if (index == null) return null;
                else {
                    Node t = index;
                    index = index.next;
                    return t.string;
                }
            }

            public boolean hasNext() {
                return index != null;
            }
        }

        private Node front;
        private Node rear;

        public Queue() {
            front = null;
            rear = null;
        }

        void add(String s) {
            Node node = new Node(s);
            if (front == null) {
                front = node;
                rear = node;
            } else rear.next = node;
        }

        Node remove() {
            Node f = front;
            if (f != null) {
                front = f.next;
            }
            return f;
        }

        Iterator iterator() {
            return this.new Iterator();
        }
    }

    /*
    exercise17
    已在16题上修改。
     */
}
