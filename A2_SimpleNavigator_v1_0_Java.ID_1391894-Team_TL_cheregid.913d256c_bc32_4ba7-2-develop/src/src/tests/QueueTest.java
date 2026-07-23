// tests/QueueTest.java
package tests;

import org.junit.jupiter.api.*;
import s21_queue.Queue;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {
    @Test
    void pushPopSingle() {
        Queue<Integer> q = new Queue<>();
        q.push(100);
        assertEquals(100, q.pop());
        assertTrue(q.isEmpty());
    }
    @Test
    void pushMultipleFIFO() {
        Queue<String> q = new Queue<>();
        q.push("x");
        q.push("y");
        q.push("z");
        assertEquals("x", q.pop());
        assertEquals("y", q.pop());
        assertEquals("z", q.pop());
    }
    @Test
    void frontAndBack() {
        Queue<Integer> q = new Queue<>();
        q.push(1);
        q.push(2);
        q.push(3);
        assertEquals(1, q.front());
        assertEquals(3, q.back());
        assertEquals(3, q.size());
    }
    @Test
    void frontBackAfterPop() {
        Queue<Character> q = new Queue<>();
        q.push('A');
        q.push('B');
        q.pop();
        assertEquals('B', q.front());
        assertEquals('B', q.back());
    }
    @Test
    void popOnEmptyThrows() {
        Queue<Double> q = new Queue<>();
        assertThrows(NoSuchElementException.class, q::pop);
    }
    @Test
    void frontOnEmptyThrows() {
        Queue<Integer> q = new Queue<>();
        assertThrows(NoSuchElementException.class, q::front);
    }
    @Test
    void backOnEmptyThrows() {
        Queue<Integer> q = new Queue<>();
        assertThrows(NoSuchElementException.class, q::back);
    }
    @Test
    void sizeAndIsEmpty() {
        Queue<Object> q = new Queue<>();
        assertEquals(0, q.size());
        assertTrue(q.isEmpty());
        q.push(1);
        assertFalse(q.isEmpty());
        q.push(2);
        assertEquals(2, q.size());
        q.pop();
        assertEquals(1, q.size());
        q.pop();
        assertTrue(q.isEmpty());
    }
}