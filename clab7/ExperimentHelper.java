/**
 * Created by hug.
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    private static int log2(int i){
        return (int)(Math.log(i)/Math.log(2.0));
    }
    public static int optimalIPL(int N) {
        int sum = 0;
        int i = 2;
        int k = 0; //floor count.
        while (i <= N){
            k = log2(i) ;
            sum += k;
            i++;
        }
        return sum;
    }

    public static void main(String args[]){
        for (int i = 1; i <= 10; i++){
            System.out.println(i + ": " + optimalAverageDepth(i));
        }
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {

        return (double)optimalIPL(N)/(double)N;
    }

}
