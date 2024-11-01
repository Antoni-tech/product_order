import { PayloadAction, Reducer } from "@reduxjs/toolkit"
import { ActionsToolbar, Connector } from "../../shared/entities/Connector/Connector";
import { IFieldRelation, RuleResponseFields } from "../../shared/entities/Rule/Rule";


interface DataType {
	id: number;
	name: string;
	fieldType: string;
	maxSize: number;
	key?: number;
	allowEmpty: boolean;
	description: string
	prohibitSpecCharacters: boolean;
	allowArray: boolean;
	maxArray: number;
	children: DataType[] | null;
}
export type ConnectorOutputState = {
	fieldList: Array<DataType> | []
	fieldResponse: DataType | null
	item: ActionsToolbar | null
	connector: Connector | null
	connectors: {
		totalCount: number
		list: Array<Connector> | null
	} | null
	defaultFields: Array<number> | []
	fieldsResponse: Array<RuleResponseFields> | null
	fieldRelation: IFieldRelation | null

	selectedField: {
		name: string,
		id: number,
	}
	url: string | null
}

const State: ConnectorOutputState = {
	item: null,
	fieldList: [],
	fieldResponse: null,
	connector: null,
	connectors: null,
	defaultFields: [],
	fieldsResponse: null,
	fieldRelation: null,
	selectedField: {
		name: '',
		id: 0,
	},
	url: null
}

export enum ActionTypes {
	CONNECTOR_OUT_ADD_ROW = "CONNECTOR_OUT_ADD_ROW",
	CONNECTOR_OUT_REMOVE_ROW = "CONNECTOR_OUT_REMOVE_ROW",
	CONNECTOR_OUT_EVENT_ITEM = "CONNECTOR_OUT_EVENT_ITEM",
	CONNECTOR_OUT_GET = "CONNECTOR_OUT_GET",
	CONNECTOR_OUT_ALL = "CONNECTOR_OUT_ALL",

	CONNECTOR_UPDATE_ROW = "CONNECTOR_UPDATE_ROW",
	CONNECTOR_PICK_ROW = "CONNECTOR_PICK_ROW",
	CONNECTOR_UNPICK_ROW = "CONNECTOR_UNPICK_ROW",
	CONNECTOR_OUT_CHANGE_POS = "CONNECTOR_OUT_CHANGE_POS",

	CONNECTOR_OUT_GET_FIELD_RESPONSE = "CONNECTOR_OUT_GET_FIELD_RESPONSE",
	CONNECTOR_OUT_GET_FIELDS_RELATION = "CONNECTOR_OUT_GET_FIELDS_RELATION",
	CONNECTOR_OUT_SET_FIELDS_RELATION = "CONNECTOR_OUT_SET_FIELDS_RELATION",

	CONNECTOR_OUT_SELECT_FIELD = "CONNECTOR_OUT_SELECT_FIELD",
	CONNECTOR_OUT_GET_URL = "CONNECTOR_OUT_GET_URL",
	CONNECTOR_OUT_SET_FIELDS_TABLE = "CONNECTOR_OUT_SET_FIELDS_TABLE"

}

const updateFields = (fields: DataType[], parentId: number | null, newField: DataType): DataType[] => {
	if (parentId === null) {
		// Добавляем новое поле на верхний уровень
		return [...fields, newField];
	}

	return fields.map(field => {
		if (field.id === parentId) {
			// Найден элемент с нужным parentId, добавляем newField в его children
			return { ...field, children: [...(field.children || []), newField] };
		} else if (field.children) {
			// Рекурсивно обходим все дочерние элементы
			return { ...field, children: updateFields(field.children, parentId, newField) };
		}
		return field;
	});
};

