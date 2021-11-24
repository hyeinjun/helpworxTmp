<template>
  <v-card class="dialogCard">
    <v-card-title>신규 문의에 답변하시겠습니까?</v-card-title>
    <v-card-text class="cardText mt-2"
      >제목: {{ newInquModalData.sbjct }}</v-card-text
    >
    <v-card-text class="cardText mb-n5"
      >문의자: {{ newInquModalData.inquUsrId }}</v-card-text
    >

    <v-card-actions>
      <v-spacer></v-spacer>

      <v-btn color="green darken-1" text @click="clickNo">No</v-btn>

      <v-btn color="red darken-1" text @click="clickYes">Yes</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: ["newInquModalData"],
  data() {
    return {
      inquId: "",
    };
  },
  methods: {
    setting() {
      this.inquId = this.$props.newInquModalData.inquId;
      setTimeout(() => {
        this.clickNo();
      }, 5000);
    },
    clickYes() {
      this.$emit("changeNewInquModal", this.inquId);
    },
    clickNo() {
      this.$emit("changeNewInquModal", null);
    },
  },
  watch: {
    // 5초 후 모달창 지우기
    newInquModalData() {
      this.setting();
    },
  },
  created() {
    this.setting();
  },
};
</script>

<style scoped>
.cardText {
  text-align: justify;
  font-size: 17px;
}
.dialogCard {
  position: fixed !important;
  bottom: 5px;
  right: 5px;
  width: 400px !important;
}
</style>
