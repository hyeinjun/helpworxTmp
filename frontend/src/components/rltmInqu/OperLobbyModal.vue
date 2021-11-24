<template>
  <v-card height="600">
    <v-toolbar color="rgb(42, 46, 72)" dark>
      <v-toolbar-title>HelpWorX</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn large @click="closeModal" icon>
        <v-icon dark>
          mdi-close-circle
        </v-icon>
      </v-btn>
    </v-toolbar>

    <v-main class="mt-n10" v-if="currentTab == '신규'">
      <h4 class="mb-3" v-if="newChatList.length > 0">
        답변할 문의를 선택해주세요😊
      </h4>
      <h4 class="mb-3" v-else>신규 문의가 없습니다😢</h4>
      <v-virtual-scroll
        v-if="newChatList.length > 0"
        :items="newChatList"
        height="430"
        item-height="64"
        style="overflow-x:hidden;"
      >
        <template v-slot:default="{ item }">
          <v-list-item :key="item.inquId">
            <v-list-item-action class="v-list-item-action mr-n1">
              <span class="sysSpan">[{{ item.inquUsrId }}]</span>
              <v-btn
                @click="startAns(item.inquId)"
                height="40px"
                class="chatBtn justify-start"
              >
                {{ item.sbjct.substring(0, 25) }}
              </v-btn>
              <div class="chatTim">{{ item.tim }}</div>
            </v-list-item-action>
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-main>

    <v-main class="mt-n10" v-else>
      <h4 class="mb-3" v-if="chatList.length > 0">
        이어할 문의를 선택해주세요😊
      </h4>
      <h4 class="mb-3" v-else>진행중인 문의가 없습니다.😢</h4>
      <v-virtual-scroll
        v-if="chatList.length > 0"
        :items="chatList"
        height="430"
        item-height="64"
        style="overflow-x:hidden;"
      >
        <template v-slot:default="{ item }">
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
                v-if="item.uncnfrmCnt > 0"
                overlap
                :content="item.uncnfrmCnt"
                class="badge"
                color="red"
              >
              </v-badge>
              <v-btn
                v-if="dateDiff(item.date)" 
                @click="ctnuInqu(item.inquId)"
                height="40px"
                width="360px"
                class="chatBtn justify-start borderRed"
              >
                {{ item.dscr.substring(0, 25) }}
              </v-btn>
              <v-btn
                v-else
                @click="ctnuInqu(item.inquId)"
                height="40px"
                width="360px"
                class="chatBtn justify-start"
              >
                {{ item.dscr.substring(0, 25) }}
              </v-btn>

              <div class="chatTim">{{ item.tim }}</div>
            </v-list-item-action>
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-main>
    <v-footer absolute class="justify-center" color="rgb(42, 46, 72)">
      <v-btn dark v-on:click="currentTab = '기존'" class="tabBtn newTab"
        >기존
        <v-badge 
          v-if="chatListCnt > 0" 
          overlap :content="chatListCnt"
          color="red"
          offset-x="-5"
          ></v-badge
      ></v-btn>

      <v-btn dark class="tabBtn" v-on:click="currentTab = '신규'"
        >신규
        <v-badge 
          v-if="newCnt > 0" 
          overlap :content="newCnt"
          class="tabBadge"
          color="red"
          offset-x="-5"
          ></v-badge>
      </v-btn>
    </v-footer>
  </v-card>
</template>

<script>
import { mdiCloseCircle } from "@mdi/js";
import API from "../../api/API";
export default {
  data() {
    return {
      mdiCloseCircle,
      dialog_lobby: true,
      dialog_start: false,

      usrId: this.$store.state.usrInfo.usrId,
      usrNm: this.$store.state.usrInfo.usrNm, // vuex에 저장된 usrNm을 사용

      currentTab: "기존",
      tabs: ["기존", "신규"],
    };
  },
  props: ["chatList", "newChatList", "newCnt", "chatListCnt"],
  created() {},
  methods: {
    closeModal() {
      this.$emit("changeDialogOper", null);
    },
    ctnuInqu(target) {
      this.$emit("changeDialogOper", target);
    },
    startAns(target) {
      API.startAns(
        "inquId=" + target,
        (res) => {
          this.$emit("changeDialogOper", target);
        },
        (err) => {
          console.log(err);
        }
      );
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
  width: -webkit-fill-available;
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
.inquUsrId {
  font-size: 13px;
}
.sbjct {
  align-self: center;
  text-align-last: left;
  margin-top: 5px;
}
.ansBtn {
  width: 75px;
  background-color: rgb(42, 46, 72) !important;
  color: aliceblue !important;
  margin-top: 2px;
}
.tabBtn {
  background-color: inherit !important;
  width: 180px;
  font-weight: bold;
  border-radius: 0;
}
.newTab {
  border-right: 1px solid white;
}
.badge {
  z-index: 1;
}
.finNotfcText {
  font-size: 1px;
  color: red;
}
.borderRed {
  border: 1px solid red !important;
}
</style>
