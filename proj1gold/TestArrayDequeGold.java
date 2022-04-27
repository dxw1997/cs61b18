import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque(){
        StudentArrayDeque<Integer> dqs = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> dqa = new ArrayDequeSolution<>();
        String msg = "";
        ///for and random numbers.
        for(int i = 0;i < 1000000;i++ ){
            double rn = StdRandom.uniform();
            if(rn < 0.25){
                dqs.addFirst(i);
                dqa.addFirst(i);
                String mt = "addFirst("+i+")\n";
                msg = msg+mt;
            }else if(rn < 0.5){
                dqs.addLast(i);
                dqa.addLast(i);
                String mt = "addLast("+i+")\n";
                msg = msg+mt;
            }else if(rn < 0.75){
                Integer s = dqs.removeFirst();
                Integer a = dqa.removeFirst();
                String mt = "removeFirst()\n";
                msg = msg+mt;
                if((s == null && a == null) || s.equals(a)){
                }else{
                    assertEquals(msg, a, s);
                    return;
                }
            }else{
                Integer s = dqs.removeLast();
                Integer a = dqa.removeLast();
                String mt = "removeLast()\n";
                msg = msg+mt;
                if((s == null && a == null) || s.equals(a)){
                }else{
                    assertEquals(msg, a, s);
                    return;
                }
            }
        }
        return;
    }
}
