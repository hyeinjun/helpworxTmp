<template>
  <v-card class="pa-6">
    <v-container fluid>
      <v-row no-gutters justify="space-between">
        <v-col cols="auto" class="ma-1"> {{ totalCount }} 건 </v-col>
        <v-col cols="auto">
          <v-btn
            v-if="able == false"
            class="mr-1"
            color="rgb(42, 46, 72)"
            dark
            @click="useModify"
          >
            수정
          </v-btn>
          <v-btn
            v-if="able == true"
            class="mr-1"
            color="rgb(42, 46, 72)"
            dark
            @click="updateBtn"
          >
            저장
          </v-btn>
          <v-btn
            color="rgb(42, 46, 72)"
            dark
            @click.stop="dialog_delete = true"
          >
            휴면전환
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

    <yes-no-dialog
      v-if="dialog_delete"
      :dialogText="(dialogText = '선택 회원들이 휴면 전환됩니다.')"
      :dialogTitle="(dialogTitle = '정말로 전환하시겠습니까?')"
      @click="checkYn"
    />
    <v-dialog v-model="dialog_back" width="500" class="text-center">
      <v-card>
        <v-card-title class="headline">
          휴면 전환되었습니다.
        </v-card-title>
        <v-card-text>목록으로 되돌아갑니다.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red" text @click="goBack">
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialog_fail" width="500" class="text-center">
      <v-card>
        <v-card-title class="headline">
          전환에 실패했습니다.
        </v-card-title>
        <v-card-text>휴면 전환할 회원을 선택해주세요.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red" text @click.stop="dialog_fail = false">
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
import "tui-grid/dist/tui-grid.css";
import "tui-pagination/dist/tui-pagination.css";
import { Grid } from "@toast-ui/vue-grid";
import API from "../../api/API.js";
import YesNoDialog from "../common/YesNoDialog.vue";

export default {
  components: {
    Grid,
    YesNoDialog,
  },
  props: {
    requestData: Object,
  },
  data() {
    return {
      totalCount: 0,
      able: false,
      dialog_delete: false,
      dialog_back: false,
      dialog_fail: false,
    };
  },
  created() {
    const dataSource = {
      contentType: "application/json",
      headers: { accessToken: "Bearer " + this.$store.state.token.accessToken },
      api: {
        readData: {
          url: `${process.env.VUE_APP_API_URL}/customer/list`,
          method: "GET",
          initParams: this.requestData,
        },
        updateData: {
          url: `${process.env.VUE_APP_API_URL}/customer`,
          method: "PUT",
        },
      },
      //첫 화면에 전체 데이터를 가져온다.
      initialRequest: true,
    };
    this.gridProps = {
      data: dataSource,
      options: {
        rowHeaders: ["checkbox"],
        selectionUnit: "row",
        pageOptions: {
          perPage: 10,
        },
      },
      columns: [
        {
          header: "고객사 코드",
          name: "coCd",
          align: "left",
          width: 100,
        },
        {
          header: "고객사명",
          name: "coNm",
          align: "left",
          minwidth: 200,
        },
        {
          header: "고객 ID",
          name: "usrId",
          align: "left",
          minwidth: 200,
          editor: {
            type: "text",
            useViewMode: false,
          },
        },
        {
          header: "고객 이름",
          name: "nm",
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
          width: 80,
          align: "center",
          editor: {
            type: "radio",
            options: {
              listItems: [
                { text: "Yes", value: true },
                { text: "No", value: false },
              ],
            },
            useViewMode: false,
          },
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
    useModify() {
      this.able = !this.able;
      this.$refs.tuiGrid.invoke("enable");
    },
    doubleClick(evt) {
      if (this.able == false) {
        let usrId = this.$refs.tuiGrid.invoke("getValue", evt.rowKey, "usrId");
        this.$router.push("/manager/cust/detail/" + usrId);
      }
    },
    insertCust() {
      this.$router.push("/manager/cust/insert");
    },
    checkYn(value) {
      this.dialog_delete = false;
      if (value == "Y") {
        this.deleteCheckedRows();
      }
    },
    deleteCheckedRows() {
      // grid 에서 check 된 rows 들을 삭제하기 위한 method
      let checkedRows = this.$refs.tuiGrid.invoke("getCheckedRows");
      if (checkedRows != "") {
        let data = [];

        for (let row of checkedRows) {
          data.push(row.usrId);
        }
        let params = "usrIds=" + data.toString();

        // delete api call
        API.deleteCustomerList(
          params,
          () => {
            //reload data
            this.readGridData(this.requestData);

            this.dialog_back = true;
          },
          (err) => {
            console.log(err);
          }
        );
      } else {
        this.dialog_fail = true;
      }
    },
    goBack() {
      this.dialog_back = false;
      //reload data
      this.readGridData(this.requestData);
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
