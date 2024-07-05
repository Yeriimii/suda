<script>
  import { createEventDispatcher } from "svelte";

  export let value = '';
  export let minRows = 1;
  export let maxRows;

  $: minHeight = `${1 + minRows * 1.2}em`;
  $: maxHeight = maxRows ? `${1 + maxRows * 1.2}em` : `auto`;

  let dispatch = createEventDispatcher();

  const handlePress = (event) => {
    if (event.keyCode === 13) {
      if (!event.shiftKey) {
        event.preventDefault();
        dispatch('send')
      }
    }
  }

</script>

<div class="container max-w-[214px]">
	<pre
      class="p-[0.5em]"
      aria-hidden="true"
      style="min-height: {minHeight}; max-height: {maxHeight}"
  >{value + '\n'}</pre>

  <textarea class="text-[15px] px-2 py-2" bind:value placeholder="채팅 입력..." autocomplete="off" on:keydown={handlePress}></textarea>
</div>

<style lang="postcss">
    .container {
        position: relative;
    }

    pre, textarea {
        font-family: inherit;
        box-sizing: border-box;
        border: 1px solid #eee;
        line-height: 1.2;
        overflow: hidden;
    }

    textarea {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        resize: none;
    }

    textarea {
        @apply outline-none border-b-2 border-0
    }

    textarea:focus {
        @apply transition-all duration-500 border-b-black
    }
</style>