import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { ConnectorService } from "../service/Model/ConnectorService";

export class ConnectorController {
	private dispatch: Dispatch<PayloadAction<any>>
	private service: ConnectorService

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
		this.service = new ConnectorService(this.dispatch)
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

}
