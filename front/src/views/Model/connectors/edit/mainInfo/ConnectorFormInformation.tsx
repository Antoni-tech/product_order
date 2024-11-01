import { FC, ReactNode, useCallback, useEffect, useState } from 'react'
import { Flex, Input, Radio, Space, Spin, Switch, Table, Tooltip, Typography, Button, notification } from 'antd'
import { Connector } from '../../../../../shared/entities/Connector/Connector';
import { useFormikContext } from 'formik';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleInfo } from '@fortawesome/free-solid-svg-icons';
import { APIDao } from '../../../../../repository/APIRequester';
import { useParams } from 'react-router-dom';
import { useAppSelector } from '../../../../../redux/Store';
import dataSource from './mockTable.json'
import servSource from './mockServer.json'
import { useClipboard } from 'use-clipboard-copy';
import { CopyOutlined } from '@ant-design/icons';
import { ConnectorOutputController } from '../../../../../controllers/ConnectorOutputController';
import { useDispatch } from 'react-redux';
import { EnterOutlined } from '@ant-design/icons';

type ISingleField = {
	title: string;
	fullTitle?: string
	children: ReactNode
}


const SingleField: FC<ISingleField> = ({ title, children, fullTitle }) => {
	return (
		<Flex gap='middle' justify='flex-start' align='flex-start'>
			<Flex style={{ width: '25%' }} justify='flex-start' align='center' gap='middle'>
				<Typography.Title level={5} style={{ margin: 0 }}>{title}</Typography.Title>
				{fullTitle && <Tooltip title={fullTitle}>
					<FontAwesomeIcon icon={faCircleInfo} />
				</Tooltip>}
			</Flex>
			{children}
		</Flex>
	)
}



