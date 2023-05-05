package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.auth.UserDetailsServiceImpl;
import is.tech.token.CustomAuthenticationToken;
import is.tech.token.TokenService;
import is.tech.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class LoginController {
    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl userDetailsService;

    public LoginController(TokenService tokenService, AuthenticationManager authManager,
            UserDetailsServiceImpl userDetailsService) {
        this.tokenService = tokenService;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
    }

    record LoginRequest(String username, String password) {};
    @PostMapping(value="/api/login")
    public String login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username, request.password);
        Authentication auth = authManager.authenticate(authenticationToken);

        User user = (User) userDetailsService.loadUserByUsername(request.username);

        return tokenService.generateAccessToken(user);
    }

    @GetMapping("/api/token/parse")
    @SecurityRequirement(name = "bearerAuth")
    public String parseToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return tokenService.parseToken(token);
    }

    @GetMapping("/api/authDetails")
    @SecurityRequirement(name = "bearerAuth")
    public String authDetails(CustomAuthenticationToken auth) {
        return "User id: " + auth.getUserId();
    }
}
