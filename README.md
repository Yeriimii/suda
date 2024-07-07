## Suda
간단한 채팅 서비스를 제공하는 프로젝트입니다.  

## Suda 서비스 개요
- 사용자는 Google 또는 Kakao 계정으로 로그인하면 채팅방을 개설할 수 있습니다.
- 개설된 채팅방의 주소를 공유해서 다른 사용자를 초대할 수 있습니다.
- 채팅방에서 텍스트뿐만 아니라 이미지도 공유할 수 있습니다.

## 채팅방 화면
### 기본 화면
<img src="https://github.com/Yeriimii/suda/assets/87357932/7f4d9bd4-d787-42ba-9990-352f40544995" alt="채팅방 예시" width="350" height="450">

### 새로운 채팅이 생겼을 때 스크롤 이동
<img src="https://github.com/Yeriimii/suda/assets/87357932/f288a588-b153-4d35-a60b-03560a45ecf7" alt="스크롤 이동" width="350">

## ERD
<img src="https://github-production-user-asset-6210df.s3.amazonaws.com/87357932/344742428-496394ad-53ac-43ec-8dd0-23257d7d0719.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20240703%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240703T152855Z&X-Amz-Expires=300&X-Amz-Signature=0ef4f4c4a933479c5290a1a040490de4dfbdd2479e840c54c84e21fcd653b497&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=0" alt="ERD" width="350">

- CHAT_ROOMS 테이블 의 pk 가 uuid 인 이유: 채팅방에 초대할 때 외부에 노출되기 때문에 사용자가 유추할 수 없도록 의도했습니다.
