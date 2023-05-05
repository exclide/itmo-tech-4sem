package is.tech.token;

import is.tech.user.User;
import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomAuthenticationToken extends JwtAuthenticationToken {
    private long userId;
    private Long manufacturerId;

    public CustomAuthenticationToken(Jwt jwt) {
        super(jwt);
    }

    public CustomAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public CustomAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
    }

    public CustomAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name, long userId, Long manufacturerId) {
        super(jwt, authorities, name);
        this.userId = userId;
        this.manufacturerId = manufacturerId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "CustomAuthenticationToken{" +
                "userId=" + userId +
                '}';
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }
}
