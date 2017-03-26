import java.util.Objects;

/**
 * Created by oren.afek on 3/26/2017.
 */
public class SourceLine {

    public static class TestLineFactory {
        private String testClassName;

        public TestLineFactory(String testName) {
            this.testClassName = testName;
        }

        public SourceLine createTestLine(String content, int lineNo) {
            return !Objects.equals(content, "") ? new SourceLine(testClassName, content.trim(), lineNo) : EMPTY;
        }

    }

    public final static SourceLine EMPTY = new SourceLine("", "", -1);
    private String testClassName;

    public static SourceLine getEMPTY() {
        return EMPTY;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    private String testName;
    protected String content;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    private int lineNo;

    protected SourceLine(String testClassName, String content, int lineNo) {
        this.testClassName = testClassName;
        this.content = content;
        this.lineNo = lineNo;
    }

    public String generateTestMethod(SourceLine sourceLine) {
        return String.format(
                "@Test\n" +
                        "public void %s_%d() {" +
                        "   %s" +
                        "}"

                , sourceLine.getTestName(), sourceLine.getLineNo(), sourceLine.getContent());
    }

    public boolean contains(String subString) {
        return this.content.contains(subString);
    }

    @Override
    public String toString() {
        return String.format("%s.%s(%d): %s", testClassName, testName, lineNo, content);
    }
}
