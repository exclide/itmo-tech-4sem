package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.auth.UserDetailsServiceImpl;
import is.tech.models.CarManufacturer;
import is.tech.repository.RoleRepository;
import is.tech.user.Role;
import is.tech.user.User;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserDetailsServiceImpl userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserController(UserDetailsServiceImpl userService,
                          BCryptPasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    record RegistrationRequest(Long id, String username, String password) {};
    @PostMapping(value="/api/users")
    @Secured("SCOPE_ADMIN")
    public User addUser(@RequestBody RegistrationRequest request) {
        User user = new User(
                request.id,
                request.username,
                passwordEncoder.encode(request.password),
                new HashSet<>()
        );

        return userService.saveUser(user);
    }

    record RoleRequest(Long id, String roleName) {};
    @PostMapping(value="/api/roles")
    @Secured("SCOPE_ADMIN")
    public Role addRole(@RequestBody RoleRequest request) {
        Role role = new Role(request.id, request.roleName);

        return roleRepository.save(role);
    }

    @PutMapping(value="/api/users/{id}")
    @Secured("SCOPE_ADMIN")
    public User addRoleToUser(@PathVariable Long id, @RequestParam Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new SecurityException("Can't find role with id: " + roleId));

        User user = userService.findUserById(id);

        user.addRole(role);

        return userService.saveUser(user);
    }

    @GetMapping(value="/api/users")
    //@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Secured("SCOPE_ADMIN")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
