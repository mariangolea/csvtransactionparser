package test.com.mariangolea.fintracker.banks.csvparser.ui;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.JFXPanel;
import javax.swing.SwingUtilities;
import org.junit.BeforeClass;
import org.junit.Test;

public class FXUITest {

    @BeforeClass
    public static void initToolkit()
            throws InterruptedException {
        String headless = System.getProperty("java.awt.headless", "false");
        if (!Boolean.parseBoolean(headless)) {
            final CountDownLatch latch = new CountDownLatch(1);
            SwingUtilities.invokeLater(() -> {
                new JFXPanel();
                latch.countDown();
            });

            fxInitialized = latch.await(10L, TimeUnit.SECONDS);
        }
    }

    protected static boolean fxInitialized = false;
    
    @Test
    public void junitisStupid(){
        //find a way not to need this mock test case....
    }
}
