<template>
  <div class="error_wrapper">
    <div class="error_status"><span>4</span><span>0</span><span>4</span></div>
    <div class="error_msg">
      <p>PAGE NOT FOUND</p>
      <p>요청한 페이지를 찾을 수 없습니다.</p>
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
  margin-top: 50px;
}
</style>