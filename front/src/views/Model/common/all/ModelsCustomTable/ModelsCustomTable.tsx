import React, { useEffect, useState } from 'react'
import { Button, Flex, Popconfirm, Table, TableProps, message } from 'antd'
import { ConnectorListCols, DataStructureListCols, modelListCols, RuleListCols } from './modelListTitles'
import { Rule } from '../../../../../shared/entities/Rule/Rule'
import { Connector } from '../../../../../shared/entities/Connector/Connector'
import styles from './modelTable.module.scss'
import { SingleDataStructure } from '../../../../../shared/entities/DataStructure/DataStructure'
import { RulesController } from '../../../../../controllers/RulesController'
import { useDispatch } from 'react-redux'
import { ModelsController } from '../../../../../controllers/ModelsController'
import { useAppSelector } from '../../../../../redux/Store'
import { ConnectorInputController } from '../../../../../controllers/ConnectorInputController'
import { ConnectorOutputController } from '../../../../../controllers/ConnectorOutputController'

import { RightOutlined, UpOutlined } from "@ant-design/icons";
import { DataStructureController } from '../../../../../controllers/DataStructureController'
import { ConnectorController } from '../../../../../controllers/ConnectorController'


interface ModelsCustomTableProps {
	dataSource: Connector[] | Rule[] | SingleDataStructure[];
	type: string;
	requestParams: TableParams
	pickIdFromTable?: (id: number) => void
}

interface TableParams {
	page: number,
	size: number,
	userId: number
}

