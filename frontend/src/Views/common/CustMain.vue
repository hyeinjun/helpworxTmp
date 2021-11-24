<template>
  <div>
      <v-main class="ma-3">
        <v-container fluid>
          <div class="pageTitle">HOME</div>
            <MainSystem/>
            <v-row class="mb-6 d-flex">
              <v-col cols="12" md="6">
                <MyIncmpSrList/>
              </v-col>
              <v-col cols="12" md="6">
                <CoIncmpSrList/>
              </v-col>
            </v-row>
        </v-container>
      </v-main>
  </div>
</template>

<script>
import MainSystem from '../../components/customer/MainSystem.vue'
import MyIncmpSrList from '../../components/customer/MyIncmpSrList.vue'
import CoIncmpSrList from '../../components/customer/CoIncmpSrList.vue'

export default {
components: {
    MainSystem,
    MyIncmpSrList,
    CoIncmpSrList
  },
  data() {
    return{

    }
  },
}
</script>

<style>
.pageTitle{
  font-size: 1.5em;
  text-align: left;
}
</style>