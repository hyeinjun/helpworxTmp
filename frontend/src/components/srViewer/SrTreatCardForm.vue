<template>
    <v-card class="mt-3 pa-1" v-if="hasAuth || isCompleted">
        <v-row class="mb-n5" justify="space-between" align="center">
            <v-col cols="auto">
                <v-card-title>SR 처리</v-card-title>
            </v-col>
            <v-col cols="auto">
                <div v-if="hasAuth && isCompleted" class="mr-2">
                    <v-btn v-if="!modifiable" class="mr-1" color="rgb(42, 46, 72)" dark @click="updateMode(true)">수정</v-btn>
                    <v-btn v-if="modifiable" class="mr-1" color="rgb(42, 46, 72)" dark @click="saveTreat">저장</v-btn>
                    <v-btn v-if="modifiable" class="mr-1" color="rgb(42, 46, 72)" dark @click="clickCancel">처리 취소</v-btn>
                    <v-btn v-if="modifiable" class="mr-1" color="rgb(42, 46, 72)" dark @click="updateMode(false)">취소</v-btn>
                </div>
                <div v-else-if="hasAuth" class="mr-2">
                    <v-btn v-if="modifiable" class="mr-1" color="rgb(42, 46, 72)" dark @click="saveTreat">저장</v-btn>
                </div>
            </v-col>
        </v-row>
        
        <!-- 삭제 시 나오는 모달 -->
        <yes-no-dialog
            v-if="dialog_delete"
            dialogTitle="정말로 취소하시겠습니까?"
            dialogText="SR 처리 내역이 삭제됩니다."
            @click="clickDialog"
        ></yes-no-dialog>

        <v-container fluid>
            <v-row class="justify-center">
                <v-col cols="1">
                    <v-subheader style="align-content: center; white-space:nowrap;" class="pb-2 mr-2" >처리자</v-subheader>
                </v-col>
                <v-col cols="2" class="ml-n10" align="left">
                    <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="operatorId"></v-text-field>
                </v-col>
                <v-col cols="1">
                    <v-subheader style="align-content: center; white-space:nowrap;" class="pb-2 ml-n5">소요시간</v-subheader>
                </v-col>
                <v-col cols="2" class="ml-n10" align="left">
                    <v-text-field background-color="#EEEEEE" solo flat dense filled readonly :value="rqmtTim"></v-text-field>
                </v-col>
                <v-spacer></v-spacer>
            </v-row>

        <v-row class="mt-n7">
            <v-col cols="1">
                <v-subheader class="pb-2 mr-2 ml-n3" style="white-space:nowrap;">처리시간</v-subheader>
            </v-col>
            <v-col cols="2" class="ml-n10" align="left">
                <date-picker
                    :propsDateInfo="treatBeginTim"
                    :readOnly="true"
                ></date-picker>
            </v-col>
            <v-col cols="1">
                <v-subheader v-if="modifiable" class="pb-2 justify-center">~ *{{'\xa0\xa0\xa0\xa0\xa0'}}</v-subheader>
                <v-subheader v-else class="pb-2 justify-center">{{'\xa0\xa0\xa0'}}~{{'\xa0\xa0\xa0\xa0\xa0'}}</v-subheader>
            </v-col>
            <v-col cols="2" class="ml-n10" align="left">
                <date-picker
                    :propsDateInfo="treatEndTim"
                    :readOnly="!modifiable"
                    :minTime="treatBeginTim"
                    @change="changeDate"
                ></date-picker>
            </v-col>
            <v-spacer></v-spacer>
        </v-row>

        <v-row v-if="modifiable" class="mt-n7" style="height: 350px;">
            <v-col cols="1">
            <v-subheader>*내용</v-subheader>
            </v-col>
            <v-col v-if="modifiable" class="ml-n10">
                <div class="mr-1">
                    <editor
                        align="left"
                        height="300px"
                        initialEditType="wysiwyg"
                        previewStyle="vertical"
                        ref="tuiEditor"
                        :initialValue="treatDscr"
                    />
                </div>
            </v-col>
        </v-row>
        <v-row v-else class="mt-n7" style="height: 350px;">
            <v-col cols="1">
            <v-subheader>내용</v-subheader>
            </v-col>
            <v-col class="ml-n7" align="left">
                <div class="ml-n3 mr-1 px-3 textViewer">
                    <viewer
                        v-if="treatDscr!=null"
                        :initialValue="treatDscr"
                        height="300px"
                    />
                </div>
            </v-col>
        </v-row>
    </v-container>
  </v-card>
