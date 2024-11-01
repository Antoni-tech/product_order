import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { ConnectorServiceInput } from "../service/Model/ConnectorServiceInput";

export class ConnectorInputController {
	private dispatch: Dispatch<PayloadAction<any>>
	private service: ConnectorServiceInput

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
		this.service = new ConnectorServiceInput(this.dispatch)
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
