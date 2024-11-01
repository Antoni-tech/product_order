import { PayloadAction, Reducer } from "@reduxjs/toolkit"
import { ActionsToolbar, Field } from "../../shared/entities/Connector/Connector";
import { IFieldRelation, IResultOfTestData, QualityRule, QuantityRule, Rule, RuleResponseFields, SingleRelation } from "../../shared/entities/Rule/Rule";

export type RuleState = {
	fieldList: Array<Field> | []
	fieldResponse: Field | null
	item: ActionsToolbar | null
	rule: Rule | null
	rules: {
		totalCount: number
		list: Array<Rule> | null
	} | null

	fullRule: boolean
	selectedJsonData: {
		type?: string,
		id: number,
		number: number,
		textValue: string,
		toIncidents: boolean,
		condition: string
		resultCondition?: string
	} | null
	selectedField: {
		name: string,
		id: number,
		isEdit?: boolean
		testValueJson: number | string
	}

	fieldsResponse: Array<RuleResponseFields> | null
	defaultFieldList: Array<number>
	fieldRelation: IFieldRelation | null
	valueToCondition: string | null
	isValueSelecting: boolean
	resultOfTestData: Array<IResultOfTestData> | IResultOfTestData | null
}


const State: RuleState = {
	item: null,
	fieldList: [],
	fieldResponse: null,
	rule: null,
	rules: null,

	fullRule: false,
	selectedJsonData: null,
	selectedField: {
		name: '',
		id: 0,
		isEdit: false,
		testValueJson: 0
	},
	fieldsResponse: null,
	fieldRelation: null,
	defaultFieldList: [],
	valueToCondition: null,
	isValueSelecting: false,
	resultOfTestData: null
}

export enum ActionTypes {
	RULE_ADD_ROW = "RULE_ADD_ROW",
	RULE_EVENT_ITEM = "RULE_EVENT_ITEM",
	RULE_GET = "RULE_GET",
	RULE_ALL = "RULE_ALL",
	RULE_DELETE = "RULE_DELETE",
	RULE_EDIT = "RULE_EDIT",

	RULE_FROM_MODEL = "RULE_FROM_MODEL",
	RULE_SELECT_JSON_DATA = "RULE_SELECT_JSON_DATA",
	RULE_ADD_JSON_DATA = "RULE_ADD_JSON_DATA",
	RULE_REMOVE_JSON_DATA = "RULE_REMOVE_JSON_DATA",
	RULE_CHANGE_RULETYPE = "RULE_CHANGE_RULETYPE",
	RULE_EDIT_JSON_DATA = "RULE_EDIT_JSON_DATA",

	RULE_ADD_FIELD = "RULE_ADD_FIELD",
	RULE_SELECT_FIELD = "RULE_SELECT_FIELD",
	RULE_REMOVE_FIELD = "RULE_REMOVE_FIELD",
	RULE_EDIT_FIELD = "RULE_EDIT_FIELD",

	RULE_GET_FIELD_RESPONSE = "RULE_GET_FIELD_RESPONSE",
	RULE_GET_FIELDS_RELATION = "RULE_GET_FIELDS_RELATION",
	RULE_SET_FIELDS_RELATION = "RULE_SET_FIELDS_RELATION",

	RULE_SELECT_VALUE_TO_CONDITION = "RULE_SELECT_VALUE_TO_CONDITION",
	RULE_GET_RESULT_OF_TESTDATA = "RULE_GET_RESULT_OF_TESTDATA"
}

