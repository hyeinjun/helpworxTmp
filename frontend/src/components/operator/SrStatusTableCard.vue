<template>
  <div>
    <v-expansion-panels class="panels">
      <v-expansion-panel>
          <v-row class="d-flex-inline rowAlign mt-6 mb-n16" no-gutters dense>
            <v-col cols="12" md="1">
              * 고객사
            </v-col>
            <v-col cols="12" md="2" >
              <combo-box-component
                :items="companyList"
                :firstSelectedItem="coCd"
                @selected="selectedCompanyComboBox"
              ></combo-box-component>
            </v-col>
            <v-col cols="12" md="1" >
              * 시스템
            </v-col>
            <v-col cols="12" md="2" >
              <v-combobox outlined dense
                :items="systemList"
                v-model="selectedSys"
                @selected="selectedSystemComboBox"
              ></v-combobox>
            </v-col>
            <v-spacer></v-spacer>
          </v-row>
          <br>
           <v-expansion-panel-header>
          <template v-slot:actions>
            <v-icon  class="pos">
                mdi-arrow-down-box
            </v-icon>
          </template>
           </v-expansion-panel-header>

        <v-expansion-panel-content class="cont" >
          <sr-data-table
            :coCd="coCd"
            :sysCd="sysCd"
            :rcptAvgTime="rcptAvgTime"
            :treatAvgTime="treatAvgTime"
            :isStatus="true"
          ></sr-data-table>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </div>
</template>

<script>
import { mapState } from "vuex";
import SrDataTable from "./SrDataTable.vue";
import ComboBoxComponent from "../customer/ComboBoxComponent.vue";

export default {
  components: {
    SrDataTable,
    ComboBoxComponent
  },
  props: {
    treatAvgTime: Number,
    rcptAvgTime: Number,
  },
  data() {
    return {
      systemList: [],
      coCd: "",
      sysCd: 0,
      selectedCo:"",
      selectedSys:"",
    };
  },
  created() {
    this.selectedCo = this.companyList[0];
    this.coCd = this.$store.state.usrInfo.coCd;
  },
  methods: {
    selectedCompanyComboBox(value) {
      this.$emit("selectedCompany", value);
      this.systemList = [];
      this.systemByCompany[value].forEach((element) => {
        this.systemList.push({
          text: element.text,
          value: element.value,
        });
      });
      this.selectedSys=this.systemList[0];
      this.coCd = value;
    },
    selectedSystemComboBox(value) {
      this.$emit("selectedSystem", value);
      this.sysCd = value;
    },
  },
  watch: {
      selectedCo() {
          this.selectedCompanyComboBox(this.selectedCo.value);
      },
      selectedSys() {
        this.selectedSystemComboBox(this.selectedSys.value);
      }
  },
  computed: {
    ...mapState({
      companyList: (state) => state.CommonData.companyList,
      systemByCompany: (state) => state.CommonData.systemByCompany,
    }),
  },
};
</script>

<style scoped>

.rowAlign{
    align-items: baseline;
    text-align: center;
}
.panels{
    position:relative;
}
.pos{
    position: absolute !important;
    left: 50%;
    bottom: -10px;
}
.cont {
    border-top: 1px dotted #d6d0d0;
    padding-top: 10px;
}
</style>