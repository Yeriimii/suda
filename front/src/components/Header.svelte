<script>
  import ProfilePopup from "./ProfilePopup.svelte";

  export let login;

  // 프로필 팝업창 토글
  let onProfilePopup = false;

  $: login;

  const onProfilePopupClick = () => {
    onProfilePopup = !onProfilePopup;
  }

  const closeProfilePopup = () => {
    onProfilePopup = false;
  }

  function handleLogout(event) {
    console.log('logout', event.detail)
    login = null;
  }
</script>

<header id="header" class="fixed top-0 left-0 w-screen z-[10]">
    <div class="flex h-[92px] px-16 py-[1.5rem] justify-between items-center">
        <div id="logo w-[88px] h-[46px]">
            <a class="text-gray-800 font-bold text-6xl select-none" href="http://localhost:5173">SUDA</a>
        </div>

        <button type="button" class="account border border-gray-400/70 flex justify-between items-center gap-2 p-4 w-[78px] h-[40px] text-sm rounded-full bg-white hover:drop-shadow-lg active:transition-all active:duration-100 active:scale-[0.98]" on:click={onProfilePopupClick}>
            <span class="icon">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M3 12H21" stroke="#333333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M3 6H21" stroke="#333333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M3 18H21" stroke="#333333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            {#if login === true}
                <img class="w-[30px] h-[30px] rounded-full" src={JSON.parse(localStorage.getItem('jwt')).profileImageUrl} alt="프로필 이미지"/>
            {:else}
                <span class="user flex justify-center items-center size-[32px] font-bold p-[7px] text-[8px] rounded-full text-white bg-gray-500">
                <svg width="30" height="30" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21" stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z" stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            {/if}
        </button>
        {#if onProfilePopup}
            <div class="overlay" on:click={closeProfilePopup}></div>
            <ProfilePopup bind:onProfilePopup={onProfilePopup} on:logout={handleLogout} />
        {/if}
    </div>

</header>