package server;

import element.Worker;

import java.sql.SQLException;
import java.util.TreeSet;
import java.util.stream.Stream;

public class DataManager {
    private static TreeSet<Worker> collection;
    private DatabaseHandler handler;

    public  DataManager(DatabaseHandler handler) {
        this.handler = handler;
    }

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
    public boolean login(String username, String password) throws SQLException {
        return handler.login(username, password);
    }

    public boolean register(String username, String password) throws SQLException {
        return handler.register(username, password);
    }
}
