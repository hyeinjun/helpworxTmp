<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객 조회</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <v-card class="mb-3">
            <v-container fluid>
              <v-row no-gutters dense class="mt-3">
                  <v-col sm="1" class="mr-2 mt-2">
                  * 고객사
                </v-col>
                <v-col sm="2" class="mr-5">
                  <v-select
                    v-model="coCd"
                    :items="coList"
                    item-text="coNm"
                    item-value="coCd"
                    menu-props="auto"
                      dense outlined 
                  ></v-select>
                </v-col>
                  <v-col sm="1" class="mr-2 mt-2">
                  고객 ID
                </v-col>
                <v-col sm="2" class="mr-5">
                  <v-text-field
                    placeholder="고객ID"
                      dense outlined 
                    v-model="usrId"
                  ></v-text-field>
                </v-col>
                
                  <v-col sm="1" class="mr-2 mt-2">
                  고객이름
                </v-col>
                <v-col sm="2">
                  <v-text-field
                    placeholder="고객이름"
                      dense outlined 
                    v-model="nm"
                  ></v-text-field> </v-col
                ><v-spacer></v-spacer>
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

          <div class="custList">
            <grid-cust-list :requestData="requestData"> </grid-cust-list>
          </div>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import API from "@/api/API.js";
import GridCustList from "../../components/manager/GridCustList.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    GridCustList,
    BreadCrumbs,
  },
  data() {
    return {
      coCd: this.$store.state.usrInfo.coCd,
      nm: "",
      usrId: "",
      coList: [],
      requestData: {
        coCd: this.$store.state.usrInfo.coCd,
        nm: "",
        usrId: "",
      },
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/manager/main",
        },
        {
          text: "고객 조회",
          disabled: true,
        },
      ],
    };
  },
  created() {
    API.getCoNmList(
      "",
      (res) => {
        this.coList = [{ coNm: "전체", coCd: "" }];

        res.forEach((element) => {
          if (element.useYn == true) {
            let dataForm = {
              coCd: element.coCd,
              coNm: element.coNm,
            };
            this.coList.push(dataForm);
          }
        });
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    selectedCo(value) {
      this.coCd = value;
    },
    clickButton() {
      // 조회 정보를 grid component에 전달하는 props 데이터 변경
      this.requestData = {
        coCd: this.coCd,
        nm: this.nm,
        usrId: this.usrId,
      };
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
