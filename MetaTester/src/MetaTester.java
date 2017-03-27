import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
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
    private String testName;
    private List<SourceLine> sourceLines;

    public MetaTester(Class<?> clazz) throws InitializationError {
        super(clazz);
        this.testClass = clazz;
        this.testName = this.testClass.getSimpleName();
        this.sourceFile = openSourceFile(testName);
        this.sourceLines = readAllLines(testName);

    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(testClass, "MetaTester");
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        getAssertLines(getTestLines(method.getName())).forEach(System.out::println);
        Class<?> newTestClass = new TestClassGenerator().generate("CustomTest", this.sourceLines);
        try {
            Object o = newTestClass.newInstance();
            System.out.println(o.getClass());
        } catch (InstantiationException | IllegalAccessException ignore) {/**/}
        super.runChild(method, notifier);
    }

    private File openSourceFile(String className) {
        String path = Paths.get("").toAbsolutePath().toString();
        sourceFile = new File(String.format("%s\\test\\%s.java", path, className));
        return sourceFile;
    }

    private List<SourceLine> readAllLines(String testName) {
        List<SourceLine> result = new ArrayList<>();
        try {
            BufferedReader linesStream = new BufferedReader(new FileReader(sourceFile));
            String line = linesStream.readLine();
            SourceLine.SourceLineFactory factory = new SourceLine.SourceLineFactory(testName);
            for (int i = 1; line != null; i++) {
                result.add(factory.createSourceLine(line, i));
                line = linesStream.readLine();
            }

            result.remove(SourceLine.EMPTY);
            return result;

        } catch (IOException ignore) {/**/}

        return new ArrayList<>();
    }

    private SourceLine getEndOfMethodPosition(SourceLine start) {
        int paren = 0;
        for (int i = start.getLineNo(); i <= this.sourceLines.size(); ++i) {
            String line = this.sourceLines.get(i).getContent();
            paren += line.contains("{") ? 1 : line.contains("}") ? -1 : 0;
            if (paren <= 0) {
                return this.sourceLines.get(i);
            }
        }

        return SourceLine.EMPTY;

    }

    private Stream<SourceLine> getTestLines(String testName) {
        sourceLines.forEach(sourceLine -> sourceLine.setTestName(testName));
        SourceLine start =
                sourceLines.stream().filter(line -> line.contains("public void " + testName + "(){"))
                        .findFirst().orElse(SourceLine.EMPTY);


        return this.sourceLines.subList(start.getLineNo(), getEndOfMethodPosition(start).getLineNo()).stream();

    }

    private Stream<SourceLine> getAssertLines(Stream<SourceLine> testLines) {
        return testLines.filter(l -> l.contains("assert"));
    }


    private void x() {
        TestSuite suite = new TestSuite();
        TestClass c = new TestClass(Object.class);

        suite.addTest(new JUnit4TestAdapter(Object.class)); // new ...(Test1.class)
        suite.addTest(new JUnit4TestAdapter(Object.class));
    }
}
