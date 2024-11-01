
import { Flex, Table, FloatButton, Input, TreeSelect, message } from "antd";
import RulesDataStructureControl from './controlElements';
import { FieldVariable } from "../../../../../shared/entities/Rule/Rule";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { ActionTypes } from "../../../../../redux/Model/RuleReducer";


import type { TableColumnsType } from 'antd';
import { Button, Modal } from 'antd'
import { EditOutlined, MoreOutlined, SaveOutlined } from '@ant-design/icons';
import { useAppSelector } from "../../../../../redux/Store";
import { RulesController } from "../../../../../controllers/RulesController";
import { useParams } from "react-router-dom";

import styles from './dataStructTable.module.scss'

type ItreeData = {
	value: string | number,
	title: string | number,
	children?: ItreeData[]
}

function RulesDataStructureTable(data: { data: FieldVariable[] }) {
	const [calcatedId, setCalculatedId] = useState(0)
	const [fieldsList, setFieldList] = useState<FieldVariable[]>([]);


	const dispatch = useDispatch()
	const selectedFieldId = useAppSelector(state => state.RuleReducer.selectedField.id)
	const selectedFieldTestData = useAppSelector(state => state.RuleReducer.selectedField.testValueJson)

	const fieldsRelationId = useAppSelector(state => state.RuleReducer.fieldRelation?.fieldRelationRequestSubDataDTOList)
	const currentFieldId = useAppSelector(state => state.RuleReducer.selectedField.id)

	const fieldsRelationTags = useAppSelector(state => state.RuleReducer.fieldsResponse)
	// const tagsArray = fieldsRelationTags && fieldsRelationTags.map(item => Object.values(item.tags));
	const tagsArray = fieldsRelationTags?.flatMap(item =>
		Object.entries(item.tags).map(([id, name]) => ({ id: parseInt(id), name }))
	) ?? [];




	const [edit, setEdit] = useState(false)

	const [inputValue, setInputValue] = useState('')
	const [currentInputTestData, setCurrentInputTestData] = useState<number | string>(0)
	const [isModalOpen, setIsModalOpen] = useState(false);

	const [relationalFieldId, setRelationalFieldId] = useState(0)
	const rulesController = new RulesController(dispatch)
	const { fromModel } = useParams()

	const [treeData, setTreeData] = useState<ItreeData[]>()

	const fieldsRelation = useAppSelector(state => state.RuleReducer.fieldRelation)
	const fieldSelection = useAppSelector(state => state.RuleReducer.fieldsResponse)

	const [value, setValue] = useState(0)
	const [defaultTitle, setDefaultTitle] = useState<string>()
	const [isNewField, setIsNewField] = useState(false)
	const fieldRelation = useAppSelector(state => state.RuleReducer.fieldRelation)

	const [messageApi, contextHolder] = message.useMessage();


	const getRelationalFieldId = (value: number) => {
		setRelationalFieldId(value)
	}


	const saveFieldRelation = () => {
		if (fromModel && fieldsRelation) {

			const filteredRelation = fieldsRelation.fieldRelationRequestSubDataDTOList.map(({ srcSummaryFieldId, varSummaryFieldId, varName, srcName }) => ({ srcSummaryFieldId, varSummaryFieldId, varName, srcName }))

			rulesController.fieldRelation({ sdvmodelStructId: Number(fieldsRelation.sdvmodelStructId), fieldRelationRequestSubDataDTOList: filteredRelation }).then(() =>
				rulesController.getFieldRelation(Number(fromModel)).then(() => messageApi.open({
					type: 'success',
					content: 'Fields have been successfully related',
				}))
			)

		}
	}

	const onChange = (newValue: number) => {

		console.log('@', tagsArray)

		getRelationalFieldId(newValue)
		setValue(newValue)
		setDefaultTitle(tagsArray.find((item) => item.id === newValue)?.name ?? undefined)


		dispatch({
			type: ActionTypes.RULE_SET_FIELDS_RELATION,
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



	const showModal = () => {
		setIsModalOpen(true);

	};

	const handleOk = () => {
		setIsModalOpen(false);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	interface DataType {
		key: React.ReactNode;
		name: string;
		id: number;
		testValueJson: string | number | null;
		children?: DataType[];
	}


	const columns: TableColumnsType<DataType> = [
		{
			title: 'LID',
			dataIndex: 'id',
			key: 'id',
			width: '10%',
		},
		{
			title: 'Наименование данных или ключ',
			dataIndex: 'name',
			key: 'name',
			width: '40%',
			render: (text: string, record: DataType) =>
				<div>
					{(edit === true && selectedFieldId === record.id) || (isNewField && selectedFieldId === record.id) ?
						<Input onChange={(e) => setInputValue(e.target.value)}
							value={inputValue}
						// defaultValue={text}
						/> :
						<span
							onClick={() => {
								dispatch({
									type: ActionTypes.RULE_SELECT_VALUE_TO_CONDITION,
									payload: { valueToCondition: text, isValueSelecting: true }
								})
							}}
							style={{ cursor: 'pointer' }}>{text}</span>}
				</div>
		},
		{
			title: 'Тестовое значение',
			dataIndex: 'testValueJson',
			key: 'testValueJson',
			width: '20%',
			render: (text: string, record: DataType) =>
				<div>{(edit === true && selectedFieldId === record.id) || (isNewField && selectedFieldId === record.id) ?
					<Input
						onChange={(e) => {
							// setCurrentInputTestData(e.target.value as string | number)

							const newValue = e.target.value.trim();
							const parsedValue = !isNaN(Number(newValue)) ? Number(newValue) : newValue;
							setCurrentInputTestData(parsedValue);

						}}
						value={currentInputTestData}
					// defaultValue={text} 
					/>
					: <span>{text}</span>}</div>
		},
		{
			title: 'Связь с данными',
			width: '30%',
			key: 'edit',
			render: (text, record) => <Flex gap='small'>
				<Button
					type="primary"
					shape="circle"
					icon={(selectedFieldId === record.id! && edit) || (isNewField && selectedFieldId === record.id) ? <SaveOutlined /> : <EditOutlined />}
					onClick={() => {
						if (edit) {
							setIsNewField(false);
							dispatch({
								type: ActionTypes.RULE_EDIT_FIELD,
								payload: { name: inputValue, id: selectedFieldId, testValueJson: currentInputTestData }
							});



						} else {
							setInputValue(record.name)
							setCurrentInputTestData(record.testValueJson as string | number)
						}
						setEdit(!edit);
					}}
				/>

				{fromModel && <div>
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
				</div>}

			</Flex>
		}
	];


	useEffect(() => {
		setFieldList(data.data.length > 0 ? data.data.filter(item => !item.defaultField) : [])
	}, [data])

	useEffect(() => {
		if (fieldRelation)
			setValue(fieldRelation?.fieldRelationRequestSubDataDTOList.find(v => v.varSummaryFieldId === currentFieldId)?.srcSummaryFieldId as number)

	}, [])

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
			console.log('trdt', mapFields(fieldSelection))
		}
	}, [fieldSelection])

	const addField = () => {
		setCalculatedId(fieldsList ? fieldsList.length + 1 : 0)
		setFieldList([...fieldsList, { name: `newField-${calcatedId}`, id: calcatedId, key: calcatedId, testValueJson: 0 }]);

		dispatch({
			type: ActionTypes.RULE_ADD_FIELD,
			payload: { name: `newField-${calcatedId}`, id: calcatedId, testValueJson: 0 }
		})
		dispatch({
			type: ActionTypes.RULE_SELECT_FIELD,
			payload: {
				name: `newField-${calcatedId}`,
				id: calcatedId,
				testData: 0
			}
		})
		setEdit(true)
		setIsNewField(true)
	}

	const removeField = (idToRemove: number) => {

		dispatch({
			type: ActionTypes.RULE_REMOVE_FIELD,
			payload: idToRemove
		});
	};




	return (
		<Flex vertical gap='middle' style={{ marginBottom: 20, width: '70%' }} >

			{contextHolder}

			<Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
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
							// fieldsRelationId?.find(field => field.srcSummaryFieldId === newValue)?.srcName as string
						)}
				/>
			</Modal>

			<RulesDataStructureControl addField={addField} removeField={removeField} saveFieldRelation={saveFieldRelation} />
			<Table
				columns={columns}
				dataSource={fieldsList}
				rowKey={(row) => row.id}
				rowClassName={(row) => (row.id === selectedFieldId ? styles.picked : styles.default)}
				size='small'
				onRow={(record: any) => {
					return {
						onClick: () => {

							dispatch({ type: ActionTypes.RULE_SELECT_FIELD, payload: record })
						},
					};
				}}

			/>

		</Flex>
	)
}

export default RulesDataStructureTable