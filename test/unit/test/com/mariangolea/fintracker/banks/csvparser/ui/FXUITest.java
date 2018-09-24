/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.ui;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.JFXPanel;
import javax.swing.SwingUtilities;
import org.junit.BeforeClass;

/**
 * Takes care of initialising the FX thread.
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class FXUITest {
    
    @BeforeClass
    public static void initToolkit()
            throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            latch.countDown();
        });

        fxInitialized = latch.await(10L, TimeUnit.SECONDS);
    }
    
    static boolean fxInitialized; 
}
