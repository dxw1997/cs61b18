import java.util.LinkedList;
import java.util.List;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int MLen = 0;
        String[] asc = new String[asciis.length];
        for(int i = 0;i < asciis.length;i++ ){
            MLen = MLen >= asciis[i].length()?MLen:asciis[i].length();
            asc[i] = asciis[i];
        }
        for(int i = MLen-1;i >= 0;i-- ){
            sortHelperLSD(asc, i);
        }
        return asc;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        List<String>[] buckets = new List[257];
        for(int i = 0;i < 257;i++ ) buckets[i] = new LinkedList<>();
        for(int i = 0;i < asciis.length;i++ ){
            if(asciis[i].length() <= index){
                int idx = 0;
                buckets[idx].add(asciis[i]);
            }else{
                int idx = (int)asciis[i].charAt(index) + 1;
                buckets[idx].add(asciis[i]);
            }
        }
        int k = 0;
        for(int i = 0;i < 257;i++ ){
            for(int j = 0;j < buckets[i].size();j++, k++){
                asciis[k] = buckets[i].get(j);
            }
        }
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args){
        String[] asciis = new String[5];
        asciis[0] = "acd";
        asciis[1] = "acddd";
        asciis[2] = "bcd";
        asciis[3] = "ec";
        asciis[4] = "gcd";
        String[] r = sort(asciis);
        for(int i = 0;i < r.length;i++ ){
            System.out.print(" "+r[i]);
        }
    }
}
