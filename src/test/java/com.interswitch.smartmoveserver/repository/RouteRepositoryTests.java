package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

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
    private UserRepository userRepository;

    private Route route;
    private Route savedRoute;

    @Before
    public void setUp() {
        route = new Route();
        route.setName("my_route");
        route.setType(Enum.TransportMode.RAIL);
        User user = buildTestUser();
        userRepository.save(user);
        route.setOwner(user);
        route.setStart("start_terminal");
        route.setStop("stop_terminal");
        route.setPrice(500);
        route.setActive(true);
        Route route1 = new Route();
        route1.setName("my_route2");
        route1.setType(Enum.TransportMode.BUS);
        route1.setOwner(user);
        route1.setStart("start_terminal");
        route1.setStop("stop_terminal");
        route1.setPrice(100);
        route1.setActive(false);
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
            assertThat(route1.getStart()).isEqualTo(route.getStart());
            assertThat(route1.getStop()).isEqualTo(route.getStop());
            assertThat(route1.getPrice()).isEqualTo(route.getPrice());
            assertThat(route1.isActive()).isEqualTo(route.isActive());
        });
    }

    @Test
    public void testFindAll() {
        Iterable<Route> routes = routeRepository.findAll();
        Iterator<Route> routeIterator = routes.iterator();

        int i = 0;
        while(routeIterator.hasNext()) {
            i++;
            routeIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByOwner() {
        Iterable<Route> routes = routeRepository.findAllByOwner(savedRoute.getOwner());
        Iterator<Route> routeIterator = routes.iterator();

        int i = 0;
        while(routeIterator.hasNext()) {
            i++;
            routeIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(1);
    }


    @After
    public void testDelete() {
        routeRepository.deleteAll();
        assertEquals(routeRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
