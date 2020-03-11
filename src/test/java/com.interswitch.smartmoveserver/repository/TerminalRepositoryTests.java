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

import java.util.List;

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
        terminal.setEnabled(true);
        Terminal terminal1 = new Terminal();
        terminal1.setName("my_terminal2");
        terminal1.setType(Enum.TransportMode.BUS);
        terminal1.setOwner(user);
        terminal1.setLocation("my_location_two");
        terminal1.setEnabled(true);
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
            assertThat(terminal1.isEnabled()).isEqualTo(terminal.isEnabled());
        });
    }

    @Test
    public void testFindAll() {
        List<Terminal> terminals = terminalRepository.findAll();
        assertThat(terminals.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByOwner() {
        List<Terminal> terminals = terminalRepository.findAllByOwner(savedTerminal.getOwner());
        assertThat(terminals.size()).isGreaterThanOrEqualTo(1);
    }


    @After
    public void testDelete() {
        terminalRepository.deleteAll();
        assertEquals(terminalRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
