import { DataStructureRepositoryInstance } from "../../repository/Models/DataStructureRepository";
import { Dispatch } from "react";
import { PayloadAction } from "@reduxjs/toolkit";
import { ActionTypes } from "../../redux/Model/DataStructureReducer";



export class DataStructureService {
	private dispatch: Dispatch<PayloadAction<any>>
	private repository = DataStructureRepositoryInstance

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
				type: ActionTypes.DATA_STRUCTURE_GET_ALL,
				payload: res?.Some
			})
			return res
		})
	}

	public async get(uuid: string) {
		return await this.repository.get(uuid).then(res => {
			this.dispatch({
				type: ActionTypes.DATA_STRUCTURE_GET,
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



