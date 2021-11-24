<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
            <v-row>
            <v-col> <div class="pageTitle">정보 수정 요청 수정</div></v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
            <v-card class="pa-5">
            <InfoModReqModifyCard></InfoModReqModifyCard>
            </v-card>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import InfoModReqModifyCard from "../../components/customer/InfoModReqModifyCard.vue"

import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
export default {
  components:{
    InfoModReqModifyCard,
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
          text: "정보 수정 요청 조회",
          disabled: false,
          href: "/customer/info/list",
        },{
          text: "정보 수정 요청 상세",
          disabled: false,
          href: "/customer/info/detail/"+this.$route.params.reqId,
        },
        {
          text: "정보 수정 요청 수정",
          disabled: true,
        },
      ],
    }
  }
}
</script>
<style>

</style>