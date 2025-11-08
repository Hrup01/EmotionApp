<template>
  <div id="recordPage">
    <NavBar :title="navbarTitle" :margin-left="marginLeft"></NavBar>
    <div class="navbarRightPic">
        <img src="@/assets/image/更多1.png" alt="" class="more" @click="changeFlag">
        <div class="hiden" ref="hiden" v-show="flag">
            <div class="delete">
                <img src="@/assets/image/手账/删除本页.png" alt="">
                <p>删除手账</p>
            </div>
            <div class="rename" @click="changeName(0)">
                <img src="@/assets/image/手账记录/重命名.png" alt="">
                <p>重命名</p>
            </div>
        </div>
    </div>
    <div class="recordBook">
      <div class="item" v-for="item in recordList" :key="item.id"><img :src="item.url" alt=""></div>
    </div>
    <div class="message">
      <input type="text" class="name" ref="name" v-model="name"> 
      <div class="time">{{ time }}</div>
    </div>
    <div class="add-record"></div>
    <div class="show-option">
      <ul>
        <li v-for="item in recordList" :key="item.id" :class="{active: item.isCheck}"></li>
      </ul>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'recordPage',
    components: {
      NavBar
    },
    data () {
      return {
        navbarTitle: '手账记录',
        marginLeft: '115px',
        flag: false,
        recordList: [
          { id: 0, url: require('@/assets/image/手账记录/封面1.png'), name: '我的手账', time: '2025/10/23', isCheck: true },
          { id: 1, url: require('@/assets/image/手账记录/封面2.png'), name: '我的手账', time: '2025/10/23', isCheck: false },
          { id: 2, url: require('@/assets/image/手账记录/封面3.png'), name: '我的手账', time: '2025/10/23', isCheck: false },
          { id: 3, url: require('@/assets/image/手账记录/封面4.png'), name: '我的手账', time: '2025/10/23', isCheck: false },
          { id: 4, url: require('@/assets/image/手账记录/封面5.png'), name: '我的手账', time: '2025/10/23', isCheck: false },
        ],
        // 当前手账的名字和时间
        name: '',
        time: ''
      }
    },
    methods: {
      changeFlag () {
          this.flag = this.flag ? false : true
      },
      // 重命名
      changeName (id) {
        // input聚焦 --- 清空
        this.$refs.name.focus()
        this.name = ''
        this.$refs.name.addEventListener('blur', () => {
            this.name = this.name ? this.name.trim() : this.recordList[id].name
        })
        this.$refs.name.addEventListener('keyup', (e) => {
            // console.log(e)
            if (e.key === 'Enter') {
                // console.log(11)
                this.name = this.name ? this.name.trim() : this.recordList[id].name
                this.$refs.name.blur()
            }
        })
        this.flag = false
        // 将新名字存到数组中并存到仓库里
        this.recordList[id].name = this.name
        // this.$store.commit('bulletJournal/setName',)
      }
    },
    mounted () {
      // 初始化手账名称和创建时间
      // console.log(this.$store.state.name)
      if (!this.$store.state.name) {
        this.name = this.recordList[0].name
      } else {
        this.name = this.$store.state.name[0].name
      }
      this.time = this.recordList[0].time
    }
}
</script>

<style lang="less" scoped>
#recordPage {
  width: 390px;
  height: 852px;
  background: #feefce;
}
.navbarRightPic {
    .more {
        position: fixed;
        top: 50px;
        right: 22px;
        width: 24px;
    }
    .hiden {
        margin-top: 6px;
        padding-left: 16px;
        position: fixed;
        right: 29px;
        width: 104px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        height: 66px;
        border-radius: 8px;
        background: #FFE7B3;
        img {
            margin-right: 9px;
            width: 18.25px;
            height: 18.77px;
        }
        p {
            margin-top: 2px;
            color: #5e5e5e;;
            font-size: 10.95px;
            font-weight: 600;
        }
        .delete,
        .rename {
            display: flex;
        }
    }
}
.recordBook {
  margin-top: 63px;
  display: flex;
  overflow: hidden;
  height: 370px;
  .item {
    margin-left: 20px;
    flex-shrink: 0;
    width: 253px;
    height: 358px;
    border-radius: 8px;
    img {
      height: 100%;
    }
  }
}
.message {
  margin-top: 10px;
  margin-left: 23px;
  .name {
    border: 0;
    background: #feefce;
    color: #000000cc;
    font-size: 18px;
  }
  .time {
    color: #000000b3;
    font-size: 14px;
  }
}
.add-record {
  position: fixed;
  top: 571px;
  right: 12px;
  width: 50px;
  height: 50px;
  background: url('@/assets/image/手账记录/添加图片.png') no-repeat;
  background-size: cover;
}
.show-option {
  margin-top: 90px;
  ul {
    display: flex;
    justify-content: center;
    gap: 8px;
    li {
      width: 40px;
      height: 4px;
      background: #cccccc;
    }
    .active {
      background: #f4961e;
    }
  }
}
</style>