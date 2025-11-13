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
const EditInformation = () => import ('@/views/layout/user/editInformation.vue') 

// 一级路由
const Login = () => import ('@/views/login')
const WeeklyReports = () => import ('@/views/weeklyReports')
const MoodOption = () => import ('@/views/moodOption')
const Log = () => import ('@/views/log')
const AI = () => import ('@/views/AI')
const WhiteNoise = () => import ('@/views/whiteNoise')
const OtherHomePage = () => import ('@/views/otherHomePage')
const MessageCenter = () => import ('@/views/messageCenter')
const NewFollow = () => import ('@/views/newFollow')
const Comment = () => import ('@/views/comment')
const LikeReceived = () => import ('@/views/likeReceived')
const MyAttention = () => import ('@/views/myAttention')
const PostNotes = () => import ('@/views/postNotes')
const State = () => import ('@/views/state')
const Meditation = () => import ('@/views/meditation')
const CountDown = () => import ('@/views/countDown')
const NoteDetail = () => import ('@/views/noteDetail')
const PrivateLetter = () => import ('@/views/privateLetter')
const WoodFish = () => import ('@/views/woodFish')
const MoreSet = () => import ('@/views/woodFish/moreSet')
const ClockIn = () => import ('@/views/clockIn')
const DiaryNoteBook = () => import ('@/views/diaryNoteBook')
const DiaryDetail = () => import ('@/views/diaryNoteBook/diaryDetail')
const BulletJournal = () => import ('@/views/bullet-journal')
const BulletJournalRecord = () => import ('@/views/bullet-journal/record.vue')
const EmotionalPuzzle = () => import ('@/views/emotional-puzzle/')
const PuzzleOption = () => import ('@/views/emotional-puzzle/puzzleOption.vue')
const PsychologicalTestIndex = () => import ('@/views/psychologicalTest')
const PsychologicalTest = () => import ('@/views/psychologicalTest/psychologicalTest.vue')
const HistoryTest = () => import ('@/views/psychologicalTest/historyTest.vue')
const TestResult = () => import ('@/views/psychologicalTest/testResult.vue')
const SetUp = () => import ('@/views/layout/user/setup.vue')
const Shop = () => import ('@/views/shop')

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
            { path: '/community/attention', component: Attention },
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
    { path: '/test', component: test },
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
    { path: '/state', component: State },
    { path: '/meditation', component: Meditation },
    { path: '/countDown', component: CountDown },
    { path: '/noteDetail/:id', component: NoteDetail },
    { path: '/privateLetter/:id', component: PrivateLetter },
    { path: '/woodFish', component: WoodFish },
    { path: '/woodFish/moreSet', component: MoreSet },
    { path: '/clockIn', component: ClockIn },
    { path: '/user/editInformation', component: EditInformation },
    { path: '/user/setup', component: SetUp },
    { path: '/diaryNoteBook', component: DiaryNoteBook },
    { path: '/diaryDetail/:season', component: DiaryDetail },
    { path: '/bulletJournal/:id', component: BulletJournal },
    { path: '/bulletJournalRecord', component: BulletJournalRecord },
    { path: '/emotionalPuzzle', component: EmotionalPuzzle },
    { path: '/puzzleOption', component: PuzzleOption },
    { path: '/psychologicalTestIndex', component: PsychologicalTestIndex },
    { path: '/psychologicalTest', component: PsychologicalTest },
    { path: '/historyTest', component: HistoryTest },
    { path: '/testResult', component: TestResult },
    { path: '/shop', component: Shop },
  ]
})

export default router
