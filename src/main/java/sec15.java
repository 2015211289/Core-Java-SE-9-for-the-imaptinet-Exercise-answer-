import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class sec15 {

    //exercise1
    /*
    module,requires,to,opens可以作为类名，包名和模块名。
    transitive也可以作为模块名。
     */

    //exercise2
    /*
    编译时错误，提示该包没有exports。
     */

    //exercise3
    /*
    需要加上：
    requires java.logging;
     */

    //exercise4
    /*
    无法访问未命名模块。
    需要将JAR包放入模块路径下，成为自动模块，然后添加requires。
     */

    //exercise5
    void exercise5() throws Exception {
        Driver driver = new Driver() {
            @Override
            public Connection connect(String url, Properties info) throws SQLException {
                return null;
            }

            @Override
            public boolean acceptsURL(String url) throws SQLException {
                return false;
            }

            @Override
            public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
                return new DriverPropertyInfo[0];
            }

            @Override
            public int getMajorVersion() {
                return 0;
            }

            @Override
            public int getMinorVersion() {
                return 0;
            }

            @Override
            public boolean jdbcCompliant() {
                return false;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return Logger.getGlobal();
            }
        };
        Logger logger = driver.getParentLogger();
    }

    //exercise6
    @sec11.Todo(message = "假设这个注释是在别的模块声明的")
    void exercise6() throws Throwable {
        try {
            new oracle.jbdc.driver.OracleDriver();
        } catch (NoClassDefFoundError er) {
            new String();
        }
    }
    /*
    requires transitive static对于static的第一种情况，是不需要的transitive，因为使用
    模块不需要有该注释，否则他应该直接引用。对于第二种情况，根本不需要transitive，因为使用
    模块没有用到。
    所以不会考虑requires transitive static。
     */

    //exercise7
    /*
    编译能通过，但运行时发生异常java.util.ServiceConfigurationError。
    因为服务加载器是动态加载的，所以会在运行时检测。
     */

    //exercise8
    /*
    代码略。
    改进完则不需要provides，直接exports服务提供者工厂类的包。
     */

    //exercise9
    /*
    实现服务的模块使用requires transitive 引用服务模块，然后消费者引用实现服务的模块。
    */

    //exercise10
    /*
    使用命令 jdeps -s *.jar

    hamcrest-core-1.3.jar -> java.base
    jcommon-1.0.23.jar -> java.base
    jcommon-1.0.23.jar -> java.datatransfer
    jcommon-1.0.23.jar -> java.desktop
    jfreechart-1.0.19-experimental.jar -> java.base
    jfreechart-1.0.19-experimental.jar -> java.desktop
    jfreechart-1.0.19-experimental.jar -> jcommon-1.0.23.jar
    jfreechart-1.0.19-experimental.jar -> jfreechart-1.0.19.jar
    jfreechart-1.0.19-swt.jar -> java.base
    jfreechart-1.0.19-swt.jar -> java.desktop
    jfreechart-1.0.19-swt.jar -> jcommon-1.0.23.jar
    jfreechart-1.0.19-swt.jar -> jfreechart-1.0.19.jar
    jfreechart-1.0.19-swt.jar -> not found
    jfreechart-1.0.19.jar -> java.base
    jfreechart-1.0.19.jar -> java.datatransfer
    jfreechart-1.0.19.jar -> java.desktop
    jfreechart-1.0.19.jar -> java.sql
    jfreechart-1.0.19.jar -> java.xml
    jfreechart-1.0.19.jar -> jcommon-1.0.23.jar
    jfreechart-1.0.19.jar -> servlet.jar
    jfreesvg-2.0.jar -> java.base
    jfreesvg-2.0.jar -> java.desktop
    jfreesvg-2.0.jar -> java.logging
    jfreesvg-2.0.jar -> not found
    junit-4.11.jar -> hamcrest-core-1.3.jar
    junit-4.11.jar -> java.base
    orsoncharts-1.4-eval-nofx.jar -> java.base
    orsoncharts-1.4-eval-nofx.jar -> java.desktop
    orsoncharts-1.4-eval-nofx.jar -> java.logging
    orsonpdf-1.6-eval.jar -> java.base
    orsonpdf-1.6-eval.jar -> java.desktop
    orsonpdf-1.6-eval.jar -> java.logging
    servlet.jar -> java.base
    swtgraphics2d.jar -> java.base
    swtgraphics2d.jar -> java.desktop
    swtgraphics2d.jar -> jfreechart-1.0.19.jar
    swtgraphics2d.jar -> not found

    jdeps -s jfreechart-1.0.19-demo.jar

    jfreechart-1.0.19-demo.jar -> java.base
    jfreechart-1.0.19-demo.jar -> java.datatransfer
    jfreechart-1.0.19-demo.jar -> java.desktop
    jfreechart-1.0.19-demo.jar -> java.logging
    jfreechart-1.0.19-demo.jar -> java.sql
    jfreechart-1.0.19-demo.jar -> not found
     */

    //exercise11
    /*
    在示例文件中加入module-info.java文件并重新编译，则构成一个模块化的JAR。
    lib目录的JAR文件会自动模块。
    */

    //exercise12
    /*
    发现自动模块不能用做jlink操作.具体原因是自动模块和非命名模块会引入所有的模块，这样的话
    使得jlink操作没有意义，因为没有最小的集合捆绑。
     */

    //exercise13
    /*
    JavaFx SceneBuilder一般采用集成开发环境来运行程序。所以不会用命令行启动程序。
     */
}
