package kz.symtech.antifraud.adminservice.entities.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.symtech.antifraud.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_password_restore")
@Entity
@Getter
@Setter
public class UserPasswordRestore extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users user;

    @Column(name = "code", nullable = false, length = 40)
    private String code;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "used", nullable = false)
    private boolean used;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
