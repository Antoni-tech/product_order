import { Button, Flex, FloatButton, Modal, Table, TreeSelect } from 'antd'
import { MoreOutlined, SaveOutlined } from '@ant-design/icons';
import { useAppSelector } from '../../../../../redux/Store'
import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { ConnectorOutputController } from '../../../../../controllers/ConnectorOutputController';
import { useParams } from 'react-router-dom';
import { ActionTypes } from '../../../../../redux/Model/ConnectorOuputReducer';
import { useFormikContext } from 'formik';

type ItreeData = {
	value: string | number,
	title: string | number,
	children?: ItreeData[]
}

function ConnectorLinkTable() {

	const dispatch = useDispatch()
	const { fromModel, id } = useParams()
	const formik = useFormikContext()
	const dataSource = useAppSelector(store => store.ConnectorOutputReducer.connector?.fields)
	const fieldSelection = useAppSelector(state => state.ConnectorOutputReducer.fieldsResponse)
	const fieldsRelation = useAppSelector(state => state.ConnectorOutputReducer.fieldRelation)
	const currentFieldId = useAppSelector(state => state.ConnectorOutputReducer.selectedField.id)
	const fieldsRelationId = useAppSelector(state => state.ConnectorOutputReducer.fieldRelation?.fieldRelationRequestSubDataDTOList)


	const [isModalOpen, setIsModalOpen] = useState(false);
	const [treeData, setTreeData] = useState<ItreeData[]>()

	const conOutController = new ConnectorOutputController(dispatch)

	const [relationalFieldId, setRelationalFieldId] = useState(0)
	const [value, setValue] = useState(0)
	const [defaultTitle, setDefaultTitle] = useState<string>()

	const tagsArray = fieldSelection?.flatMap(item =>
		Object.entries(item.tags).map(([id, name]) => ({ id: parseInt(id), name }))
	) ?? [];


	useEffect(() => {

		if (fieldSelection) {


			const mapChildren = (fields: any[]): any[] => {
				return fields.map((field) => ({
					value: field.id,
					disabled: field.children !== null,
					title: field.name,
					children: field.children ? mapChildren(field.children) : null,
				}));
			}
			const mapFields = (fields: any[]): any[] => {
				return fields.map((field) => ({
					value: field.id,
					disabled: field.children !== null,
					title: field.type,
					children: field.fields ? mapChildren(field.fields) : null,
				}));
			};

			setTreeData(mapFields(fieldSelection));

			conOutController.getFieldRelation(Number(fromModel))

		}
	}, [fieldSelection])



	const cols = [
		{
			title: 'Имя',
			dataIndex: 'name',
			key: 'name',
		},
		{
			title: 'Тип данных',
			dataIndex: 'fieldType',
			key: 'fieldType',
		},
		{
			title: 'Макс. размер',
			dataIndex: 'maxSize',
			key: 'maxSize',
		},
		{
			title: 'Пустые',
			dataIndex: 'allowEmpty',
			key: 'allowEmpty',
		},
		{
			title: 'Спецсимволы',
			dataIndex: 'prohibitSpecCharacters',
			key: 'prohibitSpecCharacters',
		},
		{
			title: 'Множество',
			dataIndex: 'allowArray',
			key: 'allowArray',
		},
		{
			title: 'Макс. кол-во',
			dataIndex: 'maxArray',
			key: 'maxArray',
		},
		{
			title: 'Связь с данными',
			key: 'edit',
			render: (text: string, record: any) => {

				return (record.fieldType !== 'OBJECT' ?




					<div>
						{fieldsRelationId?.find(field => field.varSummaryFieldId === record.id) ?
							<Button onClick={showModal}>
								{fieldsRelationId?.find(field => field.varSummaryFieldId === record.id)?.srcName ?
									fieldsRelationId?.find(field => field.varSummaryFieldId === record.id)?.srcName :
									defaultTitle

								}
							</Button>
							:
							<Button type="primary" shape="circle" icon={<MoreOutlined />} onClick={showModal} />
						}
					</div>


					: null)
			}
		},

	]

	const showModal = () => { setIsModalOpen(true); }

	const handleOk = () => {
		setIsModalOpen(false);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const saveFieldRelation = () => {
		if (fieldsRelation) {

			const filteredRelation = fieldsRelation.fieldRelationRequestSubDataDTOList.map(({ srcSummaryFieldId, varSummaryFieldId, varName, srcName }) => ({ srcSummaryFieldId, varSummaryFieldId, varName, srcName }))

			conOutController.fieldRelation({ sdvmodelStructId: Number(fieldsRelation.sdvmodelStructId), fieldRelationRequestSubDataDTOList: filteredRelation }).then(() =>
				conOutController.getFieldRelation(Number(fromModel))
			)

		}
	}

	const getRelationalFieldId = (value: number) => {
		setRelationalFieldId(value)
	}

	const onChange = (newValue: number) => {


		getRelationalFieldId(newValue)
		setValue(newValue)
		setDefaultTitle(tagsArray.find((item) => item.id === newValue)?.name ?? undefined)


		dispatch({
			type: ActionTypes.CONNECTOR_OUT_SET_FIELDS_RELATION,
			payload: {
				sdvmodelStructId: fromModel,
				relation: {
					srcName: tagsArray.find((item) => item.id === newValue)?.name ?? undefined,
					srcSummaryFieldId: newValue,
					varSummaryFieldId: currentFieldId
				}
			}
		})
	};

	return (
		<Flex vertical justify='flex-end' align='flex-end' style={{ width: '100%', position: 'relative' }}>

			<Button
				onClick={saveFieldRelation}
				style={{ width: 40, position: 'absolute', top: '-50px', right: 0, zIndex: 100 }}
				icon={<SaveOutlined />}
			/>

			<Modal title="Pick a connector" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
				<TreeSelect
					showSearch
					style={{ width: '100%' }}
					dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
					placeholder="Please select"
					allowClear
					treeDefaultExpandAll
					treeData={treeData}
					value={value}
					onChange={(newValue: number) =>
						onChange(
							newValue,
						)}
				/>
			</Modal>

			<Table
				columns={cols}
				dataSource={dataSource}
				size='small'
				rowKey={(row) => row.id}
				style={{ width: '100%' }}
				onRow={(record: any) => {
					return {
						onClick: () => {
							dispatch({ type: ActionTypes.CONNECTOR_OUT_SELECT_FIELD, payload: record })
						},
					};
				}}
			/>


			<FloatButton icon={<SaveOutlined />} onClick={
				() => {
					formik.handleSubmit()
					setTimeout(() => {
						conOutController.get(String(id)).then()
					}, 300)

				}}
			/>
		</Flex>


	)
}

export default ConnectorLinkTable