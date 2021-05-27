package server;

import element.Worker;

import java.util.TreeSet;
import java.util.stream.Stream;

public class DataManager {
    private static TreeSet<Worker> collection;

    public void add(Worker worker) {
        collection.add(worker);
    }
    public boolean isEmpty() {
        return collection.isEmpty();
    }
    public Worker first() {
        return collection.first();
    }
    public void clear() {
        collection.clear();
    }
    public Stream<Worker> stream() {
        return collection.stream();
    }
    public int size() {
        return collection.size();
    }
    public void remove(Worker elem) {
        collection.remove(elem);
    }
    public Worker floor(Worker worker) {
        return collection.floor(worker);
    }
}
