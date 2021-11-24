<template>
  <v-card class="mt-5" v-if="hasAuth || isCompleted">
    <v-row justify="space-between" align="center">
      <v-col cols="auto">
        <v-card-title>SR 접수</v-card-title>
      </v-col>
      <v-col v-if="hasAuth && isCompleted" cols="auto" class="mr-2">
        <v-btn
          v-if="!isCmplTreat && !modifiable"
          class="mr-1" color="rgb(42, 46, 72)" dark
          @click="updateMode(true)">
          수정
        </v-btn>
        <v-btn
          v-if="modifiable"
          class="mr-1" color="rgb(42, 46, 72)" dark
          @click="saveRcpt">
          저장
        </v-btn>
        <v-btn
          v-if="modifiable"
          class="mr-1" color="rgb(42, 46, 72)" dark
          @click="dialog_delete = true">
          접수 취소
        </v-btn>
        <v-btn v-if="modifiable" class="mr-1" color="rgb(42, 46, 72)" dark @click="updateMode(false)">취소</v-btn>
      </v-col>
      <v-col v-else-if="hasAuth" cols="auto" class="mr-2">
        <v-btn
          class="mr-1" color="rgb(42, 46, 72)" dark
          @click="saveRcpt">
          저장
        </v-btn>
      </v-col>
    </v-row>

    <!-- 접수 취소 시 나오는 모달 -->
    <yes-no-dialog
      v-if="dialog_delete"
      dialogTitle="정말로 취소하시겠습니까?"
      dialogText="SR 접수 내역이 삭제됩니다."
      @click="clickDialog"
    ></yes-no-dialog>

    <v-container fluid>
      <v-row class="mt-n7">
        <v-col cols="1">
          <v-subheader class="pb-2 mr-2" style="white-space:nowrap;">접수자</v-subheader>
        </v-col>
        <v-col cols="2" class="ml-n10" align="left">
          <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="receiverId"></v-text-field>
        </v-col>
        <v-col cols="1">
          <v-subheader v-if="modifiable" class="pb-2 mr-2 ml-n5 pl-1" style="font-size:1.05em; white-space:nowrap;">*완료예정일</v-subheader>
          <v-subheader v-else class="pb-2 mr-2 ml-n4 pl-1" style="font-size:1.05em; white-space:nowrap;">완료예정일</v-subheader>
        </v-col>
        <v-col cols="2" class="ml-n10">
          <date-picker
            :propsDateInfo="cmplPrdt"
            :readOnly="!modifiable"
            @change="changeDate"
          ></date-picker>
        </v-col>
        <v-spacer></v-spacer>
      </v-row>


      <v-row v-if="modifiable" class="mt-n7">
        <v-col cols="1">
          <v-subheader class="pb-2">*내용</v-subheader>
        </v-col>
        <v-col class="ml-n10">
          <div class="mr-1">
            <editor
              align="left"
              height="300px"
              initialEditType="wysiwyg"
              previewStyle="vertical"
              ref="tuiEditor"
              :initialValue="rcptDscr"
            />
          </div>
        </v-col>
      </v-row>
      <v-row v-else class="mt-n7">
        <v-col cols="1">
          <v-subheader class="pb-2">내용</v-subheader>
        </v-col>
        <v-col class="ml-n7" align="left">
          <div class="ml-n3 mr-1 px-3 textViewer">
              <viewer
                  v-if="rcptDscr!=null"
                  :initialValue="rcptDscr"
                  height="300px"
              />
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import API from "@/api/API";
import DatePicker from '../common/DatePicker.vue'
import YesNoDialog from '../common/YesNoDialog.vue'

export default {
  components: {
    DatePicker,
    YesNoDialog
  },
  data() {
    return {
      usrId: this.$store.state.usrInfo.usrId,
      coCd: this.$store.state.usrInfo.coCd,
      srId: 0,
      rcptDscr: null,
      receiverId: "",
      cmplPrdt: "",
      dialog_delete: false,
      modal: "",
      isChanged: false,
      isCompleted: false,
      modifiable: false,
    };
  },
  props: ["sysNm", "jobGb", "reqGb", "isCmplTreat", "isCmplRcpt", "hasAuth"],
  created() {
    this.srId = Number(this.$route.params.srId);
    this.isCompleted = this.isCmplRcpt;
    if(!this.isCompleted) {
      this.modifiable = true;
    }
    if(!this.isCmplTreat) {
      this.receiverId = this.$store.state.usrInfo.usrId;
    }
    // 접수 정보 가져오기
    this.getRcpt();
  },
  watch: {
    cmplPrdt() {
      this.isChanged = true;
    },
    hasAuth(value) {
      this.hasAuth = value;
    }
  },
  methods: {
    changeDate(value) {
      this.cmplPrdt = value;
    },
    getRcpt() {
      API.getRcptBySrId(
        "srId=" + this.srId,
        (res) => {
          if (res) {
            var t = new Date(res.cmplPrdt);
            var year = t.getFullYear();
            var month = t.getMonth() + 1;
            if (month < 10) month = "0" + month;
            var date = t.getDate();
            if (date < 10) date = "0" + date;
            this.rcptDscr = res.rcptDscr
            console.log(this.rcptDscr);
            // this.$refs.tuiEditor.invoke("setHtml", res.rcptDscr);
            this.receiverId = res.recivrId; 
            this.cmplPrdt = year + "-" + month + "-" + date;
            this.isCompleted = true;
          }else {
            this.isCompleted = false;
          }
        },
        (err) => {
          console.log(err);
        }
      );
    },
    deleteRcpt() {
      this.dialog_delete = false;
      API.deleteRcpt(
        "srId=" + this.srId,
        (res) => {
          console.log(res);
          this.getRcpt();
        },
        (err) => {
          console.log(err);
        }
      );
      this.$router.go(0);
    },
    saveRcpt() {
      if(this.checkInput()) {
        let data = {};
        data.srId = this.srId;
        data.recivrId = this.usrId;
        data.rcptDscr = this.$refs.tuiEditor.invoke('getHtml');
        data.cmplPrdt = this.cmplPrdt;
        data.treatBeginTim = new Date();
        API.saveSrRcpt(
          data,
          (res) => {
            console.log(res);
            this.isCompleted = true;
            this.$router.go(0);
          },
          (err) => {
            console.log(err);
          }
        );
      }else {
        alert('필수 항목을 모두 채워주세요.')
      }
      
    },
    checkInput() {
      let dscr = this.$refs.tuiEditor.invoke('getHtml');
      if(dscr == '' || this.cmplPrdt == '') {
          return false;
      }
      return true;
    },
    clickDialog(value) {
      if(value === 'Y') {
          this.deleteRcpt();
      }else {
          this.dialog_delete = false;
      }
    },
    updateMode(value) {
      this.modifiable = value;
    },
  },
};
</script>

<style scoped>
.v-subheader {
  font-weight: bold;
  font-size: 1.07em;
}
.btnCol {
  align-self: center;
  text-align: left;
}
</style>
