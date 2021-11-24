<template>
  <v-card class="my-5">
    <v-card-title>SR 반려</v-card-title>
    <v-container fluid>
      <v-row class="justify-center">
        <v-col cols="1">
          <v-subheader class="pb-2 mr-2">반려자</v-subheader>
        </v-col>
        <v-col cols="2" class="ml-n10" align="left">
           <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="retrner"></v-text-field>
        </v-col>
        <v-col cols="1">
          <v-subheader class="pb-2 mr-2">반려일</v-subheader>
        </v-col>
        <v-col cols="2" class="ml-n10" align="left">
          <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="rjtTim"></v-text-field>
        </v-col>
        <v-spacer></v-spacer>
      </v-row>
      
      <v-row class="mt-n7 d-flex justify-left">
        <v-col cols="1">
          <v-subheader class="pb-2 mr-2 ml-n3">반려사유</v-subheader>
        </v-col>
        <v-col class="ml-n7" align="left" >
          <div class="ml-n3 mr-1 px-3" style="height: 150px; border: 1px solid #EEEEEE; border-radius: 5px">
            <div class="mt-3">{{rjtDscr}}</div>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import API from "@/api/API";
export default {
  data() {
    return {
      srId: 0,
      retrner: "",
      rjtTim: "",
      rjtDscr: "",
      modal: false,
    };
  },
  created() {
    this.srId = Number(this.$route.params.srId);
    // sr의 정보 가져오기
    API.getRejectWithSrId(
      "srId=" + this.srId,
      (res) => {
        var t = new Date(res.rjtTim);
        var year = t.getFullYear();
        var month = t.getMonth() + 1;
        if (month < 10) month = "0" + month;
        var date = t.getDate();
        if (date < 10) date = "0" + date;
        this.rjtTim = year + "-" + month + "-" + date;
        this.retrner=res.retrner;
        this.rjtDscr=res.rjtDscr
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
  },
};
</script>

<style scoped>
.v-subheader {
  font-weight: bold;
  font-size: 1.07em;
}
</style>
