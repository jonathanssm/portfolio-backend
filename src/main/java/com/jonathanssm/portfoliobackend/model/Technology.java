package com.jonathanssm.portfoliobackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
@Table(name = "technologies", schema = "portfolio")
@AuditTable(value = "technologies_aud", schema = "portfolio_aud")
@EntityListeners(AuditingEntityListener.class)
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private TechnologyType type;

    @Column(name = "icon_url")
    private String iconUrl;

    private Long version;
}
