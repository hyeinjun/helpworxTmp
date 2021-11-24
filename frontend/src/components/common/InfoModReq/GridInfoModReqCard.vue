<template>
  <grid
    ref="tuiGrid"
    :data="gridProps.data"
    :columns="gridProps.columns"
    :options="gridProps.options"
    @dblclick="doubleClick"
    @onGridUpdated="onGridUpdated"
  ></grid>
</template>

<script>
import "tui-date-picker/dist/tui-date-picker.css";
import "tui-grid/dist/tui-grid.css";
import "tui-pagination/dist/tui-pagination.css";
import { Grid } from "@toast-ui/vue-grid";

export default {
  components: {
    Grid,
  },
  props: {
    requestData: Object,
    auth: String
  },
  data() {
    return {
      gridProps: null,
      totalCount: 0,
      able: false,
    };
  },
  created() {
    const dataSource = {
      contentType: "application/json",
      headers: { accessToken: "Bearer " + this.$store.state.token.accessToken },
      api: {
        readData: {
          url: `${process.env.VUE_APP_API_URL}/${this.auth}/info-mod/list`,
          method: "GET",
          initParams: this.requestData,
        },
      },
      //첫 화면에 전체 데이터를 가져온다.
    };
    this.gridProps = {
      data: dataSource,
      disable: true,
      options: {
        selectionUnit: "row",
        pageOptions: {
          perPage: 10,
        },
      },
      columns: [
        {
          header: "접수 코드",
          name: "reqId",
          align: "right",
          width: 100,
        },
        {
          header: "제목",
          name: "reqSbjct",
          align: "left",
          minwidth: 200,
        },

        {
          header: "신청자",
          name: "usrId",
          align: "center",
          width: 200,
        },
        {
          header: "진행 상황",
          name: "state",
          width: 200,
          align: "left",
        },
        {
          header: "요청일",
          name: "reqTim",
          minWidth: 200,
          align: "center",
        },
      ],
    };
  },
  methods: {
    readGridData(params) {
      // grid rendering 후 readData 호출
      // ('readData', page', params)
      this.$refs.tuiGrid.invoke("readData", 1, params);
    },
    doubleClick(evt) {
      if (this.able == false) {
        let reqId = this.$refs.tuiGrid.invoke("getValue", evt.rowKey, "reqId");
        let auth="";
        if(this.$store.state.usrInfo.menuAuth=="user"){
          auth="customer"
        }else{
          auth="manager"
        }
        this.$router.push(
          "/" + auth + "/info/detail/" + reqId
        );
      }
    },
    onGridUpdated(ev) {
      // 총 조회 건수
     this.$emit("totalCount",this.$refs.tuiGrid.invoke("getPaginationTotalCount"));
    },
  },
  watch: {
    requestData: {
      deep: true, // object 내의 변경 감지하기 위한 옵션
      handler(newData) {
        this.readGridData(newData);
      },
    },
  },
};
</script>

<style>
.resultCount {
  font-size: 1em;
}
.count {
  font-size: 1.1em;
  color: rgb(42, 46, 72);
  font-weight: bold;
  display: inline;
}
.tui-pagination .tui-is-selected,
.tui-pagination strong {
  background: rgb(42, 46, 72);
  border-color: rgb(42, 46, 72);
}

.tui-pagination .tui-is-selected:hover {
  background-color: rgb(42, 46, 72);
}

.tui-pagination .tui-first-child.tui-is-selected {
  border-left: 1px solid rgb(42, 46, 72);
}

.tui-pagination .tui-last-child.tui-is-selected {
  border-right: 1px solid rgb(42, 46, 72);
}
</style>
