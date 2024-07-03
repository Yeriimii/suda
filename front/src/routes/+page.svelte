<script>
  import Header from "../components/Header.svelte";
  import {writable} from "svelte/store";
  import {onMount} from "svelte";
  import ChatMenuPopup from "../components/ChatPopup.svelte";

  export const jwtToken = writable("");

  let login = false;
  let onChatMenuPopup = false;

  // Function to store JWT token from URL parameters
  const getTokens = () => {
    const tokens = {
      "jwt-access-token": "",
      "jwt-refresh-token": ""
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
  }

  const storeJwtToken = (name, token) => {
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

    if (payload) {
      jwtToken.set(payload);
      localStorage.setItem(name, JSON.stringify(payload));
      console.log(`${name} token!`, payload)
    }
  };

  const onChatMenuPopupClick = () => {
    console.log("click!", onChatMenuPopup)
    onChatMenuPopup = !onChatMenuPopup;
  }

  // Store the JWT token on mount
  onMount(() => {
    const tokens = getTokens();

    if (tokens.jwt !== undefined) {
      storeJwtToken('jwt', tokens.jwt);
      storeJwtToken('refresh', tokens.refresh);
    }

    if (localStorage.getItem('jwt') !== null) {
      login = true;
    }
  });
</script>

<svelte:head>
    <title>우리들의 수다, Suda</title>
</svelte:head>

<Header login={login} />

<div class="fixed bottom-16 right-16">
  <div class="relative flex justify-center items-center ">
    <button type="button" class="rounded-full size-14 text-sm bg-sky-300/80 hover:bg-sky-300 active:transition-all active:duration-75 active:scale-[0.98]" on:click={onChatMenuPopupClick}>
      <span class="text-white text-[24px]">
        <i class="bi bi-chat-fill"></i>
      </span>
    </button>
    {#if onChatMenuPopup}
      <ChatMenuPopup bind:onPopupOpen={onChatMenuPopup} login={login} />
    {/if}
  </div>
</div>
