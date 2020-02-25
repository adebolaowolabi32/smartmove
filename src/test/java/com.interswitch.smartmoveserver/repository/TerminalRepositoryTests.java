package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Terminal;
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
public class TerminalRepositoryTests {
    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;

    private Terminal terminal;
    private Terminal savedTerminal;

    @Before
    public void setUp() {
        terminal = new Terminal();
        terminal.setName("my_terminal");
        terminal.setType(Enum.TransportMode.RAIL);
        User user = buildTestUser();
        userRepository.save(user);
        terminal.setOwner(user);
        terminal.setLocation("my_location_one");
        terminal.setActive(true);
        Terminal terminal1 = new Terminal();
        terminal1.setName("my_terminal2");
        terminal1.setType(Enum.TransportMode.BUS);
        terminal1.setOwner(user);
        terminal1.setLocation("my_location_two");
        terminal1.setActive(true);
        assertNotNull(terminalRepository.save(terminal1));
        savedTerminal = terminalRepository.save(terminal);
        assertNotNull(savedTerminal);
    }

    @Test
    public void testFindById() {
        terminalRepository.findById(savedTerminal.getId()).ifPresent(terminal1 -> {
            assertThat(terminal1.getName()).isEqualTo(terminal.getName());
            assertThat(terminal1.getOwner()).isEqualTo(terminal.getOwner());
            assertThat(terminal1.getType()).isEqualTo(terminal.getType());
            assertThat(terminal1.getLocation()).isEqualTo(terminal.getLocation());
            assertThat(terminal1.isActive()).isEqualTo(terminal.isActive());
        });
    }

    @Test
    public void testFindAll() {
        Iterable<Terminal> terminals = terminalRepository.findAll();
        Iterator<Terminal> terminalIterator = terminals.iterator();

        int i = 0;
        while(terminalIterator.hasNext()) {
            i++;
            terminalIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByOwner() {
        Iterable<Terminal> terminals = terminalRepository.findAllByOwner(savedTerminal.getOwner());
        Iterator<Terminal> terminalIterator = terminals.iterator();

        int i = 0;
        while(terminalIterator.hasNext()) {
            i++;
            terminalIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(1);
    }


    @After
    public void testDelete() {
        terminalRepository.deleteAll();
        assertEquals(terminalRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
