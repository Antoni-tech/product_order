import { combineReducers, configureStore, PayloadAction, Store } from "@reduxjs/toolkit";
import { ModalReducer, ModalState } from "./Modal/ModalReducer";
import thunk from "redux-thunk";
import { AuthState, AuthReducer } from "./Auth/AuthReducer";
import { TypedUseSelectorHook, useSelector } from "react-redux";
import { BreadcrumbsState, BreadcrumbsReducer } from "./Breadcrumbs/BreadcrumbsReducer";
import { ConnectorInputReducer, ConnectorInputState } from "./Model/ConnectorInputReducer";
import { UserReducer, UserState } from "./Users/UserReducer";
import { ConnectorOutputReducer, ConnectorOutputState } from "./Model/ConnectorOuputReducer";
import { DataStructureReducer, DataStructureState } from "./Model/DataStructureReducer";
import { RuleReducer, RuleState } from "./Model/RuleReducer";
import { ModelReducer, ModelState } from "./Model/ModelReducer";
import { DHTMLXReducer, DHTMLXState } from "./DHTMLX/DHTMLReducer";


const reducer = combineReducers({

	ModalReducer,
	AuthReducer,
	ConnectorInputReducer,
	ConnectorOutputReducer,
	RuleReducer,
	ModelReducer,
	DataStructureReducer,
	UserReducer,
	BreadcrumbsReducer,
	DHTMLXReducer
})

type AppState = {
	ModalReducer: ModalState
	ModelReducer: ModelState
	AuthReducer: AuthState
	ConnectorInputReducer: ConnectorInputState
	ConnectorOutputReducer: ConnectorOutputState
	RuleReducer: RuleState
	DataStructureReducer: DataStructureState
	UserReducer: UserState
	BreadcrumbsReducer: BreadcrumbsState
	DHTMLXReducer: DHTMLXState
}

export const useAppSelector: TypedUseSelectorHook<AppState> = useSelector

export const store: Store<any, PayloadAction<any>> = configureStore({
	reducer,
	middleware: [thunk],
	devTools: true
})


export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
