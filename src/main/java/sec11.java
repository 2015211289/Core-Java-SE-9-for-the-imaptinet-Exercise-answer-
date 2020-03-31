import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class sec11 {

    //exercise1
    /*
    在Object.clone方法中，获取该类的注解。如果有@Cloneable，则执行操作，否则返回异常。
    此时，@cloneable应该是类注解，并且是运行时可见。
     */

    //exercise2
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Serializable {
    }

    @Serializable
    static class Test {
        int id;
        @Transinet
        float name;
        public Test2 test2;

        public Test(int a, int b) {
            id = a;
            name = b;
        }

        public Test() {
        }
    }

    @Serializable
    static class Test2 {
        int year;
        public Test test;

        public Test2(int a) {
            year = a;
        }

        public Test2() {
        }
    }

    static public class Serialize {
        private static Map<Integer, Object> map = new HashMap<>();
        private static Map<Integer, Object> inMap = new HashMap<>();

        public static void serialize(Object obj, PrintWriter out) {
            if (obj == null) return;
            Class<?> cl = obj.getClass();
            Serializable se = cl.getAnnotation(Serializable.class);
            if (se == null) {
                out.println(obj);
                return;
            }
            if (map.containsKey(obj.hashCode())) {
                out.println(obj.hashCode());
                return;
            }
            out.println(cl.getName() + " " + obj.hashCode());
            map.put(obj.hashCode(), obj);

            for (Field f : cl.getDeclaredFields()) {
                Transinet tr = f.getAnnotation(Transinet.class);
                if (tr == null) {
                    f.setAccessible(true);
                    out.println(f.getName());
                    try {
                        Serialize.serialize(f.get(obj), out);
                    } catch (ReflectiveOperationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            out.println();
        }

        public static Object antiSerialize(Scanner in) throws Exception {
            String[] typeAndId = in.nextLine().split(" ");
            if (typeAndId.length == 1)
                return inMap.get(Integer.valueOf(typeAndId[0]));
            Class<?> cl = Class.forName(typeAndId[0]);
            int hashcode = Integer.valueOf(typeAndId[1]);
            Object obj = cl.getDeclaredConstructor().newInstance();
            inMap.put(hashcode, obj);
            String s = in.nextLine();
            while (!s.equals("")) {
                Field f = cl.getDeclaredField(s);
                Class<?> c = f.getType();
                if (c == int.class) {
                    int i = Integer.valueOf(in.nextLine());
                    f.set(obj, i);
                } else if (c == char.class) {
                    f.set(obj, in.next().charAt(0));
                } else if (c == boolean.class) {
                    boolean i = Boolean.valueOf(in.nextLine());
                    f.set(obj, i);
                } else if (c == float.class) {
                    float i = Float.valueOf(in.nextLine());
                    f.set(obj, i);
                } else if (c == double.class) {
                    double i = Double.valueOf(in.nextLine());
                    f.set(obj, i);
                } else if (c == byte.class) {
                    byte i = Byte.valueOf(in.nextLine());
                    f.set(obj, i);
                } else if (c == short.class) {
                    short i = Short.valueOf(in.nextLine());
                    f.set(obj, i);
                } else if (c == long.class) {
                    long i = Long.valueOf(in.nextLine());
                    f.set(obj, i);
                } else {
                    Object n = antiSerialize(in);
                    f.set(obj, n);
                }
                s = in.nextLine();
            }
            return obj;
        }
    }
    /*
    实现了对于域为8种基本类型和其他可序列化类型的序列化操作和反序列化操作。
    使用文本文件记录序列化对象，使用Hashcode来避免循环引用和引用唯一性。
    但注意，对于每个注解@Serializable的类，需要提供无参数构造函数。
     */

    //exercise3
    /*
    已在exercise2上修改。
     */

    //exercise4
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transinet {
    }
    /*
    已在exercise2上修改。
     */

    //exercise5
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @Repeatable(Todos.class)
    public @interface Todo {
        String message();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Todos {
        Todo[] value();
    }

    @Todo(message = "Please complete this method")
    @Todo(message = "That'Ok, ignore it")
    void exercise5(int a, int b) {

    }

    @SupportedAnnotationTypes("sec11.Todo")
    @SupportedSourceVersion(SourceVersion.RELEASE_12)
    public class TodoAnnotationProcessor extends AbstractProcessor {
        @Override
        public boolean process(Set<? extends TypeElement> annotations,
                               RoundEnvironment roundEnv) {
            if (annotations.size() == 0) return true;
            try {
                try (PrintWriter out = new PrintWriter(Files.newBufferedWriter
                        (Paths.get("Todo_list"), StandardCharsets.UTF_8))) {
                    out.println("Automatically generated by TodoProcessor");
                    out.println("Todo items:");

                    for (Element e : roundEnv.getElementsAnnotatedWith(Todo.class)) {
                        if (e instanceof ExecutableElement) {
                            ExecutableElement ex = (ExecutableElement) e;
                            Todo[] todos = ex.getAnnotationsByType(Todo.class);
                            for (Todo t : todos) {
                                out.println(t.message());
                            }
                            out.println(ex.getSimpleName().toString());
                        }
                    }
                }
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        ex.getMessage());
            }
            return true;
        }
    }
    /*
    要先把TodoAnnotationProcessor类单独放在一个java文件中，编译，然后输入命令：
    javac -processor TodoAnnotationProcessor sec11.java
    即可生成Todo list。
     */

    //exercise6
    /*
    在exercise5上修改。
     */

    //exercise7
    @Repeatable(Params.class)
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Param {
        String description();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Params {
        Param[] value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Return {
        String description();
    }


    @Param(description = "a, one variable")
    @Param(description = "b, one variable")
    @Return(description = "return 0")
    int exercise7(int a, int b) {
        return 0;
    }


    public class JdocAnnotationProcessor extends AbstractProcessor {
        @Override
        public boolean process(Set<? extends TypeElement> annotations,
                               RoundEnvironment roundEnv) {
            if (annotations.size() == 0) return true;
            try {
                try (PrintWriter out = new PrintWriter(Files.newBufferedWriter
                        (Paths.get("javadoc.html"), StandardCharsets.UTF_8))) {
                    out.println("Automatically generated by jDocProcessor");
                    out.println("<html><body>");

                    Set<Class<? extends Annotation>> set = new HashSet<>();
                    set.add(Param.class);
                    set.add(Return.class);
                    for (Element e :
                            roundEnv.getElementsAnnotatedWithAny(set)) {
                        if (e instanceof ExecutableElement) {
                            ExecutableElement ex = (ExecutableElement) e;
                            Param[] params = ex.getAnnotationsByType(Param.class);
                            for (Param p : params) {
                                out.println("<p>" + p.description() + "</p>");
                            }
                            Return re = ex.getAnnotation(Return.class);
                            if (re != null)
                                out.println("<p>" + re.description() +
                                        "</p>");
                            out.println("<h4>" + ex.getSimpleName().toString() +
                                    "</h4>");
                        }
                    }
                    out.println("</body></html>");
                }
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        ex.getMessage());
            }
            return true;
        }
    }

    //exercise8
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @Repeatable(TestCases.class)
    public @interface TestCase {
        String params();

        int expected();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface TestCases {
        TestCase[] value();
    }

    @TestCase(params = "1,2", expected = 3)
    @TestCase(params = "1,7", expected = 8)
    public static int exercise8(int a, int b) {
        return a + b;
    }

    public class TestAnnotationProcessor extends AbstractProcessor {
        @Override
        public boolean process(Set<? extends TypeElement> annotations,
                               RoundEnvironment roundEnv) {
            if (annotations.size() == 0) return true;
            try {
                JavaFileObject sourceFile = processingEnv.getFiler().
                        createSourceFile("Test.java");
                try (PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
                    out.println("//Automatically generated by TestProcessor");
                    out.println("public class Test {");
                    out.println("\tpublic static void main(String[] args) {");

                    for (Element e :
                            roundEnv.getElementsAnnotatedWith(TestCase.class)) {
                        if (e instanceof ExecutableElement) {
                            ExecutableElement ex = (ExecutableElement) e;
                            TestCase[] cases =
                                    ex.getAnnotationsByType(TestCase.class);
                            for (TestCase t : cases) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("assert(");
                                sb.append(roundEnv.getRootElements().getClass().toString());
                                sb.append("." + ex.getSimpleName().toString() + "(");
                                String[] params = t.params().split(",");
                                for (int i = 0; i < params.length - 1; i++) {
                                    sb.append(params[i] + ",");
                                }
                                sb.append(params[params.length - 1]);
                                sb.append(") == ");
                                sb.append(t.expected() + ");");
                                out.println(sb.toString());
                            }

                            out.println("\t}");
                            out.println("}");
                        }
                    }
                }
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        ex.getMessage());
            }
            return true;
        }
    }

    //exercise9
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Testcases.class)
    public @interface Testcase {
        String params();

        String expected();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Testcases {
        Testcase[] value();
    }

    @Testcase(params = "hello, world", expected = "hello world")
    @Testcase(params = "thank, you", expected = "thank you")
    public static String exercise9(String a, String b) {
        return a + b;
    }

    static public class toTest {
        public static boolean toTest(Method m) {
            if (m == null) return false;
            Testcase[] cases = m.getDeclaredAnnotationsByType(Testcase.class);
            if (cases.length == 0) return true;
            for (Testcase testcase : cases) {
                String[] params = testcase.params().split(",");
                Class<?>[] types = m.getParameterTypes();
                Object[] p = new Object[params.length];
                for (int j = 0; j < types.length; j++) {
                    Class<?> c = types[j];
                    if (c == int.class) {
                        int i = Integer.valueOf(params[j]);
                        p[j] = i;
                    } else if (c == char.class) {
                        p[j] = params[j].charAt(0);
                    } else if (c == boolean.class) {
                        boolean i = Boolean.valueOf(params[j]);
                        p[j] = i;
                    } else if (c == float.class) {
                        float i = Float.valueOf(params[j]);
                        p[j] = i;
                    } else if (c == double.class) {
                        double i = Double.valueOf(params[j]);
                        p[j] = i;
                    } else if (c == byte.class) {
                        byte i = Byte.valueOf(params[j]);
                        p[j] = i;
                    } else if (c == short.class) {
                        short i = Short.valueOf(params[j]);
                        p[j] = i;
                    } else if (c == long.class) {
                        long i = Long.valueOf(params[j]);
                        p[j] = i;
                    } else {
                        p[j] = params[j];
                    }
                }
                try {
                    Object r = m.invoke(null, p);
                    if (!r.toString().equals(testcase.expected())) return false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return true;
        }
    }
    /*
    要求返回值类型重写了toString()方法（或者使用Object类的方法），要求结果的toString()
    与expect字符串相等。
    参数类型范围有基本类型加上String。
     */

    //exercise10
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Resource {
        String name();
    }

    static class Source {
        @Resource(name = "http://horstmann.com")
        public String source;

        public Source() {
        }
    }

    static public class LoadResource {
        public static void load(Object obj) {
            if (obj == null) return;
            Class<?> cl = obj.getClass();
            for (Field f : cl.getDeclaredFields()) {
                Resource re = f.getAnnotation(Resource.class);
                String result = null;
                if (re != null) {
                    try {
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI(re.name())).GET().build();
                        HttpResponse<String> response = client.send(request,
                                HttpResponse.BodyHandlers.ofString());
                        String body = response.body();
                        result = body;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        f.set(obj, result);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    /*
    实现只处理http协议的URL地址。
     */
}
