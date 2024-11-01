import { Dispatch } from "react";
import { PayloadAction } from "@reduxjs/toolkit";
import { ActionTypes } from "../../redux/Model/ConnectorInputReducer";
import { ConnectorRepositoryInstance } from "../../repository/Models/ConnectorRepository";


export class ConnectorService {
	private dispatch: Dispatch<PayloadAction<any>>
	private repository = ConnectorRepositoryInstance

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
				type: ActionTypes.CONNECTOR_IN_ALL,
				payload: res?.Some
			})
			return res
		})
	}

	public async get(uuid: string) {
		return await this.repository.get(uuid).then(res => {
			this.dispatch({
				type: ActionTypes.CONNECTOR_IN_GET,
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
}



