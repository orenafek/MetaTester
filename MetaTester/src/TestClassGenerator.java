import java.util.Collection;

/**
 * Created by oren.afek on 3/26/2017.
 */
public class TestClassGenerator {

    public Class<?> generate(Collection<? extends SourceLine> source) {

    }

    private void compileSourceCode(String sourceCode) {

    }

    private enum SourceCodeParts {
        ;

        private static String getClassString(Collection<ImportLine> imports, String className) {
            return importStatementString(imports) + classHeaderString(className) + "\n" + "}";


        private static String importStatementString(Collection<ImportLine> importLines) {
            return importLines.stream().reduce("", (s, importLine) -> s + importLine.getContent() + "\n",
                    (s, s2) -> s + s2);
        }


        private static String classHeaderString(String className) {
            return "public class " + className + " { \n";
        }


    }
}
