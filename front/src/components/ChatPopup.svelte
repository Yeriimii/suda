<script>
  import Textarea from "./Textarea.svelte";
  import { beforeUpdate, afterUpdate, onDestroy, onMount } from "svelte";
  import { connect } from "../lib/stompClient"
  import { member } from "$lib/auth"
  import { get } from "svelte/store";
  import "dayjs/locale/ko";
  import relativeTime from "dayjs/plugin/relativeTime";
  import dayjs from "dayjs";

  export let onPopupOpen;
  export let login;

  dayjs.extend(relativeTime);
  dayjs.locale("ko");

  let message = '';
  let chatLog = [];
  let stompClient;

  $: login;
  $: message;
  $: chatLogs = chatLog;
  $: WelcomeMessage = stompClient === null ? '연결 중입니다.' : '연결 되었습니다.';

  $: user = {
    id: 0,
    username: 'guest',
    profileImageUrl: null
  };

  const closePopup = () => {
    onPopupOpen = false;
  }

  onMount(() => {
    user = {
      id: get(member).id,
      username: get(member).username,
      profileImageUrl: get(member).profileImageUrl
    };

    stompClient = connect("ws://localhost:8080/suda", message => {
      console.log('received message', message)
      chatLog = [...chatLog, message]; // TODO: 채팅 내역을 임시 저장하는 스토어 생성 필요
      chatLogs = chatLog
      console.log('chatLogs:', chatLogs)
    });
  });

  onDestroy(() => {
    stompClient.disconnect();
  })

  let previousChatCount = 0;

  // DOM 업데이트 전에 호출됩니다.
  beforeUpdate(() => {
    previousChatCount = chatLogs.length - 1; // 메세지가 전송되자마자 chatLogs 가 증가하므로 1을 뺀다.
  });

  // DOM 업데이트 후에 호출됩니다.
  afterUpdate(() => {
    // 새로운 채팅 메시지가 추가된 경우
    if (chatLogs.length > previousChatCount) {
      const newChatId = chatLogs.length - 1;
      const newChatElement = document.querySelector(`[data-chat-id="${newChatId}"]`);
      if (newChatElement) {
        newChatElement.scrollIntoView({behavior: 'smooth'});
      }
    }
  });

  const sendMessage = () => {
    console.log(user !== null ? user.username : 'tester', message);

    let chatRoomId = "1";

    const newMessage = {
      id: user !== null ? user.id : 0,
      username: user !== null ? user.username : 'tester',
      profileImageUrl: user !== null ? user.profileImageUrl : null,
      message: message,
      timestamp: dayjs().format()
    };

    console.log('new:', newMessage);

    stompClient.sendMessage(`/pub/chat.message.${chatRoomId}`, newMessage);

    message = '';
  }

  const elapsedTime = (date) => {
    return dayjs(date).fromNow();
  };
</script>

<div
    class="chat-popup-container absolute z-[99] bottom-16 right-0 w-[320px] min-w-[280px] pl-3 pr-1 py-1 border bg-white drop-shadow-md rounded-xl animate-fade duration-300"
    role="menu">
  <!-- 헤더 -->
  <div class="flex justify-between items-center">
    <span>1:1 채팅방</span>
    <button type="button" on:click={closePopup}
            class="flex justify-center items-center size-12 rounded-full hover:bg-gray-200">
      <span class="text-gray-800 font-bold text-2xl">
        <i class="bi bi-x-lg"></i>
      </span>
    </button>
  </div>

  <!--  대화창  -->
  <div class="min-h-[280px] max-h-[280px] overflow-y-auto">
    <div class="flex flex-col gap-2">
      <span class="text-[12px] text-gray-400 text-center">{WelcomeMessage}</span>
      {#each chatLogs as chat, idx}
        <!-- 채팅 요소 하나 -->
        <div data-chat-id={idx} class="flex gap-2 justify-start items-center text-gray-800">
          <!-- 프로필 이미지 -->
          <div class="self-start">
            {#if chat.profileImageUrl !== null}
              <img class="w-[30px] h-[30px] rounded-full" src={chat.profileImageUrl} alt="프로필 이미지"/>
            {:else}
              <div class="size-8 min-w-8 min-h-8 bg-amber-200 rounded-full content-center">
                <span class="text-sm">guest</span>
              </div>
            {/if}
          </div>
          <!-- 채팅 내용 -->
          <div class="flex flex-col pl-1 pr-4 text-[14px] break-all whitespace-pre-wrap">
            <!-- 작성자 + 작성 시간 -->
            <div class="flex gap-2 items-end">
              <span class="font-bold">{chat.username}</span>
              <span class="text-[11px] text-gray-400">{elapsedTime(chat.timestamp)}</span>
            </div>
            <!-- 메시지 -->
            <div class="text-[13px]">{chat.message}</div>
          </div>
        </div>
      {/each}
    </div>
  </div>

  <!-- 입력 폼 -->
  <div class="flex w-full gap-3 my-1 justify-start items-center overflow">
    <!--  프로필 이미지  -->
    {#if login === true}
      <img class="w-[30px] h-[30px] rounded-full" src={user.profileImageUrl} alt="프로필 이미지"/>
    {:else}
      <div class="size-8 min-w-8 min-h-8 bg-amber-200 rounded-full content-center">
        <span class="text-sm">tester</span>
      </div>
    {/if}

    <!--  텍스트 입력창  -->
    <div class="w-full">
      <Textarea bind:value={message} minRows={1} maxRows={40} on:send={sendMessage}/>
    </div>

    <!--  전송 버튼  -->
    <button
        class="flex justify-center -ml-1 mr-1 p-2 size-8 min-w-8 min-h-8 rounded-full bg-sky-300/80 hover:bg-sky-300 active:transition-all active:scale-[0.98] active:duration-75"
        on:click={sendMessage}>
      <span class="text-md text-center -translate-y-0.5 text-white">
        <i class="bi bi-send-fill"></i>
      </span>
    </button>
  </div>
</div>

<style>

</style>