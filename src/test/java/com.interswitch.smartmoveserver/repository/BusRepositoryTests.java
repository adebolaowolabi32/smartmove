package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Bus;
import com.interswitch.smartmoveserver.repository.BusRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusRepositoryTests {
    @Autowired
    BusRepository busRepository;

    @Test
    public void testFindById() {
        Bus bus = new Bus();
        bus.setId("testbus");
        bus.setDeviceId("A00000002");
        bus.setOwnerId("1230000002");
        bus.setRegNo("LAG0000002");
        Bus savedBus = busRepository.save(bus);
        busRepository.findById(savedBus.getId()).ifPresent(bus1 -> {
            assertThat(bus1.getDeviceId()).isEqualTo(bus1.getDeviceId());
            assertThat(bus1.getOwnerId()).isEqualTo(bus1.getOwnerId());
            assertThat(bus1.getRegNo()).isEqualTo(bus1.getRegNo());
        });
    }

    @Test
    public void testFindAll() {
        Bus bus = new Bus();
        bus.setId("testbus");
        bus.setDeviceId("A00000002");
        bus.setOwnerId("1230000002");
        bus.setRegNo("LAG0000002");
        Bus bus1 = new Bus();
        bus1.setId("testbus1");
        bus1.setDeviceId("B00000002");
        bus1.setOwnerId("1240000002");
        bus1.setRegNo("LAG0000003");
        busRepository.save(bus);
        busRepository.save(bus1);
        Iterable<Bus> buses = busRepository.findAll();
        Iterator<Bus> busIterator = buses.iterator();
        int i = 0;
        while(busIterator.hasNext()) {
            i++;
            busIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testDelete() {
        Bus bus = new Bus();
        bus.setId("testbus");
        bus.setDeviceId("A00000002");
        bus.setOwnerId("1230000002");
        bus.setRegNo("LAG0000002");
        Bus savedBus = busRepository.save(bus);
        busRepository.deleteById(savedBus.getId());
        assertThat(busRepository.findById(savedBus.getId())).isEmpty();
    }
}

