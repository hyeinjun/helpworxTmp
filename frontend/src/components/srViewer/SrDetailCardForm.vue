<template>
  <v-card class="mt-5 pa-1">
    <v-card-title>SR 상세</v-card-title>
      <v-container fluid>
        <v-row class="justify-center mt-n7" align-content="center">
          <v-col cols="1">
            <v-subheader style="align-content: center;" class="pb-2">시스템</v-subheader>
          </v-col>
          <v-col cols="2" class="ml-n10" align="left">
            <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="sysNm"></v-text-field>
          </v-col>
          <v-col cols="auto">
            <v-subheader class="pb-2 mr-2">업무구분</v-subheader>
          </v-col>
          <v-col cols="2" class="ml-n10" align="left">
            <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="jobGb"></v-text-field>
          </v-col>
          <v-col cols="auto">
            <v-subheader class="pb-2 mr-2">요청구분</v-subheader>
          </v-col>
          <v-col cols="2" class="ml-n10" align="left">
            <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="reqGb"></v-text-field>
          </v-col>
          <v-spacer></v-spacer>
        </v-row>
      
        <v-row class="mt-n7 d-flex justify-left">
          <v-col cols="1">
            <v-subheader class="pb-2">제목</v-subheader>
          </v-col>
          <v-col class="ml-n10 mr-1" align="left">
            <v-text-field class="only-outline" outlined flat dense readonly :value="sbjct"></v-text-field>
          </v-col>
        </v-row>
        <v-row class="mt-n7 d-flex justify-left" style="height: 330px;">
          <v-col cols="1">
            <v-subheader class="pb-2">내용</v-subheader>
          </v-col>
          <v-col class="ml-n7" align="left" >
            <div class="ml-n3 mr-1 px-3 textViewer">
              <viewer
              v-if="srDscr!=null"
              :initialValue="srDscr"
              height="300px"
            /> 
            </div>
          </v-col>
        </v-row>
      </v-container>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      srId: 0,
      dialog_delete: false,
      dialog_back: false,
      srDscr:null,
    };
  },
  props: ["isCmplRcpt", "sysNm", "jobGb", "reqGb", "sbjct", "dscr"],
  created() {
    this.srId = Number(this.$route.params.srId);
  },
  watch: {
    dscr() {
      this.srDscr = this.$props.dscr;
    }
  }
};
</script>

<style scoped>
.v-subheader {
  font-weight: bold;
  font-size: 1.07em;
}
.only-outline.v-text-field--outlined >>> fieldset {
  border-color: #EEEEEE;
}

</style>
