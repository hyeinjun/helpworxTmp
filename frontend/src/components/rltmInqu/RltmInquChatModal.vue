<template>
  <v-card height="600" class="justify-center chatModalCard">
    <v-toolbar color="rgb(42, 46, 72)" dark>
      <v-toolbar-title>HelpWorX</v-toolbar-title>
      <v-spacer></v-spacer>

      <!-- 대화 종료 , 창 닫기 메뉴 -->
      <v-menu bottom left offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn dark icon v-bind="attrs" v-on="on" @click="getMenu">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item v-for="(item, i) in menuItems" :key="i">
            <v-btn
              color="rgb(42, 46, 72)"
              dark
              width="100px"
              @click="clickMenu(item)"
              >{{ item }}</v-btn
            >
          </v-list-item>
        </v-list>
      </v-menu>

      <v-btn large @click="closeModal" icon>
        <v-icon dark>
          mdi-close-circle
        </v-icon>
      </v-btn>
    </v-toolbar>

    <!-- v-virtual-scroll 사용 시 itemHeight 동적 지정 안되는 문제 -->
    <v-main class="mt-n12 ">
      <div class="scroll">
        <ul style="list-style:none;">
          <li v-for="item in logList" :key="item.inquLogId">
            <!-- 내가 입력한 채팅이면 오른쪽에 보여주기 -->
              <div v-if="ifMyLog(item)">
                <v-row class="myChatRow">
                  <v-spacer></v-spacer>
                  <v-col cols="7" md="7">
                      <v-card class="logBubble myLog" width="fit-content">
                        {{ item.inquDscr }}
                      <span class="myLogTim">{{ item.tim }}</span>
                      </v-card>
                  </v-col>
                  <v-col cols="2" md="2" class="mr-1">
                    <v-avatar color="rgb(42, 46, 72)" size="40" class="avatar">
                      <span class="white--text">{{
                        makeAvatarNm(item.usrNm)
                      }}</span>
                    </v-avatar>
                  </v-col>
                </v-row>
              </div>
              <!-- 상대방 채팅이면 왼쪽에 보여주기 -->
              <div v-else>
                <v-row class="yourChatRow">
                  <v-col cols="2">
                    <v-avatar color="rgb(42, 46, 72)" size="40" class="avatar">
                      <span class="white--text">{{
                        makeAvatarNm(item.usrNm)
                      }}</span>
                    </v-avatar>
                  </v-col>
                  <v-col>
                    <v-card class="logBubble yourLog" width="fit-content">
                      {{ item.inquDscr }}
                      <span class="yourLogTim">{{ item.tim }}</span>
                    </v-card>
                  </v-col>
                </v-row>
              </div>
          </li>
        </ul>

      </div>
    </v-main>

    <!-- 채팅이 종료된 상태가 아니면 입력창 보여주기 -->
    <v-footer absolute class="justify-center " v-if="!isEnd">
      <v-textarea
        class="mt-4"
        solo
        dense
        rows="1"
        auto-grow
        v-model="message"
        v-on:keyup.enter="sendMessage"
      ></v-textarea>
      <v-btn icon color="rgb(42, 46, 72)" class="mb-2" @click="sendMessage">
        <v-icon large class="ml-3">mdi-send</v-icon>
      </v-btn>
    </v-footer>

    <v-footer absolute class="justify-center " v-else>
      <v-card-title class="justify-center"
        >이미 종료된 문의입니다!</v-card-title
      >
    </v-footer>
  </v-card>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mdiCloseCircle } from "@mdi/js";
