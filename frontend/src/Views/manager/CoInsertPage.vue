<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객사 등록</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <CoInsertCard></CoInsertCard>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import CoInsertCard from "../../components/manager/CoInsertCard.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    CoInsertCard,
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
          text: "고객사 등록",
          disabled: true,
        },
      ],
    };
  },
};
</script>
<style></style>
