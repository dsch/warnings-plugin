package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link GhsMultiParser}.
 */
public class GhsMultiParserTest extends ParserTester {
    private static final String TYPE = new GhsMultiParser().getGroup();

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException
     *             if the file could not be read
     */
    @Test
    public void parseMultiLine() throws IOException {
        Collection<FileAnnotation> warnings = new GhsMultiParser().parse(openFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 3, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();
        checkWarning(annotation, 37,
                "transfer of control bypasses initialization of:\n            variable \"CF_TRY_FLAG\" (declared at line 42)\n            variable \"CF_EXCEPTION_NOT_CAUGHT\" (declared at line 42)\n        CF_TRY_CHECK_EX(ex2);",
                "./maindir/tests/TestCase_0101.cpp", TYPE, "#546-D",
                Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation, 29,
                "label\n          \"CF_TRY_LABELex1\" was declared but never referenced\n     CF_TRY_EX(ex1)",
                "./maindir/tests/TestCase_0101.cpp", TYPE, "#177-D",
                Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation, 9,
                "extra\n          \";\" ignored\n  TEST_DSS( CHECK_4TH_CONFIG_DATA, 18, 142, 'F');",
                "./maindir/tests/TestCase_1601.cpp", TYPE, "#381-D",
                Priority.NORMAL);
    }

    @Test
    public void parseWrap() throws IOException {
        Collection<FileAnnotation> warnings = new GhsMultiParser().parse(openFile("ghsmulti_wrap.txt"));

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 3, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();
        checkWarning(annotation, 66,
                "variable \"ap\" was declared but never referenced\n" +
                        "                              const os_appcontext_t *ap = &OS_appTableBase[a];",
                "C:/MyWork/kernel/kern-initappdata.c", TYPE,"#177-D", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation, 77,
                "variable \"tp\" was declared but never referenced\n" +
                        "                              const os_task_t *tp = &OS_taskTableBase[t];",
                "C:/MyWork/kernel/kern-initappdata.c", TYPE, "#177-D", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation, 88,
                "nonstandard type for a bitfield\n" +
                        "                              char i:3;",
                "C:/MyWork/x.c", TYPE, "#230", Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "ghsmulti.txt";
    }
}

