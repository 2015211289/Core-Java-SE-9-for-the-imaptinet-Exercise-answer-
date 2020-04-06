import jscheme.JScheme;

import javax.script.*;
import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sec14 {

    //exercise1
    static String exercise1(String content) throws IOException{
        String[] lines=content.split("\n");
        Pattern pattern = Pattern.compile("<%(.*?)%>");
        StringBuilder sb = new StringBuilder();
        sb.append("public class Jsp {\n");
        sb.append("\tpublic static String run() {\n");
        sb.append("\tString result = new String();\n");
        for(String line:lines){
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()){
                String match=matcher.group(1);
                int s=matcher.start();
                int e=matcher.end();
                if (match.startsWith("=")) {
                    sb.append("result+=");
                    sb.append("\"");
                    sb.append(line.substring(0,s));
                    sb.append("\"+");
                    sb.append(match.substring(1));
                    sb.append("+\"");
                    sb.append(line.substring(e));
                    sb.append("\"+\"\\n\";\n");
                } else {
                    sb.append(match);
                    sb.append("\n");
                }
            }else {
                sb.append("result+=\"");
                sb.append(line);
                sb.append("\"+\"\\n\";\n");
            }
        }
        sb.append("\treturn result;}\n");
        sb.append("}");

        List<StringSource> sources = List.of(new StringSource("Jsp",
                sb.toString()));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();

        List<ByteArrayClass> classes = new ArrayList<>();
        StandardJavaFileManager stdFileManager = compiler
                .getStandardFileManager(null, null, null);
        JavaFileManager fileManager = new ForwardingJavaFileManager<JavaFileManager>(
                stdFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location,
                                                       String className, JavaFileObject.Kind kind, FileObject sibling)
                    throws IOException {
                if (kind == JavaFileObject.Kind.CLASS) {
                    ByteArrayClass outfile = new ByteArrayClass(className);
                    classes.add(outfile);
                    return outfile;
                } else
                    return super.getJavaFileForOutput(location, className,
                            kind, sibling);
            }
        };
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                collector, null, null, sources);
        Boolean result = task.call();
        for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics()) {
            System.out.println(d);
        }
        System.out.println(result);
        ByteArrayClassLoader loader = new ByteArrayClassLoader(classes);
        try{
            Class<?> cl = Class.forName("Jsp", true, loader);
            Method m=cl.getDeclaredMethod("run");
            Object o=m.invoke(null);
            return (String) o;

        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    static class StringSource extends SimpleJavaFileObject {
        private String code;

        StringSource(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + ".java"),
                    Kind.SOURCE);
            this.code = code;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    static class ByteArrayClass extends SimpleJavaFileObject {
        private ByteArrayOutputStream out;

        ByteArrayClass(String name) {
            super(URI.create("bytes:///" + name.replace('.', '/') + ".class"),
                    Kind.CLASS);
        }

        public byte[] getCode() {
            return out.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            // TODO Auto-generated method stub
            out = new ByteArrayOutputStream();
            return out;
        }
    }

    static class ByteArrayClassLoader extends ClassLoader {
        private Iterable<ByteArrayClass> classes;

        public ByteArrayClassLoader(Iterable<ByteArrayClass> classes) {
            this.classes = classes;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            for (ByteArrayClass cl : classes) {
                if (cl.getName().equals("/" + name.replace('.', '/') + ".class")) {
                    byte[] bytes = cl.getCode();
                    return defineClass(name, bytes, 0, bytes.length);
                }
            }
            throw new ClassNotFoundException(name);
        }
    }

    //exercise2
    static String exercise2a(String json) throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.put("str",json);
        engine.eval("var obj=JSON.parse(str)");
        Object result=engine.eval("JSON.stringify(obj)");
        return (String) result;
    }

    static String exercise2b(String json) throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        Object j=engine.eval("JSON");
        Object result= ((Invocable) engine).invokeMethod(j,"parse",
                json);
        result= ((Invocable) engine).invokeMethod(j,"stringify",
                result);
        return (String) result;
    }

    public interface JSON{
        Object parse(String str);
        String stringify(Object obj);
    }

    static String exercise2c(String json)throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        Object j=engine.eval("JSON");
        JSON js=((Invocable) engine).getInterface(j,JSON.class);
        Object o=js.parse(json);
        return js.stringify(o);
    }

    //exercise3
    static void exercise3a() throws ScriptException{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        String js="function f(n) { return n<=1?1:n*f(n-1)}\nf(30)";
        if(engine instanceof Compilable){
            CompiledScript script = ((Compilable) engine).compile(js);
            script.eval();
        }
    }

    static void exercise3b() throws ScriptException{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        String js="function f(n) { return n<=1?1:n*f(n-1)}\nf(30)";
        engine.eval(js);
    }

    /*
    同样计算30的阶乘。编译后脚本平均用时828ms，未编译脚本平均用时838ms。差别不大。
     */

    //exercise4
    static void exercise4(){
        
    }

}
