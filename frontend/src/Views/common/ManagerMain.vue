<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <div class="pageTitle">HOME</div>
          <div class="MainCoList">
            <GridMainCoList />
          </div>
          <v-row class="justify-center">
            <v-col cols="12" sm="6">
              <v-card class="pa-3"> 
                <div class="justify-center">

                  <v-btn text id="btn1" v-on:click="currentTab = 0" class="tabs leftTab selected">계약 종료 예정 회사

                    </v-btn
                  >
                  <v-btn text id="btn2" v-on:click="currentTab = 1" class="tabs"> 신규 계약 회사

                    </v-btn
                  >
                </div>

                <div class="tab-content">
                  <div v-if="currentTab == 0"><GridEndSchdCoList /></div>
                  <div v-else-if="currentTab == 1"><GridNewContCoList /></div>
                </div>
              </v-card>
            </v-col>
            <v-col cols="12" sm="6">
              <GridMainInfoModReqList />
            </v-col>
          </v-row>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
//import MainCoList from '../../components/manager/MainCoList.vue'
//import EndSchdCoList from '../../components/manager/EndSchdCoList.vue'
//import NewContCoList from '../../components/manager/NewContCoList.vue'
import GridEndSchdCoList from "../../components/manager/GridEndSchdCoList.vue";
import GridNewContCoList from "../../components/manager/GridNewContCoList.vue";
import GridMainCoList from "../../components/manager/GridMainCoList.vue";
import GridMainInfoModReqList from "../../components/manager/GridMainInfoModReqList.vue";

export default {
  components: {
    GridNewContCoList,
    GridEndSchdCoList,
    GridMainCoList,
    GridMainInfoModReqList,
  },
  data() {
    return {
      currentTab: 0,
    };
  },
  watch: {
    currentTab(element) {
      if(this.currentTab==0){
        document.getElementById('btn1').classList.add("selected");document.getElementById('btn2').classList.remove("selected");
      }else{
        document.getElementById('btn2').classList.add("selected");document.getElementById('btn1').classList.remove("selected");
      }
    }
  }
};
</script>

<style>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
.MainCoList {
  margin-top: 10px;
  margin-bottom: 20px;
}
.tabs {
  display: inline !important; 
    font-size: 1.1em;
    text-align: left;
    padding: 5px;
    width: 50% !important;
    border-radius: 0px 0px 0px 0px !important;
}
.leftTab {
  border-right: 1px solid black; 

}
.selected {
  background-color: lightgray !important;
}
</style>
