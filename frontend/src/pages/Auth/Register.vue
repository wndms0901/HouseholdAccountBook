<template>
  <div>
    <!-- <b-form @submit="onRegister"> -->
    <b-row class="m-0" cols="1" cols-sm="1" cols-md="2" cols-lg="2" cols-xl="3">
      <div id="registerForm">
        <p class="text-center h1 p-5">회원가입</p>
        <b-form-group id="user-email" label-for="user-email">
          <b-form-input
            class="form-control form-control-lg"
            id="user-email"
            v-model="user.email"
            type="email"
            placeholder="이메일"
            required
          ></b-form-input>
          <p class="text-danger" v-show="valueCheck.email">
            올바른 이메일 주소가 아닙니다.
          </p>
        </b-form-group>
        <b-form-group id="user-name" label-for="user-name">
          <b-form-input
            class="form-control form-control-lg"
            id="user-name"
            v-model="user.name"
            type="text"
            placeholder="이름"
            required
          ></b-form-input>
          <p class="text-danger" v-show="valueCheck.name">
            한글과 영문 대 소문자를 사용하세요. (특수기호, 공백 사용 불가)
          </p>
        </b-form-group>
        <b-form-group id="user-password" label-for="user-password">
          <b-form-input
            class="form-control form-control-lg"
            id="user-password"
            v-model="user.password"
            type="password"
            placeholder="비밀번호"
            required
          ></b-form-input>
          <p class="text-danger" v-show="valueCheck.password">
            8자리 ~ 20자리 이내로 영문,숫자,특수문자를 혼합하여 입력해주세요.
          </p>
        </b-form-group>
        <b-form-group id="user-password-check" label-for="user-password-check">
          <b-form-input
            class="form-control form-control-lg"
            id="user-password-check"
            v-model="user.passwordCheck"
            type="password"
            placeholder="비밀번호 재확인"
            required
          ></b-form-input>
          <p class="text-danger" v-show="valueCheck.passwordCheck">
            비밀번호가 일치하지 않습니다.
          </p>
        </b-form-group>
        <b-button
          type="submit"
          variant="primary"
          size="lg"
          block
          @click="onRegister"
          >가입하기</b-button
        >
        <div id="loginLink">
          <p>계정이 있으신가요? <a href="/user/login">로그인</a></p>
        </div>
      </div>
    </b-row>
    <!-- </b-form> -->
    <!-- <b-modal id="modal-center" centered hide-backdrop v-model="show" ok-only>
      <p class="my-4">회원가입이 완료 되었습니다.</p>
    </b-modal> -->
  </div>
</template>
<script>
export default {
  name: "Register",
  components: {},
  data() {
    return {
      user: {
        email: "",
        name: "",
        password: "",
        passwordCheck: "",
      },
      valueCheck: {
        email: false,
        name: false,
        password: false,
        passwordCheck: false,
      },
      show: true,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.userStore.initialState.status.loggedIn;
    },
  },

  watch: {
    user: {
      deep: true,
      handler() {
        this.validationCheck(this.user);
      },
    },
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/");
    }
  },
  methods: {
    // 공백, null, undefined 체크
    isEmpty(target) {
      if (target === undefined || target === null || target === "") {
        return true;
      }

      return false;
    },

    // 이메일 유효성
    chkEmail(str) {
      let regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
      if (!regExp.test(str)) {
        return true;
      }

      return false;
    },
    // 이름 유효성
    chkUsername(str) {
      //   let num = str.search(/[0-9]/g);
      //   let eng = str.search(/[a-z]/gi);
      let spe = str.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

      if (this.isEmpty(str)) {
        return true;
      }
      // 공백 여부 체크
      if (str.search(/\s/) != -1) {
        return true;
      }
      // 특수문자 체크
      if (spe > -1) {
        return true;
      }

      return false;
    },

    // 비밀번호 유효성
    chkPwd(str) {
      var num = str.search(/[0-9]/g);
      var eng = str.search(/[a-z]/gi);
      var spe = str.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

      if (str.length < 8 || str.length > 20) {
        return true;
      }
      // 공백 여부 체크
      if (str.search(/\s/) != -1) {
        return true;
      }

      if (num < 0 || eng < 0 || spe < 0) {
        return true;
      }

      return false;
    },
    validationCheck(user) {
      this.valueCheck.email = this.chkEmail(user.email);
      this.valueCheck.name = this.chkUsername(user.name);
      this.valueCheck.password = this.chkPwd(user.password);
      this.valueCheck.passwordCheck = user.password !== user.passwordCheck;
    },
    openMsgBoxOk(title, msg, btnTitle) {
      this.$bvModal
        .msgBoxOk(msg, {
          title: title,
          // size: "md",
          // buttonSize: "md",
          // okVariant: "success",
          okTitle: btnTitle,
          headerClass: "p-3 border-bottom", //border-bottom-0
          bodyClass: "text-center h3",
          footerClass: "p-2 position-relative text-center border-top", //border-top-0
          // noCloseOnBackdrop: true,
          // hideBackdrop: true,
          // centered: true,
        })
        // eslint-disable-next-line no-unused-vars
        .then((value) => {
          this.$router.push("/user/login");
        })
        // eslint-disable-next-line no-unused-vars
        .catch((err) => {
          // An error occurred
        });
    },
    // 회원가입 버튼 click
    onRegister() {
      // this.$bvModal
      //   .msgBoxOk("Action completed", { centered: true })
      //   .then((value) => {})
      //   .catch((err) => {
      //     // An error occurred
      //   });
      this.validationCheck(this.user);
      if (
        !(
          this.valueCheck.email ||
          this.valueCheck.name ||
          this.valueCheck.password ||
          this.valueCheck.passwordCheck
        )
      ) {
        console.log("this.user", this.user);
        this.$store
          .dispatch("userStore/register", this.user)
          .then((Response) => {
            console.log(Response.data);
            const title = "가입 완료";
            const msg = "회원가입이 완료되었습니다.";
            const btnTitle = "로그인";
            this.openMsgBoxOk(title, msg, btnTitle);
          })
          .catch((Error) => {
            console.log(Error);
          });
      }
    },
  },
};
</script>
<style scoped>
#registerForm {
  margin-left: auto;
  margin-right: auto;
  padding: 20px;
}
#loginLink {
  margin-top: 50px;
  padding: 10px;
  border: solid 1px lightgrey;
  border-radius: 5px;
  text-align: center;
  /* vertical-align: middle; */
}
#loginLink p {
  margin: 0px;
}
</style>