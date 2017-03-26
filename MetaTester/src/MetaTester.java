import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by oren.afek on 3/26/2017.
 */
public class MetaTester extends BlockJUnit4ClassRunner {

    private Class<?> testClass;
    private File sourceFile;
    private List<String> sourceLines;

    public MetaTester(Class<?> clazz) throws InitializationError {
        super(clazz);
        this.testClass = clazz;
        this.sourceFile = openSourceFile(this.testClass.getSimpleName());
        this.sourceLines = readAllLines();
    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(testClass, "MetaTester");
    }

    @Override
    public void run(RunNotifier runNotifier) {
        runNotifier.addListener(new RunListener() {
            @Override
            public void testStarted(Description description) throws Exception {

            }
        });

        super.run(runNotifier);
    }

    private File openSourceFile(String className) {
        String path = Paths.get("").toAbsolutePath().toString();
        sourceFile = new File(String.format("%s\\%s.java", path, className));
        return sourceFile;
    }

    private List<String> readAllLines(){
        try {
            BufferedReader linesStream = new BufferedReader(new FileReader(sourceFile));
            String line = linesStream.readLine();
            List<String> l = new ArrayList<>();
            while(line != null) {
                line = linesStream.readLine();
                l.add(line);
            }

            return l;

        } catch (IOException ignore) {}

        return new ArrayList<>();
    }

    private Stream<String> onlyAss
    private void x(){
        TestSuite suite = new TestSuite();
        TestClass c = new TestClass(Object.class);

        suite.addTest(new JUnit4TestAdapter(Object.class)); // new ...(Test1.class)
        suite.addTest(new JUnit4TestAdapter(Object.class));
    }
}
