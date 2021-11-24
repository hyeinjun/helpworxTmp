<template>
    <v-card height="600" class="justify-center">
    <v-toolbar color="rgb(42, 46, 72)" dark>
      <v-toolbar-title>HelpWorX</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
          large
          @click="closeModal"
          icon
        >
          <v-icon dark>
            mdi-close-circle
          </v-icon>
        </v-btn>
    </v-toolbar>
    <v-main class="ma-3">
      <div>
        <v-card-title class="justify-center">문의할 시스템을 선택해주세요!</v-card-title>
      </div>

      <!-- 시스템 고르기 -->
      <div v-for="(item, index) in sysList" :key="index" >
        <v-btn elevation="2" outlined 
          class="sysBtn my-5" 
          @click="selectedSys(item)">
          {{item.sysNm}} 
        </v-btn>
      </div>

    </v-main>
  </v-card>
</template>

<script>
import API from "@/api/API";
import { mdiCloseCircle } from "@mdi/js";

export default {
  data() {
    return {
      mdiCloseCircle,
      dialog_start: true,
      newInquId: 0,
      coCd: this.$store.state.usrInfo.coCd,
      usrId: this.$store.state.usrInfo.usrId,
      usrNm: this.$store.state.usrInfo.usrNm, // vuex에 저장된 usrNm을 사용usrId: "minjun",
      sysCd: 0,
      sysNm: "",
      // 회사에서 사용하는 시스템만 문의 가능하므로 배열에 담기
      sysList: [],
    };
  },
  props: ["resetSysInfo"],
  created() {
    API.getUsingSysList(
      "coCd="+this.coCd,
      (res) => {
        this.sysList = res;
      },
      (err) => {
        console.log(err);
      }
    )
  },
  methods: {
    closeModal() {
      console.log(this.sysCd+" "+this.sysNm);

      this.$emit("changeDialogStart", false, this.sysCd, this.sysNm);
    },
    selectedSys(target) {
      this.sysCd = target.sysId;
      this.sysNm = target.sysNm;
      console.log(this.sysCd+" "+this.sysNm);
    }
  },
  watch: {
    sysCd() {
      if(this.sysCd>0)
        this.closeModal();
    },
    resetSysInfo() {
      this.sysCd=0;
      this.sysNm="";
    }
  }
};
</script>

<style scoped>
.sysBtn {
  width: 150px;
}
.sysBtn:hover {
  background-color: rgb(42, 46, 72);
  color: aliceblue;
}
</style>