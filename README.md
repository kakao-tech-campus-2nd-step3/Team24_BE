# Team24_BE
24조 백엔드_박수빈

## 개발 스프린트 2주차

### 1. UserProfile 구현
- 구현 내용
  - 일단 네이버 로그인 작동시 토큰발급
  - 발급된 토큰으로 현재 접속한 계정정보 가져오기
  - get 하면 현재 계정정보와 userprofile에 들어가는 내용 출력
  - post 하면 본문에 들어간 내용으로 회원 정보 수정
- 엔티티에 들어가는 내용
  -  profileId : 1부터 1씩 증가시키는 genereted value
  -  userId : 현재 접속한 계정의 이메일
  -  userNickName : default
  -  userBody : default
  - userimageUrl : default
  - point : 1000
- 시행착오
  - 원래는  userId에 현재 접속중인 id값을 넣으려고 했는데
  그 아이디 값이 토큰처럼 지저분한 문자열이라 이메일 값을 입력받아
  userid에 맵핑했습니다. 

  
