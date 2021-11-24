<template>
  <div>
      <v-row class="justify-center">
        <v-col cols="12" md="3">
            <v-card elevation="2" shaped id="regProgress">
                <v-icon v-if="!returnState" class="progressIcon" color="green" large>mdi-check-circle-outline</v-icon>
                <v-card-subtitle class="justify-center">요청 등록</v-card-subtitle>
                <v-card-text v-if="regId">등록자: {{regId}}</v-card-text> <v-card-text v-if="!regId">.</v-card-text>
                <v-card-text v-if="regState">상태: {{regState}}</v-card-text> <v-card-text v-if="!regState">.</v-card-text>
                <v-card-text v-if="regTim">등록일: {{regTim}}</v-card-text> <v-card-text v-if="!regTim">.</v-card-text>
            </v-card>
        </v-col>
        <v-col cols="12" md="3">
            <v-card elevation="2" shaped id="rcptProgress">
                <v-icon v-if="rcptState=='접수 완료'" class="progressIcon" color="green" large>mdi-check-circle-outline</v-icon>
                <v-icon v-if="returnState" class="progressIcon" color="red" large>mdi-cancel</v-icon>
                <v-card-subtitle class="justify-center">요청 접수</v-card-subtitle>
                <v-card-text v-if="recivrId">접수자: {{recivrId}}</v-card-text> <v-card-text v-if="!recivrId">.</v-card-text>
                <v-card-text v-if="rcptState">상태: {{rcptState}}</v-card-text> <v-card-text v-if="!rcptState">.</v-card-text>
                <v-card-text v-if="rcptDate">접수일: {{rcptDate}}</v-card-text> <v-card-text v-if="!rcptDate">.</v-card-text>
            </v-card>
        </v-col>
        <v-col cols="12" md="3">
            <v-card elevation="2" shaped id="doProgress">
                <v-icon v-if="doState=='진행 완료'" class="progressIcon" color="green" large>mdi-check-circle-outline</v-icon>
                <v-icon v-if="doState=='진행 중'" class="progressIcon" color="#02355A" large>fas fa-circle-notch fa-spin</v-icon>
                <v-card-subtitle class="justify-center">요청 진행</v-card-subtitle>
                <v-card-text v-if="doId">진행자: {{doId}}</v-card-text> <v-card-text v-if="!doId">.</v-card-text>
                <v-card-text v-if="doState">상태: {{doState}}</v-card-text> <v-card-text v-if="!doState">.</v-card-text>
                <v-card-text v-if="doTim">진행시간: {{doTim}}</v-card-text> <v-card-text v-if="!doTim">.</v-card-text>
            </v-card>
        </v-col>
        <v-col cols="12" md="3">
            <v-card elevation="2" shaped id="treatProgress">
                <v-icon v-if="treatState=='처리 완료'" class="progressIcon" color="green" large>mdi-check-circle-outline</v-icon>
                <v-card-subtitle class="justify-center">요청 처리</v-card-subtitle>
                <v-card-text v-if="opetrId">처리자: {{opetrId}}</v-card-text> <v-card-text v-if="!opetrId">.</v-card-text>
                <v-card-text v-if="treatState">상태: {{treatState}}</v-card-text> <v-card-text v-if="!treatState">.</v-card-text>
                <v-card-text v-if="treatTim">처리일: {{treatTim}}</v-card-text> <v-card-text v-if="!treatTim">.</v-card-text>
            </v-card>
        </v-col>
      </v-row>
  </div>
</template>

<script>
import API from "@/api/API";
import { mdiCheckCircleOutline } from '@mdi/js';
import { mdiCancel } from '@mdi/js';
export default {
    components: {
        // eslint-disable-next-line vue/no-unused-components
        mdiCheckCircleOutline,
        // eslint-disable-next-line vue/no-unused-components
        mdiCancel
    },
    data() {
        return {
            srId: 0,
            regId: "",
            regState:"등록 완료",
            regTim: "", 
            recivrId:"",
            rcptState:"",
            rcptDate:"",
            doId:"",
            doState:"",
            doTim:"",
            opetrId:"",
            treatState:"",
            treatTim:"",
            returnState:"",
    }
    },
    created() {
        this.srId = this.$route.params.srId;
        this.getSrRcptTreatInfo();

    },
    methods: {
        getSrRcptTreatInfo() {
        API.getSrRcptTreatWithSrId(
        "srId=" + this.srId,
        (res) => {
            this.regId = res.regId;
            this.regState=res.regState;
            this.regTim=res.regTim;
            this.recivrId=res.recivrId;
            this.rcptState=res.rcptState;
            this.rcptDate=res.rcptDate;
            this.doId=res.doId;
            this.doState=res.doState;
            this.doTim=res.doTim;
            this.opetrId=res.opetrId;
            this.treatState=res.treatState;
            this.treatTim=res.treatTim;
            this.returnState=res.returnState;
            this.addClass();
        },
        (err) => {
          console.log(err);
        }
      );
        },
        addClass() {
            var reg = document.getElementById('regProgress');
            var rcpt = document.getElementById('rcptProgress');
            var doP = document.getElementById('doProgress');
            var treat = document.getElementById('treatProgress');
            if(this.returnState) {
                // 반려상태
                rcpt.classList.remove('done');
                doP.classList.remove('done');
                treat.classList.remove('done');

                rcpt.classList.add('returnedRed');
                doP.classList.add('returned');
                treat.classList.add('returned');

            }
            else {
                reg.classList.add('done');
                if(this.rcptState) {
                    rcpt.classList.add('done');
                    if(this.doState=="진행 완료") {
                        doP.classList.add('done');
                    } else if(this.doState=="진행 중") doP.classList.add('now');
                    else {
                        doP.classList.remove('done');
                        doP.classList.remove('now');
                    }
                }
                if(this.treatState) {
                    doP.classList.remove('now');
                    doP.classList.add('done');
                    treat.classList.add('done');
                }
            }
        }
    }
          
}
</script>

<style scoped>
.done {
    border: 3px solid green !important;
}
.now {
    border: 3px solid #02355A !important;
}
.returned {
    background-color: rgb(180, 177, 177) !important; 
}
.returnedRed {
    border: 3px solid rgb(255, 0, 0) !important;
}
.progressIcon {
    float: right;
}
</style>