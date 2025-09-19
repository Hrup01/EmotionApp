import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/login'
import WeeklyReports from '@/views/weeklyReports'
import MoodOption from '@/views/moodOption'
import Log from '@/views/log'

Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    { path: '/', redirect: '/moodOption' },
    { path: '/login', component: Login },
    { path: '/weeklyReports', component: WeeklyReports },
    { path: '/moodOption', component: MoodOption },
    { path: '/log/:mood', component: Log }
  ]
})

export default router
