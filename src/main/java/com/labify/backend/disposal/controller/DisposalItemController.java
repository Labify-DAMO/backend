package com.labify.backend.disposal.controller;

import com.labify.backend.auth.service.CustomUserDetails;
import com.labify.backend.disposal.dto.DisposalCreateRequestDto;
import com.labify.backend.disposal.dto.DisposalResponseDto;
import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.service.DisposalItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diposals")
@RequiredArgsConstructor
public class DisposalItemController {
    private DisposalItemService disposalItemService;

    // [POST] /diposals
    // 폐기물 등록
    @PostMapping
    public ResponseEntity<DisposalResponseDto> registerDisposal(
            @RequestBody DisposalCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        DisposalItem item = disposalItemService.registerDisposal(dto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(DisposalResponseDto.from(item));
    }
}
