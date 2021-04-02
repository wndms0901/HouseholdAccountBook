<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>예산쓰기</p>
        </div>
        <div class="date_wrap">
          <div class="date_picker_box" style="display: flex">
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.from"
              input-class="datepickerInput budgetDateInput"
              :disabled="true"
              :language="ko"
            ></datepicker>
            <span class="calendarIconBox"
              ><b-icon class="calendarIcon" icon="calendar3"></b-icon></span
            >&ensp; <span style="font-size: 1.5em">-</span>&ensp;
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.to"
              input-class="datepickerInput budgetDateInput"
              :disabled="true"
              :language="ko"
            ></datepicker>
            <span class="calendarIconBox"
              ><b-icon class="calendarIcon" icon="calendar3"></b-icon
            ></span>
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
<style>
.budgetDateInput {
  cursor: default;
}
</style>