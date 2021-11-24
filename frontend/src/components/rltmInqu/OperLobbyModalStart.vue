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
              <v-card height="40px" class="chatBtn">
                <v-row>
                  <v-spacer></v-spacer>

                  <v-col cols="8" class="sbjct">
                    {{item.sbjct.substring(0,25)}}
                  </v-col>
                  <v-col cols="3">
                    <v-btn @click="startAns(item.inquId)" class="ansBtn">답변하기</v-btn>
                  </v-col>
                </v-row>
                 
              </v-card>
                <div class="inquUsrId">문의자: {{item.inquUsrId}}</div>

            </v-list-item-action>
          </v-list-item>

        </template>
      </v-virtual-scroll>

    </v-main>
    <v-footer absolute class="justify-center" color="rgb(42, 46, 72)">
      <v-btn width="400" height="40" color="rgb(42, 46, 72, 0)" dark @click="cntuAnsModal">기존 문의 이어하기</v-btn>
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
    };
  },
  props: ["chatList"],
  created() {

  },
  methods: {
    closeModal() {
      this.dialog_lobby = false;
      this.$emit("changeDialogOperStart", false, null);
    },
    startAns(target) {
      console.log(target);
      API.startAns(
        "inquId="+target,
        (res) => {
          this.$emit("changeDialogOperStart", false, target);
        },
        (err) => {
          console.log(err);
        }
      )
      
    },
    cntuAnsModal () {
      this.$emit("changeDialogOperStart", true, null);
    }
  },
};
</script>

<style scoped>
.inquUsrId {
  font-size: 13px;
}
.chatBtn {
  width: -webkit-fill-available;
  background-color: #D7DDE2 !important;
}
.v-list-item-action {
  width: 390px;
}
.sbjct {
  align-self: center;
  text-align-last: left;
  margin-top: 5px;
}
.ansBtn {
  width: 75px;
  background-color: #2C5E9E !important;
  color: aliceblue !important;
  margin-top: 2px;
}
</style>