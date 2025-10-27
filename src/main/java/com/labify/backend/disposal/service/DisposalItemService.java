package com.labify.backend.disposal.service;

import com.labify.backend.disposal.dto.DisposalCreateRequestDto;
import com.labify.backend.disposal.dto.DisposalListResponseDto;
import com.labify.backend.disposal.dto.DisposalResponseDto;
import com.labify.backend.disposal.dto.DisposalUpdateRequestDto;
import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.entity.DisposalStatus;
import com.labify.backend.disposal.repository.DisposalItemRepository;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.repository.LabRepository;
import com.labify.backend.user.entity.User;
import com.labify.backend.waste.entity.WasteType;
import com.labify.backend.waste.repository.WasteTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalItemService {
    private final LabRepository labRepository;
    private final WasteTypeRepository wasteTypeRepository;
    private final DisposalItemRepository disposalItemRepository;

    // 폐기물 등록 로직
    @Transactional
    public DisposalItem registerDisposal(DisposalCreateRequestDto dto, User createdBy) {
        Lab lab = labRepository.findById(dto.getLabId())
                .orElseThrow(() -> new EntityNotFoundException("Lab not found"));

        WasteType wasteType = wasteTypeRepository.findByName(dto.getWasteTypeName())
                .orElseThrow(() -> new EntityNotFoundException("WasteType not found: " + dto.getWasteTypeName()));

        DisposalItem disposalItem = new DisposalItem();

        disposalItem.setLab(lab);
        disposalItem.setWasteType(wasteType);
        disposalItem.setWeight(dto.getWeight());
        disposalItem.setUnit(dto.getUnit());
        disposalItem.setMemo(dto.getMemo());
        disposalItem.setAvailableUntil(dto.getAvailableUntil());
        disposalItem.setCreatedBy(createdBy);
        disposalItem.setStatus(DisposalStatus.STORED);
        disposalItem.setCreatedAt(LocalDateTime.now());

        return disposalItemRepository.save(disposalItem);

    }

    // 폐기물 등록 정보 수정 로직
    @Transactional
    public DisposalItem patchDisposalItem(Long itemId, DisposalUpdateRequestDto request) {
        DisposalItem item = disposalItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("DisposalItem not found"));

        // null이 아닌 필드만 업데이트
        if (request.getWasteTypeId() != null) {
            item.setWasteType(wasteTypeRepository.findById(request.getWasteTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("WasteType not found")));
        }

        if (request.getWeight() != null) {
            item.setWeight(request.getWeight());
        }

        if (request.getUnit() != null) {
            item.setUnit(request.getUnit());
        }

        if (request.getMemo() != null) {
            item.setMemo(request.getMemo());
        }

        if (request.getStatus() != null) {
            item.setStatus(request.getStatus());
        }

        if (request.getAvailableUntil() != null) {
            item.setAvailableUntil(request.getAvailableUntil());
        }

        return item;
    }

    // 폐기물 전체 조회 (또는 필터링 가능)
    @Transactional
    public DisposalListResponseDto getDisposalItemInfo(User user, DisposalStatus status, Pageable pageable) {
        Page<DisposalItem> itemPage = disposalItemRepository.findDisposalItemsByStatus(user.getUserId(), status, pageable);

        List<DisposalResponseDto> itemDtos = itemPage.getContent().stream()
                .map(DisposalResponseDto::from)
                .toList();

        long totalCount = itemPage.getTotalElements();

        return DisposalListResponseDto.builder()
                .totalCount(totalCount)
                .disposalItems(itemDtos)
                .build();
    }
}
