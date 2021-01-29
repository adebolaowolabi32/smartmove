package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteRepositoryTests {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;

    private Route route;
    private Route savedRoute;

    @BeforeAll
    public void setUp() {
        Terminal terminal = new Terminal();
        terminal.setName("my_terminal");
        terminal.setMode(Enum.TransportMode.RAIL);
        Terminal start = terminalRepository.save(terminal);
        Terminal terminal1 = new Terminal();
        terminal1.setName("my_terminal_1");
        terminal1.setMode(Enum.TransportMode.RAIL);
        Terminal stop = terminalRepository.save(terminal1);
        route = new Route();
        route.setName("my_route");
        route.setMode(Enum.TransportMode.RAIL);
        User user = buildTestUser();
        userRepository.save(user);
        route.setOwner(user);
        route.setStartTerminal(start);
        route.setStopTerminal(stop);
        route.setFare(500);
        route.setEnabled(true);
        Route route1 = new Route();
        route1.setName("my_route2");
        route1.setMode(Enum.TransportMode.BUS);
        route1.setOwner(user);
        route1.setStartTerminal(stop);
        route1.setStopTerminal(start);
        route1.setFare(100);
        route1.setEnabled(false);
        assertNotNull(routeRepository.save(route1));
        savedRoute = routeRepository.save(route);
        assertNotNull(savedRoute);
    }

    @Test
    public void testFindById() {
        routeRepository.findById(savedRoute.getId()).ifPresent(route1 -> {
            assertEquals(route1.getName(), route.getName());
            assertEquals(route1.getOwner(), route.getOwner());
            assertEquals(route1.getMode(), route.getMode());
            assertEquals(route1.getStartTerminal().getId(), route.getStartTerminal().getId());
            assertEquals(route1.getStopTerminal().getId(), route.getStopTerminal().getId());
            assertEquals(route1.getFare(), route.getFare());
            assertEquals(route1.isEnabled(), route.isEnabled());
        });
    }

    @Test
    public void testFindAll() {
        List<Route> routes = routeRepository.findAll();
        assertTrue(routes.size() >= 2);
    }

    @Test
    public void testFindByOwner() {
        List<Route> routes = routeRepository.findAllByOwner(savedRoute.getOwner());
        assertTrue(routes.size() >= 1);
    }


    @AfterAll
    public void testDelete() {
        routeRepository.deleteAll();
        assertEquals(routeRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
