<template>
  <div class="error_wrapper">
    <div class="error_status">
      <div v-if="status !== 'Error'">
        <span>{{ status[0] }}</span
        ><span>{{ status[1] }}</span
        ><span>{{ status[2] }}</span>
      </div>
      <div v-if="status === 'Error'">
        <span>{{ status }}</span>
      </div>
    </div>
    <div class="error_msg">
      <pre>{{ statusText }}</pre>
      <pre>{{ msg }}</pre>
      <b-button class="btn-fill" variant="primary" size="lg" @click="goMain"
        >메인페이지</b-button
      >&ensp;
      <b-button variant="primary" size="lg" @click="goBack"
        >이전페이지</b-button
      >
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      status: "404",
      statusText: "Page Not Found",
      msg: "요청한 페이지를 찾을 수 없습니다.",
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.userStore.initialState.status.loggedIn;
    },
    errorStatus() {
      return this.$route.params.status;
    },
  },
  methods: {
    goMain() {
      this.$router.push({ path: "/mybook/write" });
    },
    goBack() {
      if (!this.loggedIn) {
        this.$router.push({ path: "/user/login" });
      } else {
        this.$router.go(-1);
      }
    },
  },
  mounted() {
    this.status = this.errorStatus ? String(this.errorStatus) : "Error";
    this.statusText = this.$route.params.statusText;
    this.msg =
      "요청하신 페이지를 처리 중에 오류가 발생했습니다.\n입력하신 주소가 정확한지 확인 후 다시 시도해 주시기 바랍니다.";
  },
};
</script>
<style>
.error_wrapper {
  flex-direction: column;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: -50px;
  left: 0;
  width: 100%;
  height: 100%;
}
.error_wrapper p {
  font-size: 2em;
}
.error_wrapper pre {
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 2em;
}
.error_status {
  font-size: 15em;
}
.error_status span:nth-child(2) {
  color: #3472f7;
}
.error_msg {
  position: relative;
  text-align: center;
  top: -50px;
}
.error_msg button {
  width: 270px;
  margin-top: 30px;
  margin-left: auto;
  margin-right: auto;
}
</style>