package server;

import element.Worker;

import java.sql.SQLException;
import java.util.stream.Stream;

public class DataManager {
    private final DatabaseHandler handler;

    public  DataManager(DatabaseHandler handler) {
        this.handler = handler;
    }

    public void add(Worker worker) {
        handler.add(worker);
    }
    public boolean isEmpty() {
        return handler.isEmpty();
    }
    public Worker first() {
        return handler.first();
    }
    public void clear(String username) {
        handler.clear(username);
    }
    public Stream<Worker> stream() {
        return handler.stream();
    }
    public int size() {
        return handler.size();
    }
    public void remove(Worker elem) {
        handler.remove(elem);
    }

    public Worker floor(Worker worker) {
        return handler.floor(worker);
    }

    public boolean login(String username, String password) throws SQLException {
        return handler.login(username, password);
    }

    public boolean register(String username, String password) throws SQLException {
        return handler.register(username, password);
    }
}
