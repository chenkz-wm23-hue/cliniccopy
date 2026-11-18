/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

public class LinkedList<T> implements LinkedListInterface<T> {

    class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int numberOfEntries = 0; 

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        numberOfEntries++;
    }

    @Override
    public void addLast(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        numberOfEntries++;
    }

    @Override
    public T deleteFirst() {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        Node current = head;
        head = head.next;
        current.next = null;

        if (head == null) {
            tail = null;
        }

        numberOfEntries--;
        return current.data;
    }

    @Override
    public T deleteLast() {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        if (head == tail) {
            T data = head.data;
            head = tail = null;
            numberOfEntries--;
            return data;
        }

        Node current = head;
        Node prev = null;

        while (current.next != null) {
            prev = current;
            current = current.next;
        }

        prev.next = null;
        tail = prev;
        numberOfEntries--;

        return current.data;
    }

    @Override
    public T deleteAt(int pos) {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        if (pos == 1) {
            return deleteFirst();
        }

        Node current = head;
        Node prev = null;
        int count = 1;

        while (count < pos && current != null) {
            prev = current;
            current = current.next;
            count++;
        }

        if (current == null) {
            throw new RuntimeException("Invalid position");
        }

        prev.next = current.next;
        if (current == tail) {
            tail = prev;
        }

        current.next = null;
        numberOfEntries--;
        return current.data;
    }

    @Override
    public boolean search(T data) {
        Node current = head;
        while (current != null) {
            if (data.equals(current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void displayAll() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }


    public T getEntry(int givenPosition) {
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            Node currentNode = head;
            for (int i = 1; i < givenPosition; i++) {
                currentNode = currentNode.next; 
            }
            return currentNode.data;
        }
        return null; 
    }
    
    public T getFirst() {
    if (isEmpty()) {
        throw new RuntimeException("List is empty");
    }
    return head.data; 
}


    public int size() {
        return numberOfEntries;
    }
}
