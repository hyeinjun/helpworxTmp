<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객 등록</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <CustInsertCard></CustInsertCard>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import CustInsertCard from "../../components/manager/CustInsertCard.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    CustInsertCard,
    BreadCrumbs,
  },
  data() {
    return {
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/manager/main",
        },
        {
          text: "고객 조회",
          disabled: false,
          href: "/manager/cust/list",
        },
        {
          text: "고객 등록",
          disabled: true,
        },
      ],
    };
  },
};
</script>
<style></style>
