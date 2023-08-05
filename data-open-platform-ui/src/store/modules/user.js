import {getInfo, login, logout} from '@/api/user'
import {getToken, removeToken, setToken} from '@/utils/auth'
import {md5Encrypt} from '@/utils/md5'
import {resetRouter} from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ROLES: (state, role) => {
    state.roles = role
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { email, password, codeToken, codeResult } = userInfo
    return new Promise((resolve, reject) => {
      login({ email: email.trim(), password: md5Encrypt(password.trim()).toUpperCase(), codeToken: codeToken.trim(), codeResult: codeResult.trim() }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data)
        setToken(data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { name, roleCode } = data
        commit('SET_NAME', name)
        commit('SET_ROLES', [roleCode])
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_NAME', '')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_NAME', '')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

