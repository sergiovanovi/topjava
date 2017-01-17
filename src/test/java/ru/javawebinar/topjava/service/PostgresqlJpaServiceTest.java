package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.POSTGRES, Profiles.JPA})
public class PostgresqlJpaServiceTest extends ServiceTest {
}
