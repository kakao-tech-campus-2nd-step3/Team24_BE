# Team24_BE
24조 백엔드 - 장진영

## 개발 스프린트 1주차

### 1. Spring Security 를 활용한 OAuth2 로그인 기능 구현
- 구현 내용
    - 백엔드 서버에서 OAuth2 로그인 처리를 다하고 서버에서 JWT 발급
    - ACCESS TOKEN 과 REFRESH TOKEN 을 COOKIE 에 저장 후 안드로이드 측에 전달
    - ACCESS TOKEN 만료 시 정해진 API(/reissue) 로 요청을 보내 REFRESH TOKEN 을 이용한 재발급
    - 로그아웃 시 저장된 REFRESH TOKEN 삭제
- 앞으로 할 부분
    - 코드 리팩토링 : 문자열, 숫자 상수 도입
    - 회원 ROLE Enum 도입
    - 카카오 OAuth2 등록
    - 안드로이드 측과 로그인 기능 연결 (CORS 등 해결 필요)

## 개발 스프린트 2주차

### 1. Kakao Login 기능 추가

## 개발 스프린트 3주차

### 1. 팀원들 코드 리팩토링 및 결제 모듈 공부

## 개발 스프린트 4주차

### 1. 유저 HISTORY 개발
### 2. 기존 Category Class 삭제 -> Category Enum Class 도입
