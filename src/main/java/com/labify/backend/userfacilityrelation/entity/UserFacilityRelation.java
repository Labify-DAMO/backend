package com.labify.backend.userfacilityrelation.entity; // 새 패키지를 만듭니다.

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_facility_relation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFacilityRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Builder
    public UserFacilityRelation(User user, Facility facility) {
        this.user = user;
        this.facility = facility;
    }
}