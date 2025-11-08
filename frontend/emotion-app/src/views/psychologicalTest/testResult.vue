<template>
  <div id="testResult">
    <NavBar :title="title"></NavBar>
    <div class="body">
        <div class="score">
            <div class="beneath"></div>
            <div class="above">
                <img src="@/assets/image/测评/笑脸1.png" alt="">
                <p>{{ score }}</p>
                <img src="@/assets/image/测评/笑脸2.png" alt=""  class="state">
            </div>
        </div>
        <div class="result">{{ result }}</div>
        <img src="@/assets/image/测评/狗1.png" alt="" class="pet">
        <div class="recommend-function">
            <div class="title">功能推荐</div>
            <ul>
                <li @click="$router.push('/meditation')">
                    <p>冥想引导</p>
                    <img src="@/assets/image/测评/冥想.png" alt="">
                </li>
                <li @click="$router.push('/whiteNoise')">
                    <p>白噪音放松</p>
                    <img src="@/assets/image/测评/白噪音.png" alt="">
                </li>
                <li @click="$router.push('/woodFish')">
                    <p>木鱼舒缓</p>
                    <img src="@/assets/image/测评/木鱼.png" alt="">
                </li>
            </ul>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
  components: {
    NavBar
  },
  data () {
    return {
        title: '心理测评结果',
        score: 95,
        result: '焦虑情绪轻微，多为短期应激反应，通过休息、运动等方式可自行缓解，无需过度担心。'
    }
  }
}
</script>

<style lang="less" scoped>
#testResult {
    width: 390px;
    height: 852px;
    background: #faf4e6;
}
.body {
    position: relative;
    margin: 16px auto;
    width: 339px;
    height: 700px;
    border-radius: 20px;
    background: #fdf9f0;
    .beneath{
        position: absolute;
        top: 29px;
        right: 9px;
        width: 307px;
        height: 97px;
        border-radius: 30px;
        background: #d7d6ef;
    }
    .above {
        position: absolute;
        // padding: 19px 24px;
        display: flex;
        align-items: center;
        top: 24px;
        left: 16px;
        width: 307px;
        height: 97px;
        border-radius: 30px;
        background: #e6e5f0;
        font-size: 48px;
        img {
            margin-left: 24px;
            margin-right: 41px;
            width: 60px;
        }
        .state {
            position: absolute;
            top: 65px;
            right: 44px;
            width: 28px;
        }
    }
    .result {
        margin-top: 143px;
        margin-left: 27px;
        padding: 33px 29px;
        width: 285px;
        height: 140px;
        background: url('@/assets/image/测评/气泡框.png') no-repeat;
        background-size: contain;
    }
    .pet {
        margin-left: 46px;
        width: 118px;
    }
    .recommend-function {
        .title {
            margin-top: 35px;
            margin-left: 10px;
            font-size: 20px;
        }
        ul {
            margin: 14px auto;
            display: flex;
            justify-content: space-between;
            width: 319px;
            li {
                position: relative;
                padding: 14px;
                width: 101px;
                height: 130.07px;
                border-radius: 20px;
                color: #000000ad;
                font-size: 14px;
                img {
                    position: absolute;
                    right: 8px;
                    bottom: 10px;
                   
                }
            }
            li:nth-child(1) {
                background: #f6dde2;
                img {
                    width: 66px;
                }
            }
            li:nth-child(2) {
                background: #C7DEF0;
                img {
                    width: 71px;
                }
            }
            li:nth-child(3) {
                background: #DBE6DD;
                img {
                    width: 60px;
                }
            }
        }
    }
}
</style>