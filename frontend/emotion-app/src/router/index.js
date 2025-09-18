import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/login'
import WeeklyReports from '@/views/weeklyReports'
import MoodOption from '@/views/moodOption'

Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    { path: '/login',component: Login },
    { path: '/weeklyReports',component: WeeklyReports },
    { path: '/moodOption',component: MoodOption }
  ]
})

export default router
