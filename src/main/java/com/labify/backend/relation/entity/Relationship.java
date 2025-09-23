package com.labify.backend.relation.entity;

import com.labify.backend.facility.entity.Facility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "relationship")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationshipId;

    // 연구소 역할을 하는 Facility (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_facility_id", nullable = false)
    private Facility labFacility;

    // 수거업체 역할을 하는 Facility (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_facility_id", nullable = false)
    private Facility pickupFacility;
}