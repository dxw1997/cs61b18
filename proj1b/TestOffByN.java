import org.junit.Test;
import static org.junit.Assert.*;


public class TestOffByN {
    @Test
    public void testEqualChars(){
        OffByN offbyn = new OffByN(5);

        assertTrue(offbyn.equalChars('a','f'));
        assertTrue(offbyn.equalChars('f','a'));
        assertFalse(offbyn.equalChars('h','f'));
    }
}
