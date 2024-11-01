import { AxiosError, AxiosRequestConfig, AxiosResponse } from "axios"
import { ErrorGenerator } from "../shared/errors/CustomErrors"
import axios from 'axios';
import { authErrorHandler } from "../hooks/AuthHook";

export interface Requester {
	getRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>>
	postRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>>
	putRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>>
	uploadFile<T>(url: string, formData: FormData): Promise<AxiosResponse<T>>;
	deleteRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>>;

	showBaseURL: () => void
}

class Dao implements Requester {
	constructor() {
		console.log(window.location.host);
	}
	private baseURL: string = process.env.NODE_ENV === "development" ? "http://localhost:8080/" : `https://${window.location.host}:8080/`
	private basicAuth = 'Basic ' + btoa("test:test");

	public async getRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>> {
		return await axios({
			...config,
			method: "GET",
			url: this.baseURL + url,
			withCredentials: true,
			headers: {
				"X-Requested-With": "XMLHttpRequest",
				"Authorization": this.basicAuth
			}
		}).then(res => res).catch((err: AxiosError) => {
			const errorData = JSON.parse(err.request.response)
			if ([401, 403].includes(Number(errorData.status)) || Number(errorData.code) === 10110) {
				authErrorHandler.handleAuthChange(false)
			}
			throw new ErrorGenerator({ code: errorData.code, message: errorData.message })
		})
	}

	public async postRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>> {
		return await axios({
			...config,
			method: "POST",
			url: this.baseURL + url,
			withCredentials: true,
			headers: {
				"X-Requested-With": "XMLHttpRequest",
				"Authorization": this.basicAuth
			}
		}).then(res => res).catch((err: AxiosError) => {
			const errorData = JSON.parse(err.request.response)
			if ([401, 403].includes(Number(errorData.status)) || Number(errorData.code) === 10110) {
				authErrorHandler.handleAuthChange(false)
			}
			throw new ErrorGenerator(
				{
					code: errorData.code,
					message: Number(errorData.code) === 10109 ? (
						`Невозможно подключить, есть одинаковые услуги:
                        ${errorData.message.split(',')
							.map((item: string) => item.replace(/"/g, '').trim())
							.join('\n')} `)
						: errorData.message
				})
		})
	}

	public async uploadFile<T>(url: string, formData: FormData): Promise<AxiosResponse<T>> {
		try {
			const response = await axios.post(this.baseURL + url, formData, {
				withCredentials: true,
				headers: {
					"X-Requested-With": "XMLHttpRequest",
					"Authorization": this.basicAuth,
					"Content-Type": "multipart/form-data"
				}
			});

			return response;
		} catch (error) {

			if (axios.isAxiosError(error)) {
				const errorData = JSON.parse(error.request.response);

				if ([401, 403].includes(Number(errorData.status)) || Number(errorData.code) === 10110) {
					authErrorHandler.handleAuthChange(false);
				}
				throw new ErrorGenerator({ code: errorData.code, message: errorData.message })
			} else {
				throw error;
			}
		}
	}

	public async putRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>> {
		return await axios({
			...config,
			method: "PUT",
			url: this.baseURL + url,
			withCredentials: true,
			headers: {
				"X-Requested-With": "XMLHttpRequest",
				"Authorization": this.basicAuth
			}
		}).then(res => res).catch((err: AxiosError) => {
			const errorData = JSON.parse(err.request.response)
			if ([401, 403].includes(Number(errorData.status)) || Number(errorData.code) === 10110) {
				authErrorHandler.handleAuthChange(false)
			}
			throw new ErrorGenerator({ code: errorData.code, message: errorData.message })
		})
	}

	public async deleteRequest<T>(url: string, config: AxiosRequestConfig): Promise<AxiosResponse<T>> {
		try {
			const response = await axios({
				...config,
				method: "DELETE",
				url: this.baseURL + url,
				withCredentials: true,
				headers: {
					"X-Requested-With": "XMLHttpRequest",
					"Authorization": this.basicAuth
				}
			});

			return {
				data: {} as T,
				status: response.status,
				statusText: response.statusText,
				headers: response.headers,
				config: response.config
			};
		} catch (error) {
			if (axios.isAxiosError(error)) {
				const errorData = error.response?.data;
				if (errorData && ([401, 403].includes(Number(errorData.status)) || Number(errorData.code) === 10110)) {
					authErrorHandler.handleAuthChange(false);
				}
				throw new ErrorGenerator({ code: errorData?.code, message: errorData?.message });
			} else {
				throw error;
			}
		}
	}

	public showBaseURL() {
		return this.baseURL
	}

}

export const APIDao = new Dao()
