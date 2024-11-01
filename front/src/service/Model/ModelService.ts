import { Dispatch } from "react";
import { PayloadAction } from "@reduxjs/toolkit";
import { ModelRepositoryInstance } from "../../repository/Models/ModelRepository";
import { ActionTypes } from "../../redux/Model/ModelReducer";


export class ModelService {
	private dispatch: Dispatch<PayloadAction<any>>
	private repository = ModelRepositoryInstance

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
	}

	public async createOrUpdateModel(data: any) {
		return await this.repository.createOrUpdate(data).then(res => {
			return res
		})
	}

	public async getModelAll(data: any) {
		return await this.repository.getAll(data).then(res => {
			this.dispatch({
				type: ActionTypes.MODEL_ALL,
				payload: res?.Some
			})
			return res
		})
	}

	public async getModel(uuid: string) {
		return await this.repository.get(uuid).then(res => {
			this.dispatch({
				type: ActionTypes.MODEL_GET,
				payload: res?.Some
			})
			return res
		})
	}
	public async deleteModel(uuid: string) {
		return await this.repository.delete(uuid).then(res => {
			return res
		})
	}

	public async duplicateModel(uuid: string) {
		return await this.repository.duplicate(uuid).then(res => {
			return res
		})
	}

	public async transactionCounterRequest(summaryDataVersionIdModel: number, state: string) {
		return await this.repository.transactionCounterRequest(summaryDataVersionIdModel, state).then(res => {
			return res
		})
	}

	public async getTransactionCounters(summaryDataVersionIdModel: string, summaryDataVersionId: string) {
		return await this.repository.getTransactionCounters(summaryDataVersionIdModel, summaryDataVersionId).then(res => {
			this.dispatch({
				type: ActionTypes.MODEL_TRANSACTIONS_COUNTER,
				payload: res?.Some
			})
			return res
		})
	}



	public async getAllIncidentsByModel(uuid: string, data: any) {
		return await this.repository.getAllIncidentsByModel(uuid, data).then(res => {
			this.dispatch({
				type: ActionTypes.MODEL_GET_ALL_INCIDENTS,
				payload: res?.Some
			})
			return res
		})
	}
}



