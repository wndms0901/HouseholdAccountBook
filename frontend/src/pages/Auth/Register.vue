<template>
  <div class="registerForm">
    <p class="text-center h1 p-5">회원가입</p>
    <b-form-group id="user-email" label-for="user-email">
      <b-form-input
        ref="email"
        class="form-control form-control-lg"
        id="user-email"
        v-model="user.email"
        type="email"
        placeholder="이메일"
        required
        @blur="emailCheck"
      ></b-form-input>
      <p class="text-danger" v-show="valueCheck.emailBlank">
        이메일을 입력해 주세요.
      </p>
      <p class="text-danger" v-show="valueCheck.emailFormat">
        올바른 이메일 주소가 아닙니다.
      </p>
      <p class="text-danger" v-show="valueCheck.emailExistence">
        이미 사용 중인 이메일입니다.
      </p>
    </b-form-group>
    <b-form-group id="user-name" label-for="user-name">
      <b-form-input
        ref="name"
        class="form-control form-control-lg"
        id="user-name"
        v-model="user.name"
        type="text"
        placeholder="이름"
        required
        @blur="nameCheck"
      ></b-form-input>
      <p class="text-danger" v-show="valueCheck.nameBlank">
        이름을 입력해 주세요.
      </p>
      <p class="text-danger" v-show="valueCheck.nameFormat">
        한글과 영문 대 소문자를 사용하세요. (특수기호, 공백 사용 불가)
      </p>
    </b-form-group>
    <b-form-group id="user-password" label-for="user-password">
      <b-form-input
        ref="password"
        class="form-control form-control-lg"
        id="user-password"
        v-model="user.password"
        type="password"
        placeholder="비밀번호"
        required
        @blur="passwordCheck"
      ></b-form-input>
      <p class="text-danger" v-show="valueCheck.passwordBlank">
        비밀번호를 입력해 주세요.
      </p>
      <p class="text-danger" v-show="valueCheck.passwordFormat">
        8자리 ~ 20자리 이내로 영문,숫자,특수문자를 혼합하여 입력해 주세요.
      </p>
    </b-form-group>
    <b-form-group id="user-password-confirm" label-for="user-password-confirm">
      <b-form-input
        ref="passwordConfirm"
        class="form-control form-control-lg"
        id="user-password-confirm"
        v-model="user.passwordConfirm"
        type="password"
        placeholder="비밀번호 재확인"
        required
        @blur="passwordConfirmCheck"
      ></b-form-input>
      <p class="text-danger" v-show="valueCheck.passwordConfirm">
        비밀번호가 일치하지 않습니다.
      </p>
    </b-form-group>
    <b-button
      type="submit"
      class="btn-fill"
      variant="primary"
      size="lg"
      block
      @click="onRegister"
      >가입하기</b-button
    >
    <div id="loginLink">
      <p>
        계정이 있으신가요?
        <b-link href="#" @click="onclickLogin">로그인</b-link>
      </p>
    </div>
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
        passwordConfirm: "",
      },
      valueCheck: {
        emailBlank: false,
        emailFormat: false,
        emailExistence: false,
        nameBlank: false,
        nameFormat: false,
        passwordBlank: false,
        passwordFormat: false,
        passwordConfirm: false,
      },
    };
  },
  mounted() {},
  methods: {
    // 이메일 유효성 체크
    emailCheck() {
      if (this.isEmpty(this.user.email)) {
        this.valueCheck.emailBlank = true;
        this.valueCheck.emailFormat = false;
        this.valueCheck.emailExistence = false;
      } else {
        if (this.chkEmail(this.user.email)) {
          // 이메일 형식이 아닌 경우
          this.valueCheck.emailBlank = false;
          this.valueCheck.emailFormat = true;
          this.valueCheck.emailExistence = false;
        } else {
          // 이메일 중복확인
          this.$store
            .dispatch("userStore/selectRegisterCheck", this.user.email)
            .then((res) => {
              console.log("res", res);
              if (res.email) {
                this.valueCheck.emailBlank = false;
                this.valueCheck.emailFormat = false;
                this.valueCheck.emailExistence = true;
              } else {
                this.valueCheck.emailBlank = false;
                this.valueCheck.emailFormat = false;
                this.valueCheck.emailExistence = false;
              }
            })
            .catch((error) => {
              console.log(error);
            });
        }
      }
    },
    // 이름 유효성 체크
    nameCheck() {
      if (this.isEmpty(this.user.name)) {
        // 이름 빈값
        this.valueCheck.nameBlank = true;
        this.valueCheck.nameFormat = false;
      } else if (this.chkUsername(this.user.name)) {
        // 특수기호, 공백 사용 체크
        this.valueCheck.nameBlank = false;
        this.valueCheck.nameFormat = true;
      } else {
        this.valueCheck.nameBlank = false;
        this.valueCheck.nameFormat = false;
      }
    },
    // 비밀번호 유효성 체크
    passwordCheck() {
      if (this.isEmpty(this.user.password)) {
        // 비밀번호 빈값
        this.valueCheck.passwordBlank = true;
        this.valueCheck.passwordFormat = false;
      } else if (this.chkPwd(this.user.password)) {
        // 비밀번호 유효성 체크
        this.valueCheck.passwordBlank = false;
        this.valueCheck.passwordFormat = true;
      } else {
        this.valueCheck.passwordBlank = false;
        this.valueCheck.passwordFormat = false;
      }
    },
    // 비밀번호 재확인 체크
    passwordConfirmCheck() {
      if (this.user.password !== this.user.passwordConfirm) {
        this.valueCheck.passwordConfirm = true;
      } else {
        this.valueCheck.passwordConfirm = false;
      }
    },
    // 공백, null, undefined 체크
    isEmpty(target) {
      if (target === undefined || target === null || target === "") {
        return true;
      }
      return false;
    },
    // 이메일 유효성
    chkEmail(str) {
      let regExp =
        /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
      if (!regExp.test(str)) {
        return true;
      }
      return false;
    },
    // 이메일 중복 체크
    chkDuplicateEmail() {
      // 회원 등록 확인
      this.$store
        .dispatch("userStore/selectRegisterCheck", this.user.email)
        .then((res) => {
          alert(res.email);
          if (res.email) {
            return true;
          } else {
            return false;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 이름 유효성
    chkUsername(str) {
      const regex = /^[가-힣|a-z|A-Z|0-9|]+$/;
      return !regex.test(str);
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
    // 유효성 체크
    validationCheck(valueCheck) {
      this.emailCheck();
      this.nameCheck();
      this.passwordCheck();
      this.passwordConfirmCheck();
      return (
        valueCheck.emailBlank ||
        valueCheck.emailFormat ||
        valueCheck.emailExistence ||
        valueCheck.nameBlank ||
        valueCheck.nameFormat ||
        valueCheck.passwordBlank ||
        valueCheck.passwordFormat ||
        valueCheck.passwordConfirm
      );
    },
    openMsgBoxOk(title, msg, btnTitle) {
      this.$bvModal
        .msgBoxOk(msg, {
          title: title,
          okTitle: btnTitle,
          modalClass: "registerModal",
          titleClass: "registerModalTitle",
          headerClass: "p-2 border-bottom ", //border-bottom-0
          bodyClass: "p-3 text-center registerModalBody",
          footerClass: "p-2 border-top registerModalFooter", //border-top-0
          noCloseOnBackdrop: true,
          // hideBackdrop: true,
          //centered: true,
        })
        .then(() => {
          this.$router.push("/user/login");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 회원가입 버튼 click
    onRegister() {
      if (this.validationCheck(this.valueCheck)) {
        if (
          this.valueCheck.emailBlank ||
          this.valueCheck.emailFormat ||
          this.valueCheck.emailExistence
        ) {
          this.$refs.email.focus();
        } else if (this.valueCheck.nameBlank || this.valueCheck.nameFormat) {
          this.$refs.name.focus();
        } else if (
          this.valueCheck.passwordBlank ||
          this.valueCheck.passwordFormat
        ) {
          this.$refs.password.focus();
        } else if (this.valueCheck.passwordConfirm) {
          this.$refs.passwordConfirm.focus();
        }
      } else {
        this.$store
          .dispatch("userStore/register", this.user)
          .then((res) => {
            const title = "가입 완료";
            const msg = "회원가입이 완료되었습니다.";
            const btnTitle = "로그인";
            this.openMsgBoxOk(title, msg, btnTitle);
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    // 로그인 link click
    onclickLogin() {
      this.$router.push("/user/login");
    },
  },
};
</script>
<style>
.registerForm {
  margin-left: auto;
  margin-right: auto;
  padding: 20px;
  width: 30%;
  min-height: calc(100% - 60px);
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
.registerModal {
  transform: translate(0, 25%);
}
.registerModalTitle {
  font-size: 20px;
  font-weight: bold;
}
.registerModalBody {
  font-size: 24px;
}
.registerModalFooter {
  justify-content: center !important;
}
</style>