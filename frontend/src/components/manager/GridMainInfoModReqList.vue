<template>
  <v-card v-if="loading" class="pa-3">
    <div class="grid-title">완료되지 않은 요청</div>
    <responsive-main-grid
      type="router"
      :gridSize="gridMaxSize"
      :originData="infoList"
      :columns="headers"
      link="/manager/info/detail/"
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
      infoList: [],
      loading: false,
      headers: [
        {
          header: "요청 코드",
          name: "reqId",
          align: "right",
          width: 80,
        },
        {
          header: "요청자",
          name: "usrId",
          align: "left",
          width: 100,
        },
        {
          header: "요청 제목",
          name: "reqSbjct",
          align: "left",
          minwidth: 200,
        },
        {
          header: "상태",
          name: "state",
          align: "center",
          width: 100,
        },
      ],
    };
  },
  created() {
    API.getReqList(
      "",
      (res) => {
        res.forEach((element, index) => {
          res[index].id = element.reqId;
        });
        this.infoList = res;
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

<style>
.grid-title {
  font-size: 1.1em;
  text-align: left;
  padding: 5px;
}
</style>
