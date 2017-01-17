package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.HSQLDB, Profiles.JDBC})
public class HsqldbJdbcServiceTest extends ServiceTest {
}
