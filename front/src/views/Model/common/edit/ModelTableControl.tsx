import { useEffect, useState } from 'react';

import { useDispatch, useSelector } from 'react-redux';

import { Flex, Button, Divider, Modal, FloatButton, Table, Typography, message } from 'antd'
import { PlusOutlined, MinusOutlined, StopOutlined, PlayCircleOutlined, PauseCircleOutlined, SaveOutlined } from '@ant-design/icons';
import type { TableColumnsType } from 'antd';
import type { GetProp } from 'antd';
import { useAppSelector } from '../../../../redux/Store';
import { ComponentType } from '../../../../common/constants';
import { Rule } from '../../../../shared/entities/Rule/Rule';
import { Connector } from '../../../../shared/entities/Connector/Connector';
import { ActionTypes } from '../../../../redux/Model/ModelReducer';
import { useFormikContext } from 'formik';
import { ModelsController } from '../../../../controllers/ModelsController';
import { useParams } from 'react-router-dom';
import { ConnectorController } from '../../../../controllers/ConnectorController';
import { TableParams } from '../all/Models';
import { RulesController } from '../../../../controllers/RulesController';


type DataType = {
	name: string;
	id: number;
	type: string;
	resultIncremental?: string,
	launchSecondStage?: string,
	daysRemaining?: string,
	queueNumber?: string
	subtype: string
}



