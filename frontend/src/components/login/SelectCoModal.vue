<template>
  <v-card class="text-center">
      <v-card-title class="headline grey lighten-2 justify-center my-3">
        접속하실 회사를 골라주세요
      </v-card-title>

      <v-container fluid>
        <v-row class="justify-center">
          <v-col class="d-flex my-5" md="7">
            <v-select v-model="selected" label="회사 선택" :items="coList">
            </v-select>
          </v-col>
        </v-row>
      </v-container>
    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn class="mr-2" color="rgba(42, 46, 72)" dark @click="OperOrMngMain">
        들어가기
      </v-btn><v-btn class="mr-2" color="rgba(42, 46, 72)" dark @click="cancelModal">
        취소
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      modal:true,
      selected: "",
    };
  },
  created() {},
  methods: {
    OperOrMngMain() {
      console.log(this.selected);
      this.$store.commit("usrInfo/changeCo", {
        coCd: this.selected,
        coNm: this.coList.find((e) => e.value == this.selected).text,
      });
      this.$store.commit("usrInfo/loginCnt");
      this.$router.push("/manager/main");
    },
    cancelModal(){
      this.modal=false;
      this.$emit('modalValue', this.modal);
    }
  },
  computed: {
    ...mapState({
      coList: (state) => state.CommonData.companyList,
    }),
  },
};
</script>

<style>
</style>
