<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row align="center">
            <v-col> <div class="pageTitle">SR 현황</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>

          <!-- 표 카드 -->
          <sr-status-table-card
            @selectedCompany="selectedCompany"
            @selectedSystem="selectedSystem"
            :rcptAvgTime="rcptAvgTime"
            :treatAvgTime="treatAvgTime"
          ></sr-status-table-card>

          <!-- 차트 카드 -->
          <v-card class="pa-6 my-5">
            <v-row no-gutters>
              <v-col cols="6" md="6">
                <line-chart
                  v-if="loadedChart1"
                  :chartDataProps="chartData"
                  :chartOptionsProps="chartOptions"
                  :styles="chartStyle"
                ></line-chart>
              </v-col>
              <v-col cols="6" md="6">
                <line-chart
                  v-if="loadedChart2"
                  :chartDataProps="chartData2"
                  :chartOptionsProps="chartOptions"
                  :styles="chartStyle"
                ></line-chart>
              </v-col>
            </v-row>
          </v-card>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import LineChart from "../../components/operator/LineChart.js";
import SrStatusTableCard from "../../components/operator/SrStatusTableCard.vue";
import API from "@/api/API";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    LineChart,
    SrStatusTableCard,
    BreadCrumbs,
  },
  data() {
    return {
      coCd: null,
      sysCd: 0,
      loadedChart1: true,
      loadedChart2: true,
      chartData: {
        labels: [],
        datasets: [],
      },
      chartData2: {
        labels: [],
        datasets: [],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
                suggestedMax: 20,
                maxTicksLimit: 10,
              },
            },
          ],
        },
      },
      chartStyle: {
        fluid: true,
      },
      treatAvgTime: 0,
      rcptAvgTime: 0,
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/operator/main",
        },
        {
          text: "SR 현황",
          disabled: true,
        },
      ],
    };
  },
  created() {},
  mounted() {},
  methods: {
    getSrCountData() {
      API.getSrCntStatusChart(
        `coCd=${this.coCd}&sysCd=${this.sysCd}`,
        (res) => {
          this.loadedChart1 = false;
          this.chartData.labels = res.label;
          this.chartData.datasets = [
            {
              label: "SR 건수",
              fill: false,
              data: res.data,
              borderColor: "rgb(0,120,195)",
              lineTension: 0.1,
            },
          ];
          this.loadedChart1 = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
    getAvgTimeData() {
      API.getSrAvgTimeDataChart(
        `coCd=${this.coCd}&sysCd=${this.sysCd}`,
        (res) => {
          this.loadedChart2 = false;
          this.chartData2.labels = res.label;
          this.chartData2.datasets = [
            {
              label: "평균 접수 시간",
              fill: false,
              data: res.rcptData,
              borderColor: "#00AFE6",
              lineTension: 0.1,
            },
            {
              label: "평균 처리 시간",
              fill: false,
              data: res.treatData,
              borderColor: "#F5911E",
              lineTension: 0.1,
            },
          ];
          this.loadedChart2 = true;

          let index = 6;
          this.treatAvgTime = Number(
            Number.parseFloat(res.treatData[index]).toFixed(1)
          );
          this.rcptAvgTime = Number(
            Number.parseFloat(res.rcptData[index]).toFixed(1)
          );
        },
        (err) => {
          console.log(err);
        }
      );
    },
    selectedCompany(value) {
      this.coCd = value;
    },
    selectedSystem(value) {
      this.sysCd = value;
      this.getSrCountData();
      this.getAvgTimeData();
    },
  },
};
</script>

<style></style>
