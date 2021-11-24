<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-subheader>요청 코드</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="reqId"></v-text-field>
      
      </v-col>
    </v-row>
    <v-row class="mt-n7 justify-center">
      <v-col cols="2">
        <v-subheader>요청자</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="usrId"></v-text-field>
      
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="2">
        <v-subheader>진행상황</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10 mr-6" align="left">
        <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="state"></v-text-field>
      
        
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>요청 제목</v-subheader>
      </v-col>
      <v-col cols="5" class="ml-n10" align="left">
        <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="reqSbjct"></v-text-field>
      
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>요청 내용</v-subheader>
      </v-col>
      
      <v-col class="ml-n7 mr-5" align="left">
        <!-- initialValue는 최초 1회만 가져오기 때문에 v-if로 값이 들어왔는지 확인해야한다. -->
        <!-- 회사 설명이 비어있을 수 있으므로 다른 키로 값이 들어왔는지 확인한다. -->
        <div class="ml-n3 mr-1 px-3 textViewer" style=" border: 3px solid #EEEEEE;">
          <viewer v-if="usrId != null" :initialValue="reqDscr"/>
        </div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="2">
        <v-subheader>요청일</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="reqTim"></v-text-field>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import API from "@/api/API";

export default {
  props: {
    curState: String,
  },
  data() {
    return {
      reqId: null,
      usrId: null,
      reqSbjct: null,
      reqDscr: null,
      reqTim: null,
      state: null,
      firstGet: true,
    };
  },
  created() {
    this.reqId = this.$route.params.reqId;
    console.log(this.reqId);
    // 회사 정보 가져오기
    if (this.$store.state.usrInfo.menuAuth == "user") {
      API.getInfoModReq(
        "reqId=" + this.reqId,
        (res) => {
          this.usrId = res.usrId;
          this.reqSbjct = res.reqSbjct;
          this.reqDscr = res.reqDscr;
          this.reqTim = res.reqTim;
          this.state = res.state;
          this.emitInfo();
        },
        (err) => {
          console.log(err);
        }
      );
    } else if (this.$store.state.usrInfo.menuAuth == "manager") {
      console.log("hey");
      API.getMngInfoModReq(
        "reqId=" + this.reqId,
        (res) => {
          this.usrId = res.usrId;
          this.reqSbjct = res.reqSbjct;
          this.reqDscr = res.reqDscr;
          this.reqTim = res.reqTim;
          this.state = res.state;
          this.emitInfo();
        },
        (err) => {
          console.log(err);
        }
      );
    }
  },
  methods: {
    emitInfo() {
      this.$emit("reqId", this.reqId);
      this.$emit("usrId", this.usrId);
      this.$emit("state", this.state);
      console.log("first" + this.firstGet);
    },
    changeState() {
      if (this.firstGet == false) {
        console.log("get");
        let data = {};
        data.state = this.state;
        data.reqId = this.reqId;
        API.modifyInfoState(
          data,
          (res) => {
             console.log(res);
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
  },
  watch: {
    curState() {
      if (this.state != this.curState) {
        this.state = this.curState;
      }
    },
    state() {
      this.changeState();
      this.firstGet = false;
    },
  },

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
