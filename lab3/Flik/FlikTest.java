import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {
    @Test
    public void testIsSameNumber(){
        assertTrue(Flik.isSameNumber(-128,-128));
        assertTrue(Flik.isSameNumber(10,10));
        assertTrue(Flik.isSameNumber(128,128));
        assertTrue(Flik.isSameNumber(129,129));
    }
}
