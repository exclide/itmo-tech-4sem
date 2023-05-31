package is.tech.auth;

import is.tech.repository.UserRepository;
import is.tech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User saveUser(User user) {
        User foundUser = userRepository.findById(user.getId()).orElse(null);
        if (foundUser != null) {
            foundUser.setUsername(user.getUsername());
            foundUser.setPassword(user.getPassword());
            foundUser.setRoles(user.getRoles());
        }

        return userRepository.save(user);
    }


    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new SecurityException("Can't find user with id: " + id));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
