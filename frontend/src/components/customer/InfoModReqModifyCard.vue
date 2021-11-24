<template>
  <div>
    <v-row>
      <v-col cols="2">
        <v-subheader>요청 번호</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="reqId"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
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
      <v-spacer></v-spacer>
      <v-col cols="2">
        <v-subheader>진행상황</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10 mr-6" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="state"
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
          ref="tuiEditor"
          v-if="reqSbjct != null"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        <v-subheader>요청일</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="reqTim"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="justify-center mt-5 mb-2">
      <div class="mr-0">
        <v-col cols="12" sm="1" xs="2" class="pa-1">
          <v-btn color="rgb(42, 46, 72)" dark @click="checkValidation"
            >등록</v-btn
          >
        </v-col>
      </div>
      <div class="mr-0">
        <v-col cols="12" sm="1" xs="2" class="pa-1">
          <v-btn color="rgb(42, 46, 72)" dark @click="goList">닫기</v-btn>
        </v-col>
      </div>
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
      reqId: this.$route.params.reqId,
      usrId: "",
      reqSbjct: "",
      reqDscr: "",
      reqTim: "",
      state: "",
      insertFailModal: false,
    };
  },
  created() {
    API.getInfoModReq(
      "reqId=" + this.reqId,
      (res) => {
        this.usrId = res.usrId;
        this.reqSbjct = res.reqSbjct;
        this.$refs.tuiEditor.invoke("setHtml", res.reqDscr);
        this.reqTim = res.reqTim;
        this.state = res.state;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    goList() {
      this.$router.push("/customer/info/detail/" + this.reqId);
    },
    openInsertModal() {
      this.insertFailModal = true;
    },
    closeInsertModal(value) {
      this.insertFailModal = false;
    },
    checkValidation() {
      if (this.reqSbjct == "" || this.resDscr == "") {
        this.openInsertModal();
      } else {
        this.insertInfoModReq();
      }
    },
    insertInfoModReq() {
      let data = {};

      data.reqSbjct = this.reqSbjct;
      data.reqDscr = this.$refs.tuiEditor.invoke("getHtml");
      data.reqId = this.reqId;

      API.modifyInfoModReq(
        data,
        (res) => {
          console.log(res);
          this.$router.push("/customer/info/detail/" + this.reqId);
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
