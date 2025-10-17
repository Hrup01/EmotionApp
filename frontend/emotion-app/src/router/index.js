import Vue from 'vue'
import VueRouter from 'vue-router'

// 二、三级路由
import Layout from '@/views/layout'
import Home from '@/views/layout/home.vue'
import Heal from '@/views/layout/heal.vue'

import Community from '@/views/layout/community'
import Recommend from '@/views/layout/community/recommend.vue'
import Attention from '@/views/layout/community/attention.vue'

import User from '@/views/layout/user/index.vue'
import Note from '@/views/layout/user/note.vue'
import Collect from '@/views/layout/user/collect.vue'

// 一级路由
import Login from '@/views/login'
import WeeklyReports from '@/views/weeklyReports'
import MoodOption from '@/views/moodOption'
import Log from '@/views/log'
import AI from '@/views/AI'
import WhiteNoise from '@/views/whiteNoise'
import OtherHomePage from '@/views/otherHomePage'
import MessageCenter from '@/views/messageCenter'
import NewFollow from '@/views/newFollow'
import Comment from '@/views/comment'
import LikeReceived from '@/views/likeReceived'
import MyAttention from '@/views/myAttention'
import PostNotes from '@/views/postNotes'
import MyState from '@/views/myState'
import Meditation from '@/views/meditation'
import CountDown from '@/views/countDown'
import NoteDetail from '@/views/noteDetail'
import PrivateLetter from '@/views/privateLetter'
import WoodFish from '@/views/woodFish'

import test from '@/views/test.vue'


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
        { path: '/user', 
          component: User,
          children: [
            { path: '/user/note', component: Note },
            { path: '/user/collect', component: Collect }
          ],
          redirect: '/user/collect'
        }
      ],
      redirect: '/home'
    },
    { path: '/login', component: Login },
    { path: '/weeklyReports', component: WeeklyReports },
    { path: '/moodOption', component: MoodOption },
    { path: '/log/:mood', component: Log },
    { path: '/ai', component: AI },
    { path: '/whiteNoise', component: WhiteNoise },
    { path: '/otherHomePage/:id', component: OtherHomePage },
    { path: '/messageCenter', component: MessageCenter },
    { path: '/newFollow', component: NewFollow },
    { path: '/comment', component: Comment },
    { path: '/likeReceived', component: LikeReceived },
    { path: '/myAttention', component: MyAttention },
    { path: '/postNotes', component: PostNotes },
    { path: '/myState', component: MyState },
    { path: '/meditation', component: Meditation },
    { path: '/countDown', component: CountDown },
    { path: '/noteDetail/:id', component: NoteDetail },
    { path: '/privateLetter/:id', component: PrivateLetter },
    { path: '/woodFish', component: WoodFish },
    { path: '/test', component: test },
  ]
})

export default router
