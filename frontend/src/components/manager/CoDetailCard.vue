<template>
  <v-card class="pa-5">
    <v-row>
      <v-col cols="2">
        <v-subheader style="align-content: center;">고객사 코드</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="coCd"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>고객사명</v-subheader>
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

    <v-row class="mt-n7" >
      <v-col cols="2">
        <v-subheader>고객사 설명</v-subheader>
      </v-col>
      <v-col class="ml-n7 mr-5" align="left"
        >
        <div class="ml-n3 mr-1 px-3 textViewer" style="border: 3px solid #EEEEEE;">
          <!-- initialValue는 최초 1회만 가져오기 때문에 v-if로 값이 들어왔는지 확인해야한다. -->
          <!-- 회사 설명이 비어있을 수 있으므로 다른 키로 값이 들어왔는지 확인한다. -->
          <viewer v-if="coNm != null" :initialValue="coDscr" height="400px" />
        </div>
      </v-col>
    </v-row>

    <v-row class="mt-3 justify-center">
      <v-col cols="2">
        <v-subheader>대표자</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="remnNm"
        ></v-text-field>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="2">
        <v-subheader>사업자 번호</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10 mr-6" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="bizmanNo"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7 justify-center">
      <v-col cols="2">
        <v-subheader>회사 주소</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="coAddr"
        ></v-text-field>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="2">
        <v-subheader>설립일</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10 mr-6" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="fondDt"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row class="mt-n7 justify-center">
      <v-col cols="2">
        <v-subheader>계약 시작일</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="contBeginDt"
        ></v-text-field>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="2">
        <v-subheader>계약 종료일</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10 mr-6" align="left">
        <v-text-field
          background-color="#EEEEEE"
          solo
          flat
          dense
          filled
          readonly
          :value="contEndDt"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row class="mt-n7 pl-5">
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
      ><v-col cols="3">
        <v-checkbox
          label="People WorX"
          v-model="pwxUseYn"
          readonly
          color="grey"
        ></v-checkbox> </v-col
      ><v-col cols="3">
        <v-checkbox
          label="Sales WorX"
          v-model="swxUseYn"
          readonly
          color="grey"
        ></v-checkbox>
      </v-col> </v-row
    ><v-row class="mt-n7 pl-5"
      ><v-col cols="3">
        <v-checkbox
          label="r-PLANNA"
          v-model="rplUseYn"
          readonly
          color="grey"
        ></v-checkbox
      ></v-col>
      <v-col cols="3">
        <v-checkbox
          label="r-CoCoAna"
          v-model="rcoUseYn"
          readonly
          color="grey"
        ></v-checkbox
      ></v-col>
    </v-row>
    <v-row class="justify-center mt-5 mb-2">
      <div class="ma-0">
        <v-col cols="12" sm="1" xs="2" class="pa-1">
          <v-btn color="rgba(42, 46, 72)" dark @click="goCoModify">수정</v-btn>
        </v-col>
      </div>
      <div class="ma-0">
        <v-col cols="12" sm="1" xs="2" v-if="useYn == true" class="pa-1">
          <v-btn color="rgb(42, 46, 72)" dark @click="dialogDelete"
            >휴면전환</v-btn
          >
        </v-col>
      </div>
      <div class="ma-0">
        <v-col cols="12" sm="1" xs="2" class="pa-1">
          <v-btn color="rgb(42, 46, 72)" dark @click="goBack">닫기</v-btn>
        </v-col>
      </div>
    </v-row>
    <yes-no-dialog
      v-if="dialog_delete"
      :dialogText="(dialogText = '해당 회사를 휴면회사로 전환합니다..')"
      :dialogTitle="(dialogTitle = '정말로 전환하시겠습니까?')"
      @click="checkYn"
    />

    <v-dialog v-model="dialog_back" width="500" class="text-center">
      <v-card>
        <v-card-title class="headline">
          전환되었습니다.
        </v-card-title>
        <v-card-text> 이전 페이지로 돌아갑니다.</v-card-text>
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
      coCd: null,
      coNm: null,
      coDscr: null,
      contBeginDt: null,
      contEndDt: null,
      useYn: false,
      pwxUseYn: false,
      swxUseYn: false,
      rplUseYn: false,
      rcoUseYn: false,
      remnNm: null,
      bizmanNo: null,
      coAddr: null,
      fondDt: null,
      dialog_delete: false,
      dialog_back: false,
    };
  },
  created() {
    this.coCd = this.$route.params.coCd;
    // 회사 정보 가져오기
    API.getCoDetail(
      "coCd=" + this.coCd,
      (res) => {
        this.coCd = res.coCd;
        this.coNm = res.coNm;
        this.coDscr = res.coDscr;
        console.log(this.coDscr);
        this.contBeginDt = res.contBeginDt.substr(0, 10);
        this.contEndDt = res.contEndDt.substr(0, 10);
        this.useYn = res.useYn;
        this.pwxUseYn = res.pwxUseYn;
        this.swxUseYn = res.swxUseYn;
        this.rplUseYn = res.rplUseYn;
        this.rcoUseYn = res.rcoUseYn;
        this.remnNm = res.remnNm;
        this.bizmanNo = res.bizmanNo;
        this.coAddr = res.coAddr;
        this.fondDt = res.fondDt;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    deleteCo() {
      API.deleteCo(
        "coCd=" + this.coCd,
        (res) => {
          console.log(res);
          this.dialog_back = true;
        },
        (err) => {
          console.log(err);
        }
      );
    },
    dialogDelete() {
      this.dialog_delete = true;
    },
    checkYn(value) {
      this.dialog_delete = false;
      if (value == "Y") {
        this.deleteCo();
      }
    },
    goBack() {
      this.dialog_back = false;
      // co list 페이지로 이동
      this.$router.push("/manager/co/list");
    },
    goCoModify() {
      this.$router.push("/manager/co/modify/" + this.coCd);
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
