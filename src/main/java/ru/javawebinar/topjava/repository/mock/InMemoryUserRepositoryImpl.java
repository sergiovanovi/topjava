package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.06.2015.
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private List<User> repository = new CopyOnWriteArrayList<>();

    @Override
    public boolean delete(int id) {
        LOG.info("delete " + id);
        return repository.remove(repository.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .get());
    }

    @Override
    public User save(User user) {
        LOG.info("save " + user);
        repository.add(user);
        return user;
    }

    @Override
    public User get(int id) {
        LOG.info("get " + id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");
        return repository.isEmpty() ? Collections.emptyList() : repository.stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        LOG.info("getByEmail " + email);
        return repository.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .get();
    }
}
