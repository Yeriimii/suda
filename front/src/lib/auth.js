import { get, writable } from "svelte/store";

function loadState(key, initial) {
  if (typeof window === 'undefined') {
    return initial; // 서버 사이드 렌더링 시 초기값 반환
  }
  const stored = localStorage.getItem(key);
  return stored ? JSON.parse(stored) : initial;
}

// 상태를 로컬 스토리지에 저장하는 함수
function saveState(key, state) {
  localStorage.setItem(key, JSON.stringify(state));
  console.log(`${key} is stored to LocalStorage!`, state)
}

function setMember() {
  const initValue = {
    id: '',
    username: '',
    profileImageUrl: '',
  };

  // 로컬 스토리지에서 멤버를 가져옴
  const storedMember = loadState('member', initValue);

  const { subscribe, update, set } = writable({...storedMember});

  const init = () => {
    // 로그인 상태면 조기 종료
    if (get(member).id !== '') {
     return console.log('현재 로그인 된 상태입니다.', get(member).id)
    }

    const tokens = getTokens();
    if (tokens.jwt !== "") {
      storeToken('jwt', tokens.jwt); // jwt
      storeToken('refresh', tokens.refresh); // refresh 토큰
      const loginId = storeMemberState(getPayload(tokens.jwt));
      console.log('로그인 상태를 저장했습니다.', loginId);
    } else {
      console.log('로그인 정보가 없습니다.');
    }
  }

  const getTokens = () => {
    const tokens = {
      jwt: "",
      refresh: ""
    }

    const cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
      let cookie = cookies[i].trim();
      if (cookie.indexOf('jwt-access-token=') === 0) {
        console.log("jwt cookie!", cookie.substring(17, cookie.length));
        tokens.jwt = cookie.substring(17, cookie.length);
      }
      if (cookie.indexOf('jwt-refresh-token=') === 0) {
        console.log("ref cookie!", cookie.substring(18, cookie.length));
        tokens.refresh = cookie.substring(18, cookie.length);
      }
    }
    return tokens;
  };

  const getPayload = (token) => {
    const base64Payload = token.split('.')[1];
    const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(
      decodeURIComponent(
        window.atob(base64).split('')
          .map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
          })
          .join('')
      )
    );
    return payload;
  }

  const storeToken = (name, token) => {
    localStorage.setItem(name, token);
    console.log(`${name} token is stored to LocalStorage!`, token)
  };

  const storeMemberState = (payload) => {
    update(data => {
      member.id = payload.id;
      member.username = payload.username;
      member.profileImageUrl = payload.profileImageUrl;
      saveState('member', member);
      return member;
    });
    return payload.id;
  };

  const logout = () => {
    set({...initValue});
    localStorage.removeItem('member');
    localStorage.removeItem('jwt');
    localStorage.removeItem('refresh');
  };

  return {
    subscribe,
    init,
    logout,
  };
}

export const member = setMember();