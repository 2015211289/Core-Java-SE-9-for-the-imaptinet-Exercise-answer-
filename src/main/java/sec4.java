import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleFunction;

public class sec4 {

    //exercise1
    static class Point {
        protected double x;
        protected double y;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return Double.compare(point.getX(), getX()) == 0 &&
                    Double.compare(point.getY(), getY()) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getX(), getY());
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    static class LabeledPoint extends Point {
        private String label;

        public LabeledPoint(String label, double x, double y) {
            super(x, y);
            this.label = label;
            this.x = x;
            this.y = y;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LabeledPoint)) return false;
            if (!super.equals(o)) return false;
            LabeledPoint that = (LabeledPoint) o;
            return Objects.equals(getLabel(), that.getLabel());
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), getLabel());
        }

        @Override
        public String toString() {
            return "LabeledPoint{" +
                    "x=" + getX() +
                    ", y=" + getX() +
                    ", label=" + getLabel() +
                    '}';
        }
    }

    //exercise2
    /*
    在exercise1上已修改。
    */

    //exercise3
    /*
    在exercise1上已修改。
    */

    //exercise4
    static abstract class Shape implements Cloneable {
        protected Point point;

        public Shape(Point point) {
            this.point = point;
        }

        public void moveBy(double dx, double dy) {
            point.x += dx;
            point.y += dy;
        }

        public abstract Point getCenter();

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class Circle extends Shape implements Cloneable {
        private double radius;

        public Circle(Point center, double radius) {
            super(center);
            this.radius = radius;
        }

        @Override
        public Point getCenter() {
            return point;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class Rectangle extends Shape implements Cloneable {
        private double width;
        private double height;

        public Rectangle(Point topleft, double width, double height) {
            super(topleft);
            this.width = width;
            this.height = height;
        }

        @Override
        public Point getCenter() {
            return new Point(point.getX() + width / 2, point.getY() + height / 2);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class Line extends Shape implements Cloneable {
        private Point to;

        public Line(Point from, Point to) {
            super(from);
            this.to = to;
        }

        @Override
        public Point getCenter() {
            return new Point((point.getX() + to.getX()) / 2,
                    (point.getY() + to.getY()) / 2);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    //exercise5
    /*
    已在exercise4上修改。
     */

    //exercise6
    static public class Item {
        private String description;
        private double price;

        public Item(String description, double price) {
            this.description = description;
            this.price = price;
        }

        public boolean equals(Object otherObject) {
            // A quick test to see if the objects are identical
            if (this == otherObject) return true;
            // Check that otherObject is an Item instance
            if (!(otherObject instanceof Item)) return false;
            // Test whether the instance variables have identical values
            Item other = (Item) otherObject;
            return Objects.equals(description, other.description)
                    && price == other.price;
        }

        public int hashCode() {
            return Objects.hash(description, price);
        }
    }

    public class DiscountedItem extends Item {
        private double discount;

        public DiscountedItem(String description, double price, double discount) {
            super(description, price);
            this.discount = discount;
        }

        public boolean equals(Object otherObject) {
            // A quick test to see if the objects are identical
            if (this == otherObject) return true;
            // Must return false if the explicit parameter is null
            if (otherObject == null) return false;
            // Check that otherObject is a Item
            if (Item.class == otherObject.getClass())
                return super.equals(otherObject);
            if (DiscountedItem.class == otherObject.getClass()) {
                DiscountedItem other = (DiscountedItem) otherObject;
                return discount == other.discount && super.equals(otherObject);
            } else return false;
        }

        public int hashCode() {
            return Objects.hash(super.hashCode(), discount);
        }
    }

    /*
    assuming x.equals(y)==true, it shows that at least they have same
    description value
    and price value. so y.equals(x) also be true.
    so it is symmetric.

    when x is a DiscountedItem, y is a Item, but z is a DiscountedItem with
    different discount from x, the answer must be in this case: x.equals(y)
    ==true, y.equals(z)==true, but x.equals(x)==false!
    so it's not transitive.
     */

    //exercise7
    public enum Color {
        BlACK, RED, BLUE, GREEN, CYAN, MAGENTA, YELLOW, WHITE;

        static Color getRed() {
            return RED;
        }

        static Color getGreen() {
            return GREEN;
        }

        static Color getBlue() {
            return BLUE;
        }
    }

    //exercise8
    /*
    getCanonicalName()对于局部类返回null，其他返回官方名称。
    getName对于数组返回[，加上一个代表类型的大写字母。
    getTypeName返回类的名字。
    getSimpleName直接返回类名，不加前缀。
    toString返回class或者interface，后面接着由getName返回的名称。
    toGenericString多返回类型的修饰符和参数类型列表，用<>引出。
     */

    //exercise9
    public String toString(Object obj) {
        Class<?> cl = obj.getClass();
        StringBuilder stringBuilder = new StringBuilder();
        while (cl != null) {
            for (Field f : cl.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                try {
                    f.setAccessible(true);
                    Object value = f.get(obj);
                    stringBuilder.append(
                            Modifier.toString(f.getModifiers()) + " " +
                                    f.getDeclaringClass() + " " +
                                    f.getName() + ": " + value + "\n"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cl = cl.getSuperclass();
        }
        return stringBuilder.toString();
    }

    //exercise10
    void exercise10() {
        Class<?> cl = int[].class;
        while (cl != null) {
            for (Method m : cl.getDeclaredMethods()) {
                System.out.println(
                        Modifier.toString(m.getModifiers()) + " " +
                                m.getReturnType().getCanonicalName() + " " +
                                m.getName() +
                                Arrays.toString(m.getParameters()));
            }
            cl = cl.getSuperclass();
        }
    }

    //exercise11
    void exercise11() {
        Class<?> cl = java.lang.System.class;
        try {
            Field field = cl.getField("out");
            field.trySetAccessible();
            Object o = field.get(null);
            Method method = field.getType().getMethod("println", String.class);
            method.invoke(o, "Hello World!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //exercise12
    /*
    采用练习11的pinrtln方法输出"Hello World！"10000遍，用时222ms。
    直接使用System.out.println,用时112ms。
    说明采用反射机制的性能更差，而且较低。
     */

    void exercise13(java.lang.reflect.Method method, double low, double high,
                    double steps)
            throws Exception {
        for (double i = low; Double.compare(i, high) <= 0; i += steps) {
            System.out.println(method.invoke(null, i));
        }
    }

    void exercise13(DoubleFunction<Object> doubleFunction, double low,
                    double high, double steps) {
        for (double i = low; Double.compare(i, high) <= 0; i += steps) {
            System.out.println(doubleFunction.apply(i));
        }
    }
    /*
    用函数式接口会更安全，反射会存在权限问题,会抛出已检查异常。
    方法1计算输出1到10000的二次根号，用时185ms。
    方法2用时119ms，效率更高。
    用函数式接口更方便，使用method更麻烦，需要处理异常。
     */
}
