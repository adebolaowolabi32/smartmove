package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user;

    private User savedUser;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Alice");
        user.setLastName("Com");
        
        Set<Integer> b = new HashSet<Integer>();
        
        user.setRole(Enum.Role.VEHICLE_OWNER);
        user.setEmail("alice@example.com");
        user.setMobileNo("123456789");
        User user2 = new User();
        user2.setFirstName("Bob");
        user2.setLastName("Net");
        user2.setRole(Enum.Role.AGENT);
        user2.setEmail("bob@example.com");
        user2.setMobileNo("987654321");
        assertNotNull(userRepository.save(user2));
        savedUser = userRepository.save(user);
        assertNotNull(savedUser);
    }


    @Test
    public void testGetAll(){
        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 2);
    }


    @Test
    public void testFindById() {
        userRepository.findById(user.getId()).ifPresent(user1 -> {
            assertEquals(user1.getFirstName(), user.getFirstName());
            assertEquals(user1.getLastName(), user.getLastName());
            assertEquals(user1.getRole(), user.getRole());
            assertEquals(user1.getEmail(), user.getEmail());
            assertEquals(user1.getMobileNo(), user.getMobileNo());
        });
    }

    @Test
    public void testFindByEmail(){
        userRepository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            assertEquals(user1.getFirstName(), user.getFirstName());
            assertEquals(user1.getLastName(), user.getLastName());
            assertEquals(user1.getRole(), user.getRole());
            assertEquals(user1.getMobileNo(), user.getMobileNo());
        });
    }

    @Test
    public void testFindByMobileNumber() {
        userRepository.findByMobileNo(user.getMobileNo()).ifPresent(user1 -> {
            assertEquals(user1.getFirstName(), user.getFirstName());
            assertEquals(user1.getLastName(), user.getLastName());
            assertEquals(user1.getRole(), user.getRole());
            assertEquals(user1.getEmail(), user.getEmail());
        });
    }

    @After
    public void testDelete() {
        userRepository.deleteAll();
        assertEquals(userRepository.findAll().iterator().hasNext(), false);
    }
}
