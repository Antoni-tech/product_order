import { Button, Modal, Table } from 'antd'
import { useFormikContext } from 'formik';
import React, { useState } from 'react'
import { useDispatch } from 'react-redux';
import { DataStructureController } from '../../../../../controllers/DataStructureController';
import { useAppSelector } from '../../../../../redux/Store';

type IDataSource = {
	versionId: number,
	userName: string,
	models: Array<any>,
	name: string,
}
function SelectableStructure() {

	const formik = useFormikContext()

	const dispatch = useDispatch()
	const dataStructureController = new DataStructureController(dispatch);

	const userValue = useAppSelector(store => store.UserReducer?.user)

	const [isModalOpen, setIsModalOpen] = useState(false);
	const [dataSource, setDataSource] = useState<Array<IDataSource>>([])
	const [selectedDataSourceId, setSelectedDataSourceId] = useState(0)

	const showModal = () => {

		if (userValue && userValue.id) {
			const reqConfig = {
				params: {
					page: 0,
					size: 10,
					userId: parseInt(userValue.id, 10),
				}
			}
			dataStructureController.getAll(reqConfig?.params, null).then((res) => setDataSource(res.Some))
		}

		setIsModalOpen(true);
	};

	const handleOk = () => {
		setIsModalOpen(false);
		// formik.setFieldValue('extended', selectedDataSourceId).then(() => formik.handleSubmit())
		formik.handleSubmit()
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const cols = [
		{
			title: 'Название',
			dataIndex: 'name',
		},
		{
			title: 'Создал',
			dataIndex: 'userName',
		}
	]

	const rowSelection = {
		onChange: (selectedRowKeys: React.Key[], selectedRows: IDataSource[]) => {
			setSelectedDataSourceId(selectedRows[0].versionId)
		},
		getCheckboxProps: (record: IDataSource) => ({
			disabled: record.name === 'Disabled User', // Column configuration not to be checked
			name: record.name,
		}),
	};

	return (
		<>
			<Button type="primary" onClick={showModal}>
				Выбрать структуру данных
			</Button>
			<Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
				<Table
					dataSource={dataSource}
					columns={cols}
					rowKey={(dataSource) => dataSource.versionId}

					rowSelection={{
						type: 'radio',
						...rowSelection,
					}}
				/>
			</Modal>
		</>

	)
}

export default SelectableStructure