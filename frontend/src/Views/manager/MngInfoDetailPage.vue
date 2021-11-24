<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
           <v-row>
            <v-col><div class="pageTitle">정보 수정 요청 상세</div></v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>

          <v-card class="pa-5 mb-3">
            <info-mod-req-detail-card
              @reqId="setReqId"
              @usrId="setUsrId"
              @state="setState"
              :curState="curState"
            />

            <v-row class="justify-center mt-5 mb-2">
              <div class="mr-0">
                <v-col cols="12" sm="1" xs="2" class="pa-1">
                  <v-btn color="rgba(42, 46, 72)" dark @click="infoModify"
                    >고객 정보 수정</v-btn
                  >
                </v-col>
              </div>
              <div class="mr-0">
                <v-col cols="12" sm="1" xs="2" class="pa-1">
                  <v-btn
                    color="rgba(42, 46, 72)"
                    dark
                    @click.stop="dialog_delete = true"
                    >삭제</v-btn
                  >
                </v-col>
              </div>
              <div class="mr-0">
                <v-col cols="12" sm="1" xs="2" class="pa-1">
                  <v-btn color="rgb(42, 46, 72)" dark @click="goList"
                    >닫기</v-btn
                  ></v-col
                >
              </div></v-row
            >
          </v-card>
          <yes-no-dialog
            v-if="dialog_delete"
            :dialogText="(dialogText = '삭제한 글은 다시 복구할 수 없습니다.')"
            :dialogTitle="(dialogTitle = '정말로 삭제하시겠습니까?')"
            @click="checkYn"
          />
          <v-dialog v-model="dialog_back" width="500" class="text-center">
            <v-card>
              <v-card-title class="headline">
                삭제되었습니다.
              </v-card-title>
              <v-card-text>목록으로 되돌아갑니다.</v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red" text @click="goList">
                  YES
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <div v-if="modify == true">
            <cust-modify-card :usrId="usrId" @close="closeModify" />
          </div>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import InfoModReqDetailCard from "../../components/common/InfoModReq/InfoModReqDetailCard";
import CustModifyCard from "../../components/manager/CustModifyCard.vue";
import API from "../../api/API.js";
import YesNoDialog from "../../components/common/YesNoDialog.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    InfoModReqDetailCard,
    CustModifyCard,
    YesNoDialog,
    BreadCrumbs,
  },
  data() {
    return {
      reqId: null,
      curState: null,
      state: null,
      usrId: null,
      modify: false,
      dialog_delete: false,
      dialog_back: false,
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/manager/main",
        },
        {
          text: "정보 수정 요청 조회",
          disabled: false,
          href: "/manager/info/list",
        },
        {
          text: "정보 수정 요청 상세",
          disabled: true,
        },
      ],
    };
  },
  methods: {
    setReqId(value) {
      this.reqId = value;
      console.log(this.reqId);
    },
    setUsrId(value) {
      this.usrId = value;
      console.log(this.usrId);
    },
    setState(value) {
      this.state = value;
    },
    closeModify(value) {
      if (value == true) {
        this.curState = "완료";
      }
      this.modify = false;
    },
    infoModify() {
      this.curState = "접수";
      this.modify = true;
    },
    checkYn(value) {
      console.log("checkYn");
      this.dialog_delete = false;
      if (value == "Y") {
        this.infoDelete();
      }
    },
    infoDelete() {
      API.deleteMngInfoModReq(
        "reqId=" + this.reqId,
        () => {
          this.dialog_back = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
    goList() {
      this.dialog_back = false;
      this.$router.push("/manager/info/list");
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
