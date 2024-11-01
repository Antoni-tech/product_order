import { PayloadAction, Reducer } from "@reduxjs/toolkit"
import { SingleDataStructure, UsedField } from "../../shared/entities/DataStructure/DataStructure";

export type DataStructureState = {
	dataStructureList: {
		list: SingleDataStructure[] | null
		totalCount: number
	} | null
	dataStructure: SingleDataStructure | null;
	selectedField: UsedField | null
	defaultFields: Array<number> | null
}

const State: DataStructureState = {
	dataStructureList: null,
	dataStructure: null,
	selectedField: null,
	defaultFields: null
}

export enum ActionTypes {

	// GENERAL
	DATA_STRUCTURE_GET = "Data_STRUCTURE_GET",
	DATA_STRUCTURE_GET_ALL = "Data_STRUCTURE_GET_ALL",
	DATA_STRUCTURE_CHANGE_INFO = "DATA_STRUCTURE_CHANGE_INFO",

	//FIELDS_OPERATIONS
	DATA_STRUCTURE_FIELDS_ADD_ROW = "DATA_STRUCTURE_FIELDS_ADD_ROW",
	DATA_STRUCTURE_FIELDS_SELECT_ROW = "DATA_STRUCTURE_FIELDS_SELECT_ROW",
	DATA_STRUCTURE_FIELDS_REMOVE_ROW = "DATA_STRUCTURE_FIELDS_REMOVE_ROW",
	DATA_STRUCTURE_FIELDS_EDIT_ROW = "DATA_STRUCTURE_FIELDS_EDIT_ROW"

}

export const DataStructureReducer: Reducer<DataStructureState, PayloadAction<any>> =
	(state = State, action: PayloadAction<any>): DataStructureState => {
		switch (action.type) {

			case ActionTypes.DATA_STRUCTURE_GET:
				return state = {
					...state,
					dataStructure: action.payload,
					defaultFields: action.payload.fields.map((item: UsedField) => (item.id))
				}

			case ActionTypes.DATA_STRUCTURE_GET_ALL:
				return state = { ...state, dataStructureList: action.payload }

			case ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW:
				return state = {
					...state,
					selectedField: action.payload
				}

			case ActionTypes.DATA_STRUCTURE_CHANGE_INFO:
				return state = { ...state, dataStructure: state.dataStructure ? { ...state.dataStructure, [action.payload.key]: action.payload.value } : null }

			case ActionTypes.DATA_STRUCTURE_FIELDS_ADD_ROW:
				return state = {
					...state,
					dataStructure: state.dataStructure ?
						{ ...state.dataStructure, fields: [...state.dataStructure.fields, action.payload] } :
						null
				}

			case ActionTypes.DATA_STRUCTURE_FIELDS_REMOVE_ROW:
				return state = {
					...state,
					dataStructure: state.dataStructure ?
						{ ...state.dataStructure, fields: state.dataStructure.fields.filter(item => item.id !== action.payload) } :
						null
				}

			case ActionTypes.DATA_STRUCTURE_FIELDS_EDIT_ROW:
				return {
					...state,
					selectedField: state.selectedField ? { ...state.selectedField, [action.payload.key]: action.payload.value } : null,
					dataStructure: state.dataStructure ? {
						...state.dataStructure,
						fields: state.dataStructure.fields.map(item => {
							if (item.id === action.payload.id) {
								return { ...item, [action.payload.key]: action.payload.value };
							}
							return item;
						})
					} : null
				};




			default:
				return state
		}
	}

