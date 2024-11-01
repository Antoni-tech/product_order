import {Dispatch, PayloadAction} from "@reduxjs/toolkit"
import {Reducer} from "react"
import {BreadcrumbsData} from "../../shared/entities/Breadcrumbs/BreadCrumbsData"

export type BreadcrumbsState = {
    breadcrumbsData: Array<BreadcrumbsData>
}

const State: BreadcrumbsState = {
    breadcrumbsData: []
}

export enum BreadcrumbsTypes {
    BREADCRUMBS_SET_LINKS = "BREADCRUMBS_SET_LINKS"
}

export const BreadcrumbsReducer: Reducer<BreadcrumbsState, PayloadAction<Array<BreadcrumbsData>, BreadcrumbsTypes>> =
    (state = State, action: PayloadAction<Array<BreadcrumbsData>, BreadcrumbsTypes>): BreadcrumbsState => {
        switch (action.type) {
            case BreadcrumbsTypes.BREADCRUMBS_SET_LINKS:
                console.log("c:breadcrumbsData", state)
                console.log("n:breadcrumbsData", action.payload)
                console.log("s:breadcrumbsData", sessionStorage.getItem("breadcrumbsData"))

                return state = {
                    ...state,
                    breadcrumbsData: action.payload
                }
            default:
                return state
        }
    }

export const SetBreadcrumbsLinks = (dispatch: Dispatch<PayloadAction<Array<BreadcrumbsData>, BreadcrumbsTypes>>, payload: Array<BreadcrumbsData>) => {
    sessionStorage.setItem("breadcrumbsData", JSON.stringify(payload));
}

export const AddedBreadcrumbsLinks = (dispatch: Dispatch<PayloadAction<Array<BreadcrumbsData>, BreadcrumbsTypes>>, newLink: BreadcrumbsData) => {
    const savedBreadItems = sessionStorage.getItem("breadcrumbsData");
    let breadcrumbsData: Array<BreadcrumbsData> = [];
    if (savedBreadItems) {
        breadcrumbsData = JSON.parse(savedBreadItems);
    }
    const existingIndex = breadcrumbsData.findIndex((breadcrumb) => breadcrumb.link === newLink.link);
    if (existingIndex !== -1) {
        breadcrumbsData = breadcrumbsData.slice(0, existingIndex + 1); // Оставляем только элементы до и включая existingIndex
    } else {
        breadcrumbsData.push(newLink);
    }
    sessionStorage.setItem("breadcrumbsData", JSON.stringify(breadcrumbsData));
}

export const GetPreviousBreadcrumbsLink = () => {
    const savedBreadItems = sessionStorage.getItem("breadcrumbsData");
    let breadcrumbsData: Array<BreadcrumbsData> = [];
    if (savedBreadItems) {
        breadcrumbsData = JSON.parse(savedBreadItems);
    }
    if (breadcrumbsData.length > 1) {
        return breadcrumbsData[breadcrumbsData.length - 2];
    }
}