<template>
  <v-card class="pa-6">
    <v-container fluid>
      <v-row no-gutters justify="space-between">
        <v-col cols="auto" class="ma-1">
          {{totalCount}} 건
        </v-col>
        <v-col cols="auto">
          <v-btn
            v-if="able == false"
            class="ma-1"
            color="rgb(42, 46, 72)"
            dark
            @click="useModify"
          >
            수정
          </v-btn>
          <v-btn
            v-if="able == true"
            class="ma-1"
            color="rgb(42, 46, 72)"
            dark
            @click="updateBtn"
          >
            저장
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
    <grid
      ref="tuiGrid"
      :data="gridProps.data"
      :columns="gridProps.columns"
      :options="gridProps.options"
      @dblclick="doubleClick"
      @onGridUpdated="onGridUpdated"
    ></grid>
  </v-card>
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
          url: `${process.env.VUE_APP_API_URL}/co/list`,
          method: "GET",
          initParams: this.requestData,
        },
        updateData: {
          url: `${process.env.VUE_APP_API_URL}/co`,
          method: "PUT",
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
          editor: {
            type: "text",
            useViewMode: false,
          },
        },

        {
          header: "사용 여부",
          name: "useYn",
          align: 'center',
          width: 80,
          editor: {
            type: "radio",
            options: {
              listItems: [
                { text: "사용", value: true },
                { text: "미사용", value: false },
              ],
            },
            useViewMode: false,
          },
        },
        {
          header: "계약 시작일",
          name: "contBeginDt",
          minWidth: 200,
          align: 'center',
          editor: {
            type: "datePicker",
            options: {
              format: "yyyy-MM-dd",
              useViewMode: true,
            },
          },
        },
        {
          header: "계약 만료일",
          name: "contEndDt",
          minWidth: 200,
          align: 'center',
          editor: {
            type: "datePicker",
            options: {
              format: "yyyy-MM-dd",
            },
            useViewMode: true,
          },
        },
        {
          header: "고객 수",
          name: "custNum",
          align: "right",
          width: 70,
        },
        {
          header: "사용 시스템 수",
          name: "sysNum",
          align: "right",
          width: 100,
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
        let coCd = this.$refs.tuiGrid.invoke("getValue", evt.rowKey, "coCd");
        this.$router.push("/manager/co/detail/" + coCd);
      }
    },
    onGridUpdated(ev) {
      // 총 조회 건수
      this.totalCount = this.$refs.tuiGrid.invoke("getPaginationTotalCount");
    },
    updateBtn() {
      const { rowKey, columnName } = this.$refs.tuiGrid.invoke(
        "getFocusedCell"
      );

      if (rowKey && columnName) {
        this.$refs.tuiGrid.invoke("finishEditing", rowKey, columnName);
      }
      console.log(this.$refs.tuiGrid.invoke("getFocusedCell"));

      this.$refs.tuiGrid.invoke("blur");
      this.$refs.tuiGrid.invoke("request", "updateData", {
        checkedOnly: false,
        modifiedOnly: true,
      });

      this.$refs.tuiGrid.invoke("on", "response", (ev) => {
        const { response } = ev.xhr;
        const responseObj = JSON.parse(response);

        console.log("result : ", responseObj.result);
        console.log("data : ", responseObj.data);
      });
      this.able = false;

      this.$refs.tuiGrid.invoke("enable", false);
      this.requestData;
    },
    insertCo() {
      this.$router.push("/co/insert");
    },
    useModify() {
      this.able = !this.able;
      this.$refs.tuiGrid.invoke("enable");
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
