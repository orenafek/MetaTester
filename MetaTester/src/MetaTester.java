import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.TestWithParameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oren Afek
 * @since 3/27/2017
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
        Class<?> newTestClass = new TestClassGenerator().generate("CustomTest", this.sourceLines);
        TestSuite suite = new TestSuite(newTestClass);
        TestResult result = new TestResult();
        suite.run(result);

        try {
            new BlockJUnit4ClassRunnerWithParametersFactory().createRunnerForTestWithParameters(
                    new TestWithParameters(" ", new TestClass(newTestClass), new ArrayList<>())).run(notifier);
        } catch (InitializationError ignore) {/**/}

        //Uncomment this to run the original test aswell
        /*super.runChild(method, notifier);*/
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
                if (line.contains("void") && line.contains("()")) {
                    factory.setTestMethodName(line.replace("public void ", "").replace("()", "").replace("{", "").trim());
                }
                result.add(factory.createSourceLine(line, i));
                line = linesStream.readLine();
            }

            result.remove(SourceLine.EMPTY);
            return result;

        } catch (IOException ignore) {/**/}

        return new ArrayList<>();
    }


}
