<template>
  <div id="diaryNoteBook">
    <NavBar :is-diary-note-book="isDiaryNoteBook" :title="navbarTitle" :margin-left="marginLeft"></NavBar>
    <div class="time wrapper">
      <div class="prev" @click="prevYear"><img src="@/assets/image/日记本/左1.png" alt=""></div>
      <div class="year">{{ year }}</div>
      <div class="next" @click="nextYear"><img src="@/assets/image/日记本/右1.png" alt=""></div>
    </div>
    <div class="noteBook" @click="enterNoteBook">
      <img src="@/assets/image/日记本/夏.png" alt="" ref="fengmian">
      <div class="seasonTime">{{ currentYear }}/{{ month.firstMonth }} - {{ currentYear }}/{{ month.lastMonth }}</div>
    </div>
    <div class="season wrapper">
      <div class="item" ref="spring" @click="optionSeason(0)">春</div>
      <div class="item" ref="summer" @click="optionSeason(1)">夏</div>
      <div class="item" ref="autumn" @click="optionSeason(2)">秋</div>
      <div class="item" ref="winter" @click="optionSeason(3)">冬</div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';
export default {
    name: 'diaryNoteBook',
    components: {
      NavBar
    },
    data () {
      return {
        isDiaryNoteBook: true,
        navbarTitle: '日记本',
        marginLeft: '120px',
        // 当前的年份
        currentYear: '',
        // 选择的年份
        year: '',
        season: 'spring',
        // 一季度所包含的三个月份
        month: {
          firstMonth: '01',
          sencondMonth: '02',
          lastMonth: '03'
        }
      }
    },
    methods: {
      // 切换上一年
      prevYear () {
        this.year -= 1
      },
      // 切换下一年
      nextYear () {
        // 判断 -- 当前显示年份为今年 -- return
        if (this.year === this.currentYear) return
        this.year += 1
      },
      // 进入日记详情页面
      enterNoteBook () {
        // 传入选择的年份和季节
        this.$store.commit('diary/setSeason', this.season)
        this.$store.commit('diary/setYear', this.year)
        // 传入属于该季节的月份
        this.$store.commit('diary/setMonth', this.month)
        // 跳转页面
        this.$router.push(`/diaryDetail/${this.seasom}`)
      },
      // 选择季节
      optionSeason (num) {
        const season = document.querySelectorAll('#diaryNoteBook .season .item')
        // 改变日记本封面
        const fengmian = this.$refs.fengmian
        // 选中元素添加active类 循环去掉其他active
        season.forEach((item, index) => {
          if (index === 0) item.classList.remove('active-spring')
          if (index === 1) item.classList.remove('active-summer')
          if (index === 2) item.classList.remove('active-autumn')
          if (index === 3) item.classList.remove('active-winter')
        })
        if (num === 0) {
          season[num].classList.add('active-spring')
          fengmian.src = require('@/assets/image/日记本/春.png')
          this.season = 'spring'
          this.month = {
            firstMonth: '01',
            sencondMonth: '02',
            lastMonth: '03'
          }
        }
        if (num === 1) {
          season[num].classList.add('active-summer')
          fengmian.src = require('@/assets/image/日记本/夏.png')
          this.season = 'summer'
          this.month = {
            firstMonth: '04',
            sencondMonth: '05',
            lastMonth: '06'
          }
        }
        if (num === 2) {
          season[num].classList.add('active-autumn')
          fengmian.src = require('@/assets/image/日记本/秋.png')
          this.season = 'autumn'
          this.month = {
            firstMonth: '07',
            sencondMonth: '08',
            lastMonth: '09'
          }
        }
        if (num === 3) {
          season[num].classList.add('active-winter')
          fengmian.src = require('@/assets/image/日记本/冬.png')
          this.season = 'winter'
          this.month = {
            firstMonth: '10',
            sencondMonth: '11',
            lastMonth: '12'
          }
        }
      }
    },
    mounted () {
      // 获取今年年份
      const date = new Date()
      this.currentYear = date.getFullYear()
      this.year = this.currentYear
      // 从仓库中读取上一次选择了什么季节 -- 默认是秋
      const season = this.$store.state.diary.season
      if (!season) {
        this.$refs.spring.classList.add('active-spring')
        this.$refs.fengmian.src = require('@/assets/image/日记本/春.png')
      }
      if (season === 'spring') {
        this.$refs.spring.classList.add('active-spring')
        this.$refs.fengmian.src = require('@/assets/image/日记本/春.png')
      }
      if (season === 'summer') {
        this.$refs.summer.classList.add('active-summer')
        this.$refs.fengmian.src = require('@/assets/image/日记本/夏.png')
      }
      if (season === 'autumn') {
        this.$refs.autumn.classList.add('active-autumn')
        this.$refs.fengmian.src = require('@/assets/image/日记本/秋.png')
      }
      if (season === 'winter') {
        this.$refs.winter.classList.add('active-winter')
        this.$refs.fengmian.src = require('@/assets/image/日记本/冬.png')
      }
    }
}
</script>

<style lang="less" scoped>
#diaryNoteBook {
  width: 390px;
  height: 844px;
  background: #feefce;
  // background-image: url();
}
.wrapper {
  margin: 0 auto;
  width: 330px;
}
.time {
  margin-top: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 156px;
  height: 26px;
  // background-color: #fff;
  img {
    margin-top: 2px;
    width: 20px;
  }
}
.noteBook {
  position: relative;
  margin-top: 32px;
  margin-left: 33px;
  width: 330px;
  height: 466px;
  // border-radius: 20px;
  // background: url('@/assets/image/日记本/夏.png');
  background-size: cover;
  // box-shadow: 4px 3px 8px 0 #00000040;
  img {
    position: absolute;
    top: 0;
    width: 330px;
    z-index: 0;
  }
  .seasonTime {
    position: absolute;
    margin-top: 420px;
    margin-left: 91px;
    color: #00000080;
    z-index: 1;
  }
}
.season {
  margin-top: 38px;
  display: flex;
  justify-content: space-between;
  width: 220px;
  .item {
    width: 40px;
    height: 56px;
    text-align: center;
    line-height: 38px;
  }
  .active-spring {
    background: url('@/assets/image/日记本/春_组件.png') no-repeat;
    background-size: contain;
  }
  .active-summer {
    background: url('@/assets/image/日记本/夏_组件.png') no-repeat;
    background-size: contain;
  }
  .active-autumn {
    background: url('@/assets/image/日记本/秋_组件.png') no-repeat;
    background-size: contain;
  }
  .active-winter {
    background: url('@/assets/image/日记本/冬_组件.png') no-repeat;
    background-size: contain;
  }
}
</style>