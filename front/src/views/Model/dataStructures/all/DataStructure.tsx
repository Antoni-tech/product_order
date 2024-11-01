import { Button, Flex, Input, Modal, message } from 'antd'
import { useEffect, useState } from 'react'
import { PlusOutlined, CopyOutlined } from '@ant-design/icons';
import ModelsCustomTable from '../../common/all/ModelsCustomTable/ModelsCustomTable';
import { SingleDataStructure } from '../../../../shared/entities/DataStructure/DataStructure';
import { useDispatch } from 'react-redux';
import { DataStructureController } from '../../../../controllers/DataStructureController';
import { useAppSelector } from '../../../../redux/Store';
import { TableParams } from '../../common/all/Models';

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClone } from "@fortawesome/free-regular-svg-icons";


function DataStructureList() {

	const dispatch = useDispatch()
	const dataStructureController = new DataStructureController(dispatch);

	const DataStructures = useAppSelector(store => store.DataStructureReducer.dataStructureList?.list)

	const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1)
	const [previewPaginatorPosition, setPreviewPaginatorPosition] = useState(1)

	const [isModalOpen, setIsModalOpen] = useState(false);
	const [messageApi, contextHolder] = message.useMessage();
	const userValue = useAppSelector(store => store.UserReducer?.user)

	const [uniqId, setUniqId] = useState(0)
	const [pickedId, setPickedId] = useState<string>('')

	const tableParams: TableParams = {
		page: 0,
		size: 10,
		userId: 1
	};

	const loadAllDataStructures = () => {
		if (userValue) {
			if (userValue?.id === undefined) return;
			const reqConfig = {
				params: {
					page: (currentPaginatorPosition === previewPaginatorPosition) ? 0 : (currentPaginatorPosition > 0) ? currentPaginatorPosition - 1 : 0,
					size: 10,
					userId: parseInt(userValue.id, 10),
					// userId: 1,
				}
			}
			dataStructureController.getAll(reqConfig?.params, null).then()
		}
	}

	const pickIdFromTable = (id: number) => {
		setPickedId(String(id))
	}

	const showModal = () => {
		setIsModalOpen(true);
	};



	const handleOk = () => {
		const newId = DataStructures ? DataStructures.length + 1 : 0;

		const newField = {
			name: `Data Structure #${newId}`,
			isCreate: true,
			fields: []
		}

		dataStructureController.createOrUpdate(newField)
			.then(res => {
				if (!res?.None) {
					messageApi.open({
						type: 'success',
						content: 'New model added successfully',
					});
					loadAllDataStructures()
				} else {
					messageApi.open({
						type: 'error',
						content: 'Error create connector',
					});
				}
			})
		setUniqId(newId);

		setIsModalOpen(false);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	useEffect(() => {
		loadAllDataStructures()
	}, [userValue, currentPaginatorPosition, uniqId])

	return (
		<Flex vertical gap='middle' align='flex-end' style={{ position: 'relative' }}>

			{contextHolder}

			<Flex gap='small' style={{ position: 'absolute', top: 15, left: 0, zIndex: 5 }}>
				<Button icon={<PlusOutlined />} onClick={showModal} />
				<Button icon={<FontAwesomeIcon icon={faClone} />} />
			</Flex>

			<ModelsCustomTable
				requestParams={tableParams}
				type="DATA_STRUCTURE"
				dataSource={DataStructures as SingleDataStructure[]}
				pickIdFromTable={pickIdFromTable}
			/>

			<Modal title="Создание структуры данных" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
				<label htmlFor="">Название</label>
				<Input defaultValue={'Структура данных'} />
			</Modal>
		</Flex>
	)
}

export default DataStructureList