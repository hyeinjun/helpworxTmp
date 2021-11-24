<template>
  <v-card class="pa-5">
    <v-row>
      <v-col cols="2">
        <v-subheader>고객사 코드</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="coCd"></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>* 고객사명</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field outlined dense v-model="coNm" :rules="[rules.required]"></v-text-field>
      </v-col>
    </v-row>

    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>고객사 설명</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5" align="left">
        <editor
          v-if="coCd != ''"
          height="400px"
          initialEditType="wysiwyg"
          previewStyle="vertical"
          ref="tuiEditor"
          scrollTop=false
        />
      </v-col>
    </v-row>
    <v-row class="mt-10 justify-center">
      <v-col cols="2">
        <v-subheader>대표자</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
       <v-text-field outlined dense v-model="remnNm"></v-text-field>
      </v-col>
      <v-spacer></v-spacer>
       <v-col cols="2" class="mr-n12">
        <v-subheader>사업자 번호</v-subheader>
      </v-col>
      <v-col cols="12" sm="6" md="3" class="mr-5">
        <v-text-field outlined dense v-model="bizmanNo"></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7 justify-center">
      <v-col cols="2">
        <v-subheader>회사 주소</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10" align="left">
        <v-text-field outlined dense v-model="coAddr"></v-text-field>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="2" class="mr-n12">
        <v-subheader>설립일</v-subheader>
      </v-col>
     <v-col cols="12" sm="6" md="3" class="mr-5">
       <date-picker
          :propsDateInfo="fondDt"
          @change="setFondDt"
        />
      </v-col>
    </v-row>
    <v-row class="mt-n7 justify-center">
      <v-col cols="2" class="mr-n12">
        <v-subheader>* 계약 시작일</v-subheader>
      </v-col>
      <v-col cols="12" sm="6" md="3" class="ml-2">
        <date-picker
          :propsDateInfo="contBeginDt"
          :maxTime="contEndDt"
          @change="setBeginDt"
        />
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="2" class="mr-n12">
        <v-subheader>* 계약 종료일</v-subheader>
      </v-col>
      <v-col cols="12" sm="6" md="3" class="mr-5">
        <date-picker
          :propsDateInfo="contEndDt"
          :minTime="contBeginDt"
          @change="setEndDt"
        />
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
      ><v-col cols="3">
        <v-checkbox
          label="People WorX"
          v-model="pwxUseYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox> </v-col
      ><v-col cols="3">
        <v-checkbox
          label="Sales WorX"
          v-model="swxUseYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox>
      </v-col> </v-row
    ><v-row class="mt-n7 pl-5"
      ><v-col cols="3">
        <v-checkbox
          label="r-PLANNA"
          v-model="rplUseYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox
      ></v-col>
      <v-col cols="3">
        <v-checkbox
          label="r-CoCoAna"
          v-model="rcoUseYn"
          color="rgb(42, 46, 72)"
        ></v-checkbox
      ></v-col>
    </v-row>

    <v-row class="justify-center mt-5 mb-2">
      <div class="ma-0">
      <v-col class="pa-1" cols="12" sm="1" xs="2">
        <v-btn color="rgb(42, 46, 72)" dark @click="checkValidation"
          >수정 완료</v-btn
        >
      </v-col></div><div class="ma-0">
      <v-col cols="12" sm="1" xs="2" class="pa-1" >
        <v-btn color="rgb(42, 46, 72)" dark @click="goCoDetail">닫기</v-btn>
      </v-col></div>
    </v-row>

    <alert-dialog
      v-if="insertFailModal"
      dialogTitle="등록에 실패하였습니다."
      dialogText="필수 항목을 모두 채워주세요."
      @close="closeInsertModal"
    ></alert-dialog>

  </v-card>
</template>

<script>
import API from "@/api/API";
import AlertDialog from "../common/AlertDialog.vue";
import DatePicker from "../common/DatePicker.vue";

export default {
  components: {
    AlertDialog,
    DatePicker,
  },
  data() {
    return {
      coCd: "",
      coNm: "",
      contBeginDt: "",
      contEndDt: "",
      useYn: false,
      pwxUseYn: false,
      swxUseYn: false,
      rplUseYn: false,
      rcoUseYn: false,
      remnNm: null,
      bizmanNo: null,
      coAddr: null,
      fondDt: null,
      insertFailModal: false,
      rules: {
        required: (value) => !!value || "필수 입력 항목입니다.",
      },
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
        this.$refs.tuiEditor.invoke("setHtml", res.coDscr);
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
    goCoDetail() {
      this.$router.push("/manager/co/detail/" + this.coCd);
    },
    openInsertModal() {
      this.insertFailModal = true;
    },
    closeInsertModal(value) {
      this.insertFailModal = false;
    },
    checkValidation() {
      if (
        this.coCd == "" ||
        this.coNm == "" ||
        this.contBeginDt == "" ||
        this.contEndDt == ""
      ) {
        this.openInsertModal();
      } else {
        this.updateCo();
      }
    },
    setBeginDt(value){
      this.contBeginDt=value;
    },
    setEndDt(value){
      this.contEndDt=value;
    },
    setFondDt(value){
      this.fondDt=value;
    },
    updateCo() {
      let data = {};

      data.coCd = this.coCd;
      data.coNm = this.coNm;
      data.coDscr = this.$refs.tuiEditor.invoke("getHtml");
      data.contBeginDt = this.contBeginDt;
      data.contEndDt = this.contEndDt;
      data.useYn = this.useYn;
      data.pwxUseYn = this.pwxUseYn;
      data.swxUseYn = this.swxUseYn;
      data.rplUseYn = this.rplUseYn;
      data.rcoUseYn = this.rcoUseYn;
      data.remnNm=this.remnNm;
      data.bizmanNo=this.bizmanNo;
      data.coAddr=this.coAddr;
      data.fondDt=this.fondDt;

      API.modifyCo(
        data,
        (res) => {
          this.$router.push("/manager/co/detail/" + this.coCd);
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
  border-color: #EEEEEE;
}
</style>
