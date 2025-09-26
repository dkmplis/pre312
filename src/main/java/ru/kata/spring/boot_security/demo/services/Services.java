package ru.kata.spring.boot_security.demo.services;


import java.util.List;

public interface Services<T> {
    List<T> getAll();

    T findById(int id);

    void addNew(T t);

    void deleteById(int id);

    void update(int id, T t);

    T findByEmail(String username);
}