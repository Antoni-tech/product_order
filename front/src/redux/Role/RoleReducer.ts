import { PayloadAction } from "@reduxjs/toolkit"
import { Reducer } from "react"
import { RoleController } from "../../controllers/RoleController"
import { Privileges } from "../../shared/entities/Role/Privileges"
import { Role, RoleList } from "../../shared/entities/Role/Role"

export type RoleState = {
    roleList: Array<Role>
    privileges: Array<Privileges>
}

const State: RoleState = {
    roleList: [],
    privileges: []

}

export enum ActionTypes {
    ROLE_GET = "ROLE_GET",
    ROLE_UPDATE = "ROLE_UPDATE",
    ROLE_CREATE = "ROLE_CREATE",
    PRIVILEGES_GET = "PRIVILEGES_GET"

}

export const RoleReducer: Reducer<RoleState, PayloadAction<any, string>> = (state = State, action: PayloadAction<any, string>): RoleState => {
    switch (action.type) {
        case ActionTypes.ROLE_GET:
            return state = {
                ...state,
                roleList: action.payload
            }
        case ActionTypes.PRIVILEGES_GET:
            return state = {
                ...state,
                privileges: action.payload
            }

        default:
            return state
    }
}

