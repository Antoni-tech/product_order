import {PayloadAction} from "@reduxjs/toolkit";
import {Reducer} from "react";
import {CustomModalConfig} from "../../shared/entities/Modal/ModalConfig";
import {ComponentType} from "../../common/constants";

export type ModalState = {
    data: CustomModalConfig | null
    stateModel: ComponentType
}

const State: ModalState = {
    data: null,
    stateModel: ComponentType.Models
}

export enum ModalTypes {
    MODAL_ADD = "MODAL_ADD",
    MODAL_DELETE = "MODAL_DELETE",
    MODEL_ADD = "MODAL_ADD"
}

export const ModalReducer: Reducer<ModalState, PayloadAction<any>> = (state = State, action: PayloadAction<any>): ModalState => {
    switch (action.type) {
        case ModalTypes.MODAL_ADD:
            return state = {
                ...state,
                data: action.payload
            }
        case ModalTypes.MODAL_DELETE:
            return state = {
                ...state,
                data: action.payload
            }
        case ModalTypes.MODEL_ADD:
            return state = {
                ...state,
                stateModel: action.payload
            }
        default:
            return state
    }
}
