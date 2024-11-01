import { PayloadAction } from "@reduxjs/toolkit";
import { Reducer } from "react";
import { AuthorizationData } from "../../shared/entities/Auth/AuthorizationData";

export type AuthState = {
    endUserData: AuthorizationData | null
}

const State: AuthState = {
    endUserData: null
}

export enum AuthTypes {
    AUTH_SET_TOKEN = "AUTH_SET_TOKEN",
    AUTH_DELETE_TOKEN = "AUTH_DELETE_TOKEN"
}

export const AuthReducer: Reducer<AuthState, PayloadAction<any, string>> = (state = State, action: PayloadAction<any, string>): AuthState => {
    switch (action.type) {
        case AuthTypes.AUTH_SET_TOKEN:
            return state = {
                ...state,
                endUserData: action.payload
            }

        case AuthTypes.AUTH_DELETE_TOKEN:
            return state = {
                ...state,
                endUserData: null
            }
        default:
            return state
    }
}

