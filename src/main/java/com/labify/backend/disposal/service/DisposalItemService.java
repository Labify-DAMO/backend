package com.labify.backend.disposal.service;

import com.labify.backend.disposal.dto.DisposalCreateRequestDto;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisposalItemService {
    private LabRepository labRepository;
    private WasteTypeRepository wasteTypeRepository;
    private DisposalItemRepository disposalItemRepository;

    // 폐기물 등록 로직
    @Transactional
    public DisposalItem registerDisposal(DisposalCreateRequestDto dto, User createdBy) {
        Lab lab = labRepository.findById(dto.getLabId()).orElseThrow(() -> new EntityNotFoundException("Lab not found"));
        WasteType wasteType = wasteTypeRepository.findById(dto.getWasteTypeId()).orElseThrow(() -> new EntityNotFoundException("Waste not found"));

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


}
