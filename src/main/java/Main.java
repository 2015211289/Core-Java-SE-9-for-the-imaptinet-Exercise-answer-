import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

@FunctionalInterface
interface p {
    Color apply(int x, int y);
}

/**
 * <code>Main</code>
 *
 * @author seth
 * @version 1.0
 */

public class Main {
    /**
     * @deprecated {@link Main#average(int...)}
     */
    public static int name = 0;

    static {
        repeat((a, b) -> "123" + a + b, 2, 3);
        int[] temp = {1, 1, 2, 3};
        for (int atg : temp) {
            new Thread(() -> System.out.println(atg)).start();
        }


        try (PrintWriter out = new PrintWriter("put.txt")) {
            out.println('1');

        } catch (IOException e) {
            Throwable[] sec = e.getSuppressed();

        }

        //assert 1<=0:"cuowu";
        Logger.getGlobal().warning("hello");

        Logger logger = Logger.getLogger("main");
        logger.setLevel(Level.FINE);
        logger.setUseParentHandlers(false);
        try {
//            FileHandler handler = new FileHandler();
//            handler.setLevel(Level.FINE);
//            logger.addHandler(handler);
//            logger.fine("hello world!!!");

//            BigInteger limit= new BigInteger("5");
//            Stream<BigInteger> intergers = Stream.iterate(BigInteger.ZERO,
//                    (BigInteger n) -> n.compareTo(limit) < 0,
//                    (BigInteger n)->n.add(BigInteger.ONE));
//
//            Object[] p = intergers.peek(System.out::println).peek(x->{
//                return;}).toArray();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Random random = new Random();

    public static void main(String[] args) {
        try {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
//                Files.newOutputStream(Paths.get("1.txt")));
//
//
//            Employee employee=new Employee("xx",123);
//            objectOutputStream.writeObject(employee);

//            ProcessBuilder processBuilder= new ProcessBuilder("ls");
//            Process process=processBuilder.start();
//            Scanner in =new Scanner(process.getInputStream());
//            while(in.hasNextLine()){
//                System.out.println(in.nextLine());
//            }

            Runnable task = () -> {
                for (int i = 0; i < 10000; i++) {
                    System.out.println(i);
                }
            };
            Thread thread = new Thread(task);
            thread.start();
            thread.join();
            Runnable task1 = () -> {
                for (int i = 0; i < 100; i++) {
                    System.out.println(i);
                }
            };
            Thread thread1 = new Thread(task1);
            thread1.start();
            //thread.join(1);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @param ret hjk
     * @return hjk
     * @see <a href="https://www.baidu.com">123</a>
     */
    public static int average(int... ret) {
        int x = 1;
        for (int a : ret) {
            x += a;
        }
        return x;
    }

    private static void repeat(BiFunction<Integer, Integer, String> xc,
                               Integer x, Integer y) {
        String p = xc.apply(x, y);
        System.out.println(p);
    }

    public static class Employee implements Serializable {
        private static final long serialVersionUID = 1L;
        transient private int id;
        private String name;

        public Employee(String name, int id) {
            this.id = id;
            this.name = name;

        }
    }

}
