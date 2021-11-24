<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row align="center">
            <v-col> <div class="pageTitle">SR 시스템 현황</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <v-card class="pa-6 my-5">
            <v-row no-gutters>
              <v-col cols="1" class="pt-2">
                * 사용 시스템
              </v-col>
              <v-col class="d-inline-flex" cols="2">
                <combo-box-component
                  :items="systemList"
                  @selected="selectedSystemComboBox"
                ></combo-box-component>
              </v-col>
            </v-row>
            <bar-chart
              v-if="loaded"
              :chartDataProps="chartData"
              :chartLabelProps="chartLabel"
            ></bar-chart>
          </v-card>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import { mapState } from "vuex";
import BarChart from "../../components/operator/BarChart.js";
import ComboBoxComponent from "../../components/customer/ComboBoxComponent.vue";
import API from "@/api/API";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    BarChart,
    ComboBoxComponent,
    BreadCrumbs,
  },
  data() {
    return {
      loaded: false,
      chartData: [],
      chartLabel: [],
      sysCd: 0,
      systemList: [],
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/operator/main",
        },
        {
          text: "SR 시스템 현황",
          disabled: true,
        },
      ],
    };
  },
  created() {
    this.systemList = this.systemByCompany[this.$store.state.usrInfo.coCd];
  },
  mounted() {
    this.chartLabel = [];
    this.companyList.forEach((e) => this.chartLabel.push(e.text));
    this.loaded = true;
  },
  methods: {
    selectedSystemComboBox(value) {
      this.sysCd = value;

      this.chartData = [];

      let color = [
        "#02355A",
        "#D9ECF0",
        "#A5D1D9",
        "#166a8f",
        "#4E5B6B",
        "#537bc4",
        "#001537",
      ];

      this.loaded = false;

      API.getSrSystemChart(
        "sysCd=" + this.sysCd,
        (res) => {
          res.forEach((element, index) => {
            this.chartData.push({
              label: element.label,
              data: element.data,
              backgroundColor: color[index],
              barPercentage: 0.4,
            });
          });
          this.loaded = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
  },
  computed: {
    ...mapState({
      // 고객사 선택 comboBox
      companyList: (state) => state.CommonData.companyList,
      systemByCompany: (state) => state.CommonData.systemByCompany,
    }),
  },
};
</script>

<style></style>
