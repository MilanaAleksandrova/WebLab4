package ru.itmo.web.lab4.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.itmo.web.lab4.entity.User;
import ru.itmo.web.lab4.security.PasswordHasher;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AuthService {

    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 128;

    @PersistenceContext(unitName = "lab4PU")
    private EntityManager em;

    public User register(String rawUsername, String rawPassword) {
        String username = validateUsername(rawUsername);
        String password = validatePassword(rawPassword);

        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = new User(username, PasswordHasher.sha256(password), LocalDateTime.now());

        em.persist(user);
        return user;
    }

    public User login(String rawUsername, String rawPassword) {
        String username = validateUsername(rawUsername);
        String password = validatePassword(rawPassword);

        User user = findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String hash = PasswordHasher.sha256(password);
        if (!user.getPasswordHash().equals(hash)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    public User findById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(User.class, id);
    }

    private User findByUsername(String username) {
        List<User> users = em.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username",
                        User.class
                )
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();

        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    private String validateUsername(String rawUsername) {
        if (rawUsername == null) {
            throw new IllegalArgumentException("Username is required");
        }
        String username = rawUsername.trim();
        if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username length must be between 3 and 50");
        }
        return username;
    }

    private String validatePassword(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Password is required");
        }
        if (rawPassword.length() < MIN_PASSWORD_LENGTH || rawPassword.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password length must be between 6 and 128");
        }
        return rawPassword;
    }
}
