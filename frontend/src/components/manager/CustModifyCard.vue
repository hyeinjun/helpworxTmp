<template>
  <v-card class="pa-5">
    <v-row>
      <v-col cols="2">
        <v-subheader>고객아이디</v-subheader>
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
        <v-subheader>* 고객사</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-select
          v-model="coCd"
          dense
          :items="coList"
          menu-props="auto"
          outlined
        ></v-select>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>Password변경</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          outlined
          dense
          v-model="pwd"
          :type="showPwd ? 'text' : 'password'"
          :append-icon="showPwd ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="showPwd = !showPwd"
          :rules="[rules.pwdLength]"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-subheader>Password확인</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          outlined
          dense
          v-model="pwdChk"
          :type="showPwdChk ? 'text' : 'password'"
          :append-icon="showPwdChk ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="showPwdChk = !showPwdChk"
          :rules="[rules.pwdCheck]"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>* 고객이름</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          outlined
          dense
          v-model="nm"
          :rules="[rules.required]"
        ></v-text-field>
      </v-col>

      <v-col cols="2">
        <v-subheader>사번</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field outlined dense v-model="empno"></v-text-field> </v-col
    ></v-row>

    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>전화번호</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          outlined
          dense
          v-model="telNo"
          @keyup="getPhoneMask(telNo)"
        ></v-text-field>
      </v-col>

      <v-col cols="2">
        <v-subheader>* E-mail</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          outlined
          dense
          v-model="email"
          :rules="[rules.required, rules.email]"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7 pl-5">
      <v-col cols="3">
        <v-checkbox
          label="사용 여부"
          v-model="useYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox>
      </v-col>
    </v-row>
    <v-row class="mt-n7 pl-5"
      ><v-col cols="2">
        <v-checkbox
          label="고객"
          v-model="userYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox> </v-col
      ><v-col cols="2">
        <v-checkbox
          label="운영자"
          v-model="operatorYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox>
      </v-col>
      <v-col cols="2">
        <v-checkbox
          label="관리자"
          v-model="managerYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox
      ></v-col>
    </v-row>
    <v-row class="justify-center mt-5 mb-2"
      ><div class="ma-0">
        <v-col class="pa-1" cols="12" sm="1" xs="2">
          <v-btn color="rgb(42, 46, 72)" dark @click="ChangeYesNoDialog"
            >수정 완료</v-btn
          >
        </v-col>
      </div>
      <div class="ma-0">
        <v-col class="pa-1" cols="12" sm="1" xs="2">
          <v-btn color="rgb(42, 46, 72)" dark @click="goBack">닫기</v-btn>
        </v-col>
      </div>
    </v-row>
    <yes-no-dialog
      v-if="yesNoDialogModal"
      :dialogText="(dialogText = '수정 내용을 저장하시겠습니까?')"
      :dialogTitle="(dialogTitle = '수정확인')"
      @click="clickYn"
    />

    <alert-dialog
      v-if="insertFailModal"
      dialogTitle="수정에 실패하였습니다."
      dialogText="필수 항목을 확인해주세요."
      @close="closeInsertModal"
    ></alert-dialog>

  </v-card>
</template>

<script>
import API from "@/api/API";
import YesNoDialog from "../common/YesNoDialog.vue";
import AlertDialog from "../common/AlertDialog.vue";
import { mapState } from "vuex";

