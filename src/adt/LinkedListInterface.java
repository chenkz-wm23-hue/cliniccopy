package adt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public interface LinkedListInterface<T> {
    
    void addFirst(T data);
    void addLast(T data);
    T deleteFirst();
    T deleteLast();
    T deleteAt(int pos);
    boolean search(T data);
    void displayAll();
    boolean isEmpty();
    T getEntry(int givenPosition);  
    T getFirst();                   
    int size();                     
}

