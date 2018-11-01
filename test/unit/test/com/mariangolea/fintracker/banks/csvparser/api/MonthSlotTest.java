package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import static org.junit.Assert.*;
import org.junit.Test;

public class MonthSlotTest {

    @Test
    public void simpleTests() {
        MonthSlot slot = new MonthSlot(1, 2018);
        MonthSlot slot2 = new MonthSlot(1, 2018);
        MonthSlot three = new MonthSlot(1, 2019);
        MonthSlot fourth = null;

        assertEquals(slot, slot);
        assertEquals(slot, slot2);
        assertNotEquals(slot, three);
        assertNotEquals(slot, fourth);

        assertEquals(slot.hashCode(), slot2.hashCode());
        assertNotEquals(slot.hashCode(), three.hashCode());

        assertEquals(0, slot.compareTo(slot));
    }
}