export default {
  props: {
    usrId: String,
  },
  components: {
    YesNoDialog,
    AlertDialog
  },
  data() {
    return {
      coCd: "",
      coNm: "",
      nm: "",
      pwd: "",
      pwdChk: "",
      useYn: false,
      userYn: false,
      operatorYn: false,
      managerYn: false,
      empno: "",
      email: "",
      telNo: "",
      modify: false,
      showPwd: false,
      showPwdChk: false,
      insertFailModal: false,
      yesNoDialogModal: false,
      rules: {
        required: (value) => !!value || "필수 입력 항목입니다.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return (
            pattern.test(value) ||
            "정확한 이메일 주소를 적어주세요. ex) kolon123@kolon.com"
          );
        },
        pwdCheck: (value) =>
          value === this.pwd || "패스워드가 일치하지 않습니다.",
        pwdLength: (value) =>
          (value != "" && value.length > 7 && value.length < 17) ||
          "패스워드는 8자 이상 16자 이하여야 합니다.",
      },
    };
  },
  created() {
    API.getCustomerDetail(
      "usrId=" + this.usrId,
      (res) => {
        this.coCd = res.coCd;
        this.coNm = res.coNm;
        this.nm = res.nm;
        this.useYn = res.useYn;
        this.userYn = res.userYn;
        this.operatorYn = res.operatorYn;
        this.managerYn = res.managerYn;
        this.empno = res.empno;
        this.email = res.email;
        this.telNo = res.telNo;
        console.log(this.coNm);
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    getPhoneMask(val) {
      let res = this.getMask(val);
      this.telNo = res;
    },

    getMask(phoneNumber) {
      if (!phoneNumber) return phoneNumber;
      phoneNumber = phoneNumber.replace(/[^0-9]/g, "");

      let res = "";
      if (phoneNumber.length < 3) {
        res = phoneNumber;
      } else {
        if (phoneNumber.substr(0, 2) == "02") {
          if (phoneNumber.length <= 5) {
            //02-123-5678
            res = phoneNumber.substr(0, 2) + "-" + phoneNumber.substr(2, 3);
          } else if (phoneNumber.length > 5 && phoneNumber.length <= 9) {
            //02-123-5678
            res =
              phoneNumber.substr(0, 2) +
              "-" +
              phoneNumber.substr(2, 3) +
              "-" +
              phoneNumber.substr(5);
          } else if (phoneNumber.length > 9) {
            //02-1234-5678
            res =
              phoneNumber.substr(0, 2) +
              "-" +
              phoneNumber.substr(2, 4) +
              "-" +
              phoneNumber.substr(6);
            if (phoneNumber.length > 10) {
              res =
                phoneNumber.substr(0, 2) +
                "-" +
                phoneNumber.substr(2, 4) +
                "-" +
                phoneNumber.substr(6);
              res = res.substr(0, 12);
            }
          }
        } else {
          if (phoneNumber.length < 8) {
            res = phoneNumber;
          } else if (phoneNumber.length == 8) {
            res = phoneNumber.substr(0, 4) + "-" + phoneNumber.substr(4);
          } else if (phoneNumber.length == 9) {
            res =
              phoneNumber.substr(0, 3) +
              "-" +
              phoneNumber.substr(3, 3) +
              "-" +
              phoneNumber.substr(6);
          } else if (phoneNumber.length == 10) {
            res =
              phoneNumber.substr(0, 3) +
              "-" +
              phoneNumber.substr(3, 3) +
              "-" +
              phoneNumber.substr(6);
          } else if (phoneNumber.length > 10) {
            //010-1234-5678
            res =
              phoneNumber.substr(0, 3) +
              "-" +
              phoneNumber.substr(3, 4) +
              "-" +
              phoneNumber.substr(7);
            if (phoneNumber.length > 11) {
              res =
                phoneNumber.substr(0, 3) +
                "-" +
                phoneNumber.substr(3, 4) +
                "-" +
                phoneNumber.substr(7);
              res = res.substr(0, 13);
            }
          }
        }
      }
      return res;
    },

    goBack() {
      console.log("trueee");
      this.$emit("close", this.modify);
    },
    openInsertModal() {
      this.insertFailModal = true;
    },
    closeInsertModal(value) {
      this.insertFailModal = false;
    },
    ChangeYesNoDialog() {
      this.yesNoDialogModal = true;
    },
    checkValidation() {
      var pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (
        this.coCd == "" ||
        this.usrId == "" ||
        this.nm == "" ||
        this.email == "" ||
        !pattern.test(this.email) ||
        this.pwd != this.pwdChk ||
        this.pwd.length > 16 ||
        (this.pwd.length < 8 && this.pwd != "")
      ) {
        this.openInsertModal();
      } else {
        this.updateCust();
      }
    },
    clickYn(value) {
      this.yesNoDialogModal = false;
      if (value == "Y") {
        this.checkValidation();
      }
    },
    updateCust() {
      let data = {};
      console.log(this.coCd);
      data.coCd = this.coCd;
      data.coNm = this.coList.find((e) => e.value == this.coCd).text;
      data.usrId = this.usrId;
      data.nm = this.nm;
      if (this.pwd.length > 7) {
        data.pwd = this.pwd;
      }
      data.useYn = this.useYn;
      data.userYn = this.userYn;
      data.operatorYn = this.operatorYn;
      data.managerYn = this.managerYn;
      data.empno = this.empno;
      data.email = this.email;
      data.telNo = this.telNo;

      API.modifyCustomer(
        data,
        (res) => {
          console.log(res);
          this.modify = true;
          this.goBack();
        },
        (err) => {
          console.log(err);
        }
      );
    },
  },
  computed: {
    ...mapState({
      coList: (state) => state.CommonData.companyList,
    }),
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
