import React, { useEffect, useState } from 'react'
import { Table, Flex, Button, Tooltip, Select, Radio, RadioChangeEvent, Input, Switch } from 'antd'
import { QualityRule, Rule } from '../../../../../shared/entities/Rule/Rule';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../../../redux/Model/RuleReducer';
import { PlusOutlined, MinusOutlined } from '@ant-design/icons';
import { useAppSelector } from '../../../../../redux/Store';
import './index'
import { useParams } from 'react-router-dom';
import { ModelsController } from '../../../../../controllers/ModelsController';
import { CheckOutlined, CloseOutlined } from '@ant-design/icons';

function RulesQualityGrid({ rule }: { rule: Rule | null }) {

	const [dataSource, setDataSource] = useState<QualityRule[] | null>()
	const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
	const dispatch = useDispatch()
	const modelsController = new ModelsController(dispatch);

	// const rowNumber = useAppSelector(state => state.RuleReducer.selectedJsonData.number)
	const selectedJsonData = useAppSelector(store => store.RuleReducer.selectedJsonData)
	const modelComponents = useAppSelector(store => store.ModelReducer.model?.modelStructComponents)
	const { fromModel } = useParams()

	const outputConList = useAppSelector(store =>
		store.ModelReducer.model?.modelStructComponents
			.filter(con => con?.type === 'CONNECTOR_OUTPUT')
			.map(con => ({
				label: con?.name ?? '',
				value: con?.modelComponentId ?? 0
			}))
	);


	const handleChangeNumber = (e: React.ChangeEvent<HTMLInputElement>, number: number) => {
		dispatch({
			type: ActionTypes.RULE_EDIT_JSON_DATA,
			payload: {
				...selectedJsonData,
				id: number,
				newCondition: (selectedJsonData && selectedJsonData.condition),
				number: e.target.value,
				isQuality: true,
			}
		});
	};
	const handleChange = (e: React.ChangeEvent<HTMLInputElement>, number: number) => {
		dispatch({
			type: ActionTypes.RULE_EDIT_JSON_DATA,
			payload: {
				...selectedJsonData,
				id: number,
				newCondition: (selectedJsonData && selectedJsonData.condition),
				isQuality: true,
				textValue: e.target.value
			}
		});
	};

	const handleCon = (value: number | undefined, number: number) => {
		console.log('@@@@', value)
		dispatch({
			type: ActionTypes.RULE_EDIT_JSON_DATA,
			payload: {
				id: number,
				...selectedJsonData,
				newCondition: (selectedJsonData && selectedJsonData.condition),
				isQuality: true,
				connectorOutputVersionId: value
			}
		});
	};

	const onChange = (checked: boolean, number: number) => {
		console.log("@", checked)
		dispatch({
			type: ActionTypes.RULE_EDIT_JSON_DATA,
			payload: {
				id: number,
				...selectedJsonData,
				newCondition: (selectedJsonData && selectedJsonData.condition),
				isQuality: true,
				toIncidents: checked
			}
		});
	};


	const columns = [
		{
			title: 'Число',
			dataIndex: 'number',
			key: 'number',
			width: fromModel ? '20%' : '30%',
			render: (text: number, record: any) => {
				return <span style={{ display: 'flex', alignItems: 'center' }}>
					<div style={selectedJsonData?.id === record.id ?
						{ borderRadius: '100%', width: 10, height: 10, backgroundColor: 'green', marginRight: 5 }
						:
						{ borderRadius: '100%', width: 10, height: 10, backgroundColor: 'inherit', marginRight: 5 }}>
					</div>

					{selectedJsonData?.id === record.id ?
						<Input
							style={{ width: 80 }}
							value={text}
							onClick={e => e.stopPropagation()}
							onChange={(e) => handleChangeNumber(e, record.id)}
						/>
						: text}

				</span>
			}
		},
		{
			title: 'Значение',
			dataIndex: 'textValue',
			key: 'textValue',
			width: fromModel ? '20%' : '70%',
			render: (text: string, record: any) => {

				return (selectedJsonData?.id === record.id && record.type !== 'NO_EXPRESSION' ?

					<Input
						value={text}
						style={{ width: 120 }}
						onClick={e => e.stopPropagation()}
						onChange={(e) => handleChange(e, record.id)}
					>

					</Input>

					: <span>{text}</span>)
			}
		},
		{
			title: 'В инциденты',
			dataIndex: 'toIncidents',
			key: 'toIncidents',
			width: '20%',
			hidden: !fromModel,
			render: (text: boolean, record: any) => {
				return (selectedJsonData?.id === record.id && fromModel ?
					<div onClick={e => e.stopPropagation()}>
						<Switch
							checkedChildren={<CheckOutlined />}
							unCheckedChildren={<CloseOutlined />}
							style={{ backgroundColor: '#d9d9d9' }}
							checked={text}
							onChange={(value: boolean) => onChange(value, record.id)} />
					</div>
					:
					<span>{text === true ? "Да" : "Нет"}</span>)
			}
		},
		{
			title: 'Коннектор отправки',
			dataIndex: 'connectorOutputVersionId',
			key: 'connectorOutputVersionId',
			width: '40%',
			hidden: !fromModel,
			render: (text: number, record: any) => {
				return (selectedJsonData?.id === record.id && fromModel ?
					<Select
						value={modelComponents ? modelComponents.filter(con => con?.type === 'CONNECTOR_OUTPUT').find(con => con?.modelComponentId === text)?.name : null}
						style={{ width: '90%' }}
						onClick={e => e.stopPropagation()}
						onChange={(value: string | number | undefined) => handleCon(value as number, record.id)}
						options={outputConList}
						allowClear
					/>
					:
					<span>{modelComponents ? modelComponents.filter(con => con?.type === 'CONNECTOR_OUTPUT').find(con => con?.modelComponentId === text)?.name : null}</span>)
			}
		},

	];


	useEffect(() => {
		if (rule?.summarySubType === 'QUALITY' && fromModel) {
			modelsController.getModel(String(fromModel))
		}
	}, [fromModel])

	useEffect(() => {
		if (rule?.summarySubType === 'QUALITY') {
			const qualityRule = rule?.jsonData as QualityRule[];
			setDataSource(qualityRule ? qualityRule : []);
		}
	}, [rule, fromModel]);


	const addRow = () => {

		const last = dataSource && dataSource?.length > 0 ? Number(dataSource?.length) + 1 : 1

		dispatch({
			type: ActionTypes.RULE_ADD_JSON_DATA,
			payload: {
				id: last,
				sendingId: -1,
				number: last,
				textValue: "HIGH",
				toIncidents: true,
				// con_out: `con${last}`,
				condition: `A${last}+B${last}`
			}
		})
	}

	const removeRow = () => {
		dispatch({
			type: ActionTypes.RULE_REMOVE_JSON_DATA,
			payload: selectedRowKeys[0]
		})
	}

	const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
		console.log('selectedRowKeys changed: ', newSelectedRowKeys);
		setSelectedRowKeys(newSelectedRowKeys);
	};

	const rowSelection = {
		selectedRowKeys,
		onChange: onSelectChange,
	};

	return (
		<Flex vertical gap='middle' justify='flex-end'>
			<Flex style={{ width: '100%' }} justify='flex-end' gap='small' >
				<Tooltip title="Add a field">
					<Button type="primary" icon={<PlusOutlined />} onClick={addRow} />
				</Tooltip>

				<Tooltip title="Remove a field">
					<Button type="primary" icon={<MinusOutlined />} onClick={removeRow} />
				</Tooltip>
			</Flex>


			<Table
				columns={columns}
				// scroll={{ y: 120 }}
				// style={{ minHeight: 215 }}
				dataSource={dataSource as QualityRule[]}
				rowKey={(row) => row.id}
				onRow={(row) => ({
					onClick: () => {
						dispatch({
							type: ActionTypes.RULE_SELECT_JSON_DATA,
							payload: row
						})

						onSelectChange([row.id] as React.Key[])
					}
				})}
				// rowSelection={{
				// 	type: 'radio',
				// 	...rowSelection
				// }

				size='small'
			/>
		</Flex>
	)
}

export default RulesQualityGrid