export const ConnectorOutputReducer: Reducer<ConnectorOutputState, PayloadAction<any>> =
	(state = State, action: PayloadAction<any>): ConnectorOutputState => {
		switch (action.type) {
			case ActionTypes.CONNECTOR_OUT_ADD_ROW:
				const newField = action.payload;
				const parentId = action.payload.fromParent;

				// Проверяем, что state.connector существует и имеет свойство fields
				if (state.connector?.fields) {
					const updatedFields = updateFields(state.connector.fields, parentId, newField);
					const updatedConnector = { ...state.connector, fields: updatedFields };
					return { ...state, connector: updatedConnector };
				}

				// Если state.connector не существует или не имеет свойства fields, возвращаем текущее состояние
				return state;

			case ActionTypes.CONNECTOR_OUT_EVENT_ITEM:
				return { ...state, item: action.payload };
			case ActionTypes.CONNECTOR_OUT_GET:
				function getAllIds(field: DataType) {
					const ids = [];

					if (field.children) {
						field.children.forEach((child) => {
							ids.push(...getAllIds(child));
						});
					}

					if (field.id) {
						ids.push(field.id);
					}

					return ids.flat();
				}


				return state = {
					...state,
					connector: action.payload,
					defaultFields: action.payload?.fields ? action.payload?.fields.map((field: DataType) => getAllIds(field)).flat() : []
				}
			case ActionTypes.CONNECTOR_OUT_ALL:
				return state = { ...state, connectors: action.payload }

			case ActionTypes.CONNECTOR_UPDATE_ROW:
				const keyToUpdate = action.payload.key;
				const newData = action.payload.data;

				if (state.fieldResponse && typeof state.fieldResponse === 'object' &&
					state.connector && state.connector.fields) {

					const updateFieldById = (fields: DataType[], id: number): DataType[] => {
						return fields.map((field) => {
							if (field.id === id) {
								// Update the field with matching id
								return { ...field, [keyToUpdate]: newData };
							}

							if (field.children) {
								// Recursively update the field in children
								const updatedChildren = updateFieldById(field.children, id);
								return { ...field, children: updatedChildren };
							}

							return field;
						});
					};

					const updatedFields = updateFieldById(state.connector.fields, state.fieldResponse.id);

					// Create a new connector object with the updated fields
					const updatedConnector = { ...state.connector, fields: updatedFields };

					// Update the fieldResponse
					const updatedFieldResponse = { ...state.fieldResponse, [keyToUpdate]: newData };

					return {
						...state,
						connector: updatedConnector,
						fieldResponse: updatedFieldResponse,
					};
				}

				return state;

			case ActionTypes.CONNECTOR_PICK_ROW:
				const pickedFieldId = action.payload;
				const findFieldById = (fields: DataType[], id: number): DataType | null => {
					for (const field of fields) {
						if (field.id === id) {
							return field;
						}
						if (field.children) {
							const nestedField = findFieldById(field.children, id);
							if (nestedField) {
								return nestedField;
							}
						}
					}
					return null;
				};

				const pickedField = state.connector?.fields ? findFieldById(state.connector.fields, pickedFieldId.field) : null;

				return { ...state, fieldResponse: pickedField || null };

			case ActionTypes.CONNECTOR_OUT_CHANGE_POS:

				const removeFields = (obj: any): any => {
					if (obj && typeof obj === 'object') {
						const { children, ...rest } = obj;
						let updatedChildren = null;
						if (children && Array.isArray(children)) {
							updatedChildren = children.map(removeFields);
							if (updatedChildren.some((child: any) => child !== null && child !== undefined)) {
								rest.fieldType = "OBJECT";
							}
						}
						return {
							...rest,
							children: updatedChildren !== null && updatedChildren.length > 0 ? updatedChildren : null
						};
					}
					return obj;
				};


				if (state.connector) {
					const updatedFields = action.payload.map(removeFields);
					const updatedConnector = { ...state.connector, fields: updatedFields };
					return { ...state, connector: updatedConnector };
				}

				return state;

			case ActionTypes.CONNECTOR_OUT_REMOVE_ROW:
				const idToRemove = state.fieldResponse?.id;

				if (idToRemove && state.connector && state.connector.fields) {
					const removeItemById = (fields: DataType[], id: number): DataType[] => {
						return fields.reduce((acc, field) => {
							if (field.id === id) {
								// Omit the item with matching id
								return acc;
							}

							if (field.children) {
								// Recursively remove the item from children
								const updatedChildren = removeItemById(field.children, id);
								acc.push({ ...field, children: updatedChildren });
							} else {
								acc.push(field);
							}

							return acc;
						}, [] as DataType[]);
					};

					const updatedFields = removeItemById(state.connector.fields, idToRemove);

					// Create a new connector object with the updated fields
					const updatedConnector = { ...state.connector, fields: updatedFields };

					return { ...state, connector: updatedConnector, fieldResponse: null, item: null };
				}

				return state;

			case ActionTypes.CONNECTOR_UNPICK_ROW:
				return { ...state, fieldResponse: null };

			case ActionTypes.CONNECTOR_OUT_GET_FIELD_RESPONSE:
				return state = { ...state, fieldsResponse: action.payload }

			case ActionTypes.CONNECTOR_OUT_GET_FIELDS_RELATION: {
				return state = { ...state, fieldRelation: { sdvmodelStructId: action.payload.sdvmodelStructId, fieldRelationRequestSubDataDTOList: action.payload.data } }
			}

			case ActionTypes.CONNECTOR_OUT_SET_FIELDS_RELATION: {

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

			case ActionTypes.CONNECTOR_OUT_SELECT_FIELD: {
				return {
					...state, selectedField: action.payload
				}
			}

			case ActionTypes.CONNECTOR_OUT_GET_URL: {
				return {
					...state, url: action.payload
				}
			}
			case ActionTypes.CONNECTOR_OUT_SET_FIELDS_TABLE:
				const updatedConnector = { ...state.connector, fields: action.payload };
				//@ts-ignore
				return state = { ...state, connector: updatedConnector }


			default:
				return state
		}
	}

