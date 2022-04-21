

public class HelloNumbers{
    public static void main(String[] args){
        int sum = 0, i = 0;
	while(i < 10){
	    System.out.print(sum + " ");
	    sum += ++i;
	}
	System.out.print("\n");
    }
}

