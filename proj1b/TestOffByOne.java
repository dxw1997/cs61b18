import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    //Uncomment this class once you've created your CharacterComparator interface and OffByOne class. **/
    @Test
    public void testEqualChars(){
        assertTrue(offByOne.equalChars('a','b'));
        assertTrue(offByOne.equalChars('b','a'));
        assertTrue(offByOne.equalChars('&','%'));
        assertTrue(offByOne.equalChars('%','&'));
        assertTrue(offByOne.equalChars('A','B'));
        assertTrue(offByOne.equalChars('B','A'));

        assertFalse(offByOne.equalChars('a','a'));
        assertFalse(offByOne.equalChars('a','A'));
        assertFalse(offByOne.equalChars('A','a'));
        assertFalse(offByOne.equalChars('A','A'));
        assertFalse(offByOne.equalChars('A','b'));
        assertFalse(offByOne.equalChars('b','A'));
        assertFalse(offByOne.equalChars('z','Z'));
        assertFalse(offByOne.equalChars('W','B'));
        assertFalse(offByOne.equalChars('z','a'));
        assertFalse(offByOne.equalChars('a','z'));

        ///newly added
        //assertTrue(offByOne.equalChars('a','b'));
        //assertFalse(offByOne.equalChars('a','a'));
        //assertFalse(offByOne.equalChars('a','d'));
        //assertFalse(offByOne.equalChars('e','b'));
        //assertTrue(offByOne.equalChars('c','b'));
    }
}
