<template>
  <div class="error_wrapper">
    <div class="error_status">
      <span>{{ status[0] }}</span
      ><span>{{ status[1] }}</span
      ><span>{{ status[2] }}</span>
    </div>
    <div class="error_msg">
      <!-- <p>{{ statusText }}</p> -->
      <pre>{{ statusText }}</pre>
      <pre>{{ msg }}</pre>
      <b-button
        class="btn-fill"
        variant="primary"
        size="lg"
        block
        @click="goBack"
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
  },
  methods: {
    goBack() {
      if (!this.loggedIn) {
        this.$router.push({ path: "/user/login" });
      } else {
        this.$router.go(-1);
      }
    },
  },
  mounted() {
    this.status = this.$route.query.status;
    this.statusText = this.$route.query.statusText;
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
  width: 530px;
  margin-top: 50px;
  margin-left: auto;
  margin-right: auto;
}
</style>