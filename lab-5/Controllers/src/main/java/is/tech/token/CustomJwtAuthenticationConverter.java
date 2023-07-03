package is.tech.token;

import is.tech.auth.UserDetailsServiceImpl;
import is.tech.models.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;


public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserDetailsServiceImpl userService;
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomJwtAuthenticationConverter(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public CustomAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(source);
        String principalClaimValue = source.getSubject();
        User user = (User) userService.loadUserByUsername(principalClaimValue);
        return new CustomAuthenticationToken(source, authorities, principalClaimValue, user.getId(), user.getCarManufacturerId());
    }


}