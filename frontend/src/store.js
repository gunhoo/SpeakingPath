import { configureStore } from '@reduxjs/toolkit'
import { areas, searchTags } from './store/consultSearch'
import { stepNum } from './store/Reservation'
import { loginToken, loginId } from './store/UserInfo'
import storage from 'redux-persist/lib/storage'
import { combineReducers } from '@reduxjs/toolkit'
import { persistReducer } from 'redux-persist'
import thunk from 'redux-thunk'
import { selectedCsltInfo } from './store/consultantInfo'



const reducers = combineReducers({
  areas : areas.reducer,
  searchTags : searchTags.reducer,
  stepNum : stepNum.reducer,
  loginToken : loginToken.reducer,
  loginId : loginId.reducer,
  selectedCsltInfo : selectedCsltInfo.reducer
})

const persistConfig = {
  key : "root",
  storage,
  whitelist : ['loginToken', 'loginId', 'selectedCsltInfo']
}

const persistedReducer = persistReducer(persistConfig, reducers)

const store = configureStore({
  reducer: persistedReducer,
  devTools : process.env.NODE_ENV !== 'production',
  middleware: [thunk]
})

export default store