import { mdiSend } from "@mdi/js";
import { mdiDotsVertical } from "@mdi/js";
import API from "../../api/API";
export default {
  data() {
    return {
      mdiCloseCircle,
      mdiSend,
      mdiDotsVertical,

      dialog_chat: true,
      isEnd: false, // 종료 여부, 종료되면 채팅 입력 X
      message: "",
      inquUsrId: "",
      logList: [],
      menuItems: [],

      coCd: this.$store.state.usrInfo.coCd,
      usrId: this.$store.state.usrInfo.usrId,
      usrNm: this.$store.state.usrInfo.usrNm, // vuex에 저장된 usrNm을 사용

      tmpInquId: 0,
    };
  },
  props: ["inquId", "sysCd"],
  created() {
    this.resetChat();
    // 메뉴
    this.getMenu();
    this.connect();
  },
  methods: {
    closeModal() {
      this.dialog_chat = false;
      this.$emit("changeDialogChat", this.dialog_chat);
    },
    getMenu() {
      if (this.isEnd) this.menuItems = ["창 닫기"];
      else this.menuItems = ["문의 종료", "창 닫기"];
    },
    clickMenu(target) {
      if (target == "문의 종료") {
        this.finInqu();
      } else {
        this.closeModal();
      }
    },
    finInqu() {
      API.finInqu(
        this.$props.inquId,
        (res) => {
          this.isEnd = true;
          this.menuItems = ["창 닫기"];
        },
        (err) => {
          console.log(err);
        }
      );
    },
    resetChat() {
      (this.logList = []),
        API.getBeforeChat(
          "inquId=" + this.inquId,
          (res) => {
            this.logList = res.list;
            this.isEnd = res.state;
          },
          (err) => {
            console.log(err);
          }
        );

      API.getInquUsrIdByInquId(
        "inquId=" + this.$props.inquId,
        (res) => {
          this.inquUsrId = res;
        },
        (err) => {
          console.log(err);
        }
      );

      // 채팅 읽음 처리
      API.putLogStateCnfrm(
        this.$props.inquId,
        (res) => {
          console.log(res);
        },
        (err) => {
          console.log(err);
        }
      )
    },
    // 내가 작성한 채팅인지 상대가 작성한 채팅인지 파악
    ifMyLog(data) {
      let ret = false;
      // 유저인 경우
      if (data.inquUsrId == this.usrId) {
        if (this.usrId == data.sndr) ret = true;
      }
      // 운영자인 경우
      else {
        if (data.sndr != data.inquUsrId) ret = true;
      }
      return ret;
    },
    // 아바타에 보여줄 글자 자르기
    makeAvatarNm(nm) {
      return nm.substring(nm.length - 2, nm.length);
    },

    // 소켓
    sendMessage() {
      if (this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    send() {
      console.log("Send message:" + this.message);

      let data = {};
      data.inquId = this.$props.inquId;
      data.inquUsrId = this.inquUsrId; // 문의자
      data.inquDscr = this.message;
      data.tim = new Date();
      data.sndr = this.usrId; // 발신자
      data.coCd = this.coCd;

      if (this.stompClient && this.stompClient.connected) {
        // log db에 저장
        API.saveInquLog(
          data,
          (res) => {
            // 운영자에게 알림
            if (this.logList.length == 0) {
              this.stompClient.send("/app/alert", JSON.stringify(data), {});
            }
            // 메시지 전송
            this.stompClient.send("/app/receive", JSON.stringify(data), {});
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
    connect() {
      const serverURL = `${process.env.VUE_APP_API_URL}/socket`;
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("소켓 연결 성공", frame);
          this.stompClient.subscribe("/topic/log", (res) => {
            if (JSON.parse(res.body).inquId == this.inquId) {
              console.log("구독으로 받은 메시지 입니다.");
              console.log(res);
              // 채팅을 받아서 list에 바로 추가해서 보여줌
              this.logList.push(JSON.parse(res.body));
            }
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
    scrollToBottom() {
      var container = document.querySelector(".scroll");
      var height = container.scrollHeight;
      container.scrollTop = height;
    }
  },
  watch: {
    // inquId가 바뀌면 채팅창이 바뀌었다는 의미 => reset해줌
    inquId() {
        this.resetChat();
        this.scrollToBottom();
    },
  },
  mounted() {
    this.scrollToBottom();
  },
  updated() {
    this.scrollToBottom();
  }
};
</script>

<style scoped>
.logBubble {
  max-width: 225px;
  width: auto;
  padding: 10px;
}
.myLog {
  margin-right: -32px;
  background-color: rgb(245, 145, 30) !important;
  position: relative;
}
.yourLog {
  margin-left: -32px;
  background-color: rgb(0, 175, 230) !important;
}
.myLogTim {
  font-size: 5px;
  position: absolute;
  left: -30px;
  bottom: 2px;
}
.yourLogTim {
  font-size: 5px;
  position: absolute;
  right: -30px;
  bottom: 2px;
}
.chatModalCard {
  overflow: hidden;
}
.avatar {
  z-index: 1;
}
.myChatRow {
  text-align: -webkit-right;
}
.myChatRow {
  text-align: -webkit-right;
}
.scroll {
  height: 430px;
  overflow-y: scroll;
  overflow-x: hidden;

}
</style>
