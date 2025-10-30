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


<br>

## 🚀 실행 방법

### 1️⃣ 사전 요구사항

- **JDK 17** 이상
- **MySQL 8.0** 이상
- **Gradle** (프로젝트에 포함된 Gradle Wrapper 사용 가능)

### 2️⃣ 데이터베이스 설정

MySQL에 데이터베이스를 생성합니다.

```sql
CREATE DATABASE Labify CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3️⃣ 환경 변수 설정

프로젝트 **루트 디렉토리**에 `.env` 파일을 생성하고 다음 내용을 입력합니다.

```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/Labify?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
AI_SERVER_URL=http://localhost:8000
JWT_SECRET=your-jwt-secret-key-here
```

### 4️⃣ 프로젝트 실행

#### Gradle Wrapper 사용 (권장)

```bash
# Windows
gradlew bootRun

# Mac/Linux
./gradlew bootRun
```

#### IDE에서 실행

1. IDE에 환경 변수 플러그인을 설치합니다 (IntelliJ의 경우 EnvFile 플러그인)
2. `BackendApplication.java` 파일을 실행합니다.

### 5️⃣ 서버 확인

- 기본 주소: `http://localhost:8080`
- API 테스트는 Postman 또는 Swagger UI를 사용하세요.

<br>

## 👥 팀원

<table>
    <tr>
        <th>Picture</th>
        <td align="center" width="150px">
        <a href="https://github.com/romdyfo"><img src="https://github.com/romdyfo.png" width="100px;" alt=""/></a>
        </td>
        <td align="center" width="150px">
        <a href="https://github.com/romdyfo"><img src="https://github.com/romdyfo.png" width="100px;" alt=""/></a>
        </td>
    </tr>
    <tr>
        <th>Name</th>
        <td align="center">
        <a href="https://github.com/romdyfo"><b>김지희</b></a>
        </td>
        <td align="center">
        <a href="https://github.com/romdyfo"><b>김지희</b></a>
        </td>
    </tr>
    <tr>
        <th>Position</th>
        <td align="center">
            Tech Leader<br/>
            Backend<br/>
        </td>
        <td align="center">
            Backend<br/>
        </td>
    </tr>
    <tr>
        <th>GitHub</th>
        <td align="center"><a href="https://github.com/romdyfo"><img src="http://img.shields.io/badge/romdyfo-green?style=social&logo=github"/></a></td>
        <td align="center"><a href="https://github.com/romdyfo"><img src="http://img.shields.io/badge/romdyfo-green?style=social&logo=github"/></a></td>
    </tr>
</table>
