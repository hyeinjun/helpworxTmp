<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row align="center">
            <v-col> <div class="pageTitle">SR 접수 / 처리</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>

          <!-- 진행 상태 안내 -->
          <ProgressArrow />
          <sr-detail-card-form
            :sysNm="sysNm"
            :jobGb="jobGb"
            :reqGb="reqGb"
            :sbjct="sbjct"
            :dscr="dscr"
          />

          <sr-rcpt-card-form
            v-if="!isReturned && loadingSr && loadingState"
            :reqGb="reqGb"
            :jobGb="jobGb"
            :sysNm="sysNm"
            :isCmplTreat="isCmplTreat"
            :isCmplRcpt="isCmplRcpt"
            :hasAuth="hasAuth"
            @rcptForm="rcptForm"
          ></sr-rcpt-card-form>

          <sr-treat-card-form
            :sysNm="sysNm"
            :isCompleted="isCmplTreat"
            :hasAuth="hasAuth"
            v-if="isCmplRcpt && !isReturned && loadingSr && loadingState"
          ></sr-treat-card-form>

          <sr-reject-card v-if="isReturned"></sr-reject-card>

          <v-row class="justify-center mt-1">
            <!-- 반려 버튼 -->
            <v-col cols="auto">
              <v-btn
                v-if="!isReturned && hasAuth"
                class="mx-1"
                color="rgb(42, 46, 72)"
                dark
                @click="dialog_delete = true"
              >반려</v-btn>
              <v-btn
                v-else-if="isReturned && hasAuth"
                class="mx-1"
                color="rgb(42, 46, 72)"
                dark
                @click.stop="dialog_cancel = true"
              >반려 취소</v-btn>
              <v-btn color="rgb(42, 46, 72)" dark @click="goBack">닫기</v-btn>
            </v-col>
          </v-row>
          
          <v-dialog v-model="dialog_delete" width="500">
            <v-card class="text-center">
              <v-card-title class="headline">
                반려 이유를 작성해주세요.
              </v-card-title>
              <div class="rejectDiv mt-3 mb-n2">
                <v-text-field
                  class="rejectTextField"
                  dense
                  solo
                  v-model="rjtDscr"
                ></v-text-field>
              </div>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red" text @click="returnedSr">
                  확인
                </v-btn>
                <v-btn
                  color="green darken-1"
                  text
                  @click="dialog_delete = false"
                >
                  취소
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <yes-no-dialog
            v-if="dialog_cancel"
            dialogTitle="정말로 취소하시겠습니까?"
            dialogText="SR 반려 내역이 취소됩니다."
            @click="clickCancelDialog"
          ></yes-no-dialog>
          
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import ProgressArrow from "../../components/srViewer/ProgressArrow.vue";
import SrDetailCardForm from "../../components/srViewer/SrDetailCardForm.vue"
import SrRcptCardForm from "../../components/operator/SrRcptCardForm.vue";
import SrTreatCardForm from "../../components/srViewer/SrTreatCardForm.vue";
import SrRejectCard from "../../components/operator/SrRejectCard.vue";
import API from "../../api/API";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
import YesNoDialog from '../../components/common/YesNoDialog.vue'

export default {
  components: {
    ProgressArrow,
    SrDetailCardForm,
    // OldSrDetailCardForm,
    SrRcptCardForm,
    SrTreatCardForm,
    SrRejectCard,
    BreadCrumbs,
    YesNoDialog,
  },
  data() {
    return {
      hasAuth: false,
      usrId: this.$store.state.usrInfo.usrId,
      srId: 0,
      coCd: "",
      jobGb: "",
      sysNm: "",
      reqGb: "",
      sbjct: "",
      dscr: "",
      rcptDscr: "",
      cmplPrdt: "",
      recivrId: "",
      opetrId: "",
      treatDscr: "",
      treatBeginTim: "",
      treatEndTim: "",
      rqmtTim: "",
      dialog_delete: false,
      dialog_cancel: false,
      isCmplRcpt: false,
      isCmplTreat: false,
      isReturned: false,
      rjtDscr: "",
      loadingState: false,
      loadingSr: false,
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/operator/main",
        },
        {
          text: "SR 조회",
          disabled: false,
          href: "/operator/srlist",
        },
        {
          text: "SR 접수 / 처리",
          disabled: true,
        },
      ],
    };
  },
  created() {
    this.srId = this.$route.params.srId;
    
    API.getSrWithSrId(
      `srId=${this.srId}`,
      (res) => {
        this.sysNm = res.sysNm;
        this.jobGb = res.jobGb;
        this.reqGb = res.reqGb;
        this.sbjct = res.sbjct;
        this.dscr = res.dscr;
        this.coCd = res.coCd;
        this.$emit("srDetail",this.sysNm,this.jobGb,this.reqGb);

        if(this.coCd == this.$store.state.usrInfo.coCd) {
          this.hasAuth = true;
        }
        
        this.loadingSr = true;
      },
      (err) => {
        console.log(err);
      }
    );

    API.getRcptTreatState(
      "srId=" + this.srId,
      (res) => {
        console.log(res);
        this.isReturned = res.isReturned;
        this.isCmplRcpt = res.isCmplRcpt;
        this.isCmplTreat = res.isCmplTreat;
        this.loadingState = true;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    srDetail(sysNm, jobGb, reqGb) {
      this.jobGb = jobGb;
      this.sysNm = sysNm;
      this.reqGb = reqGb;
    },
    rcptForm(recivrId, rcptDscr, cmplPrdt) {
      this.recivrId = recivrId;
      this.rcptDscr = rcptDscr;
      this.cmplPrdt = cmplPrdt;
    },
    treatForm(opetrId, treatDscr, treatBeginTim, treatEndTim, rqmtTim) {
      this.opetrId = opetrId;
      this.treatDscr = treatDscr;
      this.treatBeginTim = treatBeginTim;
      this.treatEndTim = treatEndTim;
      this.rqmtTim = rqmtTim;
    },
    // 반려
    returnedSr() {
      this.dialog_delete = false;
      let data = {};
      data.srId = this.srId;
      data.rjtDscr = this.rjtDscr;
      data.rjtTim = new Date();
      data.retrner = this.usrId;
      API.rejectSr(
        data,
        (res) => {
          console.log(res);
          this.$router.go();
        },
        (err) => {
          console.log(err);
        }
      );
    },
    // 반려 취소
    cancelReturnedSr() {
      this.dialog_cancel = false;
      let data = "srId=" + Number(this.srId);
      API.cancelReturnSr(
        data,
        (res) => {
          console.log("새로고침");
          console.log(res);
          this.$router.go();
        },
        (err) => {
          console.log(err);
        }
      );
    },
    goBack() {
      this.$router.go(-1);
    },
    clickCancelDialog(value) {
      if(value == 'Y') {
        this.cancelReturnedSr();
      }else {
        this.dialog_cancel = false;
      }
    }
  },
};
</script>

<style scoped>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
.rejectTextField {
  width: 400px;
}
.rejectDiv {
  text-align: -webkit-center;
}
</style>
