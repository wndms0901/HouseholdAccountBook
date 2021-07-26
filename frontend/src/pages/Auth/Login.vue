<template>
  <div class="loginForm">
    <p class="text-center h1 p-5">가계부</p>
    <b-form-group id="input-group-1" label-for="input-1">
      <b-form-input
        class="form-control form-control-lg"
        id="input-1"
        v-model="user.email"
        type="email"
        ref="email"
        placeholder="이메일"
        required
      ></b-form-input>
      <p class="text-danger" v-show="valueCheck.email">
        이메일을 입력해주세요.
      </p>
    </b-form-group>
    <b-form-group id="input-group-2" label-for="input-2">
      <b-form-input
        class="form-control form-control-lg"
        id="input-2"
        v-model="user.password"
        type="password"
        ref="password"
        placeholder="비밀번호"
        @keyup.enter="onLogin"
        required
      ></b-form-input>
      <p class="text-danger" v-show="showLoginFailMsg">
        가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.
      </p>
      <p class="text-danger" v-show="valueCheck.password">
        비밀번호를 입력해주세요.
      </p>
    </b-form-group>
    <b-button
      type="submit"
      class="btn-fill"
      variant="primary"
      size="lg"
      block
      @click="onLogin"
      ><span>로그인</span></b-button
    >
    <div class="py-3">
      <b-link class="pr-3" href="#" @click="onclickRegister">회원가입</b-link>
      <b-link class="pr-3" href="#" @click="openPasswordFindModal"
        >비밀번호 찾기</b-link
      >
      <b-link href="#" id="test-login" @click="onTestLogin">테스트 계정</b-link>
      <b-tooltip target="test-login" triggers="hover" variant="dark">
        테스트 계정으로 로그인할 수 있습니다.
      </b-tooltip>
    </div>
    <!-- 비밀번호 찾기 Modal -->
    <passwordFind v-if="showPasswordFindModal">
      <!-- top 슬롯 콘텐츠 -->
      <template slot="top">
        <span>비밀번호 재설정</span>
      </template>
      <!-- /top -->
      <!-- cotent 슬롯 콘텐츠 -->
      <div class="text-center" v-show="modalContent1">
        <input
          type="email"
          ref="emailModal"
          v-model="email"
          placeholder="이메일을 입력하세요."
          @keyup.enter="onclickSearch"
        />
        <button class="primaryBtn ml-1" @click="onclickSearch">검색</button>
      </div>
      <div class="text-center" v-show="modalContent2">
        <input type="radio" checked="checked" />&ensp;<span
          >{{ email }}(으)로 이메일 보내기</span
        >
        <button class="primaryBtn ml-3" @click="sendPasswordResetEmail">
          다음
        </button>
      </div>
      <div class="text-center" v-show="modalContent3">
        <span
          >{{ email }}(으)로 임시 비밀번호가 발송되었습니다.<br />임시
          비밀번호로 로그인 후 내정보에서 비밀번호를 변경해 주세요.</span
        >
      </div>
      <!-- /cotent -->
      <!-- footer 슬롯 콘텐츠 -->
      <template slot="footer">
        <button class="basicBtn" @click="closePasswordFindModal">닫기</button>
      </template>
      <!-- /footer -->
    </passwordFind>
  </div>
</template>

<script>
import passwordFind from "src/components/Modal/PasswordFind";
export default {
  name: "Login",
  components: { passwordFind },
  data() {
    return {
      user: {
        email: "",
        password: "",
      },
      email: "",
      valueCheck: {
        email: false,
        password: false,
      },
      showLoginFailMsg: false,
      showPasswordFindModal: false,
      modalContent1: true,
      modalContent2: false,
      modalContent3: false,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.userStore.initialState.status.loggedIn;
    },
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/");
    }
  },
  methods: {
    validationCheck() {
      this.showLoginFailMsg = false;
      this.valueCheck.email = false;
      this.valueCheck.password = false;
      if (!this.user.email) {
        this.valueCheck.email = true;
        this.$refs.email.focus();
      } else if (!this.user.password) {
        this.valueCheck.password = true;
        this.$refs.password.focus();
      }
    },
    onLogin() {
      this.validationCheck();
      if (this.user.email && this.user.password) {
        this.$store.dispatch("userStore/login", this.user).then(
          (data) => {
            if (data.error) {
              // 로그인 실패
              this.user.password = "";
              this.$refs.password.focus();
              this.showLoginFailMsg = true;
            } else {
              // 로그인 성공
              this.$router.push("/");
            }
          },
          (error) => {}
        );
      }
    },
    // 회원가입 click
    onclickRegister() {
      this.$router.push("/user/register");
    },
    // 비밀번호 찾기 모달 open
    openPasswordFindModal() {
      this.showPasswordFindModal = true;
      this.modalContent1 = true;
      this.modalContent2 = false;
      this.modalContent3 = false;
      this.email = "";
    },
    // 비밀번호 찾기 모달 close
    closePasswordFindModal() {
      this.showPasswordFindModal = false;
      this.modalContent1 = true;
      this.modalContent2 = false;
      this.modalContent3 = false;
      this.email = "";
    },
    // 비밀번호 찾기 모달 찾기 버튼 click
    onclickSearch() {
      if (this.email) {
        // 회원 등록 확인
        this.$store
          .dispatch("userStore/selectRegisterCheck", this.email)
          .then((res) => {
            if (res.email) {
              this.modalContent1 = false;
              this.modalContent2 = true;
              this.modalContent3 = false;
            } else {
              alert("입력하신 이메일을 찾을 수 없습니다.");
              this.$refs.emailModal.focus();
            }
          })
          .catch((Error) => {
            console.log(Error);
          });
      } else {
        alert("이메일을 입력해 주세요.");
      }
    },
    // 임시 비밀번호 이메일 전송
    sendPasswordResetEmail() {
      if (this.email === "test@test.com") {
        alert("테스트 계정은 비밀번호 재설정이 불가능합니다.");
      } else {
        this.$store
          .dispatch("userStore/sendPasswordResetEmail", this.email)
          .then(() => {
            this.modalContent1 = false;
            this.modalContent2 = false;
            this.modalContent3 = true;
          })
          .catch((Error) => {
            console.log(Error);
          });
      }
    },
    // 테스트 계정으로 로그인
    onTestLogin() {
      this.$store
        .dispatch("userStore/testIdLogin")
        .then(() => {
          // 쓰기 화면으로 이동
          this.$router.push("/");
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
  },
};
</script>
<style scoped>
.loginForm {
  margin-left: auto;
  margin-right: auto;
  padding: 20px;
  width: 30%;
  min-height: calc(100% - 60px);
  /* height: inherit; */
}
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  text-align: center;
}
li {
  display: inline;
}
li a {
  padding: 20px;
}
</style>