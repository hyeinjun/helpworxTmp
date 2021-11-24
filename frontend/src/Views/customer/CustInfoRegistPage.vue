<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
            <v-row>
            <v-col> <div class="pageTitle">정보 수정 요청 등록</div></v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
            <v-card class="pa-5">
            <InfoModReqRegistCard></InfoModReqRegistCard>
            </v-card>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import InfoModReqRegistCard from "../../components/customer/InfoModReqRegistCard.vue"
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
export default {
  components:{
    InfoModReqRegistCard,
    BreadCrumbs,
  },
  data(){
    return{
crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/customer/main",
        },
        {
          text: "정보 수정 요청 등록",
          disabled: true,
        },
]
    }
  }
}
</script>
<style>

</style>