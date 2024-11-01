import { Flex, Button, Divider, Modal, Checkbox, Table } from 'antd'
import { PlusOutlined, MinusOutlined, StopOutlined, PlayCircleOutlined, PauseCircleOutlined } from '@ant-design/icons';
import { useState } from 'react';
import type { GetProp } from 'antd';
import { IModelStructComponents } from '../../../shared/entities/Connector/Connector';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../redux/Model/ModelReducer';
import type { TableColumnsType } from 'antd';



type DataType = {
	name: string;
	id: number;
	type: string;
}


function AntTableControl({ fullData, checkedValues }: { fullData: any, checkedValues: IModelStructComponents[] }) {

	const [isModalOpen, setIsModalOpen] = useState(false);
	const [pickedFields, setPickedFields] = useState<number[]>([])

	const [dataSource, setDataSource] = useState<DataType[]>()

	const dispatch = useDispatch()

	const showModal = () => {
		setIsModalOpen(true);

		setDataSource(fullData.map((item: any) => ({
			name: item.name,
			id: item.versionId,
			type: item.type
		})))
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

		setIsModalOpen(false);

		const newValues = pickedFields.filter(value => !checkedValues.some(checkedValue => checkedValue.modelComponentId === value))
			.map(value => ({
				modelComponentId: value,
				//@ts-ignore
				type: fullData.find(item => item.versionId === value).type,
				//@ts-ignore
				launchSecondStage: fullData.find(item => item.versionId === value).launchSecondStage,
				//@ts-ignore
				queueNumber: fullData.find(item => item.versionId === value).queueNumber,
				//@ts-ignore
				daysRemaining: fullData.find(item => item.versionId === value).daysRemaining,
				//@ts-ignore
				resultIncremental: fullData.find(item => item.versionId === value).resultIncremental,
				//@ts-ignore
				amountOfTransactions: fullData.find(item => item.versionId === value).amountOfTransactions,
				//@ts-ignore
				amountOfErrors: fullData.find(item => item.versionId === value).amountOfErrors,
			}));

		dispatch({
			type: ActionTypes.MODEL_ADD_FIELDS,
			payload: {
				defaultValues: checkedValues,
				newValues: newValues
			}
		})
		// console.log('@', checkedValues.map((v: any) => v.modelComponentId))
		console.log('@', newValues)

		console.log('@checked', checkedValues)
		console.log('@@', fullData)

		console.log('@@picked', pickedFields)
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const onChange: GetProp<typeof Checkbox.Group, 'onChange'> = (pickedValues) => {
		const pickedValuesStringArray = pickedValues.map(Number);
		setPickedFields(pickedValuesStringArray);
	};


	return (
		<Flex gap="middle">

			<Modal title="Добавить" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>

				{/* <Checkbox.Group
					style={{ width: '100%' }}
					onChange={onChange}
					defaultValue={checkedValues && checkedValues.map((v: any) => v.modelComponentId)} >
					{fullData !== undefined && fullData.map((v: any, index: number) => <Checkbox value={v.versionId} key={index}>{v.versionId}</Checkbox>)}
				</Checkbox.Group> */}

				<Table
					columns={columns}
					dataSource={dataSource}
					rowKey={(dataSource: DataType) => dataSource.id}
					rowSelection={{
						type: 'radio'
					}}
				/>

			</Modal>

			<Flex gap='small'>
				<Button icon={<PlusOutlined />} onClick={showModal} />
				<Button icon={<MinusOutlined />} />
			</Flex>

			<Divider type="vertical" />

			<Flex gap='small'>
				<Button icon={<PlayCircleOutlined />} />
				<Button icon={<PauseCircleOutlined />} />
				<Button icon={<StopOutlined />} />
			</Flex>

		</Flex>
	)
}

export default AntTableControl