<template>
  <responsive-main-grid v-if="loading"
    type="router"
    :gridSize="gridMaxSize"
    :originData="coList"
    :columns="headers"
    link="/manager/co/detail/"
  ></responsive-main-grid>
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
      coList: [],
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
          width: 200,
        },
        {
          header: "계약 종료 일자",
          name: "contEndDt",
          align: "center",
          minwidth: 200,
        },
      ],
    };
  },
  created() {
    API.getEndSchdCoList(
      "",
      (res) => {
        res.forEach((element, index) => {
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
  methods: {},
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

<style></style>
