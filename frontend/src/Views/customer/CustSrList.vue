<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">SR 조회</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>

          <v-card>
            <v-container fluid>
              <v-row no-gutters dense class="mt-2" style="align-items:baseline;">
                <v-col sm="1" >
                  * 사용 시스템
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="systemList"
                    @selected="selectedSystemComboBox"
                  ></combo-box-component>
                </v-col>
                <v-col sm="1">
                  업무 구분
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="jobGbList"
                    @selected="selectedJobGb"
                  ></combo-box-component>
                </v-col>
                <v-col sm="1">
                  범위
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="rangeList"
                    @selected="selectedRange"
                  ></combo-box-component>
                </v-col>
                <v-col sm="1">
                  요청 구분
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="reqGbList"
                    @selected="selectedReqGb"
                  ></combo-box-component>
                </v-col>
              </v-row>
              <v-row no-gutters dense style="align-items:baseline;">
                <v-col sm="1">
                  SR 상태
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="stateList"
                    @selected="selectedState"
                  ></combo-box-component>
                </v-col>
                <v-col sm="1">
                  제목
                </v-col>
                <v-col sm="2">
                  <v-text-field outlined v-model="sbjct" dense></v-text-field>
                </v-col>
                <v-col sm="1">
                  내용
                </v-col>
                <v-col sm="5">
                  <v-text-field outlined v-model="dscr" dense></v-text-field>
                </v-col>
              </v-row>
              <v-row no-gutters dense class="mt-n3">
                <v-spacer></v-spacer>
                <v-col cols="12" md="1">
                  <v-btn
                    color="rgb(42, 46, 72)"
                    dark
                    value="조회"
                    @click="clickButton"
                  >
                    조회
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
          <v-card class="pa-6 my-5">
            <grid-component :requestData="requestData"></grid-component>
          </v-card>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import { mapState } from "vuex";
import ComboBoxComponent from "../../components/customer/ComboBoxComponent.vue";
import GridComponent from "../../components/customer/GridComponent.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    ComboBoxComponent,
    GridComponent,
    BreadCrumbs,
  },
  data() {
    return {
      requestData: {
        coCd: 1,
        sysCd: 0,
        jobGbId: 0,
        reqGb: null,
        state: null,
        sbjct: null,
        dscr: null,
        usrId: null,
      },
      coCd: 1,
      sysCd: 0,
      jobGbId: 0,
      reqGb: null,
      state: null,
      sbjct: null,
      dscr: null,
      usrId: null,

      //   콤보박스 item
      systemList: [],

      jobGbList: [],

      rangeList: ["전체", "나의 SR"],

      reqGbList: ["전체", "개선", "오류", "문의", "데이터 수정"],

      stateList: ["전체", "대기", "진행중", "해결", "반려"],
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/customer/main",
        },
        {
          text: "SR 조회",
          disabled: true,
        },
      ],
    };
  },
  created() {
    this.systemList = [];
    this.systemListByCompany[this.$store.state.usrInfo.coCd].forEach((element) => {
      this.systemList.push({
        text: element.text,
        value: element.value,
      });
    });
  },
  methods: {
    // 시스템-업무구분 다단콤보
    selectedSystemComboBox(value) {
      // value : 선택된 버튼의 value
      this.sysCd = value;

      // jobList에는 다른 시스템의 jobGb까지 다 포함하기 때문에 현재 시스템의 jobGb 정보만 checked에 저장
      let checked = this.jobList.find((e) => e.sysCd == value);

      // jobGbList init
      this.jobGbList = [{ text: "전체", value: 0 }];

      checked.job.forEach((element) => {
        let dataForm = {
          text: element.job,
          value: element.jobGbId,
        };
        this.jobGbList.push(dataForm);
      });
    },
    selectedJobGb(value) {
      this.jobGbId = value;
    },
    selectedRange(value) {
      //usrId
      if (value == "나의 SR") {
        this.usrId = "minjun"; // 로그인 시 세션의 로그인 아이디 사용 예정
      }
    },
    selectedReqGb(value) {
      if (value != "전체") {
        this.reqGb = value;
      }
    },
    selectedState(value) {
      if (value != "전체") {
        this.state = value;
      }
    },
    clickButton() {
      // 조회 정보를 grid component에 전달하는 props 데이터 변경
      this.requestData = {
        coCd: this.$store.state.usrInfo.coCd,
        sysCd: this.sysCd,
        jobGbId: this.jobGbId,
        reqGb: this.reqGb,
        state: this.state,
        sbjct: this.sbjct,
        dscr: this.dscr,
        usrId: this.usrId,
      };
    },
  },
  computed: {
    ...mapState({
      systemListByCompany: (state) => state.CommonData.systemByCompany,

      // 현재 가능한 시스템 전체의 jobGb 리스트
      jobList: (state) => state.CommonData.jobGb,
    }),
  },
};
</script>

<style></style>
