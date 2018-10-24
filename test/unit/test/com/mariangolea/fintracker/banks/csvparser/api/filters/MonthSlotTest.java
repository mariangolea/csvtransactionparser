package test.com.mariangolea.fintracker.banks.csvparser.api.filters;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class MonthSlotTest {
    @Test
     public void simpleTests(){
         MonthSlot slot = new MonthSlot(1, 2018);
         MonthSlot slot2 = new MonthSlot(1, 2018);
         MonthSlot three = new MonthSlot(2, 2018);
         MonthSlot fourth = null;
         
         assertEquals(slot, slot);
         assertEquals(slot, slot2);
         assertNotEquals(slot, three);
         assertNotEquals(slot, fourth);
         
         assertEquals(slot.hashCode(), slot2.hashCode());
         assertNotEquals(slot.hashCode(), three.hashCode());
     }
}
