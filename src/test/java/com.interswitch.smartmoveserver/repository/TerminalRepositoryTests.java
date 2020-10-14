package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
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
public class TerminalRepositoryTests {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;

    private Terminal terminal;

    private Terminal savedTerminal;

    @BeforeAll
    public void setUp() {
        terminal = new Terminal();
        terminal.setName("my_terminal");
        terminal.setMode(Enum.TransportMode.RAIL);
        User user = buildTestUser();
        userRepository.save(user);
        terminal.setOwner(user);
        //terminal.setLocation("my_location_one");
        terminal.setEnabled(true);
        Terminal terminal1 = new Terminal();
        terminal1.setName("my_terminal2");
        terminal1.setMode(Enum.TransportMode.BUS);
        terminal1.setOwner(user);
        //terminal1.setLocation("my_location_two");
        terminal1.setEnabled(true);
        assertNotNull(terminalRepository.save(terminal1));
        savedTerminal = terminalRepository.save(terminal);
        assertNotNull(savedTerminal);
    }

    @Test
    public void testFindById() {
        terminalRepository.findById(savedTerminal.getId()).ifPresent(terminal1 -> {
            assertEquals(terminal1.getName(), terminal.getName());
            assertEquals(terminal1.getOwner(), terminal.getOwner());
            assertEquals(terminal1.getMode(), terminal.getMode());
            //assertEquals(terminal1.getLocation(), terminal.getLocation());
            assertEquals(terminal1.isEnabled(), terminal.isEnabled());
        });
    }

    @Test
    public void testFindAll() {
        List<Terminal> terminals = terminalRepository.findAll();
        assertTrue(terminals.size() >= 2);
    }

    @Test
    public void testFindByOwner() {
        List<Terminal> terminals = terminalRepository.findAllByOwner(savedTerminal.getOwner());
        assertTrue(terminals.size() >= 1);
    }


    @AfterAll
    public void testDelete() {
        terminalRepository.deleteAll();
        assertEquals(terminalRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
