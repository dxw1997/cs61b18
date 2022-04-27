
public class Palindrome {

    public Deque<Character> wordToDeque(String word){
        Deque<Character> d = new LinkedListDeque<Character>();
        for(int i = 0;i < word.length();++i ){
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public boolean isPalindrome(String word){
        if(word.length() <= 1) return true;
        Deque<Character> d = wordToDeque(word);
        while(d.size() > 1){
            Character c1 = d.removeFirst();
            Character c2 = d.removeLast();
            if(!c1.equals(c2)) return false;
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc){
        if(word.length() <= 1) return true;
        Deque<Character> d = wordToDeque(word);
        while(d.size() > 1){
            char c1 = d.removeFirst();
            char c2 = d.removeLast();
            if(cc.equalChars(c1, c2) == false) return false;
        }
        return true;
    }
}
