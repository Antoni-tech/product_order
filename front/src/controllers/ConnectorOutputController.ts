import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { ConnectorServiceOutput } from "../service/Model/ConnectorServiceOutput";
import { IFieldRelation } from "../shared/entities/Rule/Rule";

export class ConnectorOutputController {
	private dispatch: Dispatch<PayloadAction<any>>
	private service: ConnectorServiceOutput

	constructor(dispatch: Dispatch<PayloadAction<any>>) {
		this.dispatch = dispatch
		this.service = new ConnectorServiceOutput(this.dispatch)
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

	public async accessField(sdvModelId: string, sdvConOutId: string) {
		return this.service.accessField(sdvModelId, sdvConOutId).then(res => {
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

	public async getUrl(uuid: string) {
		return this.service.getUrl(uuid).then(res => {
			return res
		})
	}
}