</template>

<script>
import DatePicker from "../common/DatePicker.vue"
import YesNoDialog from '../common/YesNoDialog.vue'
import API from "@/api/API";

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
            treatBeginTim: "",
            treatEndTim: "",
            treatDscr: null,
            operatorId: "",
            rqmtTim: "",
            dialog_delete: false,
            modal1: "",
            modal2: "",
            readOnly: true,
            modifiable: false
        };
    },
    props: {
        sysNm: String,
        isCompleted: Boolean,
        hasAuth: Boolean
    },
    created() {
        this.srId = Number(this.$route.params.srId);

        if(!this.isCompleted && this.hasAuth) {
            this.modifiable = true;
            this.operatorId = this.$store.state.usrInfo.usrId
        }
        // 접수 정보 가져오기
        this.getTreat();
    },
    watch: {
        treatBeginTim() {
            this.diffTime(this.treatBeginTim, this.treatEndTim);
            this.$emit("treatForm", this.usrId, this.treatDscr, this.treatBeginTim, this.treatEndTim, this.rqmtTim);
        },
        treatEndTim() {
            this.diffTime(this.treatBeginTim, this.treatEndTim);
            this.$emit("treatForm", this.usrId, this.treatDscr, this.treatBeginTim, this.treatEndTim, this.rqmtTim);
        },
        treatDscr() {
            this.$emit("treatForm", this.usrId, this.treatDscr, this.treatBeginTim, this.treatEndTim, this.rqmtTim);
        },
    },
    methods: {
        changeDate(value) {
            this.treatEndTim = value;
        },
        updateMode(value) {
            this.modifiable = value;
        },
        getTreat() {
            API.getTreatBySrId(
                "srId=" + this.srId,
            (res) => {
                console.log(res);
                if(res.treatBeginTim==null) this.treatBeginTim = "";
                else this.treatBeginTim=this.dateForm(res.treatBeginTim);
                if(res.treatEndTim==null) this.treatEndTim = "";
                else this.treatEndTim=this.dateForm(res.treatEndTim);
                this.treatDscr=res.treatDscr;

                if(res.opetrId != null) {
                    this.operatorId = res.opetrId;
                }
                
                this.rqmtTim=res.rqmtTim;
            },
            (err) => {
                console.log(err);
            });
        },
        saveTreat() {
            if(this.checkInput()) {
                let data = {};
                data.srId = this.srId;
                data.opetrId = this.usrId;
                data.treatDscr = this.$refs.tuiEditor.invoke('getHtml');
                data.treatEndTim = this.treatEndTim;
                data.rqmtTim = this.rqmtTim;
                API.saveSrTreat(
                    data,
                    (res) => {
                        console.log(res);
                        this.$router.go(0);
                    },
                    (err) => {
                        console.log(err);
                    }
                )
                this.modifiable = false;
            } else {
                alert('내용을 입력하세요.');
            }
        },
        checkInput() {
            let dscr = this.$refs.tuiEditor.invoke('getHtml');
            if(dscr == '' || this.treatEndTim == '') {
                return false;
            }
            return true;
        },
        deleteTreat() {
            this.dialog_delete = false;
            API.deleteTreat(
                "srId=" + this.srId,
                (res) => {
                    console.log(res);
                    this.getTreat();
                },
                (err) => {
                    console.log(err);
                });
            this.$router.go(0);
        },
        dateForm(inputDate) {
            var t = new Date(inputDate);
            var year = t.getFullYear();
            var month = (t.getMonth()+1);
            if(month<10) month = '0'+month;
            var date = t.getDate();
            if(date<10) date = '0'+date;
            return year+'-'+month+'-'+date;
        },
        diffTime(beginTim, endTim) {
            var diffSec = (new Date(endTim).getTime() - new Date(beginTim).getTime()) / 1000;
            var diffDate = diffSec / (60*60*24);
            var diffHour = (diffSec - diffDate * 60 * 60 * 24) / (60 * 60);
            var rqmtTim = "";
            if(diffDate>0) rqmtTim+=diffDate+"일 ";
            if(diffHour>0) rqmtTim+=diffHour+"시간 ";
            this.rqmtTim = rqmtTim;
        },
        clickCancel() {
            this.dialog_delete = true;
        },
        clickDialog(value) {
            if(value === 'Y') {
                this.deleteTreat();
            }else {
                this.dialog_delete = false;
            }
        }
    },
}
</script>

<style scoped>
.v-subheader {
  font-weight: bold;
  font-size: 1.07em;
}
</style>