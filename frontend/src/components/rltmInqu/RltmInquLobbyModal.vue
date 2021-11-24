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
      <h4 class="mb-3">채팅을 시작하거나 이전 채팅을 선택해주세요😊</h4>
      <v-virtual-scroll
        :items="chatList"
        height="430"
        item-height="64"
        style="overflow-x:hidden;"
      >
        <template v-slot:default="{item}">
          <v-list-item :key="item.inquId">
            <v-list-item-action class="v-list-item-action mr-n1">
              <v-row style="width:inherit; text-align: justify;">
                <v-col >
                  <span class="sysSpan">[{{item.sysNm}}]</span>
                </v-col>
                <v-col>
                  <span v-if="dateDiff(item.date)" class="finNotfcText">문의가 끝났다면 종료해주세요!</span>
                </v-col>
                <v-spacer></v-spacer>
              </v-row>
              <v-badge       
                v-if="item.uncnfrmCnt>0"
                overlap
                class="badge"
                :content="item.uncnfrmCnt"
                color="red"
              >
                </v-badge>
                <v-btn 
                  v-if="dateDiff(item.date)" 
                  @click="ctnuInqu(item.inquId)" 
                  height="40px" width="360px" 
                  class="chatBtn justify-start borderRed">
                  {{item.dscr.substring(0,20)}}
                </v-btn>
                <v-btn v-else 
                  @click="ctnuInqu(item.inquId)" 
                  height="40px" width="360px" 
                  class="chatBtn justify-start">
                  {{item.dscr.substring(0,20)}}
                </v-btn>
                
              <div class="chatTim">{{item.tim}}</div>

            </v-list-item-action>
          </v-list-item>

        </template>
      </v-virtual-scroll>

    </v-main>
    <v-footer absolute class="justify-center" color="rgb(42, 46, 72)">
      <v-btn color="rgb(42, 46, 72)" width="100%" dark @click="startInqu">새로 문의 하기</v-btn>
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
      this.dialog_lobby = false;
      this.$emit("changeDialogInqu", this.dialog_lobby);
    },
    startInqu() {
      this.dialog_lobby = false;
      this.dialog_start = true;
      this.$emit("changeDialogInqu", this.dialog_lobby, this.dialog_start, null);
    },
    ctnuInqu(target) {
      this.dialog_lobby = false;
      this.dialog_start = false;
      this.$emit("changeDialogInqu", this.dialog_lobby, this.dialog_start, target);
    },
    dateDiff(tim) {
      var today = new Date();
      var chatTim = new Date(tim);
      var diff = (today.getTime() - chatTim.getTime()) / 1000;
      var diffDate = diff / (60*60*24);
      if(diffDate>7) {
        return true;
      } 
      return false;
    }
  },
};
</script>

<style scoped>
.chatTim {
  font-size: 10px;
}
.chatBtn {
  border: 1px solid rgb(42, 46, 72);
}
.v-list-item-action {
  width: 390px;
}
.sysSpan {
  font-size: 8px;
  font-weight: bolder;
  align-self: baseline;
}
.finNotfcText {
  font-size: 1px;
  color: red;

}
.borderRed {
  border: 1px solid red !important;
}
.badge {
  z-index: 1;
}
</style>