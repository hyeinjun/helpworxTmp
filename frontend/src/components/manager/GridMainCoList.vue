<template>
  <v-card v-if="loading" class="pa-3"> 
    <div class="grid-title">고객사 목록</div>
    <responsive-main-grid
      type="router"
      :gridSize="gridMaxSize"
      :originData="coList"
      :columns="headers"
      link="/manager/co/detail/"
    ></responsive-main-grid>
  </v-card>
</template>

<script>
import API from "@/api/API";
import ResponsiveMainGrid from "../mainPage/ResponsiveMainGrid.vue";

export default {
  components: {
    ResponsiveMainGrid,
  },
  data() {
    return {
      coList:[],
      loading: false,
      headers: [
        {
          header: "회사 코드",
          name: "coCd",
          align: "left",
          width: 100,
        },
        {
          header: "회사명",
          name: "coNm",
          align: "left",
          minwidth: 200,
        },
        {
          header: "고객 수",
          name: "custNum",
          align: "right",
          width: 100,
        },
        {
          header: "사용 시스템 수",
          name: "sysNum",
          align: "right",
          width: 120,
        },
        {
          header: "사용 여부",
          name: "useYn",
          align: "center",
          width: 100,
        },
      ],
    };
  },
  created() {
    API.getCompanyList(
      "",
      (res) => {
        res.forEach((element,index) => {
          res[index].id = element.coCd;
        });
        this.coList = res;
        this.loading = true;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
  },
  computed: {
      gridMaxSize() {
        switch (this.$vuetify.breakpoint.name) {
          case "xs":
            return 2;
          case "sm":
            return 3;
          case "md":
            return 4;
          case "lg":
            return 5;
          case "xl":
            return 7;
        }
        return 0;
      },
    },
};
</script>

<style>
.grid-title {
    font-size: 1.1em;
    text-align: left;
    padding: 5px;
}
</style>
