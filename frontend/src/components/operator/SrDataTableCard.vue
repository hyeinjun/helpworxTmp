<template>
    <v-card class="ma-2" outlined>
        <v-card-title>{{ cardTitle }}</v-card-title>
            <div class="d-flex justify-center">
                <v-card
                    min-width="100"
                    v-for="(value, name, index) in itemData"
                    :key = index
                    class="ma-2"
                    outlined
                >
                    <v-card-title class="justify-center">{{ itemLabels[index] }}</v-card-title>
                    <v-divider></v-divider>
                    <v-card-title class="justify-center">{{ value }}</v-card-title>
                </v-card>
            </div>
    </v-card>
</template>

<script>
export default {
    props: {
        cardTitle: String,
        itemLabels: Array,
        itemData: Object,
    },
}
</script>

<style scoped>
.v-card {
    border-color: rgba(133, 133, 133, 0.7);
}
.v-divider {
    border-color: rgba(133, 133, 133, 0.7);
}

</style>