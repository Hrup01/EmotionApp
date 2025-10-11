import Vue from 'vue'
import VueRouter from 'vue-router'

// 二、三级路由
import Layout from '@/views/layout'
import Home from '@/views/layout/home.vue'
import Heal from '@/views/layout/heal.vue'
import Community from '@/views/layout/community'
import Recommend from '@/views/layout/community/recommend.vue'
import Attention from '@/views/layout/community/attention.vue'
import User from '@/views/layout/user.vue'

// 一级路由
import Login from '@/views/login'
import WeeklyReports from '@/views/weeklyReports'
import MoodOption from '@/views/moodOption'
import Log from '@/views/log'
import AI from '@/views/AI'
import WhiteNoise from '@/views/whiteNoise'
import OtherHomePage from '@/views/otherHomePage'


Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    { path: '/', 
      component: Layout,
      children: [
        { path: '/home', component: Home },
        { path: '/heal', component: Heal },
        { path: '/community',
          component: Community,
          children: [
            { path: '/community/recommend', component: Recommend },
            { path: '/community/attention', component: Attention }
          ],
          redirect: '/community/recommend'
        },
        { path: '/user', component: User }
      ],
      redirect: '/home'
    },
    { path: '/login', component: Login },
    { path: '/weeklyReports', component: WeeklyReports },
    { path: '/moodOption', component: MoodOption },
    { path: '/log/:mood', component: Log },
    { path: '/ai', component: AI },
    { path: '/whiteNoise', component: WhiteNoise },
    { path: '/otherHomePage', component: OtherHomePage },
  ]
})

export default router
