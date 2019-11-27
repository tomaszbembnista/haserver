package eu.wordpro.ha.server.service;

import java.util.List;
import java.util.Optional;

public interface Service<T> {

    T save(T dto);

    Optional<T> findOne(Long id);

    List<T> findAll();

    void delete(Long id);

}