function ModelTableControl() {

	const dispatch = useDispatch()
	const formik = useFormikContext()

	const modelController = new ModelsController(dispatch);
	const connectorController = new ConnectorController(dispatch)
	const rulesController = new RulesController(dispatch)

	const { id } = useParams()

	const { selectedModelField, model, incidentParams } = useAppSelector(store => store.ModelReducer)


	const inputConList = useAppSelector(state =>
		state.ConnectorInputReducer.connectors?.list
			?.filter((item) => item.type === 'CONNECTOR_INPUT')
	);

	const outputConList = useAppSelector(state =>
		state.ConnectorInputReducer.connectors?.list
			?.filter((item) => item.type === 'CONNECTOR_OUTPUT')
	);
	const RulesList = useAppSelector(state => state.RuleReducer.rules?.list)


	const [isModalOpen, setIsModalOpen] = useState(false);
	const [dataSource, setDataSource] = useState<DataType[] | null>();
	const [selectedIDs, setSelectedIds] = useState<number[]>([])
	const [pickedField, setPickedField] = useState<DataType[]>()
	const [messageApi, contextHolder] = message.useMessage();
	const [totalState, setTotalState] = useState('STOP')

	const tableParams: TableParams = {
		page: 0,
		size: 10,
		userId: 1
	};


	const changeState = (state: string) => {
		modelController.transactionCounterRequest(model?.versionId as number, state)
			.then(() => setTotalState(state));
	}

	const data = {
		params: {
			page: 0,
			size: 10,
			userId: 1,
			modelId: model?.versionId
		}
	}

	const getIncidents = (uuid: string, data: any) => {
		modelController.getAllIncidentsByModel(uuid, incidentParams)
	}

	useEffect(() => {
		if (model) {
			setTotalState(model.state);
			connectorController.getAll(tableParams, null)
			rulesController.getAll(tableParams, null)
		}
	}, [model]);

	useEffect(() => {
		let timerId: NodeJS.Timeout;

		const fetchData = () => {
			modelController.getModel(String(id))
			// .then(() => getIncidents(String(id), data.params));
			timerId = setTimeout(fetchData, 10000);// CHANGE TO 10000
		};

		if (model && totalState === 'RUN') {
			fetchData();
		}

		if (model && totalState === 'PAUSE') {
			modelController.getModel(String(id)).then(() => {
				messageApi.open({
					type: 'success',
					content: 'Model has been paused',
				})
			})
		}

		if (model && totalState === 'STOP') {
			modelController.getModel(String(id)).then(() => {
				messageApi.open({
					type: 'success',
					content: 'Model has been stopped',
				})
			})
		}

		return () => {
			clearTimeout(timerId);
		};
	}, [totalState]);


	const showModal = () => {
		setIsModalOpen(true);

		console.log('@', inputConList)


		setSelectedIds(model ? model.modelStructComponents?.map((item: any) => item.modelComponentId) : [])

		if (selectedModelField?.type === 'CONNECTOR_INPUT') {

			setDataSource(inputConList && inputConList.map((item: any) => ({
				name: item.name,
				id: item.versionId,
				type: item.type,
				launchSecondStage: item.launchSecondStage,
				resultIncremental: item.resultIncremental,
				daysRemaining: item.daysRemaining,
				subtype: item.subtype
			})))

		}

		if (selectedModelField?.type === 'CONNECTOR_OUTPUT') {
			setDataSource(outputConList && outputConList.map((item: any) => ({
				name: item.name,
				id: item.versionId,
				type: item.type,
				daysRemaining: item.daysRemaining,
				resultIncremental: item.resultIncremental,
				subtype: item.subtype
			})))
			// setFullData(outputConList)
		}

		if (selectedModelField?.type === 'RULE') {
			setDataSource(RulesList && RulesList.map((item: any) => ({
				name: item.name,
				id: item.versionId,
				type: item.type,
				queueNumber: item.queueNumber,
				daysRemaining: item.daysRemaining,
				resultIncremental: item.resultIncremental,
				subtype: item.summarySubType
			})))
			// setFullData(RulesList)
		}


	};

	const columns: TableColumnsType<DataType> = [
		{
			title: 'id',
			dataIndex: 'id',
		},
		{
			title: 'name',
			dataIndex: 'name',
		},
		{
			title: 'type',
			dataIndex: 'type',
		},
	];

	const handleOk = () => {



		dispatch({
			type: ActionTypes.MODEL_ADD_FIELDS,
			payload: {
				values: pickedField && pickedField.map((item: DataType, index) => {
					console.log(item)

					const isQuality = item.subtype === 'QUALITY';
					const queueNumber = isQuality
						? Number(model?.modelStructComponents?.filter(component => component.subtype === 'QUALITY').length) + 2
						: (model?.modelStructComponents?.filter(component => component.type === item.type).length ?? 0) + index;

					return {
						name: item?.name,
						modelComponentId: item?.id,
						type: item?.type,
						subtype: item?.subtype,
						resultIncremental: item?.resultIncremental,
						launchSecondStage: item?.launchSecondStage,
						daysRemaining: item?.daysRemaining,
						queueNumber: queueNumber,
					};
				}),
			}


		})
		setIsModalOpen(false);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	}

	return (
		<Flex gap="middle" justify='flex-start' style={{ width: '100%', paddingLeft: 20 }}>
			{contextHolder}
			<Modal title="Добавить" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>

				<Table
					columns={columns}
					scroll={{ y: 240 }}
					dataSource={dataSource ? dataSource : []}
					rowKey={(dataSource: DataType) => dataSource.id}
					rowSelection={{
						type: 'checkbox',
						getCheckboxProps: (record: DataType) => ({
							disabled: selectedIDs.includes(record.id), // Column configuration not to be checked
						}),
						onChange: (selectedRowKeys: React.Key[], selectedRows: DataType[]) => {
							setPickedField(selectedRows);
						},
					}}
				/>

			</Modal>

			<Typography.Title level={5} style={{ width: '25%' }}>Логика</Typography.Title>
			<Flex justify='flex-end' gap='middle' style={{ width: '75%' }}>


				<Flex gap='small'>
					<Button disabled={selectedModelField?.type === undefined} icon={<PlusOutlined />} onClick={showModal} />
					{selectedModelField?.summaryState === "DRAFT" ?
						<Button
							icon={<MinusOutlined />}
							onClick={() => dispatch({ type: ActionTypes.MODEL_REMOVE_FIELDS, payload: { id: selectedModelField?.modelComponentId } })}
						/> :
						null}
				</Flex>

				<Divider style={{ height: '100%' }} type="vertical" />

				<Flex gap='small'>
					<Button icon={<PlayCircleOutlined />}
						onClick={() => changeState('RUN')}
						disabled={totalState === 'RUN'} />
					<Button icon={<PauseCircleOutlined />}
						onClick={() => {
							changeState('PAUSE')
						}
						}
						disabled={totalState === 'PAUSE'} />
					<Button
						onClick={() => changeState('STOP')}
						icon={<StopOutlined />} disabled={totalState === 'STOP'} />
				</Flex>

			</Flex>

			<FloatButton onClick={() => formik.handleSubmit()} icon={<SaveOutlined />} />

		</Flex>
	)
}

export default ModelTableControl