import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Oren Afek
 * @since 3/26/2017
 */
public class TestClassGenerator {

    private static final String FS = System.getProperty("file.separator");
    private static final String NL = System.getProperty("line.separator");
    private static final String sourcePath = makePath(System.getProperty("user.dir"), "test", "generated");
    public static final String packageName = "generated";
    private static final String classPath = makePath(System.getProperty("user.dir"), "out", "test", "MetaTester");
    public static final String JAVA_SUFFIX = ".java";

    public Class<?> generate(String testClassName, Collection<? extends SourceLine> source) {
        Collection<ImportLine> imports = source.stream()
                .filter(l -> l instanceof ImportLine).map(l -> (ImportLine) l).collect(Collectors.toList());
        Collection<TestLine> testLines = source.stream()
                .filter(l -> l instanceof TestLine).map(l -> (TestLine) l).collect(Collectors.toList());
        return loadClass(testClassName, getClassString(testLines, imports, testClassName));
    }

    private static String makePath(String... dirs) {
        StringBuilder path = new StringBuilder();
        for (String dir : dirs) {
            path.append(dir).append(FS);
        }
        return path.substring(0, path.length() - 1);
    }

    private void compileSourceCode(String className, String sourceCode) {
        FileWriter writer = null;
        String fileName = className + JAVA_SUFFIX;
        File sourceFile = new File(makePath(sourcePath, className + JAVA_SUFFIX));
        try {
            sourceFile.createNewFile();
            writer = new FileWriter(sourceFile);
            writer.write(sourceCode);
            writer.close();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
            File classFile = new File(classPath);
            classFile.createNewFile();
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(classFile));
            StringWriter out = new StringWriter();
            compiler.getTask(out, fileManager, null, null, null,
                    fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile))).call();

            fileManager.close();


        } catch (IOException ignore) {/**/}


    }

    private Class<?> loadClass(String className, String sourceCode) {
        compileSourceCode(className, sourceCode);
        File objectFile = new File(classPath);
        try {
            URL url = objectFile.toURI().toURL();
            URL[] urls = new URL[]{url};
            return new URLClassLoader(urls).loadClass(packageName + "." + className);

        } catch (IOException | ClassNotFoundException ignore) {
            ignore.printStackTrace();
        }

        return Object.class;
    }

    private static String getClassString(Collection<TestLine> testsLines,
                                         Collection<ImportLine> imports, String className) {
        return packageHeaderString(packageName) + "\n" +
                importStatementString(imports) + "\n" +
                classHeaderString(className) + "\n" +
                testMethods(testsLines) + "\n" + "}";
    }

    private static String testMethods(Collection<TestLine> testsLines) {
        return testsLines.stream().map(TestLine::generateTestMethod)
                .reduce((acc, s1) -> acc + "\n\n" + s1).orElse("");
    }


    private static String importStatementString(Collection<ImportLine> importLines) {
        return importLines.stream().reduce("", (s, importLine) -> s + importLine.getContent() + "\n",
                (s, s2) -> s + s2);
    }


    private static String classHeaderString(String className) {
        return "public class " + className + " { \n";
    }

    private static String packageHeaderString(String packageName) {
        return "package " + packageName + ";";
    }


}
