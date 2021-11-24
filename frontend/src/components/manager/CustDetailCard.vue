<template>
  <v-card class="pa-5">
    <v-row>
      <v-col cols="2">
        <v-subheader>고객사</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="coNm"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>고객아이디</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left"
        ><v-text-field
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
        <v-subheader>고객이름</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left"
        ><v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="nm"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-subheader>사번</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="empno"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>전화번호</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="telNo"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-subheader>E-mail</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="email"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n5 pl-5">
      <v-col cols="3">
        <v-checkbox
          label="사용 여부"
          v-model="useYn"
          readonly
          color="grey"
        ></v-checkbox>
      </v-col>
    </v-row>
    <v-row class="mt-n7 pl-5"
      ><v-col cols="2">
        <v-checkbox
          label="고객"
          v-model="userYn"
          readonly
          color="grey"
        ></v-checkbox> </v-col
      ><v-col cols="2">
        <v-checkbox
          label="운영자"
          v-model="operatorYn"
          readonly
          color="grey"
        ></v-checkbox>
      </v-col>
      <v-col cols="2">
        <v-checkbox
          label="관리자"
          v-model="managerYn"
          readonly
          color="grey"
        ></v-checkbox></v-col
    ></v-row>
    <v-row class="justify-center mt-5 mb-2">
      <v-col cols="auto" class="pa-1">
        <v-btn color="rgba(42, 46, 72)" dark @click="goCustModify">수정</v-btn>
      </v-col>

      <v-col cols="auto" class="pa-1">
        <v-btn color="rgb(42, 46, 72)" dark v-if="useYn==true" @click.stop="dialog_delete = true"
          >휴면</v-btn
        >
      </v-col>

      <v-col cols="auto" class="pa-1">
        <v-btn color="rgb(42, 46, 72)" dark @click="goBack">닫기</v-btn>
      </v-col>
    </v-row>
    <yes-no-dialog
      v-if="dialog_delete"
      :dialogText="(dialogText = '해당 회원을 휴면회원으로 전환합니다.')"
      :dialogTitle="(dialogTitle = '정말로 전환하시겠습니까?')"
      @click="checkYn"
    />
    <v-dialog v-model="dialog_back" width="500" class="text-center">
      <v-card>
        <v-card-title class="headline">
          전환되었습니다.
        </v-card-title>
        <v-card-text>이전 페이지로 되돌아갑니다.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red" text @click="goBack">
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
import API from "@/api/API";
import YesNoDialog from "../common/YesNoDialog.vue";

export default {
  components: {
    YesNoDialog,
  },
  data() {
    return {
      coNm: "",
      usrId: "",
      nm: "",
      empno: "",
      email: "",
      telNo: "",
      useYn: false,
      userYn: false,
      operatorYn: false,
      managerYn: false,
      dialog_delete: false,
      dialog_back: false,
    };
  },
  created() {
    this.usrId = this.$route.params.usrId;
    // 회사 정보 가져오기
    API.getCustomerDetail(
      "usrId=" + this.usrId,
      (res) => {
        this.usrId = res.usrId;
        this.coNm = res.coNm;
        this.nm = res.nm;
        this.empno = res.empno;
        this.email = res.email;
        this.telNo = res.telNo;
        this.useYn = res.useYn;
        this.userYn = res.userYn;
        this.operatorYn = res.operatorYn;
        this.managerYn = res.managerYn;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    deleteCust() {
      API.deleteCustomer(
        "usrId=" + this.usrId,
        (res) => {
          console.log(res);
          this.dialog_back = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
    checkYn(value) {
      this.dialog_delete = false;
      if (value == "Y") {
        this.deleteCust();
      }
    },
    goBack() {
      this.dialog_back = false;
      // user list 페이지로 이동
      this.$router.push("/manager/cust/list");
    },
    goCustModify() {
      this.$router.push("/manager/cust/modify/" + this.usrId);
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
