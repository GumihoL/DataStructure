import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        d.printDeque();
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome(){
        String testWord1 = "aba";
        assertTrue(palindrome.isPalindrome(testWord1));
        String testWord2 = "Abcfdfyrewt";
        assertFalse(palindrome.isPalindrome(testWord2));
        String testWord3 = "AbccbA";
        assertTrue(palindrome.isPalindrome(testWord3));
        String testWord4 = "";
        assertTrue(palindrome.isPalindrome(testWord4));
        CharacterComparator cc = new OffByOne();
        String testWord5 = "flake";
        assertTrue(palindrome.isPalindrome(testWord5, cc));
        CharacterComparator ccN = new OffByN(5);
        String testWord6 = "abf";
        assertTrue(palindrome.isPalindrome(testWord6, ccN));
    }

    @Test public void testModular(){
        int a = 5 % 10;
        int b = -3 % 10;
        int c = 0 % 10;
    }
}