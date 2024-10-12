# Team24_BE

## 개발 스프린트 4 리뷰 받고 싶은 내용

이가현
- 커밋 단위 & 커밋 메시지

24조
- Test Case 위주로 리뷰 받고싶습니다.
- 이번 주 작성한 테스트 코드는 다음과 같습니다
  - Challenge Repository, Challenge Service
  - History Service, History Controller
  - Security Test (아직 다 작성하지 않았지만 방향성이 맞는지 확인하기 위해 merge 했습니다!!)
  - Controller 와 Security 를 통합하여 단위테스트를 진행하려고 하니 너무 의존성이 복잡해서 분리했습니다.
    - Controller 테스트에서는 Controller 에만 집중하기 위해 Security 를 통과한 상황을 가정하고
    - Security Test 에서 Token 으로 인한 통과 성공, 실패 테스트를 진행했습니다.
    - 이렇게 테스트 하는게 맞는지 궁금합니다!!