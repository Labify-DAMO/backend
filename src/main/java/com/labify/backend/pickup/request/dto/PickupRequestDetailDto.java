package com.labify.backend.pickup.request.dto;

import com.labify.backend.disposal.dto.DisposalItemSimpleDto;
import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PickupRequestDetailDto {
    private final Long requestId;
    private final LocalDate requestDate;
    private final PickupRequestStatus status;
    private final List<DisposalItemSimpleDto> disposalItems;

    public PickupRequestDetailDto(PickupRequest entity) {
        this.requestId = entity.getId();
        this.requestDate = entity.getRequestDate();
        this.status = entity.getStatus();
        this.disposalItems = entity.getItems().stream()
                .map(item -> new DisposalItemSimpleDto(item.getDisposalItem()))
                .collect(Collectors.toList());
    }
}