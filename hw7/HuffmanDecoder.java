
import java.util.List;
import java.util.ArrayList;


public class HuffmanDecoder {
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("please input decode filename");
            return;
        }
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie bt = (BinaryTrie)or.readObject();
        BitSequence bs = (BitSequence)or.readObject();
        List<Character> ls = new ArrayList<>();
//        System.out.println(bs.length());
        while(bs.length() > 0){
            Match m = bt.longestPrefixMatch(bs);
            bs = bs.allButFirstNBits(m.getSequence().length());
            ls.add(m.getSymbol());
        }
        char[] carr = new char[ls.size()];
        for(int i = 0;i < ls.size();i++ )
            carr[i] = ls.get(i);
        String newname = "original"+args[0].substring(0,args[0].length()-4);
        FileUtils.writeCharArray(newname, carr);
    }
}
