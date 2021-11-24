<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객 상세</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <CustDetailCard></CustDetailCard>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import CustDetailCard from "../../components/manager/CustDetailCard";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    CustDetailCard,
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
          text: "고객 상세",
          disabled: true,
        },
      ],
    };
  },
};
</script>

<style>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
</style>
