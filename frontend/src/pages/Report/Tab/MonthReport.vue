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
        <div class="monthReport_center_content">
          <GChart
            type="PieChart"
            :data="chartData"
            :options="chartOptions"
            v-show="showIncome"
          />
          <grid
            ref="Grid"
            v-bind:style="styleObject"
            class="ag-theme-alpine"
            headerHeight="0"
            :gridOptions="gridOptions"
            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :rowData="rowData"
            :singleClickEdit="false"
            :suppressRowClickSelection="false"
            rowSelection="multiple"
            @grid-ready="onGridReady"
          ></grid>
        </div>
      </div>
      <div class="monthReport_right">
        <div class="monthReport_right_top">
          <h2>일별 추이</h2>
        </div>
        <div class="monthReport_right_content">
          <GChart
            type="LineChart"
            :data="lineChartData"
            :options="lineChartOptions"
          />
          <table>
            <tr>
              <td>총 지출</td>
              <td style="text-align: right">100000원</td>
            </tr>
            <tr>
              <td colspan="2" style="text-align: right">
                지난달 일평균 13,000원
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { GChart } from "vue-google-charts";
export default {
  components: { GChart },
  props: {
    user: Object,
    period: Object,
  },
  data() {
    return {
      selected: "income",
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      styleObject: null,
      rowData: [],
      chartData: [
        ["Category", "Total"],
        ["2014", 1000],
        ["2015", 1170],
        ["2016", 660],
        ["2017", 1030],
        ["2018", 103],
        ["2019", 103],
      ],
      chartOptions: {
        chart: {
          title: "Company Performance",
          subtitle: "Sales, Expenses, and Profit: 2014-2017",
        },
        legend: {
          position: "bottom",
          alignment: "center",
          orientation: "vertical",
        },
      },
      lineChartData: [],
      lineChartOptions: {
        height: 300,
        chart: {
          title: "Company Performance",
          subtitle: "Sales, Expenses, and Profit: 2014-2017",
        },
        legend: {
          position: "top",
          alignment: "center",
          orientation: "vertical",
        },
        hAxis: {
          format: "MM/dd",
        },
      },
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
  beforeMount() {
    this.styleObject = {
      width: "400px",
      height: "200px",
      marginTop: "10px",
      marginLeft: "auto",
      marginRight: "auto",
    };
    this.gridOptions = {
      //onCellValueChanged: onCellValueChanged,
      //enableColResize: true,
      // enableSorting: true,
      // enableFilter: true,
      // animateRows: false,
    };

    this.columnDefs = [
      { field: "incomeId", hide: true },
      { headerName: "내역", field: "incomeDescription" },
      { headerName: "메모", field: "memo" },
    ];
    this.rowData = [{ incomeDescription: "1", memo: "1" }];
  },
  mounted() {
    this.getWeekOfMonth();
    const now = new Date();
    const val = this.$moment(now);
    const val2 = this.$moment(now).add(1, "days");
    const val3 = this.$moment(now).add(2, "days").format("MM.DD");
    const val4 = this.$moment(now).add(3, "days").format("MM.DD");
    const val5 = this.$moment(now).add(4, "days").format("MM.DD");
    const val6 = this.$moment(now).add(5, "days").format("MM.DD");
    const val7 = this.$moment(now).add(6, "days").format("MM.DD");
    const val8 = this.$moment(now).add(7, "days").format("MM.DD");
    const val9 = this.$moment(now).add(8, "days").format("MM.DD");
    const val10 = this.$moment(now).add(9, "days").format("MM.DD");
    console.log(typeof val);
    this.lineChartData = [
      ["Gün", "지출 추이", "지난달 일평균"],
      [new Date(2021, 3, 1), 1000, 400],
      [new Date(2021, 3, 2), 1170, 400],
      [new Date(2021, 3, 3), 1000, 400],
      [new Date(2021, 3, 4), 1170, 400],
      [new Date(2021, 3, 5), 1000, 400],
      [new Date(2021, 3, 6), 1170, 400],
      [new Date(2021, 3, 7), 1000, 400],
      [new Date(2021, 3, 8), 1170, 400],
      [new Date(2021, 3, 9), 1000, 400],
      [new Date(2021, 3, 10), 1170, 400],
      [new Date(2021, 3, 11), 1000, 400],
      [new Date(2021, 3, 12), 1170, 400],
      [new Date(2021, 3, 13), 1000, 400],
      [new Date(2021, 3, 14), 1170, 400],
      [new Date(2021, 3, 15), 1000, 400],
      [new Date(2021, 3, 16), 1170, 400],
      [new Date(2021, 3, 17), 1000, 400],
      [new Date(2021, 3, 18), 1170, 400],
      [new Date(2021, 3, 19), 1000, 400],
      [new Date(2021, 3, 20), 1170, 400],
      [new Date(2021, 3, 21), 1000, 400],
      [new Date(2021, 3, 22), 1170, 400],
      [new Date(2021, 3, 23), 1000, 400],
      [new Date(2021, 3, 24), 1170, 400],
      [new Date(2021, 3, 25), 1000, 400],
      [new Date(2021, 3, 26), 1170, 400],
      [new Date(2021, 3, 27), 1000, 400],
      [new Date(2021, 3, 28), 1170, 400],
      [new Date(2021, 3, 29), 1000, 400],
      [new Date(2021, 3, 30), 1170, 400],
    ];
  },

  methods: {
    onGridReady(params) {
      console.log("params", params);
      this.gridApi = params.api;
      this.columnApi = params.columnApi;

      params.api.sizeColumnsToFit();
    },
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
  padding: 20px;
}
.monthReport_left_top > h2 {
  margin: 0;
}
.monthReport_center {
  width: 33%;
  height: 550px;
  border: 1px solid lightgray;
  background-color: white;
  margin-right: 20px;
}
.monthReport_center_top {
  padding: 20px 20px 0px 20px;
}
.monthReport_center_top > h2 {
  margin: 0;
  padding-bottom: 10px;
}
.monthReport_center_content {
  margin-left: auto;
  margin-right: auto;
  text-align: center;
  /* vertical-align: top; */
}

.monthReport_right {
  width: 33%;
  height: 550px;
  border: 1px solid lightgray;
  background-color: white;
}
.monthReport_right_top {
  padding: 20px;
}
.monthReport_right_top > h2 {
  margin: 0;
  padding-bottom: 10px;
}
.monthReport_right_content {
  text-align: center;
  /* vertical-align: top; */
}
.monthReport_right_content > table {
  margin-top: 20px;
  width: 400px;
}
</style>
