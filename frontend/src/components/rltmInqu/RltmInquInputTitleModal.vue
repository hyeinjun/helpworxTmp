<template>
  <v-card height="600" class="justify-center">
    <v-toolbar color="rgb(42, 46, 72)" dark>
      <v-toolbar-title>HelpWorX</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn large @click="goBack" icon>
        <v-icon dark>
          mdi-arrow-left-circle
        </v-icon>
      </v-btn>
      <v-btn large @click="closeModal" icon>
        <v-icon dark>
          mdi-close-circle
        </v-icon>
      </v-btn>
    </v-toolbar>
    <v-main class="ma-3">
      <div>
        <v-card-title class="justify-center"
          >문의 내용을 간략히 입력해주세요!</v-card-title
        >
      </div>

      <v-text-field
        outlined
        dense
        v-model="sbjct"
        ref="sbjct"
        :rules="[() => !!sbjct || '제목을 입력해주세요!']"
        required
      ></v-text-field>
      <v-btn @click="inputTitle">확인</v-btn>
    </v-main>
  </v-card>
</template>

<script>
import API from "@/api/API";
import { mdiCloseCircle } from "@mdi/js";
import { mdiArrowLeftCircle } from "@mdi/js";

export default {
  data() {
    return {
      mdiCloseCircle,
      mdiArrowLeftCircle,
      dialog_start: true,
      newInquId: 0,
      coCd: this.$store.state.usrInfo.coCd,
      usrId: this.$store.state.usrInfo.usrId,
      usrNm: this.$store.state.usrInfo.usrNm, // vuex에 저장된 usrNm을 사용
      sbjct:"",
    };
  },
  props: ["sysCd","dialogFlg"],
  created() {
  },
  methods: {
    closeModal() {
      this.dialog_title = false;
      this.$emit("changeDialogTitle", this.dialog_title, false, null);
    },
    goBack() {
      this.dialog_title = false;
      this.$emit("changeDialogTitle", this.dialog_title, true, null);
    },
    inputTitle() {
      // 제목 입력 안하면 안넘어감
      if(this.sbjct==""){
        this.$refs["sbjct"].validate(true);
        return;
      }

      // 데이터베이스에 문의 시스템, 제목을 가지고 새로 생성
      let data = {};
      data.sysCd = this.$props.sysCd;
      data.coCd = this.coCd;
      data.usrId = this.usrId;
      data.state = "진행중";
      data.beginTim = new Date();
      data.sbjct = this.sbjct;

      API.createInquWithSysCd(
        data,
        (res) => {
          this.newInquId = res;
          this.dialog_title = false;
          this.$emit(
            "changeDialogTitle",
            this.dialog_title,
            false,
            this.newInquId
          );
        },
        (err) => {
          console.log(err);
        }
      );
    },
  },
  watch: {
    dialogFlg() {
      this.sbjct = "";
    }
  },
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
