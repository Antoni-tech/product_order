import { Option } from "../../shared/utilities/OptionT";
import { APIDao, Requester } from "../APIRequester";
import { Connector } from "../../shared/entities/Connector/Connector";

class ConnectorRepository<T> {
	private dao: Requester

	constructor(dao: Requester) {
		this.dao = dao
	}

	public async createOrUpdate(data: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/connector/create-or-update`, { data })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async getAll(data: T, body: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/connector/all`, {
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
		return await this.dao.getRequest<Connector>(`core-service/api/connector/${uuid}`, {})
			.then(res => {
				const result: Option<Connector> = {
					Some: res?.data
				}
				return result
			})
	}


	public async duplicate(uuid: string): Promise<Option<T>> {
		return this.dao.postRequest<T>(`core-service/api/connector/duplicate`, { params: { id: uuid } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}
}


export const ConnectorRepositoryInstance = new ConnectorRepository<any>(APIDao)
