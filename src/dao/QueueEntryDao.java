/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.LinkedList;
import adt.LinkedListInterface;
import entity.Patient;
import entity.QueueEntry;
import java.time.LocalDateTime;

/**
 * QueueEntryDAO - hard coded in-memory list of queue entries
 */
public class QueueEntryDao {
    private final LinkedListInterface<QueueEntry> queueEntries;

    public QueueEntryDao(LinkedListInterface<Patient> patients) {
        queueEntries = new LinkedList<>();
        initializeData(patients);
    }

    private void initializeData(LinkedListInterface<Patient> patients) {
        if (patients.size() >= 5) {
            Patient p1 = patients.getEntry(1);
            queueEntries.addLast(new QueueEntry(p1, "Flu symptoms", LocalDateTime.now()));

            Patient p2 = patients.getEntry(2);
            queueEntries.addLast(new QueueEntry(p2, "Back pain", LocalDateTime.now().minusMinutes(10)));

            Patient p3 = patients.getEntry(3);
            queueEntries.addLast(new QueueEntry(p3, "Fever", LocalDateTime.now().minusMinutes(20)));

            Patient p4 = patients.getEntry(4);
            queueEntries.addLast(new QueueEntry(p4, "Headache", LocalDateTime.now().minusMinutes(30)));

            Patient p5 = patients.getEntry(5);
            queueEntries.addLast(new QueueEntry(p5, "Routine check-up", LocalDateTime.now().minusMinutes(40)));
        }
    }

    public LinkedListInterface<QueueEntry> getAllQueueEntries() {
        return queueEntries;
    }
}
