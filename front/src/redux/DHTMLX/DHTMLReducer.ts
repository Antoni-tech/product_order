import {PayloadAction} from "@reduxjs/toolkit";
import {Reducer} from "react";
import {ActionsType, ComponentType} from "../../common/constants";

export interface EventObject {
    componentType:ComponentType;
    actions: ActionsType;
    id: string | number;
    object: any;
}

export type DHTMLXState = {
    event: EventObject | null
}

const State: DHTMLXState = {
    event: null,
}

export enum DHTMLXTypes {
    EVENT_TOOLTIP = "EVENT_TOOLTIP",
}

export const DHTMLXReducer: Reducer<DHTMLXState, PayloadAction<any>> = (state = State, action: PayloadAction<any>):
    DHTMLXState => {
    switch (action.type) {
        case DHTMLXTypes.EVENT_TOOLTIP:
            return state = {
                ...state,
                event: action.payload
            }
        default:
            return state
    }
}
