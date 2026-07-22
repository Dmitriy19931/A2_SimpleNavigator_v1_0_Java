
package tests;

import org.junit.jupiter.api.*;
import s21_stack.Stack;
import java.util.EmptyStackException;
import static org.junit.jupiter.api.Assertions.*;

public class StackTest {
    @Test
    void pushAndPopSingle() {
        Stack<Integer> s = new Stack<>();
        s.push(10);
        assertEquals(10, s.pop());
        assertTrue(s.isEmpty());
    }
    @Test
    void pushMultiplePopOrder() {
        Stack<String> s = new Stack<>();
        s.push("a");
        s.push("b");
        s.push("c");
        assertEquals("c", s.pop());
        assertEquals("b", s.pop());
        assertEquals("a", s.pop());
    }
    @Test
    void topDoesNotRemove() {
        Stack<Integer> s = new Stack<>();
        s.push(42);
        assertEquals(42, s.top());
        assertEquals(1, s.size());
        assertEquals(42, s.top());
    }
    @Test
    void popOnEmptyThrows() {
        Stack<Double> s = new Stack<>();
        assertThrows(EmptyStackException.class, s::pop);
    }
    @Test
    void topOnEmptyThrows() {
        Stack<Character> s = new Stack<>();
        assertThrows(EmptyStackException.class, s::top);
    }
    @Test
    void sizeIncreasesDecreases() {
        Stack<Integer> s = new Stack<>();
        assertEquals(0, s.size());
        s.push(1);
        assertEquals(1, s.size());
        s.push(2);
        assertEquals(2, s.size());
        s.pop();
        assertEquals(1, s.size());
        s.pop();
        assertEquals(0, s.size());
    }
    @Test
    void isEmptyReflectsState() {
        Stack<Object> s = new Stack<>();
        assertTrue(s.isEmpty());
        s.push(new Object());
        assertFalse(s.isEmpty());
        s.pop();
        assertTrue(s.isEmpty());
    }
}