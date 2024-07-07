## Suda
간단한 채팅 서비스를 제공하는 프로젝트입니다.  

## Suda 서비스 개요
- 사용자는 Google 또는 Kakao 계정으로 로그인하면 채팅방을 개설할 수 있습니다.
- 개설된 채팅방의 주소를 공유해서 다른 사용자를 초대할 수 있습니다.
- 채팅방에서 텍스트뿐만 아니라 이미지도 공유할 수 있습니다.

## 채팅방 화면
### 기본 화면
<img src="https://gist.github.com/assets/87357932/03722a84-db51-4c66-a11d-e5bac1a4e920" alt="채팅방 예시" width="350" height="450">

### 새로운 채팅이 생겼을 때 스크롤 이동
<img src="https://gist.github.com/assets/87357932/fe87e69e-d431-4b48-96bf-b6032765bea1" alt="스크롤 이동" width="350">

## ERD
<img src="https://gist.github.com/assets/87357932/496394ad-53ac-43ec-8dd0-23257d7d0719" alt="ERD" width="350">

- CHAT_ROOMS 테이블 의 pk 가 uuid 인 이유: 채팅방에 초대할 때 외부에 노출되기 때문에 사용자가 유추할 수 없도록 의도했습니다.
