import { configureStore } from '@reduxjs/toolkit'
import { areas, searchTags } from './store/consultSearch'
import { stepNum } from './store/Reservation'
import { loginToken, loginId } from './store/UserInfo'
import storage from 'redux-persist/lib/storage'
import { combineReducers } from '@reduxjs/toolkit'
import { persistReducer } from 'redux-persist'
import thunk from 'redux-thunk'
import { selectedCsltInfo } from './store/consultantInfo'
import { csltList } from './store/consultantList'
import { csltTimes } from './store/consultantTimes'
import { pronWrong, pronCorrect } from './store/pron'
import { profileInfo, profileClick } from './store/profileInfo'
import { recogList, recogNum, recogNameNum } from './store/recog'
import mediaConfigReducer from './store/mediaConfig'
import sessionStorage from 'redux-persist/es/storage/session'



const reducers = combineReducers({
  areas : areas.reducer,
  searchTags : searchTags.reducer,
  stepNum : stepNum.reducer,
  loginToken : loginToken.reducer,
  loginId : loginId.reducer,
  selectedCsltInfo : selectedCsltInfo.reducer,
  csltList : csltList.reducer,
  csltTimes : csltTimes.reducer,
  pronWrong : pronWrong.reducer,
  pronCorrect : pronCorrect.reducer,
  profileInfo : profileInfo.reducer,
  profileClick : profileClick.reducer,
  recogList : recogList.reducer,
  recogNum : recogNum.reducer,
  recogNameNum : recogNameNum.reducer,
  mediaConfig : mediaConfigReducer,
})


const persistConfig = {
  key : "root",
  storage : sessionStorage,
  whitelist : ['loginToken', 'loginId', 'selectedCsltInfo', 'profileInfo', 'profileClick','mediaConfig']
}

const persistedReducer = persistReducer(persistConfig, reducers)

const store = configureStore({
  reducer: persistedReducer,
  devTools : process.env.NODE_ENV !== 'production',
  middleware: [thunk]
})

export default store