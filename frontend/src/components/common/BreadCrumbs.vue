<template>
  <v-breadcrumbs :items="crumbs" style="justify-content:flex-end;">
    <template v-slot:divider>
      <v-icon>mdi-chevron-right</v-icon>
    </template>
  </v-breadcrumbs>
</template>

<script>
export default {
  props: ["crumbs"],
};
</script>
