/**
 * Created by oren.afek on 3/27/2017.
 */
public class TestLine extends SourceLine {
    protected TestLine(String testClassName, String content, int lineNo) {
        super(testClassName, content, lineNo);
    }

    public String generateTestMethod() {
        return String.format(
                "@Test\n" +
                        "public void %s_%d() {" +
                        "   %s" +
                        "}"

                , getTestName(), getLineNo(), getContent());
    }
}
