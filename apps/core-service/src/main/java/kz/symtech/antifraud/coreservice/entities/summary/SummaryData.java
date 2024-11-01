package kz.symtech.antifraud.coreservice.entities.summary;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class SummaryData implements Serializable {

    @Id
    @Column
    @GeneratedValue
    private UUID id;

    private Short version;

    private Long userCreateId;

    @Column(nullable = false)
    private String email;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Date createDate;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updateDate;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private SummaryDataType type;

    @OneToMany(mappedBy = "summaryData", cascade = CascadeType.ALL)
    private List<SummaryDataVersion> summaryDataVersions;
}
