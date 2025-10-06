package com.labify.backend.pickup.request.entity;

public enum PickupRequestStatus {
    REQUESTED,  // 요청됨
    SCHEDULED,  // 수거 예정됨
    COMPLETED,  // 완료
    CANCELED    // 취소
}