package main;

import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oren Afek
 * @since 5/17/2017.
 */
public class AssertJTransformations {

    /*

    SED_OPTIONS=-i
    FILES_PATTERN=${1:-*Test.java}
    # echo "SED_OPTIONS = ${SED_OPTIONS}"

    echo ''
    echo "Converting JUnit assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN"
    echo ''
    echo ' 1 - Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*0,[[:blank:]]*\(.*\).size())/assertThat(\2).as(\1).isEmpty()/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*0,[[:blank:]]*\(.*\).size())/assertThat(\1).isEmpty()/g' '{}' \;

    echo ' 2 - Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size())/assertThat(\3).as(\1).hasSize(\2)/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size())/assertThat(\2).hasSize(\1)/g' '{}' \;

    echo ' 3 - Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isCloseTo(\2, within(\4))/g' '{}' \;
    # must be done before assertEquals("description", expected, actual) -> assertThat(actual).as("description").isEqualTo(expected)
    # will only replace triplets without double quote to avoid matching : assertEquals("description", expected, actual)
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\))/assertThat(\2).isCloseTo(\1, within(\3))/g' '{}' \;

    echo ' 4 - Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isEqualTo(\2)/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g' '{}' \;

    echo ' 5 - Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertArrayEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isEqualTo(\2)/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertArrayEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g' '{}' \;

    echo ' 6 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNull(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isNull()/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNull([[:blank:]]*\(.*\))/assertThat(\1).isNull()/g' '{}' \;

    echo ' 7 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotNull(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isNotNull()/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotNull([[:blank:]]*\(.*\))/assertThat(\1).isNotNull()/g' '{}' \;

    echo ' 8 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertTrue(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isTrue()/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertTrue([[:blank:]]*\(.*\))/assertThat(\1).isTrue()/g' '{}' \;

    echo ' 9 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertFalse(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isFalse()/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertFalse([[:blank:]]*\(.*\))/assertThat(\1).isFalse()/g' '{}' \;

    echo '10 - Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertSame(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isSameAs(\2)/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isSameAs(\1)/g' '{}' \;

    echo '11 - Replacing : assertNotSame(expected, actual) ................ by : assertThat(actual).isNotSameAs(expected)'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotSame(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isNotSameAs(\2)/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isNotSameAs(\1)/g' '{}' \;

    echo ''
    echo '12 - Replacing JUnit static imports by AssertJ ones, at this point you will probably need to :'
    echo '12 --- optimize imports with your IDE to remove unused imports'
    echo '12 --- add "import static org.assertj.core.api.Assertions.within;" if you were using JUnit number assertions with deltas'
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.junit.Assert.assertEquals;/import static org.assertj.core.api.Assertions.assertThat;/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.junit.Assert.fail;/import static org.assertj.core.api.Assertions.fail;/g' '{}' \;
    find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.junit.Assert.\*;/import static org.assertj.core.api.Assertions.*;/g' '{}' \;
    echo ''
     */

    private static String getReplacerString(String replacePattern, String[] args) {
        return String.format(replacePattern, (Object[]) args);
    }

    private static String[] getTemplatedValues(String s, String pattern) {
        Matcher m = Pattern.compile(pattern).matcher(s);
        List<String> l = new ArrayList<>();
        // m.find();
        for (int i = 1; i <= m.groupCount() && m.matches(); i++) {
            l.add(m.group(i));
            //m.find();
        }
        String[] a = new String[l.size()];
        return l.toArray(a);
    }

    private static String replace(String s, String matchPattern, String replacePattern) {
        try {
            return replace(s, matchPattern, replacePattern, naturalsByTemplateString(replacePattern));
        } catch (Exception ignore) {/**/}
        return s;
    }

    private static int[] naturalsByTemplateString(String template) {
        String findStr = "%s";
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {

            lastIndex = template.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return naturalsInLengthOf(count);
    }

    private static int[] naturalsInLengthOf(int n) {
        int[] result = new int[n];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    private static <T> T[] rearange(T[] original, T[] dest, int[] order) {
        for (int i = 0; i < order.length; i++) {
            dest[i] = original[order[i]];
        }
        return dest;
    }

    private static String replace(String s, String matchPattern, String replacePattern, int[] orderOfTemplatedValues) {
        String[] a = getTemplatedValues(s, matchPattern);
        String[] rearranged = new String[orderOfTemplatedValues.length];
        rearranged = rearange(a, rearranged, orderOfTemplatedValues);
        return s.replaceAll(matchPattern, getReplacerString(replacePattern, rearranged));
    }

    private static String replaceAssertEquals_isEmpty(String s) {
        //language=RegExp
        return replace(s, "assertEquals\\(0,(.*).size\\(\\)\\);", "assertThat(%s).isEmpty\\(\\);");
    }

    private static String replaceAssertEquals_hasSize(String s) {
        //language=RegExp
        return replace(s, "assertEquals\\((.*),(.*).size\\(\\)\\);", "assertThat\\(%s\\).hasSize\\(%s\\);", new int[]{1, 0});
    }

    private static String replaceAssertEquals_isEqualTo(String s) {
        //language=RegExp
        return replace(s, "assertEquals\\((.*),(.*)\\);", "assertThat\\(%s\\).isEqualTo\\(%s\\);", new int[]{1, 0});
    }

    private static String replaceAssertNull_isNull(String s) {
        //language=RegExp
        return replace(s, "assertNull\\((.*)\\);", "assertThat\\(%s\\).isNull\\(\\);");
    }

    private static String replaceAssertNotNull_isNotNull(String s) {
        //language=RegExp
        return replace(s, "assertNotNull\\((.*)\\);", "assertThat\\(%s\\).isNotNull\\(\\);");
    }

    private static String replaceAssertTrue_isTrue(String s) {
        //language=RegExp
        return replace(s, "assertTrue\\((.*)\\);", "assertThat\\(%s\\).isTrue\\(\\);");
    }

    private static String replaceAssertFalse_isFalse(String s) {
        //language=RegExp
        return replace(s, "assertFalse\\((.*)\\);", "assertThat\\(%s\\).isFalse\\(\\);");
    }

    public static void main(String[] args) {
        try {
            MetaTester mt = new MetaTester(SimpleTest2.class);
            String s = "assertEquals(0,listy.size());";
            System.out.println(replaceAssertEquals_isEmpty(s));
            System.out.println(replaceAssertEquals_hasSize(s));
            System.out.println(replaceAssertEquals_isEqualTo(s));
            String s2 = "assertNull(listy);";
            System.out.println(replaceAssertNull_isNull(s2));
            String s3 = "assertNotNull(listy);";
            System.out.println(replaceAssertNotNull_isNotNull(s3));
            String s4 = "assertTrue(5 > 3);";
            System.out.println(replaceAssertTrue_isTrue(s4));
            String s5 = "assertFalse(0 > 9);";
            System.out.println(replaceAssertFalse_isFalse(s5));
        } catch (InitializationError initializationError) {
            initializationError.printStackTrace();
        }
    }


}
