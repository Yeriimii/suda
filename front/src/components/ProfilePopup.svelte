<script>
  import { createEventDispatcher } from "svelte";
  import { goto } from "$app/navigation";

  export let onProfilePopup

  const dispatch = createEventDispatcher();

  let user = {
    "username": null,
    "profileImageUrl": null
  }

  user = JSON.parse(localStorage.getItem('jwt'));

  console.log('popup: ', user)

  const onLogin = () => {
    goto('/login');
    onProfilePopup = !onProfilePopup;
  }

  const onLogout = () => {
    localStorage.removeItem('jwt');
    localStorage.removeItem('refresh');

    dispatch('logout', 'bye');

    onProfilePopup = !onProfilePopup;
  }
</script>

<div class="profile-popup-container" role="menu">
    {#if user === null}
        <button class="profile-popup-btn" role="menuitem" tabindex="0"
                on:click={(e) => {e.stopPropagation(); onLogin();}}>
            <span class="profile-popup-btn-label">로그인</span>
        </button>
    {:else if user !== null}
        <button class="profile-popup-btn" role="menuitem" tabindex="0"
                on:click={(e) => {e.stopPropagation();}}>
            <span class="profile-popup-btn-label">내 채팅방</span>
        </button>
        <button class="profile-popup-btn error" role="menuitem" tabindex="0"
                on:click={(e) => {e.stopPropagation(); onLogout();}}>
            <span class="profile-popup-btn-label">로그아웃</span>
        </button>
    {/if}
</div>

<style lang="postcss">
    .profile-popup-container {
        @apply absolute z-[99] top-[72px] right-[64px] w-[135px] min-w-[135px] bg-white drop-shadow-md rounded-xl animate-slidein
    }

    .profile-popup-btn {
        @apply w-full px-4 py-2 flex justify-center items-center border border-t-0 border-slate-300/80 text-[13px] text-slate-600
    }

    .profile-popup-btn.error {
        @apply text-red-500
    }

    .profile-popup-btn.error:hover {
        @apply text-white bg-red-500/85
    }

    .profile-popup-btn:only-child {
        @apply rounded-xl border hover:rounded-xl
    }

    .profile-popup-btn:not(:only-child):first-child {
        @apply rounded-xl rounded-b-none border-t
    }

    .profile-popup-btn:not(:only-child):last-child {
        @apply rounded-xl rounded-t-none border-b
    }

    .profile-popup-btn:not(.error):hover {
        @apply bg-gray-100
    }

    .profile-popup-btn-label {
        @apply text-center whitespace-nowrap
    }
</style>