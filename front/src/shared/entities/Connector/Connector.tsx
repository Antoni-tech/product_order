interface DataType {
	id: number;
	name: string;
	description: string
	fieldType: string;
	maxSize: number;
	allowEmpty: boolean;
	prohibitSpecCharacters: boolean;
	allowArray: boolean;
	maxArray: number;
	children: DataType[] | null;

	newItem?: boolean
}

export type Field = {
	name: string;
	type: string;
	maxSize: number;
	allowEmpty: boolean;
	banSpecChar: boolean;
}


export type ItemModel = {
	versionId: number,
	type: string,
	name: string,
	modelName: string,
	ruleType: string,
}

export type Connector = {
	url: string,
	summarySubType: string,
	summaryState: string,
	summaryDataId: string,
	state: string,
	connectorId: string,
	userName: string,
	versionId: number,
	templateId: string,
	version: number,
	createDate: string,
	updateDate: string,
	type: string,
	name: string,
	email: string,
	description: string,
	connectorPurpose: string,
	connectorSubspecies: string,
	technology: string,
	dataFormat: string,
	// fields: Array<Field>
	fields: Array<DataType>
	models: Array<ItemModel>
	daysRemaining: string,
	inputDataType: string
}

export type IModel = {
	userName: string
	summaryDataId: string
	versionId: number
	userId: number
	state: string
	version: number
	isActive: boolean
	createDate: string
	numberOfData: null
	type: string
	name: string
	description: string
	modelStructComponents: IModelStructComponents[]
}

export type IModelStructComponents = {
	modelComponentId: number
	summaryState: string
	type: string
	subtype: string
	userName: string
	launchSecondStage: boolean
	queueNumber: null | number
	daysRemaining: number
	resultIncremental: boolean
	amountOfTransactions: number
	amountOfErrors: number
	state: string
	name: string
	// additional
	shared?: boolean
	dataType?: string
	dataFormat?: string
	isConfirmed?: boolean
}

export type ActionsToolbar = {
	actions: string;
	connector: Field;
}

export type IIncidentList = {
	id: string,
	number: number,
	textValue: string,
	// jsonData: Array<ISingleIncidentJsonData>
	jsonData: string
	summaryDataVersionModelId: number,
	summaryDataVersionConnectorId: number,
}

export type IncidentParams = {
	page: number,
	size: number,
	userId: number | null,
	modelId: number | null
}

export type ISingleIncidentJsonData = {
	condition: string,
	operation: string,
	data: any
}
