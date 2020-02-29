import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class sec5 {

    //exercise1
    public ArrayList<Double> readValues(String filename) throws IOException {
        Path path = Paths.get(filename);
        ArrayList<Double> arrayList = new ArrayList<>();
        Scanner in = new Scanner(path, StandardCharsets.UTF_8);
        while (in.hasNext()) {
            if (in.hasNextDouble()) arrayList.add(in.nextDouble());
            else {
                throw new IOException("Not Found Double");
            }
        }
        return arrayList;
    }

    //exercise2
    public double sumOfValues(String filename) throws IOException {
        ArrayList<Double> arrayList = readValues(filename);
        double sum = 0;
        for (double x : arrayList) {
            sum += x;
        }
        return sum;
    }

    //exercise3
    public void exercise3(String filename) {
        try {
            System.out.println(sumOfValues(filename));
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //exercise4
    public ArrayList<Double> readValues2(String filename) {
        Path path = Paths.get(filename);
        ArrayList<Double> arrayList = new ArrayList<>();
        try {
            Scanner in = new Scanner(path, StandardCharsets.UTF_8);
            while (in.hasNext()) {
                if (in.hasNextDouble()) arrayList.add(in.nextDouble());
                else return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return arrayList;
    }

    public double sumOfValues2(String filename) {
        ArrayList<Double> arrayList = readValues2(filename);
        if (arrayList == null) return Double.NaN;
        double sum = 0;
        for (double x : arrayList) {
            sum += x;
        }
        return sum;
    }

    //exercise5
    public void exercise5(String read, String write) throws IOException, RuntimeException {
        Scanner in = null;
        PrintWriter out = null;
        try {
            in = new Scanner(Paths.get(read));
            out = new PrintWriter(write);
            while (in.hasNext()) {
                out.println(in.next().toLowerCase());
            }
            out.close();
            in.close();
        } catch (IOException e) {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                throw e;
            } catch (RuntimeException e1) {
                e.addSuppressed(e1);
                throw e;
            }
        } catch (NullPointerException e) {
            throw e;
        } catch (RuntimeException e) {
            try {
                out.close();
                in.close();
                throw e;
            } catch (RuntimeException e1) {
                e.addSuppressed(e1);
                throw e;
            }
        }
    }

    //exercise6
    void exercise6_1(Path path) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Caught IOException: " + ex.getMessage());
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void exercise6_2(Path path) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("Caught IOException: " + ex.getMessage());
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void exercise6_3(Path path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            int n = bufferedReader.read();
        } catch (IOException ex) {
            System.out.println("Caught IOException: " + ex.getMessage());
        }
    }

    //exercise7
    /*
    第二段代码把变量初始化放在了try的外面，如果构造方法产生异常，将无法捕获。
     */

    //exercise8
    /*
    在关闭资源时，如果该资源实现closable接口，则调用该方法。若发生异常，则把异常赋予
    lastException变量，并修改私有变量，表示资源关闭。
    在try-with=resource中，如果try中发生异常，则调用close方法。若关闭资源产生异常，则将
    此异常lastException抑制，附加到原先引发的异常上。
     */

    //exercise9
    public AutoCloseable lock(ReentrantLock reentrantLock) {
        reentrantLock.lock();
        return new AutoCloseable() {
            @Override
            public void close() throws Exception {
                try {
                    reentrantLock.unlock();
                } catch (IllegalMonitorStateException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //exercise10
    /*
    会抛出未检查异常。
    因为构造函数引起的异常一般是由于资源的查询或权限错误，这让初学者尽早的发现问题，而不是
    调用方法时才去解决。
     */

    //exercise11
    public int factorial(int n) {
        if (n == 1) {
            Exception e = new ClassNotFoundException();
            e.printStackTrace();
            return 1;
        } else return n * factorial(n - 1);
    }

    //exercise12
    /*
    Objects.requireNonNull抛出一个NullPointException异常，通过查看栈踪迹，可以发现
    是哪里出了问题。
    assert obj!=null抛出一个AssertionError的异常。常用于在测试检查代码是否正确，并在实际
    构建可以通过类加载器禁止来消除断言，增加代码效率，而不用修改代码。
     */

    //exercise13
    public int min(int[] values) {
        if (values.length == 0) return Integer.MIN_VALUE;
        int m = values[0];
        for (int x : values) {
            if (x < m) m = x;
        }

        assert test(m, values);
        return m;
    }

    private boolean test(int x, int[] values) {
        for (int i : values) {
            if (x > i) return false;
        }
        return true;
    }
    /*
    对一个100000000大小的数组执行该函数。
    打开断言用时370ms。
    禁用断言用时295ms。
    移除断言用时289ms。
     */

    //exercise14
    Filter filter = new Filter() {
        @Override
        public boolean isLoggable(LogRecord record) {
            String m = record.getMessage();
            if (m.matches(".*性.*") || m.matches(".*毒品.*") ||
                    m.matches(".*C\\+\\+.*")) return false;
            else return true;
        }
    };

    //exercise15
    class HtmlFormatter extends Formatter {
        @Override
        public String getHead(Handler h) {
            return "<html>\n";
        }

        @Override
        public String getTail(Handler h) {
            return "</html>\n";
        }

        public HtmlFormatter() {
            super();
        }

        @Override
        public String format(LogRecord record) {
            String string = super.formatMessage(record);
            return "<p>" + string + "</p>\n";
        }
    }
}
