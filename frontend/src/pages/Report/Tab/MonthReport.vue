<template>
  <div>
    <div class="report_top">
      <table>
        <tr>
          <th rowspan="2">{{ startDate }} - {{ endDate }}</th>
          <td>
            수입 합계 <span class="ml-2 income_color">{{ totalIncome }}</span>
          </td>
        </tr>
        <tr>
          <td>
            지출 합계
            <span class="ml-2 expenditure_color">{{ totalExpenditure }}</span>
          </td>
        </tr>
      </table>
    </div>
    <div class="monthReport_centent">
      <div class="monthReport_left">
        <div class="monthReport_left_top">
          <h2>요약</h2>
        </div>
        <div>
          <b-table
            class="monthReport_left_content_table"
            responsive
            :items="summaryTable.items"
            :fields="summaryTable.fields"
            :tbody-tr-class="tableRowClass"
          >
          </b-table>
          <b-table
            class="monthReport_left_content_table"
            responsive
            borderless
            :items="totalTable.items"
            :fields="totalTable.fields"
          >
          </b-table>
        </div>
      </div>
      <div class="monthReport_center">
        <div class="monthReport_center_top">
          <h2>통계</h2>
          <b-form-radio-group v-model="selected" class="mb-3 text-center">
            <b-form-radio class="font-weight-bold income_color" value="income"
              >수입</b-form-radio
            >&ensp;<b-form-radio
              class="font-weight-bold expenditure_color"
              value="expenditure"
              >지출</b-form-radio
            >
          </b-form-radio-group>
        </div>
        <div class="monthReport_center_content">
          <GChart
            type="PieChart"
            :data="pieChartData"
            :options="pieChartOptions"
          />
          <b-table
            class="monthReport_center_content_table"
            sticky-header
            responsive
            :items="chartTable.items"
            :fields="chartTable.fields"
          ></b-table>
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
              <td style="text-align: right">{{ totalExpenditure }}원</td>
            </tr>
            <tr>
              <td colspan="2" style="text-align: right">
                지난달 일평균 {{ dailyAverageLastMonth }}원
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
      totalIncomeNumber: 0,
      totalExpenditureNumber: 0,
      dailyAverageLastMonthNumber: 0,
      selected: "expenditure",
      summaryTable: {
        fields: [
          { key: "weekList", label: "", class: "w-50" },
          { key: "weeklyExpenditure", label: "", class: "text-right" },
        ],
        items: [],
      },
      totalTable: {
        fields: [
          { key: "title", label: "", class: "w-25 font-weight-bold" },
          { key: "total", label: "", class: "text-right font-weight-bold" },
        ],
        items: [],
      },
      chartTable: {
        fields: [
          { key: "largeCategoryName", label: "", class: "text-left w-50" },
          { key: "totalCategory", label: "", class: "text-right" },
        ],
        items: [],
      },
      monthReportData: {},
      pieChartHeader: ["Category", "Total"],
      pieChartRows: [],
      updatePieChartData: [],
      pieChartOptions: {
        chartArea: { top: 0, height: "70%" },
        // chart: {
        //   title: "Company Performance",
        //   subtitle: "Sales, Expenses, and Profit: 2014-2017",
        // },
        legend: {
          position: "bottom",
          alignment: "center",
          orientation: "vertical",
        },
      },
      lineChartHeader: ["지출일자", "지출 추이", "지난달 일평균"],
      updateLineChartData: [],
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
        vAxis: {
          format: "#,###",
          // viewWindow: {
          //   min: -0,
          // },
        },
      },
    };
  },
  computed: {
    startDate() {
      const periodFrom = this.period.from;
      let month = periodFrom.getMonth() + 1;
      month = month / 10 >= 1 ? month : "0" + month;
      let day = periodFrom.getDate();
      day = day / 10 >= 1 ? day : "0" + day;
      return month + "." + day;
    },
    endDate() {
      const periodTo = this.period.to;
      let month = periodTo.getMonth() + 1;
      month = month / 10 >= 1 ? month : "0" + month;
      let day = periodTo.getDate();
      day = day / 10 >= 1 ? day : "0" + day;
      return month + "." + day;
    },
    totalIncome() {
      return String(this.totalIncomeNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    totalExpenditure() {
      return String(this.totalExpenditureNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    dailyAverageLastMonth() {
      return String(this.dailyAverageLastMonthNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    pieChartData() {
      return [this.pieChartHeader, ...this.updatePieChartData];
    },
    lineChartData() {
      return [this.lineChartHeader, ...this.updateLineChartData];
    },
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        const periodFrom = this.$moment(newData.from);
        const periodTo = this.$moment(newData.to);
        const diff = periodTo.diff(periodFrom, "months");
        // 월보고서 기간이 변경되었을때만 재조회
        if (diff === 0) {
          this.getWeekOfMonth();
        }
      },
    },
    selected: {
      handler(newData) {
        // 카테고리 통계 차트 data
        this.setPieChartData(this.pieChartRows);
        // 카테고리 테이블 data
        this.setPieChartTableData(this.monthReportData);
      },
    },
  },
  created() {},
  beforeMount() {},
  mounted() {
    this.getWeekOfMonth();
  },
  methods: {
    tableRowClass(item, type) {
      if (!item || type !== "row") return;
      if (item.bottom) {
        return "border-bottom";
      }
    },
    // 주 목록 조회
    getWeekOfMonth() {
      const weekOfMonthList = [];
      const period_from = _.cloneDeep(this.period.from);
      const period_to = _.cloneDeep(this.period.to);

      // 첫번째 주
      const startWeekMon = this.$moment(period_from).isoWeekday(1);
      const startWeekFrom = this.$moment(period_from);
      const startWeekTo = this.$moment(period_from).isoWeekday(7);
      // 첫번째 주의 목요일
      const startWeekThu = this.$moment(period_from).isoWeekday(4)._d;
      const startMonth = startWeekThu.getMonth() + 1;
      const startWeek = this.getWeek(
        startWeekMon._d,
        startWeekTo._d,
        startMonth,
        startWeekTo._d,
        startMonth
      );

      // 마지막 주
      const endWeekFrom = this.$moment(period_to).isoWeekday(1);
      const endWeekTo = this.$moment(period_to);
      const endWeekSun = this.$moment(period_to).isoWeekday(7);
      // 마지막 주의 목요일
      const endWeekThu = this.$moment(period_to).isoWeekday(4)._d;
      const endMonth = endWeekThu.getMonth() + 1;
      const endWeek = this.getWeek(
        endWeekFrom._d,
        endWeekSun._d,
        endMonth,
        startWeekTo._d,
        startMonth
      );
      const endObj = {
        month: endMonth,
        week: endWeek,
        startDate: endWeekFrom.format("YYYYMMDD"),
        endDate: endWeekTo.format("YYYYMMDD"),
        email: this.user.userInfo.email,
      };
      weekOfMonthList.push(endObj);

      // week add
      const startDate = _.cloneDeep(startWeekTo).add(1, "days");
      const endDate = _.cloneDeep(endWeekFrom).subtract(1, "days");
      const diff = endDate.diff(startDate, "days") + 1;
      console.log(diff / 7);
      for (let i = 0; i < diff / 7; i++) {
        const weekTo = _.cloneDeep(endDate).subtract(i * 7, "days");
        const weekFrom = _.cloneDeep(weekTo).subtract(6, "days");
        const month = weekFrom.month() + 1;
        const week = this.getWeek(
          weekFrom._d,
          weekTo._d,
          month,
          startWeekTo._d,
          startMonth
        );
        const obj = {
          month: month,
          week: week,
          startDate: weekFrom.format("YYYYMMDD"),
          endDate: weekTo.format("YYYYMMDD"),
          email: this.user.userInfo.email,
        };
        weekOfMonthList.push(obj);
      }
      const startObj = {
        month: startMonth,
        week: startWeek,
        startDate: startWeekFrom.format("YYYYMMDD"),
        endDate: startWeekTo.format("YYYYMMDD"),
        email: this.user.userInfo.email,
      };
      weekOfMonthList.push(startObj);
      this.selectMonthReport(weekOfMonthList);
    },
    // 몇 번째 주인지 계산
    getWeek(from, to, month, startWeekTo, startMonth) {
      let result = 0;
      const isoWeek = this.$moment(from).isoWeek();
      const monthIsoWeek = this.$moment(from).startOf("month").isoWeek();

      if (from.getMonth() !== to.getMonth()) {
        // 한주의 시작일과 마지막일의 월이 다른 경우

        // month값이 마지막일의 월과 같으면 첫째주,
        if (to.getMonth() + 1 === month) {
          return 1;
        } else if (from.getMonth() + 1 === month) {
          // month값이 시작일의 월과 같으면 시작일 월의 마지막 주

          if (to === startWeekTo) {
            // startWeek 계산
            // 전 달의 1일
            const firstDayLastMonth = this.$moment(from).startOf("month")._d;
            // 전 달 1일이 포함된 주의 목요일
            const firstDayLastMonthThu = this.$moment(
              firstDayLastMonth
            ).isoWeekday(4)._d;
            // 전달 첫번째 주가 전전달의 마지막 주일때
            if (
              firstDayLastMonth.getMonth() !== firstDayLastMonthThu.getMonth()
            ) {
              result =
                isoWeek - monthIsoWeek > 0 ? isoWeek - monthIsoWeek : isoWeek;
            } else {
              result = isoWeek - monthIsoWeek + 1;
            }
          } else {
            // endWeek 계산
            // 첫번째 주가 전 달의 마지막 주일때
            if (startWeekTo.getMonth() + 1 !== startMonth) {
              result =
                isoWeek - monthIsoWeek > 0 ? isoWeek - monthIsoWeek : isoWeek;
            } else {
              result = isoWeek - monthIsoWeek + 1;
            }
          }
        }
      } else {
        // 한주의 시작일과 마지막일의 월이 같은 경우

        // 첫번째 주가 전 달의 마지막 주일때
        if (startWeekTo.getMonth() + 1 !== startMonth) {
          result =
            isoWeek - monthIsoWeek > 0 ? isoWeek - monthIsoWeek : isoWeek;
        } else {
          result = isoWeek - monthIsoWeek + 1;
        }
      }
      return result;
    },
    // 월 보고서 조회
    selectMonthReport(weekOfMonthList) {
      const reportRequestDto = {
        email: this.user.userInfo.email,
        startDate: this.$moment(this.period.from).format("YYYYMMDD"),
        endDate: this.$moment(this.period.to).format("YYYYMMDD"),
        lastMonthStartDate: this.$moment(this.period.from).format("YYYYMMDD"),
        lastMonthEndDate: this.$moment(this.period.to).format("YYYYMMDD"),
        periodDtoList: weekOfMonthList,
      };
      console.log("reportRequestDto>>", reportRequestDto);
      this.$store
        .dispatch("reportStore/selectMonthReport", reportRequestDto)
        .then((res) => {
          console.log("결과>", res.data);
          // 수입/지출 합계, 지난달 일평균
          this.totalExpenditureNumber = res.data.totalExpenditure;
          this.totalIncomeNumber = res.data.totalIncome;
          this.dailyAverageLastMonthNumber = res.data.dailyAverageLastMonth;
          // 요약 테이블 data
          _.forEach(res.data.weeklyExpenditureDtoList, function (obj, index) {
            res.data.weeklyExpenditureDtoList[index].weeklyExpenditure =
              String(obj.weeklyExpenditure).replace(
                /\B(?=(\d{3})+(?!\d))/g,
                ","
              ) + "원";
            res.data.weeklyExpenditureDtoList[index].bottom =
              res.data.weeklyExpenditureDtoList.length === index + 1;
          });
          this.summaryTable.items = res.data.weeklyExpenditureDtoList;
          // 총 합계 테이블 data
          this.totalTable.items = [
            { title: "총 지출", total: this.totalExpenditure + "원" },
          ];

          // 카테고리 통계 차트 data
          this.setPieChartData(res.data.categoryStatisticsDtoList);
          // 카테고리 테이블 data
          const monthReportData = _.cloneDeep(res.data);
          this.setPieChartTableData(monthReportData);

          // 일별 차트 data
          const period_from = _.cloneDeep(this.period.from);
          const period_to = _.cloneDeep(this.period.to);
          const diff = this.$moment(period_to).diff(period_from, "days") + 1;
          let updateLineChartData = [];
          for (let i = 0; i < diff; i++) {
            const dt = this.$moment(period_from)
              .add(i, "days")
              .format("YYYY-MM-DD");
            const year = Number(dt.substr(0, 4));
            const month = Number(dt.substr(5, 2));
            const day = Number(dt.substr(8, 2));
            // 해당 일자에 지출 내역이 있는지 확인
            const filterDailyExpenditure = _.filter(
              res.data.dailyExpenditureDtoList,
              {
                expenditureYear: year,
                expenditureMonth: month,
                expenditureDay: day,
              }
            );
            let dailyExpenditure =
              filterDailyExpenditure.length > 0
                ? filterDailyExpenditure[0].dailyExpenditure
                : 0;
            const arry = [
              new Date(year, month - 1, day),
              dailyExpenditure,
              res.data.dailyAverageLastMonth,
            ];
            updateLineChartData.push(arry);
          }
          this.updateLineChartData = updateLineChartData;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 카테고리 통계 차트 data
    setPieChartData(pieChartRows) {
      this.pieChartRows = pieChartRows;
      let updatePieChartData = [];
      if (this.selected === "income") {
        // 수입 radio button
        const incomeCtgryList = _.filter(this.pieChartRows, {
          categoryType: "INC",
        });
        _.forEach(incomeCtgryList, function (obj, index) {
          updatePieChartData.push([obj.largeCategoryName, obj.totalCategory]);
        });
      } else {
        // 지출 radio button
        const expenditureCtgryList = _.filter(this.pieChartRows, {
          categoryType: "EXP",
        });
        _.forEach(expenditureCtgryList, function (obj, index) {
          updatePieChartData.push([obj.largeCategoryName, obj.totalCategory]);
        });
      }
      this.updatePieChartData = updatePieChartData;
    },
    // 카테고리 테이블 data
    setPieChartTableData(monthReportData) {
      this.monthReportData = monthReportData;
      let updatePieChartTableData = [];
      let total = 0;
      if (this.selected === "income") {
        // 수입 radio button
        total = this.totalIncome; // 총 수입
        const incomeCtgryList = _.filter(
          monthReportData.categoryStatisticsDtoList,
          {
            categoryType: "INC",
          }
        );
        _.forEach(incomeCtgryList, function (obj, index) {
          updatePieChartTableData.push({
            largeCategoryName: obj.largeCategoryName,
            totalCategory:
              String(obj.totalCategory).replace(/\B(?=(\d{3})+(?!\d))/g, ",") +
              "원",
          });
        });
      } else {
        // 지출 radio button
        total = this.totalExpenditure; // 총 지출
        const expenditureCtgryList = _.filter(
          monthReportData.categoryStatisticsDtoList,
          {
            categoryType: "EXP",
          }
        );
        _.forEach(expenditureCtgryList, function (obj, index) {
          updatePieChartTableData.push({
            largeCategoryName: obj.largeCategoryName,
            totalCategory:
              String(obj.totalCategory).replace(/\B(?=(\d{3})+(?!\d))/g, ",") +
              "원",
          });
        });
      }
      // 전체 합계
      updatePieChartTableData.push({
        largeCategoryName: "전체",
        totalCategory: total + "원",
      });
      this.chartTable.items = updatePieChartTableData;
    },
  },
};
</script>
<style>
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
.monthReport_center_content_table {
  width: 350px;
  max-height: 190px;
  overflow-y: auto;
  margin-left: auto;
  margin-right: auto;
}
.monthReport_center_content_table tr:last-child td {
  font-weight: bold;
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
}
.monthReport_right_content > table {
  margin-top: 20px;
  width: 400px;
}
.monthReport_right_content > table tr td {
  font-weight: bold;
}
.monthReport_right_content > table tr:last-child td {
  color: #888888;
}
</style>
