<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-subheader>요청자</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="usrId"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>* 요청 제목</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field outlined dense v-model="reqSbjct"></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>요청 내용</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <editor
          height="400px"
          initialEditType="wysiwyg"
          previewStyle="vertical"
          dense
          ref="tuiEditor"
        />
      </v-col>
    </v-row>
    <v-row class="justify-center mt-7 mb-2">
      <div class="mr-0">
      <v-col cols="12" sm="1" xs="2" class="pa-1">
        <v-btn color="rgb(42, 46, 72)" dark @click="checkValidation"
          >등록</v-btn
        >
      </v-col></div><div class="mr-0">
      <v-col cols="12" sm="1" xs="2" class="pa-1">
        <v-btn color="rgb(42, 46, 72)" dark @click="goList">닫기</v-btn>
      </v-col></div>
    </v-row>

    <alert-dialog
      v-if="insertFailModal"
      dialogTitle="등록에 실패하였습니다."
      dialogText="필수 항목을 모두 채워주세요."
      @close="closeInsertModal"
    ></alert-dialog>
    
  </div>
</template>

<script>
import API from "@/api/API";
import AlertDialog from "../common/AlertDialog.vue";

export default {
  components: {
    AlertDialog,
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      reqSbjct: "",
      insertFailModal: false,
    };
  },
  created() {},
  methods: {
    goList() {
      this.$router.push("/customer/info/list");
    },
    openInsertModal() {
      this.insertFailModal = true;
    },
    closeInsertModal(value) {
      this.insertFailModal = false;
    },
    checkValidation() {
      if (this.reqSbjct == "") {
        this.openInsertModal();
      } else {
        this.insertInfoModReq();
      }
    },
    insertInfoModReq() {
      let data = {};

      data.reqSbjct = this.reqSbjct;
      data.reqDscr = this.$refs.tuiEditor.invoke("getHtml");
      data.usrId = this.usrId;

      API.insertInfoModReq(
        data,
        (res) => {
          console.log(res);
          this.$router.push("/customer/info/list");
        },
        (err) => {
          console.log(err);
        }
      );
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
  border-color: #eeeeee;
}
</style>
