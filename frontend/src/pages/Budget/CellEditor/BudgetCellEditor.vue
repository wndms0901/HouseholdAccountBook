<template>
  <input ref="input" class="inputCell" v-model="value" />
</template>
<script>
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      value: "",
    };
  },
  watch: {
    value: {
      handler(newData, oldValue) {
        // 숫자만 입력
        this.value = newData.replace(/[^0-9]/g, "");

        // 한자릿수 0만 입력/ 빈값일 때 0으로 치환
        if (parseInt(newData) === 0 || newData === "") {
          this.value = "0";
        }

        if (newData.length > 1 && parseInt(newData) < 10) {
          // 문자열의 맨 앞 0 제거
          this.value = newData.replace("0", "");
        }
        // 값 체크
        if (parseInt(newData) >= 10000000000) {
          this.value = oldValue;
        }
      },
    },
  },
  created() {},
  beforeMount() {},
  mounted() {
    this.value = this.params.value;
    Vue.nextTick(() => {
      if (this.$refs.input) {
        this.$refs.input.select();
      }
    });
  },
  methods: {
    /* Component Editor Lifecycle methods */
    // the final value to send to the grid, on completion of editing
    getValue() {
      return this.value;
    },
    // Gets called once before editing starts, to give editor a chance to
    // cancel the editing before it even starts.
    isCancelBeforeStart() {
      return false;
    },
    // Gets called once when editing is finished (eg if enter is pressed).
    // If you return true, then the result of the edit will be ignored.
    isCancelAfterEnd() {
      return false;
    },
  },
});
</script>
<style>
.inputCell {
  width: inherit;
  border-style: none;
}
</style>