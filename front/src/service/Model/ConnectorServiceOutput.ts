import { Dispatch } from "react";
import { PayloadAction } from "@reduxjs/toolkit";
import { ActionTypes } from "../../redux/Model/ConnectorOuputReducer";
import { ConnectorOutputRepositoryInstance } from "../../repository/Models/ConnectorOutputRepository";
import { IFieldRelation } from "../../shared/entities/Rule/Rule";


export class ConnectorServiceOutput {
	private dispatch: Dispatch<PayloadAction<any>>
	private repository = ConnectorOutputRepositoryInstance

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
				type: ActionTypes.CONNECTOR_OUT_ALL,
				payload: res?.Some
			})
			return res
		})
	}

	public async get(uuid: string) {
		return await this.repository.get(uuid).then(res => {
			this.dispatch({
				type: ActionTypes.CONNECTOR_OUT_GET,
				payload: res?.Some
			})
			return res
		})
	}

	public async accessField(sdvModelId: string, sdvConOutId: string) {
		return await this.repository.accessField(sdvModelId, sdvConOutId).then(res => {
			this.dispatch({
				type: ActionTypes.CONNECTOR_OUT_GET_FIELD_RESPONSE,
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
				type: ActionTypes.CONNECTOR_OUT_GET_FIELDS_RELATION,
				payload: { data: res?.Some, sdvmodelStructId: sDVModelId }
			})
			return res
		})

	}

	public async getUrl(uuid: string) {
		return await this.repository.getUrl(uuid).then(res => {
			this.dispatch({
				type: ActionTypes.CONNECTOR_OUT_GET_URL,
				payload: res?.Some
			})
			return res
		})
	}
}





