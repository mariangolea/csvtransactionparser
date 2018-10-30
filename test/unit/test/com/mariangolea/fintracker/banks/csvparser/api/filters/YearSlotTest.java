package test.com.mariangolea.fintracker.banks.csvparser.api.filters;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class YearSlotTest {
    
     @Test
     public void simpleTests(){
         YearSlot slot = new YearSlot(2018);
         YearSlot slot2 = new YearSlot(2018);
         YearSlot three = new YearSlot(2019);
         YearSlot fourth = null;
         
         assertEquals(slot, slot);
         assertEquals(slot, slot2);
         assertNotEquals(slot, three);
         assertNotEquals(slot, fourth);
         
         assertEquals(slot.hashCode(), slot2.hashCode());
         assertNotEquals(slot.hashCode(), three.hashCode());
     }
}
