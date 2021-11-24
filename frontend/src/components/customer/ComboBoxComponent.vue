<template>
    <div>
        <v-select                
            :items="items"
            outlined
            v-model="selected"
            label="선택"
            single-line
            dense
        ></v-select>
  </div>
</template>

<script>
export default {
    props: {
        items: Array,
        firstSelectedItem: String
    },
    data() {
        return {
            selected: 0
        }
    },
    created() {
        this.selectItem();

        if(this.firstSelectedItem != undefined) {
            this.selected = this.firstSelectedItem;
        }
    },
    watch: {
        selected: function () {
            this.$emit('selected', this.selected);
        },
        items: function() {
            this.selectItem();
        }
    },
    methods: {
        selectItem() {
            this.selected = this.items[0];
            try {
                if(this.items[0].value != undefined) {
                    this.selected = this.items[0].value;
                }
            }catch(e) {
                console.log(e);
            }
        }
    }
}
</script>

<style>

</style>