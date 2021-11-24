<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">SR 등록</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <SrRegistForm />
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import SrRegistForm from "../../components/customer/SrRegistForm.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";
export default {
  components: {
    SrRegistForm,

    BreadCrumbs,
  },
  data() {
    return {
      crumbs: [
        {
          text: "HOME",
          disabled: false,
          href: "/customer/main",
        },
        {
          text: "SR 등록",
          disabled: true,
        },
      ],
    };
  },
  created() {},
};
</script>

<style>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
</style>
