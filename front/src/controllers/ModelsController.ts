import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { ModelService } from "../service/Model/ModelService";

export class ModelsController {
	private dispatch: Dispatch<PayloadAction<any>>
	private service: ModelService

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
		this.service = new ModelService(this.dispatch)
	}

	public async createOrUpdateModel(data: any) {
		return this.service.createOrUpdateModel(data).then(res => {
			return res
		})
	}

	public async getModelAll(data: any) {
		return this.service.getModelAll(data).then(res => {
			return res
		})
	}

	public async getModel(uuid: string) {
		return this.service.getModel(uuid).then(res => {
			return res
		})
	}

	public async deleteModel(uuid: string) {
		return this.service.deleteModel(uuid).then(res => {
			return res
		})
	}

	public async duplicateModel(uuid: string) {
		return this.service.duplicateModel(uuid).then(res => {
			return res
		})
	}

	public async transactionCounterRequest(summaryDataVersionIdModel: number, state: string,) {
		return this.service.transactionCounterRequest(summaryDataVersionIdModel, state).then(res => {
			return res
		})
	}

	public async getTransactionCounters(summaryDataVersionIdModel: string, summaryDataVersionId: string) {
		return this.service.getTransactionCounters(summaryDataVersionIdModel, summaryDataVersionId).then(res => {
			return res
		})
	}

	public async getAllIncidentsByModel(uuid: string, data: any) {
		return this.service.getAllIncidentsByModel(uuid, data).then(res => {
			return res
		})
	}

}