const ModelsCustomTable: React.FC<ModelsCustomTableProps> = ({ dataSource, type, pickIdFromTable, requestParams }) => {
	const [selectedRowId, setSelectedRowId] = useState<number>()
	const ruleDataType = dataSource as Rule[]
	const dispatch = useDispatch()
	const userValue = useAppSelector(store => store.UserReducer?.user)

	const controller = new ModelsController(dispatch);
	const ruleController = new RulesController(dispatch)
	const connectorController = new ConnectorController(dispatch)
	const conOutController = new ConnectorOutputController(dispatch)
	const dataStructureController = new DataStructureController(dispatch);

	const totalModels = useAppSelector(store => store.ModelReducer?.models?.totalCount)
	const totalConnectors = useAppSelector(store => store.ConnectorInputReducer?.connectors?.totalCount)
	const totalRules = useAppSelector(store => store.RuleReducer?.rules?.totalCount)

	const [loading, setLoading] = useState(false)
	const [messageApi, contextHolder] = message.useMessage();

	const [tableParams, setTableParams] = useState<TableParams>(requestParams);


	const confirm = (e?: React.MouseEvent<HTMLElement>, id?: number) => {

		if (userValue && userValue.id) {

			// const reqConfig = {
			// 	params: {
			// 		page: 0,
			// 		size: 100,
			// 		userId: parseInt(userValue.id, 10),
			// 	}
			// }

			if (id)
				controller.deleteModel(String(id)).then(() =>
					messageApi.open({
						type: 'loading',
						content: `deleting the ${type}`,
					}).then(() =>
						type === 'RULE' ?
							ruleController.getAll(tableParams, null).then() :
							type === 'CONNECTOR' ?
								connectorController.getAll(tableParams, null).then() :
								type === 'MODEL' ?
									controller.getModelAll(tableParams).then() :
									type === 'DATA_STRUCTURE' ?
										dataStructureController.getAll(tableParams, null).then() :
										null
					)
				)

		}
	};


	const getRandomuserParams = (params: TableParams) => ({
		...params,
		page: params.page,
	});

	const fetchData = () => {
		setTableParams({
			...tableParams
		});
		setLoading(true);

		type === 'RULE' ?
			ruleController.getAll(tableParams, getRandomuserParams(tableParams)).then(() => {
				setLoading(false)
				setTableParams({
					...tableParams,
				})
			}) :
			type === 'CONNECTOR' ?
				connectorController.getAll(tableParams, getRandomuserParams(tableParams)).then(() => {
					setLoading(false)
					setTableParams({
						...tableParams,
					})
				}) :
				type === 'MODEL' ?
					controller.getModelAll(tableParams).then(() => {
						setLoading(false)
						setTableParams({
							...tableParams,
						})
					}
					)
					:
					dataStructureController.getAll(tableParams, getRandomuserParams(tableParams)).then(() => {
						setLoading(false)
						setTableParams({
							...tableParams,
						})
					})

	};

	const handleTableChange: TableProps['onChange'] = (pagination, filters, sorter) => {
		const newPage = pagination.current ? pagination.current - 1 : 0;
		const newPageSize = Number(pagination.pageSize);

		if (newPage >= 0 && (newPage !== tableParams.page || newPageSize !== tableParams.size)) {
			setTableParams({
				...tableParams,
				page: newPage,
				size: newPageSize,
			});
		}
	};

	const cancel = (e?: React.MouseEvent<HTMLElement>) => {
		console.log(e);

	};

	useEffect(() => {
		if (type) {
			fetchData();
			console.log('@@', dataSource, '| req', requestParams)
		}
	}, [JSON.stringify(tableParams), type]);

	if (type === 'RULE') {
		return (
			<Table
				style={{ width: '100%' }}
				columns={RuleListCols}
				size="small"
				dataSource={ruleDataType}
				rowKey={row => row.versionId as number}
				onChange={handleTableChange}
				loading={loading}
				pagination={{ position: ['topRight'], current: tableParams.page + 1, pageSize: tableParams.size, total: totalRules }}
				onRow={(record) => {
					return {
						onClick: () => {
							setSelectedRowId(record.versionId);
							if (pickIdFromTable)
								pickIdFromTable(record.versionId as number)
						},
					};
				}
				}
				rowClassName={(record) => (record.versionId === selectedRowId ? styles.picked : styles.default)}
				expandable={{
					expandIcon: ({ expanded, onExpand, record }) =>
						expanded ? (
							<UpOutlined onClick={e => onExpand(record, e)} />
						) : (
							<RightOutlined onClick={e => onExpand(record, e)} />
						)
					,
					expandedRowRender: (record) =>
						<Flex style={{ margin: 0 }} justify='space-between'>
							<Flex vertical>
								{contextHolder}
								<span>id: {record.versionId}</span>
								<span>Название: {record.name}</span>
								<span>Создал: {record.userName}</span>
								<span>Цель: Определение инцидента</span>
							</Flex>
							{record.summaryState === 'DRAFT' &&

								<Popconfirm
									title={`Delete the rule`}
									description={`Are you sure to delete  ${record.name} ?`}
									onConfirm={(e?: React.MouseEvent<HTMLElement>) => confirm(e, record.versionId)}
									onCancel={cancel}
									okText="Yes"
									cancelText="No"
								>
									<Button danger>Delete</Button>
								</Popconfirm>
							}
						</Flex>,
					rowExpandable: (record) => record.name !== 'Not Expandable',
				}}
			/>
		)
	}

	else if (type === 'DATA_STRUCTURE') {
		return (
			<Table
				style={{ width: '100%' }}
				columns={DataStructureListCols}
				size="small"
				dataSource={dataSource as SingleDataStructure[]}
				rowKey={row => row.versionId as number}
				onChange={handleTableChange}
				loading={loading}
				pagination={{ position: ['topRight'], current: tableParams.page + 1, pageSize: tableParams.size, total: totalRules }}
				onRow={(record) => {
					return {
						onClick: () => {
							setSelectedRowId(record.versionId);
						},
					};
				}
				}

				rowClassName={(record) => (record.versionId === selectedRowId ? styles.picked : styles.default)}
				expandable={{
					expandIcon: ({ expanded, onExpand, record }) =>
						expanded ? (
							<UpOutlined onClick={e => onExpand(record, e)} />
						) : (
							<RightOutlined onClick={e => onExpand(record, e)} />
						)
					,
					expandedRowRender: (record) =>
						<Flex style={{ margin: 0 }} justify='space-between'>
							<Flex vertical>
								{contextHolder}
								<span>id: {record.versionId}</span>
								<span>Название: {record.name}</span>
								<span>Создал: {record.userName}</span>
								<span>Цель: Определение инцидента</span>
							</Flex>
							{record.summaryState === 'DRAFT' &&

								<Popconfirm
									title={`Delete the rule`}
									description={`Are you sure to delete  ${record.name} ?`}
									onConfirm={(e?: React.MouseEvent<HTMLElement>) => confirm(e, record.versionId)}
									onCancel={cancel}
									okText="Yes"
									cancelText="No"
								>
									<Button danger>Delete</Button>
								</Popconfirm>
							}
						</Flex>,
					rowExpandable: (record) => record.name !== 'Not Expandable',
				}}
			/>
		)
	}

	else if (type === 'CONNECTOR') {

		return (
			<Table
				style={{ width: '100%' }}
				columns={ConnectorListCols}
				size="small"
				dataSource={dataSource as Connector[]}
				rowKey={row => row.versionId as number}
				onChange={handleTableChange}
				loading={loading}
				pagination={{ position: ['topRight'], current: tableParams.page + 1, pageSize: tableParams.size, total: totalConnectors }}
				onRow={(record) => {
					return {
						onClick: () => {
							setSelectedRowId(record.versionId)
							if (pickIdFromTable)
								pickIdFromTable(record.versionId)
						},
					};
				}
				}
				rowClassName={(record) => (record.versionId === selectedRowId ? styles.picked : styles.default)}
				expandable={{

					expandIcon: ({ expanded, onExpand, record }) =>
						expanded ? (
							<UpOutlined onClick={e => onExpand(record, e)} />
						) : (
							<RightOutlined onClick={e => onExpand(record, e)} />
						)
					,
					expandedRowRender: (record) =>


						<Flex justify='space-between'>
							{contextHolder}
							<Flex style={{ margin: 0 }} vertical>
								<span>id: {record.versionId}</span>
								<span>Название: {record.name}</span>
								<span>Создал: {record.userName}</span>
								<span>
									Используется в моделях: {record.models.map((model, index, array) => (
										<span key={index}>
											{model.modelName}
											{index === array.length - 1 ? '.' : ', '}
										</span>
									))}
								</span>
							</Flex>

							{record.summaryState === 'DRAFT' && <Popconfirm
								title={`Delete the rule`}
								description={`Are you sure to delete  ${record.name} ?`}
								onConfirm={(e?: React.MouseEvent<HTMLElement>) => confirm(e, record.versionId)}
								onCancel={cancel}
								okText="Yes"
								cancelText="No"
							>
								<Button danger>Delete</Button>
							</Popconfirm>}

						</Flex>
					,
					rowExpandable: (record) => record.name !== 'Not Expandable',
				}}
			/>
		)
	}

	else

		return (
			<Table
				style={{ width: '100%', height: '430px' }}
				columns={modelListCols}
				size="small"
				dataSource={dataSource as Connector[]}
				rowKey={row => row.versionId as number}
				onChange={handleTableChange}
				loading={loading}
				pagination={{ position: ['topRight'], current: tableParams.page + 1, pageSize: tableParams.size, total: totalModels }}
				onRow={(record) => {
					return {
						onClick: () => {
							setSelectedRowId(record.versionId)
							if (pickIdFromTable)
								pickIdFromTable(record.versionId)
						},
					};
				}
				}
				rowClassName={(record) => (record.versionId === selectedRowId ? styles.picked : styles.default)}
				expandable={{

					expandIcon: () => null,
					expandRowByClick: true,
					expandedRowRender: (record) =>


						<Flex justify='space-between'>
							{contextHolder}
							<Flex style={{ margin: 0 }} vertical>
								<span>id: {record.versionId}</span>
								<span>Название: {record.name}</span>
								<span>Создал: {record.userName}</span>
								{type === 'MODEL' ?
									<Flex vertical>
										<span>Процесс-источник: Управление стратегией</span>
										<span>Процесс-назначение: Управление стратегией</span>
									</Flex> :
									<span>Используестя в моделях: {record.models.map((model, index) => {
										return <span key={index}>{model.modelName} {index === record.models.length - 1 ? '.' : ';'}</span>
									})} </span>
								}
							</Flex>

							{record.summaryState === 'DRAFT' && <Popconfirm
								title={`Delete the rule`}
								description={`Are you sure to delete  ${record.name} ?`}
								onConfirm={(e?: React.MouseEvent<HTMLElement>) => confirm(e, record.versionId)}
								onCancel={cancel}
								okText="Yes"
								cancelText="No"
							>
								<Button danger>Delete</Button>
							</Popconfirm>}

						</Flex>
					,
					rowExpandable: (record) => record.name !== 'Not Expandable',
				}}
			/>

		)
}

export default ModelsCustomTable