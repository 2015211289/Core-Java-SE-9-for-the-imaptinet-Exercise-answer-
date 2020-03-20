import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class sec9 {

    //exercise1
    void exercise1(InputStream in, OutputStream out) throws IOException {
        try (in; out) {
            in.transferTo(out);
        }
    }

    void exercise1_(InputStream in, OutputStream out) throws IOException {
        Files.copy(in, Paths.get("temp"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("temp"), out);
    }

    //exercise2
    void exercise2(String filename) throws IOException {
        try (Scanner in = new Scanner(Paths.get(filename),
                StandardCharsets.UTF_8);
             PrintWriter out =
                     new PrintWriter((Files.newBufferedWriter(Paths.get(filename + ".toc"),
                             StandardCharsets.UTF_8)))) {
            int line = 0;
            while (in.hasNextLine()) {
                String[] strings = in.nextLine().split("\\PL+");
                line++;
                for (String s : strings) {
                    out.println(s + ":" + line);
                }
            }
        }
    }

    //exercise3
    static void exercise3(String filename) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename));
             Reader in = new InputStreamReader(inputStream, StandardCharsets.US_ASCII)) {
            int code = in.read();
            while (code != -1) {
                if (code > 127) {
                    System.out.println("No ASCII");
                    break;
                }
                code = in.read();
            }
            if (code == -1) {
                System.out.println("ASCII");
            }
        }
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename));
             Reader in = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1)) {
            int code = in.read();
            while (code != -1) {
                if (code > 255) {
                    System.out.println("No ISO_8859_1");
                    break;
                }
                code = in.read();
            }
            if (code == -1) {
                System.out.println("ISO_8859_1");
            }
        }
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename));
             Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            int code = in.read();
            while (code != -1) {
                if (code > 127) {
                    System.out.println("No UTF_8");
                    break;
                }
                code = in.read();
            }
            if (code == -1) {
                System.out.println("UTF_8");
            }
        }
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename));
             Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_16BE)) {
            int code = in.read();
            while (code != -1) {
                if (code > 127) {
                    System.out.println("No UTF_16BE");
                    break;
                }
                code = in.read();
            }
            if (code == -1) {
                System.out.println("UTF_16BE");
            }
        }
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename));
             Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_16LE)) {
            int code = in.read();
            while (code != -1) {
                if (code / 256 != 0 || code > 32768) {
                    System.out.println("No UTF_16LE");
                    break;
                }
                code = in.read();
            }
            if (code == -1) {
                System.out.println("UTF_16LE");
            }
        }
    }

    //exercise4
    static int exercise4a(String filename) throws IOException {
        try (Scanner in = new Scanner(Paths.get(filename),
                StandardCharsets.UTF_8)) {
            int line = 0;
            while (in.hasNextLine()) {
                in.nextLine();
                line++;
            }
            return line;
        }
    }

    static int exercise4b(String filename) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            int line = 0;
            while (reader.readLine() != null) line++;
            return line;
        }
    }

    static int exercise4c(String filename) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            return (int) reader.lines().count();
        }
    }
    /*
    读取一个13.4MB的大文件
    a: 1781ms; b: 426ms; c: 348ms。 c方式最快，同时c也是最方便的。
     */

    //exercise5
    void exercise5() {
        CharsetEncoder charsetEncoder = StandardCharsets.UTF_16.newEncoder();
        byte[] bytes = charsetEncoder.replacement();
        System.out.println("UTF_16" + new String(bytes) +
                "UTF_8" + new String(StandardCharsets.UTF_8.newEncoder()
                .replacement()) +
                "US_ASCII" + new String(StandardCharsets.US_ASCII.newEncoder()
                .replacement()) +
                "ISO_8859_1" + new String(StandardCharsets.ISO_8859_1.newEncoder()
                .replacement()));
    }

    //exercise6
    static void exercise6(String filename) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            file.seek(file.getFilePointer() + 18);
            byte[] bytes = new byte[4];
            file.readFully(bytes);
            int width =
                    ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
            file.seek(file.getFilePointer() + 6);
            file.readFully(bytes, 0, 2);
            short Bits =
                    ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
            if (Bits >= 8) file.seek(file.getFilePointer() + 24);
            else {
                if (Bits == 1) file.seek(file.getFilePointer() + 28);
                else file.seek(file.getFilePointer() + 24 + (long) Math.pow(
                        2, Bits) * 4);
            }
            int len;
            if ((Bits * width / 8) % 4 != 0)
                len = 4 - (Bits * width / 8) % 4 + (Bits * width / 8);
            else len = (Bits * width / 8);
            byte[] pixels = new byte[len];
            int result = file.read(pixels);
            while (result != -1) {
                result = file.read(pixels);
            }
        }
    }
    /*
    注意，windows下BMP文件是小端的，而且像素值是从底向上保存，RGB是向后保存，即BGR。
     */

    //exercise7
    static void exercise7(String filename) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            byte[] bytes = new byte[(int) file.length()];
            file.read(bytes);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(byteBuffer);
            byte[] Digest = messageDigest.digest();
            for (byte s : Digest)
                System.out.printf("%X%X", (s + 256) / 16, (s + 256) % 16);
        }
    }
    /*
    补码负数加上256，变成绝对值。
    或者用Byte.toUnsignedInt
     */

    //exercise8
    static void exercise8(String filepath) throws Exception {
        Path zipPath = Paths.get("myfile.zip");
        URI uri = new URI("jar", zipPath.toUri().toString(), null);
        Path sourcepath = Paths.get(filepath);
        try (FileSystem zipfs = FileSystems.newFileSystem(uri,
                Collections.singletonMap("create", "true"));
             Stream<Path> entries = Files.walk(sourcepath)) {

            entries.forEach(p -> {
                try {
                    Path q = zipfs.getPath("/").resolve(p.toString());
                    if (Files.isDirectory(p))
                        Files.createDirectory(q);
                    else
                        Files.copy(p, q);
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            });
        }
    }

    //exercise9
    static void exericse9(String website, String username, String password)
            throws Exception {
        URL url = new URL(website);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        String input = username + ":" + password;
        String encoding =
                Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encoding);
        connection.setRequestProperty("Accept-Charset", "UTF_8");
        try (InputStream in = connection.getInputStream()) {
            byte[] content = in.readAllBytes();
            System.out.println(new String(content));
        }
    }

    //exercise10
    static ArrayList<Integer> exercise10a() {
        String string = "+ 10 - 10 + 25 + 94 - 15";
        String regex = "([-+])\\s+(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        ArrayList<Integer> arrayList = new ArrayList<>();
        while (matcher.find()) {
            String mark = matcher.group(1);
            String number = matcher.group(2);
            int n = Integer.valueOf(number);
            if (mark.equals("-")) n = -n;
            arrayList.add(n);
        }
        return arrayList;
    }

    static ArrayList<Integer> exercise10b() {
        String string = "+ 10 - 10 + 25 + 94 - 15";
        String regex = "\\s";
        Pattern pattern = Pattern.compile(regex);
        String[] tokens = pattern.split(string);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("-")) {
                arrayList.add(-Integer.valueOf(tokens[i + 1]));
            } else if (tokens[i].equals("+")) {
                arrayList.add(Integer.valueOf(tokens[i + 1]));
            }
        }
        return arrayList;
    }

    //exercise11
    static String[] exercise11(String path) {
        String[] result = new String[3];
        String regex = "(.*/)(.+)\\.(.+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            result[0] = matcher.group(1);
            result[1] = matcher.group(2);
            result[2] = matcher.group(3);
        }
        return result;
    }

    //exercise12
    /*
    实现9.4.4例子
     */
    static void exercise12() {
        String input = "Blackwell Toaster USD29.95";

        Pattern pattern = Pattern.compile("(\\p{Alnum}+(\\s+\\p{Alnum}+)*)" +
                "\\s+" +
                "([A-Z]{3})([0-9.]*)");
        Matcher matcher = pattern.matcher(input);
        String result = matcher.replaceAll("items: $1\ncurrency: $3\nprice: $4");
        System.out.println(result);
    }

    //exercise13
    static <T> T exercise13(T object) throws IOException {
        if (object instanceof Serializable) {
            byte[] bytes;
            try (ByteArrayOutputStream bout = new ByteArrayOutputStream();
                 ObjectOutputStream out =
                         new ObjectOutputStream(bout);) {
                out.writeObject(object);
                bytes = bout.toByteArray();
            }
            try (ObjectInputStream in =
                         new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                @SuppressWarnings("unchecked")
                T t = (T) in.readObject();
                return t;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }

    //exercise14
    static class Point implements Serializable {
        double x, y;
        private static final long serialVersionUID = -1118387579088780090L;

        public Point(double a, double b) {
            x = a;
            y = b;
        }
    }

    static void exercise14() {
        Point[] points = new Point[2];
        points[0] = new Point(1, 1);
        points[1] = new Point(2, 2);
        Path path = Paths.get("Serializable");
        try (ObjectOutputStream out =
                     new ObjectOutputStream(Files.newOutputStream(path));
             ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            for (Point p : points) {
                out.writeObject(p);
            }
            for (int i = 0; i < points.length; i++)
                points[i] = (Point) in.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //exercise15
    /*
    把x，y的类型从int改为double
     */
    static void exercise15() {
        Path path = Paths.get("Serializable");
        Point[] points = new Point[2];
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            for (int i = 0; i < points.length; i++)
                points[i] = (Point) in.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*
    当新版本读取旧版本的文件，抛出InvalidClassException异常。
    如果修正了serialVersionUID，则会抛出InvalidClassException; incompatible types for
    field x.
    如果需要兼容新旧版本，需要对应实例变量的类型相同。
     */

    //exercise16
    /*
    Externalizable: DataFlavor, MLet, PrivateMLet
    writeReplace: CertPath, Certificate
    readResolve: AWTKeyStroke, ICC_Profile, TextAttribute, KepRep, CertPathREp,
    CertificateREp, Attribute, Field, SimpleType, EnumSyntax
     */
}
