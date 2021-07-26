<template>
  <div class="myInfoBox">
    <h2>내정보</h2>
    <table>
      <tr>
        <td><span>이메일</span></td>
        <td>
          <span>{{ this.user.userInfo.email }}</span>
        </td>
      </tr>
      <tr>
        <td><span>이름</span></td>
        <td>
          <span>{{ this.user.userInfo.name }}</span>
        </td>
      </tr>
      <tr>
        <td>
          <span class="text-danger">*</span>&nbsp;
          <span>기존 비밀번호</span>
        </td>
        <td><input type="password" ref="password" v-model="password" /></td>
      </tr>
      <tr>
        <td>
          <span class="text-danger">*</span>&nbsp;<span>새 비밀번호</span>
        </td>
        <td>
          <input type="password" ref="newPassword" v-model="newPassword" />
        </td>
      </tr>
      <tr>
        <td>
          <span class="text-danger">*</span>&nbsp;<span>새 비밀번호 확인</span>
        </td>
        <td>
          <input
            type="password"
            ref="newPasswordCheck"
            v-model="newPasswordCheck"
          />
        </td>
      </tr>
    </table>
    <div class="text-center">
      <button class="btn btn-primary btn-fill" @click="updateMyInfo">
        수정하기
      </button>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      user: this.$store.state.userStore.initialState.user,
      password: "",
      newPassword: "",
      newPasswordCheck: "",
    };
  },
  methods: {
    updateMyInfo() {
      if (this.validationCheck()) {
        const userDto = {
          email: this.user.userInfo.email,
          password: this.password,
          newPassword: this.newPassword,
        };
        this.$store
          .dispatch("userStore/updatePassword", userDto)
          .then((res) => {
            if (res) {
              alert("비밀번호 변경이 완료되었습니다.");
              this.$router.push("/");
            } else {
              alert("기존 비밀번호를 정확하게 입력해 주세요.");
              this.password = "";
              this.newPassword = "";
              this.newPasswordCheck = "";
              this.$refs.password.focus();
            }
          })
          .catch((Error) => {
            console.log(Error);
          });
      }
    },
    validationCheck() {
      if (!this.password) {
        alert("기존 비밀번호를 입력하세요.");
        this.$refs.password.focus();
        return false;
      }
      if (!this.newPassword) {
        alert("새 비밀번호를 입력하세요.");
        this.$refs.newPassword.focus();
        return false;
      }
      if (!this.newPasswordCheck) {
        alert("새 비밀번호 확인을 입력하세요.");
        this.$refs.newPasswordCheck.focus();
        return false;
      }
      if (this.newPassword !== this.newPasswordCheck) {
        alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
        this.newPasswordCheck = "";
        this.$refs.newPasswordCheck.focus();
        return false;
      }
      if (this.chkPwd(this.newPassword)) {
        alert(
          "비밀번호는 8자리 ~ 20자리 이내로 영문,숫자,특수문자를 혼합하여 입력해주세요."
        );
        this.newPassword = "";
        this.newPasswordCheck = "";
        this.$refs.newPassword.focus();
        return false;
      }
      return true;
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
  },
};
</script>
<style scoped>
.myInfoBox {
  margin-top: 100px;
  margin-bottom: 100px;
  margin-left: auto;
  margin-right: auto;
  padding: 50px;
  width: 50%;
  height: calc(100% - 302px);
  background-color: #fff;
  border: 1px solid #babfc7;
}
.myInfoBox h2 {
  margin: 0;
  text-align: center;
}
.myInfoBox span {
  font-size: 20px;
  color: #555;
}
.myInfoBox table {
  margin-top: 40px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 70px;
  width: 530px;
  border-collapse: collapse;
}
.myInfoBox table tr {
  /* border: 1px solid black; */
}
.myInfoBox table tr td {
  padding: 15px;
}

.myInfoBox input {
  background-color: #ffffff;
  border: 1px solid lightgray;
  border-radius: 4px;
  color: #555;
  padding: 8px 12px;
  width: 300px;
  height: 40px;
  font-size: 20px;
}
.myInfoBox div button {
  border-color: #3472f7;
}
</style>