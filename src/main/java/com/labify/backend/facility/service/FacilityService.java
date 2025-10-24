package com.labify.backend.facility.service;

import com.labify.backend.common.response.GeneralException;
import com.labify.backend.facility.dto.FacilityInfoResponseDto;
import com.labify.backend.facility.dto.FacilityRequestDto;
import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.facility.exception.FacilityErrorCode;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Transactional
    public Facility registerFacility(User manager, FacilityRequestDto dto) {
        // 컨트롤러에서 받은 manager 객체를 그대로 사용! (DB 재조회 불필요)
        if (manager == null) {
            throw new IllegalStateException("관리자 정보를 찾을 수 없습니다.");
        }

        Facility facility = new Facility();
        // 사용자에게 받은 정보 이용
        facility.setType(dto.getType());
        facility.setName(dto.getName());
        facility.setAddress(dto.getAddress());
        // 서버에서 랜덤 코드 생성해서 저장
        facility.setFacilityCode(generateUniqueFacilityCode());
        facility.setManager(manager);

        facilityRepository.save(facility);

        manager.setFacility(facility);
        userRepository.save(manager);

        return facility;
    }

    // 유니크한 코드가 나올 때까지 코드를 재생성하는 메서드
    private String generateUniqueFacilityCode() {
        String code;
        do {
            code = generateFacilityCode();
        } while (facilityRepository.existsByFacilityCode(code)); // DB에 코드가 존재하는 동안 반복
        return code;
    }

    // 6자리의 영문 대문자+숫자 조합 코드를 생성
    private String generateFacilityCode() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();

        return IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))))
                .collect(Collectors.joining());
    }

    @Transactional
    public Facility getFacilityByCode(String code) {
        return facilityRepository.findByFacilityCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));
    }

    @Transactional
    public FacilityInfoResponseDto getMyFacility(User authUser) {
        // LazyInitializationException 방지: facility까지 같이 로드됨
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Facility facility = user.getFacility();
        if (facility == null) {
            throw new GeneralException(FacilityErrorCode.FACILITY_NOT_FOUND);
        }

        return FacilityInfoResponseDto.from(facility);
    }
}
