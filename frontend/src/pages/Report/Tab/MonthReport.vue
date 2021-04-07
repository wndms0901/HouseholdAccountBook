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
        <div class="monthReport_left_content">
          <b-table
            class="monthReport_left_content_table"
            responsive
            :items="tableItems"
            :fields="tableFields"
            :tbody-tr-class="rowClass"
          >
          </b-table>
          <b-table
            class="monthReport_left_content_table"
            responsive
            borderless
            :items="table2Items"
            :fields="table2Fields"
          >
          </b-table>
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
      tableFields: [
        { key: "period", label: "" },
        { key: "total", label: "", class: "text-right" },
      ],
      tableItems: [],
      table2Fields: [
        { key: "title", label: "", class: "w-25" },
        { key: "value", label: "", class: "text-right" },
      ],
      table2Items: [],
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
      lineChartData: [["Gün", "지출 추이", "지난달 일평균"]],
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
          // minValue: new Date(2021, 3, 1),
          // maxValue: new Date(2021, 3, 31),
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
    this.tableItems = [
      { period: "20210401", total: 1000, bottom: false },
      { period: "20210401", total: 1000, bottom: false },
      { period: "20210401", total: 1000, bottom: false },
      { period: "20210401", total: 1000, bottom: false },
      { period: "20210401", total: 1000, bottom: true },
    ];
    this.table2Items = [{ title: "총 지출", value: 5000 }];
    this.getWeekOfMonth();
    const period_from = _.cloneDeep(this.period.from);
    const period_to = _.cloneDeep(this.period.to);
    const diff = this.$moment(period_to).diff(period_from, "days") + 1;
    for (let i = 0; i < diff; i++) {
      const dt = this.$moment(period_from).add(i, "days").format("YYYY-MM-DD");
      const year = Number(dt.substr(0, 4));
      const month = Number(dt.substr(5, 2));
      const day = Number(dt.substr(8, 2));
      console.log("year", year, month, day);
      const arry = [new Date(year, month - 1, day), 1000, 400];
      this.lineChartData.push(arry);
    }
  },

  methods: {
    rowClass(item, type) {
      if (!item || type !== "row") return;
      if (item.bottom) {
        return "border-bottom";
      }
    },
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
  padding-bottom: 10px;
}
.monthReport_left_content {
}
.monthReport_left_content_table {
  width: 400px;
  margin-left: auto;
  margin-right: auto;
  /* height: 10px;
  margin-left: auto;
  margin-right: auto;
  border-collapse: collapse;
  border: 1px solid black;
  overflow: auto; */
}
/* .monthReport_left_content > table {
  margin-left: auto;
  margin-right: auto;
  display: block;
  width: 400px;
  height: 200px;
  border-collapse: collapse;
  border: 1px solid blue;
}

.monthReport_left_content > table td {
  border: 1px solid #000;
  border-top: 0;
  width: 200px;
}
.monthReport_left_content > table tbody {
  display: block;
  height: 200px;
  overflow: auto;
} */

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
