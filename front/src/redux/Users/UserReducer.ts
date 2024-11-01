import {PayloadAction} from "@reduxjs/toolkit"
import {Reducer} from "react"
import {User} from "../../shared/entities/Users/User"

export type UserState = {
    userList: Array<User>
    user?: User,
    userListAll: { users: Array<User>, count: number }
}

const State: UserState = {
    userList: [],
    user: undefined,
    userListAll: {
        users: [],
        count: 0
    }
}

export enum ActionTypes {
    USER_LIST = "USER_LIST",
    USER_LIST_ALL = "USER_LIST_ALL",
    USER_GET = "USER_GET",
    USER_CREATE = "USER_CREATE",
    USER_UPDATE = "USER_UPDATE",
    CHANGE_PASSWORD = "CHANGE_PASSWORD"

}

export const UserReducer: Reducer<UserState, PayloadAction<any, string>> = (state = State, action: PayloadAction<any, string>): UserState => {
    switch (action.type) {
        case ActionTypes.USER_LIST:
            return state = {
                ...state,
                userList: action.payload,
            }
        case ActionTypes.USER_LIST_ALL:
            return state = {
                ...state,
                userListAll: action.payload,
            }
        case ActionTypes.USER_GET:
            return state = {
                ...state,
                user: action.payload
            }
        default:
            return state
    }
}


