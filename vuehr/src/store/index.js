import {createStore} from 'vuex'

const store = createStore({
    state: {
        routes: [],
        currentHr: JSON.parse(window.sessionStorage.getItem("user"))
    },
    mutations: {
        INIT_CURRENTHR(state, hr) {
            state.currentHr = hr;
        },
        initRoutes(state, data) {
            state.routes = data;
        }
    }
})

export default store;
