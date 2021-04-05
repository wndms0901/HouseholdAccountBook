<template>
  <div>
    <div class="monthReport_top">
      <table style="width: 600px">
        <tr>
          <th rowspan="2"><h1>1.1 - 1.31</h1></th>
          <td>수입 합계 <span class="text-danger">0</span></td>
        </tr>
        <tr>
          <td>지출 합계 <span class="text-primary">0</span></td>
        </tr>
      </table>
    </div>
    <div class="monthReport_centent">
      <div class="monthReport_left">
        <div class="monthReport_left_top">
          <h2>요약</h2>
        </div>
      </div>
      <div class="monthReport_center">
        <div class="monthReport_center_top">
          <h2>통계</h2>
          <b-form-radio-group v-model="selected" class="mb-3 text-center">
            <b-form-radio value="income">수입</b-form-radio>&ensp;<b-form-radio
              value="expenditure"
              >지출</b-form-radio
            >
          </b-form-radio-group>
        </div>
        <div class="monthReport_center_content"></div>
      </div>
      <div class="monthReport_right"></div>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  props: {
    user: Object,
    period: Object,
  },
  data() {
    return {
      selected: "income",
    };
  },
  computed: {
    showIncome() {
      return this.selected == "income";
    },
    showExpenditure() {
      return this.selected == "expenditure";
    },
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {},
    },
  },
  created() {},
  beforeMount() {},
  mounted() {
    this.getWeekOfMonth();
  },

  methods: {
    getWeekOfMonth() {
      const weekOfMonthList = [];
      const period_from = _.cloneDeep(this.period.from);
      const period_to = _.cloneDeep(this.period.to);

      // 마지막 주
      const endWeekFrom = this.$moment(period_to).weekday(0);
      const endWeekTo = this.$moment(period_to);
      const endMonth = endWeekFrom.month() + 1;
      const endWeek = this.getWeek(endWeekFrom, endWeekTo);

      const endObj = {
        month: endMonth,
        week: endWeek,
        from: endWeekFrom.date(),
        to: endWeekTo.date(),
      };
      weekOfMonthList.push(endObj);

      // 첫번째 주 add
      const firstdayofweek = this.$moment(period_from).weekday(0);
      const startWeekFrom = this.$moment(period_from);
      const startWeekTo = this.$moment(period_from).weekday(6);
      const startMonth = firstdayofweek.month() + 1;
      const startWeek = this.getWeek(firstdayofweek, startWeekTo);

      // week add
      const startDate = _.cloneDeep(startWeekTo).add(1, "days");
      const endDate = _.cloneDeep(endWeekFrom).subtract(1, "days");
      const diff = endDate.diff(startDate, "days") + 1;
      console.log(diff / 7);
      for (let i = 0; i < diff / 7; i++) {
        const weekTo = _.cloneDeep(endDate).subtract(i * 7, "days");
        const weekFrom = _.cloneDeep(weekTo).subtract(6, "days");
        const month = weekFrom.month() + 1;
        const week = this.getWeek(weekFrom, weekTo);
        const obj = {
          month: month,
          week: week,
          from: weekFrom.date(),
          to: weekTo.date(),
        };
        weekOfMonthList.push(obj);
      }

      const startObj = {
        month: startMonth,
        week: startWeek,
        from: startWeekFrom.date(),
        to: startWeekTo.date(),
      };
      weekOfMonthList.push(startObj);
      console.log("weekOfMonthList", weekOfMonthList);
    },
    getWeek(from, to) {
      let value = 0;
      const fromMonth = this.$moment(from).month();
      const toMonth = this.$moment(to).month();
      if (fromMonth != toMonth) {
        value = 1;
      } else {
        value = 0;
      }
      return (
        this.$moment(from).week() -
        this.$moment(from).startOf("month").week() +
        value
      );
    },
  },
};
</script>
<style scoped>
.monthReport_top {
  border: 1px solid lightgray;
  border-radius: 5px;
  background-color: white;
}
.monthReport_top > table tr th {
  width: 50%;
}
.monthReport_top > table tr th h1 {
  margin: 10px 20px;
}
.monthReport_centent {
  margin-top: 30px;
  display: flex;
}
.monthReport_left {
  width: 33%;
  height: 550px;
  border: 1px solid lightgray;
  background-color: white;
  margin-right: 20px;
}
.monthReport_left_top {
  padding: 10px;
}
.monthReport_left_top > h2 {
  margin: 0;
  padding-bottom: 10px;
}
.monthReport_center {
  width: 33%;
  height: 550px;
  border: 1px solid lightgray;
  background-color: white;
  margin-right: 20px;
}
.monthReport_center_top {
  padding: 10px;
}
.monthReport_center_top > h2 {
  margin: 0;
  padding-bottom: 10px;
}
.monthReport_center_content {
  text-align: center;
  vertical-align: top;
}
.monthReport_right {
  width: 33%;
  height: 550px;
  border: 1px solid lightgray;
  background-color: white;
}
</style>
