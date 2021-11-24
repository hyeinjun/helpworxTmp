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

          <!-- 검색 조건 카드 -->
          <v-card>
            <v-container fluid>
              <v-row no-gutters dense class="mt-3" style="align-items:baseline;">
                <v-col sm="1">
                  * 고객사
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="companyList"
                    :firstSelectedItem="coCd"
                    @selected="selectedCompanyComboBox"
                  ></combo-box-component>
                </v-col>
                <v-col sm="1">
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
                    @selected="selectedJobGbComboBox"
                  ></combo-box-component>
                </v-col>
              </v-row>
              <v-row no-gutters dense style="align-items:baseline;">
                <v-col sm="1">
                  요청 구분
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="reqGbList"
                    @selected="selectedReqComboBox"
                  ></combo-box-component>
                </v-col>
                <v-col sm="1">
                  SR 상태
                </v-col>
                <v-col sm="2">
                  <combo-box-component
                    :items="stateList"
                    @selected="selectedStateComboBox"
                  ></combo-box-component>
                </v-col>
              </v-row>
              <v-row no-gutters dense class="mb-n3" style="align-items:baseline;">
                <v-col cols="1">
                  제목
                </v-col>
                <v-col cols="2">
                  <v-text-field outlined dense v-model="sbjct"></v-text-field>
                </v-col>
                <v-col cols="1">
                  내용
                </v-col>
                <v-col cols="5">
                  <v-text-field outlined dense v-model="dscr"></v-text-field>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="1">
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

          <!-- 그리드 -->
          <v-card class="pa-6 my-5">
            <sr-list-grid-component
              :requestData="requestData"
            ></sr-list-grid-component>
          </v-card>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import { mapState } from "vuex";
import ComboBoxComponent from "../../components/customer/ComboBoxComponent.vue";
import SrListGridComponent from "../../components/operator/SrListGridComponent.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    ComboBoxComponent,
    SrListGridComponent,
    BreadCrumbs,
  },
  data() {
    return {
      requestData: {
        coCd: 0,
        sysCd: 0,
        jobGbId: 0,
        reqGb: null,
        state: null,
        sbjct: null,
        dscr: null,
      },

      coCd: 0,
      sysCd: 0,
      jobGbId: 0,
      reqGb: null,
      sbjct: null,
      dscr: null,

      // 콤보박스 item
      systemList: [],
      jobGbList: [],
      reqGbList: ["전체", "개선", "오류", "문의", "데이터 수정"],
      stateList: ["전체", "대기", "진행중", "해결", "반려"],
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/operator/main",
        },
        {
          text: "SR 조회",
          disabled: true,
        },
      ],
    };
  },
  created() {
    this.coCd = this.$store.state.usrInfo.coCd;
  },
  methods: {
    selectedCompanyComboBox(value) {
      this.coCd = value;

      this.systemList = [];
      this.systemListByCompany[value].forEach((element) => {
        this.systemList.push({
          text: element.text,
          value: element.value,
        });
      });
    },
    selectedSystemComboBox(value) {
      // value : 선택된 버튼의 value
      this.sysCd = value;

      // jobList에는 다른 시스템의 jobGb까지 다 포함하기 때문에 현재 시스템의 jobGb 정보만 checked에 저장
      let checked = this.jobList.find((e) => e.sysCd == value);

      // jobGbList init
      this.jobGbList = [{ text: "전체", value: 0 }];

      if (checked != undefined) {
        checked.job.forEach((element) => {
          let dataForm = {
            text: element.job,
            value: element.jobGbId,
          };
          this.jobGbList.push(dataForm);
        });
      }
    },
    selectedJobGbComboBox(value) {
      this.jobGbId = value;
    },
    selectedReqComboBox(value) {
      if (value != "전체") {
        this.reqGb = value;
      }
    },
    selectedStateComboBox(value) {
      if (value != "전체") {
        this.state = value;
      }
    },
    clickButton() {
      if (this.coCd == "0" || this.sysCd == 0) {
        alert("필수 항목을 모두 채워주세요");
      } else {
        this.requestData = {
          coCd: this.coCd,
          sysCd: this.sysCd,
          jobGbId: this.jobGbId,
          reqGb: this.reqGb,
          state: this.state,
          sbjct: this.sbjct,
          dscr: this.dscr,
        };
      }
    },
  },
  computed: {
    ...mapState({
      // 고객사 선택 comboBox
      companyList: (state) => state.CommonData.companyList,

      // 현재 가능한 시스템 전체의 jobGb 리스트
      jobList: (state) => state.CommonData.jobGb,

      // 회사별 사용 시스템 리스트
      systemListByCompany: (state) => state.CommonData.systemByCompany,
    }),
  },
};
</script>

<style></style>
