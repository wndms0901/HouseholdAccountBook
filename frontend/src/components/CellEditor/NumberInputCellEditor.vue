<template>
  <input ref="input" class="inputCell" v-model="value" maxlength="13" />
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
      handler(newData, oldData) {
        // 입력값이 숫자가 아니면 공백
        this.value = String(this.value).replace(/[^0-9]/g, "");
        // ,값 공백처리
        this.value = String(this.value).replace(/,/g, "");
        // 정규식을 이용해서 3자리 마다 , 추가
        this.value = String(this.value).replace(
          /(\d)(?=(?:\d{3})+(?!\d))/g,
          "$1,"
        );
        let newValue = String(newData).replace(/,/g, "");
        // 한자릿수 0만 입력/ 빈값일 때 0으로 치환
        if (parseInt(newData) === 0 || newData === "") {
          this.value = "0";
        }
        // 문자열의 맨 앞 0 제거
        if (newValue.length > 1 && parseInt(newValue) < 10) {
          newValue = String(newValue).replace(/[^0-9]/g, "");
          this.value = newValue.replace("0", "");
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
    onKeyup() {
      // 입력값이 숫자가 아니면 공백
      this.value = this.value.replace(/[^0-9]/g, "");
      // ,값 공백처리
      this.value = this.value.replace(/,/g, "");
      // 정규식을 이용해서 3자리 마다 , 추가
      this.value = this.value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    setComma(value) {
      return String(value).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
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