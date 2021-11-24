<template>
  <v-card class="pa-2 my-5"> 
    <!-- 시스템 선택 -->
    <v-row class="mb-n8 pt-1" style="align-items:baseline;">
      <v-col cols="12" md="1" sm="2" >* 시스템</v-col>
      <v-col cols="12" md="2" sm="4" >
        <v-select
          v-model="selectedCoCdAndSysCd"
          :items="sysItems"
          dense
          outlined
        ></v-select>
      </v-col>
    </v-row>

    <!-- 전체 건수, 월별 건수 -->
    <sr-data-table
      :coCd="coCd"
      :sysCd="sysCd"
      :isStatus="false"
    ></sr-data-table>
  </v-card>
</template>

<script>
import API from "@/api/API";
import SrDataTable from "../operator/SrDataTable.vue";

export default {
  components: {
    SrDataTable,
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      coCd: this.$store.state.usrInfo.coCd,
      selectedCoCdAndSysCd: 0,
      sysItems: [],
      sysCd: 0,
      totalItemsLabel: ["총 (건)", "수정", "개선", "오류", "문의"],
      totalItems: {
        totalCnt: 0,
        dataModCnt: 0,
        imprvmCnt: 0,
        errorCnt: 0,
        inquCnt: 0,
      },
      mmItemsLabel: ["이번달", "지난달"],
      mmItems: {
        thisMmCnt: 0,
        prevMmCnt: 0,
      },
    };
  },
  created() {
    API.getSysNmByCoCd(
      "coCd=" + this.coCd,
      (res) => {
        this.sysItems = [];
        for (let index = 0; index < res.length; index++) {
          this.sysItems.push(res[index].text);
        }
        this.selectedCoCdAndSysCd = this.sysItems[0];
        this.updateJobGb();
      },
      (err) => {
        console.log(err);
      }
    );
  },
  watch: {
    // 시스템 선택 시 API 호출
    selectedCoCdAndSysCd(sys) {
      if (sys == "PeopleWorx") this.sysCd = 1;
      else if (sys == "SalesWorx") this.sysCd = 2;
      else if (sys == "r-PLANNA") this.sysCd = 3;
      else this.sysCd = 4;

      // API.getCntWithSysCd(
      //   "coCd=" + this.coCd + "&sysCd=" + sysCd,
      //   (res) => {
      //     console.log(res.totalCnt);
      //     this.totalItems.totalCnt = res.totalCnt;
      //     this.totalItems.dataModCnt = res.dataModCnt;
      //     this.totalItems.imprvmCnt = res.imprvmCnt;
      //     this.totalItems.errorCnt = res.errorCnt;
      //     this.totalItems.inquCnt = res.inquCnt;
      //     this.mmItems.thisMmCnt = res.thisMmCnt;
      //     this.mmItems.prevMmCnt = res.prevMmCnt;
      //   },
      //   (err) => {
      //     console.log(err);
      //   }
      // );
    },
  },
};
</script>

<style scoped>
.cardText {
  font-size: 1.2em;
}
.cardTitle {
  font-family: Bold;
  font-size: 1.2em;
}
</style>
