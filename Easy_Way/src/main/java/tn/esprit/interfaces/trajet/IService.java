package tn.esprit.interfaces.trajet;

import java.util.List;

public interface IService <T>{

    void add(T t);

    List<T> getAll();

    void update(T t);

    void delete(T t);

    <T> T getById(int t);

}
