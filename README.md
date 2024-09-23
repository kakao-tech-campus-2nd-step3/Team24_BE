# Team24_BE
24조 백엔드

## 개발 스프린트 1주차

### 1. Spring Security 를 활용한 OAuth2 로그인 기능 구현
- **코드리뷰 받고 싶은 부분**
  - Access Token 과 Refresh Token 을 Cookie 에 넣어서 안드로이드에 반환하는 형식으로 구현을 했습니다.
  - 그 후 안드로이드 측에서 내부 저장소에 해당 값들을 저장하고 인증 인가가 필요한 api에 요청을 보낼 때 직접 헤더에 값을 넣어서 요청을 보내는 식으로 구현했습니다.
  - 위 방식이 옳은지 궁금합니다!!
  - Spring Security 를 활용한 인증 인가 방식의 흐름에서 어딘가 큰 잘못이 없는지에 대해서 리뷰를 받고 싶습니다!!
  - Spring Security 를 공부하면서 코드를 작성한 상황이라 디테일한 부분(Entity 검증 등)이 많이 떨어집니다 ㅎㅎ..
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
