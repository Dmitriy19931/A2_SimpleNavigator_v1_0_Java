package s21_queue;

import java.util.NoSuchElementException;

public class Queue<T> {
    private Node<T> front, back;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    public Queue() {
        front = back = null;
        size = 0;
    }

    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        if (back != null) back.next = newNode;
        back = newNode;
        if (front == null) front = back;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException();
        T value = front.data;
        front = front.next;
        if (front == null) back = null;
        size--;
        return value;
    }

    public T front() {
        if (isEmpty()) throw new NoSuchElementException();
        return front.data;
    }

    public T back() {
        if (isEmpty()) throw new NoSuchElementException();
        return back.data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
}