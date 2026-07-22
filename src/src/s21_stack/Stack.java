package s21_stack;

import java.util.EmptyStackException;

public class Stack<T> {
    private Node<T> top;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    public Stack() {
        top = null;
        size = 0;
    }

    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T value = top.data;
        top = top.next;
        size--;
        return value;
    }

    public T top() {
        if (isEmpty()) throw new EmptyStackException();
        return top.data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
}