export type Field = {
	name: string;
	type: string;
	maxSize: number;
	allowEmpty: boolean;
	banSpecChar: boolean;
}

export type FieldVariable = {
	id: number,
	name: string;
	defaultField?: boolean
	testValueJson: string | number | null;
	key: React.ReactNode;
	children?: FieldVariable[];
}

export type ItemModel = {
	versionId: number,
	type: string,
	name: string,
	ruleType: string,
}

export enum RuleType {
	QUANTITY = 'QUANTITY',
	QUALITY = 'QUALITY'
}

export type QualityRule = {
	id: number;
	number: number;
	textValue: string;
	condition: string;
	toIncidents: boolean;
	sendConnectorId: string;
	connectorOutputVersionId: number;
	sendingId: number;
}

export type QuantityRule = {
	id: number;
	number?: number;
	textValue: string;
	condition: string;
	resultCondition: string;
	sendingId: number;
}

export type Rule = {
	summarySubType: string,
	summaryState: string,
	connectorId?: string,
	versionId?: number,
	userId?: string,
	state?: string,
	createDate?: string,
	updateDate?: string,
	type?: RuleType,
	name: string,
	userName: string,
	email?: string,
	numberOfData?: number;
	description?: string,
	saveResult: boolean,
	jsonData: Array<QuantityRule | QualityRule> | null;
	fields: Array<FieldVariable>,
	resultIncremental?: string,
	ruleType: RuleType,
}

export type RuleRequest = {
	extended: number | null
	summarySubType: string
	isCreate: boolean;
	type: string;
	isTemplate: boolean;
	name: string;
	description: string;
	saveResult: boolean;
	versionId: string;
	ruleType: string;
	queueNumber: number;
	resultIncremental: boolean;
	jsonData: Array<QuantityRule | QualityRule>;
	fields: Array<FieldVariable>;
}

export type ActionsToolbar = {
	actions: string;
	connector: Field;
}

interface Tag {
	[key: string]: string;
}

interface FieldOfRelation {
	id: number;
	name: string;
	defaultField: boolean;
	fieldType: string;
	maxArray: number;
	srcRelationId: number | null;
	children: null | Field[];
}

export type RuleResponseFields = {
	id: number;
	tags: Tag;
	type: string;
	fields: FieldOfRelation[];
}

export type SingleRelation = {
	varSummaryFieldId: number,
	srcSummaryFieldId: number,
	fieldRelationId?: number,
	varName: string,
	srcName: string
}

export type IFieldRelation = {
	sdvmodelStructId: number,
	fieldRelationRequestSubDataDTOList: Array<SingleRelation>
}

export type IResultOfTestData = {
	number: number;
	condition: string
	result: any
}