public class OffByN  implements CharacterComparator{

    private int N;

    public OffByN(int N_){
        N = N_;
    }
    @Override
    public boolean equalChars(char x, char y){
        int diff = x-y;
        if(diff == N || diff == -N)return true;
        return false;
    }
}
