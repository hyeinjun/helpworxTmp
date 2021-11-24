<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">정보 수정 요청 조회</div></v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <div class="searchCompany">
            <v-card
              ><v-container fluid>
                <v-row no-gutters dense class="mt-3">
                  <v-col sm="1" class="mr-2 mt-2">
                    진행상태
                  </v-col>
                  <v-col sm="2" class="mr-5">
                    <v-select
                      :items="stateList"
                      v-model="state"
                      menu-props="auto"
                      dense
                      outlined
                    ></v-select>
                  </v-col>
                  <v-col sm="1" class="mr-2 mt-2">
                    아이디
                  </v-col>
                  <v-col sm="2" class="mr-5">
                    <v-text-field
                      placeholder="아이디"
                      dense
                      outlined
                      v-model="usrId"
                    ></v-text-field>
                  </v-col>
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="1" class="mt-2">
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
          </div>

          <div class="InfoModReqList">
            <v-card class="pa-6 my-5">
              <v-container fluid>
                <v-row no-gutters justify="space-between">
                  <v-col cols="auto" class="ma-1"> {{ totalCount }} 건 </v-col>
                </v-row>
              </v-container>
              <grid-info-mod-req
                :requestData="requestData"
                :auth="auth"
                @totalCount="getTotalCount"
              />
            </v-card>
          </div>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import GridInfoModReq from "../../components/common/InfoModReq/GridInfoModReqCard.vue";

import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
export default {
  components: {
    GridInfoModReq,
    BreadCrumbs,
  },
  data() {
    return {
      requestData: {
        state: "",
        usrId: "",
      },
      auth:"mng",
      state: null,
      usrId: null,
      totalCount: null,
      stateList: [
        { text: "전체", value: null },
        { text: "대기", value: "대기" },
        { text: "접수", value: "접수" },
        { text: "완료", value: "완료" },
        { text: "반려", value: "반려" },
      ],
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/manager/main",
        },
        {
          text: "정보 수정 요청 조회",
          disabled: true,
        },
      ],
    };
  },
  methods: {
    clickButton() {
      // 조회 정보를 grid component에 전달하는 props 데이터 변경
      this.requestData = {
        state: this.state,
        usrId: this.usrId,
      };
    },
    deleteInfoModReq() {},
    getTotalCount(value) {
      this.totalCount = value;
    },
  },
};
</script>

<style>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
.coList {
  margin-top: 15px;
}
</style>
