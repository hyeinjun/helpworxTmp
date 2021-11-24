<template>
  <div>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객사 상세</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>

          <CoDetailCard></CoDetailCard>
        </v-container>
      </v-main>
  </div>
</template>

<script>
import CoDetailCard from "../../components/manager/CoDetailCard";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    CoDetailCard,
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
          text: "고객사 조회",
          disabled: false,
          href: "/manager/co/list",
        },
        {
          text: "고객사 상세",
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
