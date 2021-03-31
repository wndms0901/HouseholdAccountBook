<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>예산쓰기</p>
        </div>
        <div
          style="width: 100%; position: relative; display: flex; height: 88px"
        >
          <div id="date_picker_box" style="display: flex">
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.from"
              :input-class="$style.datepickerInput"
              :disabled="true"
              :language="ko"
            ></datepicker>
            <span style="font-size: 2em">-</span>
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.to"
              :input-class="$style.datepickerInput"
              :disabled="true"
              :language="ko"
            ></datepicker>
          </div>
        </div>
        <div class="tabs" style="width: 100%">
          <b-tabs content-class="mt-3">
            <b-tab title="예산쓰기" active
              ><budgetWrite :user="user" :period="period"></budgetWrite
            ></b-tab>
          </b-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import datepicker from "vuejs-datepicker";
import budgetWrite from "./Tab/BudgetWrite.vue";
import { ko } from "vuejs-datepicker/dist/locale";
export default {
  components: { datepicker, budgetWrite },
  data() {
    return {
      user: this.$store.state.userStore.initialState.user,
      ko: ko,
      period: {
        from: "",
        to: "",
      },
    };
  },
  computed: {},
  beforeCreate() {},
  created() {
    this.setPeriod();
  },
  mounted() {},
  methods: {
    setPeriod() {
      let today = new Date();
      let startDate = new Date(today.getFullYear(), today.getMonth(), 1);
      let endDate = new Date(today.getFullYear(), today.getMonth() + 1, 0);
      this.period.from = startDate;
      this.period.to = endDate;
    },
  },
};
</script>
<style module>
.datepickerInput {
  width: 180px;
  text-align: center;
  font-size: 2em;
  background-color: white;
}
</style>