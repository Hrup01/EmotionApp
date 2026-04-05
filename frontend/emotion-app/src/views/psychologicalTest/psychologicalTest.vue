<template>
  <div id="psychologicalTest">
    <NavBar></NavBar>
    <img src="@/assets/image/测评/狗.png" alt="" class="dog">
    <div class="test-num wrapper">
        <div class="progress">题 {{ progress }}/<span class="total">10</span></div>
        <div class="progress-bar">
            <div class="base item"></div>
            <div class="superstratum item" :style="{width: progress * 10 + '%'}"></div>
        </div>
    </div>
    <div class="subject wrapper">
        <div class="question">{{ question[progress - 1].content }}</div>
        <ul class="options">
            <li @click="selectOption('none', 1)">
                <img src="@/assets/image/测评/A.png" alt="">
                <div class="A item" ref="none">几乎没有</div>
            </li>
            <li @click="selectOption('sometimme', 2)">
                <img src="@/assets/image/测评/B.png" alt="" ref="B">
                <div class="B item" ref="sometimme">有时有</div>
            </li>
            <li @click="selectOption('offen', 3)">
                <img src="@/assets/image/测评/C.png" alt="" ref="C">
                <div class="C item" ref="offen">经常有</div>
            </li>
            <li @click="selectOption('all', 4)">
                <img src="@/assets/image/测评/D.png" alt="" ref="D">
                <div class="D item" ref="all">几乎总是有</div>
            </li>
        </ul>
    </div>
    <div class="bottom">
        <div class="prev item" @click="prevQuestion">上一题</div>
        <div class="next item" v-if="!isFinish" @click="nextQuestion">下一题</div>
        <div class="submit item" v-else @click="submitSelect">提交</div>
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
            progress: 1,
            // percent: 10,
            question: [
                { content: '1. 感到紧张、焦虑或烦躁', option: '' },
                { content: '2. 无法停止或控制担忧', option: '' },
                { content: '3. 对各种事情过度担忧', option: '' },
                { content: '4. 难以放松下来', option: '' },
                { content: '5. 容易感到疲倦', option: '' },
                { content: '6. 注意力难以集中', option: '' },
                { content: '7. 变得容易生气或易怒', option: '' },
                { content: '8. 感到害怕，好像有可怕的事情要发生', option: '' },
                { content: '9. 出现心跳加速、胸闷或呼吸急促（非生理疾病导致）', option: '' },
                { content: '10. 出现睡眠问题（如入睡困难、多梦、早醒）', option: '' },
            ],
            // 当前的选择
            currentOption: '',
            isFinish: false,
            score: 0
        }
    },
    methods: {
        // 点击上/下一题 -- 1.改问题 2.改进度 - 如果->10 则isFinish = true
        prevQuestion () {
            if (this.progress === 1) return
            if (this.isFinish) this.isFinish = false
            this.progress --
            // 从数组中得知选择
            const option = this.question[this.progress - 1].option
            // console.log(option)
            this.$refs.none.style.backgroundColor = '#fff'
            this.$refs.sometimme.style.backgroundColor = '#fff'
            this.$refs.offen.style.backgroundColor = '#fff'
            this.$refs.all.style.backgroundColor = '#fff'
            this.$refs[option].style.backgroundColor = '#F3CF80'
        },
        nextQuestion () {
            if (this.progress === 10) return
            if (this.progress === 9) this.isFinish = true
            this.progress ++
            this.$refs.none.style.backgroundColor = '#ffffffa8'
            this.$refs.sometimme.style.backgroundColor = '#ffffffa8'
            this.$refs.offen.style.backgroundColor = '#ffffffa8'
            this.$refs.all.style.backgroundColor = '#ffffffa8'
            // 从数组中得知选择
            // const option = this.question[this.progress].option
            // this.$refs[option].style.backgroundColor = '#F3CF80'
            // 将选择推到数组里
            this.question[this.progress - 2].option = this.currentOption
            // console.log(this.question)
        },
        // 选择选项
        selectOption (option, score) {
            // 变颜色
            // console.log(this.$refs[options])
            // 其他颜色变为白色
            // const options = document.querySelectorAll('#psychologicalTest .options li')
            // console.log(options)
            this.$refs.none.style.backgroundColor = '#ffffffa8'
            this.$refs.sometimme.style.backgroundColor = '#ffffffa8'
            this.$refs.offen.style.backgroundColor = '#ffffffa8'
            this.$refs.all.style.backgroundColor = '#ffffffa8'
            this.$refs[option].style.backgroundColor = '#F3CF80'
            this.currentOption = option
            this.score += score
        },
        // 提交选择
        submitSelect () {
            // post

            this.$router.push('testResult')
        }
    }
}
</script>

<style lang="less" scoped>
#psychologicalTest {
    width: 390px;
    height: 852px;
    background: linear-gradient(180deg, #faf4e6 0%, #f7e4b9 100%);
}
.wrapper {
    margin: 0 auto;
    width: 316px;
}
.dog {
    position: absolute;
    top: 142px;
    right: 33px;
    width: 102px;
}
.test-num {
    .progress {
        margin-top: 15px;
        color: #000000cc;
        font-size: 24px;
        .total {
            font-size: 18px;
        }
    }
    .progress-bar {
        position: relative;
        margin-top: 4px;
        width: 90px;
        height: 8px;
        .item {
            position: absolute;
            top: 0;
            height: 8px;
            border-radius: 8px;
        }
        .base {
            width: 90px;
            background: #ffffff;
        }
        .superstratum {
            background: #f3cf80;
            transition: all 0.3s;
        }
    }
}
.subject {
    margin-top: 60px;
    .question {
        margin: 0 auto;
        width: 222px;
        color: #000000c7;
        font-size: 18px;
    }
    .options {
        margin-top: 15px;
        li {
            position: relative;
            width: 320px;
            height: 94px;
            .item {
                // position: relative;
                margin-top: 24px;
                margin-left: 16px;
                padding: 10px 62px;
                width: 300px;
                height: 46px;
                border-radius: 60px;
                border: 1px solid #f3cf80;
                background: #ffffffa8;
                font-size: 18px;
            }
            img {
                position: absolute;
                top: 10px;
                left: -2px;
                width: 66px;
            }
        }
        
    }
}
.bottom {
    margin: 26px auto;
    display: flex;
    justify-content: space-between;
    width: 300px;
    .item {
        width: 145px;
        height: 50px;
        border-radius: 65px;
        border: 1px solid #f3cf80;
        background: #ffffffc7;
        line-height: 50px;
        text-align: center;
        font-size: 20px;
    }
    .next,
    .submit {
        background: #f3cf80;
    }
}
</style>