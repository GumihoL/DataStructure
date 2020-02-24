
public class OffByN implements CharacterComparator{
    private int distance;
    public OffByN(int N){
        distance = N;
    }
    @Override
    public boolean equalChars(char x, char y){
        if (x - y == 5 || y - x == 5){
            return true;
        }
        return false;
    }
}