<template>
  <v-card class="mt-3 pa-1">
    <v-card-title>SR 접수</v-card-title>
    <v-container fluid>
      <v-row class="mt-n7 d-flex">
        <v-col cols="1">
          <v-subheader class="pb-2 mr-2" style="white-space:nowrap;">접수자</v-subheader>
        </v-col>
        <v-col cols="2" class="ml-n10" align="left">
          <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="receiverId"></v-text-field>
        </v-col>
        <v-col cols="1">
          <v-subheader class="pb-2 mr-2 ml-n4 pl-1" style="font-size:1.05em; white-space:nowrap;">완료예정일</v-subheader>
        </v-col>
        <v-col cols="2" class="ml-n10">
          <date-picker
            v-if="loadTextFields"
            :propsDateInfo="cmplPrdt"
            :readOnly="isCustomer"
          ></date-picker>
        </v-col>
        <v-spacer></v-spacer>
      </v-row>

      <v-row class="mt-n7">
        <v-col cols="1">
          <v-subheader class="pb-2">내용</v-subheader>
        </v-col>
        <v-col class="ml-n10" align="left">
          <div class="px-3 mr-1 textViewer">
            <viewer
              v-if="rcptDscr!=null"
              :initialValue="rcptDscr"
              height="300px"
            />
          </div>
          
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import API from "@/api/API";
import DatePicker from '../common/DatePicker.vue'

export default {
  components: {
    DatePicker
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      coCd: this.$store.state.usrInfo.coCd,
      srId: 0,
      rcptDscr: null,
      cmplPrdt: "",
      receiverId: "",
      dialog_delete: false,
      modal:"",
      loadTextFields: false
    };
  },
  props: ["sysNm", "jobGb", "reqGb", "isCmplTreat", "isCustomer"],
  created() {
    this.srId = Number(this.$route.params.srId);
  
    // 접수 정보 가져오기
    this.getRcpt();

  },
  watch: {
    cmplPrdt(){
      this.$emit("rcptForm",this.usrId, this.rcptDscr, this.cmplPrdt);
    },
    rcptDscr(){
      this.$emit("rcptForm",this.usrId, this.rcptDscr, this.cmplPrdt);
    }
  },
  methods: {
    getRcpt() {
      API.getRcptBySrId(
        "srId=" + this.srId,
        (res) => {
          var t = new Date(res.cmplPrdt);
          var year = t.getFullYear();
          var month = (t.getMonth()+1);
          if(month<10) month = '0'+month;
          var date = t.getDate();
          if(date<10) date = '0'+date;
          this.rcptDscr = res.rcptDscr;
          this.receiverId = res.recivrId;
          this.cmplPrdt = year+'-'+month+'-'+date;
          this.loadTextFields = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
    deleteRcpt() {
      this.dialog_delete = false;
      API.deleteRcpt(
        "srId=" + this.srId,
        (res) => {
          console.log(res);
          this.getRcpt();
        },
        (err) => {
          console.log(err);
        }
      );
      this.$router.go(0);
    },
  },
};
</script>

<style scoped>
.v-subheader {
  font-weight: bold;
  font-size: 1.07em;
}
</style>
