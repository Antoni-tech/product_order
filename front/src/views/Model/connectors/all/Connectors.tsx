import { FC, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { ConnectorInputController } from "../../../../controllers/ConnectorInputController";
import { ConnectorOutputController } from "../../../../controllers/ConnectorOutputController";
import { useAppSelector } from "../../../../redux/Store";
import { Button, Flex, Input, Modal, Select, Space, TreeSelect, message } from "antd";
import { PlusOutlined, CopyOutlined } from '@ant-design/icons';
import { Connector } from "../../../../shared/entities/Connector/Connector";
import ModelsCustomTable from "../../common/all/ModelsCustomTable/ModelsCustomTable";
import { TableParams } from "../../common/all/Models";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClone } from "@fortawesome/free-regular-svg-icons";
import { ConnectorController } from "../../../../controllers/ConnectorController";

const Connectors: FC = () => {

	const dispatch = useDispatch();


	const connectorController = new ConnectorController(dispatch);

	const userValue = useAppSelector(store => store.UserReducer?.user)
	const inputConList = useAppSelector(store => store.ConnectorInputReducer.connectors?.list)

	const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1)
	const [previewPaginatorPosition, setPreviewPaginatorPosition] = useState(1)

	const [messageApi, contextHolder] = message.useMessage();

	const [dataSource, setDataSource] = useState<Connector[]>([])
	const [uniqId, setUniqId] = useState(0)
	const [pickedId, setPickedId] = useState<string>('')
	const [isModalOpen, setIsModalOpen] = useState(false);

	const [conName, setConName] = useState('')

	const [conGeneralType, setConGeneralType] = useState('')
	const [conType, setConType] = useState('')
	const [conSubType, setConSubType] = useState('')

	const tableParams: TableParams = {
		page: 0,
		size: 10,
		userId: 1
	};

	const conTypeList = [
		'REQUEST_RESPONSE',
		'STREAMING',
		'STREAMING_WITH_REQUEST_RESPONSE',
		'WITHOUT_WAITING_RESPONSE'
	]



	const newId = inputConList ? inputConList.length + 1 : 0;

	const showModal = () => {
		setConName(`Connector #${newId}`)
		setIsModalOpen(true);
	};

	const handleOk = () => {
		setIsModalOpen(false);
		addNew()
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const selectOptions = [
		{
			value: 'parent 1',
			title: 'Получение',
			disabled: true,
			children: [
				{
					value: 'parent 1-0',
					title: 'Ожидающий',
					disabled: true,
					children: [
						{
							value: 'item 1-1-1',
							title: 'Запрос-ответ',
						},
						{
							value: 'item 1-1-2',
							title: 'Потоковый',
						},
						{
							value: 'item 1-1-3',
							title: 'Потоковый с запросом-ответом',
						},
					],
				},
				{
					value: 'parent 1-1',
					title: 'С запросом/инициацией на получение',
					children: [
						{
							value: 'item 1-2-1',
							title: 'Запрос-ответ',
						},
						{
							value: 'item 1-2-2',
							title: 'Потоковый',
						},
					],
				},
			],
		},
		{
			value: 'parent.sec',
			title: 'Отправка',
			disabled: true,
			children: [
				{
					value: 'parent 2',
					title: 'Ожидающий',
					disabled: true,
					children: [
						{
							value: 'item 2-1-1',
							title: 'Без ожидания ответа',
						},
						{
							value: 'item 2-1-3',
							title: 'Потоковая',
						},
					],
				},
				{
					value: 'parent 3',
					title: 'Отправка',
					disabled: true,
					children: [
						{
							value: 'item 2-1-2',
							title: 'Запрос-ответ',
						},
						{
							value: 'item 2-1-4',
							title: 'Потоковый с запросом-ответом',
						},
					]
				}
			]
		}
	];



	const handleChange = (value: string) => {

		const fieldList = [
			{
				name: 'item 1-1-1',
				generalType: 'CONNECTOR_INPUT',
				subType: 'AWAITINGER',
				conType: conTypeList[0],
			},
			{
				name: 'item 1-1-2',
				generalType: 'CONNECTOR_INPUT',
				subType: 'AWAITINGER',
				conType: conTypeList[1],
			},
			{
				name: 'item 1-1-3',
				generalType: 'CONNECTOR_INPUT',
				subType: 'AWAITINGER',
				conType: conTypeList[2],
			},
			{
				name: 'item 1-2-1',
				generalType: 'CONNECTOR_INPUT',
				subType: 'REQUESTER',
				conType: conTypeList[0],
			},
			{
				name: 'item 1-2-2',
				generalType: 'CONNECTOR_INPUT',
				subType: 'REQUESTER',
				conType: conTypeList[1],
			},
			{
				name: 'item 1-2-2',
				generalType: 'CONNECTOR_INPUT',
				subType: 'REQUESTER',
				conType: conTypeList[1],
			},
			{
				name: 'item 2-1-1',
				generalType: 'CONNECTOR_OUTPUT',
				subType: 'AWAITINGER',
				conType: conTypeList[3],
			},
			{
				name: 'item 2-1-2',
				generalType: 'CONNECTOR_OUTPUT',
				subType: 'REQUESTER',
				conType: conTypeList[0],
			},
			{
				name: 'item 2-1-3',
				generalType: 'CONNECTOR_OUTPUT',
				subType: 'AWAITINGER',
				conType: conTypeList[1],
			},
			{
				name: 'item 2-1-4',
				generalType: 'CONNECTOR_OUTPUT',
				subType: 'REQUESTER',
				conType: conTypeList[2],
			},
		]

		const selectedField = fieldList.find(field => field.name === value);

		setConGeneralType(String(selectedField?.generalType))
		setConType(String(selectedField?.conType))
		setConSubType(String(selectedField?.subType))

		console.log('@', selectedField);
	};



	const loadAllConnectors = () => {
		if (userValue) {
			if (userValue?.id === undefined) return;

			connectorController.getAll(tableParams, { showFields: true }).then()
		}
	}

	const changeName = (e: React.ChangeEvent<HTMLInputElement>) => {
		setConName(e.target.value);
	}

	const addNew = () => {

		const newInputCon = {
			name: conName,
			isCreate: true,
			// connectorPurpose: "GetRD, IDODAC",
			summarySubType: conSubType,
			connectorSubType: conType,
			type: conGeneralType,
			fields:
				[]
		}


		connectorController.createOrUpdate(newInputCon)
			.then(res => {
				if (!res?.None) {
					messageApi.open({
						type: 'success',
						content: 'New connector added successfully',
					});
					loadAllConnectors()
				} else {
					messageApi.open({
						type: 'error',
						content: 'Error create connector',
					});
				}
			})

		setUniqId(newId);
	}

	const duplicate = () => {
		connectorController.duplicate(pickedId)
			.then(res => {
				if (!res?.None) {
					messageApi.open({
						type: 'success',
						content: `Connector id:${pickedId} was duplicated`,
					});
					loadAllConnectors()
				} else {
					messageApi.open({
						type: 'error',
						content: 'Error create connector',
					});
				}
			})
		console.log(pickedId)
	}

	const pickIdFromTable = (id: number) => {
		setPickedId(String(id))
	}

	useEffect(() => {
		loadAllConnectors()
		if (inputConList) {
			setDataSource(inputConList)
		}
	}, [userValue, currentPaginatorPosition, uniqId])

	useEffect(() => {
		if (inputConList) {
			setDataSource(inputConList)
		}
	}, [inputConList])


	return (
		<Flex vertical gap='middle' align="flex-end" style={{ position: 'relative' }}>
			{contextHolder}

			<Flex gap='small' style={{ position: 'absolute', top: 15, left: 0, zIndex: 5 }}>
				<Button onClick={showModal} icon={<PlusOutlined />} />
				<Button onClick={duplicate} icon={<FontAwesomeIcon icon={faClone} />} />
			</Flex>

			<ModelsCustomTable
				requestParams={tableParams}
				type="CONNECTOR"
				dataSource={dataSource}
				pickIdFromTable={pickIdFromTable}
			/>


			<Modal
				title="Добавление коннектора"
				open={isModalOpen}
				onOk={handleOk}
				onCancel={handleCancel}
				okButtonProps={{ disabled: conType === '' }}
			>
				<Space direction="vertical" size="middle" style={{ display: 'flex', width: 400 }}>

					<Flex gap="small" justify="space-between" align="center">
						<span>Название</span>
						<Input
							style={{ width: '70%', height: 32, borderRadius: 8 }}
							value={conName}
							onChange={changeName}
						/>
					</Flex>

					<Flex gap="small" justify="space-between" align="center">
						<span>Тип коннектора</span>
						<TreeSelect
							style={{ width: '70%' }}
							treeData={selectOptions}
							onChange={handleChange}
							treeDefaultExpandAll
						/>
					</Flex>


				</Space>

			</Modal>

		</Flex>
	)
}

export default Connectors
