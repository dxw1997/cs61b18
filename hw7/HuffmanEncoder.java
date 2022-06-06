
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols){
        Map<Character, Integer> res = new HashMap<>();
        for(int i = 0;i < inputSymbols.length;i++ ){
            if(res.containsKey(inputSymbols[i])){
                res.replace(inputSymbols[i], res.get(inputSymbols[i]), res.get(inputSymbols[i])+1);
            }else{
                res.put(inputSymbols[i], 1);
            }
        }
        return res;
    }
    public static void main(String[] args){
//        if(args.length != 1){
//            System.out.println("please input the filename!");
//            return;
//        }
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);
        BinaryTrie bt = new BinaryTrie(frequencyTable);
        String saveFileName = args[0] + ".huf";
        ObjectWriter ow = new ObjectWriter(saveFileName);
        ow.writeObject(bt);
        Map<Character, BitSequence> lt = bt.buildLookupTable();
        List<BitSequence> lbs = new ArrayList<>();
        for(int i = 0;i < inputSymbols.length;i++ ){
            lbs.add(lt.get(inputSymbols[i]));
        }
        System.out.println(saveFileName);
        BitSequence bss = BitSequence.assemble(lbs);
        ow.writeObject(bss);
    }
}
