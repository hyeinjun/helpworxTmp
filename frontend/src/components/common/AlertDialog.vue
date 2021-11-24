<template>
  <v-dialog :width="width" v-model="dialog">
    <v-card>
      <v-card-title class="headline grey lighten-2 justify-center my-3">
        {{dialogTitle}}
      </v-card-title>

      <v-container fluid>
        <v-row class="justify-center">
          <v-col class="my-10">
            {{dialogText}}
          </v-col>
        </v-row>
      </v-container>

      <v-divider></v-divider>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn class="mr-2" color="rgba(42, 46, 72)" dark @click="cancelButton">
          닫기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      dialog: true
    }
  },
  props: {
    width: {
      type: Number,
      default: 500
    },
    dialogTitle: String,
    dialogText: String
  },
  methods: {
    cancelButton() {
      this.$emit('close');
    }
  }
}
</script>

<style>

</style>