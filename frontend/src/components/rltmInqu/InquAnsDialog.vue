<template>
  <v-alert
    color="rgb(42, 46, 72)"
    border="left"
    elevation="2"
    colored-border
    icon="mdi-comment-processing-outline"
    class="dialogCard"
  >
    <v-btn icon @click="clickNo" class="closeBtn">
      <v-icon dark>
        mdi-close
      </v-icon>
    </v-btn>
    <div @click="clickAns">
      <v-card-text class="sndrText">{{
        inquAnsModalData.sndr
      }}</v-card-text>
      <v-card-text class="cardText">{{ inquDscr }}</v-card-text>
      <div class="tim">{{inquAnsModalData.tim}}</div>
    </div>
  </v-alert>
</template>

<script>
import { mdiClose } from "@mdi/js";
import { mdiCommentProcessingOutline } from "@mdi/js";
export default {
  components: {
    // eslint-disable-next-line vue/no-unused-components
    mdiClose,
    // eslint-disable-next-line vue/no-unused-components
    mdiCommentProcessingOutline,
  },
  props: ["inquAnsModalData"],
  data() {
    return {
      inquId: "",
      inquDscr: "",
    };
  },
  created() {
    this.setting();
  },
  methods: {
    setting() {
      this.inquId = this.$props.inquAnsModalData.inquId;
      this.inquDscr = this.$props.inquAnsModalData.inquDscr;
      if (this.inquDscr.length > 35)
        this.inquDscr = this.inquDscr.substring(0, 35) + "...";
      // 5초 후 모달창 지우기
      setTimeout(() => {
        this.clickNo();
      }, 5000);
    },
    clickAns() {
      this.$emit("changeInquAnsModal", this.inquId);
    },

    clickNo() {
      this.$emit("changeInquAnsModal", null);
    },
  },
  watch: {
    inquAnsModalData() {
      this.setting();
    },
  },
};
</script>

<style scoped>
.cardText {
  text-align: start;
  margin-top: -25px;
  width: 330px !important;
}
.sndrText {
  text-align: start;
  margin-top: -10px;
  font-size: 1em !important;
  font-weight: bold !important;
}
.dialogCard {
  position: fixed !important;
  bottom: 5px;
  right: 5px;
  width: 400px !important;
  height: 100px !important;
  padding: 8px !important;
}
.closeBtn {
  float: right;
  align-items: baseline;
}
.tim{
  font-size: 10px;
  position: absolute;
  bottom: 10px;
  right: 30px;
}
</style>
