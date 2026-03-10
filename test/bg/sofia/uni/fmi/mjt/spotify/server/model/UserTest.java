package bg.sofia.uni.fmi.mjt.spotify.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserEmail() {
        User user = new User("test@abv.bg", "123456");

        assertEquals("test@abv.bg", user.email());
    }

    @Test
    void testEqualsSameEmailDiffPassword() {
        User user1 = new User("test@abv.bg", "123456");
        User user2 = new User("test@abv.bg", "1234567");

        assertEquals(user1, user2);
    }

    @Test
    void testEqualsDiffEmail() {
        User user1 = new User("asd@abv.bg", "123456");
        User user2 = new User("test@abv.bg", "123456");

        assertNotEquals(user1, user2);
    }

    @Test
    void testEqualsWithNull() {
        User user = new User("test@abv.bg", "1234556");

        assertNotEquals(user, null);
    }

    @Test
    void testHashCodeSameEmail() {
        User user1 = new User("test@abv.bg", "123455456");
        User user2 = new User("test@abv.bg", "1234556");

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
