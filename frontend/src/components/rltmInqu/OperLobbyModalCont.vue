<template>
    <v-card height="600">
    <v-toolbar color="rgb(42, 46, 72)" dark>
      <v-toolbar-title>HelpWorX</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
          large
          @click="closeModal"
          icon
        >
          <v-icon dark>
            mdi-close-circle
          </v-icon>
        </v-btn>
    </v-toolbar>
    <v-main class="mt-n10">
      <h4 class="mb-3">답변할 문의를 선택해주세요😊</h4>
      <v-virtual-scroll
        :items="chatList"
        height="430"
        item-height="64"
      >
        <template v-slot:default="{item}">
          <v-list-item :key="item.inquId">
            <v-list-item-action class="v-list-item-action mr-n1">
              <span class="a">[{{item.sysNm}}]</span>
              <v-btn @click="ctnuInqu(item.inquId)" height="40px" class="chatBtn justify-start"  >
                 {{item.dscr.substring(0,25)}}
              </v-btn>
                <div class="chatTim">{{item.tim}}</div>

            </v-list-item-action>
          </v-list-item>

        </template>
      </v-virtual-scroll>

    </v-main>
    <v-footer absolute class="justify-center" color="rgb(42, 46, 72)">
      <v-btn width="400" height="40" color="rgb(42, 46, 72, 0)" dark @click="startAnsModal">신규 문의 답변하기</v-btn>
    </v-footer>
  </v-card>
</template>

<script>
import { mdiCloseCircle } from "@mdi/js";

export default {
  data() {
    return {
      mdiCloseCircle,
      dialog_lobby: true,
      dialog_start: false,

      usrId: this.$store.state.usrInfo.usrId,
      usrNm: this.$store.state.usrInfo.usrNm, // vuex에 저장된 usrNm을 사용
    };
  },
  props: ["chatList"],
  created() {

  },
  methods: {
    closeModal() {
      this.$emit("changeDialogOperCont", false, null);
    },
    startAnsModal() {
      this.$emit("changeDialogOperCont", true, null);
    },
    ctnuInqu(target) {
      this.$emit("changeDialogOperCont", false, target);
    },
  },
};
</script>

<style scoped>
.chatTim {
  font-size: 10px;
}
.chatBtn {
  width: -webkit-fill-available;
  background-color: #D7DDE2 !important;
}
.v-list-item-action {
  width: 390px;
}
.a {
  font-size: 8px;
  font-weight: bolder;
  align-self: baseline;
}
</style>