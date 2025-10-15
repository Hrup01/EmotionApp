<template>
    <div class="item">
        <ul>
            <li v-for="item in list" :key="item.id" @click="$emit('click')">
                <!-- <audio :src="item.audioUrl" :ref="item.ref" v-show="isWhiteNoise"></audio> -->
                <div class="pic"><img :src="item.picUrl" alt=""></div>
                <p>{{ item.text }}</p>
            </li>
        </ul>
    </div>
</template>

<script>

export default {
    props: ['list', 'isWhiteNoise']
}
</script>

<style lang="less" scoped>
.item {
    ul {
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        li {
            margin: 12px 0;
            padding-top: 20px;
            width: 79px;
            height: 96px;
            border-radius: 9px;
            border: 1px solid #ecd1cd;
            background: #ffffff;
            .pic {
                margin: 0 auto;
                display: flex;
                justify-content: center;
                align-items: center;
                width: 40px;
                height: 40px;
                img {
                    // display: block;
                    // margin: 0 auto;

                    object-fit: contain;
                    width: 32px;
                }
            }
            p {
                margin-top: 2px;
                text-align: center;
                color: #000000b3;
                font-size: 14px;
                font-weight: 600;
            }
        }
    }
}
</style>