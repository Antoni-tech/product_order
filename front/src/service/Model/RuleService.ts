import { Dispatch } from "react";
import { PayloadAction } from "@reduxjs/toolkit";
import { ActionTypes } from "../../redux/Model/RuleReducer";
import { RuleRepositoryInstance } from "../../repository/Models/RuleRepository";
import { IFieldRelation } from "../../shared/entities/Rule/Rule";


export class RuleService {
	private dispatch: Dispatch<PayloadAction<any>>
	private repository = RuleRepositoryInstance

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
	}

	public async createOrUpdate(data: any) {
		return await this.repository.createOrUpdate(data).then(res => {
			return res
		})
	}

	public async getAll(data: any, body: any) {
		return await this.repository.getAll(data, body).then(res => {
			this.dispatch({
				type: ActionTypes.RULE_ALL,
				payload: res?.Some
			})
			return res
		})
	}

	public async get(uuid: string) {
		return await this.repository.get(uuid).then(res => {
			this.dispatch({
				type: ActionTypes.RULE_GET,
				payload: res?.Some
			})
			return res
		})
	}

	public async duplicate(uuid: string) {
		return await this.repository.duplicate(uuid).then(res => {
			return res
		})
	}

	public async accessField(sdvModelId: string, sdvRuleId: string) {
		return await this.repository.accessField(sdvModelId, sdvRuleId).then(res => {
			this.dispatch({
				type: ActionTypes.RULE_GET_FIELD_RESPONSE,
				payload: res?.Some
			})
			return res
		})
	}

	public async fieldRelation(fieldRelation: IFieldRelation) {
		return await this.repository.fieldRelation(fieldRelation).then(res => {
			return res
		})
	}

	public async getFieldRelation(sDVModelId: number) {
		return await this.repository.getFieldRelation(sDVModelId).then(res => {

			this.dispatch({
				type: ActionTypes.RULE_GET_FIELDS_RELATION,
				payload: { data: res?.Some, sdvmodelStructId: sDVModelId }
			})
			return res
		})

	}

	public async qualityRuleEvaluate(ruleType: string, testData: any, conditions?: Array<{ number: number, condition: string }>) {
		return await this.repository.qualityRuleEvaluate(ruleType, testData, conditions).then(res => {

			this.dispatch({
				type: ActionTypes.RULE_GET_RESULT_OF_TESTDATA,
				payload: res?.Some
			})
			return res
		})

	}

	public async quantityRuleEvaluate(ruleType: string, testData: any, condition?: string, resCondition?: string) {
		return await this.repository.quantityRuleEvaluate(ruleType, testData, condition, resCondition).then(res => {

			this.dispatch({
				type: ActionTypes.RULE_GET_RESULT_OF_TESTDATA,
				payload: res?.Some
			})
			return res
		})

	}



}



