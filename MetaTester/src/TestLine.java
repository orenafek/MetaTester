/**
 * Created by oren.afek on 3/27/2017.
 */
public class TestLine extends SourceLine {
    protected TestLine(String testClassName, String content, int lineNo) {
        super(testClassName, content, lineNo);
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
