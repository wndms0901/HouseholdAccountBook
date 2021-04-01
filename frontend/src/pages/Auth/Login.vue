<template>
  <div>
    <b-row class="m-0" cols="1" cols-sm="1" cols-md="2" cols-lg="2" cols-xl="3">
      <div id="loginForm">
        <p class="text-center h1 p-5">Sign In</p>
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
          variant="primary"
          size="lg"
          block
          @click="onLogin"
          >로그인</b-button
        >
        <div class="py-3">
          <b-link class="pr-3" href="/user/register">회원가입</b-link>
          <b-link href="#foo">비밀번호 찾기</b-link>
        </div>
        <hr />
        <div>
          <p class="text-center h2 p-2">간편 로그인</p>
          <ul>
            <li>
              <a href="#google">
                <img id="google_logo" src="../../assets/image/google.png" />
              </a>
            </li>
            <li>
              <a href="#naver">
                <img id="naver_logo" src="../../assets/image/naver.png" />
              </a>
            </li>
          </ul>
        </div>
      </div>
    </b-row>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      user: {
        email: "",
        password: "",
      },
      valueCheck: {
        email: false,
        password: false,
      },
      showLoginFailMsg: false,
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
        console.log(this.$store);
        this.$store.dispatch("userStore/login", this.user).then(
          (data) => {
            console.log(data);
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
          (error) => {
            // this.message =
            //   (error.response && error.response.data) ||
            //   error.message ||
            //   error.toString();
            console.log(error);
          }
        );
      }

      //event.preventDefault();
      //alert(JSON.stringify(this.form));
    },
  },
};
</script>
<style scoped>
#loginForm {
  margin-left: auto;
  margin-right: auto;
  padding: 20px;
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
  /*  */
  padding: 20px;
}
#naver_logo {
  width: 50px;
  height: 50px;
}
#google_logo {
  width: 60px;
  height: 60px;
}
/* .container {
  position: absolute;
  width: 400px;
  height: 400px;
  background-color: gray;
  text-align: center;
  margin-left: -200px;
  margin-top: -200px;
  left: 50%;
  top: 50%;
} */
</style>
