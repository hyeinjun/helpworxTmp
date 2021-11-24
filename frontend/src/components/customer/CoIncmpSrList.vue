<template>
  <v-card v-if="loading" class="pa-3">
    <div class="grid-title">회사 미완료 SR</div>
    <responsive-main-grid
      type="router"
      :gridSize="gridMaxSize"
      :originData="srList"
      :columns="headers"
      link="/customer/sr/detail/"
    ></responsive-main-grid>
  </v-card>
</template>

<script>
import API from "@/api/API";
import ResponsiveMainGrid from "../mainPage/ResponsiveMainGrid.vue"

export default {
  components: {
    ResponsiveMainGrid
  },
  data() {
    return {
      coCd: this.$store.state.usrInfo.coCd,
      headers: [
        {
          header: "No",
          name: "id",
          align: "right",
          width: 50,
        },
        {
          header: "시스템",
          name: "sysNm",
          align: "center",
        },
        {
          header: "제목",
          name: "sbjct",
          ellipsis: true
        },
        {
          header: "요청자",
          name: "usrId",
          align: "center"
        },
        {
          header: "요청일",
          name: "regTim",
          align: "center",
        },
        {
          header: "상태",
          name: "state",
          align: "center",
        },
      ],
      srList: [],
      loading: false,
    };
  },
  created() {
    // usrId를 가지고 back에서 sr 정보 가져오기
    API.getIncmpListWithCoCd(
      "coCd=" + this.coCd,
      (res) => {
        res.forEach((element,index) => {
          res[index].id = element.srId;
        });
        this.srList = res;
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

<style scoped>
.grid-title {
    font-size: 1.1em;
    text-align: left;
    padding: 5px;
}
</style>
