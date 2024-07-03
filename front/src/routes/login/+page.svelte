<script>
</script>

<div class="centered-container flex justify-center translate-y-[200px] min-h-screen">
    <div class="flex-col">
        <div class="flex flex-col my-10 justify-center items-center">
            <h1 class="text-gray-800 font-bold text-6xl select-none">SUDA</h1>

            <hr class="text-divider animate-slidein300">

            <a href="http://localhost:8080/oauth2/authorization/google" role="button" type="button" class="login-btn-oauth animate-slidein300">
                <span class="mr-1">
                    <i class="bi bi-google"></i>
                </span>
                Google 계정으로 로그인하기
            </a>

            <a href="http://localhost:8080/oauth2/authorization/kakao" role="button" type="button" class="login-btn-oauth animate-slidein300">
                <span class="mr-1">
                    <img class="size-[15px] select-none -translate-y-0.5 -translate-x-0.5" src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Kakao_Corp._symbol_-_2012.svg/800px-Kakao_Corp._symbol_-_2012.svg.png?20151013114543" alt="kakao" />
                </span>
                Kakao 계정으로 로그인하기
            </a>
        </div>
    </div>
</div>

<style>
    .login-btn-oauth {
        @apply flex gap-2 justify-center items-center my-3 w-[350px] px-[1rem] py-3 text-[15px] font-bold rounded-xl text-neutral-800 border border-neutral-800 shadow-sm
    }

    .login-btn-oauth:hover {
        @apply bg-gray-100/80
    }

    .login-btn-oauth:active {
        @apply transition-all duration-75 scale-[0.98] shadow-none
    }
</style>