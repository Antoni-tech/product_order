import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { RuleService } from "../service/Model/RuleService";
import { IFieldRelation } from "../shared/entities/Rule/Rule";

export class RulesController {
	private dispatch: Dispatch<PayloadAction<any>>
	private service: RuleService

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
		this.service = new RuleService(this.dispatch)
	}

	public async createOrUpdate(data: any) {
		return this.service.createOrUpdate(data).then(res => {
			return res
		})
	}

	public async getAll(data: any, body: any) {
		return this.service.getAll(data, body).then(res => {
			return res
		})
	}

	public async get(uuid: string) {
		return this.service.get(uuid).then(res => {
			return res
		})
	}

	public async duplicate(uuid: string) {
		return this.service.duplicate(uuid).then(res => {
			return res
		})
	}

	public async accessField(sdvModelId: string, sdvRuleId: string) {
		return this.service.accessField(sdvModelId, sdvRuleId).then(res => {
			return res
		})
	}

	public async fieldRelation(fieldRelation: IFieldRelation) {
		return this.service.fieldRelation(fieldRelation).then(res => {
			return res
		})
	}

	public async getFieldRelation(sDVModelId: number) {
		return this.service.getFieldRelation(sDVModelId).then(res => {
			return res
		})
	}

	public async qualityRuleEvaluate(ruleType: string, testData: any, conditions?: Array<{ number: number, condition: string }>) {
		return this.service.qualityRuleEvaluate(ruleType, testData, conditions).then(res => {
			return res
		})
	}

	public async quantityRuleEvaluate(ruleType: string, testData: any, condition?: string, resCondition?: string) {
		return this.service.quantityRuleEvaluate(ruleType, testData, condition, resCondition).then(res => {
			return res
		})
	}
}
