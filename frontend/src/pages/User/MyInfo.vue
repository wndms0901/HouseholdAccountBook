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
        <td><input type="password" /></td>
      </tr>
      <tr>
        <td>
          <span class="text-danger">*</span>&nbsp;<span>새 비밀번호</span>
        </td>
        <td><input type="password" /></td>
      </tr>
      <tr>
        <td>
          <span class="text-danger">*</span>&nbsp;<span>새 비밀번호 확인</span>
        </td>
        <td><input type="password" /></td>
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
    };
  },
  methods: {
    updateMyInfo() {},
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
