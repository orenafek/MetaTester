package main;

/**
 * @author Oren Afek
 * @since 3/27/2017
 */
public class TestLine extends SourceLine {
    protected TestLine(String testName, String testClassName, String content, int lineNo) {
        super(testClassName, content, lineNo);
        this.testName = testName;

    }

    public String generateTestMethod() {
        return String.format(
                "\t" + "@Test\n" +
                        "\t" + "public void %s_%d() {" + "\n" +
                        "\t" +      "%s" + "\n" +
                        "\t" + "}"

                , getTestName(), getLineNo(), getContent());
    }
}
