<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객사 수정</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <CoModifyCard></CoModifyCard>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import CoModifyCard from "../../components/manager/CoModifyCard.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
export default {
  components: {
    CoModifyCard,
    BreadCrumbs,
  },
  data() {
    return {
      crumbs: [],
      path: "",
    };
  },
  created() {
    this.path = this.$route.params.coCd;
    this.crumbs = [
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
        disabled: false,
        href: "/manager/co/detail/" + this.path,
      },
      {
        text: "고객사 수정",
        disabled: true,
      },
    ];
  },
};
</script>

<style></style>
