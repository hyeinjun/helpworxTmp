<template>
  <v-card >
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

    <v-row class="justify-center">
      <v-col cols="auto">
        <v-btn color="rgb(42, 46, 72)" dark class="mx-1" @click="checkValidation">저장</v-btn>
        <v-btn color="rgb(42, 46, 72)" dark class="mx-1" @click="goDetail">닫기</v-btn>
      </v-col>
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
import AlertDialog from "../common/AlertDialog.vue"

export default {
  components: {
    AlertDialog
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      coCd: this.$store.state.usrInfo.coCd,
      srId: 0,
      sysCd:0,
      sysNm: "",
      jobGb: "",
      jobGbId:0,
      reqGb: "",
      sbjct: "",
      state:"",
      regTim:"",
      sysItems: [],
      jobGbItems: [],
      reqGbItems: ["개선", "오류", "문의", "데이터수정"],
      insertFailModal: false,
    };
  },
  created() {
    this.srId = Number(this.$route.params.srId);
    // sr의 정보 가져오기
    API.getSrWithSrId(
      "srId=" + this.srId,
      (res) => {
        this.sysNm = res.sysNm;
        this.jobGb = res.jobGb;
        this.reqGb = res.reqGb;
        this.sbjct = res.sbjct;
        this.$refs.tuiEditor.invoke('setHtml',res.dscr);
        this.state = res.state;
        this.regTim = res.regTim;
      },
      (err) => {
        console.log(err);
      }
    );

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
        
        data.srId = this.srId;
        data.sysCd = this.sysCd;
        data.jobGbId = this.jobGbId;
        data.usrId = this.usrId;
        data.reqGb = this.reqGb;
        data.sbjct = this.sbjct;
        data.dscr = this.$refs.tuiEditor.invoke('getHtml');
        data.state = this.state;
        data.regTim = new Date();
        data.coCd = this.coCd;

        API.modifySr(
            data,
            (res) => {
                console.log(res);
                this.$router.push('/customer/sr/detail/'+this.srId);
            },
            (err) => {
                console.log(err);
            }
        );
      },
    goDetail() {
      this.$router.push('/customer/sr/detail/'+this.srId);
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
  },
};
</script>

<style></style>
