<template>
  <div>
    <v-app>
      <v-main class="ma-3">
        <v-container fluid>
          <v-row>
            <v-col> <div class="pageTitle">고객 수정</div> </v-col>
            <v-spacer></v-spacer>
            <v-col>
              <BreadCrumbs :crumbs="crumbs" />
            </v-col>
          </v-row>
          <cust-modify-card :usrId="usrId" @close="goList" />
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import CustModifyCard from "../../components/manager/CustModifyCard.vue";
import BreadCrumbs from "../../components/common/BreadCrumbs.vue";

export default {
  components: {
    CustModifyCard,
    BreadCrumbs,
  },
  data() {
    return {
      crumbs: [],
      path: "",
      usrId: this.$route.params.usrId,
    };
  },
  created() {
    this.path = this.$route.params.usrId;
    this.crumbs = [
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
        disabled: false,
        href: "/manager/cust/detail/" + this.path,
      },
      {
        text: "고객 수정",
        disabled: true,
      },
    ];
  },
  methods: {
    goList(value) {
      console.log(value);
        this.$router.push("/manager/cust/detail/" + this.usrId);
    },
  },
};
</script>

<style></style>