export const RuleReducer: Reducer<RuleState, PayloadAction<any>> =
	(state = State, action: PayloadAction<any>): RuleState => {
		switch (action.type) {
			case ActionTypes.RULE_ADD_ROW:
				return state = { ...state, fieldResponse: action.payload, }
			case ActionTypes.RULE_EVENT_ITEM:
				return { ...state, item: action.payload };
			case ActionTypes.RULE_GET:

				return state = {
					...state,
					rule: action.payload,
					defaultFieldList: action.payload.fields ? action.payload.fields.map((field: {
						name: string,
						id: number,
						isEdit?: boolean
						testValueJson: number | string
					}) => field.id) : []
				}

			case ActionTypes.RULE_ALL:
				return state = { ...state, rules: action.payload }
			case ActionTypes.RULE_DELETE:
				return state = {
					...state, rules: state.rules ?
						{ ...state.rules, list: state.rules.list ? state.rules.list.filter(item => item.versionId !== action.payload) : null }

						: null
				}
			case ActionTypes.RULE_GET_FIELD_RESPONSE:
				return state = { ...state, fieldsResponse: action.payload }
			case ActionTypes.RULE_GET_FIELDS_RELATION: {
				return state = { ...state, fieldRelation: { sdvmodelStructId: action.payload.sdvmodelStructId, fieldRelationRequestSubDataDTOList: action.payload.data } }
			}

			case ActionTypes.RULE_EDIT:

				if (state && state.rule) {

					return {
						...state,
						rule: {
							...state.rule,
							[action.payload.key]: action.payload.value,
						}
					}
				}
				return {
					...state
				};



			case ActionTypes.RULE_ADD_FIELD:
				return {
					...state,
					rule: state.rule
						? {
							...state.rule,
							fields: state.rule.fields
								? [...state.rule.fields, action.payload]
								: [action.payload],
						}
						: null,
				};


			case ActionTypes.RULE_FROM_MODEL:
				return {
					...state,
					fullRule: action.payload
				};

			case ActionTypes.RULE_SELECT_JSON_DATA:
				return {
					...state,
					selectedJsonData: action.payload
				}

			case ActionTypes.RULE_ADD_JSON_DATA: {
				return {
					...state,
					rule: state.rule
						? {
							...state.rule,
							jsonData: state.rule.jsonData
								? [...state.rule.jsonData, action.payload]
								: [action.payload],
						}
						: null,
				};
			}
			case ActionTypes.RULE_REMOVE_JSON_DATA: {
				console.log(action.payload)

				return {
					...state,
					rule: state.rule
						? {
							...state.rule,
							jsonData: state.rule.jsonData
								? state.rule.jsonData.filter((item: QuantityRule | QualityRule) => {
									if ('id' in item) {
										// Объект типа QualityRule
										return item.id !== action.payload;
									} else {
										// Объект типа QuantityRule
										return true; // Оставить все элементы типа QualityRule
									}
								})
								: null,
						}
						: null,
				};
			}

			case ActionTypes.RULE_CHANGE_RULETYPE: {
				return {
					...state,
					rule: state.rule
						? {
							...state.rule,
							ruleType: action.payload
						}
						: null,
				}
			}



			case ActionTypes.RULE_EDIT_JSON_DATA: {
				// Обновление selectedJsonData
				const updatedSelectedJsonData = {
					...state.selectedJsonData,
					id: action.payload.id,
					condition: action.payload.newCondition,
					resultCondition: action.payload.newResultCondition,
					number: action.payload.number || 0,
					textValue: action.payload.textValue,
					toIncidents: action.payload.toIncidents,

					connectorOutputVersionId: action.payload.connectorOutputVersionId !== undefined ? action.payload.connectorOutputVersionId : null
				};

				console.log('payload', action.payload)

				// Обновление jsonData, если rule существует и является массивом
				let updatedJsonData = null;

				if (state.rule && Array.isArray(state.rule.jsonData)) {

					updatedJsonData = state.rule.jsonData.map((item: QuantityRule | QualityRule) => {

						if ('id' in item && item.id === updatedSelectedJsonData.id) {
							if (action.payload.isQuality === true) {
								console.log('Обновление элемента качества:', updatedSelectedJsonData);
								return {
									...item,
									number: updatedSelectedJsonData.number,
									condition: updatedSelectedJsonData.condition,
									toIncidents: updatedSelectedJsonData.toIncidents,
									textValue: updatedSelectedJsonData.textValue,
									connectorOutputVersionId: updatedSelectedJsonData.connectorOutputVersionId
								};
							}
							// else if (action.payload.isQuality === false) {
							// 	console.log('Обновление элемента количества:', item);
							// 	return {
							// 		// ...item,
							// 		condition: updatedSelectedJsonData.condition,
							// 		resultCondition: updatedSelectedJsonData.resultCondition,
							// 	};
							// }
						}

						else {
							if (action.payload.isQuality === false) {
								return {
									...item,
									condition: updatedSelectedJsonData.condition,
									resultCondition: updatedSelectedJsonData.resultCondition,
								}
							}
							if (action.payload.isQuality === true) {
								return {
									...item
								}
							}
						}
						return item;
					});
				}

				return {
					...state,
					selectedJsonData: updatedSelectedJsonData,
					rule: state.rule ? {
						...state.rule,
						jsonData: updatedJsonData// Использование updatedJsonData, если он не null, иначе сохранение существующего jsonData
					} : null,
				};
			}









			case ActionTypes.RULE_SELECT_FIELD: {
				return {
					...state, selectedField: action.payload
				}
			}

			case ActionTypes.RULE_REMOVE_FIELD: {
				return {
					...state,
					rule: state.rule
						? {
							...state.rule,
							fields: state.rule.fields.filter((item: any) => item.id !== action.payload)
						}
						: null,
				};
			}

			case ActionTypes.RULE_EDIT_FIELD: {
				if (!state.rule) {
					return state;
				}

				const updatedFields = state.rule.fields.map((item: any) => {
					if (item.id === action.payload.id) {
						return { ...item, name: action.payload.name, testValueJson: action.payload.testValueJson };
					} else {
						return item;
					}
				});

				return {
					...state,
					rule: {
						...state.rule,
						fields: updatedFields
					}
				};
			}

			case ActionTypes.RULE_SET_FIELDS_RELATION: {

				const updatedFieldRelation = state.fieldRelation?.fieldRelationRequestSubDataDTOList.map((item) => {
					if (item.varSummaryFieldId === action.payload.relation.varSummaryFieldId) {
						return action.payload.relation;
					} else {
						return item;
					}
				}) ?? [];

				if (!updatedFieldRelation.some(item => item.varSummaryFieldId === action.payload.relation.varSummaryFieldId)) {
					updatedFieldRelation.push(action.payload.relation);
				}

				return {
					...state,
					fieldRelation: {
						sdvmodelStructId: action.payload.sdvmodelStructId,
						fieldRelationRequestSubDataDTOList: updatedFieldRelation,
					}
				};


			}

			case ActionTypes.RULE_SELECT_VALUE_TO_CONDITION: {

				return {
					...state,
					isValueSelecting: action.payload.isValueSelecting,
					valueToCondition: action.payload.valueToCondition
				}
			}


			case ActionTypes.RULE_GET_RESULT_OF_TESTDATA: {

				return {
					...state,
					resultOfTestData: action.payload
				}
			}

		}
		return state
	}

