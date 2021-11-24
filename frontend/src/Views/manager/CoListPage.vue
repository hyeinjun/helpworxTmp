<template>
  <div>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객사 조회</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
            <v-card
              ><v-container fluid>
                <v-row no-gutters dense class="mt-3">
                  <v-col sm="1" class="mr-2 mt-1">
                    고객사코드
                  </v-col>
                  <v-col sm="2" class="mr-5">
                    <v-autocomplete
                      class="auto"
                      placeholder="고객사 코드"
                      v-model="coCd"
                      :items="coCdList" 
                      dense outlined 
                    ></v-autocomplete>
                  </v-col>

                  <v-col sm="1" class="mr-2 mt-1">
                    고객사명
                  </v-col>
                  <v-col sm="2" class="mr-5">
                    <v-text-field
                      placeholder="고객사명"
                      outlined
                      dense
                      v-model="coNm"
                    ></v-text-field>

                  </v-col>

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

          <div class="coList">
            <grid-co-list :requestData="requestData"></grid-co-list>
          </div>
        </v-container>
      </v-main>
  </div>
</template>

<script>
import GridCoList from "../../components/manager/GridCoList.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
import API from "../../api/API";

export default {
  components: {
    GridCoList,
    BreadCrumbs,
  },
  data() {
    return {
      requestData: {
        coCd: "",
        coNm: "",
      },
      coCd: "",
      coNm: "",
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/manager/main",
        },
        {
          text: "고객사 조회",
          disabled: true,
        },
      ],
      coCdList: [],
    };
  },
  methods: {
    clickButton() {
      // 조회 정보를 grid component에 전달하는 props 데이터 변경
      this.requestData = {
        coCd: this.coCd,
        coNm: this.coNm,
      };
    },
  },
  created() {
    // 고객사코드 받아오기
    API.getCoCdList(
      '',
      (res) => {
        this.coCdList = res;
      },
      (err) => {
        console.log(err);
      }
    )
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
.v-menu__content {
  z-index: 999 !important;
}
</style>
