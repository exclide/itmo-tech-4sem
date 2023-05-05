package is.tech.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_role")
public class Role implements GrantedAuthority {
    @Id
    @Column(name="role_id")
    private Long id;
    private String name;

    @Override
    public String getAuthority() {
        return this.getName();
    }
}
