<template>
  <div>
    <v-main class="ma-3">
      <v-container fluid>
        <v-row >
          <v-col> <div class="pageTitle">실시간 문의 조회</div> </v-col>
          <v-spacer></v-spacer>
          <v-col>
            <BreadCrumbs :crumbs="crumbs" />
          </v-col>
        </v-row>
        <v-card>
          <v-container fluid>
            <v-row class="pt-1" style="align-items:baseline;">
              <v-col sm="1">* 시스템
              </v-col>
              <v-col cols="2" class="ml-n5" align="left">
                <v-select
                  v-model="selectedSys"
                  :items="systemList"
                  dense
                  outlined
                ></v-select>
              </v-col>
              <v-col sm="1" class="ml-5">* 상태
              </v-col>
              <v-col cols="2" class="ml-n5" align="left">
                <v-select
                  v-model="selectedState"
                  :items="stateList"
                  dense
                  outlined
                ></v-select>
              </v-col>
               <v-spacer></v-spacer>
            </v-row>

            <v-row class="justify-center my-n8"  style="align-items:baseline;" >
              <v-col sm="1">
              내용
              </v-col>
              <v-col cols="5" class="ml-n5 " align="left">
                <v-text-field outlined v-model="dscr" dense></v-text-field>
              </v-col>
              <v-spacer></v-spacer>
              <v-col cols="12" md="1">
                <v-btn
                  color="rgb(42, 46, 72)"
                  dark
                  value="조회"
                  @click="clickButton"
                >
                  조회
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
        <v-card class="pa-6 my-5">
            <searched-inqu-list
            :requestData="requestData"
            :router="router"
          ></searched-inqu-list>
        </v-card>

      </v-container>
    </v-main>
  </div>
</template>

<script>
import SearchedInquList from "../../components/operator/SearchedInquList.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
import API from "@/api/API";

export default {
  components: {
    SearchedInquList,
    BreadCrumbs,
  },
  data() {
    return {
      coCd: this.$store.state.usrInfo.coCd,
      systemList: [],
      stateList: ["전체", "진행중", "완료"],
      selectedState: "진행중",
      selectedSys: "",
      dscr: "",
      requestData: [],
      router: "/inqu/find/co-sys-state-dscr",
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/operator/main",
        },
        {
          text: "실시간 문의 목록",
          disabled: true,
        },
      ],
    };
  },
  created() {
    API.getSysNmByCoCd(
      "coCd="+this.coCd,
      (res) => {
          this.systemList=[];
          for (let index = 0; index < res.length; index++) {
              this.systemList.push(res[index].text);
          }
          this.selectedSys = this.systemList[0];
          this.updateJobGb();
      },
      (err) => {
          console.log(err);
      }
    );
  },
  methods: {
    clickButton() {
      var sysCd = "";
      this.requestData = [];
      if (this.selectedSys == "PeopleWorx") sysCd = 1;
      else if (this.selectedSys == "SalesWorx") sysCd = 2;
      else if (this.selectedSys == "r-PLANNA") sysCd = 3;
      else sysCd = 4;

      this.requestData = {
        coCd: this.coCd,
        sysCd: sysCd,
        state: this.selectedState,
        dscr: this.dscr,
      };
    },
  },
};
</script>

<style>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
</style>
