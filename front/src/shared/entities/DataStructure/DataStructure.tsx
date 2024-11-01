export type SingleDataStructure = {
	versionId: number
	type: string
	name: string
	description: string
	userName: string
	summaryState: string
	models: Array<UsedModel>
	connector: Array<{ name: string }>
	fields: Array<UsedField>
	inStructure: string
}

type UsedModel = {
	modelId: number
	modelName: string
	daysRemaining: number
	queueNumber: number
	resultIncremental: boolean | null
	launchSecondStage: boolean | null
}

export type UsedField = {
	id: number
	name: string
	defaultField: boolean
	fieldType: null | boolean
	maxArray: null | boolean
	srcRelationId: null | number
	testValueJson: number
	children: UsedField | null
}