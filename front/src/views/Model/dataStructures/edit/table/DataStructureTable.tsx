import { Flex, Input, InputNumber, Select, Switch, Table, Typography } from 'antd'
import React, { useState } from 'react'
import { useAppSelector } from '../../../../../redux/Store'
import ControlPanel from './ControlPanel'
import { useDispatch } from 'react-redux'
import { ActionTypes } from '../../../../../redux/Model/DataStructureReducer'
import styles from './dataStructureTable.module.scss'

function DataStructureTable() {

	const dispatch = useDispatch()
	const fields = useAppSelector(store => store.DataStructureReducer.dataStructure?.fields)
	const selectedField = useAppSelector(store => store.DataStructureReducer.selectedField)
	const [isEditMode, setEditMode] = useState(false)

	const activateEditMode = () => {
		setEditMode(true)
	}

	const columns = [{
		title: 'Название',
		dataIndex: 'name',
		width: '30%',
		render: (text: string, record: any) => isEditMode && record.id === selectedField?.id ?
			<Input
				defaultValue={text}
				onClick={e => e.stopPropagation()}
				style={{ padding: '0 5px' }}
				onChange={e => {
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_EDIT_ROW,
						payload: {
							id: record.id,
							key: 'name',
							value: e.target.value
						}
					})
				}}
			/> :
			<span
				onClick={(e) => {
					e.stopPropagation()
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW,
						payload: record
					})
					activateEditMode()
				}}
				style={{ cursor: 'pointer' }}
			>{text}</span>,
	},
	{
		title: 'Тип поля',
		dataIndex: 'fieldType',
		width: '20%',
		render: (text: string, record: any) => isEditMode && record.id === selectedField?.id ?
			<Select
				defaultValue={text}
				onClick={e => e.stopPropagation()}
				style={{ padding: '0 5px' }}
				options={[
					{ value: 'STRING', label: 'STRING' },
					{ value: 'INTEGER', label: 'INTEGER' },
					{ value: 'LONG', label: 'LONG' },
					{ value: 'DOUBLE', label: 'DOUBLE' },
				]}

				onChange={(value: string) => {
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_EDIT_ROW,
						payload: {
							id: record.id,
							key: 'fieldType',
							value
						}
					})
				}}
			/> :
			<span
				onClick={(e) => {
					e.stopPropagation()
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW,
						payload: record
					})
					activateEditMode()
				}}
				style={{ cursor: 'pointer' }}
			>{text}</span>,
	},
	{
		title: 'Дефолтное',
		dataIndex: 'defaultField',
		width: '15%',
		render: (text: boolean, record: any) => isEditMode && record.id === selectedField?.id ?


			<div onClick={e => e.stopPropagation()}>
				<Switch
					defaultValue={text}

					style={{ padding: '0 5px' }}
					onChange={(value) => {
						dispatch({
							type: ActionTypes.DATA_STRUCTURE_FIELDS_EDIT_ROW,
							payload: {
								id: record.id,
								key: 'defaultField',
								value: value
							}
						})
					}}
				/>
			</div>
			:
			<span
				onClick={(e) => {
					e.stopPropagation()
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW,
						payload: record
					})
					activateEditMode()
				}}
				style={{ cursor: 'pointer' }}
			>{text === false ? 'Нет' : 'Да'}</span>,
	},
	{
		title: 'Количество',
		dataIndex: 'maxArray',
		width: '10%',
		render: (text: string, record: any) => isEditMode && record.id === selectedField?.id ?
			<InputNumber
				defaultValue={text}
				onClick={e => e.stopPropagation()}
				style={{ padding: '0 5px' }}
				onChange={(value) => {
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_EDIT_ROW,
						payload: {
							id: record.id,
							key: 'maxArray',
							value: value
						}
					})
				}}
			/> :
			<span
				onClick={(e) => {
					e.stopPropagation()
					dispatch({
						type: ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW,
						payload: record
					})
					activateEditMode()
				}}
				style={{ cursor: 'pointer' }}
			>{text}</span>,
	},
	{
		title: 'Тестовое',
		dataIndex: 'testValueJson',
		width: '10%',
	},
	{
		title: 'Связь',
		dataIndex: 'srcRelationId',
		width: '15%',
	},
	]

	return (
		<Flex gap='middle' style={{ marginTop: 20 }}>
			<Typography.Title level={5} style={{ width: '25%' }}>Структура данных</Typography.Title>

			<Flex vertical style={{ width: '75%' }} align='flex-end' gap='middle'>
				<ControlPanel activateEditMode={activateEditMode} />
				<Table
					columns={columns}
					dataSource={fields}
					size='small'
					rowKey={record => record.id}
					style={{ width: '100%' }}
					onRow={(row) => ({
						onClick: () => {
							setEditMode(false)
							dispatch({
								type: ActionTypes.DATA_STRUCTURE_FIELDS_SELECT_ROW,
								payload: row
							})
						}
					})}
					rowClassName={(row) => (row.id === selectedField?.id ? styles.picked : styles.default)}
				/>
			</Flex>

		</Flex>
	)
}

export default DataStructureTable