function ConnectorFormInformation({ connectorType, defaultInfo }: { connectorType: 'input' | 'output', defaultInfo?: Connector }) {

	const dispatch = useDispatch()
	const formik = useFormikContext()
	const dao = APIDao
	const { fromModel } = useParams()
	const modelSummaryDataId = useAppSelector(store => store.ModelReducer.model?.summaryDataId)
	const userId = useAppSelector(store => store.ModelReducer.model?.userId)
	const clipboard = useClipboard();
	const [api, contextHolder] = notification.useNotification();
	const originalLink = useAppSelector(state => state.ConnectorOutputReducer?.connector?.url)

	const [url, setUrl] = useState<string>()
	const ConnectorOutController = new ConnectorOutputController(dispatch)


	const cols = [
		{
			title: 'Name',
			dataIndex: 'name',
			key: 'name',
		},
		{
			title: 'description',
			dataIndex: 'description',
			key: 'description',
		},
		{
			title: 'dnsName',
			dataIndex: 'dnsName',
			key: 'dnsName',
		},
		{
			title: 'ip',
			dataIndex: 'ip',
			key: 'ip',
		},
		{
			title: 'confirmAuth',
			dataIndex: 'confirmAuth',
			key: 'confirmAuth',
			render: (text: boolean) => <span>{text === true ? 'Да' : 'Нет'}</span>,

		},
		{
			title: 'userLogin',
			dataIndex: 'userLogin',
			key: 'userLogin',
		},
		{
			title: 'authType',
			dataIndex: 'authType',
			key: 'authType',
		},
	]

	const outputServerCols = [
		{
			title: 'Наименование',
			dataIndex: 'name',
			key: 'name',
		},
		{
			title: 'Описание',
			dataIndex: 'description',
			key: 'description',
		},
		{
			title: 'DNS-имя',
			dataIndex: 'dns',
			key: 'dns',
		},
		{
			title: 'IP-адрес',
			dataIndex: 'ip',
			key: 'ip',
		},
		{
			title: 'Требуется аутентификация',
			dataIndex: 'auth',
			key: 'auth',
		},
		{
			title: 'Учетная запись',
			dataIndex: 'login',
			key: 'login',
		},
		{
			title: 'Тип аутентификации',
			dataIndex: 'authType',
			key: 'authType',
		},
	]


	if (defaultInfo) {

		const CopyLink =
			(urlType: string) => {
				const url = `${dao.showBaseURL()}connector-handler-service/api/v1/connector-handler/${modelSummaryDataId}/${summaryDataId}/${urlType}${userId}`
				clipboard.copy(url);
				api.info({
					message: `Успешно скопировано`,
					description: `Скопирована ссылка: ${url}`,
					placement: 'bottomRight',
				});
			}

		const editField = (e: React.ChangeEvent<HTMLInputElement>) => {
			formik.setFieldValue('name', e.target.value)
		}

		const editTextarea = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
			formik.setFieldValue('description', e.target.value)
		}

		const { versionId, type, name, description, summarySubType, url, connectorSubspecies, models, userName, summaryDataId } = defaultInfo as Connector

		return (
			<Space direction='vertical' style={{ width: '100%' }} size='middle'>

				{contextHolder}

				<SingleField title='Идентификатор'>
					<span>{versionId}</span>
				</SingleField>

				<SingleField title='Вид коннектора'>
					<span>


						{
							type === 'CONNECTOR_INPUT' && summarySubType === 'AWAITINGER' ? 'Ожидающий'
								:
								type === 'CONNECTOR_INPUT' && summarySubType === 'REQUESTER' ? 'Исходящий'
									:
									type === 'CONNECTOR_OUTPUT' && summarySubType === 'AWAITINGER' ? 'Отправка данных (Ожидающий)'
										:
										'Отправка данных'}
					</span>
				</SingleField>

				<SingleField title='Используется в моделях'>
					<span>{models.length > 0 ? models.map(model => model.modelName).join(', ') : 'Не используется ни в 1 модели'}</span>
				</SingleField>

				<SingleField title='Наименование'>
					<Input defaultValue={name} style={{ width: '40%', height: 32 }} onChange={editField} />
				</SingleField>

				<SingleField title='Описание'>
					<Input.TextArea
						defaultValue={description}
						rows={3}
						style={{ width: '40%', resize: 'none' }}
						onChange={editTextarea}
					/>
				</SingleField>

				<SingleField title='Создал'>
					<span>{userName}</span>
				</SingleField>

				<SingleField title='Совместное использование' fullTitle='Разрешить совместное использование коннектора 2-мя или более моделями'>
					<Switch defaultChecked />
				</SingleField>


				{connectorType === 'input' && <SingleField fullTitle='Разрешить входящие соединения с коннектором для систем и учетных записей' title='Входящие соединения'>
					<Table
						columns={cols}
						dataSource={dataSource}
						rowKey={record => record.id}
						size="small"
						style={{ width: '65%' }} />
				</SingleField>}

				{connectorType === 'output' && <SingleField fullTitle='Удаленный сервер и учетная запись' title='Удаленный сервер'>
					<Table
						columns={outputServerCols}
						dataSource={servSource}
						rowKey={record => record.id}
						size="small"
						style={{ width: '65%' }} />
				</SingleField>}

				{connectorType === 'output' && <SingleField title='URI (Path)'>
					<Flex vertical style={{ width: '30%' }} gap='middle'>
						<Flex justify='space-between'>
							<Input
								style={{ width: 304 }}
								onChange={e => {
									formik.setFieldValue('url', e.target.value)
									setUrl(e.target.value)
								}}
								defaultValue={url}
								// enterButton={<EnterOutlined />}
								placeholder='https://example.com'
							// onSearch={() => formik.setFieldValue('url', url)}
							/>
						</Flex>
						{originalLink && <span>Полный адрес: {originalLink}</span>}
					</Flex>
				</SingleField>}

				{fromModel && connectorType === 'input' && <SingleField fullTitle='Адрес коннектора для внешних систем' title='Адрес коннектора'>

					<Flex vertical style={{ width: '60%' }}>

						<Flex justify='space-between' align='center'>
							<Flex style={{ width: '85%' }} justify='space-between' align='center'>

								<Button icon={<CopyOutlined />} onClick={() => CopyLink('')} />
								<span style={{ width: '30%' }}>Основной</span>
								<span style={{ width: '55%', color: 'blue', overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>
									{dao.showBaseURL()}api/v1/connector-handler/{modelSummaryDataId}/{summaryDataId}/{userId}
								</span>
							</Flex>

							<Radio.Group value={2} style={{ width: '10%', display: 'flex' }}>
								<Radio value={1}>GET</Radio>
								<Radio value={2}>POST</Radio>
							</Radio.Group>
						</Flex>


						<Flex justify='space-between' align='center'>
							<Flex style={{ width: '85%' }} justify='space-between' align='center'>


								<Button icon={<CopyOutlined />} onClick={() => CopyLink('status/')} />

								<span style={{ width: '30%' }}>
									Для проверки состояния
								</span>
								<span
									style={{ width: '55%', color: 'blue', overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>
									{dao.showBaseURL()}api/v1/connector-handler/{modelSummaryDataId}/{summaryDataId}/status/{userId}
								</span>

							</Flex>
							<Radio.Group value={2} style={{ width: '10%', display: 'flex' }}>
								<Radio value={1}>GET</Radio>
								<Radio value={2}>POST</Radio>
							</Radio.Group>
						</Flex>


						<Flex justify='space-between' align='center'>
							<Flex style={{ width: '85%' }} justify='space-between' align='center'>

								<Button icon={<CopyOutlined />} onClick={() => CopyLink('result/')} />

								<span style={{ width: '30%' }}>Для получения результата</span>
								<span style={{ width: '55%', color: 'blue', overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>
									{dao.showBaseURL()}api/v1/connector-handler/{modelSummaryDataId}/{summaryDataId}/result/{userId}
								</span>
							</Flex>

							<Radio.Group value={2} style={{ width: '10%', display: 'flex' }}>
								<Radio value={1}>GET</Radio>
								<Radio value={2}>POST</Radio>
							</Radio.Group>
						</Flex>

					</Flex>
				</SingleField>}

			</Space>
		)
	}

	else return (<Spin />)


}

export default ConnectorFormInformation