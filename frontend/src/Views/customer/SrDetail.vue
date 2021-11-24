<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">SR 상세</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <!-- 진행 상태 안내 -->
          <ProgressArrow />
          <SrDetailCardForm
            :sysNm="sysNm"
            :jobGb="jobGb"
            :reqGb="reqGb"
            :sbjct="sbjct"
            :dscr="dscr"
          />
          <SrRcptCardForm
            :reqGb="reqGb"
            :jobGb="jobGb"
            :sysNm="sysNm"
            :isCustomer="true"
            v-if="isCmplRcpt"
          />
          <SrTreatCardForm
            :sysNm="sysNm"
            v-if="isCmplTreat"
            :isCompleted="isCmplTreat"
          />
          <sr-reject-card v-if="isReturned"></sr-reject-card>

          <v-row class="justify-center mt-1">
            <v-col>
              <v-btn
                v-if="!isCmplRcpt && !isReturned && usrId == writer"
                class="ma-1"
                color="rgba(42, 46, 72)"
                dark
                @click="goModifyPage"
              >
                수정
              </v-btn>
              <v-btn
                v-if="!isCmplRcpt && !isReturned && usrId == writer"
                class="ma-1"
                color="rgb(42, 46, 72)"
                dark
                @click.stop="dialog_delete = true"
              >
                삭제</v-btn
              >
              <v-btn @click="goBack" class="ma-1" color="rgb(42, 46, 72)" dark>
                닫기
              </v-btn>
            </v-col>

            <yes-no-dialog
              v-if="dialog_delete"
              dialogTitle="정말로 삭제하시겠습니까?"
              dialogText="삭제하면 되돌릴 수 없습니다."
              @click="dialog_delete"
            ></yes-no-dialog>
          </v-row>

          <v-dialog v-model="dialog_back" width="500" class="text-center">
            <v-card>
              <v-card-title class="headline">
                삭제되었습니다.
              </v-card-title>
              <v-card-text>이전 페이지로 되돌아갑니다.</v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red" text @click="goBack">
                  YES
                </v-btn>
                <v-btn color="green darken-1" text @click="dialog_back = false">
                  NO
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import ProgressArrow from "../../components/srViewer/ProgressArrow.vue";
import SrDetailCardForm from "../../components/srViewer/SrDetailCardForm.vue";
import SrTreatCardForm from "../../components/srViewer/SrTreatCardForm.vue";
import SrRcptCardForm from "../../components/customer/SrRcptCardForm.vue";
import SrRejectCard from "../../components/operator/SrRejectCard";
import API from "../../api/API";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
import YesNoDialog from "../../components/common/YesNoDialog.vue";

export default {
  components: {
    ProgressArrow,
    SrDetailCardForm,
    SrTreatCardForm,
    SrRcptCardForm,
    SrRejectCard,
    BreadCrumbs,
    YesNoDialog,
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      srId: 0,
      jobGb: "",
      sysNm: "",
      reqGb: "",
      sbjct: "",
      dscr: "",
      writer: "",
      isCmplRcpt: false,
      isCmplTreat: false,
      isReturned: false,
      dialog_delete: false,
      dialog_back: false,
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/customer/main",
        },
        {
          text: "SR 조회",
          disabled: false,
          href: "/customer/sr/list",
        },
        {
          text: "SR 상세",
          disabled: true,
        },
      ],
    };
  },
  created() {
    this.srId = this.$route.params.srId;
    API.getSrWithSrId(
      "srId=" + this.srId,
      (res) => {
        this.sysNm = res.sysNm;
        this.jobGb = res.jobGb;
        this.reqGb = res.reqGb;
        this.sbjct = res.sbjct;
        this.dscr = res.dscr;
        this.writer = res.writer;
      },
      (err) => {
        console.log(err);
      }
    );

    API.getRcptTreatState(
      "srId=" + this.srId,
      (res) => {
        this.isCmplRcpt = res.isCmplRcpt;
        this.isCmplTreat = res.isCmplTreat;
        this.isReturned = res.isReturned;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    deleteSr() {
      let data = "srId=" + Number(this.srId);
      API.deleteSr(
        data,
        (res) => {
          console.log(res);
          this.dialog_back = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
    goBack() {
      this.dialog_back = false;
      // sr list 페이지로 이동
      this.$router.push("/customer/sr/list");
    },
    goModifyPage() {
      this.$router.push("/customer/sr/modify/" + this.srId);
    },
    clickDialog(value) {
      this.dialog_delete = false;
      if (value === "Y") {
        this.deleteSr();
      }
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
