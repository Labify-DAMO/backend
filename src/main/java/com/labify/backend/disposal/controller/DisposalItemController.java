package com.labify.backend.disposal.controller;

import com.labify.backend.auth.service.CustomUserDetails;
import com.labify.backend.disposal.dto.DisposalCreateRequestDto;
import com.labify.backend.disposal.dto.DisposalListResponseDto;
import com.labify.backend.disposal.dto.DisposalResponseDto;
import com.labify.backend.disposal.dto.DisposalUpdateRequestDto;
import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.entity.DisposalStatus;
import com.labify.backend.disposal.service.DisposalItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disposals")
@RequiredArgsConstructor
public class DisposalItemController {
    private final DisposalItemService disposalItemService;

    // [POST] /disposals
    // 폐기물 등록
    @PostMapping
    public ResponseEntity<DisposalResponseDto> registerDisposal(
            @RequestBody DisposalCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        DisposalItem item = disposalItemService.registerDisposal(dto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(DisposalResponseDto.from(item));
    }

    // [PATCH] /disposals/{disposalItemId}
    @PatchMapping("/{disposalItemId}")
    public ResponseEntity<DisposalResponseDto> patchDisposalItem(
            @PathVariable Long disposalItemId,
            @RequestBody DisposalUpdateRequestDto request) {
        DisposalItem updated = disposalItemService.patchDisposalItem(disposalItemId, request);
        return ResponseEntity.ok(DisposalResponseDto.from(updated));
    }

    // [GET] /disposals?status=STORED (null 값 넣을 경우 전체 조회)
    @GetMapping
    public ResponseEntity<DisposalListResponseDto> getDisposalList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) DisposalStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        DisposalListResponseDto response = disposalItemService.getDisposalItemInfo(userDetails.getUser(), status, pageable);
        return ResponseEntity.ok(response);
    }
}
