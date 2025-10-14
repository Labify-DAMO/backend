# 🔬 Labify Backend

연구실/실험실의 **스마트한 폐기물 관리**를 위한 백엔드 서버입니다.<br>
`JDK 17`, `Spring Boot 3.5.5`, `Gradle` 환경에서 개발되었습니다.

<br>

## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Build Tool | Gradle |
| ORM | Spring Data JPA |
| Database | MySQL |
| Security | Spring Security |
| 기타 | Lombok, Validation, JWT |

<br>

## 🗂️ 폴더 구조

```
src
├─main
│ ├─java/com/labify/backend
│ │ ├─ai
│ │ ├─auth
│ │ ├─config
│ │ ├─disposal
│ │ ├─facility
│ │ ├─inviterequest
│ │ ├─lab
│ │ ├─notification
│ │ ├─pickup
│ │ │ └─request
│ │ ├─qr
│ │ ├─user
│ │ ├─userfacilityrelation
│ │ ├─waste
│ │ └─BackendApplication.java
│ └─resources
│   └─application.yml
│   └─data.sql
└─test
  └─java/com/labify/backend
    └─BackendApplication.java

```


#### 도메인 내부 구조 (공통 패턴)

```
<domain>
├─controller    # API 요청 처리
├─dto           # 요청/응답 DTO
├─entity        # DB 테이블 매핑 엔티티
├─repository    # JPA Repository
└─service       # 비즈니스 로직
```

모든 도메인은 위 구조를 따릅니다.
