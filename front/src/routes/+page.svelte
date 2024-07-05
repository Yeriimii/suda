<script>
  import { onMount } from "svelte";
  import { member } from "$lib/auth.js"
  import Header from "../components/Header.svelte";
  import ChatMenuPopup from "../components/ChatPopup.svelte";

  let login = false;
  let onChatMenuPopup = false;

  const onChatMenuPopupClick = () => {
    onChatMenuPopup = !onChatMenuPopup;
  }

  onMount(() => {
    member.init(); // 회원 스토어 초기화

    const storedMember = localStorage.getItem('member');
    if (storedMember !== null) {
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
    <button type="button" class="rounded-full size-14 text-sm bg-sky-300/80 hover:bg-sky-300 hover:drop-shadow-lg active:transition-all active:duration-75 active:scale-[0.98]" on:click={onChatMenuPopupClick}>
      <span class="text-white text-[24px]">
        <i class="bi bi-chat-fill"></i>
      </span>
    </button>
    {#if onChatMenuPopup}
      <ChatMenuPopup bind:onPopupOpen={onChatMenuPopup} login={login} />
    {/if}
  </div>
</div>
