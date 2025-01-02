package rest.repository;

import java.util.List;

public interface Repository<T,K> {

    T findById(K id);
    boolean deleteById(K id);
    List<T> findAll();
    void save(T t);
    boolean update(T t);
}
