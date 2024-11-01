import { Button, Flex, Table, TableProps, Typography } from 'antd'
import { incidentColumns } from './tableCols'
import { useAppSelector } from '../../../../../redux/Store'
import { IIncidentList, ISingleIncidentJsonData } from '../../../../../shared/entities/Connector/Connector'
import { useEffect, useState } from 'react'
import { ModelsController } from '../../../../../controllers/ModelsController'
import { useDispatch } from 'react-redux'
import { useParams } from 'react-router-dom'
import { UserController } from '../../../../../controllers/UserController'
import { ActionTypes } from '../../../../../redux/Model/ModelReducer'

interface TableParams {
	page: number,
	size: number,
	userId: number,
	modelId: string
}

function IncidentsTable() {

	// const dataSource = useAppSelector(store => store.ModelReducer.incidentsList)
	const { model } = useAppSelector(store => store.ModelReducer)
	const { id } = useParams()
	const dispatch = useDispatch()
	const [dataSource, setDataSource] = useState<IIncidentList[]>();
	const [totalCount, setTotalCount] = useState(0)

	const modelController = new ModelsController(dispatch);

	const [loading, setLoading] = useState(false);
	const [tableParams, setTableParams] = useState<TableParams>({
		page: 1,
		size: 10,
		userId: 1,
		modelId: String(id) || '1'
	});

	const getRandomuserParams = (params: TableParams) => ({
		...params,
		page: params.page,
	});

	const fetchData = () => {
		setTableParams({
			...tableParams,
			modelId: String(id)
		});
		setLoading(true);
		modelController.getAllIncidentsByModel(String(id), getRandomuserParams(tableParams))
			.then((res) => {
				setDataSource(res.Some.incidents);
				setTotalCount(res.Some.totalCount)
				setLoading(false);
				setTableParams({
					...tableParams,
				});
			});
	};


	const handleTableChange: TableProps['onChange'] = (pagination, filters, sorter) => {

		console.log('pagination', pagination);

		const newPage = pagination.current ? pagination.current - 1 : 0;
		// Adjust the page to be 0-indexed
		const newPageSize = Number(pagination.pageSize);

		if (newPage >= 0 && (newPage !== tableParams.page || newPageSize !== tableParams.size)) {
			setTableParams({
				...tableParams,
				page: newPage,
				size: newPageSize,
				userId: 1, // Assuming userId is constant
				modelId: String(id)
			});

			dispatch({ type: ActionTypes.MODEL_SET_INCIDENT_PARAMS, payload: { ...tableParams, page: newPage, size: newPageSize } })
		}
	};


	useEffect(() => {
		if (model) {
			fetchData();
		}
	}, [JSON.stringify(tableParams), model]);

	return (
		<Flex style={{ width: '100%', marginTop: 50 }} gap='small' justify='flex-start' vertical>
			<Typography.Title level={5} style={{ width: '25%', marginLeft: 20 }}>Инциденты:</Typography.Title>

			<Table
				scroll={{ y: 500 }}
				style={{ width: '100%' }}
				columns={incidentColumns}
				dataSource={dataSource as IIncidentList[]}
				onChange={handleTableChange}
				pagination={{ current: tableParams.page + 1, pageSize: tableParams.size, total: totalCount, position: ['bottomLeft'] }}
				rowKey={row => row.id}
				size='small'
				loading={loading}
				expandable={{
					expandedRowRender: (record) => {
						try {
							const jsonData = JSON.parse(record.jsonData);
							return (
								<Flex vertical style={{ overflow: 'hidden' }}>
									{jsonData.map((singleRecord: ISingleIncidentJsonData, index: number) => (
										<Flex vertical style={{ overflow: 'hidden' }} key={index}>
											{singleRecord.condition !== '' && singleRecord.condition ?

												<span style={{ textOverflow: 'ellipsis', whiteSpace: 'nowrap', overflow: 'hidden' }}>
													condition: {singleRecord.condition}
												</span>

												: null
											}

											{singleRecord.operation !== '' && singleRecord.operation ?

												<span style={{ textOverflow: 'ellipsis', whiteSpace: 'nowrap', overflow: 'hidden' }}>
													operation: {singleRecord.operation}
												</span>

												: null
											}

											<span>Data: {

												singleRecord.data ?
													Object.entries(singleRecord.data).map(([key, value]) => `${key}:${value}; `) : null
											}</span>

										</Flex>
									))}
								</Flex>
							);
						} catch (error) {
							console.error('Error parsing JSON:', error);
							return <span>Error parsing JSON</span>; // or handle the error in a different way
						}
					}



				}}
			/>
		</Flex>
	)
}

export default IncidentsTable

