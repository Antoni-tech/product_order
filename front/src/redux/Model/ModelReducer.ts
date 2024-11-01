import { PayloadAction, Reducer } from "@reduxjs/toolkit"
import { ActionsToolbar, Connector, Field, IIncidentList, IModel, IModelStructComponents, IncidentParams } from "../../shared/entities/Connector/Connector";

export type ModelState = {
	fieldList: Array<Field> | []
	fieldResponse: Field | null
	item: ActionsToolbar | null
	// model: Connector | null | IModel
	model: IModel | null
	models: {
		totalCount: number
		list: Array<Connector> | null
	} | null
	selectedModelField: IModelStructComponents | null
	CurrentModelTab: string
	incidentsList: {
		totalCount: number
		list: Array<IIncidentList> | null
	} | null
	incidentParams: IncidentParams
}

const State: ModelState = {
	item: null,
	fieldList: [],
	fieldResponse: null,
	model: null,
	models: null,
	CurrentModelTab: '1',
	selectedModelField: null,
	incidentParams: {
		page: 0,
		size: 10,
		userId: null,
		modelId: null
	},
	incidentsList: null
}

export enum ActionTypes {
	MODEL_ADD_ROW = "MODEL_ADD_ROW",
	MODEL_EVENT_ITEM = "MODEL_EVENT_ITEM",
	MODEL_GET = "MODEL_GET",
	MODEL_ALL = "MODEL_ALL",
	MODEL_DELETE = "MODEL_DELETE",

	MODEL_EDIT_MODEL_INFO = "MODEL_EDIT_MODEL_INFO",
	MODEL_SELECT_FIELD = "MODEL_SELECT_FIELD",
	MODEL_ADD_FIELDS = "MODEL_ADD_FIELDS",
	MODEL_REMOVE_FIELDS = "MODEL_REMOVE_FIELDS",

	MODEL_TRANSACTIONS_COUNTER = "MODEL_TRANSACTIONS_COUNTER",
	MODEL_EDIT_CHANGE_QUEUE_NUMBER = "MODEL_EDIT_CHANGE_QUEUE_NUMBER",
	MODEL_EDIT_LAUNCH_SECOND_STAGE = "MODEL_EDIT_LAUNCH_SECOND_STAGE",
	GENERAL_CHANGE_MODEL_TAB = "GENERAL_CHANGE_MODEL_TAB",

	MODEL_GET_ALL_INCIDENTS = "MODEL_GET_ALL_INCIDENTS",
	MODEL_SET_INCIDENT_PARAMS = "MODEL_SET_INCIDENT_PARAMS",
}

export const ModelReducer: Reducer<ModelState, PayloadAction<any>> =
	(state = State, action: PayloadAction<any>): ModelState => {
		switch (action.type) {
			case ActionTypes.MODEL_ADD_ROW:
				return state = { ...state, fieldResponse: action.payload, }
			case ActionTypes.MODEL_EVENT_ITEM:
				return { ...state, item: action.payload };
			case ActionTypes.MODEL_GET:
				return state = { ...state, model: action.payload }
			case ActionTypes.MODEL_ALL:
				return state = { ...state, models: action.payload }
			case ActionTypes.MODEL_GET_ALL_INCIDENTS:
				return state = { ...state, incidentsList: action.payload }
			case ActionTypes.GENERAL_CHANGE_MODEL_TAB:
				return state = {
					...state,
					CurrentModelTab: action.payload
				}
			case ActionTypes.MODEL_DELETE:
				return state = {
					...state,
					models: state.models ?
						{
							...state.models,
							list: state.models.list ? state.models.list.filter(item => item.versionId !== action.payload) : null
						}
						: null
				};
			case ActionTypes.MODEL_EDIT_MODEL_INFO:
				return state = {
					...state,
					model: state.model ? {
						...state.model,
						name: action.payload.name,
						description: action.payload.description,
						type: action.payload.type
					} : null
				}

			case ActionTypes.MODEL_ADD_FIELDS: {
				const newValue = action.payload.values;
				// const defaultValues = action.payload.defaultValues;

				return {
					...state,
					model: state.model ? {
						...state.model,
						modelStructComponents: state.model.modelStructComponents ?
							[
								...state.model.modelStructComponents, ...newValue
							] :
							[...newValue]
					} : null
				};
			}

			case ActionTypes.MODEL_REMOVE_FIELDS: {
				const idToRemove = action.payload.id;

				let updatedComponents = state.model?.modelStructComponents || [];


				return {
					...state,
					model: state.model ? {
						...state.model,
						modelStructComponents: updatedComponents.filter(component => component.modelComponentId !== idToRemove)
					} : null
				};
			}


			case ActionTypes.MODEL_SELECT_FIELD: {
				return {
					...state,
					selectedModelField: action.payload
				}
			}


			case ActionTypes.MODEL_TRANSACTIONS_COUNTER: {
				const updatedModel = state.model ? {
					...state.model,
					modelStructComponents: state.model.modelStructComponents.map(component => {
						if (component.modelComponentId === action.payload.id) {
							return {
								...component,
								counter: action.payload.counter,
								errors: action.payload.errors
							};
						}
						return component;
					})
				} : null;

				return {
					...state,
					model: updatedModel
				};
			}

			case ActionTypes.MODEL_EDIT_CHANGE_QUEUE_NUMBER: {
				const updatedModel = state.model ? {
					...state.model,
					modelStructComponents: state.model.modelStructComponents.map(component => {
						if (component.modelComponentId === action.payload.id) {
							return {
								...component,
								queueNumber: action.payload.queueNumber
							};
						}
						return component;
					})
				} : null;

				return {
					...state,
					model: updatedModel
				};
			}

			case ActionTypes.MODEL_EDIT_LAUNCH_SECOND_STAGE: {
				const updatedModel = state.model ? {
					...state.model,
					modelStructComponents: state.model.modelStructComponents.map(component => {
						if (component.modelComponentId === action.payload.id) {
							return {
								...component,
								launchSecondStage: action.payload.launchSecondStage
							};
						}
						return component;
					})
				} : null;

				return {
					...state,
					model: updatedModel
				};
			}

			case ActionTypes.MODEL_SET_INCIDENT_PARAMS: {
				return state = { ...state, incidentParams: action.payload }
			}

			default:
				return state
		}
	}

