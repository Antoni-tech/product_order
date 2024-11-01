import { Option } from "../../shared/utilities/OptionT";
import { APIDao, Requester } from "../APIRequester";
import { Connector } from "../../shared/entities/Connector/Connector";
import { IFieldRelation } from "../../shared/entities/Rule/Rule";

class ConnectorOutputRepository<T> {
	private dao: Requester

	constructor(dao: Requester) {
		this.dao = dao
	}

	public async createOrUpdate(data: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/connector-output/create-or-update`, { data })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async getAll(data: T, body: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/connector-output/all`, {
			params: { ...data },
			data: { ...body }
		})
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async get(uuid: string): Promise<Option<Connector>> {
		return await this.dao.getRequest<Connector>(`core-service/api/connector-output/${uuid}`, {})
			.then(res => {
				const result: Option<Connector> = {
					Some: res?.data
				}
				return result
			})
	}

	public async duplicate(uuid: string): Promise<Option<T>> {
		return this.dao.postRequest<T>(`core-service/api/connector-output/duplicate`, { params: { id: uuid } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async accessField(sdvModelId: string, sdvConOutId: string): Promise<Option<T>> {
		return this.dao.getRequest<T>(`core-service/api/general/access-field/${sdvModelId}/${sdvConOutId}`, {})
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async fieldRelation(fieldRelation: IFieldRelation): Promise<Option<T>> {
		return this.dao.postRequest<T>(`core-service/api/v1/field-relations`, { data: fieldRelation })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async getFieldRelation(sDVModelId: number): Promise<Option<T>> {
		return this.dao.getRequest<T>(`core-service/api/v1/field-relations/${sDVModelId}`, {})
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async getUrl(uuid: string): Promise<Option<Connector>> {
		return await this.dao.getRequest<Connector>(`core-service/api/connector-output/getUrl/${uuid}`, {})
			.then(res => {
				const result: Option<Connector> = {
					Some: res?.data
				}
				return result
			})
	}

}

export const ConnectorOutputRepositoryInstance = new ConnectorOutputRepository<any>(APIDao)
