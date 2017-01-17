package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.POSTGRES, Profiles.DATAJPA})
public class PostgresqlDataJpaServiceTest extends ServiceTest {
}
