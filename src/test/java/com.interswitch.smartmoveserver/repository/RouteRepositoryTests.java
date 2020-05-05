package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteRepositoryTests {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;

    private Route route;
    private Route savedRoute;

    @Before
    public void setUp() {
        Terminal terminal = new Terminal();
        terminal.setName("my_terminal");
        terminal.setType(Enum.TransportMode.RAIL);
        long startId = terminalRepository.save(terminal).getId();
        Terminal terminal1 = new Terminal();
        terminal1.setName("my_terminal_1");
        terminal1.setType(Enum.TransportMode.RAIL);
        long stopId = terminalRepository.save(terminal1).getId();
        route = new Route();
        route.setName("my_route");
        route.setType(Enum.TransportMode.RAIL);
        User user = buildTestUser();
        userRepository.save(user);
        route.setOwner(user);
        route.setStartTerminalId(startId);
        route.setStopTerminalId(stopId);
        route.setStartTerminalName("my_terminal");
        route.setStopTerminalName("my_terminal_1");
        route.setPrice(500);
        route.setEnabled(true);
        Route route1 = new Route();
        route1.setName("my_route2");
        route1.setType(Enum.TransportMode.BUS);
        route1.setOwner(user);
        route1.setStartTerminalId(stopId);
        route1.setStopTerminalId(startId);
        route1.setStartTerminalName("my_terminal_1");
        route1.setStopTerminalName("my_terminal");
        route1.setPrice(100);
        route1.setEnabled(false);
        assertNotNull(routeRepository.save(route1));
        savedRoute = routeRepository.save(route);
        assertNotNull(savedRoute);
    }

    @Test
    public void testFindById() {
        routeRepository.findById(savedRoute.getId()).ifPresent(route1 -> {
            assertThat(route1.getName()).isEqualTo(route.getName());
            assertThat(route1.getOwner()).isEqualTo(route.getOwner());
            assertThat(route1.getType()).isEqualTo(route.getType());
            assertThat(route1.getStartTerminalId()).isEqualTo(route.getStartTerminalId());
            assertThat(route1.getStopTerminalId()).isEqualTo(route.getStopTerminalId());
            assertThat(route1.getPrice()).isEqualTo(route.getPrice());
            assertThat(route1.isEnabled()).isEqualTo(route.isEnabled());
        });
    }

    @Test
    public void testFindAll() {
        List<Route> routes = routeRepository.findAll();
        assertThat(routes.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByOwner() {
        List<Route> routes = routeRepository.findAllByOwner(savedRoute.getOwner());
        assertThat(routes.size()).isGreaterThanOrEqualTo(1);
    }


    @After
    public void testDelete() {
        routeRepository.deleteAll();
        assertEquals(routeRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
