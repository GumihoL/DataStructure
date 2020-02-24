import java.lang.reflect.Type;

public class Palindrome{
    /**
     * Return a deque<Character> consisted of String word
     * @param word
     * @return a deque
     */
    public Deque<Character> wordToDeque(String word){
        Deque<Character> deque = new ArrayDeque<>();
        for(int i = 0; i < word.length(); i++){
            deque.addLast(word.charAt(i));
        }
        return deque;
    }
    public boolean isPalindrome(String word){
        if (word.length() == 0){
            return true;
        }
        Deque<Character> deque = wordToDeque(word);
        Deque<Character> dequeCopy = new ArrayDeque<Character>((ArrayDeque) deque);
        for(int i = 0; i < deque.size(); i++){
            if(deque.removeFirst() != dequeCopy.removeLast()){
                return false;
            }
        }
        return true;
    }
    /* isPalindrome by recursion! Very beautiful.
     private boolean isPalindrome(Deque<Character> wordInDeque) {
        while (wordInDeque.size() > 1) {
            return wordInDeque.removeFirst() == wordInDeque.removeLast() && isPalindrome(wordInDeque);
        }
        return true;
    }
    public boolean isPalindrome(String word) {
        return isPalindrome(wordToDeque(word));
    }
     */
    public boolean isPalindrome(String word, CharacterComparator cc){
        Deque<Character> deque = this.wordToDeque(word);
        return isPalindrome(deque, cc);
    }
    private boolean isPalindrome(Deque<Character> deque, CharacterComparator cc){
        if (deque.size() > 1){
            return cc.equalChars((char)deque.removeFirst(), (char)deque.removeLast()) && isPalindrome(deque, cc);
        }
        return true;
    }
}