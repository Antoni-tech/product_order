package kz.symtech.antifraud.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_seq")
    @SequenceGenerator(
            name = "app_seq",
            sequenceName = "app_seq"
    )
    protected Long id;
}
