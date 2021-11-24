<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row align="center">
            <v-col> <div class="pageTitle">SR 수정</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <ModifyForm />
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import ModifyForm from "../../components/customer/ModifyForm.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    ModifyForm,
    BreadCrumbs,
  },
  data() {
    return {
      srId: 0,
      crumbs: [

      ],
    };
  },
  mounted() {
    this.srId = this.$route.params.srId;
    this.crumbs = [
        {
          text: "HOME",
          disabled: false,
          href: "/customer/main",
        },
        {
          text: "SR 조회",
          disabled: false,
          href: "/customer/sr/list",
        },
        {
          text: "SR 상세",
          disabled: false,
          href: "/customer/sr/detail/"+Number(this.srId),
        },
        {
          text: "SR 수정",
          disabled: true,
        },
      ]
  }
};
</script>

<style>
.pageTitle {
  font-size: 1.5em;
  text-align: left;
}
</style>
