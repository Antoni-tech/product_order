import { Option } from "../../shared/utilities/OptionT";
import { APIDao, Requester } from "../APIRequester";
import { Connector } from "../../shared/entities/Connector/Connector";
import { IFieldRelation } from "../../shared/entities/Rule/Rule";

class RuleRepository<T> {
	private dao: Requester

	constructor(dao: Requester) {
		this.dao = dao
	}

	public async createOrUpdate(data: T): Promise<Option<T>> {
		return await this.dao.postRequest<T>(`core-service/api/rule/create-or-update`, { data })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async getAll(data: T, body: any): Promise<Option<T>> {
		console.log("data:", data, "body", body)
		return await this.dao.postRequest<T>(`core-service/api/rule/all`, { params: { ...data }, data: { ...body } })
			.then(res => {
				const result: Option<T> = {
					Some: res?.data
				}
				return result
			})
	}

	public async get(uuid: string): Promise<Option<Connector>> {
		return await this.dao.getRequest<Connector>(`core-service/api/rule/${uuid}`, {})
			.then(res => {
				const result: Option<Connector> = {
					Some: res?.data
				}
				return result
			})
	}

	public async duplicate(uuid: string): Promise<Option<T>> {
		return this.dao.postRequest<T>(`core-service/api/rule/duplicate`, { params: { id: uuid } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}

	public async accessField(sdvModelId: string, sdvRuleId: string): Promise<Option<T>> {
		return this.dao.getRequest<T>(`core-service/api/general/access-field/${sdvModelId}/${sdvRuleId}`, {})
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


	public async qualityRuleEvaluate(ruleType: string, testData: any, conditions?: Array<{ number: number, condition: string }>): Promise<Option<T>> {


		return this.dao.postRequest<T>(`connector-handler-service/api/v1/rule-evaluate/${ruleType}`, { data: { testData, conditions } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})


	}

	public async quantityRuleEvaluate(ruleType: string, testData: any, condition?: string, resCondition?: string): Promise<Option<T>> {

		return this.dao.postRequest<T>(`connector-handler-service/api/v1/rule-evaluate/${ruleType}`, { data: { testData, condition, resCondition } })
			.then(res => {
				const result: Option<T> = {
					Some: res.data
				}
				return result
			})
	}
}





export const RuleRepositoryInstance = new RuleRepository<any>(APIDao)
