/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.cmdsupport;

import com.mariangolea.fintracker.banks.csvparser.cmdsupport.CmdArgParser;
import java.io.File;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class CmdArgParserTest {

    @Test
    public void testArgumentParsing() {
        String tmpDir = System.getProperties().get("java.io.tmpdir").toString();
        String userDir = System.getProperties().get("user.home").toString();
        String[] correctArgs = {"-folder=" + tmpDir, "-folder=" + userDir};
        String[] incorrectArgs = {"--folder=one", "-Folder=two"};

        CmdArgParser parser = new CmdArgParser();
        List<File> files = parser.getFolderFiles(incorrectArgs);
        assertTrue(files == null);

        files = parser.getFolderFiles(correctArgs);
        assertTrue(files != null && files.size() == 2);
        for (File file : files) {
            assertTrue(file != null && file.isDirectory());
        }

        files = parser.getCSVFiles(null);
        assertTrue(files == null);

        files = parser.getCSVFiles(correctArgs);
        assertTrue(files != null);
    }
}
