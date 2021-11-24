<template>
  <v-card class="pa-5">
    <v-row class="justify-center">
      <v-col cols="2">
        <v-subheader>* 시스템</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10">
        <v-select outlined dense v-model="sysNm" :items="sysItems"></v-select>
      </v-col>
      <v-spacer></v-spacer>


      <v-col cols="2">
        <v-subheader>* 업무구분</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10">
        <v-select
          class="mr-5"
          outlined
          dense
          v-model="jobGb"
          :items="jobGbItems"
        ></v-select>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>* 요청구분</v-subheader>
      </v-col>
      <v-col cols="3" class="ml-n10">
        <v-select outlined dense 
        v-model="reqGb"
        :items="reqGbItems"
        ></v-select>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>* 제목</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5">
        <v-text-field outlined dense v-model="sbjct"></v-text-field>
      </v-col>
    </v-row>
    <v-row class="mt-n7">
      <v-col cols="2">
        <v-subheader>* 내용</v-subheader>
      </v-col>
      <v-col class="ml-n10 mr-5">
        <editor
          align="left"
          height="400px"
          initialEditType="wysiwyg"
          previewStyle="vertical"
          ref="tuiEditor"
          />
      </v-col>
    </v-row>

    <v-row class="justify-center mt-7 mb-2">
      <div class="mr-0">
        <v-col cols="12" sm="1" xs="2" class="pa-1">
        <v-btn color="rgb(42, 46, 72)" dark @click="checkValidation">등록</v-btn>
      </v-col>
      </div>
      <div class="mr-0">
      <v-col cols="12" sm="1" xs="2" class="pa-1">
        <v-btn color="rgb(42, 46, 72)" dark @click="goBack">닫기</v-btn>
      </v-col>
      </div>
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
export default {
  components: {
    AlertDialog,
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      coCd: this.$store.state.usrInfo.coCd,
      sysCd:0,
      sysNm: "",
      jobGb: "",
      jobGbId:0,
      reqGb: "",
      sbjct: "",
      dscr: "",
      sysItems: [],
      jobGbItems: [],
      reqGbItems: ["개선", "오류", "문의", "데이터수정"],
      insertFailModal: false,
    };
  },
  created() {
    // 회사의 시스템 코드 가져오기
    API.getSysNmByCoCd(
        "coCd="+this.coCd,
        (res) => {
            this.sysItems=[];
            for (let index = 0; index < res.length; index++) {
                this.sysItems.push(res[index].text);
            }
            this.updateJobGb();
        },
        (err) => {
            console.log(err);
        }
    );
  },
  watch: {
      sysNm(){
        if(this.sysNm=="PeopleWorx") this.sysCd=1;
        else if(this.sysNm=="SalesWorx") this.sysCd=2;
        else if(this.sysNm=="r-PLANNA") this.sysCd=3;
        else this.sysCd=4;
        this.jobGb="";
        this.updateJobGb();
      },
      jobGb(id) {
          console.log(id);
          for (let idx = 0; idx < this.jobGbItems.length; idx++) {
              if(this.jobGbItems[idx].text == id)  
                this.jobGbId = this.jobGbItems[idx].id;
          }
      }
  },
  methods: {
      saveSr() {
        let data = {};
        
        data.usrId = this.usrId;
        data.jobGbId = this.jobGbId;
        data.sysCd = this.sysCd;
        data.reqGb = this.reqGb;
        data.sbjct = this.sbjct;
        data.dscr = this.$refs.tuiEditor.invoke('getHtml');
        data.state = "대기";
        data.regTim = new Date();
        data.coCd = this.coCd;

        API.saveSr(
            data,
            (res) => {
                console.log(res);
                this.$router.push("/customer/sr/detail/"+res);
            },
            (err) => {
                console.log(err);
            }
        );
      },
    updateJobGb(){
        API.getJobGbBySysCd(
            "sysCd="+this.sysCd,
            (res) => {
                this.jobGbItems=[];
                for (let index = 0; index < res.length; index++) {
                    let arr = {};
                    arr.id = res[index].jobGbId;
                    arr.text = res[index].job;
                    this.jobGbItems.push(arr);
                    if(this.jobGb == res[index].job) this.jobGbId = res[index].jobGbId;
                }
            },
            (err) => {
                console.log(err);
            }
        );
      },
      checkValidation() {
        if (
          this.sysCd == "" ||
          this.jobGb == "" ||
          this.reqGb == "" ||
          this.sbjct == "" ||
          this.$refs.tuiEditor.invoke('getHtml') == ""
        ) {
          this.openInsertModal();
        } else {
          this.saveSr();
        }
      },
      openInsertModal() {
        this.insertFailModal = true;
      },
      closeInsertModal(value) {
        this.insertFailModal = false;
      },
      goBack(){
          this.$router.push("/customer/sr/list");
      }
  },
};
</script>

<style></style>
