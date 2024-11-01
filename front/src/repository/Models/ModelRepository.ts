import { Option } from "../../shared/utilities/OptionT";
import { APIDao, Requester } from "../APIRequester";


class ModelRepository<T> {
	private dao: Requester

	constructor(dao: Requester) {
		this.dao = dao
	}

	public async createOrUpdate(data: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/model/create-or-update`, { data })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async getAll(data: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/model/all`, { params: { ...data }, data: {} })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async get(uuid: string): Promise<Option<T>> {
		return await this.dao.getRequest<T>(`core-service/api/model/${uuid}`, {})
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async delete(uuid: string): Promise<Option<T>> {
		return this.dao.deleteRequest<T>(`core-service/api/general`, { params: { id: uuid } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async duplicate(uuid: string): Promise<Option<T>> {
		return this.dao.postRequest<T>(`core-service/api/model/duplicate`, { params: { id: uuid } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async transactionCounterRequest(summaryDataVersionIdModel: number, state: string): Promise<Option<T>> {
		return this.dao.postRequest<T>(`core-service/api/v1/transaction-counter/${summaryDataVersionIdModel}/set-state-all`, { params: { state: state } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async getTransactionCounters(summaryDataVersionIdModel: string, summaryDataVersionId: string): Promise<Option<T>> {
		return await this.dao.getRequest<T>(`core-service/api/v1/transaction-counter/${summaryDataVersionIdModel}/${summaryDataVersionId}`, {})
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}



	public async getAllIncidentsByModel(uuid: string, data: T): Promise<Option<T>> {
		return await this.dao.getRequest<T>(`connector-handler-service/api/v1/incidents/all`, { params: { ...data } })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}
}

export const ModelRepositoryInstance = new ModelRepository<any>(APIDao)
