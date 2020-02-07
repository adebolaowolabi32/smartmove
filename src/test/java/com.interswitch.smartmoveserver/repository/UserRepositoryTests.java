package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user1 = new User();
        user1.setId("user1");
        user1.setFirstName("Alice");
        user1.setLastName("Com");
        user1.setType(User.UserType.BUS_OWNER);
        user1.setEmailAddress("alice@example.com");
        user1.setMobileNumber("123456789");
        User user2 = new User();
        user2.setId("user2");
        user2.setFirstName("Bob");
        user2.setLastName("Net");
        user2.setType(User.UserType.AGENT);
        user2.setEmailAddress("bob@example.com");
        user2.setMobileNumber("987654321");
        userRepository.save(user1);
        userRepository.save(user2);
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
        assertNotEquals(user1.getId(), 0);
        assertNotEquals(user1.getId(), 0);



    }
    @Test
    public void testFetchByName(){
        User userA = userRepository.findByEmailAddress("alice@example.com");
        assertNotNull(userA);
        assertEquals(userA.getFirstName(), "Alice");
        assertEquals(userA.getLastName(), "Com");
        assertEquals(userA.getType(), User.UserType.BUS_OWNER);
        assertEquals(userA.getEmailAddress(), "alice@example.com");
        assertEquals(userA.getMobileNumber(), "123456789");
    }

    @Test
    public void testFetchAll(){
        Iterable<User> users = userRepository.findAll();
        int count = 0;
        if (users instanceof Collection) {
            count = ((Collection<?>) users).size();
        }
        else{
            for(User p : users){
                count++;
            }
        }
        assertEquals(count, 2);
    }
}
