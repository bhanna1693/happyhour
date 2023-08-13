package com.bhanna.happyhour.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"yelp_id"})
})
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(name = "yelp_id", nullable = false)
    private String yelpId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "website", nullable = false)
    private String website;

    // if field is null, it indicates an error occurred during processing
    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_check_for_specials")
    private LocalDateTime latestSpecialsCheck;

    @Enumerated(EnumType.STRING) // Use EnumType.STRING to store enum as string
    @Column(name = "special_status", nullable = false)
    private SpecialCheckStatus specialCheckStatus = SpecialCheckStatus.INITIAL;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT) // Add this line to specify the fetch strategy
    @JsonManagedReference // prevents circular dependency during serialization
    private List<SpecialDetail> specialDetailList = List.of();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}